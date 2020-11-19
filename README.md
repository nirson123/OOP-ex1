the goal of this task is to implement a data structure of an un-directional weighted graph, and a few algorithms on it.
The graph is implemented in the “WGraph_DS” class.
The implementation is based on nodes (implemented in an inner class).
Each node has a key (id), hold information in the form of a String, and have a “tag” – a temporary information holder (int), to be used by algorithms.
Each node also hold a list of its neighbors (implemented with an HashMap in order to achieve O(1) time for insertion ,deletion and search) , and a second list (also implemented with an HashMap) with <neighbor-key , weight> pairs.
The graph itself hold a list (HashMap) of nodes, and counters for the number of nodes, the number of edges, and ModeCount – the number of changes to the graph.
The graph support adding or removing a node, connecting or disconnecting a pair of nodes, changing the weight of an edge, and observing a node, an edge, or the counters.
The algorithms are implemented in “WGraph_Algo” class.
This class holds a graph for the algorithms to be performed on, support “getGraph” method, and “init” method (to change the graph to working on).
The algorithms implemented are:
Copy: return a deep copy of the graph.
IsConnected: check if the graph is connected or not, making use of Dijkstra's algorithm.
ShortestPathDist: return the length of the shortest path between two given nodes, using Dijkstra's algorithm.
ShortestPath: return the shortest path between two given nodes, using Dijkstra's algorithm.
Save: save the graph to a file.
Load: load a graph from a file (and set to algorithms o it).

Date structures used in the task:
Hash Map: a date structures that’s stores <key,value> pairs. It have no order, and support insertion, deletion, and search in O(1) time. (documentation - https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) 
Priority Queue:  a date structure that holds items with priorities, and make sure the item with the lowest priority is at the top. Support insertion and extraction of the min in O(log n) time. (documentation - https://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html)
LinkedList: a date structure that holds items in a list (in order). Support insertion and deletion in O(1) time, and search in O(n). (documentation - https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html)
 Dijkstra's algorithm: an algorithm for calculating the shortest path from a given node to any other node in the graph. The algorithm uses a priority queue, and the node with the currently shortest distance is the one being “processed” each step  - meaning   its neighbors are check to see if getting to them “through” the current node is “cheaper” then their current recorded distance. (for farther reading - https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) 
