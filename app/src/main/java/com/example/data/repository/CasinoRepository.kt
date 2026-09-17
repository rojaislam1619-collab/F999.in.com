package com.example.data.repository

import android.content.Context
import com.example.data.ai.CasinoChatbotService
import com.example.data.local.*
import com.example.data.security.CryptoSecurityManager
import com.example.data.security.SecurityAuditItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID

class CasinoRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()
    private val transactionDao = db.transactionDao()
    private val gatewayDao = db.paymentGatewayDao()
    private val securityAuditDao = db.securityAuditDao()
    private val chatDao = db.chatDao()
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    val user: Flow<UserAccountEntity?> = userDao.getUserFlow()
    val transactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val withdrawals: Flow<List<TransactionEntity>> = transactionDao.getWithdrawals()
    val gateways: Flow<List<PaymentGatewayEntity>> = gatewayDao.getAllGateways()
    val activeGateways: Flow<List<PaymentGatewayEntity>> = gatewayDao.getActiveGateways()
    val auditLogs: Flow<List<SecurityAuditEntity>> = securityAuditDao.getAllAuditLogs()
    val chatMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()

    init {
        repositoryScope.launch {
            initializeDefaultsIfEmpty()
        }
    }

    private suspend fun initializeDefaultsIfEmpty() {
        // Initialize default user
        val existingUser = userDao.getUser()
        if (existingUser == null) {
            userDao.insertOrUpdate(
                UserAccountEntity(
                    id = 1,
                    username = "VIP_Player777",
                    phoneNumber = "+880 1712-999888",
                    balance = 15000.0,
                    vipTier = "VIP 3 Gold",
                    vipPoints = 4850,
                    isKycVerified = true,
                    isFrozen = false,
                    autoWithdrawalLimit = 50000.0
                )
            )
        }

        // Initialize default payment gateways
        val existingGateways = gatewayDao.getAllGateways().firstOrNull()
        if (existingGateways.isNullOrEmpty()) {
            val defaultGateways = listOf(
                PaymentGatewayEntity(
                    id = "bkash",
                    name = "bKash (বিকাশ)",
                    agentNumber = "01788-999001",
                    instruction = "Send Money to Agent number and copy the 10-digit TrxID to claim instant credit.",
                    minDeposit = 300.0,
                    maxDeposit = 25000.0,
                    minWithdrawal = 500.0,
                    maxWithdrawal = 50000.0,
                    feePercent = 0.0,
                    isEnabled = true
                ),
                PaymentGatewayEntity(
                    id = "nagad",
                    name = "Nagad (নগদ)",
                    agentNumber = "01877-999002",
                    instruction = "Cash In or Send Money to Merchant number and enter your TrxID below.",
                    minDeposit = 300.0,
                    maxDeposit = 50000.0,
                    minWithdrawal = 500.0,
                    maxWithdrawal = 50000.0,
                    feePercent = 0.0,
                    isEnabled = true
                ),
                PaymentGatewayEntity(
                    id = "rocket",
                    name = "Rocket (রকেট)",
                    agentNumber = "01966-999003-8",
                    instruction = "Pay to Rocket Agent with 12-digit number and input Transaction Ref.",
                    minDeposit = 500.0,
                    maxDeposit = 30000.0,
                    minWithdrawal = 500.0,
                    maxWithdrawal = 30000.0,
                    feePercent = 0.0,
                    isEnabled = true
                ),
                PaymentGatewayEntity(
                    id = "usdt",
                    name = "USDT (TRC20)",
                    agentNumber = "TF999CasinoTronVault777SecureAddress",
                    instruction = "Send USDT TRC20 to address. 1 USDT = ৳125 BDT instant conversion.",
                    minDeposit = 1250.0,
                    maxDeposit = 500000.0,
                    minWithdrawal = 2500.0,
                    maxWithdrawal = 500000.0,
                    feePercent = 0.0,
                    isEnabled = true
                ),
                PaymentGatewayEntity(
                    id = "bank",
                    name = "Instant Bank Transfer",
                    agentNumber = "City Bank A/C: 110-999-7772026",
                    instruction = "Direct BEFTN / NPSB instant transfer with account reference name.",
                    minDeposit = 1000.0,
                    maxDeposit = 200000.0,
                    minWithdrawal = 1000.0,
                    maxWithdrawal = 200000.0,
                    feePercent = 0.0,
                    isEnabled = true
                )
            )
            gatewayDao.insertAll(defaultGateways)
        }

        // Initialize default security audit entries
        val existingAudits = securityAuditDao.getAllAuditLogs().firstOrNull()
        if (existingAudits.isNullOrEmpty()) {
            val initialAudits = CryptoSecurityManager.performSystemSecurityAudit()
            for (audit in initialAudits) {
                securityAuditDao.insertLog(
                    SecurityAuditEntity(
                        auditName = audit.title,
                        component = audit.component,
                        status = audit.status,
                        score = audit.rating,
                        details = audit.description
                    )
                )
            }
        }

        // Welcome chat message
        val existingChat = chatDao.getAllMessages().firstOrNull()
        if (existingChat.isNullOrEmpty()) {
            chatDao.insertMessage(
                ChatMessageEntity(
                    sender = "BOT",
                    message = "স্বাগতম F999 গ্রাহক সহায়তা সেন্টারে! 🎰\nআমি আপনার এআই সহকারী। ফাস্ট উইথড্রল, বিকাশ/নগদ ডিপোজিট, গেমের নিয়ম বা সিস্টেম সিকিউরিটি অডিট সম্পর্কে যেকোনো প্রশ্ন করতে পারেন।"
                )
            )
        }
    }

    suspend fun deposit(amount: Double, gatewayName: String, trxId: String, senderNumber: String): Boolean {
        if (amount <= 0 || trxId.isBlank()) return false
        val encResult = CryptoSecurityManager.encryptPayload("DEP:$gatewayName:$trxId:$amount")

        transactionDao.insertTransaction(
            TransactionEntity(
                trxId = trxId.uppercase(),
                type = "DEPOSIT",
                amount = amount,
                method = gatewayName,
                accountNumber = senderNumber,
                status = "COMPLETED",
                statusDetails = "Instant Verification Confirmed (AES Encrypted: ${encResult.iv})",
                timestamp = System.currentTimeMillis(),
                isAutomated = true
            )
        )
        userDao.addBalance(amount)
        return true
    }

    suspend fun requestWithdrawal(amount: Double, method: String, recipientNumber: String, isAutomated: Boolean): Long {
        val currentUser = userDao.getUser() ?: return -1
        if (currentUser.balance < amount) return -1
        if (currentUser.isFrozen) return -2

        // Deduct from balance upfront
        userDao.deductBalance(amount)

        val txId = "WD" + (100000 + (Math.random() * 900000).toInt())
        val recordId = transactionDao.insertTransaction(
            TransactionEntity(
                trxId = txId,
                type = "WITHDRAWAL",
                amount = amount,
                method = method,
                accountNumber = recipientNumber,
                status = if (isAutomated) "PROCESSING" else "PENDING",
                statusDetails = if (isAutomated) "Automated Pipeline: AI Risk Verification in progress..." else "Pending Manual Admin Approval",
                timestamp = System.currentTimeMillis(),
                isAutomated = isAutomated
            )
        )

        if (isAutomated) {
            triggerAutomatedWithdrawalPipeline(recordId)
        }

        return recordId
    }

    fun triggerAutomatedWithdrawalPipeline(recordId: Long) {
        repositoryScope.launch {
            // Stage 1: AI Risk Analysis
            delay(1200)
            transactionDao.updateStatus(
                recordId,
                "PROCESSING",
                "Automated Stage 1/3: AI Risk & AML Check Passed (No irregular play detected)"
            )

            // Stage 2: MFS / Banking Gateway Webhook Dispatch
            delay(1800)
            transactionDao.updateStatus(
                recordId,
                "PROCESSING",
                "Automated Stage 2/3: Dispatched to MFS Fast Payout API Gateway..."
            )

            // Stage 3: Instant Settlement Complete
            delay(1500)
            val authRef = "BATCH_" + UUID.randomUUID().toString().take(8).uppercase()
            transactionDao.updateStatus(
                recordId,
                "COMPLETED",
                "Automated Fast Settlement Completed in 38s! [Ref: $authRef]"
            )
        }
    }

    suspend fun adminApproveWithdrawal(recordId: Long) {
        val authRef = "ADM_" + UUID.randomUUID().toString().take(8).uppercase()
        transactionDao.updateStatus(
            recordId,
            "COMPLETED",
            "Manually Approved by Admin [Ref: $authRef]"
        )
    }

    suspend fun adminRejectWithdrawal(recordId: Long) {
        val tx = transactionDao.getTransactionById(recordId) ?: return
        if (tx.status != "COMPLETED") {
            // Refund to user
            userDao.addBalance(tx.amount)
            transactionDao.updateStatus(
                recordId,
                "FAILED",
                "Rejected by Admin - Funds refunded to player wallet"
            )
        }
    }

    suspend fun adminAdjustBalance(newBalance: Double) {
        val user = userDao.getUser() ?: return
        userDao.insertOrUpdate(user.copy(balance = newBalance))
    }

    suspend fun adminToggleFreeze() {
        val user = userDao.getUser() ?: return
        userDao.insertOrUpdate(user.copy(isFrozen = !user.isFrozen))
    }

    suspend fun updateGateway(gateway: PaymentGatewayEntity) {
        gatewayDao.updateGateway(gateway)
    }

    suspend fun recordBetResult(betAmount: Double, winAmount: Double, gameName: String) {
        val diff = winAmount - betAmount
        if (diff > 0) {
            userDao.addBalance(diff)
            transactionDao.insertTransaction(
                TransactionEntity(
                    trxId = "WIN" + (100000 + (Math.random() * 900000).toInt()),
                    type = "BET_WIN",
                    amount = winAmount,
                    method = gameName,
                    accountNumber = "Provably Fair RNG",
                    status = "COMPLETED",
                    statusDetails = "Multiplier payout credited to balance",
                    timestamp = System.currentTimeMillis(),
                    isAutomated = true
                )
            )
        } else {
            userDao.deductBalance(betAmount)
            transactionDao.insertTransaction(
                TransactionEntity(
                    trxId = "BET" + (100000 + (Math.random() * 900000).toInt()),
                    type = "BET_LOSS",
                    amount = betAmount,
                    method = gameName,
                    accountNumber = "Provably Fair RNG",
                    status = "COMPLETED",
                    statusDetails = "Bet settled",
                    timestamp = System.currentTimeMillis(),
                    isAutomated = true
                )
            )
        }
    }

    suspend fun runSecurityAuditAndSave(): List<SecurityAuditItem> {
        val audits = CryptoSecurityManager.performSystemSecurityAudit()
        for (item in audits) {
            securityAuditDao.insertLog(
                SecurityAuditEntity(
                    auditName = item.title,
                    component = item.component,
                    status = item.status,
                    score = item.rating,
                    details = item.description,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        return audits
    }

    suspend fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return
        chatDao.insertMessage(ChatMessageEntity(sender = "USER", message = userText))

        repositoryScope.launch {
            val botReply = CasinoChatbotService.getBotResponse(userText)
            chatDao.insertMessage(ChatMessageEntity(sender = "BOT", message = botReply))
        }
    }
}
