package com.salt.apps.feature.screens.home.components.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salt.apps.core.util.Formatter.formatAmount
import com.salt.apps.core.util.Formatter.formatBalance
import com.salt.apps.feature.screens.home.components.HomeState.Success
import com.salt.apps.feature.screens.transaction.components.DateType

@Composable
fun CardTransactionBalance(transactionState: Success) {

    val dateType = when (transactionState.dateType) {
        DateType.WEEK -> "this week"
        DateType.MONTH -> "this month"
        DateType.ALL -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        val formattedMap = transactionState.totalBalance.formatBalance(
            currencyCode = "IDR",
        )
        val currencyCode = formattedMap["currencyCode"]
        val formattedAmount = formattedMap["formattedAmount"]
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total balance $dateType")
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    currencyCode.toString(),
                    style = TextStyle(fontWeight = FontWeight.Bold),
                )
                Text(
                    formattedAmount.toString(),
                    style = TextStyle(
                        fontSize = 33.sp, fontWeight = FontWeight.Bold
                    ),
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    transactionState.incomeBalance.formatAmount(
                        currencyCode = "IDR",
                        withPlus = true
                    ),
                    style = TextStyle(
                        color = Color(0xff388E3C),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    transactionState.expenseBalance.formatAmount(currencyCode = "IDR"),
                    style = TextStyle(color = Color(0xffD32F2F), fontSize = 12.sp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}