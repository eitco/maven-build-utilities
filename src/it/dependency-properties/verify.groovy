import java.nio.charset.StandardCharsets
import java.nio.file.Files



File propertyInfo = new File(new File("$basedir"), 'target/properties.txt')

assert propertyInfo.isFile()

String fileContent = new String(Files.readAllBytes(propertyInfo.toPath()), StandardCharsets.UTF_8)

assert fileContent.contains('maven.dependency.org.springframework.boot:spring-boot-starter:jar.version = 3.2.5')
assert fileContent.contains('maven.dependency.com.fasterxml.jackson.core:jackson-annotations:jar.version = 2.17.1')
assert fileContent.contains('maven.dependency.com.google.guava:guava:jar.version = 25.1-jre')
assert fileContent.contains('maven.dependency.org.springframework:spring-core:jar.version = 6.1.6')

