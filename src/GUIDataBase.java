import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class GUIDataBase extends JFrame{
	
	private Database db;
	private Table tableNames, tableInvoices, tableExpenses, expensesPerName, paymentsPerName;
	private JTabbedPane tabbedPane;
	private JPanel container;
	
	public GUIDataBase(){
		this.db = new Database();
		this.container = new JPanel();
		db.insertNewPerson("Julia", "Mi casa");
		db.insertNewPerson("Constanza", "Tu casa");
		db.insertNewPerson("Brayan", "Tu cora");
		db.insertInvoice("Julia", 11, 100);
		db.insertInvoice("Julia", 12, 101);
		db.insertInvoice("Julia", 13, 102);
		db.insertItem("Julia", 11, "Beer", 30);
		db.insertItem("Julia", 11, "Beers", 40);
		db.insertItem("Julia", 11, "Beerss", 50);
		db.insertInvoice("Constanza", 14, 103);
		db.insertInvoice("Constanza", 15, 104);
		db.insertInvoice("Brayan", 16, 105);
		
		this.setTableNames();
	    this.setTableInvoices();
	    this.setTableExpenses("Julia", 11);
		
		this.tabbedPane = new JTabbedPane();
		JPanel names = new JPanel();
		names.add(this.tableNames);
		this.tabbedPane.addTab("Table of Names", names);
		
		JPanel invoices = new JPanel();
		invoices.add(this.tableInvoices);
		this.tabbedPane.addTab("Table of Invoices", invoices);
		
		JPanel expenses = new JPanel();
		expenses.add(this.tableExpenses);
		this.tabbedPane.addTab("Table of Expenses", expenses);
		
		this.container.add(this.tabbedPane);
	}
	
	public void setTableNames(){
		String[] columnNames = {"Name", "Address"};
		this.tableNames = new Table(columnNames,db.getTableNames());
		this.tableNames.setOpaque(true);
	}
	
	public void setTableInvoices(){
		String[] columnNames = {"Name", "Invoice Number", "Payment"};
		this.tableInvoices = new Table(columnNames,db.getTableInvoices());
		this.tableInvoices.setOpaque(true);
	}
	
	public void setTableExpenses(String name, Integer invoiceNumber){
		String[] columnNames = {"Invoice Number","Item","Expense"};
		this.tableExpenses = new Table(columnNames,db.getTableExpenses(name, invoiceNumber));
		this.tableExpenses.setOpaque(true);
	}
	
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Tables");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this.tabbedPane);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    public static void main(String[] args) {
    	GUIDataBase gdb = new GUIDataBase();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	gdb.createAndShowGUI();
            }
        });
    }
}
