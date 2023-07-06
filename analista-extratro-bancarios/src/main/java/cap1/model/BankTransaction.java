package cap1.model;

import java.time.LocalDate;
import java.util.Objects;

public class BankTransaction {
	private final LocalDate date;
	private final double amount;
	private final String description;

	public BankTransaction(LocalDate date, double amount, String description) {
		this.date = date;
		this.amount = amount;
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return "\nBankTransaction [date=" + date + ", amount=" + amount + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, amount, description);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return Double.compare(this.amount, amount) == 0
				&& date.equals(this.date)
				&& description.equals(this.description);
	}
	
	

}
