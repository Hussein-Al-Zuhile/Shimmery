import com.android.ide.common.gradle.RELEASE
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.29.0"
    id("org.jetbrains.dokka") version "1.9.20"
}

kotlin {
    explicitApi()
}

tasks.withType(KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs += listOf("-Xexplicit-api=strict")
}

android {
    namespace = "com.toolsforfools.shimmery"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

mavenPublishing {

    coordinates("io.github.hussein-al-zuhile", "Shimmery", "1.2.1")

    pom {
        name.set("Shimmery")
        description.set("Shimmery is an Android library designed to allow you add configurable shimmering effect to any composable you have, very easily.")
        url.set("https://github.com/Hussein-Al-Zuhile/Shimmery")
        inceptionYear.set("2024")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("Hussein-Al-Zuhile")
                name.set("Hussein Al-Zuhile")
                email.set("hosenzuh@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/Hussein-Al-Zuhile/Shimmery")
        }

        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

        signAllPublications()
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
//    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation(libs.androidx.ui)
    implementation("androidx.compose.ui:ui-graphics:1.6.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("io.coil-kt:coil-compose:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")
}
