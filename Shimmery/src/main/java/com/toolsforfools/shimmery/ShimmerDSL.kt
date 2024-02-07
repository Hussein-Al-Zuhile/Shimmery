package com.toolsforfools.shimmery

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


data class ShimmerConfiguration(
    var shimmerType: ShimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT,
    var gradiantType: GradiantType = GradiantType.HORIZONTAL,
    var shape: Shape = RoundedCornerShape(16.dp)
) {
    var gradiantAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1200)
        set(value) {
            isGradiantAnimationSpecUpdated = field != value
            field = value
        }
    internal var isGradiantAnimationSpecUpdated = false
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

fun shimmerConfiguration(shimmerConfigurationBuilder: ShimmerConfiguration.() -> Unit): ShimmerConfiguration {
    val shimmerConfiguration = ShimmerConfiguration()
    shimmerConfiguration.apply(shimmerConfigurationBuilder)
    return shimmerConfiguration
}

enum class ShimmerType {
    WITH_ALPHA, WITH_GRADIANT, WITH_ALPHA_AND_GRADIANT;

    val withAlpha: Boolean
        get() = this != WITH_GRADIANT

    val withGradiant: Boolean
        get() = this != WITH_ALPHA
}

enum class GradiantType {
    LINEAR, HORIZONTAL, VERTICAL,
}