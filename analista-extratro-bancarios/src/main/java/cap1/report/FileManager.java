package cap1.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {
	
	public void save(String content, Path pathFile) {
		try {
			Files.write(pathFile, content.getBytes());
		} catch (IOException e) {
			System.out.println("Cannot  possible save the file "+pathFile.toString()+". \nError: " + e.getMessage());
		}
	}

}
