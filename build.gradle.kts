plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "it.unicam.cs.mpgc.rpg129091"
version = "1.0-SNAPSHOT"

val javafxVersion = "25"

repositories {
    mavenCentral()
}

application {
    mainClass.set("it.unicam.cs.mpgc.rpg129091.App")
}

dependencies {
    implementation("org.openjfx:javafx-controls:${javafxVersion}")
    implementation("org.openjfx:javafx-fxml:${javafxVersion}")


    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

javafx {
    version = "25"
    modules = listOf("javafx.controls", "javafx.fxml")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.test {
    useJUnitPlatform()
}
