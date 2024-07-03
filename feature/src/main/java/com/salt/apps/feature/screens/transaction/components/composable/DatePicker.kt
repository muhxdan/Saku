package com.salt.apps.feature.screens.transaction.components.composable

import androidx.annotation.DrawableRes
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.salt.apps.core.design.component.FormField
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    value: Instant,
    labelTextId: String,
    @DrawableRes iconId: Int,
    onDateClick: (Long) -> Unit,
    initialSelectedDateMillis: Long? = null,
    isAllDatesEnabled: Boolean = true,
) {
    var openDialog by remember { mutableStateOf(false) }

    val localDateTime = value.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val transactionDateMonth: LocalDate = LocalDate.parse(localDateTime.toString())
    val formattedDate =
        transactionDateMonth.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"))

    FormField(
        value = formattedDate,
        readOnly = true,
        label = labelTextId,
        trailingIcon = {
            IconButton(onClick = { openDialog = true }) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconId),
                    contentDescription = null,
                )
            }
        },
    )
    if (openDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis,
            selectableDates = if (!isAllDatesEnabled) {
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                        utcTimeMillis >= System.currentTimeMillis()

                    override fun isSelectableYear(year: Int): Boolean = true
                }
            } else DatePickerDefaults.AllDates,
        )
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = { openDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        onDateClick(datePickerState.selectedDateMillis!!)
                    },
                    enabled = confirmEnabled.value,
                ) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog = false }
                ) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}