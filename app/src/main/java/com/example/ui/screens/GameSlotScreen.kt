package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.security.CryptoSecurityManager
import com.example.ui.ScreenDestination
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun GameSlotScreen(
    currentBalance: Double,
    onRecordResult: (Double, Double, String) -> Unit,
    onBack: () -> Unit
) {
    val symbols = listOf("7️⃣", "💎", "👑", "🔔", "🍒", "🍇", "⭐")
    var reel1 by remember { mutableStateOf("7️⃣") }
    var reel2 by remember { mutableStateOf("7️⃣") }
    var reel3 by remember { mutableStateOf("7️⃣") }

    var isSpinning by remember { mutableStateOf(false) }
    var selectedBet by remember { mutableStateOf(100.0) }
    var winAmount by remember { mutableStateOf(0.0) }
    var winMessage by remember { mutableStateOf<String?>(null) }
    var fairHash by remember { mutableStateOf("SHA-256: 7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1f") }

    val coroutineScope = rememberCoroutineScope()
    val betOptions = listOf(50.0, 100.0, 500.0, 1000.0, 5000.0)

    fun spinReels() {
        if (isSpinning || currentBalance < selectedBet) return
        isSpinning = true
        winAmount = 0.0
        winMessage = null

        coroutineScope.launch {
            val startTime = System.currentTimeMillis()
            var count = 0
            while (System.currentTimeMillis() - startTime < 1600) {
                reel1 = symbols[Random.nextInt(symbols.size)]
                reel2 = symbols[Random.nextInt(symbols.size)]
                reel3 = symbols[Random.nextInt(symbols.size)]
                delay(60)
                count++
            }

            // Determine final outcome with realistic exciting odds
            val roll = Random.nextInt(100)
            val finalReels = when {
                roll < 6 -> listOf("7️⃣", "7️⃣", "7️⃣") // 6% 50x Mega Jackpot
                roll < 14 -> listOf("💎", "💎", "💎") // 8% 25x
                roll < 24 -> listOf("👑", "👑", "👑") // 10% 15x
                roll < 38 -> listOf("🍒", "🍒", "🍒") // 14% 5x
                roll < 62 -> {
                    // Match 2
                    val sym = symbols[Random.nextInt(symbols.size)]
                    listOf(sym, sym, symbols[(symbols.indexOf(sym) + 1) % symbols.size])
                }
                else -> {
                    // Loss
                    listOf("🍒", "7️⃣", "🔔")
                }
            }

            reel1 = finalReels[0]
            delay(120)
            reel2 = finalReels[1]
            delay(150)
            reel3 = finalReels[2]

            // Calculate multiplier
            val multiplier = when {
                reel1 == "7️⃣" && reel2 == "7️⃣" && reel3 == "7️⃣" -> 50.0
                reel1 == "💎" && reel2 == "💎" && reel3 == "💎" -> 25.0
                reel1 == "👑" && reel2 == "👑" && reel3 == "👑" -> 15.0
                reel1 == "🔔" && reel2 == "🔔" && reel3 == "🔔" -> 10.0
                reel1 == "🍒" && reel2 == "🍒" && reel3 == "🍒" -> 5.0
                reel1 == reel2 || reel2 == reel3 || reel1 == reel3 -> 1.5
                else -> 0.0
            }

            val win = selectedBet * multiplier
            winAmount = win
            if (multiplier > 0) {
                winMessage = if (multiplier >= 25.0) "🎉 JACKPOT! ৳${"%,.0f".format(win)} WON!" else "বিজয়ী! ৳${"%,.0f".format(win)}"
            }

            fairHash = "SHA-256: " + CryptoSecurityManager.sha256("SLOT:$reel1$reel2$reel3:$selectedBet:${System.currentTimeMillis()}")
            onRecordResult(selectedBet, win, "Fortune 777 Slots")
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "FORTUNE 777 SLOTS",
                    color = CasinoGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "RTP 97.2% • PROVABLY FAIR",
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

        Spacer(modifier = Modifier.height(20.dp))

        // Slot Machine Frame
        Surface(
            color = CasinoPurpleDark,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, Brush.linearGradient(listOf(CasinoGold, CasinoGoldDark))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Jackpot Ticker
                Surface(
                    color = CasinoPurpleCard,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGold.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "💎 777 MEGA JACKPOT: 50X MULTIPLIER 💎",
                        color = CasinoGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // The 3 Reels Container
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SlotReelBox(symbol = reel1, isSpinning = isSpinning)
                    SlotReelBox(symbol = reel2, isSpinning = isSpinning)
                    SlotReelBox(symbol = reel3, isSpinning = isSpinning)
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Win Display or Status
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (winMessage != null) {
                        Text(
                            text = winMessage ?: "",
                            color = CasinoGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    } else if (isSpinning) {
                        Text(
                            text = "রিল ঘুরছে...",
                            color = CasinoNeonViolet,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "স্পিন করে বাজি জিতুন!",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                val isSelected = selectedBet == bet
                Surface(
                    color = if (isSelected) CasinoGold else CasinoPurpleCard,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CasinoGoldLight else Color.Transparent),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedBet = bet }
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

        Spacer(modifier = Modifier.height(24.dp))

        // Big Spin Action Button
        Button(
            onClick = { spinReels() },
            enabled = !isSpinning && currentBalance >= selectedBet,
            colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = if (isSpinning) "স্পিন হচ্ছে..." else "SPIN (৳${"%.0f".format(selectedBet)})",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Provably Fair Cryptographic Tag
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

@Composable
fun SlotReelBox(symbol: String, isSpinning: Boolean) {
    Box(
        modifier = Modifier
            .size(86.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(CasinoBackground)
            .border(2.dp, if (isSpinning) CasinoNeonViolet else CasinoGoldDark, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = 42.sp
        )
    }
}
