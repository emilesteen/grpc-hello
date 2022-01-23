import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*;

buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.13")
    }
}

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.protobuf") version "0.8.15"
    application
}

group = "me.emilesteenkamp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.grpc:grpc-protobuf:1.43.2")
    implementation("io.grpc:grpc-stub:1.43.2")
    implementation("io.grpc:grpc-netty:1.43.2")
    implementation("io.grpc:grpc-services:1.43.2")
    api("io.grpc:grpc-kotlin-stub:1.2.0")
    testImplementation(kotlin("test"))
}

protobuf {
    protoc{
        artifact = "com.google.protobuf:protoc:3.10.1"
    }
    plugins {
        id("grpc"){
            artifact = "io.grpc:protoc-gen-grpc-java:1.33.1"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:0.1.5"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets.main {
    java.srcDirs(
        "build/generated/source/proto/main/grpc",
        "build/generated/source/proto/main/grpckt",
        "build/generated/source/proto/main/java"
        )
}
