package cap1.model;

@FunctionalInterface
public interface BankTransactionFilter {

	boolean test(BankTransaction bankTransaction);

}
