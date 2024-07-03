package com.salt.apps.feature.screens.transaction.components.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.salt.apps.core.design.component.DynamicTabBar
import com.salt.apps.core.design.component.FormField
import com.salt.apps.core.design.component.LoadingState
import com.salt.apps.core.design.icon.Icons
import com.salt.apps.core.design.icon.StoredIcon
import com.salt.apps.core.domain.model.Category
import com.salt.apps.core.util.Formatter.validateAmount
import com.salt.apps.feature.screens.category.components.category.CategoryState
import com.salt.apps.feature.screens.transaction.components.TransactionEvent
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.Save
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateAmount
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateCategory
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateDate
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateDescription
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateTransactionId
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateTransactionType
import com.salt.apps.feature.screens.transaction.components.TransactionState
import com.salt.apps.feature.screens.transaction.components.TransactionType
import kotlinx.datetime.Instant

@Composable
fun TransactionContent(
    transactionState: TransactionState,
    categoryState: CategoryState,
    onTransactionEvent: (TransactionEvent) -> Unit,
    onDoneTransaction: () -> Unit,
    id: String
) {
    val (descTextField, amountTextField) = remember { FocusRequester.createRefs() }
    LaunchedEffect(Unit) {
        if (id.isEmpty()) amountTextField.requestFocus()
        else onTransactionEvent(UpdateTransactionId(id))
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
    ) {

        when (categoryState) {
            CategoryState.Loading -> LoadingState(modifier = Modifier.fillMaxWidth())
            is CategoryState.Success -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    val tabs = listOf("Income", "Expense")
                    val selectedIndex =
                        TransactionType.entries.indexOf(transactionState.transactionType)

                    DynamicTabBar(
                        tabs = tabs,
                        selectedIndex = selectedIndex,
                        onTabSelected = { index ->
                            onTransactionEvent(
                                UpdateTransactionType(
                                    TransactionType.entries[index]
                                )
                            )
                        }
                    )

                    FormField(
                        value = transactionState.amount,
                        onValueChange = { onTransactionEvent(UpdateAmount(it.validateAmount().first)) },
                        label = "Amount",
                        placeholder = "Enter amount",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier
                            .focusRequester(amountTextField)
                            .focusProperties { next = descTextField }
                    )

                    FormField(
                        value = transactionState.description,
                        onValueChange = { onTransactionEvent(UpdateDescription(it)) },
                        label = "Description",
                        placeholder = "Enter description",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier
                            .focusRequester(descTextField),
                    )

                    CategoryExposedDropdownMenuBox(
                        currentCategory = transactionState.category,
                        categories = categoryState.categories,
                        onCategoryClick = {
                            onTransactionEvent(
                                UpdateCategory(
                                    it
                                )
                            )
                        },
                    )

                    DatePicker(
                        value = transactionState.date,
                        labelTextId = "Date",
                        iconId = Icons.Calendar,
                        initialSelectedDateMillis = transactionState.date.toEpochMilliseconds(),
                        onDateClick = {
                            onTransactionEvent(
                                UpdateDate(
                                    Instant.fromEpochMilliseconds(
                                        it
                                    )
                                )
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            onDoneTransaction()
                            onTransactionEvent(Save)
                        },
                        enabled = transactionState.amount.validateAmount().second,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryExposedDropdownMenuBox(
    currentCategory: Category?,
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var iconId by rememberSaveable { mutableIntStateOf(currentCategory?.iconId ?: 0) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        FormField(
            value = currentCategory?.title ?: "None",
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(StoredIcon.asRes(iconId)),
                    contentDescription = null,
                )
            },
            label = "Category",
            readOnly = true,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(10.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "None",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                onClick = {
                    onCategoryClick(Category())
                    iconId = 0
                    expanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(Icons.Category),
                        contentDescription = null
                    )
                },
            )
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.title.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    onClick = {
                        onCategoryClick(category)
                        iconId = category.iconId ?: 0
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                StoredIcon.asRes(category.iconId ?: 0)
                            ),
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }
}