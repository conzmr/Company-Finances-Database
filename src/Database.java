
public class Database {
	private HashTableOpenAddressing<String, PrincipalNode> hash;
	
	/*
	 * Eliminar items a esa factura
	 * Modificar expenses de los items de esa factura
	 * Obtener total de una factura
	 * Iterador de la hashtable
	 * Se sobreescriben los amounts si es el mismo item
	 * O pongo tres cosas en la hash, la key que sea el string junto con el item a caracter
	 * y luego el string y el item como valor. 
	 * 
	 * Falta el iterador de la hash. 
	 */
	
	private class PrincipalNode{
		String address;
		AVLTree<HashTableOpenAddressing<Integer, HashNode>> avl;
		
		private PrincipalNode(String address){
			this.address=address;
			this.avl= new AVLTree<>();
		}
		
		private void updateItems(Integer invoiceNumber, String oldItem, Integer oldAmount, String newItem){
			//Modificar cualquiera de los dos pero no se pueden modificar las keys. 
		}
		
		private int hashVal(String item, int price){
		    int hashKey= 0;
		    for(int i=0;i<item.length();i++){
		    	hashKey +=(int)item.charAt(i);
		    }
		    return hashKey+price;
		}
		
		private void insertItems(Integer invoiceNumber, String item, Integer amount){
			if(!this.invoiceExists(invoiceNumber)){
				this.avl.insert(new HashTableOpenAddressing<>(), invoiceNumber);
			}
			this.avl.get(invoiceNumber).add(this.hashVal(item, amount), new HashNode(item, amount)); 
		}
		
		private boolean removeItems(Integer invoiceNumber, String item, Integer amount){
			if(!this.invoiceExists(invoiceNumber)){
				System.out.println("El número de factura es inválido. ");
				return false;
			}
			if(this.avl.get(invoiceNumber).contains(this.hashVal(item, amount))){
				this.avl.get(invoiceNumber).remove(this.hashVal(item, amount));
				return true;
			}; 
			return false;
		}
		
		private boolean invoiceExists(int invoiceNumber){
			return this.avl.contains(invoiceNumber);
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
