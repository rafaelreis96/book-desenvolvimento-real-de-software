package cap1.model;

@FunctionalInterface
public interface BankTransactionSummarize {

	double summarize(double accumulator, BankTransaction bankTransaction);
	
}
