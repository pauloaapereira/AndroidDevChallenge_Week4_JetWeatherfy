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
package com.pp.jetweatherfy.ui.components.topbar

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState.Active
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pp.jetweatherfy.R
import com.pp.jetweatherfy.domain.JetWeatherfyState
import com.pp.jetweatherfy.domain.JetWeatherfyState.Idle
import com.pp.jetweatherfy.domain.JetWeatherfyState.Running
import com.pp.jetweatherfy.ui.ForecastViewModel
import com.pp.jetweatherfy.ui.theme.MediumDimension

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun JetWeatherfySearchBar(
    modifier: Modifier = Modifier,
    viewModel: ForecastViewModel,
    state: JetWeatherfyState,
    cities: List<String>
) {
    var isSearching by remember { mutableStateOf(false) }
    val query by viewModel.searchQuery.observeAsState("")

    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current

    fun unFocus() {
        softwareKeyboardController?.hideSoftwareKeyboard()
        view.clearFocus()
    }

    fun updateQuery(newQuery: String) {
        // When there is no text on the input, and the user clicks on the X
        if (query.isBlank() && newQuery.isBlank()) {
            unFocus()
        }
        viewModel.search(newQuery)
    }

    fun setQuery(newQuery: String) {
        viewModel.selectCity(newQuery)
        unFocus()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = modifier
                .onFocusChanged { focusState ->
                    isSearching = focusState == Active
                }
                .fillMaxWidth(),
            value = query,
            onValueChange = { newQuery ->
                updateQuery(newQuery)
            },
            label = { Text(text = stringResource(id = R.string.choose_city)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = stringResource(id = R.string.choose_city)
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clear),
                    contentDescription = stringResource(R.string.clear),
                    modifier = Modifier.clickable { updateQuery("") }
                )
            },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { unFocus() }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                autoCorrect = false,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedLabelColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
            ),
            enabled = state is Running || state is Idle
        )
        AnimatedVisibility(visible = isSearching) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(min = 0.dp, TextFieldDefaults.MinHeight * 5)
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.medium
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(cities) { city ->
                    SearchBarItem(text = city) { setQuery(city) }
                }
            }
        }
    }
}

@Composable
private fun SearchBarItem(text: String, onClick: () -> Unit) {
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
