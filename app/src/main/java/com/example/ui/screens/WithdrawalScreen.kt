package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.*

@Composable
fun WithdrawalScreen(
    user: UserAccountEntity?,
    withdrawals: List<TransactionEntity>,
    onWithdrawalSubmit: (Double, String, String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    val methods = listOf("bKash (বিকাশ)", "Nagad (নগদ)", "Rocket (রকেট)", "USDT (TRC20)", "Bank Transfer")
    var selectedMethod by remember { mutableStateOf(methods[0]) }
    var amountText by remember { mutableStateOf("1500") }
    var recipientNumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var isAutomatedEnabled by remember { mutableStateOf(true) }

    val quickAmounts = listOf("500", "1000", "2000", "5000", "15000", "25000")
    val balance = user?.balance ?: 0.0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "দ্রুত উইথড্রল (FAST PAYOUT)",
                        color = CasinoGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "উন্নত অটোমেটেড সিস্টেম • মাত্র ৩০-৬০ সেকেন্ডে টাকা পান",
                        color = CasinoGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Wallet Balance & Daily Auto Limit Card
        item {
            Surface(
                color = CasinoPurpleDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGoldDark.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "উত্তোলনযোগ্য ব্যালেন্স:", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "৳${"%,.2f".format(balance)}",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                    Surface(
                        color = CasinoGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Text(text = "অটো-উইথড্রল লিমিট", color = CasinoGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "৳${"%,.0f".format(user?.autoWithdrawalLimit ?: 50000.0)}/দিন", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Automated System Highlight Box
        item {
            Surface(
                color = CasinoPurpleCard,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGreen.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Bolt, contentDescription = "Automated", tint = CasinoGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "উন্নত অটোমেটেড ফাস্ট পেআউট সিস্টেম",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "AI রিস্ক ইঞ্জিন যাচাই শেষে সরাসরি MFS API এর মাধ্যমে ৩০-৬০ সেকেন্ডে টাকা ট্রান্সফার হবে।",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Switch(
                        checked = isAutomatedEnabled,
                        onCheckedChange = { isAutomatedEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = CasinoGreen,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = CasinoPurpleSurface
                        )
                    )
                }
            }
        }

        // Method Selector
        item {
            Text(text = "উইথড্রল মাধ্যম নির্বাচন করুন:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(methods) { method ->
                    val isSelected = method == selectedMethod
                    Surface(
                        color = if (isSelected) CasinoGold else CasinoPurpleCard,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { selectedMethod = method }
                    ) {
                        Text(
                            text = method,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Amount Input & Quick Chips
        item {
            Text(text = "উইথড্রল পরিমাণ (BDT):", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("৳ ", color = CasinoGold, fontWeight = FontWeight.Bold) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoPurpleSurface,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(quickAmounts) { amt ->
                    Surface(
                        color = if (amountText == amt) CasinoGold else CasinoPurpleCard,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { amountText = amt }
                    ) {
                        Text(
                            text = "৳$amt",
                            color = if (amountText == amt) Color.Black else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Recipient Number
        item {
            Text(text = "আপনার $selectedMethod রিসিভার নাম্বার:", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = recipientNumber,
                onValueChange = { recipientNumber = it },
                placeholder = { Text("01XXXXXXXXX", color = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoPurpleSurface,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Submit Button
        item {
            val amount = amountText.toDoubleOrNull() ?: 0.0
            val canSubmit = amount >= 500.0 && amount <= balance && recipientNumber.isNotBlank()

            Button(
                onClick = {
                    onWithdrawalSubmit(amount, selectedMethod, recipientNumber, isAutomatedEnabled)
                },
                enabled = canSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = if (isAutomatedEnabled) "অটোমেটেড দ্রুত উইথড্রল করুন (৳${"%,.0f".format(amount)})" else "উইথড্রল রিকোয়েস্ট দিন",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
        }

        // Live Automated Withdrawal Tracking Timeline
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "⚡ লাইভ উইথড্রল ট্র্যাকিং ও হিস্ট্রি:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        if (withdrawals.isEmpty()) {
            item {
                Surface(
                    color = CasinoPurpleDark,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "এখনও কোনো উইথড্রল রিকোয়েস্ট নেই।",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(withdrawals) { tx ->
                WithdrawalTrackingCard(tx = tx)
            }
        }
    }
}

@Composable
fun WithdrawalTrackingCard(tx: TransactionEntity) {
    Surface(
        color = CasinoPurpleCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when (tx.status) {
                "COMPLETED" -> CasinoGreen.copy(alpha = 0.5f)
                "PROCESSING" -> CasinoGold.copy(alpha = 0.5f)
                else -> CasinoPurpleSurface
            }
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (tx.status == "COMPLETED") Icons.Default.CheckCircle else Icons.Default.Sync,
                        contentDescription = tx.status,
                        tint = if (tx.status == "COMPLETED") CasinoGreen else CasinoGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${tx.method} • ৳${"%,.0f".format(tx.amount)}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Surface(
                    color = when (tx.status) {
                        "COMPLETED" -> CasinoGreen.copy(alpha = 0.2f)
                        "PROCESSING" -> CasinoGold.copy(alpha = 0.2f)
                        else -> CasinoRed.copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = tx.status,
                        color = when (tx.status) {
                            "COMPLETED" -> CasinoGreen
                            "PROCESSING" -> CasinoGold
                            else -> CasinoRed
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "রিসিভার: ${tx.accountNumber} | TrxID: ${tx.trxId}",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tx.statusDetails.ifBlank { "Automated Fast Pipeline dispatched." },
                color = if (tx.status == "COMPLETED") CasinoGreen else CasinoGoldLight,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
