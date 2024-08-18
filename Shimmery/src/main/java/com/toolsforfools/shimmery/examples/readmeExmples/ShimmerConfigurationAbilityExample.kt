package com.toolsforfools.shimmery.examples.readmeExmples

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toolsforfools.shimmery.R
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerConfiguration.shimmerConfiguration
import com.toolsforfools.shimmery.shimmerContainer.ShimmerContainer
import com.toolsforfools.shimmery.shimmerContainer.shimmerInContainer
import com.toolsforfools.shimmery.shimmerIndividual.shimmer

@Composable
private fun ShimmerConfigurationExample() {

    var isShimmeringEnabled by rememberSaveable {
        mutableStateOf(true)
    }
    var shimmerType by remember {
        mutableStateOf(ShimmerType.WITH_ALPHA_AND_GRADIANT)
    }
    var gradientType by remember {
        mutableStateOf(GradientType.HORIZONTAL)
    }
    var shape by remember {
        mutableStateOf<Shape>(RoundedCornerShape(16.dp))
    }
    var gradientAnimationSpec by remember {
        mutableStateOf<DurationBasedAnimationSpec<Float>>(tween(1000))
    }
    var alphaAnimationSpec by remember {
        mutableStateOf<DurationBasedAnimationSpec<Float>>(tween(1000))
    }

    ShimmerContainer(enabled = false) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerInContainer(
                            isShimmeringEnabled
                        ) {
                            this.shimmerType = shimmerType
                            this.gradientType = gradientType
                            this.shape = shape
                            this.gradientAnimationSpec = gradientAnimationSpec
                            this.alphaAnimationSpec = alphaAnimationSpec
                        },
                    painter = painterResource(id = R.drawable.image_nature),
                    contentDescription = "Example image"
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isShimmeringEnabled,
                    onCheckedChange = { isShimmeringEnabled = it })
                Text(text = "Is shimmering enabled")
            }

            Text(text = "Shimmer Type", style = MaterialTheme.typography.titleMedium)
            Column(Modifier.selectableGroup()) {
                ShimmerType.entries.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = it == shimmerType, onClick = { shimmerType = it })
                        Text(text = it.name)
                    }
                }
            }

            Text(text = "Gradient Type", style = MaterialTheme.typography.titleMedium)
            Column(Modifier.selectableGroup()) {
                GradientType.entries.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = it == gradientType, onClick = { gradientType = it })
                        Text(text = it.name)
                    }
                }
            }

            Text(text = "Shape", style = MaterialTheme.typography.titleMedium)
            Column(Modifier.selectableGroup()) {
                listOf<Shape>(
                    RoundedCornerShape(16.dp),
                    RectangleShape,
                    CircleShape,
                    // Triangle
                    GenericShape { size, _ ->
                        // 1)
                        moveTo(size.width / 2f, 0f)

                        // 2)
                        lineTo(size.width, size.height)

                        // 3)
                        lineTo(0f, size.height)
                    }
                ).forEachIndexed { index, shapeItem ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = shapeItem == shape, onClick = { shape = shapeItem })
                        val shapeName = when (index) {
                            0 -> "Rounded Rectangle Shape"
                            1 -> "Rectangle Shape"
                            2 -> "Circle Shape"
                            else -> "Generic Shape (Triangle)"
                        }
                        Text(text = shapeName)
                    }
                }
            }

            Text(text = "Gradient Animation Spec", style = MaterialTheme.typography.titleMedium)
            val keyframes = remember {
                keyframes<Float> {
                    0f to 0
                    0.5f to 100
                    0.6 to 200
                }
            }
            Column(Modifier.selectableGroup()) {
                listOf<DurationBasedAnimationSpec<Float>>(
                    tween(1000),
                    tween(3000),
                    snap(2000),
                    keyframes,
                ).forEachIndexed { index, animationSpec ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = animationSpec == gradientAnimationSpec,
                            onClick = { gradientAnimationSpec = animationSpec })
                        val shapeName = when (index) {
                            0 -> "Tween 1000"
                            1 -> "Tween 3000"
                            2 -> "Snap 2000"
                            else -> "Keyframes (Full customizable)"
                        }
                        Text(text = shapeName)
                    }
                }
            }

            Text(text = "Alpha Animation Spec", style = MaterialTheme.typography.titleMedium)
            val alphaKeyframes = remember {
                keyframes<Float> {
                    0f to 0
                    0.5f to 100
                    0.6 to 200
                }
            }
            Column(Modifier.selectableGroup()) {
                listOf<DurationBasedAnimationSpec<Float>>(
                    tween(1000),
                    tween(3000),
                    snap(2000),
                    alphaKeyframes,
                ).forEachIndexed { index, animationSpec ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = animationSpec == alphaAnimationSpec,
                            onClick = { alphaAnimationSpec = animationSpec })
                        val shapeName = when (index) {
                            0 -> "Tween 1000"
                            1 -> "Tween 3000"
                            2 -> "Snap 2000"
                            else -> "Keyframes (Full customizable)"
                        }
                        Text(text = shapeName)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShimmerConfigurationExamplePreview() {
    ShimmerConfigurationExample()
}