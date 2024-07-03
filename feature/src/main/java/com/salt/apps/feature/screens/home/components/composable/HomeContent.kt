package com.salt.apps.feature.screens.home.components.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.salt.apps.core.design.component.DynamicTabBar
import com.salt.apps.core.design.component.LoadingState
import com.salt.apps.core.design.icon.StoredIcon
import com.salt.apps.core.domain.model.TransactionCategory
import com.salt.apps.core.util.Formatter.formatAmount
import com.salt.apps.feature.screens.home.components.HomeEvent
import com.salt.apps.feature.screens.home.components.HomeState
import com.salt.apps.feature.screens.home.components.HomeState.Loading
import com.salt.apps.feature.screens.home.components.HomeState.Success
import com.salt.apps.feature.screens.home.components.composable.bottomsheet.TransactionBottomSheet
import com.salt.apps.feature.screens.transaction.components.DateType
import com.salt.apps.feature.screens.transaction.components.TransactionEvent
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


@Composable
fun HomeContent(
    homeState: HomeState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onHomeEvent: (HomeEvent) -> Unit,
    onTransactionEvent: (TransactionEvent) -> Unit,
    onEditTransaction: (String) -> Unit
) {
    when (homeState) {
        Loading -> LoadingState(modifier = Modifier.fillMaxSize())
        is Success -> {
            var showTransactionBottomSheet by rememberSaveable { mutableStateOf(false) }
            var selectedTransactionId by rememberSaveable { mutableStateOf("") }

            LaunchedEffect(homeState.shouldDisplayUndoTransaction) {
                if (homeState.shouldDisplayUndoTransaction) {
                    val snackBarResult = onShowSnackbar("Transaction deleted", "Undo")
                    if (snackBarResult) {
                        onHomeEvent(HomeEvent.UndoTransactionRemoval)
                    } else {
                        onHomeEvent(HomeEvent.ClearUndoState)
                    }
                }
            }
            LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
                onHomeEvent(HomeEvent.ClearUndoState)
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                item {
                    val tabs = listOf("This week", "This month", "All")
                    val selectedIndex = DateType.entries.indexOf(homeState.dateType)
                    DynamicTabBar(
                        tabs = tabs,
                        selectedIndex = selectedIndex,
                        onTabSelected = { index -> onHomeEvent(HomeEvent.UpdateDateType(DateType.entries[index])) }
                    )
                    Box(modifier = Modifier.padding(vertical = 20.dp)) {
                        CardTransactionBalance(homeState)
                    }
                }
                transactions(
                    transactionsCategories = homeState.transactions,
                    currency = "IDR",
                    onTransactionClick = {
                        selectedTransactionId = it
                        onTransactionEvent(TransactionEvent.UpdateTransactionId(it))
                        showTransactionBottomSheet = true
                    },
                )

                if (homeState.transactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Text("Don't have any transactions")
                        }
                    }
                }
            }

            if (showTransactionBottomSheet) {
                TransactionBottomSheet(
                    onDismiss = { showTransactionBottomSheet = false },
                    onEdit = { onEditTransaction(selectedTransactionId) },
                    onDelete = { onHomeEvent(HomeEvent.HideTransaction(it)) },
                )
            }
        }
    }
}

private fun LazyListScope.transactions(
    transactionsCategories: List<TransactionCategory>,
    currency: String,
    onTransactionClick: (String) -> Unit,
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    val groupedTransactionsCategories = transactionsCategories.groupBy {
        it.transaction.timestamp.toJavaInstant().truncatedTo(ChronoUnit.DAYS)
    }.toSortedMap(compareByDescending { it }).map { it.key to it.value }

    groupedTransactionsCategories.forEach { group ->
        item {
            val transactionDate =
                group.first.toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault()).date

            val dateFormat = when (transactionDate) {
                today -> "Today"
                yesterday -> "Yesterday"
                else -> {
                    val transactionDateMonth: LocalDate =
                        LocalDate.parse(transactionDate.toString())
                    val formattedDate =
                        transactionDateMonth.format(DateTimeFormatter.ofPattern("dd MMMM"))
                    formattedDate
                }
            }
            Text(
                dateFormat
            )
            Spacer(Modifier.height(10.dp))
        }

        items(
            items = group.second,
            key = { it.transaction.id },
            contentType = { "transactionCategory" },
        ) { transactionCategory ->
            val transaction = transactionCategory.transaction
            val category = transactionCategory.category
            val date = LocalDateTime.ofInstant(
                transaction.timestamp.toJavaInstant(),
                ZoneId.systemDefault()
            )

            val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
            val formattedDate = date.format(formatter)
            TransactionItem(
                amount = transaction.amount.formatAmount(
                    currencyCode = currency,
                    withPlus = true,
                ),
                icon = category?.iconId ?: StoredIcon.TRANSACTION.storedId,
                categoryTitle = formattedDate,
                onClick = { onTransactionClick(transaction.id) },
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .border(
                        border = BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.surfaceContainerLowest
                        ), shape = RoundedCornerShape(10.dp)
                    )
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
fun TransactionItem(
    amount: String,
    icon: Int,
    categoryTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(10.dp))
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = amount,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            modifier = Modifier.clickable { onClick() },
            supportingContent = {
                Text(
                    text = categoryTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(StoredIcon.asRes(icon)),
                    contentDescription = null,
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
        )
    }
}
