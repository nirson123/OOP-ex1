package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph , Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<Integer,node_info> nodes;
	private int number_of_nodes = 0;
	private int number_of_edges = 0;
	private int ModeCount = 0;
	
	//***** constructor *****
	
	public WGraph_DS() {
		this.nodes = new HashMap<>();
	}
	
	//***** methods *****

	/**
	 * return the node with the given id
	 * @param key
	 * @return the node with given id, null if does'nt exist
	 */
	@Override
	public node_info getNode(int key) {
		return nodes.get(key);
	}

	/**
	 * Return true iff node1 and node2 are connected
	 * @param node1
	 * @param node2
	 * @return true if connected, false if not(or don't exist). 
	 */
	@Override
	public boolean hasEdge(int node1, int node2) {
		nodeInfo Node1 = (nodeInfo)nodes.get(node1);						//get node1 as node object
		return(Node1 != null && Node1.neighbors.containsKey(node2));		//check if node1 exist in the graph and have node2 as neighbor
	}

	/**
	 * Return the weight of the edge node1-node2
	 * @param node1
	 * @param node2
	 * @return the weight of the edge , -1 if not connected
	 */
	@Override
	public double getEdge(int node1, int node2) {
		nodeInfo Node1 = (nodeInfo)nodes.get(node1);									//get node1 as node object
		if(Node1 == null || !Node1.weights.containsKey(node2)) return -1;				//check if node1 exist in the graph and have node2 as neighbor, return -1 if not
		else return Node1.weights.get(node2);											//return the weight of the edge between node1 and node2
	}

	/**
	 * add a node with the given key to the graph, if their is already a node with that key, the method does nothing
	 * @param key 
	 */
	@Override
	public void addNode(int key) {
		nodeInfo new_node = new nodeInfo(key);					//create new node with given key
		if(nodes.putIfAbsent(key, new_node) == null) {			//add the node to the graph, if it's not already exist.
		number_of_nodes++;										//increase the counters
		ModeCount++;
		}
	}
	
	/**
	 * add a node with the given key and info to the graph, 
	 * if their is already a node with that key, the method does nothing
	 * @param key
	 * @param info
	 */
	public void addNode(int key , String info) {
		nodeInfo new_node = new nodeInfo(key , info);			//create new node with given key and info
		if(nodes.putIfAbsent(key, new_node) == null) {			//add the node to the graph, if it's not already exist.
		number_of_nodes++;										//increase the counters
		ModeCount++;
		}
	}
	
	/**
	 * connect the edge node1-node2 with the given weight.
	 * if their is a connection, only update the weight.
	 * @param node1
	 * @param node2
	 * @param w - the weight
	 */
	@Override
	public void connect(int node1, int node2, double w) {
		nodeInfo Node1 = (nodeInfo)nodes.get(node1), Node2 = (nodeInfo)nodes.get(node2);	//get the nodes as node objects
		
		if(node1 == node2 || Node1 == null || Node2 == null || w<0) return;					//check valid input
		
		if(!hasEdge(node1, node2)) {														//if the nodes are not connected,
		Node1.neighbors.put(node2, Node2);													//connect them
		Node2.neighbors.put(node1, Node1);	
		updateWeight(node1, node2, w);														//update the weight between them
		number_of_edges++;																	//increase the counters
		ModeCount++;
		}
		
		else if(getEdge(node1, node2) == w) return;											//if the nodes are connected and the weight is good, do nothing
		else {updateWeight(node1, node2, w); ModeCount++;}									//if the nodes are connected and the weight is not good, update it
		
	}

	/**
	 * update the weight of the edge between node1 and node2. the method assume valid input(the nodes exist and connected)
	 * @param node1
	 * @param node2
	 * @param w - the new weight
	 */
	private void updateWeight(int node1 , int node2 , double w) {
		nodeInfo Node1 = (nodeInfo)nodes.get(node1);				//get the nodes as node objects
		nodeInfo Node2 = (nodeInfo)nodes.get(node2);
		
		Node1.weights.put(node2, w);								//update the weight
		Node2.weights.put(node1, w);
	}
	
	/**
	 * Return a collection of all the nodes in the graph
	 * @return collection of the nodes
	 */
	@Override
	public Collection<node_info> getV() {
		return nodes.values();
	}

	/**
	 * Return a collection of the neighbors of the given node
	 * @param node_id
	 * @return collection of the neighbors
	 */
	@Override
	public Collection<node_info> getV(int node_id) {
		nodeInfo Node = (nodeInfo)nodes.get(node_id);		//get the node as node object
		if(Node == null) return null;						//check it exist in the graph
		return Node.neighbors.values();						//return it's neighbors
	}

	/**
	 * Remove the node from the graph
	 * @param key - the node to remove
	 * @return the removed node , null if does'nt exist
	 */
	@Override
	public node_info removeNode(int key) {
		if(!nodes.containsKey(key)) return null;		//check the node exist in the graph
		nodeInfo Node = (nodeInfo)nodes.get(key);		//get the node as node object
		nodeInfo no;									
		for(node_info n : Node.neighbors.values()) {	//for each of the neighbors,
			no = (nodeInfo)n;
			no.neighbors.remove(key);					//remove the node as a neighbor
			no.weights.remove(key);
			number_of_edges--;							//and remove an edge from the count
		}
		nodes.remove(key);								//remove the node from the graph's nodes list
		number_of_nodes--;								//update the counters
		ModeCount++;
		return Node;									//return the removed node
	}

	/**
	 * Remove the edge node1-node2
	 * @param node1
	 * @param node2
	 */
	@Override
	public void removeEdge(int node1, int node2) {
		nodeInfo Node1 = (nodeInfo)nodes.get(node1);						//get the nodes as node objects
		nodeInfo Node2 = (nodeInfo)nodes.get(node2);
		
		if(Node1 == null || Node2 == null || !hasEdge(node1,node2)) return;	//check for valid input
		
		Node1.neighbors.remove(node2);										//remove the nodes from each othor's neighbors lists
		Node1.weights.remove(node2);
		Node2.neighbors.remove(node1);
		Node2.weights.remove(node1);
		
		number_of_edges--;													//update the counters
		ModeCount++;
	}

	/**
	 * @return the number of nodes in the graph
	 */
	@Override
	public int nodeSize() {
		return number_of_nodes;
	}

	/**
	 * @return the number of edges in the graph
	 */
	@Override
	public int edgeSize() {
		return number_of_edges;
	}

	/**
	 * Return the number of changes to inner state of the graph(add/remove node/edge , change edge weight)
	 */
	@Override
	public int getMC() {
		return ModeCount;
	}
	

	
	/**
	 * @return a String representing the graph
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for(node_info n : nodes.values())  {sb.append(n.toString()); sb.append("\n");}
		return sb.toString();
	}
	
	/**
	 * A class that represent a node in the graph
	 * @author nir son
	 *
	 */
	private class nodeInfo implements node_info , Serializable{
		
		private static final long serialVersionUID = 1L;
		private int key;
		private String info;
		private HashMap<Integer , node_info> neighbors;
		private HashMap<Integer , Double> weights;
		private double tag;
		
		//***** constructors *****
		
		/**
		 * constructor with given key and info
		 * @param key
		 * @param info
		 */
		public nodeInfo(int key, String info) {
			this.key = key;
			this.info = info;
			this.neighbors = new HashMap<Integer, node_info>();
			this.weights = new HashMap<Integer, Double>();
		}
		
		/**
		 * constructor with given key
		 * @param key
		 */
		public nodeInfo(int key) {
			this.key = key;
			this.info = "";
			this.neighbors = new HashMap<Integer, node_info>();
			this.weights = new HashMap<Integer, Double>();
		}
		
		//***** methods *****
		
		/**
		 *  Return the key (id) associated with this node.
		 *  @return the key
		 */
		@Override
		public int getKey() {
			return key;
		}
		
		/**
		 * return the remark (meta data) associated with this node.
		 * @return the info
		 */
		@Override
		public String getInfo() {
			return info;
		}
		
		/**
		 * Allows changing the remark (meta data) associated with this node.
		 * @param s - the new data
		 */
		@Override
		public void setInfo(String s) {
			info = s;
		}
		
		/**
		 *  Return the temporal data of this node (which can be used for algorithms)
		 *  @return the tag
		 */
		@Override
		public double getTag() {
			return tag;
		}
		
		/**
		 * Allow setting the "tag" value for temporal marking of a node
		 * @param t- the new tag
		 */
		@Override
		public void setTag(double t) {
			tag = t;
		}
		
		/**
		 * @return a String representing the node
		 */
		public String toString() {	
			StringBuilder sb = new StringBuilder("");
			sb.append("key: "); sb.append(key); sb.append(" info: ");				//add the key and info of the node to the string
			sb.append(info); sb.append(" neighbors: [");
			
			for(int n : weights.keySet()) {											//for each of the neighbors, add it's key and distance(weight) from this node
				sb.append("key: "); sb.append(n); sb.append(" distance: ");
				sb.append(weights.get(n)); sb.append(" ; ");
			}
			sb.append("]"); 
			return sb.toString();													//return the string
		}
		
	}
	
}