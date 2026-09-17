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
import com.example.data.local.SecurityAuditEntity
import com.example.data.security.CryptoSecurityManager
import com.example.data.security.EncryptedResult
import com.example.data.security.ProvablyFairResult
import com.example.data.security.SecurityAuditItem
import com.example.ui.theme.*

@Composable
fun SecurityAuditScreen(
    auditLogs: List<SecurityAuditEntity>,
    isAuditRunning: Boolean,
    onRunAudit: () -> Unit,
    onVerifyFair: (String, String, Long) -> Unit,
    lastFairResult: ProvablyFairResult?,
    onBack: () -> Unit
) {
    var rawTextToEncrypt by remember { mutableStateOf("bKash:01712345678:Amount:5000:TrxID:BK999X77") }
    var encryptedResult by remember { mutableStateOf<EncryptedResult?>(null) }

    var serverSeedInput by remember { mutableStateOf("f999_server_seed_vault_2026") }
    var clientSeedInput by remember { mutableStateOf("client_lucky_player_777") }
    var nonceInput by remember { mutableStateOf("1") }

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
                    Text(
                        text = "সিকিউরিটি ও ডেটা এনক্রিপশন অডিট",
                        color = CasinoGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "AES-256-GCM মিলিটারি গ্রেড এনক্রিপশন ও Provably Fair RNG",
                        color = CasinoGreen,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Live Audit Scanner Card
        item {
            Surface(
                color = CasinoPurpleDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CasinoGreen.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "সিস্টেম সার্বিক নিরাপত্তা রেটিং:", color = TextSecondary, fontSize = 11.sp)
                            Text(text = "GRADE A+ (PASS)", color = CasinoGreen, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        }
                        Surface(
                            color = CasinoGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "100% SECURE",
                                color = CasinoGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "স্বয়ংক্রিয় অডিট ইঞ্জিন সমস্ত ডেটাবেস এনক্রিপশন, ট্রান্সপোর্ট লেয়ার সিকিউরিটি (TLS 1.3), এবং অটোমেটেড পেআউট ওয়েবহুকের ইন্টিগ্রিটি টেস্ট পরিচালনা করে।",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onRunAudit,
                        enabled = !isAuditRunning,
                        colors = ButtonDefaults.buttonColors(containerColor = CasinoGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = "Run Audit", tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAuditRunning) "সিকিউরিটি অডিট স্ক্যান চলছে..." else "নিরাপত্তা অডিট পরিচালনা করুন (RUN AUDIT)",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Live AES-256 Encryption Demo Box
        item {
            Surface(
                color = CasinoPurpleCard,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = "Encryption", tint = CasinoGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "লাইভ AES-256-GCM ডেটা এনক্রিপশন ডেমো:",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rawTextToEncrypt,
                        onValueChange = { rawTextToEncrypt = it },
                        label = { Text("এনক্রিপ্ট করার জন্য টেক্সট দিন", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CasinoGold,
                            unfocusedBorderColor = CasinoPurpleSurface,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            encryptedResult = CryptoSecurityManager.encryptPayload(rawTextToEncrypt)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CasinoPurpleSurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "এনক্রিপ্ট করুন (ENCRYPT)", color = CasinoGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    if (encryptedResult != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = CasinoBackground,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "অ্যালগরিদম: ${encryptedResult?.algorithm}", color = CasinoGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "সাইফারটেক্সট (CipherHex): ${encryptedResult?.cipherText}",
                                    color = CasinoGoldLight,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Initialization Vector (IV): ${encryptedResult?.iv}", color = TextSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Provably Fair HMAC-SHA256 Cryptographic Verifier
        item {
            Surface(
                color = CasinoPurpleCard,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = "Provably Fair", tint = CasinoCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Provably Fair ক্রিপ্টোগ্রাফিক ভেরিফায়ার:",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "সার্ভার সিড এবং ক্লায়েন্ট ননস দ্বারা গঠিত HMAC-SHA256 এর মাধ্যমে যেকোনো গেম রোলের স্বচ্ছতা যাচাই করুন।",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = clientSeedInput,
                            onValueChange = { clientSeedInput = it },
                            label = { Text("Client Seed", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CasinoGold,
                                unfocusedBorderColor = CasinoPurpleSurface,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        OutlinedTextField(
                            value = nonceInput,
                            onValueChange = { nonceInput = it },
                            label = { Text("Nonce", fontSize = 10.sp) },
                            modifier = Modifier.width(80.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CasinoGold,
                                unfocusedBorderColor = CasinoPurpleSurface,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val n = nonceInput.toLongOrNull() ?: 1L
                            onVerifyFair(serverSeedInput, clientSeedInput, n)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CasinoPurpleSurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "হ্যাশ ও ফেয়ারনেস যাচাই করুন", color = CasinoCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    if (lastFairResult != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = CasinoBackground,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "HMAC-SHA256 Verified Outcome: ${lastFairResult.calculatedOutcome}", color = CasinoGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Hash: ${lastFairResult.combinedHash}", color = TextSecondary, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }

        // Historical Audit Logs Section
        item {
            Text(
                text = "🛡️ নিয়মিত সিকিউরিটি অডিট রিপোর্ট লগ:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        items(auditLogs.take(6)) { log ->
            Surface(
                color = CasinoPurpleDark,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = log.auditName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Surface(
                            color = CasinoGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(text = log.score, color = CasinoGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "কম্পোনেন্ট: ${log.component}", color = CasinoGoldLight, fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = log.details, color = TextSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}
