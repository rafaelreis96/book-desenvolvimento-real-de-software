package cap1;

@FunctionalInterface
public interface BankTransactionFilter {

	boolean test(BankTransaction bankTransaction);

}
