plugins {
    alias(libs.plugins.resourceful.gradle)
}

val mcVersion: String by project
val rlibVersion: String by project

tasks.register<Zip>("build") {
    archiveFileName.set("hightlight-${mcVersion}-${version}.jar")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))

    from("src") {
        filesMatching(listOf("**/*.toml", "**/*.json")) {
            expand(mapOf(
                "version" to version,
                "minecraft" to mcVersion,
                "rlib" to rlibVersion,
            ))
        }
    }
}

resourcefulGradle {
    templates {
        register("discord") {
            source = file("templates/embed.json.template")
            injectedValues = mapOf(
                "version" to version,
                "minecraft" to mcVersion,
                "link" to System.getenv("RELEASE_URL"),
            )
        }
    }
}