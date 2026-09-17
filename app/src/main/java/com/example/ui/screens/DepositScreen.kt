package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PaymentGatewayEntity
import com.example.ui.theme.*

@Composable
fun DepositScreen(
    gateways: List<PaymentGatewayEntity>,
    onDepositSubmit: (Double, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val activeGateways = gateways.ifEmpty {
        listOf(
            PaymentGatewayEntity("bkash", "bKash (বিকাশ)", "01788-999001", "Send Money to Agent number", 300.0, 25000.0, 500.0, 50000.0, 0.0, true),
            PaymentGatewayEntity("nagad", "Nagad (নগদ)", "01877-999002", "Cash In to Merchant number", 300.0, 50000.0, 500.0, 50000.0, 0.0, true),
            PaymentGatewayEntity("rocket", "Rocket (রকেট)", "01966-999003-8", "Send Money to Rocket Agent", 500.0, 30000.0, 500.0, 30000.0, 0.0, true),
            PaymentGatewayEntity("usdt", "USDT (TRC20)", "TF999CasinoTronVault777SecureAddress", "Send USDT TRC20 (1 USDT = ৳125)", 1250.0, 500000.0, 2500.0, 500000.0, 0.0, true)
        )
    }

    var selectedGateway by remember { mutableStateOf(activeGateways.firstOrNull() ?: activeGateways[0]) }
    var depositAmountText by remember { mutableStateOf("1000") }
    var senderNumber by remember { mutableStateOf("") }
    var trxId by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val quickAmounts = listOf("500", "1000", "2000", "5000", "10000", "25000")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
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
                        text = "নিরাপদ পেমেন্ট গেটওয়ে (DEPOSIT)",
                        color = CasinoGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "256-bit SSL এনক্রিপ্টেড ইনস্ট্যান্ট ওয়ালেট ক্রেডিট",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Gateways Selector Chips
        item {
            Text(
                text = "পেমেন্ট মাধ্যম সিলেক্ট করুন:",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(activeGateways) { gw ->
                    val isSelected = gw.id == selectedGateway.id
                    Surface(
                        color = if (isSelected) CasinoGold else CasinoPurpleCard,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CasinoGoldLight else Color.Transparent),
                        modifier = Modifier.clickable { selectedGateway = gw }
                    ) {
                        Text(
                            text = gw.name,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Selected Gateway Instructions Box
        item {
            Surface(
                color = CasinoPurpleDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CasinoNeonViolet.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "${selectedGateway.name} এজেন্ট নাম্বার:",
                        color = CasinoGoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedGateway.agentNumber,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(selectedGateway.agentNumber))
                                Toast.makeText(context, "নাম্বার কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CasinoPurpleCard),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = CasinoGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "কপি", color = CasinoGold, fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedGateway.instruction,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "সীমা: ৳${"%,.0f".format(selectedGateway.minDeposit)} - ৳${"%,.0f".format(selectedGateway.maxDeposit)} | কোনো ফি নেই (0%)",
                        color = CasinoGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Amount Input + Quick Chips
        item {
            Text(text = "টাকার পরিমাণ (BDT):", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = depositAmountText,
                onValueChange = { depositAmountText = it.filter { ch -> ch.isDigit() } },
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
                        color = if (depositAmountText == amt) CasinoGold else CasinoPurpleCard,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { depositAmountText = amt }
                    ) {
                        Text(
                            text = "৳$amt",
                            color = if (depositAmountText == amt) Color.Black else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Sender Number & TrxID Input
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "আপনার প্রেরক নম্বর / একাউন্ট:", color = TextSecondary, fontSize = 12.sp)
                OutlinedTextField(
                    value = senderNumber,
                    onValueChange = { senderNumber = it },
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

                Text(text = "ট্রানজেকশন আইডি (TrxID):", color = TextSecondary, fontSize = 12.sp)
                OutlinedTextField(
                    value = trxId,
                    onValueChange = { trxId = it.uppercase() },
                    placeholder = { Text("যেমন: BK987X21QA", color = TextMuted) },
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
        }

        // Security Assurance Box
        item {
            Surface(
                color = CasinoPurpleCard,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Encrypted", tint = CasinoGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "আপনার প্রতিটি পেমেন্ট ডেটা এন্ড-টু-এন্ড AES-256 এনক্রিপশনের মাধ্যমে সম্পূর্ণ সুরক্ষিত থাকে।",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Submit Button
        item {
            val amount = depositAmountText.toDoubleOrNull() ?: 0.0
            val isValid = amount >= selectedGateway.minDeposit && trxId.isNotBlank() && senderNumber.isNotBlank()

            Button(
                onClick = {
                    onDepositSubmit(amount, selectedGateway.name, trxId, senderNumber)
                },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "নিরাপদ ডিপোজিট সম্পন্ন করুন (৳${"%,.0f".format(amount)})",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
            }
        }
    }
}
