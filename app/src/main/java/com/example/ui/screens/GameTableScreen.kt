package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class PlayingCard(val suit: String, val rankName: String, val value: Int)

@Composable
fun GameTableScreen(
    currentBalance: Double,
    onRecordResult: (Double, Double, String) -> Unit,
    onBack: () -> Unit
) {
    val cardDeck = remember {
        val suits = listOf("♠", "♥", "♦", "♣")
        val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val list = mutableListOf<PlayingCard>()
        for (s in suits) {
            for ((idx, r) in ranks.withIndex()) {
                list.add(PlayingCard(s, r, idx + 1))
            }
        }
        list
    }

    var selectedChoice by remember { mutableStateOf("DRAGON") } // DRAGON, TIE, TIGER
    var betAmount by remember { mutableStateOf(200.0) }
    var dragonCard by remember { mutableStateOf<PlayingCard?>(PlayingCard("♠", "K", 13)) }
    var tigerCard by remember { mutableStateOf<PlayingCard?>(PlayingCard("♥", "9", 9)) }
    var isDealing by remember { mutableStateOf(false) }
    var gameOutcome by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val betOptions = listOf(100.0, 200.0, 500.0, 1000.0, 2500.0)

    fun dealCards() {
        if (isDealing || currentBalance < betAmount) return
        isDealing = true
        gameOutcome = null

        coroutineScope.launch {
            delay(500)
            val dCard = cardDeck[Random.nextInt(cardDeck.size)]
            val tCard = cardDeck[Random.nextInt(cardDeck.size)]
            dragonCard = dCard
            tigerCard = tCard

            val winningSide = when {
                dCard.value > tCard.value -> "DRAGON"
                tCard.value > dCard.value -> "TIGER"
                else -> "TIE"
            }

            val win = when {
                selectedChoice == winningSide && winningSide == "TIE" -> betAmount * 9.0
                selectedChoice == winningSide -> betAmount * 2.0
                else -> 0.0
            }

            gameOutcome = if (win > 0) "🎉 $winningSide জয়ী! আপনি ৳${"%,.0f".format(win)} পেয়েছেন" else "$winningSide জয়ী হয়েছে।"
            onRecordResult(betAmount, win, "Dragon Tiger Table")
            isDealing = false
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
                text = "DRAGON TIGER TABLE",
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

        Spacer(modifier = Modifier.height(16.dp))

        // Table Arena
        Surface(
            color = CasinoPurpleDark,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGoldDark.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Dragon Card
                    CardBox(
                        title = "DRAGON",
                        titleColor = CasinoRed,
                        card = dragonCard,
                        isSelected = selectedChoice == "DRAGON"
                    )

                    // VS
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 40.dp)
                    ) {
                        Text(text = "VS", color = CasinoGold, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }

                    // Tiger Card
                    CardBox(
                        title = "TIGER",
                        titleColor = CasinoCyan,
                        card = tigerCard,
                        isSelected = selectedChoice == "TIGER"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (gameOutcome != null) {
                        Text(
                            text = gameOutcome ?: "",
                            color = CasinoGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Betting Options (DRAGON 2x, TIE 9x, TIGER 2x)
        Text(text = "বাজির পক্ষ নির্বাচন করুন:", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SideBetButton(
                title = "DRAGON",
                multiplier = "2.0x",
                color = CasinoRed,
                isSelected = selectedChoice == "DRAGON",
                modifier = Modifier.weight(1f),
                onClick = { selectedChoice = "DRAGON" }
            )
            SideBetButton(
                title = "TIE",
                multiplier = "9.0x",
                color = CasinoGreen,
                isSelected = selectedChoice == "TIE",
                modifier = Modifier.weight(0.9f),
                onClick = { selectedChoice = "TIE" }
            )
            SideBetButton(
                title = "TIGER",
                multiplier = "2.0x",
                color = CasinoCyan,
                isSelected = selectedChoice == "TIGER",
                modifier = Modifier.weight(1f),
                onClick = { selectedChoice = "TIGER" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bet Amount
        Text(text = "বাজির পরিমাণ:", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
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
                        .clickable(enabled = !isDealing) { betAmount = bet }
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

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = { dealCards() },
            enabled = !isDealing && currentBalance >= betAmount,
            colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = if (isDealing) "কার্ড ডিল হচ্ছে..." else "DEAL ($selectedChoice ৳${"%.0f".format(betAmount)})",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CardBox(title: String, titleColor: Color, card: PlayingCard?, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = titleColor, fontWeight = FontWeight.Black, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(88.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(2.dp, if (isSelected) CasinoGold else Color.Transparent, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (card != null) {
                val isRed = card.suit == "♥" || card.suit == "♦"
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = card.rankName, fontSize = 28.sp, fontWeight = FontWeight.Black, color = if (isRed) Color.Red else Color.Black)
                    Text(text = card.suit, fontSize = 24.sp, color = if (isRed) Color.Red else Color.Black)
                }
            }
        }
    }
}

@Composable
fun SideBetButton(
    title: String,
    multiplier: String,
    color: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) color.copy(alpha = 0.25f) else CasinoPurpleCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSelected) color else Color.Transparent),
        modifier = modifier
            .height(62.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = if (isSelected) color else Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp)
            Text(text = multiplier, color = CasinoGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
