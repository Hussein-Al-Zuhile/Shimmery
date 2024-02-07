# Shimmary Android Jetpack Compose Library

## Introduction

Shimmary is an Android library designed to allow you add configurable shimmering effect to any composable you have, very easily.

## Installation
Latest version: [![](https://jitpack.io/v/Hussein-Al-Zuhile/Shimmery.svg)](https://jitpack.io/#Hussein-Al-Zuhile/Shimmery)

To integrate Shimmary into your Android project, follow these steps:

1. Add Jitpack.io repository to your repositories if it's not exist; in your `settings.gradle.kts`:
    ```gradle
    dependencyResolutionManagement {
		 repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		 repositories {
		    mavenCentral()
		    maven { url 'https://jitpack.io' }
		 }
    }
    ```
2. Add the following dependency in your app's `build.gradle.kts`:

    ```gradle
    implementation("com.github.Hussein-Al-Zuhile:Shimmery:1.0.1")
    ```
## Usage

Here's a simple example demonstrating how to use Shimmary in your Android application:

https://github.com/Hussein-Al-Zuhile/Shimmery/assets/43495888/f7ec3bf6-5ac4-4736-897e-021c2598ccf6

### How you can apply shimmering
Simply add the Modifier.shimmer and you can change the default configuration through the lambda.
```kotlin
Image(
    modifier = Modifier
        .fillMaxSize()
        .shimmer(isShimmeringEnabled) {
            shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT
            gradiantType = GradiantType.LINEAR
            shape = RoundedCornerShape(16.dp)
            gradiantAnimationSpec = tween(1000)
            alphaAnimationSpec = tween(1300)
        },
    painter = painterResource(id = R.drawable.image_nature),
    contentDescription = "Example image"
)
```
### Singleton Configuration Support
You can also create your singleton configuration and save it wherever you want to avoid creating multiple configurations if it is the same shimmering for the whole application, create a `ShimmerConfiguration` object and pass it as a second parameter to the modifier.
```kotlin
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
fun ShimmerableComposable(modifier: Modifier = Modifier) {
    var isEnabled by remember {
        mutableStateOf(false)
    }
    Box(
        Modifier
            .fillMaxSize()
            .shimmer(isEnabled, appShimmerConfiguration)) {
    }
}
```

## Feedback and Stars 🌟
* If you find Shimmary useful, we'd appreciate your feedback and a **star** on GitHub! Your support helps us improve and maintain the library.
* **Give Feedback:** Share your thoughts, report issues, or suggest improvements [here](https://github.com/Hussein-Al-Zuhile/Shimmery/issues/new).

## Contact Information

For any questions or farther information, feel free to contact me:
* **Email:** hosenzuh@gmail.com
* **Phone:** +963991039529
* **Linkedin:** https://www.linkedin.com/in/hussein-al-zuhile-7026011a5
