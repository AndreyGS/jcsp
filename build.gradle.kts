plugins {
    id("java")
    id("com.github.jk1.dependency-license-report") version "2.0"
}

group = "io.andreygs"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
    main {
        java {
            exclude("io/andreygs/jcsp/internal/processing/data/type/model/**")
        }
    }
}

repositories {
    mavenCentral()
}

val mockitoAgent: Configuration = configurations.create("mockitoAgent")
val mockitoLib = "org.mockito:mockito-junit-jupiter:5.2.0"
val mockitoInlineLib = "org.mockito:mockito-inline:5.2.0"

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
    testImplementation("org.apache.commons:commons-lang3:3.18.0")
    testImplementation(platform("org.junit:junit-bom:5.14.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.2")
    testImplementation("org.assertj:assertj-core:4.0.0-M1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation(mockitoLib)
    testImplementation(mockitoInlineLib)

    mockitoAgent(mockitoLib) { isTransitive = false }
    mockitoAgent(mockitoInlineLib) { isTransitive = false }
}

tasks.test {
    systemProperty("lib.config.services.custom", "infrastructure/test_config.xml")
    val args = jvmArgs ?: mutableListOf<String>().also { jvmArgs = it }
    args.add("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}
