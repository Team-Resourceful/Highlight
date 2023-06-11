import dev.architectury.plugin.ArchitectPluginExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    id("com.teamresourceful.resourcefulgradle")
    id("dev.architectury.loom") version "1.2-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT" apply false
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val minecraftVersion: String by project
    val modLoader = project.name
    val isCommon = modLoader == rootProject.projects.common.name

    base {
        archivesName.set("${rootProject.name}-$modLoader-$minecraftVersion")
    }

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    repositories {
        maven(url = "https://nexus.resourcefulbees.com/repository/maven-public/")
    }

    dependencies {
        val resourcefulLibVersion: String by project

        "minecraft"("::${minecraftVersion}")
        "mappings"(project.the<LoomGradleExtensionAPI>().officialMojangMappings())

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
        val modVersion: String = project.extensions.getByType<VersionCatalogsExtension>()
            .named("libs")
            .findVersion("mod-version").get()
            .requiredVersion
        val minecraftVersion: String by rootProject

        register("discordEmbed") {
            source.set(file("templates/release_embed.json.template"))
            injectedValues.set(mapOf(
                "version" to modVersion,
                "mc_version" to minecraftVersion,
                "forge_version" to "46.0.10",
                "fabric_version" to "0.14.21"
            ))
        }
    }
}