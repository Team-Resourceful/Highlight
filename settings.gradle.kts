enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "highlight"

pluginManagement {
    repositories {
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://maven.resourcefulbees.com/repository/maven-public/")
        gradlePluginPortal()
    }
}

plugins {
    id("com.teamresourceful.resourcefulsettings") version "0.0.6"
}


include("common")
include("fabric")
include("neoforge")