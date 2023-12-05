plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}

configurations {
    create("customConfiguration")
}

sourceSets {
    create("customSourceSet") {
        let { customSourceSet ->
            customSourceSet.java {
                srcDir("src/custom/java")
            }
            tasks.named<Jar>("jar").configure {
                from(customSourceSet.output)
            }
        }
    }
}