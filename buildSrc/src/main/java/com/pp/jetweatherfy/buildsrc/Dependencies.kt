package com.pp.jetweatherfy.buildsrc

object Configs {
    const val CompileSdkVersion = 30
    const val MinSdkVersion = 23
    const val TargetSdkVersion = 30

    const val VersionCode = 1
    const val VersionName = "1.0"
}

object ClassPaths {
    const val gradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha14"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31"
}

object Compose {
    const val composeVersion = "1.0.0-beta03"

    const val animation = "androidx.compose.animation:animation:$composeVersion"
    const val iconsExtended = "androidx.compose.material:material-icons-extended:$composeVersion"
    const val material = "androidx.compose.material:material:$composeVersion"
    const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val uiUtil = "androidx.compose.ui:ui-util:$composeVersion"
    const val uiTest = "androidx.compose.ui:ui-test-junit4:$composeVersion"
    const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha05"
    const val navigationCompose = "androidx.navigation:navigation-compose:1.0.0-alpha09"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha01"
}

object Tests {
    private const val junitVersion = "4.13.2"
    private const val junitKtx = "1.1.2"

    const val junit = "junit:junit:$junitVersion"
    const val junitKotlin = "androidx.test.ext:junit-ktx:$junitKtx"
}

object Core {
    const val androidXCore = "androidx.core:core-ktx:1.3.2"
    const val appCompat = "androidx.appcompat:appcompat:1.3.0-rc01"
    const val material = "com.google.android.material:material:1.3.0"
}

object Libs {

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31"
    }

    object Coroutines {
        private const val version = "1.4.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object DateTime {
        const val jodaTime = "net.danlew:android.joda:2.10.9.1"
    }

    object Lottie {
        const val lottie = "com.airbnb.android:lottie-compose:1.0.0-alpha07-SNAPSHOT"
    }

    object Accompanist {
        const val insets = "dev.chrisbanes.accompanist:accompanist-insets:0.6.2"
    }

    object GoogleLocation {
        const val location = "com.google.android.gms:play-services-location:18.0.0"
    }

    object Hilt {
        private const val version = "2.33-beta"
        const val library = "com.google.dagger:hilt-android:$version"
        const val googleAndroidCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val googleCompiler = "com.google.dagger:hilt-compiler:$version"
        const val testing = "com.google.dagger:hilt-android-testing:$version"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

}
