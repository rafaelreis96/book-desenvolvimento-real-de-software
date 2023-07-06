package cap1.service;

import java.util.List;

import cap1.model.BankTransaction;
import cap1.model.BankTransactionSummarize;
import cap1.model.SummaryStatistics;

public class SummaryStatisticsCalculate {
	
	final BankStatementProcessor bankStatementProcessor;

	public SummaryStatisticsCalculate(List<BankTransaction> bankTransactions ) {
		this.bankStatementProcessor = new BankStatementProcessor(bankTransactions);
	}
	
	public SummaryStatistics calculate() {
		
		final BankTransactionSummarize sum = new BankTransactionSummarize() {
			
			@Override
			public double summarize(double accumulator, BankTransaction bankTransaction) {
				return accumulator + bankTransaction.getAmount();
			}
		};
		
		final BankTransactionSummarize max = new BankTransactionSummarize() {
			
			@Override
			public double summarize(double accumulator, BankTransaction bankTransaction){
				if( bankTransaction.getAmount() > accumulator) {
					return bankTransaction.getAmount();
				}
				return accumulator;
			}
		};
		
		final BankTransactionSummarize min = new BankTransactionSummarize() {
			
			@Override
			public double summarize(double accumulator, BankTransaction bankTransaction) {
				if(bankTransaction.getAmount() < accumulator) {
					return bankTransaction.getAmount();
				}
				return accumulator;
			}
		};
		
		double resultSum = bankStatementProcessor.summarizeTransaction(sum);
		double resultMax = bankStatementProcessor.summarizeTransaction(max);
		double resultMin = bankStatementProcessor.summarizeTransaction(min);
		double resultAverage =  resultSum / bankStatementProcessor.quantityTransactions();
		
		final SummaryStatistics  summaryStatistcs = new SummaryStatistics(resultSum, resultMax, resultMin, resultAverage);
		
		return summaryStatistcs;
	}
	
	
}
