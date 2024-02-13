package com.toolsforfools.shimmery.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.max


internal val Offset.maxOffset
    get() = maxOf(x, y)