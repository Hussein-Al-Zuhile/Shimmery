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
import androidx.compose.runtime.LaunchedEffect
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
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerConfigurationBuilder

/**
 * Data class that contains the information about the shimmer container which will be passed to the children that use [shimmerInContainer] modifier.
 * @param startOffset The offset of the shimmer animation in the container.
 * @param alpha The alpha of the shimmer animation.
 * @param layoutCoordinates The layout coordinates of the container,
 * we are using it to get the size of the container and calculate the position of a child in the container.
 * @param enabled Whether the shimmer animation is enabled or not in the whole container, this parameter can be overridden by the child if you want to un-shimmer it.
 */
internal data class ShimmerContainerInformation(
    val startOffset: Float,
    val alpha: Float,
    val layoutCoordinates: LayoutCoordinates?,
    val enabled: Boolean
)

/**
 * Composition local that contains the information about the shimmer container,
 * which will be passed to the children that use [shimmerInContainer] modifier.
 */
internal val LocalShimmeringContainerInformation = compositionLocalOf<ShimmerContainerInformation> {
    error(
        "You have to use shimmerInContainer on a composable that is a child (direct or indirect) to ShimmerContainer.\n" +
                "Wrap this composable inside a ShimmerContainer to resolve the issue."
    )
}

/**
 * A composable that is used to shimmer children in a group.
 * @param enabled Whether the shimmer animation is enabled or not, can be overridden by the child.
 * @param modifier The modifier of the container.
 * @param shimmerConfiguration The shimmer configuration of the container, this will override the [LocalShimmerConfiguration] for the children.
 * @param content The content of the container.
 */
@Composable
public fun ShimmerContainer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    shimmerConfiguration: ShimmerConfiguration? = null,
    content: @Composable BoxScope.() -> Unit
) {

    val selectedShimmerConfiguration = shimmerConfiguration ?: LocalShimmerConfiguration.current

    ShimmerContainerLogic(selectedShimmerConfiguration, enabled, modifier, content)
}

/**
 * A composable that is used to shimmer children in a group.
 * @param enabled Whether the shimmer animation is enabled or not, can be overridden by the child.
 * @param modifier The modifier of the container.
 * @param buildingBlock The shimmer configuration of the container in a DSL style, this will override the [LocalShimmerConfiguration] for the children.
 * @param content The content of the container.
 */
@Composable
public fun ShimmerContainer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    buildingBlock: (ShimmerConfigurationBuilder.() -> Unit),
    content: @Composable BoxScope.() -> Unit
) {

    val shimmerConfiguration =
        with(ShimmerConfigurationBuilder().apply(buildingBlock)) {
            LocalShimmerConfiguration.current.copyAndEdit()
        }

    ShimmerContainerLogic(shimmerConfiguration, enabled, modifier, content)
}

/**
 * This function handles the logic of the [ShimmerContainer] logic,
 * it calculates the required offset and alpha of the shimmer animation and pass it to the children through [LocalShimmeringContainerInformation].
 */
@Composable
private fun ShimmerContainerLogic(
    shimmerConfiguration: ShimmerConfiguration,
    enabled: Boolean,
    modifier: Modifier,
    content: @Composable() (BoxScope.() -> Unit)
) {
    var layoutCoordinates by remember {
        mutableStateOf<LayoutCoordinates?>(null)
    }
    var isAnimationSpecChanged by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(
        key1 = shimmerConfiguration.gradientAnimationSpec,
        key2 = shimmerConfiguration.alphaAnimationSpec
    ) {
        isAnimationSpecChanged = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "Container infinite transition")
    val startOffset =
        if (shimmerConfiguration.shimmerType.withGradient && !isAnimationSpecChanged) {
            infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = layoutCoordinates?.size?.toSize()?.maxDimension ?: 0f,
                animationSpec = infiniteRepeatable(
                    shimmerConfiguration.gradientAnimationSpec,
                    RepeatMode.Restart
                ), label = "Offset animation"
            ).value
        } else {
            0f
        }
    val alpha =
        if (shimmerConfiguration.shimmerType.withAlpha && !isAnimationSpecChanged) {
            infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    shimmerConfiguration.alphaAnimationSpec,
                    RepeatMode.Reverse,
                ), label = "Alpha animation"
            ).value
        } else {
            1f
        }

    isAnimationSpecChanged = false

    CompositionLocalProvider(
        LocalShimmeringContainerInformation provides ShimmerContainerInformation(
            startOffset,
            alpha,
            layoutCoordinates,
            enabled
        ),
        LocalShimmerConfiguration provides shimmerConfiguration
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
