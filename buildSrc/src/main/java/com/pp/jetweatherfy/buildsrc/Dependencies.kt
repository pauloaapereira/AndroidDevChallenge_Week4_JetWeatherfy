package com.pp.jetweatherfy.buildsrc

object Configs {
    const val CompileSdkVersion = 30
    const val MinSdkVersion = 23
    const val TargetSdkVersion = 30

    const val VersionCode = 1
    const val VersionName = "1.0"
}

object ClassPaths {
    const val gradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha12"
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