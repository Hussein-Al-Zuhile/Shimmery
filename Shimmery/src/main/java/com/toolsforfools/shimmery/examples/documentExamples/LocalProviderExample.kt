package com.toolsforfools.shimmery.examples.documentExamples

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.toolsforfools.shimmery.shimmerConfiguration.LocalShimmerConfiguration
import com.toolsforfools.shimmery.shimmerConfiguration.shimmerConfiguration

@Composable
private fun LocalProviderExample(content: @Composable () -> Unit) {

    // In the Theme.kt file

    CompositionLocalProvider(LocalShimmerConfiguration provides shimmerConfiguration { }) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaterialTheme.typography,
            content = content
        )
    }

}