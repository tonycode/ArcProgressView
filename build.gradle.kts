import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask


plugins {
    alias(libs.plugins.gradle.versions)
}


//region tasks
/* generate gradle-wrapper with `gradle wrapper` */
tasks.wrapper {
    gradleVersion = "8.5"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

fun String.isStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { this.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    return (stableKeyword || regex.matches(this))
}

fun String.isNonStable(): Boolean = isStable().not()

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable() && currentVersion.isStable()
    }

    checkForGradleUpdate = true
    outputFormatter = "html"  // https://github.com/ben-manes/gradle-versions-plugin#report-format
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

tasks.register("showDependencyUpdates") {
    dependsOn("dependencyUpdates")
    doLast {
        exec {
            commandLine(
                "xdg-open",
                "build/dependencyUpdates/report.html"
            )
        }
    }
}
//endregion
