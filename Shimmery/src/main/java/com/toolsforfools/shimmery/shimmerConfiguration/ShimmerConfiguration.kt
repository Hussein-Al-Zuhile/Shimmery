package com.toolsforfools.shimmery.shimmerConfiguration

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * This class is used to provide the configuration for the shimmer effect.
 * You can use the [shimmerConfiguration] function to create a [ShimmerConfiguration] object.
 * @sample com.toolsforfools.shimmery.examples.ShimmerConfigurationExample
 *
 *
 * @param [shimmerType] is used to determine the type of shimmer effect to be used.
 * @param [gradientType] is used to determine the type of gradient to be used.
 * @param [shape] is used to determine the shape of the shimmer effect.
 * @param [gradientAnimationSpec] is used to determine the animation spec for the gradient.
 * @param [alphaAnimationSpec] is used to determine the animation spec for the alpha.
 */
public data class ShimmerConfiguration public constructor(
    public val shimmerType: ShimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT,
    public val gradientType: GradientType = GradientType.HORIZONTAL,
    public val shape: Shape = RoundedCornerShape(16.dp),
    public val gradientAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1200),
    public val alphaAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1000)
)

public class ShimmerConfigurationBuilder {
    public var shimmerType: ShimmerType? = null
    public var gradientType: GradientType? = null
    public var shape: Shape? = null
    public var gradientAnimationSpec: DurationBasedAnimationSpec<Float>? = null
    public var alphaAnimationSpec: DurationBasedAnimationSpec<Float>? = null
    public fun build(): ShimmerConfiguration {
        return ShimmerConfiguration(
            shimmerType ?: ShimmerType.WITH_ALPHA_AND_GRADIANT,
            gradientType ?: GradientType.HORIZONTAL,
            shape ?: RoundedCornerShape(16.dp),
            gradientAnimationSpec ?: tween(1200),
            alphaAnimationSpec ?: tween(1000)
        )
    }

    public fun ShimmerConfiguration.copyAndEdit(): ShimmerConfiguration {
        return ShimmerConfiguration(
            this@ShimmerConfigurationBuilder.shimmerType ?: shimmerType,
            this@ShimmerConfigurationBuilder.gradientType ?: gradientType,
            this@ShimmerConfigurationBuilder.shape ?: shape,
            this@ShimmerConfigurationBuilder.gradientAnimationSpec ?: gradientAnimationSpec,
            this@ShimmerConfigurationBuilder.alphaAnimationSpec ?: alphaAnimationSpec
        )
    }
}

/**
 * This function is used to create a [ShimmerConfiguration] object in a DSL style.
 */
public fun shimmerConfiguration(buildingBlock: ShimmerConfigurationBuilder.() -> Unit): ShimmerConfiguration {
    return ShimmerConfigurationBuilder().apply(buildingBlock).build()
}

/**
 * This enum class is used to determine the type of shimmer effect to be used.
 * @sample com.toolsforfools.shimmery.examples.ShimmerConfigurationExample
 */
public enum class ShimmerType {
    WITH_ALPHA, WITH_GRADIANT, WITH_ALPHA_AND_GRADIANT;

    public val withAlpha: Boolean
        get() = this != WITH_GRADIANT

    public val withGradient: Boolean
        get() = this != WITH_ALPHA
}

/**
 * This enum class is used to determine the type of gradient to be used.
 * @sample com.toolsforfools.shimmery.examples.ShimmerConfigurationExample
 */
public enum class GradientType {
    LINEAR, HORIZONTAL, VERTICAL,
}

/**
 * This object is used to provide the configuration for the shimmer effect in a global way.
 *
 * You can provide a default configuration for the shimmer effect by using the [androidx.compose.runtime.CompostionLocalProvider] function in the root composable.
 * @sample com.toolsforfools.shimmery.examples.documentExamples.LocalProviderExample
 *
 */
public val LocalShimmerConfiguration: ProvidableCompositionLocal<ShimmerConfiguration> =
    compositionLocalOf { ShimmerConfiguration() }