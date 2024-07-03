package com.salt.apps.feature.screens.home.components.composable.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.salt.apps.core.design.component.LoadingState
import com.salt.apps.core.design.component.ModalBottomSheet
import com.salt.apps.core.design.icon.Icons
import com.salt.apps.core.util.Formatter.formatAmount
import com.salt.apps.feature.screens.transaction.TransactionViewModel
import com.salt.apps.feature.screens.transaction.components.TransactionState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TransactionBottomSheet(
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: (String) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel(),
) {
    val transactionDialogState by viewModel.transactionState.collectAsStateWithLifecycle()

    TransactionBottomSheet(
        transactionState = transactionDialogState,
        onDismiss = onDismiss,
        onEdit = onEdit,
        onDelete = onDelete,
    )
}

@Composable
fun TransactionBottomSheet(
    transactionState: TransactionState,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: (String) -> Unit,
) {
    ModalBottomSheet(onDismiss = onDismiss) {
        if (transactionState.isLoading) {
            LoadingState(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
        }

        if (!transactionState.isLoading) {

            val date =
                transactionState.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val transactionDatex: LocalDate = LocalDate.parse(date.toString())
            val formattedDate =
                transactionDatex.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    transactionState.amount.toBigDecimal()
                        .formatAmount(transactionState.currency, true),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(transactionState.description)

                HorizontalDivider(Modifier.padding(vertical = 20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Transaction Type")
                    Spacer(Modifier.weight(1f))
                    Text(
                        transactionState.transactionType.name.lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Transaction Category")
                    Spacer(Modifier.weight(1f))
                    Text(
                        transactionState.category?.title ?: "none",
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Transaction Date")
                    Spacer(Modifier.weight(1f))
                    Text(
                        formattedDate,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ButtonBottomSheet(
                        title = "Modify Expense",
                        icon = Icons.Edit,
                        colorIcon = Color.White,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = {
                            onDismiss()
                            onEdit()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ButtonBottomSheet(
                        title = "Delete Expense",
                        icon = Icons.Delete,
                        colorIcon = MaterialTheme.colorScheme.error,
                        color = MaterialTheme.colorScheme.errorContainer,
                        onClick = {
                            onDismiss()
                            onDelete(transactionState.transactionId)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}