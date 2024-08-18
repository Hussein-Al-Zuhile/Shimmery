package com.toolsforfools.shimmery.examples.readmeExmples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.toolsforfools.shimmery.shimmerIndividual.shimmer
import kotlinx.coroutines.delay

@Composable
private fun AsyncImageShimmeringExample() {

    Box {
        ElevatedCard(
            Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .padding(16.dp)
        ) {
            var isLoadingImage by remember {
                mutableStateOf(true)
            }
            LaunchedEffect(key1 = Unit) {
                delay(3000)
                isLoadingImage = false
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
