import java.util.Iterator;

 /* Además formas de imprimir cada tabla.*/


public class Database {
	private HashTableOpenAddressing<String, PrincipalNode> hash;

	private class PrincipalNode{
		String address;
		AVLTree<AVLNode> avl;
		
		private PrincipalNode(String address){
			this.address=address;
			this.avl= new AVLTree<AVLNode>();
		}
		
		/*private int expensesPerInvoice(int invoiceNumber){
			int total=0;
			if(this.invoiceExists(invoiceNumber)){
				for(int i=0; i<this.avl.get(invoiceNumber).hash.getSize(); i++){
					HashNode node = this.avl.get(invoiceNumber).hash.getValue(i);
						total+=node.expense;
				}
			}
			else{
				System.out.println("Ese número de factura es inválido.");
			}
			return total;
		}*/
		
		private int expensesPerInvoice(int invoiceNumber){
			int total = 0;
			Iterator<HashNode> it = this.avl.get(invoiceNumber).hash.getIteratorValue();
			while(it.hasNext()){
				total += it.next().expense;
			}
			return total;
		}
		
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
		
		private int earningsPerInvoice(int invoiceNumber){
			if(this.invoiceExists(invoiceNumber)){
				return this.avl.get(invoiceNumber).payment-this.expensesPerInvoice(invoiceNumber);
			}
			System.out.println("Ese número de factura no está registrado. ");
			return 0;
		}
		
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
		
		private boolean updatePayment(int invoiceNumber, int payment){
			if(this.invoiceExists(invoiceNumber)){
				this.avl.get(invoiceNumber).payment=payment;
				return true;
			}
			System.out.println("Ese número de factura no existe");
			return false;
		}
		
		private int hashVal(String item, int price){
		    int hashKey= 0;
		    for(int i=0;i<item.length();i++){
		    	hashKey +=(int)item.charAt(i);
		    }
		    return hashKey+price;
		}
		
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
		
		private boolean removeItems(Integer invoiceNumber, String item, Integer amount){
			if(!this.invoiceExists(invoiceNumber)){
				System.out.println("El número de factura es inválido. ");
				return false;
			}
			if(this.avl.get(invoiceNumber).hash.contains(this.hashVal(item, amount))){
				this.avl.get(invoiceNumber).hash.remove(this.hashVal(item, amount));
				return true;
			} 
			return false;
		}
		
		private boolean invoiceExists(int invoiceNumber){
			return this.avl.contains(invoiceNumber);
		}
		
		private boolean insertInvoice(int invoiceNumber, int payment){
			if(!this.avl.contains(invoiceNumber)){
				this.avl.insert(new AVLNode(payment, invoiceNumber), invoiceNumber);
				return true;
			}
			System.out.println("Ya existe ese número de factura. ");
			return false;
		}
		
		private boolean deleteInvoice(int invoiceNumber){
			if(this.invoiceExists(invoiceNumber)){
				this.avl.delete(invoiceNumber);
				return true;
			}
			return false;
		}
		
		public String toString(){
			return this.address;
		}
		
		private class AVLNode{
			int payment,
				invoiceNumber;
			HashTableOpenAddressing<Integer, HashNode> hash;
			
			private AVLNode(Integer payment, Integer invoice){
				this.payment=payment;
				this.invoiceNumber = invoice;
				this.hash= new HashTableOpenAddressing<Integer,HashNode>();
			}
		}
		private class HashNode{
			String item;
			int expense;
			
			private HashNode(String item, int expense){
				this.item=item;
				this.expense=expense;
			}
		}
	}
	
	public Database(){
		this.hash = new HashTableOpenAddressing<>();
	}
	
	public boolean insertNewPerson(String name, String address){
		if(!this.personExists(name)){
			this.hash.add(name, new PrincipalNode(address));
			return true;
		}
		System.out.println("Ese nombre ya está registrado.");
		return false;
	}
	
	public void insertInvoice(String name, int invoiceNumber, int payment){
		if(this.personExists(name)){
			this.hash.getValue(name).insertInvoice(invoiceNumber, payment);
		}
	}
	
	public void insertItem(String name, int invoiceNumber, String item, int expense){
		if(this.personExists(name)){
			this.hash.getValue(name).insertItems(invoiceNumber, item, expense);
		}
		else{
			System.out.println("Ese usuario no está registrado.");
		}
	}
	
	public boolean updateAddress(String name, String address){
		if(this.personExists(name)){
			this.hash.getValue(name).address=address;
			return true;
		}
		System.out.println("Ese nombre no está registrado.");
		return false;
	}
	
	public boolean updatePayment(String name, int invoiceNumber, int payment){
		if(this.personExists(name)){
			this.hash.getValue(name).updatePayment(invoiceNumber, payment);
			return true;
		}
		else{
			System.out.println("Esa persona no está registrada. ");
			return false;
		}
	}

	public boolean updatePersonName(String name, String newName){
		if(this.personExists(name)){
			PrincipalNode newNode = this.hash.getValue(name);
			this.hash.remove(name);
			this.hash.add(newName, newNode);
			return true;
		}
		return false;
	}
	
	public boolean personExists(String name){
		return this.hash.contains(name);
	}
	
	public boolean deletePerson(String name){
		if(this.personExists(name)){
			this.hash.remove(name);
			return true;
		}
		else{
			System.out.println("Ese nombre no está registrado. ");
			return false;
		}
	}
	
	public String getAddress(String name){
		if(this.personExists(name)){
			return this.hash.getValue(name).address;
		}
		return "Ese nombre no está registrado.";
	}
	
	public void removeItems(String name, int invoiceNumber, String item, int expense){
		if(this.personExists(name)){
			this.hash.getValue(name).removeItems(invoiceNumber, item, expense);
		}
		else{
			System.out.println("Ese usuario no está registrado. ");
		}
	}
	
	public void removeInvoices(String name, int invoiceNumber){
		if(this.personExists(name)){
			this.hash.getValue(name).deleteInvoice(invoiceNumber);
		}
		else{
			System.out.println("El usuario no está registrado. ");
		}
	}
	
	public int getExpensesPerInvoice(String name, int invoiceNumber){
		if(this.personExists(name)){
			return this.hash.getValue(name).expensesPerInvoice(invoiceNumber);
		}
		System.out.println("Ese usuario no está registrado.");
		return 0;
	}
	
	public int getPayment(String name, int invoiceNumber){
		if(this.personExists(name)){
			return this.hash.getValue(name).avl.get(invoiceNumber).payment;
		}
		System.out.println("Ese usuario no está registrado. ");
		return 0;
	}
	
	public void updateItems(String name, Integer invoiceNumber, String oldItem, Integer oldAmount, String newItem, Integer newAmount){
		this.hash.getValue(name).updateItems(invoiceNumber, oldItem, oldAmount, newItem, newAmount);
	}
	
	public void outputTableOne(){
		this.hash.output();
	}
	
	public void TableOne(){
		StringBuilder sb = new StringBuilder("|         Name         |        Address      | \n");
		sb.append("---------------------------------\n");
		/*
		 * Imprimir toda la tabla
		*/
	}
	
	public int totalPayments(String name){
		return this.hash.getValue(name).totalPayments();
	}
	
	public int totalExpenses(String name){
		return this.hash.getValue(name).totalExpenses();
	}
	
	public int totalEarnings(String name){
		return this.hash.getValue(name).totalEarnings();
	}
	
	public int compareExpenses(String name1, String name2){
		return Math.abs(this.totalExpenses(name1)-this.totalExpenses(name2));
	}

	public static void main(String[] args){
		Database db = new Database();
		db.insertNewPerson("Ana Olvera","La Estancia #13");
		db.outputTableOne();
		
		db.insertInvoice("Ana Olvera", 010, 100);
		db.insertInvoice("Ana Olvera", 011, 100);
		db.insertInvoice("Ana Olvera", 012, 100);
		db.insertInvoice("Ana Olvera", 013, 100);
		db.insertItem("Ana Olvera", 010, "Beer", 30);
		db.insertItem("Ana Olvera", 010, "Water", 20);
		db.insertItem("Ana Olvera", 011, "Cookies", 10);
		db.insertItem("Ana Olvera", 012, "Berenjena", 5);
		db.updatePersonName("Ana Olvera", "Anita");
		db.updateItems("Anita", 010, "Beer", 30, "Tequila", 100);
		System.out.println(db.totalPayments("Anita"));
		System.out.println(db.totalExpenses("Anita"));
		System.out.println(db.totalEarnings("Anita"));
		
		db.insertNewPerson("Pedrin", "Su casa");
		db.insertInvoice("Pedrin", 014, 110);
		db.insertInvoice("Pedrin", 015, 110);
		db.insertItem("Pedrin", 014, "Beer", 30);
		db.insertItem("Pedrin", 015, "Water", 20);
		
		System.out.println(db.compareExpenses("Anita", "Pedrin"));
		db.outputTableOne();
	}

}
