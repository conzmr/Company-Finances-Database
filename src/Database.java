import java.util.Iterator;
/**
 * Implementation of the Company Finances Database using HashTableOpenAddressing and AVL Tree.
 * @author Constanza Madrigal Reyes 
 * @author Julia Paola Orduño 
 */
public class Database {
	private HashTableOpenAddressing<String, PrincipalNode> hash;

	/**
	 * Nested class, PrincipalNode, which represents the
	 * containers for value of the HashTable elements for the 
	 * Names' table. 
	 * @author Constanza Madrigal Reyes 
	 * @author Julia Paola Orduño 
	 */
	private class PrincipalNode{
		String address;
		AVLTree<AVLNode> avl;
		
		/**
		 * Creates a Node with a String containing 
		 * the address and an AVL Tree.
		 * @param String - address.
		 */
		private PrincipalNode(String address){
			this.address=address;
			this.avl= new AVLTree<AVLNode>();
		}
		
		/**
		 * Calculates the total expenses of an invoice 
		 * contained in the AVLNode. 
		 * @param int - Number of the invoice.
		 * @return int - total expenses. 
		 */
		private int expensesPerInvoice(int invoiceNumber){
			int total = 0;
			Iterator<HashNode> it = this.avl.get(invoiceNumber).hash.getIteratorValue();
			while(it.hasNext()){
				total += it.next().expense;
			}
			return total;
		}
		
		/**
		 * Updates the information of an specific item. 
		 * @param Integer - Number of the invoice.
		 * @param String - Current name of the item.
		 * @param Integer - Current amount of the item.
		 * @param String - New name of the item.
		 * @param Integer New amount of the item.
		 */
		private void updateItems(Integer invoiceNumber, String oldItem, Integer oldAmount, String newItem, Integer newAmount){
			int key = this.hashVal(oldItem, oldAmount);
			HashNode curr = this.avl.get(invoiceNumber).hash.getValue(key);
			if(newAmount != null){
				curr.expense = newAmount;
			}
			if(newItem != null){
				curr.item = newItem;
			}
		}
		
		/**
		 * Calculates the total payments of an employee. 
		 * @return int - total amount of the payments. 
		 */
		private int totalPayments(){
			int total=0;
			if(this.avl.isEmpty()){
				System.out.println("Este usuario no tienen pagos registrados.");
			}
			else{
				Iterator<AVLNode> avlIt = this.avl.getValueIterator();
				while(avlIt.hasNext()){
					total += avlIt.next().payment;
				}
			}
			return total;
		}
		
		/**
		 * Calculates the total expenses of an employee.
		 * @return int - total expenses. 
		 */
		public int totalExpenses(){
			int total=0;
			if(this.avl.isEmpty()){
				System.out.println("Este usuario no tienen pagos registrados.");
			}
			else{
				Iterator<AVLNode> avlIt = this.avl.getValueIterator();
				while(avlIt.hasNext()){
					total += this.expensesPerInvoice(avlIt.next().invoiceNumber);
				}
			}
			return total;
		}
		
		/**
		 * Calculates the total earnings of an specific invoice.  
		 * @param int - Number of the invoice.
		 * @return int - total earnings. 
		 */
		private int earningsPerInvoice(int invoiceNumber){
			if(this.invoiceExists(invoiceNumber)){
				return this.avl.get(invoiceNumber).payment-this.expensesPerInvoice(invoiceNumber);
			}
			System.out.println("Ese número de factura no está registrado. ");
			return 0;
		}
		
		/**
		 * Calculates the total expenses of an employee. 
		 * @return int - total earnings. 
		 */
		private int totalEarnings(){
			int total=0;
			if(this.avl.isEmpty()){
				System.out.println("Este usuario no tienen facturas registradas.");
			}
			else{
				Iterator<AVLNode> avlIt = this.avl.getValueIterator();
				while(avlIt.hasNext()){
					total += this.earningsPerInvoice(avlIt.next().invoiceNumber);
				}
			}
			return total;
		}
		
		/**
		 * Updates the payment amount of an specific invoice. 
		 * @param int - Number of the invoice.
		 * @param int - New amount of the payment.
		 */
		private void updatePayment(int invoiceNumber, int payment){
			if(this.invoiceExists(invoiceNumber)){
				this.avl.get(invoiceNumber).payment=payment;
			}
			else{
				System.out.println("Ese número de factura no existe");
			}
		}
		
		/**
		 * Returns a hash value as a combination of the 
		 * name and price of an item.
		 * @param String - Name of the item.
		 * @param int - Price of the item.
		 * @return int - hash value.  
		 */
		private int hashVal(String item, int price){
		    int hashKey= 0;
		    for(int i=0;i<item.length();i++){
		    	hashKey +=(int)item.charAt(i);
		    }
		    return hashKey+price;
		}
		
		/**
		 * Inserts new item in an specific invoice. 
		 * @param int - Number of the invoice.
		 * @param String - Name of the item.
		 * @param int - Amount.
		 */
		private void insertItems(int invoiceNumber, String item, int amount){
			if(!this.invoiceExists(invoiceNumber)){
				System.out.println("Ese número de factura no existe. ");
			}
			else{
				if(this.avl.get(invoiceNumber).hash.contains(this.hashVal(item, amount))){
					this.avl.get(invoiceNumber).hash.add(this.hashVal(item, amount), new HashNode(item, amount*2));
				}
				else{
					this.avl.get(invoiceNumber).hash.add(this.hashVal(item, amount), new HashNode(item, amount)); 
				}
			}
		}
		
		/**
		 * Removes an specific item of an specific invoice. 
		 * @param Integer - Number of the invoice.
		 * @param String - Name of the item. 
		 * @param Integer - Amount of the item. 
		 */
		private void removeItems(Integer invoiceNumber, String item, Integer amount){
			if(!this.invoiceExists(invoiceNumber)){
				System.out.println("El número de factura es inválido. ");
			}
			if(this.avl.get(invoiceNumber).hash.contains(this.hashVal(item, amount))){
				this.avl.get(invoiceNumber).hash.remove(this.hashVal(item, amount));
			} 
		}
		
		/**
		 * Returns if an invoice number exists.  
		 * @param int - Number of the invoice.
		 * @return boolean - Invoice exists or not. 
		 */
		private boolean invoiceExists(int invoiceNumber){
			return this.avl.contains(invoiceNumber);
		}
		
		/**
		 * Inserts new invoice.   
		 * @param int - Number of the invoice.
		 * @param int - Payment. 
		 */
		private void insertInvoice(int invoiceNumber, int payment){
			if(!this.avl.contains(invoiceNumber)){
				this.avl.insert(new AVLNode(payment, invoiceNumber), invoiceNumber);
			}
			else{
				System.out.println("Ya existe ese número de factura. ");
			}
		}
		
		/**
		 * Deletes an specific invoice. 
		 * @param int - Number of the invoice.
		 */
		private void deleteInvoice(int invoiceNumber){
			if(this.invoiceExists(invoiceNumber)){
				this.avl.delete(invoiceNumber);
			}
		}
		
		/**
		 * Returns the address. 
		 * @return String - Address.
		 */
		public String toString(){
			return this.address;
		}
		
		private int numExpenses(){
			int total = 0;
			Iterator<AVLNode> itAvl = this.avl.getValueIterator();
			while(itAvl.hasNext()){
				total += itAvl.next().hash.getSize();
			}
			return total;
		}		
		private Object[][] getTableExpenses(int invoiceNumber){
			HashTableOpenAddressing<Integer, HashNode> table = this.avl.get(invoiceNumber).hash;
			Iterator<HashNode> itHash = table.getIteratorValue();
			Object[][] data = new Object[table.getSize()][3];
			HashNode next;
			for (int i = 0; i < data.length; i++) {
				next = itHash.next();
				for (int j = 0; j < data.length; j++) {
					data[i][0] = invoiceNumber;
					data[i][1] = next.item;
					data[i][2] = next.expense;
				}
			}
			return data;
		}
		
		private Object[][] getTableTotalExpense(String name){
			Iterator<AVLNode> itAvl = this.avl.getValueIterator();
			Object[][] data = new Object[this.numExpenses()+1][3];
			int i = 0;
			while(itAvl.hasNext()){
				AVLNode node = itAvl.next();
				Iterator<HashNode> itHash = node.hash.getIteratorValue();
				while(itHash.hasNext()){
					name = i==0?name:"";
					PrincipalNode.HashNode hnode = itHash.next();
					data[i][0] = name;
					data[i][1] = hnode.item;
					data[i][2] = ""+hnode.expense;
					i++;
				}
			}
			data[data.length-1][1] = "Total";
			data[data.length-1][2] = ""+this.totalExpenses();
			return data;
		}


		private Object[][] getTableTotalPayments(String name){
			Iterator<AVLNode> itAvl = this.avl.getValueIterator();
			Object[][] data = new Object[this.avl.size()+1][3];
			AVLNode node;
			for (int i = 0; i < data.length-1; i++) {
				node = itAvl.next();
				name = i==0?name:"";
				data[i][0] = name;
				data[i][1] = ""+node.invoiceNumber;
				data[i][2] = ""+node.payment;
			}
			data[data.length-1][1] = "Total";
			data[data.length-1][2] = ""+this.totalPayments();
			return data;
		}
		
		/**
		 * Nested class, AVLNode, which represents the
		 * containers for values of the AVL elements needed
		 * to represent Invoices' table.  
		 * @author Constanza Madrigal Reyes 
		 * @author Julia Paola Orduño 
		 */
		private class AVLNode{
			int payment,
				invoiceNumber;
			HashTableOpenAddressing<Integer, HashNode> hash;
			
			/**
			 * Creates a Node with an Integer payment
			 * and an Integer invoice.
			 * @param Integer - Payment.
			 * @param Integer - Number of the invoice.
			 */
			private AVLNode(Integer payment, Integer invoice){
				this.payment=payment;
				this.invoiceNumber = invoice;
				this.hash= new HashTableOpenAddressing<Integer,HashNode>();
			}
		}
		
		/**
		 * Nested class, HashNode, which represents the
		 * containers for registers of the Expenses' table.  
		 * @author Constanza Madrigal Reyes 
		 * @author Julia Paola Orduño 
		 */
		private class HashNode{
			String item;
			int expense;
			
			/**
			 * Creates a Node with an String item
			 * and an int expense.
			 * @param String - Name of the item.
			 * @param int - Expense.
			 */
			private HashNode(String item, int expense){
				this.item=item;
				this.expense=expense;
			}
		}
	}
	
	/**
	 * Creates a new Database and initialize the outer HashTable. 
	 */
	public Database(){
		this.hash = new HashTableOpenAddressing<>();
	}
	
	/**
	 * Inserts new employee register in the database.  
	 * @param String - Name of the employee.
	 * @param String - Employee address. 
	 */
	public void insertNewPerson(String name, String address){
		if(!this.personExists(name)){
			this.hash.add(name, new PrincipalNode(address));
		}
		else{
			System.out.println("Ese nombre ya está registrado.");
		}
	}
	
	/**
	 * Inserts new invoice register for an specific employee. 
	 * @param String - Name of the employee.
	 * @param int - Number of the invoice.
	 * @param int - Payment.
	 */
	public void insertInvoice(String name, int invoiceNumber, int payment){
		if(this.personExists(name)){
			this.hash.getValue(name).insertInvoice(invoiceNumber, payment);
		}
	}
	
	/**
	 * Inserts new item into an invoice for an specific employee. 
	 * @param String - Name of the employee.
	 * @param int - Number of the invoice.
	 * @param String - Name of the item.
	 * @param int - Expense. 
	 */
	public void insertItem(String name, int invoiceNumber, String item, int expense){
		if(this.personExists(name)){
			this.hash.getValue(name).insertItems(invoiceNumber, item, expense);
		}
		else{
			System.out.println("Ese usuario no está registrado.");
		}
	}
	
	/**
	 * Updates the address of an employee. 
	 * @param String - Name of the employee.
	 * @param String - New address. 
	 */
	public void updateAddress(String name, String address){
		if(this.personExists(name)){
			this.hash.getValue(name).address=address;
		}
		else{
			System.out.println("Ese nombre no está registrado.");
		}
	}
	
	/**
	 * Updates a payment of an invoice of an specific employee. 
	 * @param String - Name of the employee.
	 * @param int - Number of the invoice.
	 * @param int - Payment. 
	 */
	public void updatePayment(String name, int invoiceNumber, int payment){
		if(this.personExists(name)){
			this.hash.getValue(name).updatePayment(invoiceNumber, payment);
		}
		else{
			System.out.println("Esa persona no está registrada. ");
		}
	}
	
	/**
	 * Updates the name of an employee. 
	 * @param String - Current name of the employee.
	 * @param String - New name of the employee.
	 */
	public void updatePersonName(String name, String newName){
		if(this.personExists(name)){
			PrincipalNode newNode = this.hash.getValue(name);
			this.hash.remove(name);
			this.hash.add(newName, newNode);
		}
	}
	
	/**
	 * Returns if an employee exists in the database. 
	 * @param String - Name of the employee.
	 */
	public boolean personExists(String name){
		return this.hash.contains(name);
	}
	
	/**
	 * Deletes an employee from the database. 
	 * @param String - Name of the employee.
	 */
	public void deletePerson(String name){
		if(this.personExists(name)){
			this.hash.remove(name);
		}
		else{
			System.out.println("Ese nombre no está registrado. ");
		}
	}
	
	/**
	 * Returns the address of an employee. 
	 * @param String - Name of the employee.
	 * @return String - Address.
	 */
	public String getAddress(String name){
		if(this.personExists(name)){
			return this.hash.getValue(name).address;
		}
		return "Ese nombre no está registrado.";
	}
	
	/**
	 * Removes if an item from an invoice from an specific employee. 
	 * @param String - Name of the employee.
	 * @param int - Number of the invoice.
	 * @param String - Name of the item.
	 * @param int - Expense.
	 */
	public void deleteItems(String name, int invoiceNumber, String item, int expense){
		if(this.personExists(name)){
			this.hash.getValue(name).removeItems(invoiceNumber, item, expense);
		}
		else{
			System.out.println("Ese usuario no está registrado. ");
		}
	}
	
	/**
	 * Removes an invoice register from an employee. 
	 * @param String - Name of the employee.
	 * @param int - Number of the invoice. 
	 */
	public void deleteInvoices(String name, int invoiceNumber){
		if(this.personExists(name)){
			this.hash.getValue(name).deleteInvoice(invoiceNumber);
		}
		else{
			System.out.println("El usuario no está registrado. ");
		}
	}
	
	/**
	 * Returns the total expenses of an invoice from an employee. 
	 * @param String - Name of the employee.
	 * @param int - Number of the invoice.
	 */
	public int getExpensesPerInvoice(String name, int invoiceNumber){
		if(this.personExists(name)){
			return this.hash.getValue(name).expensesPerInvoice(invoiceNumber);
		}
		System.out.println("Ese usuario no está registrado.");
		return 0;
	}
	
	/**
	 * Returns the payment from an invoice of an employee. 
	 * @param String - Name of the employee.
	 * @param int - Number of the invoice.
	 */
	public int getPayment(String name, int invoiceNumber){
		if(this.personExists(name)){
			return this.hash.getValue(name).avl.get(invoiceNumber).payment;
		}
		System.out.println("Ese usuario no está registrado. ");
		return 0;
	}
	
	/**
	 * Updates the item information of an invoice from an employee. 
	 * @param String - Name of the employee.
	 * @param Integer - Number of the invoice.
	 * @param String - Current name of the item.
	 * @param Integer - Current amount. 
	 * @param String - New name of the item.
	 * @param Integer - New amount of the item.
	 */
	public void updateItems(String name, Integer invoiceNumber, String oldItem, Integer oldAmount, String newItem, Integer newAmount){
		this.hash.getValue(name).updateItems(invoiceNumber, oldItem, oldAmount, newItem, newAmount);
	}
	
	/**
	 * Returns the total of payments of an employee. 
	 * @param String - Name of the employee.
	 */
	public int totalPayments(String name){
		return this.hash.getValue(name).totalPayments();
	}
	
	/**
	 * Returns the total expenses of an employee. 
	 * @param String - Name of the employee.
	 */
	public int totalExpenses(String name){
		return this.hash.getValue(name).totalExpenses();
	}
	
	/**
	 * Returns the total earnings of an employee. 
	 * @param String - Name of the employee.
	 */
	public int totalEarnings(String name){
		return this.hash.getValue(name).totalEarnings();
	}
	
	/**
	 * Returns the difference between the expenses of two employees. 
	 * @param String - Name of the employee one.
	 * @param String - Name of the employee two. 
	 */
	public int compareExpenses(String name1, String name2){
		return Math.abs(this.totalExpenses(name1)-this.totalExpenses(name2));
	}
	
	public String[][] getTableNames(){
		Iterator<String> itName = this.hash.getIteratorKey();
		Iterator<PrincipalNode> itAddress = this.hash.getIteratorValue();
		String[][] data = new String[this.hash.getSize()][2];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < 2; j++) {
				data[i][j] = j==0? itName.next(): itAddress.next().address;
			}
		}
		return data;
	}

	public int numInvoices(){
		int total = 0;
		Iterator<PrincipalNode> itNode = this.hash.getIteratorValue();
		while(itNode.hasNext()){
			total += itNode.next().avl.size();
		}
		return total;
	}


	public Object[][] getTableInvoices(){
		Iterator<String> itName = this.hash.getIteratorKey();
		Iterator<PrincipalNode> itNode = this.hash.getIteratorValue();
		Iterator<PrincipalNode.AVLNode> itAvl;
		String name;
		PrincipalNode node;
		Object[][] data = new Object[this.numInvoices()][3];
		PrincipalNode.AVLNode next;
		int i =0;
		int j = 0;
		while(itName.hasNext()){
			name = itName.next();
			node = itNode.next();
			itAvl = node.avl.getValueIterator();
			while(itAvl.hasNext()){
				next = itAvl.next();
				data[i][0] = name;
				data[i][1] = next.invoiceNumber;
				data[i][2] = next.payment;
				i++;
			}
		}
		return data;
	}

	public Object[][] getTableExpenses(String name, int invoiceNumber){
		return this.hash.getValue(name).getTableExpenses(invoiceNumber);
	}

	public Object[][] getTableTotalExpense(String name){
		return this.hash.getValue(name).getTableTotalExpense(name);
	}

	public Object[][] getTableTotalPayments(String name){
		return this.hash.getValue(name).getTableTotalPayments(name);
	}
}
