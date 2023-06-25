package cap1;

import java.util.ArrayList;
import java.util.List;

public class BankStatementProcessor {

	private final List<BankTransaction> bankTransactions;
	
	public BankStatementProcessor(List<BankTransaction> bankTransactions) {
		this.bankTransactions = bankTransactions;
	}

	public double summarizeTransaction(final BankTransactionSummarize bankTransactionSummarize) {
		double result = 0;
		for(final BankTransaction bankTransaction : bankTransactions) {
			result = bankTransactionSummarize.summarize(result, bankTransaction); 
		}
		return result;
	}
	
	public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
		final List<BankTransaction> transactions = new ArrayList<BankTransaction>();
		for(final BankTransaction bankTransaction : bankTransactions) {
			if(bankTransactionFilter.test(bankTransaction)) {
				transactions.add(bankTransaction);
			}
		}
		return transactions;
	}

	public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount) {
		return findTransactions(bankTransactionFilter -> bankTransactionFilter.getAmount() >= amount);
	}
}
