package cap1.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum TypeFile {
	HTML("Report", "html");

	private String name;
	private String extension;

	private TypeFile(String name, String extension) {
		this.name = name;
		this.extension = extension;
	}
	
	public String getName() {
		return name;
	}

	public String getExtension() {
		return extension;
	}
	
	public String getNameWithDataTime() {
		return name + " "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
	}

}
