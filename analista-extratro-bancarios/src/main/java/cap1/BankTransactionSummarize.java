package cap1;

@FunctionalInterface
public interface BankTransactionSummarize {

	double summarize(double accumulator, BankTransaction bankTransaction);
	
}
