import dev.architectury.plugin.ArchitectPluginExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    id("com.teamresourceful.resourcefulgradle")
    id("dev.architectury.loom") version "1.4-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT" apply false
}

version = project.extensions.getByType<VersionCatalogsExtension>()
    .named("libs")
    .findVersion("mod-version").get()
    .requiredVersion

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val minecraftVersion: String by project
    val modName = rootProject.name
    val modLoader = project.name
    val isCommon = modLoader == rootProject.projects.common.name
    val loom: LoomGradleExtensionAPI by project
    version = rootProject.version

    base {
        archivesName.set("$modName-$modLoader-$minecraftVersion")
    }

    loom.silentMojangMappingsLicense()

    repositories {
        maven(url = "https://nexus.resourcefulbees.com/repository/maven-public/")
        maven(url = "https://maven.neoforged.net/releases/")
    }

    dependencies {
        val resourcefulLibVersion: String by project

        "minecraft"("::${minecraftVersion}")
        "mappings"(loom.officialMojangMappings())

        val rlib = "modImplementation"(group = "com.teamresourceful.resourcefullib", name = "resourcefullib-$modLoader-$minecraftVersion", version = resourcefulLibVersion)
        if (!isCommon) {
            "include"(rlib)
        }
    }

    java {
        withSourcesJar()
    }

    tasks.jar {
        archiveClassifier.set("dev")
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveClassifier.set(null as String?)
    }

    if (!isCommon) {
        configure<ArchitectPluginExtension> {
            platformSetupLoomIde()
        }

        sourceSets.main {
            val main = this

            rootProject.projects.common.dependencyProject.sourceSets.main {
                main.java.source(java)
                main.resources.source(resources)
            }
        }

        dependencies {
            compileOnly(rootProject.projects.common)
        }
    }
}

resourcefulGradle {
    templates {
        val minecraftVersion: String by rootProject

        register("discordEmbed") {
            source.set(file("templates/release_embed.json.template"))
            injectedValues.set(mapOf(
                "version" to version,
                "mc_version" to minecraftVersion,
                "forge_version" to "20.2.56-beta",
                "fabric_version" to "0.14.24"
            ))
        }
    }
}