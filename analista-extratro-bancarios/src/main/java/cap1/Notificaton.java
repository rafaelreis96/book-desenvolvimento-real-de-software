package cap1;

import java.util.ArrayList;
import java.util.List;

public class Notificaton {

	private final List<String> errors = new ArrayList<>();
	
	public void addErrors(final String message) {
		errors.add(message);
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public String errorMessage() {
		return errors.toString();
	}
	
	public List<String> getErrors() {
		return errors;
	}
}
