package cap1.gui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import cap1.model.BankTransaction;
import cap1.model.BankTransactionFilter;

public class ExtractTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	private static final String COLUMNS[] = {
			"Data", "Amount", "Category"
	};

	private List<BankTransaction>  table =  new ArrayList<BankTransaction>();
	
	public String getColumnName(int col) {
		return COLUMNS[col];
	}
	
	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public int getRowCount() {
		return table.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return table.get(row).getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			case 1:
				return String.format( "R$ %.2f" , table.get(row).getAmount());
			case 2:
				return table.get(row).getDescription();	
			default:
				return null;
		}
	}
 
	public void setValues(List<BankTransaction> bankTransactions) {
		table.clear();
		table.addAll(bankTransactions);
		fireTableDataChanged();
	} 

}
