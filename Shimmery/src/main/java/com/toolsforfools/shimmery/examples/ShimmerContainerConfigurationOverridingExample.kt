package com.toolsforfools.shimmery.examples

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toolsforfools.shimmery.shimmerConfiguration.GradientType
import com.toolsforfools.shimmery.R
import com.toolsforfools.shimmery.shimmerConfiguration.ShimmerType
import com.toolsforfools.shimmery.shimmerConfiguration.shimmerConfiguration
import com.toolsforfools.shimmery.shimmerContainer.ShimmerContainer
import com.toolsforfools.shimmery.shimmerContainer.shimmerInContainer

@Composable
private fun ShimmerContainerConfigurationOverridingExample() {

    ShimmerContainer(
        enabled = true,
        shimmerConfiguration = shimmerConfiguration {
            gradientType = GradientType.LINEAR
            gradientAnimationSpec = tween(500)
        }
    ) {
        Surface {
            Column(Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.image_nature),
                    contentDescription = "Example Image",
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                        .shimmerInContainer()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This is the title", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shimmerInContainer(shimmerConfiguration = shimmerConfiguration {
                            shimmerType = ShimmerType.WITH_ALPHA
                        })
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This is a long description",
                    modifier = Modifier
                        .fillMaxWidth()
                        .shimmerInContainer(shimmerConfiguration = shimmerConfiguration {
                            shimmerType = ShimmerType.WITH_ALPHA
                        })
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    repeat(5) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .aspectRatio(1f)
                                    .shimmerInContainer(
                                        shimmerConfiguration = shimmerConfiguration {
                                            gradientType = GradientType.VERTICAL
                                            shape = CircleShape
                                        })
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "This is an information from 2nd API",
                                Modifier
                                    .fillMaxWidth()
                                    .shimmerInContainer(shimmerConfiguration = shimmerConfiguration {
                                        gradientType = GradientType.VERTICAL
                                    })
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShimmerContainerConfigurationOverridingPreview() {
    ShimmerContainerConfigurationOverridingExample()
}