package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.TransactionEntity
import com.example.data.local.UserAccountEntity
import com.example.ui.ScreenDestination
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    user: UserAccountEntity?,
    transactions: List<TransactionEntity>,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CasinoBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Promotional Banner
        item {
            HeroBannerCard(onDepositClick = { onNavigate(ScreenDestination.DEPOSIT) })
        }

        // Live Security & Fast Payout Status Bar
        item {
            SecurityStatusBar(onSecurityClick = { onNavigate(ScreenDestination.SECURITY) })
        }

        // Quick Feature Access Bar
        item {
            QuickActionsBar(onNavigate = onNavigate)
        }

        // Featured Games Grid / Section
        item {
            Text(
                text = "🔥 জনপ্রিয় ক্যাসিনো গেমস (HOT GAMES)",
                color = CasinoGoldLight,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Game 1: Fortune 777 Slots
                GameLobbyCard(
                    title = "Fortune 777 Slots",
                    subtitle = "ক্লাসিক ৩-রিল স্লট মেশিন • জ্যাকপট ৫০x • RTP 97.2%",
                    badge = "MEGA JACKPOT",
                    badgeColor = CasinoGold,
                    iconEmoji = "🎰",
                    buttonText = "এখনই খেলুন (PLAY)",
                    onClick = { onNavigate(ScreenDestination.SLOTS) }
                )

                // Game 2: Rocket Crash X
                GameLobbyCard(
                    title = "Rocket Crash X",
                    subtitle = "লাইভ মাল্টিপ্লায়ার রকেট ১.০০x - ৫০x+ • ক্র্যাশের আগেই Cash Out করুন!",
                    badge = "TRENDING 🚀",
                    badgeColor = CasinoRed,
                    iconEmoji = "🚀",
                    buttonText = "বাজি ধরুন (CRASH)",
                    onClick = { onNavigate(ScreenDestination.CRASH) }
                )

                // Game 3: Lucky Fortune Wheel
                GameLobbyCard(
                    title = "Lucky Fortune Wheel",
                    subtitle = "লাকি হুইল স্পিন করুন • ২x, ৫x, ১০x, ৫০x তাত্ক্ষণিক রিওয়ার্ড",
                    badge = "INSTANT WIN",
                    badgeColor = CasinoNeonViolet,
                    iconEmoji = "🎡",
                    buttonText = "স্পিন করুন (SPIN)",
                    onClick = { onNavigate(ScreenDestination.WHEEL) }
                )

                // Game 4: Dragon Tiger Table
                GameLobbyCard(
                    title = "Dragon Tiger Live Table",
                    subtitle = "হাই-স্পিড কার্ড ডুয়েলে ড্রাগন বা টাইগারের পক্ষে বাজি ধরুন • ২x পেআউট",
                    badge = "LIVE TABLE",
                    badgeColor = CasinoGreen,
                    iconEmoji = "🃏",
                    buttonText = "টেবিলে বসুন (TABLE)",
                    onClick = { onNavigate(ScreenDestination.TABLE) }
                )
            }
        }

        // Live Winners / Payouts Ticker
        item {
            Spacer(modifier = Modifier.height(18.dp))
            LiveWinnersTicker()
        }

        // Recent Activity / Transactions Section
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📋 সাম্প্রতিক লেনদেন ও পেআউট",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "সব দেখুন",
                    color = CasinoGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigate(ScreenDestination.WITHDRAWAL) }
                )
            }
        }

        items(transactions.take(5)) { tx ->
            TransactionItemRow(tx = tx)
        }
    }
}

@Composable
fun HeroBannerCard(onDepositClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.casino_hero_banner),
                contentDescription = "Casino Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark & Gold Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                CasinoBackground.copy(alpha = 0.95f),
                                CasinoBackground.copy(alpha = 0.7f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Text & Action
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = CasinoGold,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "100% WELCOME BONUS + VIP REBATE",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "F999 প্রিমিয়াম ক্যাসিনো",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
                Text(
                    text = "বিকাশ • নগদ • রকেটে ৩০ সেকেন্ডে ফাস্ট অটোমেটেড পেআউট",
                    color = CasinoGoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onDepositClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "তাত্ক্ষণিক ডিপোজিট করুন",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SecurityStatusBar(onSecurityClick: () -> Unit) {
    Surface(
        color = CasinoPurpleSurface,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CasinoNeonViolet.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onSecurityClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(CasinoGreen.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Security Verified",
                        tint = CasinoGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "AES-256 ডেটা এনক্রিপশন ও সিকিউরিটি অডিট",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "স্বয়ংক্রিয় ৩০ সেকেন্ড উইথড্রল গেটওয়ে সক্রিয়",
                        color = CasinoGreen,
                        fontSize = 11.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun QuickActionsBar(onNavigate: (ScreenDestination) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickActionButton(
            title = "ডিপোজিট",
            subtitle = "bKash / Nagad",
            icon = Icons.Default.AddCard,
            accentColor = CasinoGold,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(ScreenDestination.DEPOSIT) }
        )
        QuickActionButton(
            title = "ফাস্ট উইথড্রল",
            subtitle = "৩০-৬০ সেকেন্ড",
            icon = Icons.Default.Bolt,
            accentColor = CasinoGreen,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(ScreenDestination.WITHDRAWAL) }
        )
        QuickActionButton(
            title = "সিকিউরিটি",
            subtitle = "অডিট ও হ্যাশ",
            icon = Icons.Default.Shield,
            accentColor = CasinoCyan,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(ScreenDestination.SECURITY) }
        )
        QuickActionButton(
            title = "AI চ্যাটবট",
            subtitle = "২৪/৭ সাপোর্ট",
            icon = Icons.Default.Chat,
            accentColor = CasinoNeonViolet,
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(ScreenDestination.CHATBOT) }
        )
    }
}

@Composable
fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = CasinoPurpleCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.25f)),
        modifier = modifier
            .height(76.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
fun GameLobbyCard(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    iconEmoji: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CasinoPurpleCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CasinoPurpleSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Emoji Avatar
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CasinoPurpleDark),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = badgeColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = badge,
                            color = badgeColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Play Button
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text(
                    text = "খেলুন",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun LiveWinnersTicker() {
    val winners = listOf(
        "🏆 Player***82 won ৳14,200 on Fortune Slots",
        "⚡ User***19 received ৳25,000 bKash payout in 31s",
        "🚀 Gamer***91 cashed out 14.8x on Crash X (৳7,400)",
        "👑 VIP***55 won ৳38,000 on Dragon Tiger"
    )

    Surface(
        color = CasinoPurpleDark,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGoldDark.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CasinoGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "লাইভ উইনার্স ও অটোমেটেড পেআউট স্ট্রিম",
                    color = CasinoGoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(winners) { item ->
                    Surface(
                        color = CasinoPurpleCard,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemRow(tx: TransactionEntity) {
    Surface(
        color = CasinoPurpleCard,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = when (tx.type) {
                    "DEPOSIT" -> Icons.Default.ArrowDownward
                    "WITHDRAWAL" -> Icons.Default.ArrowUpward
                    "BET_WIN" -> Icons.Default.EmojiEvents
                    else -> Icons.Default.Casino
                }
                val tint = when (tx.type) {
                    "DEPOSIT", "BET_WIN" -> CasinoGreen
                    "WITHDRAWAL" -> CasinoGold
                    else -> CasinoRed
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(tint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = tx.type,
                        tint = tint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = when (tx.type) {
                            "DEPOSIT" -> "ডিপোজিট (${tx.method})"
                            "WITHDRAWAL" -> "উইথড্রল (${tx.method})"
                            "BET_WIN" -> "গেম জয় (${tx.method})"
                            "BET_LOSS" -> "বাজি খেলা (${tx.method})"
                            else -> tx.type
                        },
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = tx.statusDetails.ifBlank { "TrxID: ${tx.trxId}" },
                        color = TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (tx.type == "DEPOSIT" || tx.type == "BET_WIN") "+" else "-"}৳${"%,.2f".format(tx.amount)}",
                    color = if (tx.type == "DEPOSIT" || tx.type == "BET_WIN") CasinoGreen else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = tx.status,
                    color = when (tx.status) {
                        "COMPLETED" -> CasinoGreen
                        "PROCESSING" -> CasinoGold
                        else -> CasinoRed
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
