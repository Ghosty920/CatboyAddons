import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.architectury.pack200.java.Pack200Adapter
import net.fabricmc.loom.task.RemapJarTask

plugins {
    idea
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    //id("com.gradleup.shadow") version "8.3.9"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("gg.essential.loom") version "0.10.0.+"
    id("net.kyori.blossom") version "1.3.1"
    kotlin("jvm") version "2.0.0-Beta1"
    kotlin("plugin.serialization") version "1.5.10"
}

val modname: String by project
val modid: String by project
val version: String by project
group = "im.ghosty"

blossom {
    replaceToken("@NAME@", modname)
    replaceToken("@MODID@", modid)
    replaceToken("@VER@", version)
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://repo.essential.gg/repository/maven-public/")
    maven("https://repo.polyfrost.cc/releases")
}

val shadowImpl by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    shadowImpl("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")
    compileOnly("cc.polyfrost:oneconfig-1.8.9-forge:0.2.0-alpha+")

    runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.1.0")
}

sourceSets.main {
    output.setResourcesDir(file("${buildDir}/classes/kotlin/main"))
}

sourceSets["main"].java.srcDirs("src/main/kotlin")

loom {
    silentMojangMappingsLicense()
    launchConfigs.getByName("client") {
        property("mixin.debug", "true")
        property("asmhelper.verbose", "true")
        arg("--tweakClass", "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
        arg("--mixin", "mixins.${modid}.json")
    }
    runConfigs {
        getByName("client") {
            isIdeConfigGenerated = true
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("mixins.${modid}.json")
    }
    mixin.defaultRefmapName.set("mixins.${modid}.refmap.json")
}

tasks {
    processResources {
        inputs.property("modname", modname)
        inputs.property("modid", modid)
        inputs.property("version", version)

        filesMatching(listOf("mcmod.info", "mixins.${modid}.json")) {
            expand(
                mapOf(
                    "modname" to modname,
                    "modid" to modid,
                    "version" to project.version
                )
            )
        }
        dependsOn(compileJava)
    }
    named<Jar>("jar") {
        manifest.attributes(
            "FMLCorePluginContainsFMLMod" to true,
            "FMLCorePlugin" to "${modid}.forge.FMLLoadingPlugin",
            "ForceLoadAsMod" to true,
            "MixinConfigs" to "mixins.${modid}.json",
            "ModSide" to "CLIENT",
            "TweakClass" to "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker",
            "TweakOrder" to "0"
        )

        dependsOn(shadowJar)
        enabled = false
    }
    named<RemapJarTask>("remapJar") {
        archiveBaseName.set(modname)
        input.set(shadowJar.get().archiveFile)
    }
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(modname)
        archiveClassifier.set("dev")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(shadowImpl)
        mergeServiceFiles()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))
kotlin.jvmToolchain(8)