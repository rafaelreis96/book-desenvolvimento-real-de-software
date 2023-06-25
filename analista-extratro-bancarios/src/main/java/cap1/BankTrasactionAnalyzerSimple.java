package cap1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.List;

public class BankTrasactionAnalyzerSimple {
	private static final String RESOURCES = "src/main/resources/";

	public static void main(String[] args) throws IOException {
		final String fileName = args[0];
		final Path path = Paths.get(RESOURCES + fileName);
		final List<String> lines = Files.readAllLines(path);
		
		final List<BankTransaction> bankTransactions = new BankStatementCSVParser().parseLinesFromCSV(lines);

		final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);
		
		collectSummary(bankStatementProcessor);
	}
	
	public static void collectSummary(final BankStatementProcessor bankStatementProcessor) {
		final BankTransactionSummarize calculateTotalAmount = new BankTransactionSummarize() {
			public double summarize(double accumulator, BankTransaction bankTransaction) {
				return accumulator + bankTransaction.getAmount();
			}
		};
		
		final BankTransactionSummarize calculateTotalInMonthJanuary = new BankTransactionSummarize() {
			public double summarize(double accumulator, BankTransaction bankTransaction) {
				if(bankTransaction.getDate().getMonth() == Month.JANUARY) {
					return accumulator + bankTransaction.getAmount();
				}
				return accumulator;
			}
		};
		
		final BankTransactionSummarize calculateTotalInMonthFebruary = new BankTransactionSummarize() {
			public double summarize(double accumulator, BankTransaction bankTransaction) {
				if(bankTransaction.getDate().getMonth() == Month.FEBRUARY) {
					return accumulator + bankTransaction.getAmount();
				}
				return accumulator;
			}
		};
		
		final BankTransactionSummarize calculateTotalForCategory = new BankTransactionSummarize() {
			public double summarize(double accumulator, BankTransaction bankTransaction) {
				if(bankTransaction.getDescription().equalsIgnoreCase("Salary")) {
					return accumulator + bankTransaction.getAmount();
				}
				return accumulator;
			}
		};
		
		final List<BankTransaction> transactionInFrebruaryAndExpensive = bankStatementProcessor.findTransactions(new BankTransactionFilter() {
			public boolean test(BankTransaction bankTransaction) {
				return bankTransaction.getDate().getMonth() == Month.FEBRUARY && bankTransaction.getAmount() >= 1000;
			}
		});
		
		System.out.println("The total for all transactions is " + bankStatementProcessor.summarizeTransaction(calculateTotalAmount));
		System.out.println("The total for transactions in January is " + bankStatementProcessor.summarizeTransaction(calculateTotalInMonthJanuary));
		System.out.println("The total for transactions in February is " + bankStatementProcessor.summarizeTransaction(calculateTotalInMonthFebruary));
		System.out.println("The total salary received is " + bankStatementProcessor.summarizeTransaction(calculateTotalForCategory));
		System.out.println("The transactions more expensive in February are " + transactionInFrebruaryAndExpensive);
	}
	
	
}
