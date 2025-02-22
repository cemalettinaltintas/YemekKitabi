// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.navigation.safe.args.gradle.plugin)
        classpath(kotlin("gradle-plugin", version = "1.5.30"))
    }
}