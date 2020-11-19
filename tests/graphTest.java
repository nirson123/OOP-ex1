package ex1.tests;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ex1.src.WGraph_DS;

class graphTest {

	static long stratTime;
	
	@BeforeAll
	static void init() {
		System.out.println("starting tests\n"); 
		stratTime = System.nanoTime();
	}
	
	@AfterAll
	static void totalTimeTest() {
		double totalTime = (System.nanoTime() - stratTime)/Math.pow(10, 9);
		System.out.println("end tests\ntotal time: "+totalTime);
		
	}
	
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
	void TestHas_GetEdge() {
		WGraph_DS g = simple_graph_generator();
		assertTrue(g.hasEdge(0, 3) , "return false where there is edge");
		assertFalse(g.hasEdge(0, 2) , "return true where there is no edge");
		assertEquals(1 , g.getEdge(1, 3) , "return wrong edge weight");
		assertEquals(-1 , g.getEdge(1, 2) , "return wrong edge weight");
	}
	
	@Test
	void Testremove(){
		WGraph_DS g = simple_graph_generator();
		g.removeEdge(1, 0);
		assertFalse(g.hasEdge(0, 1) , "remove didnt work");
		g.connect(1, 0, 4.5);
		
		g.removeNode(3);
		assertNull(g.getNode(3) , "remove didnt work");
		assertFalse(g.hasEdge(3, 2) , "remove didnt work");
		
		assertEquals(11 , g.getMC() , "remove doesnt increase MC");
	}
	
	@Test
	void TestCounters() {
		WGraph_DS g = simple_graph_generator();
		assertEquals(4 , g.nodeSize() , "wrong number of nodes");
		assertEquals(4 , g.edgeSize() , "wrong number of edges");
		assertEquals(8 , g.getMC() , "wrong ModeCount");
	}
	
	@Test
	void TestConnect() {
		WGraph_DS g = simple_graph_generator();
		
		int CurrentMC = g.getMC();
		g.connect(3, 0, 8.9);
		assertEquals(8.9 , g.getEdge(0, 3) , "didnt update the weight");
		assertEquals(CurrentMC + 1 , g.getMC() , "updating the weight doesnt increace MC");
		
		CurrentMC = g.getMC();
		g.connect(2, 3, 1.3);
		assertEquals(CurrentMC , g.getMC() , "tring to update to the current weight increace the MC");
	}
	
	//this is a runtime test
	@Test
	void TestLargeGraph() {
		long ST = System.nanoTime();
		WGraph_DS LG = new WGraph_DS();
		for(int i=0;i<100000;i++) LG.addNode(i);
		Random rnd = new Random();
		while(LG.edgeSize()<1000000) LG.connect(rnd.nextInt(100000), rnd.nextInt(100000), rnd.nextInt(100));
		double totalTime = (System.nanoTime() - ST)/Math.pow(10, 9);
		assertTrue((totalTime < 7) , "generating a graph with 10^5 nodes and 10^6 edges took more then 7 sconds");
	}
	
}

