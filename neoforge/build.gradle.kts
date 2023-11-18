architectury {
    neoForge()
}

dependencies {
    val neoforgeVersion: String by project
    neoForge(group = "net.neoforged", name = "neoforge", version = neoforgeVersion)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to version)
    }
}