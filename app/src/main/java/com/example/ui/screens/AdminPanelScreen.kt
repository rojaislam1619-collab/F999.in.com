package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PaymentGatewayEntity
import com.example.data.local.TransactionEntity
import com.example.data.local.UserAccountEntity
import com.example.ui.theme.*

@Composable
fun AdminPanelScreen(
    user: UserAccountEntity?,
    withdrawals: List<TransactionEntity>,
    gateways: List<PaymentGatewayEntity>,
    onApproveWithdrawal: (Long) -> Unit,
    onRejectWithdrawal: (Long) -> Unit,
    onTriggerAutoPipeline: (Long) -> Unit,
    onAdjustBalance: (Double) -> Unit,
    onToggleFreeze: () -> Unit,
    onUpdateGateway: (PaymentGatewayEntity) -> Unit,
    onRunAudit: () -> Unit,
    onBack: () -> Unit
) {
    var balanceInput by remember { mutableStateOf((user?.balance ?: 15000.0).toString()) }
    var showGatewayDialog by remember { mutableStateOf<PaymentGatewayEntity?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = CasinoGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "F999 এডমিন কন্ট্রোল প্যানেল",
                            color = CasinoGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                    Text(
                        text = "পেমেন্ট গেটওয়ে, অটোমেটেড উইথড্রল ও সিকিউরিটি কন্ট্রোল",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminMetricCard(
                    title = "মোট ইউজার ব্যালেন্স",
                    value = "৳${"%,.0f".format(user?.balance ?: 0.0)}",
                    accentColor = CasinoGold,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "পেন্ডিং উইথড্রল",
                    value = "${withdrawals.count { it.status != "COMPLETED" }} টি",
                    accentColor = CasinoCyan,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "সিকিউরিটি স্ট্যাটাস",
                    value = "GRADE A+",
                    accentColor = CasinoGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section 1: Player Balance & Account Management
        item {
            Surface(
                color = CasinoPurpleDark,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CasinoPurpleSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "👤 প্লেয়ার একাউন্ট ও ব্যালেন্স অ্যাডজাস্টমেন্ট",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ইউজার: ${user?.username} (${user?.phoneNumber}) | ${user?.vipTier}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = balanceInput,
                            onValueChange = { balanceInput = it },
                            label = { Text("নতুন ব্যালেন্স (BDT)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CasinoGold,
                                unfocusedBorderColor = CasinoPurpleSurface,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val nb = balanceInput.toDoubleOrNull() ?: return@Button
                                onAdjustBalance(nb)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("সেভ", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (user?.isFrozen == true) "স্ট্যাটাস: একাউন্ট স্থগিত (FROZEN)" else "স্ট্যাটাস: সক্রিয় (ACTIVE)",
                            color = if (user?.isFrozen == true) CasinoRed else CasinoGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedButton(
                            onClick = onToggleFreeze,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (user?.isFrozen == true) CasinoGreen else CasinoRed
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (user?.isFrozen == true) "আনফ্রিজ করুন" else "একাউন্ট স্থগিত করুন")
                        }
                    }
                }
            }
        }

        // Section 2: Automated Withdrawal Queue
        item {
            Text(
                text = "⚡ উইথড্রল রিকোয়েস্ট ও অটোমেটেড পেআউট কিউ:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        val pendingWithdrawals = withdrawals.filter { it.type == "WITHDRAWAL" }
        if (pendingWithdrawals.isEmpty()) {
            item {
                Surface(
                    color = CasinoPurpleDark,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "বর্তমানে কোনো উইথড্রল পেন্ডিং নেই।",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        } else {
            items(pendingWithdrawals) { tx ->
                AdminWithdrawalRow(
                    tx = tx,
                    onApprove = { onApproveWithdrawal(tx.id) },
                    onReject = { onRejectWithdrawal(tx.id) },
                    onAutoProcess = { onTriggerAutoPipeline(tx.id) }
                )
            }
        }

        // Section 3: Payment Gateway Configuration
        item {
            Text(
                text = "💳 পেমেন্ট গেটওয়ে কনফিগারেশন:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        items(gateways) { gw ->
            AdminGatewayRow(
                gateway = gw,
                onEditClick = { showGatewayDialog = gw },
                onToggleActive = {
                    onUpdateGateway(gw.copy(isEnabled = !gw.isEnabled))
                }
            )
        }

        // Section 4: System Audit Trigger
        item {
            Button(
                onClick = onRunAudit,
                colors = ButtonDefaults.buttonColors(containerColor = CasinoPurpleSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Security, contentDescription = "Run Audit", tint = CasinoGreen, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "সিস্টেম সিকিউরিটি অডিট রান করুন (ADMIN AUDIT)",
                    color = CasinoGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }

    // Edit Gateway Modal
    if (showGatewayDialog != null) {
        val currentGw = showGatewayDialog!!
        var agentNumber by remember { mutableStateOf(currentGw.agentNumber) }
        var minDep by remember { mutableStateOf(currentGw.minDeposit.toString()) }
        var maxDep by remember { mutableStateOf(currentGw.maxDeposit.toString()) }

        AlertDialog(
            onDismissRequest = { showGatewayDialog = null },
            title = { Text(text = "${currentGw.name} সেটিংস", color = CasinoGold, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = agentNumber,
                        onValueChange = { agentNumber = it },
                        label = { Text("এজেন্ট নাম্বার / ঠিকানা") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = minDep,
                        onValueChange = { minDep = it },
                        label = { Text("সর্বনিম্ন ডিপোজিট (BDT)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = maxDep,
                        onValueChange = { maxDep = it },
                        label = { Text("সর্বোচ্চ ডিপোজিট (BDT)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val minVal = minDep.toDoubleOrNull() ?: currentGw.minDeposit
                        val maxVal = maxDep.toDoubleOrNull() ?: currentGw.maxDeposit
                        onUpdateGateway(
                            currentGw.copy(
                                agentNumber = agentNumber,
                                minDeposit = minVal,
                                maxDeposit = maxVal
                            )
                        )
                        showGatewayDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CasinoGold)
                ) {
                    Text("সংরক্ষণ করুন", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGatewayDialog = null }) {
                    Text("বাতিল", color = TextSecondary)
                }
            },
            containerColor = CasinoPurpleDark
        )
    }
}

@Composable
fun AdminMetricCard(title: String, value: String, accentColor: Color, modifier: Modifier = Modifier) {
    Surface(
        color = CasinoPurpleDark,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = TextSecondary, fontSize = 9.sp, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = accentColor, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }
    }
}

@Composable
fun AdminWithdrawalRow(
    tx: TransactionEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onAutoProcess: () -> Unit
) {
    Surface(
        color = CasinoPurpleCard,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${tx.method} • ৳${"%,.0f".format(tx.amount)}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Surface(
                    color = when (tx.status) {
                        "COMPLETED" -> CasinoGreen.copy(alpha = 0.2f)
                        "PROCESSING" -> CasinoGold.copy(alpha = 0.2f)
                        else -> CasinoRed.copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = tx.status,
                        color = when (tx.status) {
                            "COMPLETED" -> CasinoGreen
                            "PROCESSING" -> CasinoGold
                            else -> CasinoRed
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "নম্বর: ${tx.accountNumber} | TrxID: ${tx.trxId}", color = TextSecondary, fontSize = 11.sp)
            Text(text = tx.statusDetails, color = CasinoGoldLight, fontSize = 10.sp)

            if (tx.status != "COMPLETED") {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAutoProcess,
                        colors = ButtonDefaults.buttonColors(containerColor = CasinoPurpleSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CasinoGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(34.dp)
                    ) {
                        Text(text = "⚡ অটো-পেআউট", color = CasinoGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                    ) {
                        Text(text = "অনুমোদন", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onReject,
                        colors = ButtonDefaults.buttonColors(containerColor = CasinoRed),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .weight(0.8f)
                            .height(34.dp)
                    ) {
                        Text(text = "বাতিল", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminGatewayRow(
    gateway: PaymentGatewayEntity,
    onEditClick: () -> Unit,
    onToggleActive: () -> Unit
) {
    Surface(
        color = CasinoPurpleDark,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = gateway.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "নাম্বার: ${gateway.agentNumber}", color = CasinoGoldLight, fontSize = 11.sp)
                Text(
                    text = "সীমা: ৳${"%,.0f".format(gateway.minDeposit)} - ৳${"%,.0f".format(gateway.maxDeposit)}",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CasinoGold, modifier = Modifier.size(18.dp))
                }
                Switch(
                    checked = gateway.isEnabled,
                    onCheckedChange = { onToggleActive() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = CasinoGold
                    )
                )
            }
        }
    }
}
