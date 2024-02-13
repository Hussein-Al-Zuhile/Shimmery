package com.toolsforfools.shimmery.shimmerableContainer

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import com.toolsforfools.shimmery.GradiantType
import com.toolsforfools.shimmery.ShimmerConfiguration
import com.toolsforfools.shimmery.utils.maxOffset


@Composable
fun Modifier.shimmerInContainer(
    enabled: Boolean,
    shimmerConfiguration: ShimmerConfiguration? = null
): Modifier {

    val shimmerContainerInfo = LocalShimmeringContainerInfo.current
    val startOffset = shimmerContainerInfo.startOffset
    val containerPositionInWindow =
        shimmerContainerInfo.layoutCoordinates?.positionInWindow() ?: Offset.Zero

    var layoutCoordinates by remember {
        mutableStateOf<LayoutCoordinates?>(null)
    }
    val positionInWindow = layoutCoordinates?.positionInWindow() ?: Offset.Zero

    val positionInContainer = positionInWindow - containerPositionInWindow

    return this then shimmerInContainerModifierLogic(
        shimmerConfiguration = shimmerConfiguration ?: shimmerContainerInfo.shimmerConfiguration,
        enabled = enabled,
        startOffset = positionInContainer,
        alpha = shimmerContainerInfo.alpha
    ).onPlaced {
        layoutCoordinates = it
    }
}

@Composable
private fun Modifier.shimmerInContainerModifierLogic(
    shimmerConfiguration: ShimmerConfiguration,
    enabled: Boolean,
    startOffset: Offset,
    alpha: Float,
): Modifier {

    return run {
        if (enabled) {
            clip(shimmerConfiguration.shape)
        } else {
            Modifier
        }
    }.drawWithCache {

        val shimmerBrush =
            with(shimmerConfiguration) {
                if (shimmerType.withGradiant) {
                    val shimmerColors = listOf(
                        Color.LightGray.copy(alpha = 0.8f),
                        Color.LightGray.copy(alpha = 0.2f),
                        Color.LightGray.copy(alpha = 0.8f),
                    )
                    when (gradiantType) {
                        GradiantType.LINEAR -> {
                            Brush.linearGradient(
                                shimmerColors,
                                start = startOffset,
                                end = startOffset * 2f
                            )
                        }

                        GradiantType.HORIZONTAL -> {
                            Brush.horizontalGradient(
                                shimmerColors,
                                startX = startOffset.x,
                                endX = startOffset.x * 2f
                            )
                        }

                        GradiantType.VERTICAL -> {
                            Brush.verticalGradient(
                                shimmerColors,
                                startY = startOffset.y,
                                endY = startOffset.y * 2f
                            )
                        }
                    }
                } else {
                    null
                }
            }


        onDrawWithContent {
            if (enabled) {
                if (shimmerConfiguration.shimmerType.withGradiant) {
                    drawRect(
                        shimmerBrush!!,
                        alpha = alpha,
                        size = size,
                        style = Fill,
                    )
                } else {
                    drawRect(
                        Color.LightGray.copy(alpha = 0.8f),
                        alpha = alpha,
                    )
                }
            } else {
                drawContent()
            }
        }
    }
}