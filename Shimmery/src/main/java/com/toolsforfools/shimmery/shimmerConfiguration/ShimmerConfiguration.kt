package com.toolsforfools.shimmery.shimmerConfiguration

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp


data class ShimmerConfiguration(
    var shimmerType: ShimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT,
    var gradientType: GradientType = GradientType.HORIZONTAL,
    var shape: Shape = RoundedCornerShape(16.dp),
    var padding: PaddingValues = PaddingValues(0.dp),
    var width: Int? = null,
    var height: Int? = null,
    var alignment: Alignment = Alignment.Center,
    var gradientColors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.8f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.8f),
    ),
    var backgroundPainter: PainterElement? = null,
    var alphaColor: Color = Color.LightGray.copy(0.8f),
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

fun shimmerConfiguration(shimmerConfigurationBuilder: ShimmerConfiguration.() -> Unit): ShimmerConfiguration {
    val shimmerConfiguration = ShimmerConfiguration()
    shimmerConfiguration.apply(shimmerConfigurationBuilder)
    return shimmerConfiguration
}

sealed class ShimmerType {

    val withAlpha: Boolean
        get() = this != WITH_GRADIANT && this != DISABLED

    val withGradient: Boolean
        get() = this != WITH_ALPHA && this != DISABLED

    data object WITH_ALPHA : ShimmerType()
    data object WITH_GRADIANT : ShimmerType()
    data object WITH_ALPHA_AND_GRADIANT : ShimmerType()
    data object DISABLED : ShimmerType()
    companion object {
        fun values(): Array<ShimmerType> {
            return arrayOf(WITH_ALPHA, WITH_GRADIANT, WITH_ALPHA_AND_GRADIANT, DISABLED)
        }

        fun valueOf(value: String): ShimmerType {
            return when (value) {
                "WITH_ALPHA" -> WITH_ALPHA
                "WITH_GRADIANT" -> WITH_GRADIANT
                "WITH_ALPHA_AND_GRADIANT" -> WITH_ALPHA_AND_GRADIANT
                "DISABLED" -> DISABLED
                else -> throw IllegalArgumentException("No object com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType.$value")
            }
        }
    }
}

enum class GradientType {
    LINEAR, HORIZONTAL, VERTICAL,
}

data class PainterElement(
    val painter: Painter,
    val sizeToIntrinsics: Boolean = true,
    val alignment: Alignment = Alignment.Center,
    val contentScale: ContentScale = ContentScale.FillBounds,
    val alpha: Float = DefaultAlpha,
    val colorFilter: ColorFilter? = null
)

val LocalShimmerConfiguration = compositionLocalOf { ShimmerConfiguration() }