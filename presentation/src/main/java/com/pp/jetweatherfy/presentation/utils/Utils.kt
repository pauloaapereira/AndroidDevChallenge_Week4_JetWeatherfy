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
package com.pp.jetweatherfy.presentation.utils

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.lazy.LazyListState
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LazyListState.scrollToBegin(coroutineScope: CoroutineScope, animated: Boolean = true) {
    coroutineScope.launch {
        if (layoutInfo.totalItemsCount > 0) {
            if (animated)
                animateScrollToItem(0)
            else
                scrollToItem(0)
        }
    }
}

fun FragmentActivity.hasPermissions(vararg permissions: String) = permissions.all { permission ->
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun FragmentActivity.askPermissions(requestCode: Int, vararg permissions: String) {
    ActivityCompat.requestPermissions(
        this,
        permissions,
        requestCode
    )
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}
