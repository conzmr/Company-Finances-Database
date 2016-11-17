import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
		db.insertItem("Julia", 11, "Pisto", 30);
		db.insertItem("Julia", 11, "Comida", 40);
		db.insertItem("Julia", 11, "Agua", 50);
		db.insertInvoice("Constanza", 14, 103);
		db.insertInvoice("Constanza", 15, 104);
		db.insertInvoice("Brayan", 16, 105);

		this.setTableNames();
		this.setTableInvoices();

		this.tabbedPane = new JTabbedPane();
		JPanel names = new JPanel();
		names.add(this.tableNames);
		this.tabbedPane.addTab("Table of Names", names);

		JPanel invoices = new JPanel();
		invoices.add(this.tableInvoices);
		this.tabbedPane.addTab("Table of Invoices", invoices);

		this.container.add(this.tabbedPane);

		this.tableNames.table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent evt) 
			{
				Object oldName = tableNames.currentValue;
				Object newValue = tableNames.table.getValueAt(tableNames.table.getSelectedRow(), tableNames.table.getSelectedColumn()); 
				if(tableNames.table.getSelectedColumn()==0){
					db.updatePersonName(oldName.toString(), newValue.toString());
				}
				else{
					Object name = tableNames.table.getValueAt(tableNames.table.getSelectedRow(), 0);
					db.updateAddress(name.toString(), newValue.toString());
				}
			}
		});

		this.tableInvoices.table.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e) {
				Object name = tableInvoices.table.getValueAt(tableInvoices.table.getSelectedRow(), 0);
				Object invoice = tableInvoices.table.getValueAt(tableInvoices.table.getSelectedRow(), 1);
				Object currPayment = tableInvoices.currentValue;
				Object newPayment = tableInvoices.table.getValueAt(tableInvoices.table.getSelectedRow(), tableInvoices.table.getSelectedColumn());
				try{
					System.out.println(db.updatePayment(name.toString(), (Integer) invoice, (Integer) newPayment));
				}catch(ClassCastException e1){
					JOptionPane.showMessageDialog(tableInvoices.table,"Must be a number");
					tableInvoices.table.setValueAt(currPayment, tableInvoices.table.getSelectedRow(), tableInvoices.table.getSelectedColumn());
				}

			}
		});
	}

	public void setTableNames(){
		String[] columnNames = {"Name", "Address"};
		this.tableNames = new Table(columnNames,db.getTableNames(),0);
		this.tableNames.setOpaque(true);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Right-clic for menu");
		this.tableNames.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
	}

	public void setTableInvoices(){
		String[] columnNames = {"Name", "Invoice Number", "Payment"};
		this.tableInvoices = new Table(columnNames,db.getTableInvoices(),1);
		this.tableInvoices.setOpaque(true);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Right-clic for menu");
		this.tableInvoices.table.getColumnModel().getColumn(1).setCellRenderer(renderer);
	}

	public void setTableExpenses(String name, Integer invoiceNumber){
		String[] columnNames = {"Invoice Number","Item","Expense"};
		this.tableExpenses = new Table(columnNames,db.getTableExpenses(name, invoiceNumber),2);
		this.tableExpenses.setOpaque(true);
	}

	public void setExpensesPerName(String name){
		String[] columnNames = {"Name","Item","Expense"};
		this.expensesPerName = new Table(columnNames,db.getTableTotalExpense(name),3);
		this.expensesPerName.setOpaque(true);
	}

	public void setPaymentsPerName(String name){
		String[] columnNames = {"Name","Invoice","Payment"};
		this.paymentsPerName = new Table(columnNames,db.getTableTotalPayments(name),4);
		this.paymentsPerName.setOpaque(true);
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

	private class Table extends JPanel implements ActionListener{
		int numTable;
		String[] columnNames;
		Object[][] data;
		JTable table;
		JScrollPane scrollPane;
		Object currentValue;
		JPopupMenu popup;
		JMenuItem expenses,payments,items;

		public Table(String[] names, Object[][] data, int nt){
			this.columnNames = names;
			this.numTable = nt;
			this.data = data;
			DefaultTableModel model = new DefaultTableModel(this.data, this.columnNames);
			this.table = new JTable(model){
				public boolean isCellEditable(int row, int column){
					switch(numTable){
					case 1:
						if(column==0||column==1){return false;}
						else{return true;}
					case 2:
						if(column ==0){return false;}
						else{return true;}
					case 3:
						return false;
					case 4:
						return false;
					}
					return true;
				}
			};
			this.table.setPreferredScrollableViewportSize(new Dimension(600, 100));
			this.table.setFillsViewportHeight(true);
			this.scrollPane = new JScrollPane(this.table);
			this.add(this.scrollPane);
			this.table.setCellSelectionEnabled(true);
			this.table.getSelectionModel().addListSelectionListener(new CellListener());
			this.table.addMouseListener( new MouseAdapter()
			{
				public void mouseReleased(MouseEvent e)
				{
					if (e.isPopupTrigger())
					{
						JTable source = (JTable)e.getSource();
						int row = source.rowAtPoint( e.getPoint() );
						int column = source.columnAtPoint( e.getPoint() );

						if (! source.isRowSelected(row))
							source.changeSelection(row, column, false, false);

						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});
		}

		public void setCurrValue(Object val){
			this.currentValue = val;
		}

		private class CellListener implements ListSelectionListener{
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting()) {
					return;
				}
				Object value = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
				setCurrValue(value);
				addPopupMenu();
			}
		}

		public void addPopupMenu(){
			if(this.numTable==0){
				this.popup = new JPopupMenu();
				if(this.table.getSelectedColumn()==0){
					this.expenses = new JMenuItem("Show total expenses");
					this.payments = new JMenuItem("Show total payments");
					this.expenses.addActionListener(this);
					this.payments.addActionListener(this);
					this.popup.add(this.expenses);
					this.popup.add(this.payments);
				}
			}
			else if(this.numTable == 1){
				this.popup = new JPopupMenu();
				if(this.table.getSelectedColumn()==1){
					this.items = new JMenuItem("Show expenses");
					this.items.addActionListener(this);
					this.popup.add(items);
				}
			}
			this.table.setComponentPopupMenu(popup);
		}

		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			JFrame temp = new JFrame();
			Object cell = this.table.getValueAt(this.table.getSelectedRow(), this.table.getSelectedColumn());
			if(item == this.expenses){
				setExpensesPerName(cell.toString());
				temp.setContentPane(expensesPerName);
			}
			else if(item == this.payments){
				setPaymentsPerName(cell.toString());
				temp.setContentPane(paymentsPerName);
			}
			else if(item == this.items){
				Object name = this.table.getValueAt(this.table.getSelectedRow(), 0);
				setTableExpenses(name.toString(),(Integer)cell);
				tableExpenses.table.getModel().addTableModelListener(new TableModelListener(){
					public void tableChanged(TableModelEvent e) {
						Object invoice = tableInvoices.table.getValueAt(tableInvoices.table.getSelectedRow(), 1);
						Object name = tableInvoices.table.getValueAt(tableInvoices.table.getSelectedRow(), 0);
						if(tableExpenses.table.getSelectedColumn()==1){
							Object oldItem = tableExpenses.currentValue;
							Object amount = tableExpenses.table.getValueAt(tableExpenses.table.getSelectedRow(), 2);
							Object newItem = tableExpenses.table.getValueAt(tableExpenses.table.getSelectedRow(), 1);
							System.out.println(db.updateItems(name.toString(), (Integer) invoice, oldItem.toString(), (Integer) amount, newItem.toString(), null));
						}
						else if(tableExpenses.table.getSelectedColumn()==2){
							Object oldAmount = tableExpenses.currentValue;
							Object item = tableExpenses.table.getValueAt(tableExpenses.table.getSelectedRow(), 1);
							Object newAmount = tableExpenses.table.getValueAt(tableExpenses.table.getSelectedRow(), 2);
							System.out.println(db.updateItems(name.toString(), (Integer) invoice, item.toString(), (Integer) oldAmount, null, (Integer) newAmount));
						}
					}
				});
				temp.setContentPane(tableExpenses);
			}
			temp.pack();
			temp.setVisible(true);
			temp.setResizable(false);

		}
	}

}
