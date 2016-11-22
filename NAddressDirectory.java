package FinalProject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

public class NAddressDirectory<Name, Address> implements NAddressDictionaryInterface<Name, Address>{
	/*
	 * Declaración de variables de la clase AddressDirectory
	 */
	private int size,	//tamaño actual de la lista (# de elementos)
	capacity;	// capacidad total de la lista
	protected final static int DEFAULT_SIZE = 50;	//tamaño default para la creación de la lista (tamaño = 50)
	private float loadFactor;
	private int threshold;
	protected final static float DEFAULT_LOAD = 0.75f;
	private Node<Name, Address>[] addressDirectoryTable;	//arreglo de Nodos para los Nombres y Direcciones con Key: nombre, Value: address 
	private NameIterator[] names;
	private AddressIterator[] addresses;
	

	/**
	 * rehash() - hace un rehash para extender la lista al doble de la capacidad +1
	 */
	private void rehash(){
		int position;
		Node<Name, Address>[] temp = (Node<Name, Address>[]) new Object[(2*capacity)+1];
		for(int i = 0; i < capacity; i++){
			position = hash(this.addressDirectoryTable[i].name);
			this.add(temp[position].name, temp[position].address);
		}
		addressDirectoryTable = temp;
	}
	/**
	 * AddressDirectory() - Constructor por default
	 */	
	public NAddressDirectory(){
		this(DEFAULT_SIZE, DEFAULT_LOAD);
	}

	/**
	 * AddressDirectory() - Constructor con parámetro de capacidad y factor de carga
	 * @param capacity
	 */
	public NAddressDirectory(int capacity, float loadFactor){
		if(loadFactor > 1 || loadFactor <= 0){
			throw new IllegalArgumentException("Please insert a loadFactor >0 and <= 1");
		}
		this.size = 0;
		this.capacity = capacity;
		this.loadFactor = loadFactor;
		this.addressDirectoryTable = new Node[capacity];
		this.threshold = (int) (capacity * loadFactor);
	}
	/**
	 * add(name, address) - agrega a la lista el nombre y la dirección pasados en el parámetro
	 */
	public Address add(Name name, Address address) {
		int position = hash(name);
		for(Node<Name, Address> node = addressDirectoryTable[position]; node != null; node = node.next){
			if(node.name.equals(name)){
				Address saved = node.address;
				node.address = address;
				return saved;
			}
		}
		Node<Name, Address> newValue = new Node<Name, Address>(name, address, this.addressDirectoryTable[position]);
		this.addressDirectoryTable[position] = newValue;
		this.size++;
		return null;
	}

	/**
	 * remove(name) - si existe el nombre en la lista, lo remueve, de lo contrario no hace nada 
	 */
	public Address remove(Name name) {
		if(name == null){throw new NullPointerException();}
		Node<Name, Address> values = addressDirectoryTable[hash(name)];
		if(values.name.equals(name)){
			Address address = values.address;
			addressDirectoryTable[hash(name)] = values.next;
			size--;
			return address;			
		}
		while (values.next != null){
			if(values.next.name.equals(name)){
				Address address = values.next.address;
				values.next = values.next.next;
				size--;
				return address;
			}
		}
		return null;

	}

	/**
	 * getValue(name) - regresa la dirección del Nombre dado
	 */
	public Address getValue(Name name) {
		int position = hash(name);
		for(Node<Name,Address> n = addressDirectoryTable[position]; n!=null; n=n.next){
			if(n.name.equals(name)){
				return n.address;
			}
		}
		return null;
	}

	/**
	 * Contains(name) - regresa booleano de si la lista contiene el nombre especificado
	 */
	public boolean contains(Name name) {
		return (this.getValue(name) != null);
	}

	/**
	 * getKeyIterator() - regresa un iterador de los nombres
	 */
	public Iterator<Name> getKeyIterator() {
		return  new NameIterator();
	}

	/**
	 * getValueIterator() - regresa un iterador de las direcciones
	 */
	public Iterator<Address> getValueIterator() {
		return  new AddressIterator();
	}

	/**
	 * isEmpty() - regresa booleano de si la lista está vacía
	 */
	public boolean isEmpty() {
		return (this.size==0);
	}

	/**
	 * getSize() - regresa el tamaño de la lista
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * clear() - limpia la lista completa
	 */
	public void clear() {
		Node<Name, Address>[] temp = (Node<Name, Address>[]) new Object[this.capacity];
		this.addressDirectoryTable = temp;
		this.size = 0;		
	}

	/**
	 * resize() - extiende la capacidad al doble cuando alpha > 0.75
	 */
	public void resize(){
		Node<Name, Address>[] oldAddressDirectoryTable = new Node[capacity];
		System.arraycopy(this.addressDirectoryTable, 0, oldAddressDirectoryTable, 0, capacity);
		this.addressDirectoryTable = new Node[capacity*2];
		this.capacity = this.capacity*2;
		this.size = 0;
		this.threshold = (int) (capacity * loadFactor);
		for(Node<Name, Address> values : oldAddressDirectoryTable){
			for(; values != null; values = values.next){
				add(values.name, values.address);
			}
		}
		oldAddressDirectoryTable = null; //delete old table, hello garbage collector		
	}

	/**
	 * hash - hashes a key into an int
	 * @param name
	 * @return integer for the hashing
	 */
	private int hash(Name name){
		return (name.hashCode() & 0x7FFFFFFF)%capacity;
	}

	/*
	 * AddressIterator - crea iterador para las direcciones
	 */
	private class AddressIterator extends HashIterator<Address>{
		public Address next() {
			return  nextNode().address;
		}		
	}

	/*
	 * NameIterator - crea iterador para los nombres
	 */
	private class NameIterator extends HashIterator<Name>{

		public Name next(){
			return nextNode().name;
		}
	}
	/*
	 * Clase Nodo, cada nodo tiene Key: name, Value: address
	 */
	private static class Node<Name, Address>{
		/*
		 * Definir variables
		 */
		Name name;
		Address address;
		Node<Name, Address> next;

		/**
		 * Node()- asigna las variables a null
		 */
		public Node(){
			this.name = null;
			this.address = null;
			this.next = null;
		}
		/**
		 * Node(Name, Address) - asigna nombre y dirección
		 * @param name
		 * @param address
		 */
		public Node(Name name, Address address){
			this.name = name;
			this.address = address;
			this.next = null;
		}
		/**
		 * Node(Name, Address, Node) - asigna nombre, direccion y el siguiente nodo
		 * @param name
		 * @param address
		 * @param next
		 */
		public Node(Name name, Address address, Node next){
			this.name = name;
			this.address = address;
			this.next = next;
		}
	}

	/*
	 * Clase HashIterator, es el Iterador
	 */
	private abstract class HashIterator<E> implements Iterator<E>{
		/*
		 * Declaration of variables
		 */
		Node<Name, Address> next;
		int index;
		/**
		 * HashIterator() Crear iterador que apunte al elemento de la tabla
		 */
		public HashIterator(){
			if(size>0){
				/*Hacer que next apunte al primer elemento de la tabla
				 * index corresponderá al indice de la tabla
				 */
				for(int i = 0; i < capacity; i++){
					if(addressDirectoryTable[i]!=null){
						this.next = addressDirectoryTable[i];
						this.index = i;
					}
				}
			}
		}
		/**
		 * hasNext() Regresa booleano de si tiene un siguiente o no
		 */
		public boolean hasNext(){
			return next!=null;
		}
		/**
		 * nextNode() regresa el siguiente Nodo (Nombre, Dirección)
		 */
		public Node<Name, Address> nextNode(){
			Node<Name, Address> temp = this.next;
			/*
			 * Next = next.next y si es nulo, moverme en horizontal
			 */
			if(this.hasNext()){
				this.index++;
				this.next = addressDirectoryTable[index];
			}
			return temp;
		}	
	}
	public static void main(String[] args) {
		NAddressDirectory<String, String> database = new NAddressDirectory<>();
		System.out.println(database.isEmpty());
		database.add("Pablo", "CasaPablo");
		database.add("Sebastian", "CasaSebastian");
		database.add("Luis", "CasaLuis");
		System.out.println();
		System.out.println(database.threshold);
		System.out.println();
		database.add("Yorch", "CasaYorch");
		
	}
}