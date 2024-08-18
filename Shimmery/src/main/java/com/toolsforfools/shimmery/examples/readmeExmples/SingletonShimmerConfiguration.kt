package com.toolsforfools.shimmery.examples.readmeExmples

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.toolsforfools.shimmery.shimmerConfiguration.LocalShimmerConfiguration
import com.toolsforfools.shimmery.shimmerConfiguration.shimmerConfiguration

@Composable
private fun SingletonExample() {
    CompositionLocalProvider(
        LocalShimmerConfiguration provides shimmerConfiguration {
            // Some configuration updates
        }) {
    }
}
