package cap1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BankStatementCSVParser {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public BankTransaction parseFromCSV(final String line) {
		final String[] columns = line.split(",");
	
		final LocalDate date = LocalDate.parse(columns[0],DATE_FORMATTER);
		final double amount = Double.parseDouble(columns[1]);
		final String description = columns[2];
		
		return new BankTransaction(date, amount, description);
	}
	
	public List<BankTransaction> parseLinesFromCSV(final List<String> lines) {
		final List<BankTransaction> bankTransaction = new ArrayList<BankTransaction>();
		for(final String line : lines) {
			bankTransaction.add(parseFromCSV(line));
		}
		
		return bankTransaction;
	}
}
