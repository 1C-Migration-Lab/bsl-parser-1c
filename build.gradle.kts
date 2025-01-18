plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("java")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven {
        url = uri("https://maven.pkg.github.com/1c-syntax/bsl-parser")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.github.1c-syntax:bsl-parser:0.22.0")
    implementation("com.tunnelvisionlabs:antlr4-runtime:4.9.0")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

// Добавляем задачу для вывода зависимостей
tasks.register("showDeps") {
    doLast {
        configurations.compileClasspath.get().files.forEach { file ->
            println(file)
        }
    }
}
