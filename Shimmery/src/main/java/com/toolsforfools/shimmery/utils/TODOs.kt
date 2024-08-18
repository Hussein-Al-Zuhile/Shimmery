package com.toolsforfools.shimmery.utils

// TODO: Add Shimmer composables to be used independently, and will be hidden when loading finishes
// TODO: Adding ShimmerConfiguration providing using Local Providers.
// TODO: Adding shimmer colors modification
// TODO: Adding special layouting to the shimmer (width,height,padding).
// TODO: Adding special shimmering for multiline text.
// TODO: Adding image placeholder type to shimmer type
// TODO: IMPORTANT, Make the [Modifier.shimmer] and [ShimmerContainer] only creates new [ShimmerConfiguration] when the values updates,
//  this can be happen by comparing the values of the builder and the LocalShimmerConfiguration with the exist shimmerConfiguration.
// TODO: Overriding the animationSpec in runtime for [shimmerInContainer] not working currently, try to fix it.