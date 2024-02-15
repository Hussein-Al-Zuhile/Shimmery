package com.toolsforfools.shimmery.examples

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerConfiguration
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerIndividual.shimmer

private val appShimmerConfiguration by lazy {
    ShimmerConfiguration(
        shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT,
        gradientType = GradientType.VERTICAL,
        shape = RoundedCornerShape(16.dp),
    ).apply {
        alphaAnimationSpec = tween(1000)
        gradientAnimationSpec = tween(1000)
    }
}

@Composable
private fun ShimmerComposable(modifier: Modifier = Modifier) {
    var isEnabled by remember {
        mutableStateOf(false)
    }
    Box(
        Modifier
            .fillMaxSize()
            .shimmer(isEnabled, appShimmerConfiguration)
    ) {
    }
}
