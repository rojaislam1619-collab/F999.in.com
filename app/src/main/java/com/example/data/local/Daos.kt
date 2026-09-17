package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_account WHERE id = 1 LIMIT 1")
    fun getUserFlow(): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_account WHERE id = 1 LIMIT 1")
    suspend fun getUser(): UserAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserAccountEntity)

    @Update
    suspend fun update(user: UserAccountEntity)

    @Query("UPDATE user_account SET balance = balance + :amount WHERE id = 1")
    suspend fun addBalance(amount: Double)

    @Query("UPDATE user_account SET balance = balance - :amount WHERE id = 1")
    suspend fun deductBalance(amount: Double)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = 'WITHDRAWAL' ORDER BY timestamp DESC")
    fun getWithdrawals(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Query("UPDATE transactions SET status = :status, statusDetails = :details WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, details: String)

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Long): TransactionEntity?
}

@Dao
interface PaymentGatewayDao {
    @Query("SELECT * FROM payment_gateways")
    fun getAllGateways(): Flow<List<PaymentGatewayEntity>>

    @Query("SELECT * FROM payment_gateways WHERE isEnabled = 1")
    fun getActiveGateways(): Flow<List<PaymentGatewayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gateways: List<PaymentGatewayEntity>)

    @Update
    suspend fun updateGateway(gateway: PaymentGatewayEntity)
}

@Dao
interface SecurityAuditDao {
    @Query("SELECT * FROM security_audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<SecurityAuditEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SecurityAuditEntity)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}
