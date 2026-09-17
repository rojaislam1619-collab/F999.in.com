package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.CasinoViewModel
import com.example.ui.ScreenDestination
import com.example.ui.components.CasinoBottomNavigationBar
import com.example.ui.components.CasinoTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CasinoApp()
            }
        }
    }
}

@Composable
fun CasinoApp(viewModel: CasinoViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val withdrawals by viewModel.withdrawals.collectAsState()
    val gateways by viewModel.gateways.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isAuditRunning by viewModel.isAuditRunning.collectAsState()
    val lastFairResult by viewModel.lastFairResult.collectAsState()
    val notificationMessage by viewModel.notificationMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(notificationMessage) {
        notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissNotification()
        }
    }

    val showBottomBar = currentScreen in listOf(
        ScreenDestination.HOME,
        ScreenDestination.DEPOSIT,
        ScreenDestination.WITHDRAWAL,
        ScreenDestination.SECURITY,
        ScreenDestination.CHATBOT
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CasinoTopAppBar(
                user = user,
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                CasinoBottomNavigationBar(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    ScreenDestination.HOME -> {
                        HomeScreen(
                            user = user,
                            transactions = transactions,
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }

                    ScreenDestination.SLOTS -> {
                        GameSlotScreen(
                            currentBalance = user?.balance ?: 0.0,
                            onRecordResult = { bet, win, game ->
                                viewModel.recordGameResult(bet, win, game)
                            },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.CRASH -> {
                        GameCrashScreen(
                            currentBalance = user?.balance ?: 0.0,
                            onRecordResult = { bet, win, game ->
                                viewModel.recordGameResult(bet, win, game)
                            },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.WHEEL -> {
                        GameWheelScreen(
                            currentBalance = user?.balance ?: 0.0,
                            onRecordResult = { bet, win, game ->
                                viewModel.recordGameResult(bet, win, game)
                            },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.TABLE -> {
                        GameTableScreen(
                            currentBalance = user?.balance ?: 0.0,
                            onRecordResult = { bet, win, game ->
                                viewModel.recordGameResult(bet, win, game)
                            },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.DEPOSIT -> {
                        DepositScreen(
                            gateways = gateways,
                            onDepositSubmit = { amount, method, trxId, senderNumber ->
                                viewModel.submitDeposit(amount, method, trxId, senderNumber)
                            },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.WITHDRAWAL -> {
                        WithdrawalScreen(
                            user = user,
                            withdrawals = withdrawals,
                            onWithdrawalSubmit = { amount, method, recipientNumber, isAutomated ->
                                viewModel.submitWithdrawal(amount, method, recipientNumber, isAutomated)
                            },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.SECURITY -> {
                        SecurityAuditScreen(
                            auditLogs = auditLogs,
                            isAuditRunning = isAuditRunning,
                            onRunAudit = { viewModel.runSecurityAudit() },
                            onVerifyFair = { serverSeed, clientSeed, nonce ->
                                viewModel.verifyProvablyFair(serverSeed, clientSeed, nonce)
                            },
                            lastFairResult = lastFairResult,
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.CHATBOT -> {
                        ChatbotScreen(
                            messages = chatMessages,
                            onSendMessage = { viewModel.sendChatMessage(it) },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.ADMIN -> {
                        AdminPanelScreen(
                            user = user,
                            withdrawals = withdrawals,
                            gateways = gateways,
                            onApproveWithdrawal = { viewModel.adminApproveWithdrawal(it) },
                            onRejectWithdrawal = { viewModel.adminRejectWithdrawal(it) },
                            onTriggerAutoPipeline = { viewModel.adminTriggerAutoPipeline(it) },
                            onAdjustBalance = { viewModel.adminAdjustBalance(it) },
                            onToggleFreeze = { viewModel.adminToggleFreeze() },
                            onUpdateGateway = { viewModel.adminUpdateGateway(it) },
                            onRunAudit = { viewModel.runSecurityAudit() },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }
                }
            }
        }
    }
}

