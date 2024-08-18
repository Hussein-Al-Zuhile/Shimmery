package com.toolsforfools.shimmery.shimmerIndividual

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.LocalShimmerConfiguration
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerConfiguration
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerConfigurationBuilder
import com.toolsforfools.shimmery.shimmerConfiguration.shimmerConfiguration
import kotlinx.coroutines.delay

/**
 * Adds shimmer effect to a single composable.
 * @param enabled if true, shimmer effect will be applied.
 * @param buildingBlock DSL builder for [ShimmerConfiguration].
 */
@Composable
public fun Modifier.shimmer(
    enabled: Boolean,
    buildingBlock: ShimmerConfigurationBuilder.() -> Unit
): Modifier {
    if (!enabled) return this

    val inheritedShimmerConfiguration = LocalShimmerConfiguration.current
    val editedShimmerConfiguration =
        with(ShimmerConfigurationBuilder().apply(buildingBlock)) {
            inheritedShimmerConfiguration.copyAndEdit()
        }


    return shimmerModifierLogic(editedShimmerConfiguration)
}

/**
 * Adds shimmer effect to a single composable.
 * @param enabled if true, shimmer effect will be applied.
 * @param shimmerConfiguration [ShimmerConfiguration] object to configure the shimmer effect.
 */
@Composable
public fun Modifier.shimmer(
    enabled: Boolean,
    shimmerConfiguration: ShimmerConfiguration = LocalShimmerConfiguration.current
): Modifier {
    if (!enabled) return this

    return shimmerModifierLogic(
        shimmerConfiguration
    )
}

/**
 * This function handles the logic of adding shimmer effect, it choose the right brush if gradient enabled,
 * And draw a gray rectangle for the available space and clip it with provided shape, with animated alpha
 */

@Composable
private fun Modifier.shimmerModifierLogic(
    shimmerConfiguration: ShimmerConfiguration
): Modifier {


    // This is necessary to update the animationSpec,
    // because compose does not create new object when you enter the same branch of if statement even if the animationSpec is different.
    // So we created a flag observes if the animationSpec has been changed to enter the other branch one time only and then enter the first branch again.
    var isAnimationSpecChanged by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(
        key1 = shimmerConfiguration.gradientAnimationSpec,
        key2 = shimmerConfiguration.alphaAnimationSpec
    ) {
        isAnimationSpecChanged = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val alpha =
        if (shimmerConfiguration.shimmerType.withAlpha && !isAnimationSpecChanged) {
            infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    shimmerConfiguration.alphaAnimationSpec,
                    RepeatMode.Reverse
                ), label = "Alpha animation"
            ).value
        } else {
            1f
        }

    // We need the content size to calculate the target brush offset.
    var contentSize by remember {
        mutableStateOf(Size.Zero)
    }

    // Infinitely animated offset based on the content size.
    val startOffset =
        if (shimmerConfiguration.shimmerType.withGradient && !isAnimationSpecChanged) {
            infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = contentSize.width,
                animationSpec = infiniteRepeatable(
                    shimmerConfiguration.gradientAnimationSpec,
                    RepeatMode.Restart
                ), label = "Offset animation"
            ).value
        } else {
            0f
        }

    isAnimationSpecChanged = false

    return this
        .clip(shimmerConfiguration.shape)
        .drawWithCache {
            contentSize = size

            val shimmerBrush =
                shimmerConfiguration.run {
                    if (shimmerType.withGradient) {

                        val shimmerColors = listOf(
                            Color.LightGray.copy(alpha = 0.8f),
                            Color.LightGray.copy(alpha = 0.2f),
                            Color.LightGray.copy(alpha = 0.8f),
                        )

                        return@run when (gradientType) {
                            GradientType.LINEAR -> {
                                Brush.linearGradient(
                                    shimmerColors,
                                    start = Offset(startOffset, startOffset),
                                    end = Offset(startOffset * 2f, startOffset * 2)
                                )
                            }

                            GradientType.HORIZONTAL -> {
                                Brush.horizontalGradient(
                                    shimmerColors,
                                    startX = startOffset,
                                    endX = startOffset * 2f
                                )
                            }

                            GradientType.VERTICAL -> {
                                Brush.verticalGradient(
                                    shimmerColors,
                                    startY = startOffset,
                                    endY = startOffset * 2f
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


@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun ShimmerPreview() {
    Surface(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(30.dp)

        ) {
            var isLoading by remember {
                mutableStateOf(true)
            }
            LaunchedEffect(key1 = Unit) {
                delay(2000)
                isLoading = false
            }
            Row {
                Box(modifier = Modifier
                    .size(60.dp)
                    .background(Color.Yellow)
                    .padding(10.dp)
                    .shimmer(isLoading) {
                        shape = CircleShape
                    }
                    .background(Color.Red)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Box(
                    modifier = Modifier
                        .size(600.dp)
                        .shimmer(true, shimmerConfiguration { })
                ) {
                }
            }
        }
    }
}