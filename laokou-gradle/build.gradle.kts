plugins {}

allprojects {
	repositories {
		maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
		maven("https://repo.spring.io/release")
		mavenCentral()
	}
}
