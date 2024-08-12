package com.toolsforfools.shimmery.shimmerContainer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.LocalShimmerConfiguration
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerConfiguration


@Composable
public fun Modifier.shimmerInContainer(
    enabled: Boolean? = null,
    shimmerConfiguration: ShimmerConfiguration = LocalShimmerConfiguration.current
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
        shimmerConfiguration = shimmerConfiguration,
        enabled = enabled ?: shimmerContainerInfo.enabled,
        gradientStartOffset = startOffset,
        positionInContainer = positionInContainer,
        alpha = shimmerContainerInfo.alpha
    ).onPlaced {
        layoutCoordinates = it
    }
}

@Composable
public fun Modifier.shimmerInContainer(
    enabled: Boolean? = null,
    shimmerConfigurationBuilder: @Composable (ShimmerConfiguration.() -> Unit),
): Modifier {

    val shimmerConfiguration = LocalShimmerConfiguration.current.apply {
        this.shimmerConfigurationBuilder()
    }


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
        shimmerConfiguration = shimmerConfiguration,
        enabled = enabled ?: shimmerContainerInfo.enabled,
        gradientStartOffset = startOffset,
        positionInContainer = positionInContainer,
        alpha = shimmerContainerInfo.alpha
    ).onPlaced {
        layoutCoordinates = it
    }
}

@Composable
private fun Modifier.shimmerInContainerModifierLogic(
    shimmerConfiguration: ShimmerConfiguration,
    enabled: Boolean,
    gradientStartOffset: Float,
    positionInContainer: Offset,
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
                if (shimmerType.withGradient) {
                    val shimmerColors = listOf(
                        Color.LightGray.copy(alpha = 0.8f),
                        Color.LightGray.copy(alpha = 0.2f),
                        Color.LightGray.copy(alpha = 0.8f),
                    )
                    when (gradientType) {
                        GradientType.LINEAR -> {
                            Brush.linearGradient(
                                shimmerColors,
                                start = Offset(
                                    gradientStartOffset - positionInContainer.x,
                                    gradientStartOffset - positionInContainer.y
                                ),
                                end = Offset(
                                    gradientStartOffset * 2 - positionInContainer.x,
                                    gradientStartOffset * 2 - positionInContainer.y,
                                )
                            )
                        }

                        GradientType.HORIZONTAL -> {
                            Brush.horizontalGradient(
                                shimmerColors,
                                startX = gradientStartOffset - positionInContainer.x,
                                endX = gradientStartOffset * 2f - positionInContainer.x
                            )
                        }

                        GradientType.VERTICAL -> {
                            Brush.verticalGradient(
                                shimmerColors,
                                startY = gradientStartOffset - positionInContainer.y,
                                endY = gradientStartOffset * 2f - positionInContainer.y
                            )
                        }
                    }
                } else {
                    null
                }
            }

        onDrawWithContent {
            if (enabled) {
                if (shimmerConfiguration.shimmerType.withGradient) {
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