package com.pp.jetweatherfy.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Curtain(
    openedFromOutside: Boolean? = null,
    foldingDuration: Int = 250,
    mainCell: @Composable () -> Unit,
    foldCells: List<@Composable () -> Unit>
) {
    val foldScope = rememberCoroutineScope()
    var isOpened by remember { mutableStateOf(false) }
    var isTransitionRunning by remember { mutableStateOf(false) }

    fun toggleCurtain() {
        if (!isTransitionRunning) {
            isTransitionRunning = true
            isOpened = !isOpened

            foldScope.launch {
                delay(foldingDuration.toLong() * foldCells.size)
                isTransitionRunning = false
            }
        }
    }

    if (openedFromOutside != null) {
        foldScope.launch {
            delay(15L)
            isOpened = openedFromOutside
        }
    }

    Box(
        modifier = Modifier.curtainModifier(openedFromOutside != null, ::toggleCurtain)
    ) {
        MainCell(
            isOpened = isOpened,
            cellsQuantity = foldCells.size,
            foldingDuration = foldingDuration,
            content = mainCell
        )
        FoldedCells(
            isOpened = isOpened,
            foldingDuration = foldingDuration,
            foldCells = foldCells
        )
    }
}

private fun Modifier.curtainModifier(externalControl: Boolean = false, onClick: () -> Unit): Modifier {
    val modifier = wrapContentSize()
    return if (externalControl) modifier.clickable { onClick() } else modifier
}

@Composable
private fun MainCell(
    isOpened: Boolean,
    cellsQuantity: Int,
    foldingDuration: Int,
    content: @Composable () -> Unit
) {
    val mainCellTransition = updateTransition(targetState = isOpened)

    val mainCellAlpha by mainCellTransition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 100,
                delayMillis = if (isOpened) 0 else foldingDuration * cellsQuantity
            )
        }
    ) { state ->
        when (state) {
            false -> 1f
            true -> 0f
        }
    }

    Box(modifier = Modifier.alpha(mainCellAlpha)) {
        content()
    }
}

@Composable
private fun FoldedCells(
    isOpened: Boolean,
    foldingDuration: Int,
    foldCells: List<@Composable () -> Unit>
) {
    Column {
        foldCells.forEachIndexed { index, cell ->
            FoldedCell(
                isOpened = isOpened,
                cellsQuantity = foldCells.size,
                foldingDuration = foldingDuration,
                index = index,
                content = cell
            )
        }
    }
}

@Composable
private fun FoldedCell(
    isOpened: Boolean,
    cellsQuantity: Int,
    foldingDuration: Int,
    index: Int,
    content: @Composable () -> Unit
) {
    var cellMaxHeight by remember { mutableStateOf(0.dp) }
    val transition = updateTransition(targetState = isOpened)
    val foldingDelay = if (isOpened) foldingDuration * index else foldingDuration * (cellsQuantity - index)

    val rotationValue by transition.animateFloat(transitionSpec = { tween(durationMillis = foldingDuration, delayMillis = foldingDelay) }) { state ->
        when (state) {
            false -> 180f
            true -> 0f
        }
    }
    val alphaValue by transition.animateFloat(transitionSpec = { tween(durationMillis = foldingDuration, delayMillis = foldingDelay) }) { state ->
        when (state) {
            false -> 0f
            true -> 1f
        }
    }
    val sizeValue by transition.animateFloat(transitionSpec = { tween(durationMillis = foldingDuration, delayMillis = foldingDelay) }) { state ->
        when (state) {
            false -> 0.dp.value
            true -> cellMaxHeight.value
        }
    }

    Layout(
        content = content,
        modifier = Modifier
            .graphicsLayer {
                alpha = alphaValue
                rotationX = rotationValue
            }
    ) { measurables, constraints ->
        val contentPlaceable = measurables[0].measure(constraints)
        cellMaxHeight = contentPlaceable.height.dp
        layout(contentPlaceable.width, sizeValue.toInt()) {
            contentPlaceable.place(0, 0)
        }
    }
}
