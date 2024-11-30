pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}
	// 这行确保 Gradle 知道在哪里找到 libs.versions.toml
	includeBuild("gradle")
}
