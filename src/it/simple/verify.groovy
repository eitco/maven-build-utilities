import java.nio.charset.StandardCharsets
import java.nio.file.Files

File log = new File(basedir, "build.log");

File file = new File(basedir, "news.txt");

if (!file.isFile()) {

    throw new FileNotFoundException("file: " + file + "\nbuildlog:\n\t" + new String(Files.readAllBytes(log.toPath()), StandardCharsets.UTF_8).replaceAll("[\r\n]+", "\n\t") + "\n");
}

def fileContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8)

if (!fileContent.equals("please order Nougat!")) {

    throw new IllegalStateException("unexpected file content \"" + fileContent + "\"")
}
