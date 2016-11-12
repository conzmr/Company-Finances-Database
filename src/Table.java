import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Table extends JPanel{
	
	String[] columnNames;
	Object[][] data;
	JTable table;
	JScrollPane scrollPane;
	StringBuilder output;
	
	public Table(String[] names, Object[][] data){
		this.output = new StringBuilder();
		this.columnNames = names;
		this.data = data;
		this.table = new JTable(this.data, this.columnNames);
		this.table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        this.table.setFillsViewportHeight(true);
        this.scrollPane = new JScrollPane(this.table);
		this.add(this.scrollPane);
		this.table.setCellSelectionEnabled(true);
		this.table.getSelectionModel().addListSelectionListener(new CellListener());
		//this.table.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
	}
	
	private void outputSelection() {
		System.out.println(this.table.getSelectedRow());
		System.out.println(this.table.getSelectedColumn());
    }
	
	private class CellListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            outputSelection();
            System.out.println(output);
        }
	}
}
