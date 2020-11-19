package ex1.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ex1.src.WGraph_Algo;
import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;

class algoTest {

	/**
	 * create a simple graph in this form:
	 * 
	 * (0)---4.5---(1)
	 * 	\			|
	 * 	   \		|
	 * 		 7		1
	 * 			\ 	|
	 *             \|
	 * (2)---1.3---(3)
	 */
	WGraph_DS simple_graph_generator() {
		WGraph_DS g = new WGraph_DS();
		
		g.addNode(0);
		g.addNode(1);
		g.addNode(2);
		g.addNode(3);
		
		g.connect(0, 1, 4.5);
		g.connect(0, 3, 7);
		g.connect(1, 3, 1);
		g.connect(2, 3, 1.3);
		
		return g;
	}
	
	@Test
	void TestCopy() {
		WGraph_DS g = simple_graph_generator();
		WGraph_Algo ga = new WGraph_Algo();
		ga.init(g);
		
		weighted_graph g1 = ga.copy();
		
		g.removeEdge(0, 3);
		assertTrue(g1.hasEdge(0, 3), "removing edge in originsl effect new");
		
		g1.getNode(2).setInfo("test");
		assertFalse(g.getNode(2).getInfo().equals("test") , "changing info in new effect original");
		
		assertEquals(4, g1.nodeSize() , "number of nodes in new is wrong");
		assertEquals(4, g1.edgeSize() , "number of edges in new is wrong");
	}
	
	
	@Test
	void TestEdgeCasesCopy() {
		WGraph_Algo ga = new WGraph_Algo();
		ga.init(null);
		
		assertNull(ga.copy());
		ga.init(new WGraph_DS());
		assertEquals(0 , ga.copy().nodeSize() , "copy of empy should be empty");
		assertEquals(0 , ga.copy().edgeSize() , "copy of empy should be empty");
		
	}
	
	@Test
	void TestLargeGraphCopy() {			//run time test
		long ST = System.nanoTime();
		Random rnd = new Random();
		WGraph_DS g = new WGraph_DS();
		for(int i = 0 ; i < 100000 ; i++) g.addNode(i);
		while(g.edgeSize() < 1000000) g.connect(rnd.nextInt(100000), rnd.nextInt(100000), rnd.nextInt(100));
		
		WGraph_Algo ga = new WGraph_Algo(g);
		ga.copy();
		
		assertTrue((System.nanoTime() - ST) / Math.pow(10, 9) < 10 , "coping a graph with 100000 nodes and 1000000 edges took more then 10 sconds");
	}
	
	
	@Test
	void TestIsConnected() {
		WGraph_DS g = simple_graph_generator();
		WGraph_Algo ga = new WGraph_Algo(g);
		
		assertTrue(ga.isConnected() , "connected graph return false");
		
		g.removeEdge(3, 2);
		assertFalse(ga.isConnected() , "not connected graph return true");
	}
	
	
	@Test
	void TestIsConnectedEdgeCases() {
		WGraph_Algo ga = new WGraph_Algo(null);
		assertThrows(NullPointerException.class , () -> {ga.isConnected();} , "null graph doest throw NullPointerException");
		ga.init(new WGraph_DS());
		assertTrue(ga.isConnected() , "empty garph return false");
	}
	
	
	@Test
	void TestShortestPathDist() {
		WGraph_DS g = simple_graph_generator();
		WGraph_Algo ga = new WGraph_Algo(g);
		
		assertEquals(6.8 , ga.shortestPathDist(0, 2) , "the Shortest Path Distance is wrong");
		assertEquals(0, ga.shortestPathDist(1, 1) , "the Path Distance from node to itself is not 0");
		g.removeEdge(2, 3);
		assertEquals(-1 , ga.shortestPathDist(1, 2) , "the Shortest Path Distance between not connected node is not -1");
	}
	
	@Test
	void TestShortestPathDistEdgeCases() {
		WGraph_Algo ga = new WGraph_Algo(null);
		assertThrows(NullPointerException.class , () -> {ga.shortestPathDist(0, 0);} , "null graph doest throw NullPointerException");
	}
	
	@Test
	void TestShortestPath() {
		WGraph_DS g = simple_graph_generator();
		WGraph_Algo ga = new WGraph_Algo(g);
		
		LinkedList<node_info> lst = new LinkedList<node_info>();
		lst.add(g.getNode(0)); lst.add(g.getNode(1));
		lst.add(g.getNode(3)); lst.add(g.getNode(2));
		
		assertEquals(lst , ga.shortestPath(0, 2) , "the path is wrong");	
	}
	
	@Test
	void TestShortestPathEdgeCases() {
		WGraph_Algo ga = new WGraph_Algo(null);
		assertThrows(NullPointerException.class , () -> {ga.shortestPath(0, 0);} , "null graph doest throw NullPointerException");
		
		ga.init(new WGraph_DS());
		assertNull(ga.shortestPath(0, 1) , "empty graph doesnt return null");
		
		WGraph_DS g = new WGraph_DS();
		g.addNode(0);
		ga.init(g);
		LinkedList<node_info> lst = new LinkedList<node_info>();
		lst.add(g.getNode(0));
		assertEquals(lst , ga.shortestPath(0, 0) , "path from node to itself is note ifself");
	}
	
	@Test
	void TestSave_Load() {
		WGraph_DS g = simple_graph_generator();
		WGraph_Algo ga = new WGraph_Algo(g);
		
		assertTrue(ga.save("savetest") , "file wasn't sucssesfully saved");
		WGraph_DS g1 = (WGraph_DS)ga.getGraph();
		assertTrue(ga.load("savetest") , "file wasn't sucssesfully loaded");
		assertEquals(g1.toString() , ga.getGraph().toString() , "loaded worng graph");
	}
	
	
	@Test
	void TestSave_LoadNull() {
		WGraph_Algo ga = new WGraph_Algo(null);
		
		assertFalse(ga.save("savetest") , "null was sucssesfully saved");
		assertFalse(ga.load(null) , "null was sucssesfully loaded");
	}
	
	
}
