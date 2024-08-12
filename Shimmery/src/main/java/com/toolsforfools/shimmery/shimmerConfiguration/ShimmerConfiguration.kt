package com.toolsforfools.shimmery.shimmerConfiguration

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


public data class ShimmerConfiguration(
    var shimmerType: ShimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT,
    var gradientType: GradientType = GradientType.HORIZONTAL,
    var shape: Shape = RoundedCornerShape(16.dp)
) {
    var gradientAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1200)
        set(value) {
            isGradientAnimationSpecUpdated = field != value
            field = value
        }
    internal var isGradientAnimationSpecUpdated = false
        get() {
            val currentValue = field
            field = false
            return currentValue
        }

    var alphaAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1000)
        set(value) {
            isAlphaAnimationSpecUpdated = field != value
            field = value
        }
    internal var isAlphaAnimationSpecUpdated = false
        get() {
            val currentValue = field
            field = false
            return currentValue
        }

}

public fun shimmerConfiguration(shimmerConfigurationBuilder: ShimmerConfiguration.() -> Unit): ShimmerConfiguration {
    val shimmerConfiguration = ShimmerConfiguration()
    shimmerConfiguration.apply(shimmerConfigurationBuilder)
    return shimmerConfiguration
}

public enum class ShimmerType {
    WITH_ALPHA, WITH_GRADIANT, WITH_ALPHA_AND_GRADIANT;

    public val withAlpha: Boolean
        get() = this != WITH_GRADIANT

    public val withGradient: Boolean
        get() = this != WITH_ALPHA
}

public enum class GradientType {
    LINEAR, HORIZONTAL, VERTICAL,
}

public val LocalShimmerConfiguration: ProvidableCompositionLocal<ShimmerConfiguration> = compositionLocalOf { ShimmerConfiguration() }