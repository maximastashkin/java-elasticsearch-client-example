plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
}

group = "ru.rsreu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("co.elastic.clients:elasticsearch-java:8.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("jakarta.json:jakarta.json-api:2.0.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
}

tasks.test {
    useJUnitPlatform()
}