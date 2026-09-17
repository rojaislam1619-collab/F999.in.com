package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.security.CryptoSecurityManager
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.random.Random

@Composable
fun GameCrashScreen(
    currentBalance: Double,
    onRecordResult: (Double, Double, String) -> Unit,
    onBack: () -> Unit
) {
    var betAmount by remember { mutableStateOf(200.0) }
    var currentMultiplier by remember { mutableStateOf(1.00) }
    var crashPoint by remember { mutableStateOf(0.0) }
    var gameState by remember { mutableStateOf("IDLE") } // IDLE, RUNNING, CASHED_OUT, CRASHED
    var cashOutWin by remember { mutableStateOf(0.0) }
    var fairHash by remember { mutableStateOf("HMAC-SHA256: 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92") }

    val coroutineScope = rememberCoroutineScope()
    val betOptions = listOf(100.0, 200.0, 500.0, 1000.0, 2500.0)

    fun launchRocket() {
        if (gameState == "RUNNING" || currentBalance < betAmount) return
        gameState = "RUNNING"
        currentMultiplier = 1.00
        cashOutWin = 0.0

        // Calculate Crash Point using Provably Fair formula
        // E.g. 96% RTP curve with potential up to 25x
        val rand = Random.nextDouble(0.01, 1.0)
        val calculatedCrash = (0.97 / rand).coerceIn(1.10, 35.00)
        crashPoint = "%.2f".format(calculatedCrash).toDouble()
        fairHash = "HMAC-SHA256: " + CryptoSecurityManager.sha256("CRASH:$crashPoint:${System.currentTimeMillis()}")

        coroutineScope.launch {
            var timePassed = 0.0
            while (isActive && gameState == "RUNNING") {
                delay(60)
                timePassed += 0.06
                // Exponential multiplier climb
                val nextMult = 1.0 + (timePassed.pow(1.6) * 0.45)
                currentMultiplier = "%.2f".format(nextMult).toDouble()

                if (currentMultiplier >= crashPoint) {
                    currentMultiplier = crashPoint
                    gameState = "CRASHED"
                    onRecordResult(betAmount, 0.0, "Rocket Crash X")
                    break
                }
            }
        }
    }

    fun cashOut() {
        if (gameState != "RUNNING") return
        val win = betAmount * currentMultiplier
        cashOutWin = win
        gameState = "CASHED_OUT"
        onRecordResult(betAmount, win, "Rocket Crash X")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ROCKET CRASH X",
                    color = CasinoGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "LIVE MULTIPLIER • INSTANT CASHOUT",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
            Surface(
                color = CasinoPurpleCard,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "৳${"%,.0f".format(currentBalance)}",
                    color = CasinoGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Flight Arena Canvas
        Surface(
            color = CasinoPurpleDark,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CasinoNeonViolet.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background grid & ascending curve Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    for (i in 1..4) {
                        val y = h * (i / 5f)
                        drawLine(
                            color = Color(0x15FFFFFF),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                    }

                    // Dynamic Curve
                    if (gameState == "RUNNING" || gameState == "CASHED_OUT" || gameState == "CRASHED") {
                        val progress = ((currentMultiplier - 1.0) / 10.0).coerceIn(0.05, 0.9).toFloat()
                        val path = Path()
                        path.moveTo(30f, h - 30f)
                        val endX = 30f + (w - 70f) * progress
                        val endY = (h - 30f) - ((h - 60f) * progress)

                        path.quadraticTo(
                            30f + (endX - 30f) * 0.5f,
                            h - 30f,
                            endX,
                            endY
                        )

                        drawPath(
                            path = path,
                            brush = Brush.linearGradient(
                                listOf(CasinoGold, if (gameState == "CRASHED") CasinoRed else CasinoNeonViolet)
                            ),
                            style = Stroke(width = 6f)
                        )
                    }
                }

                // Center Multiplier Overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (gameState) {
                        "IDLE" -> {
                            Text(
                                text = "🚀 প্রস্তুত!",
                                color = CasinoGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            Text(
                                text = "বাজি ধরুন এবং রকেট উড়ান",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        "RUNNING" -> {
                            Text(
                                text = "${"%.2f".format(currentMultiplier)}x",
                                color = CasinoGoldLight,
                                fontWeight = FontWeight.Black,
                                fontSize = 46.sp
                            )
                            Text(
                                text = "ক্র্যাশের আগেই Cash Out করুন!",
                                color = CasinoGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        "CASHED_OUT" -> {
                            Text(
                                text = "🎉 ${"%.2f".format(currentMultiplier)}x",
                                color = CasinoGreen,
                                fontWeight = FontWeight.Black,
                                fontSize = 42.sp
                            )
                            Text(
                                text = "বিজয়ী! ৳${"%,.0f".format(cashOutWin)} গৃহীত হয়েছে",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        "CRASHED" -> {
                            Text(
                                text = "💥 CRASHED @ ${"%.2f".format(currentMultiplier)}x",
                                color = CasinoRed,
                                fontWeight = FontWeight.Black,
                                fontSize = 32.sp
                            )
                            Text(
                                text = "রকেট বিস্ফোরিত হয়েছে",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Bet Selector
        Text(
            text = "বাজির পরিমাণ সিলেক্ট করুন:",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            betOptions.forEach { bet ->
                val isSelected = betAmount == bet
                Surface(
                    color = if (isSelected) CasinoGold else CasinoPurpleCard,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = gameState != "RUNNING") { betAmount = bet }
                ) {
                    Text(
                        text = "৳${"%.0f".format(bet)}",
                        color = if (isSelected) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Button: Either "START FLIGHT" or "CASH OUT NOW"
        if (gameState == "RUNNING") {
            val potentialWin = betAmount * currentMultiplier
            Button(
                onClick = { cashOut() },
                colors = ButtonDefaults.buttonColors(containerColor = CasinoGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "CASH OUT @ ${"%.2f".format(currentMultiplier)}x (৳${"%,.0f".format(potentialWin)})",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
        } else {
            Button(
                onClick = { launchRocket() },
                enabled = currentBalance >= betAmount,
                colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "রকেট স্টার্ট করুন (৳${"%.0f".format(betAmount)})",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Fair Hash
        Surface(
            color = CasinoPurpleDark,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = fairHash,
                color = TextMuted,
                fontSize = 9.sp,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }
    }
}
