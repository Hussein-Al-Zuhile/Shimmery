# Shimmary Android Jetpack Compose Library

## Introduction

Shimmery is an Android library designed to allow you add configurable shimmering effect to any composable you have, very easily.

## Installation
Latest version: [![](https://jitpack.io/v/Hussein-Al-Zuhile/Shimmery.svg)](https://jitpack.io/#Hussein-Al-Zuhile/Shimmery)

To integrate Shimmary into your Android project, follow these steps:

1. Add Jitpack.io repository to your repositories if it's not exist; in your `settings.gradle.kts`:
    ```gradle
    dependencyResolutionManagement {
		 repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		 repositories {
		    mavenCentral()
		    maven { url = URI("https://jitpack.io") }
		 }
    }
    ```
2. Add the following dependency in your app's `build.gradle.kts`:

    ```gradle
    implementation("com.github.Hussein-Al-Zuhile:Shimmery:1.2.1")
    ```
## Usage

Here's a simple example demonstrating what configurations you can edit in Shimmery:

https://github.com/Hussein-Al-Zuhile/Shimmery/assets/43495888/f7ec3bf6-5ac4-4736-897e-021c2598ccf6

### Singleton Shimmer Configuration Hierarchy across the app
The library uses `LocalShimmerConfiguration` as a configuration for the shimmering by default.
You can provide it using `CompositionLocalProvier` Like this, and override it wherever you want in the code:
```kotlin
CompositionLocalProvider(
    LocalShimmerConfiguration provides shimmerConfiguration {
        // Some configuration updates
    }) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
}
```
## Types of Shimmer Applying
You can use it on a group of composables or on an individual composable 
### 1. Applying Shimmering for a group of composables
1. Wrap all the composables you want to add shimmering effect to in a single `ShimmerContainer`
2. Add `Modifier.shimmerInContainer` to the composables you want to add the effect to.
Voila! You have a shimmerable screen.
```kotlin
@Preview
@Composable
fun ShimmerContainerPreview() {
    Box(Modifier.padding(30.dp)) {
        ShimmerContainer(enabled = true) {
            Column {
                Row {
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(2f)
                            .shimmerInContainer()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(2f)
                            .shimmerInContainer()
                    )
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(2f)
                            .shimmerInContainer()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }
    }
}
```

https://github.com/Hussein-Al-Zuhile/Shimmery/assets/43495888/6091a750-485b-4229-be38-d8d377806220

#### Partial Unshimmering (Optional):
Imagine a use-case where the screen has two network APIs, you can cover the two sections in one container and unshimmer the loaded sections independently when each one of them succeed.
You can do this by passing `enabled` parameter to `Modifier.shimmerInContainer` in the sections that override the container behavior.
> **_NOTE:_**  This is also useful in pagination and image loading from a url use-cases.

This example dominstrate this use-case clearly:

https://github.com/Hussein-Al-Zuhile/Shimmery/assets/43495888/9844b1d5-5bd1-4add-a55d-c1ea8102b2a3

To see the code of this example you can find it [here](https://github.com/Hussein-Al-Zuhile/Shimmery/blob/master/Shimmery/src/main/java/com/toolsforfools/shimmery/examples/ShimmerContainerPartialShimmeringExample.kt)
#### Overriding the `ShimmerConfiguration` in container's children (Optional):
You can make some of the children shimmer faster or have a different `GradientType` or `ShimmerType` by passing `ShimmerConfiguration` parameter to `Modifier.shimmerInContainer`

https://github.com/Hussein-Al-Zuhile/Shimmery/assets/43495888/2fbde81d-83a3-4fc2-ae6a-cc79c37d8bee

To see the code of this example you can find it [here](https://github.com/Hussein-Al-Zuhile/Shimmery/blob/master/Shimmery/src/main/java/com/toolsforfools/shimmery/examples/ShimmerContainerConfigurationOverridingExample.kt)
### 2. Applying shimmering for single composable
Simply add `Modifier.shimmer` and you can change the default configuration through the lambda.
```kotlin
Image(
    modifier = Modifier
        .fillMaxSize()
        .shimmer(isShimmeringEnabled) {
            shimmerType = ShimmerType.WITH_ALPHA_AND_GRADIANT
            gradientType = GradientType.LINEAR
            shape = RoundedCornerShape(16.dp)
            gradientAnimationSpec = tween(1000)
            alphaAnimationSpec = tween(1300)
        },
    painter = painterResource(id = R.drawable.image_nature),
    contentDescription = "Example image"
)
```
## Feedback and Stars 🌟
* If you find Shimmary useful, we'd appreciate your feedback and a **star** on GitHub! Your support helps us improve and maintain the library.
* **Give Feedback:** Share your thoughts, report issues, or suggest improvements [here](https://github.com/Hussein-Al-Zuhile/Shimmery/issues/new).

## Contact Information

For any questions or farther information, feel free to contact me:
* **Email:** hosenzuh@gmail.com
* **Phone:** +971589877331
* **Linkedin:** https://www.linkedin.com/in/hussein-al-zuhile-7026011a5
* **Medium:** https://medium.com/@husseinalzuhile
