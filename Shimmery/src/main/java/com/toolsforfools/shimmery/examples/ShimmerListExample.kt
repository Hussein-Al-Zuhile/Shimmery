package com.toolsforfools.shimmery.examples

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.R
import com.toolsforfools.shimmery.shimmerConfiguration.shimmerConfiguration
import com.toolsforfools.shimmery.shimmerContainer.ShimmerContainer
import com.toolsforfools.shimmery.shimmerContainer.shimmerInContainer

@PreviewScreenSizes
@Composable
private fun ShimmerListPreview() {
    Surface {
        ShimmerContainer(enabled = true, shimmerConfiguration = shimmerConfiguration {
            gradientType = GradientType.LINEAR
        }) {
            LazyColumn(content = {
                items(10) {
                    ShimmerItem(enabled = true)
                }
            })
        }
    }
}

@Composable
private fun ShimmerItem(
    enabled: Boolean,
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
