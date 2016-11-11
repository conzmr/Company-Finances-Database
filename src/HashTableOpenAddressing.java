import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTableOpenAddressing<Key, Value> {
	private int size;
	private int capacity; 
	private double loadFactor;
	private Node<Key,Value>[] table;
	private final static int DEF_CAPACITY = 13;
	public static final float DEF_LOAD = 0.75f;

	private static class Node<Key,Value>{
		Key key;
		Value value;

		public Node(Key key, Value value){
			this.key=key;
			this.value=value;
		}
	}

	public HashTableOpenAddressing(){
		this(DEF_CAPACITY, DEF_LOAD);
	}

	public HashTableOpenAddressing(int capacity){
		this(capacity, DEF_LOAD);
	}

	public HashTableOpenAddressing(double loadFactor){
		this(DEF_CAPACITY, loadFactor);
	}

	public HashTableOpenAddressing(int capacity, double loadFactor){
		this.size=0;
		this.capacity=capacity;
		this.table = (Node<Key,Value>[]) new Node[this.capacity];
		this.loadFactor=loadFactor;
	}

	private int hash(Key key){
		return key.hashCode() & 0x7FFFFFFF % this.capacity;
	}

	private int hash(Key k, int tableSize){
		return k.hashCode() & 0x7FFFFFFF % tableSize;
	}

	public Value add(Key k, Value item) {
		int load = (int) (this.loadFactor*this.capacity);
		if(this.size==load){
			this.resize();
		}
		int j = this.hash(k);
		for(int i=0; i<this.capacity; i++){
			if(this.table[(j+i)%this.capacity]==null){
				this.table[(j+i)%this.capacity]=new Node<>(k,item);
				this.size++;
				return null;
			}
			else if(this.table[(j+i)%capacity].key.equals(k)){
				Value antVal = this.table[(j+i)%capacity].value; 
				this.table[(j+i)%capacity].value=item;
				return antVal;
			}
		}
		return null;
	}

	private void add(Node<Key,Value>[] table, Key k, Value item) {
		int j = this.hash(k, table.length);
		for(int i=0; i<table.length; i++){
			if(table[(j+i)%table.length]==null){
				table[(j+i)%table.length]=new Node<>(k,item);
				return;
			}
		}
	}


	public Value remove(Key k) {
		int j = findKeyIndex(k);
		Value val = null;
		if(j!=-1){
			val = this.table[j].value;
			this.table[j]=null;
			this.size--;
			int posChecked=1;
			int i=j;
			while(posChecked<=this.capacity){
				j=(j+1)%this.capacity;
				if(this.table[j]==null){
					break;
				}
				int newk=hash(this.table[j].key);
				if(j<i&&(newk<=j||newk>i)||i<j&&(newk<=j&&newk>i)){
					this.table[j]=this.table[i];
					j=i;
				}
				posChecked++;
			}
		}
		return val;
	}

	public Value getValue(Key k) {
		int j = findKeyIndex(k);
		if(j!=-1){
			return this.table[j].value;
		}
		return null;
	}

	private int findKeyIndex(Key k){
		int j = hash(k);
		int i=0;
		while (this.table[(j+i)%this.capacity]!=null&&i<this.capacity){
			if(this.table[(j+i)%this.capacity].key.equals(k)){
				return (j+i)%this.capacity;
			}
			i++;
		}
		return -1;
	}

	public boolean contains(Key k) {
		return findKeyIndex(k)!=-1;
	}

	public Value getValue(int i){
		return this.table[i].value;
	}


	public boolean isEmpty() {
		return this.size==0;
	}

	public int getSize() {
		return this.size;
	}

	public void clear() {
		for(int i=0; i<this.capacity; i++){
			this.table[i]=null;
		}
		this.size=0;
	}

	public void output(){
		if(this.isEmpty()){
			System.out.println("Hash table is empty.");
		}
		StringBuilder sb = new StringBuilder("[");
		for(int i=0; i<this.capacity; i++){
			if(this.table[i]!=null){
				sb.append("["+this.table[i].key+":"+this.table[i].value+"]");
			}
			else{
				sb.append("[]");
			}
		}
		sb.append("]");
		System.out.println(sb.toString());
	}

	private void resize(){
		Node<Key,Value>[] newTable = (Node<Key,Value>[]) new Node[this.capacity*2];
		int num = 0;
		int i=0;
		while(i<this.capacity&&num<=this.getSize()){
			if(this.table[i]!=null){
				this.add(newTable, this.table[i].key, this.table[i].value);
				num++;
			}
			i++;
		}
		this.table=newTable;
		this.capacity=this.capacity*2;
	}
}
