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
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerConfigurationBuilder


/**
 * A modifier to shimmer a composable in a [ShimmerContainer]
 * @param enabled if it's null it will use the value from [ShimmerContainer], otherwise it will use the value provided,
 * This is helpful for partial shimmering if you want to shimmer only a part of the container (in case of two loading states exist).
 * @param shimmerConfiguration configuration for the shimmer effect.
 */
@Composable
public fun Modifier.shimmerInContainer(
    enabled: Boolean? = null,
    shimmerConfiguration: ShimmerConfiguration = LocalShimmerConfiguration.current
): Modifier {
    val shimmerContainerInfo = LocalShimmeringContainerInformation.current
    if (!(enabled ?: shimmerContainerInfo.enabled)) return this // Return if not enabled


    val startOffset = shimmerContainerInfo.startOffset
    val containerPositionInWindow =
        shimmerContainerInfo.layoutCoordinates?.positionInWindow() ?: Offset.Zero

    var layoutCoordinates by remember {
        mutableStateOf<LayoutCoordinates?>(null)
    }
    val positionInWindow = layoutCoordinates?.positionInWindow() ?: Offset.Zero

    val positionInContainer = positionInWindow - containerPositionInWindow

    return shimmerInContainerModifierLogic(
        shimmerConfiguration = shimmerConfiguration,
        gradientStartOffset = startOffset,
        positionInContainer = positionInContainer,
        alpha = shimmerContainerInfo.alpha
    ).onPlaced {
        layoutCoordinates = it
    }
}

/**
 * A modifier to shimmer a child composable of a [ShimmerContainer] (direct or indirect)
 * @param enabled if it's null it will use the value from [ShimmerContainer], otherwise it will use the value provided,
 * This is helpful for partial shimmering if you want to shimmer only a part of the container (in case of two loading states exist).
 * @param buildingBlock a lambda to configure the shimmer effect.
 */
@Composable
public fun Modifier.shimmerInContainer(
    enabled: Boolean? = null,
    buildingBlock: (ShimmerConfigurationBuilder.() -> Unit),
): Modifier {
    val shimmerContainerInfo = LocalShimmeringContainerInformation.current
    if (!(enabled ?: shimmerContainerInfo.enabled)) return this


    val shimmerConfiguration = with(ShimmerConfigurationBuilder().apply(buildingBlock)) {
        LocalShimmerConfiguration.current.copyAndEdit()
    }

    val startOffset = shimmerContainerInfo.startOffset
    val containerPositionInWindow =
        shimmerContainerInfo.layoutCoordinates?.positionInWindow() ?: Offset.Zero

    var layoutCoordinates by remember {
        mutableStateOf<LayoutCoordinates?>(null)
    }
    val positionInWindow = layoutCoordinates?.positionInWindow() ?: Offset.Zero

    val positionInContainer = positionInWindow - containerPositionInWindow

    return shimmerInContainerModifierLogic(
        shimmerConfiguration = shimmerConfiguration,
        gradientStartOffset = startOffset,
        positionInContainer = positionInContainer,
        alpha = shimmerContainerInfo.alpha
    ).onPlaced {
        layoutCoordinates = it
    }
}

/**
 * This function handles the logic of adding shimmer effect, it choose the right brush if gradient enabled,
 * And draw a gray rectangle for the available space and clip it with provided shape, with animated alpha
 */
@Composable
private fun Modifier.shimmerInContainerModifierLogic(
    shimmerConfiguration: ShimmerConfiguration,
    gradientStartOffset: Float,
    positionInContainer: Offset,
    alpha: Float,
): Modifier {

    return this
        .clip(shimmerConfiguration.shape)
        .drawWithCache {

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
            }
        }
}