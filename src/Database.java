/*
 * Falta método que compare dos usuario
 * Y métodos totalPayments y totalExpenses
 * Además formas de imprimir cada tabla.
 * */


public class Database {
	private HashTableOpenAddressing<String, PrincipalNode> hash;

	private class PrincipalNode{
		String address;
		AVLTree<AVLNode> avl;
		
		private PrincipalNode(String address){
			this.address=address;
			this.avl= new AVLTree<>();
		}
		
		private int expensesPerInvoice(int invoiceNumber){
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
		}
		
		private void updateItems(Integer invoiceNumber, String oldItem, Integer oldAmount, String newItem){
			//Modificar cualquiera de los dos pero no se pueden modificar las keys. 
		}
		
		//Para totalPayments y total expenses necesito recorrer el avltree para sumar todos los payments 
		//de cada nodo o a su vez obteniendo cada nodo correr el método de total expenses y así.
		
		private int totalPayments(){
			int total=0;
			if(this.avl.isEmpty()){
				System.out.println("Este usuario no tienen pagos registrados.");
			}
			else{
				//Recorrer el árbol y sumar el payment de cada AVLNode a total.
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
			if(this.avl.contains(invoiceNumber)){
				this.avl.insert(new AVLNode(payment), invoiceNumber);
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
			int payment;
			HashTableOpenAddressing<Integer, HashNode> hash;
			
			private AVLNode(Integer payment){
				this.payment=payment;
				this.hash= new HashTableOpenAddressing<>();
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
	/*
	public boolean updatePersonName(String name){
		if(this.personExists(name)){
			//Modificar la key, que se rehashee pero no se pierda nada
		}
	}
	*/
	
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
	
	public void outputTableOne(){
		this.hash.output();
	}
	
	public void TableOne(){
		StringBuilder sb = new StringBuilder("|         Name         |        Address      | \n");
		sb.append("---------------------------------\n");
		/*
		 * Imprimir toda la tabla
		 * Checar iteradores key y value de HTOA
		*/
	}

	public static void main(String[] args){
		Database db = new Database();
		db.insertNewPerson("Ana Olvera","La Estancia #13");
		System.out.println(db.getAddress("José Olvera"));
		db.outputTableOne();
		
	}

}
