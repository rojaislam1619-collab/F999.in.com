package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserAccountEntity
import com.example.ui.ScreenDestination
import com.example.ui.theme.*

@Composable
fun CasinoTopAppBar(
    user: UserAccountEntity?,
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CasinoPurpleDark,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & VIP Tier
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onNavigate(ScreenDestination.HOME) }
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CasinoGold, CasinoGoldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "999",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "F999.COM",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = CasinoGoldLight,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = user?.vipTier ?: "VIP 2 Silver",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CasinoNeonViolet
                    )
                }
            }

            // Wallet Balance + Quick Actions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Balance Chip
                Surface(
                    color = CasinoPurpleCard,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGoldDark.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { onNavigate(ScreenDestination.DEPOSIT) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "৳",
                            color = CasinoGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%,.2f".format(user?.balance ?: 0.0),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(CasinoGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Deposit",
                                tint = Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Admin Button
                IconButton(
                    onClick = {
                        if (currentScreen == ScreenDestination.ADMIN) {
                            onNavigate(ScreenDestination.HOME)
                        } else {
                            onNavigate(ScreenDestination.ADMIN)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (currentScreen == ScreenDestination.ADMIN) CasinoGold else CasinoPurpleSurface,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Panel",
                        tint = if (currentScreen == ScreenDestination.ADMIN) Color.Black else CasinoGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CasinoBottomNavigationBar(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit
) {
    NavigationBar(
        containerColor = CasinoPurpleDark,
        tonalElevation = 10.dp,
        modifier = Modifier.height(68.dp)
    ) {
        val navItems = listOf(
            Triple(ScreenDestination.HOME, "লবি", Icons.Default.Home),
            Triple(ScreenDestination.DEPOSIT, "ডিপোজিট", Icons.Default.AccountBalanceWallet),
            Triple(ScreenDestination.WITHDRAWAL, "উইথড্রল", Icons.Default.ElectricBolt),
            Triple(ScreenDestination.SECURITY, "সিকিউরিটি", Icons.Default.Shield),
            Triple(ScreenDestination.CHATBOT, "সহায়তা", Icons.Default.SupportAgent)
        )

        navItems.forEach { (destination, title, icon) ->
            val isSelected = currentScreen == destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(destination) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = CasinoGold,
                    indicatorColor = CasinoGold,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
