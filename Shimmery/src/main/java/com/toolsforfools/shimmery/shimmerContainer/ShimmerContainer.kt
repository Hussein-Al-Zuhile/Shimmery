package com.toolsforfools.shimmery.shimmerContainer

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.toolsforfools.shimmery.shimmerConfiguration.LocalShimmerConfiguration
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerConfiguration


internal data class ShimmerContainerInfo(
    val startOffset: Float,
    val alpha: Float,
    val layoutCoordinates: LayoutCoordinates?,
    val shimmerConfiguration: ShimmerConfiguration,
    val enabled: Boolean
)

internal val LocalShimmeringContainerInfo = compositionLocalOf<ShimmerContainerInfo> {
    error(
        "You have to use shimmerInContainer on a composable inside a ShimmerContainer.\n" +
                "Wrap this composable inside a ShimmerContainer to resolve the issue."
    )
}

@Composable
public fun ShimmerContainer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    shimmerConfiguration: ShimmerConfiguration? = null,
    content: @Composable BoxScope.() -> Unit
) {

    val selectedShimmerConfiguration = shimmerConfiguration ?: LocalShimmerConfiguration.current

    var layoutCoordinates by remember {
        mutableStateOf<LayoutCoordinates?>(null)
    }

    val infiniteTransition = rememberInfiniteTransition()
    val startOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = layoutCoordinates?.size?.toSize()?.maxDimension ?: 0f,
        animationSpec = infiniteRepeatable(tween(2000)), label = ""
    )
    val alpha =
        if (selectedShimmerConfiguration.shimmerType.withAlpha && !selectedShimmerConfiguration.isAlphaAnimationSpecUpdated) {
            infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    selectedShimmerConfiguration.alphaAnimationSpec,
                    RepeatMode.Reverse,
                ), label = "Alpha animation"
            ).value
        } else {
            1f
        }

    CompositionLocalProvider(
        LocalShimmeringContainerInfo provides ShimmerContainerInfo(
            startOffset,
            alpha,
            layoutCoordinates,
            selectedShimmerConfiguration,
            enabled
        )
    ) {
        Box(modifier.onPlaced {
            layoutCoordinates = it
        }, content = content)
    }
}

@Preview
@Composable
private fun ShimmerContainerPreview() {
    Box(Modifier.padding(30.dp)) {
        ShimmerContainer(enabled = true) {
            Column {
                Row {
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(2f)
                            .shimmerInContainer()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(2f)
                            .shimmerInContainer()
                    )
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(2f)
                            .shimmerInContainer()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }
    }
}
