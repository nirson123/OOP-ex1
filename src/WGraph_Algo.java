package ex1.src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class WGraph_Algo implements weighted_graph_algorithms{
	
	private WGraph_DS g;

	//***** constructors *****
	
	public WGraph_Algo() {
		this.g = new WGraph_DS();
	}
	
	public WGraph_Algo(WGraph_DS g) {
			this.g =g;
	}
	
	//***** methods *****
	
	/**
	 * Init the graph on which this set of algorithms operates on.
	 * @param g -the new graph 
	 */
	@Override
	public void init(weighted_graph g) {
		this.g = (WGraph_DS)g;
	}

	/**
	 * @return the graph the algorithms is operating on
	 */
	@Override
	public weighted_graph getGraph() {
		return g;
	}

	/**
	 * @return a deep copy of the graph
	 */
	@Override
	public weighted_graph copy() {
		if(g == null) return null;																	//test if graph is null
		WGraph_DS new_graph = new WGraph_DS();												
		for(node_info n : g.getV()) new_graph.addNode(n.getKey() , n.getInfo());					//add copy of all the node to the new graph				
		for(node_info n : g.getV()) {																//for each node,
			for(node_info nei : g.getV(n.getKey())) {												//go through it's neighbors
				new_graph.connect(n.getKey(), nei.getKey(), g.getEdge(n.getKey(), nei.getKey()));	//and connect them in the new graph 
			}
		}
		return new_graph;																			//return the new graph
	}

	/**
	 * mark the distance of each node from the given one (at the "tag"), using Dijkstra's algorithm.
	 * return map of "parenthood" connections.
	 * @param startNode - key of the node to start at
	 * @return hash map of "parenthood" connections
	 */
	private HashMap<Integer , Integer> mark_dis (int startNode) {
		PriorityQueue<node_info> Q = new PriorityQueue<node_info>(new nodeComparator());	//the priority queue for Dijkstra's algorithm
		HashMap<Integer , Integer> parents = new HashMap<Integer , Integer>();				//hash map to to store the "parenthood" connections
		HashMap<Integer , Object> visited = new HashMap<Integer , Object>();				//hash map to keep track of what node were visited
		
		for(node_info n : g.getV()) n.setTag(Integer.MAX_VALUE);							//set all the distances to "infinity"
		g.getNode(startNode).setTag(0);														//set the start node's distance to 0
		Q.addAll(g.getV());																	//add all the nodes to priority queue
		
		double t;
		while(!Q.isEmpty()) {																//while the queue is not empty:
			node_info current = Q.poll();													//get the node with the lowest distance
			if(!visited.containsKey(current.getKey())) {									//if it's not yet visited
			for(node_info nei : g.getV(current.getKey())) {									//go through its neighbors
				if(!visited.containsKey(nei.getKey())) {									//if the neighbor is not visited
					t = current.getTag() + g.getEdge(current.getKey(), nei.getKey());		//the distance through current
					if(t < nei.getTag()) {													//if lower then the current distance
						nei.setTag(t);														//change the distance 
						parents.put(nei.getKey() , current.getKey());						//update the "parenthood"
						Q.remove(nei);
						Q.add(nei);															// re-add the updated node to the queue (because there is no "decrease-key" option in java.util.PriorityQueue) 
					}
				}
			}
			visited.put(current.getKey(), null);											//set the current node as visited
			}
		}
	return parents;																			//return the map of "parenthood" connections
	}
	
	/**
	 * Return true iff the graph is connected.
	 * @return is the graph connected
	 */
	@Override
	public boolean isConnected() {
		if(g == null) throw	new NullPointerException("graph is null");		//make sure the graph is not null, throw exception if so				
		if(g.getV().isEmpty()) return true;									//if the graph is empty, return true
		mark_dis(g.getV().iterator().next().getKey());						//mark the distances from a random node
		for(node_info n : g.getV())											//go though the node and check the distance is not "infinity"
			if(n.getTag() == Integer.MAX_VALUE) return false;
		return true;														//if all the node are in final distance, return true
	}

	/**
	 * Return the distance of the shortest path between src and dest
	 * @param src - starting node
	 * @param dest - end node
	 * @return length of the shortest path
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(g == null) throw	new NullPointerException("graph is null");		//make sure the graph is not null, throw exception if so
		node_info n = g.getNode(dest);										//get the destination node as node object
		if(g.getNode(src) == null || n == null) return -1;					//check valid input
	
	mark_dis(src);															//mark the distances from src
	if(n.getTag() == Integer.MAX_VALUE) return -1;							//if not connected, return -1
	else return n.getTag();													//if connected return the distance
	}

	/**
	 * Return the shortest path between src and dest
	 * @param src - start node
	 * @param dest - end node
	 * @return list of node representing the shortest path
	 */
	@Override
	public List<node_info> shortestPath(int src, int dest) {
		if(g == null) throw	new NullPointerException("graph is null");		//make sure the graph is not null, throw exception if so
		node_info n = g.getNode(dest);										//get the destination node as node object
		if(g.getNode(src) == null || n == null) return null;				//check valid input
			
		LinkedList<node_info> lst = new LinkedList<node_info>();				
		HashMap<Integer , Integer> parenthood = mark_dis(src);				//mark the distances from src and save the map of "parenthood" connections
		if(n.getTag() == Integer.MAX_VALUE) return null;					//if the node are not connected, return null
		
		int current = dest;
		while(current != src) {												//start form dest to src
			lst.addFirst(g.getNode(current));								//add to the lst
			current = parenthood.get(current);								//find the parent
		}
		lst.addFirst(g.getNode(src));										//add the src node
		
		return lst;															//return the list
		
	}

	/**
	 * save the graph to the file provided
	 * @param file - the file to save the graph to
	 * @return true iff the file was successfully saved 
	 */
	@Override
	public boolean save(String file) {
		if(g == null) return false;
		try {
		FileOutputStream fos = new FileOutputStream(file);			//create the stream
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(g);											//write the graph to the file
		
		oos.close();												//close the stream
		fos.close();
		
		return true;
		
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * load a graph from the given file , and init the algorithms to it 
	 * @param file - the file to load the graph from
	 * @return true iff the file was successfully loaded 
	 */
	@Override
	public boolean load(String file) {
		try {
			FileInputStream fis = new  FileInputStream(file);				//create the stream
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Object readObject = ois.readObject();							//read the file
			ois.close();													//close the stream
			fis.close();
			if(readObject instanceof WGraph_DS) {							//make sure the file is a graph
				WGraph_DS readGraph = (WGraph_DS)readObject;
				this.init(readGraph);										//replace the graph
				return true;
			}
			else return false;												//if the file is not a graph, return false
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	

}
