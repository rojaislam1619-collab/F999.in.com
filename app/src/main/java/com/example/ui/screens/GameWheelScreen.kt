package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GameWheelScreen(
    currentBalance: Double,
    onRecordResult: (Double, Double, String) -> Unit,
    onBack: () -> Unit
) {
    val prizes = listOf("2x", "0x", "5x", "1.5x", "10x", "0x", "20x", "50x")
    val colors = listOf(
        CasinoGold, Color(0xFF334155), CasinoNeonViolet, Color(0xFF475569),
        CasinoGreen, Color(0xFF1E293B), CasinoCyan, CasinoRed
    )

    var betAmount by remember { mutableStateOf(100.0) }
    var isSpinning by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var winResult by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val betOptions = listOf(50.0, 100.0, 250.0, 500.0, 1000.0)

    fun spinWheel() {
        if (isSpinning || currentBalance < betAmount) return
        isSpinning = true
        winResult = null

        val extraSpins = 5 * 360f
        val selectedIndex = Random.nextInt(prizes.size)
        val sliceAngle = 360f / prizes.size
        val targetSliceAngle = selectedIndex * sliceAngle + (sliceAngle / 2f)
        val totalRotation = rotationAngle + extraSpins + (360f - (rotationAngle % 360f)) + targetSliceAngle

        coroutineScope.launch {
            val animatable = Animatable(rotationAngle)
            animatable.animateTo(
                targetValue = totalRotation,
                animationSpec = tween(durationMillis = 3200, easing = FastOutSlowInEasing)
            ) {
                rotationAngle = this.value
            }

            val prize = prizes[selectedIndex]
            val mult = when (prize) {
                "50x" -> 50.0
                "20x" -> 20.0
                "10x" -> 10.0
                "5x" -> 5.0
                "2x" -> 2.0
                "1.5x" -> 1.5
                else -> 0.0
            }

            val win = betAmount * mult
            winResult = if (mult > 0) "🎉 $prize মাল্টিপ্লায়ার! ৳${"%,.0f".format(win)} লাভ" else "এবার হলো না, আবার চেষ্টা করুন"
            onRecordResult(betAmount, win, "Lucky Fortune Wheel")
            isSpinning = false
        }
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
            Text(
                text = "LUCKY FORTUNE WHEEL",
                color = CasinoGold,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
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

        Spacer(modifier = Modifier.height(14.dp))

        // Wheel Canvas Area
        Box(
            modifier = Modifier
                .size(240.dp)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2
                val sliceAngle = 360f / prizes.size

                for (i in prizes.indices) {
                    val startAngle = (i * sliceAngle + rotationAngle) % 360f
                    drawArc(
                        color = colors[i],
                        startAngle = startAngle,
                        sweepAngle = sliceAngle,
                        useCenter = true,
                        size = Size(size.width, size.height),
                        topLeft = Offset(0f, 0f)
                    )
                }

                // Outer border
                drawCircle(
                    color = CasinoGold,
                    radius = radius,
                    style = Stroke(width = 6f)
                )
            }

            // Center Pin / Pointer
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(CasinoGold),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VIP",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }

        // Result Message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            contentAlignment = Alignment.Center
        ) {
            if (winResult != null) {
                Text(
                    text = winResult ?: "",
                    color = CasinoGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bet Selector
        Text(
            text = "বাজির পরিমাণ:",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(6.dp))
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
                        .clickable(enabled = !isSpinning) { betAmount = bet }
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

        Button(
            onClick = { spinWheel() },
            enabled = !isSpinning && currentBalance >= betAmount,
            colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = if (isSpinning) "চাকা ঘুরছে..." else "SPIN WHEEL (৳${"%.0f".format(betAmount)})",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
    }
}
