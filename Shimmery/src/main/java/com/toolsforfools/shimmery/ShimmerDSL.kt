package com.toolsforfools.shimmery

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


data class ShimmerConfiguration(
    var shimmerType: ShimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT,
    var gradiantType: GradiantType = GradiantType.HORIZONTAL,
    var alphaAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1000),
    var gradiantAnimationSpec: DurationBasedAnimationSpec<Float> = tween(1200),
    var shape: Shape = RoundedCornerShape(16.dp)
)

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
    Linear, HORIZONTAL, VERTICAL,
}