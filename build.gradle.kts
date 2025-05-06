import com.teamresourceful.utils.Platform
import com.teamresourceful.utils.getPlatform

plugins {
    java
    id("maven-publish")
    alias(libs.plugins.resourceful.loom)
    alias(libs.plugins.resourceful.gradle)
}

subprojects {
    apply(plugin = "maven-publish")

    version = rootProject.libs.versions.mod.version.get()

    val platform = getPlatform()

    dependencies {
        if (platform == Platform.COMMON) {
            "api"(rootProject.libs.rlib.common)
        } else if (platform == Platform.FABRIC) {
            "modImplementation"(rootProject.libs.rlib.fabric) {
                "include"(this)
            }
        } else if (platform == Platform.NEOFORGE) {
            "modImplementation"(rootProject.libs.rlib.neoforge) {
                "include"(this)
            }
        }
    }
}


resourcefulGradle {
    templates {
        register("readme") {
            source = file("templates/README.md.template")
            injectedValues = mapOf(
                "version" to libs.versions.mod.version.get(),
                "minecraft" to libs.versions.minecraft.get(),
            )
        }
        register("discord") {
            source = file("templates/embed.json.template")
            injectedValues = mapOf(
                "version" to libs.versions.mod.version.get(),
                "minecraft" to libs.versions.minecraft.get(),
                "neoforge" to libs.versions.neoforge.get(),
                "fabric" to libs.versions.fabric.api.get(),
                "fabric_link" to System.getenv("FABRIC_RELEASE_URL"),
                "neoforge_link" to System.getenv("FORGE_RELEASE_URL"),
            )
        }
    }
}