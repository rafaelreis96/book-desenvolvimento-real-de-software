package cap1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cap1.model.BankTransaction;
import cap1.service.BankStatementCSVParser;

class BankStatementCSVParserTest {
	
	private BankStatementCSVParser bankStatementCSVParser;
	
	@BeforeEach
	void init() {
		this.bankStatementCSVParser = new BankStatementCSVParser();
	}

	@Test
	void shouldParseOneCorrectLine() {
		final String line  = "30-01-2017,-50,Tesco";
		
		final BankTransaction result = bankStatementCSVParser.parseFromCSV(line);
		
		final BankTransaction expected = new BankTransaction(LocalDate.of(2017, Month.JANUARY, 30), -50, "Tesco");
		
		final double tolerance = 0.0d;
		
		assertEquals(expected.getDate(), result.getDate());
		assertEquals(expected.getAmount(), result.getAmount(), tolerance);
		assertEquals(expected.getDescription(), result.getDescription());
 	}

}
