plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.7"
    kotlin("plugin.serialization") version "1.9.21"
}

group = "kipi"
version = "0.0.1"

application {
    mainClass.set("ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "2.3.7"
    val kotlinVersion = "1.9.21"
    val logbackVersion = "1.4.11"
    val exposedVersion = "0.40.1"

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.ktor:ktor-server-call-logging")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.liquibase:liquibase-core:4.20.0")
    implementation("org.postgresql:postgresql:42.5.4")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-apache5:$ktorVersion")

    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.7")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.7")

    implementation("org.apache.commons:commons-email:1.5")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

sourceSets {
    main {
        java.srcDir("src")
        resources.srcDir("resources")
    }
    test {
        java.srcDir("test")
        resources.srcDir("test")
    }
}
