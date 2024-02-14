package com.toolsforfools.shimmery

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.google.android.material.shape.ShapePath
import com.toolsforfools.shimmery.shimmerableContainer.ShimmerContainer
import com.toolsforfools.shimmery.shimmerableContainer.shimmerInContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShimmerConfigurationExample() {

    var isShimmeringEnabled by rememberSaveable {
        mutableStateOf(true)
    }
    var shimmerType by remember {
        mutableStateOf(ShimmerType.WITH_ALPHA_AND_GRADIANT)
    }
    var gradiantType by remember {
        mutableStateOf(GradiantType.HORIZONTAL)
    }
    var shape by remember {
        mutableStateOf<Shape>(RoundedCornerShape(16.dp))
    }
    var gradiantAnimationSpec by remember {
        mutableStateOf<DurationBasedAnimationSpec<Float>>(tween(1000))
    }
    var alphaAnimationSpec by remember {
        mutableStateOf<DurationBasedAnimationSpec<Float>>(tween(1000))
    }

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
                    .shimmer(isShimmeringEnabled) {
                        this.shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT
                        this.gradiantType = GradiantType.LINEAR
                        this.shape = RoundedCornerShape(16.dp)
                        this.gradiantAnimationSpec = tween(1000)
                        this.alphaAnimationSpec = tween(1300)
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

        Text(text = "Gradiant Type", style = MaterialTheme.typography.titleMedium)
        Column(Modifier.selectableGroup()) {
            GradiantType.entries.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = it == gradiantType, onClick = { gradiantType = it })
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

        Text(text = "Gradiant Animation Spec", style = MaterialTheme.typography.titleMedium)
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
                        selected = animationSpec == gradiantAnimationSpec,
                        onClick = { gradiantAnimationSpec = animationSpec })
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

@Preview
@Composable
private fun ShimmerConfigurationExamplePreview() {
    ShimmerConfigurationExample()
}

val appShimmerConfiguration by lazy {
    ShimmerConfiguration(
        shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT,
        gradiantType = GradiantType.VERTICAL,
        shape = RoundedCornerShape(16.dp),
    ).apply {
        alphaAnimationSpec = tween(1000)
        gradiantAnimationSpec = tween(1000)
    }
}

@Composable
private fun ShimmerableComposable(modifier: Modifier = Modifier) {
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

@Composable
private fun AsyncImageShimmeringExample(modifier: Modifier = Modifier) {

    Box {
        ElevatedCard(
            Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .padding(16.dp)
        ) {
            var isLoadingImage by remember {
                mutableStateOf(false)
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(isLoadingImage),
                model = "https://picsum.photos/400/200",
                contentDescription = "Example image",
                onState = { loadingState ->
                    isLoadingImage = loadingState is AsyncImagePainter.State.Loading
                }
            )
        }
    }
}

@Preview
@Composable
private fun AsyncImageShimmeringExamplePreview() {
    AsyncImageShimmeringExample()
}

@PreviewScreenSizes
@Composable
private fun ShimmerContainerPreview() {
    Surface {
        ShimmerContainer(enabled = true, shimmerConfiguration = shimmerConfiguration {
            gradiantType = GradiantType.LINEAR
        }) {
            LazyColumn(content = {
                items(10) {
                    ShimmerItemPreview(enabled = true, animationSpec = tween(1000))
                }
            })
        }
    }
}

@Composable
private fun ShimmerItemPreview(
    enabled: Boolean,
    animationSpec: DurationBasedAnimationSpec<Float>
) {
    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.image_nature),
            contentDescription = "Example image",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .shimmerInContainer(enabled),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Hello world",
                Modifier
                    .fillMaxWidth()
                    .shimmerInContainer(enabled)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "This is the long description",
                Modifier
                    .fillMaxWidth()
                    .shimmerInContainer(enabled)
            )
        }
    }
}
