package com.toolsforfools.shimmery

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.shimmer(
    enabled: Boolean,
    shimmerConfigurationBuilder: @Composable (ShimmerConfiguration.() -> Unit)? = null
): Modifier {
    val shimmerConfiguration = remember { ShimmerConfiguration() }

    shimmerConfigurationBuilder?.let { shimmerConfiguration.it() }

    return shimmerModifierLogic(shimmerConfiguration, enabled)
}

@Composable
fun Modifier.shimmer(
    enabled: Boolean,
    shimmerConfiguration: ShimmerConfiguration
): Modifier {
    return shimmerModifierLogic(shimmerConfiguration, enabled)
}

@Composable
private fun Modifier.shimmerModifierLogic(
    shimmerConfiguration: ShimmerConfiguration,
    enabled: Boolean
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val alpha = if (shimmerConfiguration.shimmerType.withAlpha) {
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
    var contentSize by remember {
        mutableStateOf(Size.Zero)
    }
    val startOffset = if (shimmerConfiguration.shimmerType.withGradiant) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = contentSize.width,
            animationSpec = infiniteRepeatable(
                shimmerConfiguration.gradiantAnimationSpec,
                RepeatMode.Restart
            ), label = "Offset animation"
        ).value
    } else {
        0f
    }
    return this then
            clip(shimmerConfiguration.shape) then
            drawWithCache {
                contentSize = size
                with(shimmerConfiguration) {

                    val shimmerBrush = if (shimmerType.withGradiant) {
                        val shimmerColors = listOf(
                            Color.LightGray.copy(alpha = 0.6f),
                            Color.LightGray.copy(alpha = 0.2f),
                            Color.LightGray.copy(alpha = 0.6f),
                        )
                        when (gradiantType) {
                            GradiantType.Linear -> {
                                Brush.linearGradient(
                                    shimmerColors,
                                    start = Offset(startOffset, startOffset),
                                    end = Offset(startOffset * 2f, startOffset * 2)
                                )
                            }

                            GradiantType.HORIZONTAL -> {
                                Brush.horizontalGradient(
                                    shimmerColors,
                                    startX = startOffset,
                                    endX = startOffset * 2f
                                )
                            }

                            GradiantType.VERTICAL -> {
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
                    onDrawWithContent {
                        if (enabled) {
                            if (shimmerType.withGradiant) {
                                drawRect(
                                    shimmerBrush!!,
                                    alpha = alpha,
                                    size = size,
                                    style = Fill,
                                )
                            } else {
                                drawRect(
                                    Color.Gray,
                                    alpha = alpha,
                                )
                            }
                        } else {
                            drawContent()
                        }
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
            Row {
                Box(modifier = Modifier
                    .size(60.dp)
                    .shimmer(true) {}) {

                }
                Spacer(modifier = Modifier.width(30.dp))
                Box(modifier = Modifier
                    .size(600.dp)
                    .shimmer(true) {}) {

                }
            }
        }
    }
}