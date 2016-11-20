package ProyectoFinal;

import java.util.Iterator;
import java.util.LinkedList;

public class AddressDirectory<Name, Address> implements DictionaryInterface<Name, Address> {
	/*
	 * Declaración de variables de la clase AddressDirectory
	 */
	private int size,	//tamaño actual de la lista (# de elementos)
				capacity;	// capacidad total de la lista
	protected final static int DEFAULT_SIZE = 50;	//tamaño default para la creación de la lista (tamaño = 50)
	private Node<Name, Address>[] addressDirectoryTable;	//arreglo de Nodos para los Nombres y Direcciones con Key: nombre, Value: addres
	
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
	public AddressDirectory(){
		this.size = 0;
		this.capacity = DEFAULT_SIZE;
		this.addressDirectoryTable = new Node[capacity];
	}

	/**
	 * AddressDirectory() - Constructor con parámetro de capacidad
	 * @param capacity
	 */
	public AddressDirectory(int capacity){
		this.size = 0;
		this.capacity = capacity;
		this.addressDirectoryTable = new Node[capacity];
	}
	/**
	 * add(name, address) - agrega a la lista el nombre y la dirección pasados en el parámetro
	 */
	public Address add(Name name, Address address) {
		int position = hash(name);
		for(Node<Name, Address> n = addressDirectoryTable[position]; n != null; n = n.next){
			if(n.name.equals(name)){
				Address saved = n.address;
				n.address = address;
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
		// TODO Auto-generated method stub
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
		return (this.capacity==0);
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
}
