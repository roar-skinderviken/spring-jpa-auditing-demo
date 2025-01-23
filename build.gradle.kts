group = "no.mycompany"
version = "0.0.1-SNAPSHOT"

plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.lombok") version "8.11"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2") // fallback db when running the app

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:mysql")
	testRuntimeOnly("org.flywaydb:flyway-mysql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("com.mysql:mysql-connector-j")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
	testImplementation(libs.mockito)
	mockitoAgent(libs.mockito) { isTransitive = false }
}

tasks.test {
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
	useJUnitPlatform()
}