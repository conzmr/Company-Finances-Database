import java.util.NoSuchElementException;

public class AVLTree<E>{
	AVLNode<E> root;
	private static final int ALLOWED_IMBALANCE = 1;
	
	public AVLTree(){
		this.root = null;
	}

	public String inOrder(){
		if(this.root != null){
			return inOrder(this.root);
		}
		else{
			return "";
		}
	}
	
	public boolean isEmpty(){
		return this.root==null;
	}
	
	public boolean contains(Integer key){
		if(!this.isEmpty()){
			AVLNode<E> nodo = this.root; 
			int cmp; 
			while(nodo!=null){
				cmp = key.compareTo(nodo.key);
				if(cmp==0){
					return true;
				}
				else if(cmp>0){
					nodo = nodo.right;
				}
				else if(cmp<0){
					nodo = nodo.left;
				}
			}
		}
		return false;
	}
	
	public E get(Integer key){
		if(this.isEmpty()){
			throw new NoSuchElementException("Tree is empty. ");
		}
		AVLNode<E> nodo = this.root;
		int cmp;
		while(nodo!=null){
			cmp=key.compareTo(nodo.key);
			if(cmp==0){
				return nodo.element;
			}
			else if(cmp<0){
				nodo=nodo.left;
			}
			else{
				nodo=nodo.right;
			}
		}
		return null;
	}
	
	private String inOrder(AVLNode<E> x){
		String output = "";
		if(x.left != null){
			output +=this.inOrder(x.left);
		}
		output += x;
		if(x.right != null){
			output +=this.inOrder(x.right);
		}
		return output;
	}
	
	public void insert(E element, Integer key){
		if(this.root != null){
			this.root = this.insert(element,this.root,key);
		}
		else{
			this.root = new AVLNode<E>(element,null,null,key);
		}
	}
	
	private AVLNode<E> insert(E element, AVLNode<E> node, Integer key){
		if(node == null){
			return new AVLNode<E>(element, null, null,key);
		}
		int cmp = key.compareTo(node.key);
		//Si el element es menor al elemento del Nodo, insertar en leftchild
		if(cmp < 0){
			node.left = insert(element,node.left,key);
		}
		//Si el element es mayor al elemento del Nodo, insertar en rightchild
		else if(cmp > 0){
			node.right = insert(element,node.right,key);
		}
		else;
		//Balancear árbol al final de la inserción
		return balance(node);
	}
	
	public void delete(Integer key) {
		this.root = delete(this.root, key);
	}
	
	private AVLNode<E> delete(AVLNode<E> n, Integer key) {
		// Base case: key doesn't exist.
		if (n == null) return null;
		// If it's in the left sub-tree, go left.
		if (key.compareTo(n.key) < 0) {
			n.left=delete(n.left, key);
			return balance(n); // Deleting may have unbalanced tree.
		} 
		// If it's in the right sub-tree, go right.
		else if (key.compareTo(n.key) > 0) {
			n.right=delete(n.right, key);
			return balance(n); // Deleting may have unbalanced tree.
		} 
		// Else, we found it! Remove n.
		else {
			// 0 children
			if (n.left == null && n.right == null)
				return null;
			// 1 child - guaranteed to be balanced.
			if (n.left == null)
				return n.right;
			if (n.right == null)
				return n.left;
			// 2 children - deleting may have unbalanced tree.
			Integer smallestKey = smallest(n.right);
			n.key=smallestKey;
			n.right = delete(n.right, smallestKey);
			return balance(n);
		}
	}
	
	private Integer smallest(AVLNode<E> n) {
		if (n.left == null)
			return n.key;
		return smallest(n.left);
	}
	
	private AVLNode<E> balance(AVLNode<E> node) {
		if(node == null){
			System.out.println("se agregó: " + node.element);
			return node;
		}
		//Si la altura del izquierdo menos la del derecho es mayor a 1
		if(this.height(node.left) - this.height(node.right)>ALLOWED_IMBALANCE){
			if(this.height(node.left.left)>=this.height(node.left.right)){
				//Cuando el leftChild es mayor, sólo se necesita una rotación simple
				node = rotateWithLeftChild(node);
			}
			else{
				//Cuando el rightChild es mayor, se requiere doble rotación
				node = doubleWithLeftChild(node);
			}
		}
		//Si la altura del derecho menos la del izquierdo es mayor a 1
		else if(this.height(node.right) - this.height(node.left)>ALLOWED_IMBALANCE){
			if(this.height(node.right.right)>=this.height(node.right.left)){
				//Cuando el rightChild es mayor, sólo se necesita una rotación simple
				node = rotateWithRightChild(node);
			}
			else{
				//Cuando el leftChild es mayor, se requiere doble rotación
				node = doubleWithRightChild(node);
			}
		}
		node.height = Math.max(this.height(node.left), this.height(node.right))+1;
		return node;
		
	}
	
	private AVLNode<E> rotateWithLeftChild(AVLNode<E> nodeX){
		//Hace una rotación del nodo hacia la derecha
		AVLNode<E> nodeY = nodeX.left;
		nodeX.left = nodeY.right;
		nodeY.right = nodeX;
		nodeX.height = Math.max(this.height(nodeX.left), this.height(nodeX.right))+1;
		nodeY.height = Math.max(this.height(nodeY.left), nodeX.height)+1;
		return nodeY;
	}
	
	private AVLNode<E> rotateWithRightChild(AVLNode<E> nodeX){
		//Hace una rotación del nodo hacia la izquierda
		AVLNode<E> nodeY = nodeX.right;
		nodeX.right = nodeY.left;
		nodeY.left = nodeX;
		nodeX.height = Math.max(this.height(nodeX.left), this.height(nodeX.right))+1;
		nodeY.height = Math.max(nodeX.height, this.height(nodeY.right))+1;
		return nodeY;
	}
	
	private AVLNode<E> doubleWithLeftChild(AVLNode<E> node){
		//Primero rota a la izquierda el leftchild y luego rota node a la derecha
		node.left = this.rotateWithRightChild(node.left);
		return this.rotateWithLeftChild(node);	
	}

	private AVLNode<E> doubleWithRightChild(AVLNode<E> node){
		//Primero rota a la derecha el rightChild y luego rota node a la izquierda
		node.right = this.rotateWithLeftChild(node.right);
		return this.rotateWithRightChild(node);	
	}
	
	private int height(AVLNode<E> n){
		if(n == null){
			return 0;
		}
		return n.height;
	}
	
	private class AVLNode<E>{
		E element;
		AVLNode<E> left,right;
		int height;
		Integer key;
		
		public AVLNode(E element, AVLNode<E> left, AVLNode<E> right, Integer key){
			this.element = element;
			this.left = left;
			this.right = right;
			this.height = 1;
			this.key = key;
		}
		
		public String toString(){
			return "[" +this.key +":"+ this.element + "-" + this.height + "]";
		}
	}
}
