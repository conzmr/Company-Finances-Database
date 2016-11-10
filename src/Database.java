
public class Database {
	private HashTableOpenAddressing<String, PrincipalNode> hash;
	
	/*
	 * Agregar items a esa factura
	 * Eliminar items a esa factura
	 * Modificar expenses de los items de esa factura
	 * Obtener total de una factura
	 * 
	 */
	
	private class PrincipalNode{
		String address;
		AVLTree<HashTableOpenAddressing<String, Integer>> avl;
		
		private PrincipalNode(String address){
			this.address=address;
			this.avl= new AVLTree<>();
		}
		
		private void insertItems(Integer invoiceNumber, String item, Integer amount){
			if(!this.invoiceExists(invoiceNumber)){
				this.avl.insert(new HashTableOpenAddressing<>(), invoiceNumber);
			}
			this.avl.get(invoiceNumber).add(item, amount);
		}
		
		private boolean removeItems(Integer invoiceNumber, String item, Integer amount){
			if(!this.invoiceExists(invoiceNumber)){
				System.out.println("El número de factura es inválido. ");
				return false;
			}
			//eliminar item sólo si el item y la amount coinciden.
			return true;
		}
		
		private boolean invoiceExists(int invoiceNumber){
			return this.avl.contains(invoiceNumber);
		}
		
		private boolean deleteInvoice(int invoiceNumber){
			if(this.invoiceExists(invoiceNumber)){
				//Agregar método remove al AVL
				return true;
			}
			return false;
		}
		
		public String toString(){
			return this.address;
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
	
	public boolean updateAddress(String name, String address){
		if(this.personExists(name)){
			this.hash.getValue(name).address=address;
			return true;
		}
		System.out.println("Ese nombre no está registrado.");
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
	
	public void insertInvoice(String name, int invoiceNumber, String item, int expense){
		PrincipalNode node = this.hash.getValue(name);
		node.insertItems(invoiceNumber, item, expense);
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
		db.deletePerson("Ana Olvera");
		db.outputTableOne();
		
	}

}
