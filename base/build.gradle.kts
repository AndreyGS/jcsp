plugins {
    id("java")
}

group = "io.andreygs"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val mockitoAgent: Configuration = configurations.create("mockitoAgent")
val mockitoLib = "org.mockito:mockito-junit-jupiter:5.21.0"

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
    testImplementation("org.apache.commons:commons-lang3:3.18.0")
    testImplementation(platform("org.junit:junit-bom:5.14.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation(mockitoLib)
    mockitoAgent(mockitoLib) { isTransitive = false }
}

tasks.test {
    val args = jvmArgs ?: mutableListOf<String>().also { jvmArgs = it }
    args.add("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}