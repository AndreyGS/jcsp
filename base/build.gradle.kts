plugins {
    id("java")
}

group = "io.andreygs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
    testImplementation("org.apache.commons:commons-lang3:3.18.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.21.0")
    testImplementation(platform("org.junit:junit-bom:5.14.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}