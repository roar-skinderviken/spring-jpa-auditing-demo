group = "no.mycompany"
version = "0.0.1-SNAPSHOT"

plugins {
    java
    alias(libs.plugins.springframework.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.freefair.lombok)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2") // fallback db when running the app

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:mysql")
    testRuntimeOnly("org.flywaydb:flyway-mysql")
    testRuntimeOnly("com.mysql:mysql-connector-j")
}

// https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    testImplementation(libs.mockito)
    mockitoAgent(libs.mockito) { isTransitive = false }
}

tasks.test {
    jvmArgs(
        "-javaagent:${mockitoAgent.asPath}",
        "-Xshare:off"
    )
    useJUnitPlatform()
}