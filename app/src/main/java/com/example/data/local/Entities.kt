package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserAccountEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "VIP_Player777",
    val phoneNumber: String = "+880 1712-345678",
    val balance: Double = 5000.0,
    val vipTier: String = "VIP 2 Silver",
    val vipPoints: Int = 1450,
    val isKycVerified: Boolean = true,
    val isFrozen: Boolean = false,
    val autoWithdrawalLimit: Double = 25000.0
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trxId: String,
    val type: String, // DEPOSIT, WITHDRAWAL, BET_WIN, BET_LOSS, BONUS
    val amount: Double,
    val method: String, // bKash, Nagad, Rocket, USDT, Bank
    val accountNumber: String,
    val status: String, // PENDING, PROCESSING, COMPLETED, FAILED
    val statusDetails: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isAutomated: Boolean = true
)

@Entity(tableName = "payment_gateways")
data class PaymentGatewayEntity(
    @PrimaryKey val id: String,
    val name: String,
    val agentNumber: String,
    val instruction: String,
    val minDeposit: Double,
    val maxDeposit: Double,
    val minWithdrawal: Double,
    val maxWithdrawal: Double,
    val feePercent: Double,
    val isEnabled: Boolean = true
)

@Entity(tableName = "security_audit_logs")
data class SecurityAuditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val auditName: String,
    val component: String,
    val status: String,
    val score: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "USER" or "BOT"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
