package FinalProject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;

public class Graph{
	public class Edge {
		Vertex destination;
		double cost; 
		/**
		 * Constructor que recibe Vertice v, double costo, el cual inicializa las variables de instancia.
		 * @param destination indica a dónde lleva la arista
		 * @param cost indica el peso de la arista, si es un grafo ponderado
		 */
		public Edge(Vertex destination, double cost){
			this.destination = destination;
			this.cost = cost;
		}
	}
	public class Vertex {
		String name; 
		List<Edge> adjacency; //lista de los vértices que son adyacentes a éste.
		int mark; //marcados
		double distance; //distancia
		Vertex before; //vertice anterior
		/**
		 * Constructor que recibe el nombre e inicializa las variables de instancia, creando
		 * la lista de adyacencia (vacia).
		 * @param name nombre del vértice
		 */
		public Vertex(String name){
			this.name = name;
			adjacency = new ArrayList<Edge>();
		}
		/**
		 * este método lo que hace es establecer el atributo marcado en 0, 
		 * anterior a null y distancia a Grafo.INFINITO (constante de la clase grafo)
		 */
		public void restart(){
			mark = 0;
			before = null;
			distance = Double.MAX_VALUE;
		}
	}
	HashMap<String,Vertex> graph;
	final double maxValue = Double.MAX_VALUE;
	/**
	 * Constructor que inicializa la variable de instancia 
	 * mapaVertices = new HashMap<String,Vertice>();
	 */
	public Graph(){graph = new HashMap<String,Vertex>();}
	/**
	 * Este método verificará si existe en el diccionario el vértice a través de su nombre, 
	 * en caso de que no, se crea el vértice y se agrega al diccionario, 
	 * devuelve el vértice creado o encontrado en el mapa.
	 * @param name
	 * @return
	 */
	public Vertex getVertex(String name){
		Vertex toReturn = this.graph.get(name);
		if(toReturn != null){return toReturn;}
		Vertex toAdd = new Vertex(name);
		this.graph.put(name, toAdd);
		return toAdd;
	}
	/**
	 * Through getVertex and with the strings name an destination, initialize two Vertex (v y w) type variables. 
	 * Once the vertex are identified, add to the list of a adjacency of vertex v, an edge to destination w and the cost.
	 * @param origin
	 * @param destination
	 * @param cost
	 */
	public void addEdge(String origin, String destination, double cost){
		Vertex v = this.getVertex(origin);
		Vertex w = this.getVertex(destination);
		v.adjacency.add(new Edge(w,cost));
		w.adjacency.add(new Edge(v,cost));
	}
	public void changeEdge(String origin, String destination, double cost){
		Vertex v = this.getVertex(origin);
		Vertex w = this.getVertex(destination);
		int size = v.adjacency.size();
		for(int i = 0; i < size; i++){
			if(v.adjacency.get(i).destination.equals(w)){
				v.adjacency.get(i).cost = cost;
				break;
			}
		}
		this.addEdge(origin, destination, cost);
	}
	
	/**
	 * Debe devolver el String que contiene el recorrido en anchura del grafo, 
	 * empezando por el vértice origen. Sigue el pseudocódigo de la presentación de clase.
	 * @param origin
	 * @return
	 */
	public String breadth(String origin){
		String output = "";
		Vertex s = this.getVertex(origin);
		for(Map.Entry<String, Vertex> u: this.graph.entrySet()){
			Vertex vertex = u.getValue();
			vertex.mark = 1;
			vertex.before = null;
			vertex.distance = this.maxValue;
		}
		s.mark = 0;
		s.distance = 0;
		s.before = null;
		Queue<Vertex> q = new LinkedList<Vertex>();
		q.add(s);
		while(!q.isEmpty()){
			Vertex u = q.poll();
			output+=u.name;
			int size = u.adjacency.size();
			for(int i = 0;i < size; i++){
				Vertex v = u.adjacency.get(i).destination;
				if(v.mark == 1){
					v.mark = 0;
					v.distance = u.distance +1;
					v.before = u;
					q.add(v);
				}
			}
			u.mark = -1;
		}
		return output;
	}
	public void restart(){
		for(Vertex v: graph.values()){
			v.restart();
		}
	}
	/**
	 * Debe devolver el String que contiene el recorrido en profundidad del grafo, 
	 * empezando por el vértice origen. Sigue el pseudocódigo de la presentación de clase.
	 * @param origin
	 * @return
	 */
	public String depth(String origin){
		Vertex string = this.getVertex(origin);
		if (string != null){
			restart();
			int time = 0;
			return depthVisit(time, string);
		}
		return "";
	}
	public String depthVisit(int time, Vertex vertex){
		String output = "";
		time = time+1;
		vertex.distance = time;
		vertex.mark = 1;
		int size = vertex.adjacency.size();
		for(int i = 0;i < size; i++){
			Vertex v = vertex.adjacency.get(i).destination;
			if(v.mark == 0){
				v.before = vertex;
				output+=depthVisit(time,v);
			}
		}
		vertex.mark = 2;
		output+=vertex.name;
		time = time+1;
		vertex.distance = time;
		return output;
	}
}

