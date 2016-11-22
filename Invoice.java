package FinalProject;

public class Invoice{
	String name;
	int invoiceNo;
	int payment;
	ExpenseDirectory expenses;
	
	public Invoice(String name, int invoice, int payment){
		this.name = name;
		this.invoiceNo = invoice;
		this.payment = payment;
		this.expenses = new ExpenseDirectory();
	}
}
