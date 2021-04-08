/*
 * Copyright 2021 Paulo Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pp.jetweatherfy.presentation.forecast.components.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusState.Active
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.presentation.R
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.HandlingErrors
import com.pp.jetweatherfy.presentation.forecast.state.ViewStatus.Loading
import com.pp.jetweatherfy.presentation.theme.MediumDimension
import com.pp.jetweatherfy.presentation.utils.hideKeyboard

private val AutoCompleteBoxSize = TextFieldDefaults.MinHeight * 5

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun JetWeatherfySearchBar(
    modifier: Modifier = Modifier,
    trailingIconModifier: Modifier = Modifier,
    query: String,
    cities: List<String>,
    viewStatus: ViewStatus,
    onQueryTyping: (String) -> Unit,
    onItemSelected: (String) -> Unit
) {
    var isSearching by remember { mutableStateOf(false) }

    val view = LocalView.current

    fun unFocus() {
        view.hideKeyboard()
        view.clearFocus()
    }

    fun updateQuery(newQuery: String) {
        if (viewStatus == Loading) return

        // When there is no text on the input, and the user clicks on the X
        if (query.isBlank() && newQuery.isBlank()) {
            unFocus()
        }

        onQueryTyping(newQuery)
    }

    fun setQuery(newQuery: String) {
        onItemSelected(newQuery)
        unFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            modifier = modifier,
            trailingIconModifier = trailingIconModifier,
            isEnabled = viewStatus != Loading && viewStatus != HandlingErrors,
            value = query,
            onFocusChanged = { isFocused -> isSearching = isFocused },
            onDone = { unFocus() },
            onValueChange = { newQuery ->
                updateQuery(newQuery)
            }
        )
        AutoComplete(
            isVisible = isSearching,
            items = cities,
            onFieldSelected = { city ->
                setQuery(city)
            }
        )
    }
}

@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    trailingIconModifier: Modifier = Modifier,
    isEnabled: Boolean,
    value: String,
    onFocusChanged: (Boolean) -> Unit,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                onFocusChanged(focusState == Active)
            }
            .fillMaxWidth(),
        value = value,
        onValueChange = { newQuery ->
            onValueChange(newQuery)
        },
        label = { Text(text = stringResource(id = R.string.choose_city)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = stringResource(id = R.string.choose_city)
            )
        },
        trailingIcon = {
            IconButton(modifier = trailingIconModifier, onClick = { if (isEnabled) onValueChange("") }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clear),
                    contentDescription = stringResource(R.string.clear)
                )
            }
        },
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            autoCorrect = false,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedLabelColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
        ),
        enabled = isEnabled
    )
}

@ExperimentalAnimationApi
@Composable
private fun AutoComplete(
    isVisible: Boolean,
    items: List<String>,
    onFieldSelected: (String) -> Unit
) {
    AnimatedVisibility(visible = isVisible) {
        LazyColumn(
            modifier = Modifier
                .heightIn(min = 0.dp, AutoCompleteBoxSize)
                .fillMaxWidth()
                .autoCompleteBorder(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items) { item ->
                AutoCompleteItem(text = item) { onFieldSelected(item) }
            }
        }
    }
}

@Composable
private fun AutoCompleteItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(MediumDimension),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.subtitle2)
    }
}

private fun Modifier.autoCompleteBorder(): Modifier = composed {
    border(
        width = 2.dp,
        color = MaterialTheme.colors.primary,
        shape = MaterialTheme.shapes.medium
    )
}
