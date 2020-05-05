import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * Contains the API necessary for a simple, (optionally) weighted directed graph.
 * We call the graph "optionally weighted" because it can be used by algorithms that use weights
 * (like Dijkstra's) and by algorithms that do not (like BFS). An algorithm like BFS would simply
 * ignore any weights present.
 * <p>
 * By convention, the n vertices will be labeled 0,1,...,n-1. The edge weights can be any int value.
 * Since we are labeling vertices from 0 to n-1, you may find arrays/arraylists helpful!
 * Self loops and parallel edges are not allowed. Your implementation should use O(m + n) space.
 * Please DO NOT use adjacency matrices!
 * <p>
 * Also note that the runtimes given are expected runtimes. As a result, you should be
 * implementing your graph using a HashMap as the primary data structure for the adjacency list.
 * <p>
 * Notice that this class also supports undirected graph. Which means you can implement an
 * undirected graph as each undirected edge between u and v being two directed edge from u to v and
 * from v to u.
 */
public class Graph {
    //list of nodes' hashMap adjacency lists
    private ArrayList<HashMap<Integer, Integer>> totList;
    private ArrayList<HashMap<Integer, Integer>> betweeness;
    private HashMap<Integer, ArrayList<Integer>> strongTies;
    
    /**
     * Initializes a graph of size {@code n}. All valid vertices in this graph thus have integer
     * indices in the half-open range {@code [0, n)}, n > 0.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param n the number of vertices in the graph
     * @throws IllegalArgumentException if {@code n} is zero or negative
     * @implSpec This method should run in O(n) time
     */
    public Graph(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        totList = new ArrayList<HashMap<Integer,Integer>>(n); //size of adj list
        betweeness = new ArrayList<HashMap<Integer,Integer>>(n); //size of adj list
        strongTies = new HashMap<Integer, ArrayList<Integer>>(n);
        for (int i = 0; i < n; i++) { //O(n) time to go through each of n nodes
            totList.add(new HashMap<Integer, Integer>()); //add adj list for each node
            betweeness.add(new HashMap<Integer, Integer>()); //add adj list for each node
//            strongTies.put(new HashMap<Integer, Integer>());
        }
    }
    
    public ArrayList<HashMap<Integer,Integer>> getList() {
    	return totList;
    }

    /**
     * Returns the number of vertices in the graph.
     * <p/>
     * Do NOT modify this method header.
     *
     * @return the number of vertices in the graph
     * @implSpec This method should run in O(1) time.
     */
    public int getSize() {
        return totList.size(); //constant time to return
    }

    /**
     * Determines if there's an directed edge from u to v.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u a vertex
     * @param v a vertex
     * @return {@code true} if the {@code u-v} edge is in this graph
     * @throws IllegalArgumentException if a specified vertex does not exist
     * @implSpec This method should run in O(1) time.
     */
    public boolean hasEdge(int u, int v) {
        //check first if vertices exist in constant time
        if (totList.size() <= u || u < 0) {
            throw new IllegalArgumentException();
        }
        if (totList.size() <= v || v < 0) {
            throw new IllegalArgumentException();
        }
        return (totList.get(u).containsKey(v)); //constant time check if list has key 
    }

    /**
     * Returns the weight of an the directed edge {@code u-v}.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u source vertex
     * @param v target vertex
     * @return the edge weight of {@code u-v}
     * @throws NoSuchElementException   if the {@code u-v} edge does not exist
     * @throws IllegalArgumentException if a specified vertex does not exist
     * @implSpec This method should run in O(1) time.
     */
    public int getWeight(int u, int v) {
        //checks in constant time using hasEdge constant method
        if (!hasEdge(u,v)) {
            throw new NoSuchElementException();
        }
        if (totList.size() <= u || u < 0) {
            throw new IllegalArgumentException();
        }
        if (totList.size() <= v || v < 0) {
            throw new IllegalArgumentException();
        }
        return (totList.get(u).get(v)); //constant to get weight
    }
    
    /**
     * Returns the weight of an the directed edge {@code u-v}.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u source vertex
     * @param v target vertex
     * @return the edge weight of {@code u-v}
     * @throws NoSuchElementException   if the {@code u-v} edge does not exist
     * @throws IllegalArgumentException if a specified vertex does not exist
     * @implSpec This method should run in O(1) time.
     */
    public int getBetweeness(int u, int v) {
        //checks in constant time using hasEdge constant method
        if (!hasEdge(u,v)) {
            throw new NoSuchElementException();
        }
        if (totList.size() <= u || u < 0) {
            throw new IllegalArgumentException();
        }
        if (totList.size() <= v || v < 0) {
            throw new IllegalArgumentException();
        }
        return (betweeness.get(u).get(v)); //constant to get weight
    }
    

    /**
     * Creates an edge from {@code u} to {@code v} if it does not already exist. A call to this
     * method should <em>not</em> modify the edge weight if the {@code u-v} edge already exists.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u      the source vertex to connect
     * @param v      the target vertex to connect
     * @param weight the edge weight
     * @return {@code true} if the graph changed as a result of this call, false otherwise (i.e., if
     * the edge is already present)
     * @throws IllegalArgumentException if a specified vertex does not exist or if u == v
     * @implSpec This method should run in O(1) time
     */
    public boolean addEdge(int u, int v, int weight) {
        //checks in constant time
        if (totList.size() <= u || u < 0) {
            throw new IllegalArgumentException();
        }
        if (totList.size() <= v || v < 0) {
            throw new IllegalArgumentException();
        }
        if (hasEdge(u,v)) { //checks if edge exists in constant time then does not modify
            return false;
        } else { //constant time to add edge, and return true
            totList.get(u).put(v, weight);
            betweeness.get(u).put(v, 0);
            return true;
        }
    }
    
    /**
     * Creates an edge from {@code u} to {@code v} if it does not already exist. A call to this
     * method should <em>not</em> modify the edge weight if the {@code u-v} edge already exists.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u      the source vertex to connect
     * @param v      the target vertex to connect
     * @param weight the edge weight
     * @return {@code true} if the graph changed as a result of this call, false otherwise (i.e., if
     * the edge is already present)
     * @throws IllegalArgumentException if a specified vertex does not exist or if u == v
     * @implSpec This method should run in O(1) time
     */
    public boolean deleteEdge(int u, int v) {
        //checks in constant time
        if (totList.size() <= u || u < 0) {
            throw new IllegalArgumentException();
        }
        if (totList.size() <= v || v < 0) {
            throw new IllegalArgumentException();
        }
        if (!hasEdge(u,v)) { //checks if edge exists in constant time then does not modify
            return false;
        } else { //constant time to add edge, and return true
            totList.get(u).remove(v);
            betweeness.get(u).remove(v);
            return true;
        }
    }

    /**
     * Returns the out-neighbors of the specified vertex.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param v the vertex
     * @return all out neighbors of the specified vertex or an empty set if there are no out
     * neighbors
     * @throws IllegalArgumentException if the specified vertex does not exist
     * @implSpec This method should run in O(outdeg(v)) time.
     */
    public Set<Integer> outNeighbors(int v) {
        //constant check if v is in bounds
        if (totList.size() <= v || v < 0) {
            throw new IllegalArgumentException();
        }
        return totList.get(v).keySet(); //O(outdeg(v)) to go through keys of v (v's adjacent nodes)
    }
    
    /**
     * Method called in constructor of graph that reads in a file and converts the lines into a 
     * LinkedList of all the Nodes in (and not in) the file.
     * 
     * @param Takes in file
     * @returns a linked list of all the nodes
     * @throws IOException
     * 
     */
    public static Graph createGraphFile(File in) throws IOException {
        int max = 0; //largest number in text file
        Scanner read = new Scanner (in); //scanner to read in file
        while (read.hasNext()) { //while there is another line to read in txtfile
            String a = read.nextLine(); //a is the entire line in a text file
            String[] cutoff = a.split(" "); //split the string into two strings at space
            String numNode = cutoff[0]; //number for current node
            String adjNum = cutoff[1]; //adjacent node to current node
            if (max < Integer.parseInt(numNode)) { //set max to bigger 
                max = Integer.parseInt(numNode);
            }
            if (max < Integer.parseInt(adjNum)) {  //set max to bigger 
                max = Integer.parseInt(adjNum);
            }
        }
        //add one as num of nods includes 0
        Graph newGraph = new Graph(max + 1);
        //use max to make graph
        read = new Scanner (in); //scanner to read in file
        while (read.hasNext()) { //while there is another line to read in txtfile
            String a = read.nextLine(); //a is the entire line in a text file
            String[] cutoff = a.split(" "); //split the string into two strings at space
            Integer numNode = Integer.parseInt(cutoff[0]); //number for current node
            Integer adjNum = Integer.parseInt(cutoff[1]); //adjacent node to current node
            
            //add edges to new graph, and assign each a random weight 0<= weight < 100
            newGraph.addEdge(numNode, adjNum, (int) (100 * Math.random()));
        }
        return newGraph;
    }
    
    public static int[] populateBetweeness(Graph g) {
        int[] max = {-1, -1, -1};
        for (int i = 0; i < g.getSize(); i++) {
            for (int j = 0; j < g.getSize(); j++) {
                if (i == j) {
                    continue;
                }
                List<Integer> path = Dijkstra.getShortestPath(g, i, j);
                for (int k = 0; k < path.size() - 1; k++) {
                    
                    int betw = g.getBetweeness(path.get(k), path.get(k + 1));
                    betw++;
                    if (betw > max[0]) {
                        max[0] = betw;
                        max[1] = path.get(k);
                        max[2] = path.get(k + 1);
                    }
                    g.betweeness.get(path.get(k)).put(path.get(k + 1), betw);
                }
            }
        }
        System.out.println("max " + max[0] + " " + max[1] + " " + max[2]);
        return max;
    }
    
    //calculates neighborhood overlap of two nodes
    public double neighOverlap (Graph g, Integer nodeA, Integer nodeB) {
    	Set<Integer> neighborsA = outNeighbors(nodeA);
    	Set<Integer> neighborsB = outNeighbors(nodeB);
    	HashSet<Integer> intersection = new HashSet<Integer>();
    	HashSet<Integer> union = new HashSet<Integer>();
    	double overlap = 0.0;
    	
    	for (Integer a : neighborsA) {
    		for (Integer b : neighborsB) {
    			if (a == b) {
    				intersection.add(a);
    			}
    		}
    	}
    	union.addAll(neighborsA);
    	union.addAll(neighborsB);
    	union.removeAll(intersection);
    	
    	//calculate neighborhood overlap
    	overlap = (double) intersection.size() / (double) union.size();
    	return overlap;
    }
    
    //calculate average neighborhood overlap
    public double avgNeighOverlap (Graph g) {
    	double numNodes = (double) g.getSize();
    	double sum = 0.0;
    	for (int i = 0; i < g.getSize(); i++) {
    		for (int j = 0; j < g.getSize(); j++) {
	    		if (g.hasEdge(i, j)) {
	    			sum = sum + neighOverlap(g, i, j);
	    		}
    		}
    	}
    	System.out.println(sum / numNodes);
    	return sum / numNodes;
    }
    
    //assigns strong and weak ties based on weight
    public void assignTies (Graph g) {    	
    	for (int i = 0; i < g.getSize(); i++) {
    		ArrayList<Integer> arr = new ArrayList<Integer>();
    		for (int j = 0; j < g.getSize(); j++) {
    			if (i == j) {
                    continue;
                }
    			if (hasEdge(i, j)) {
    				if (getWeight(i, j) > 50) { //add edge to map of strong ties
    					arr.add(j);
    					g.strongTies.put(i, arr);
    				} 
    			}
    		}		
    	}
    }
    
    //finds violations to triadic closures
    public void triadic (Graph g) {
    	assignTies(g);
    	
    	//maps node that violates with pair of nodes it has strong ties with    	
    	HashMap<Integer, ArrayList<Integer>> violations = new HashMap<Integer, ArrayList<Integer>>();
    	
    	for (Integer i : strongTies.keySet()) {
    		if (strongTies.containsKey(i) && strongTies.get(i).size() >= 2) {
	    		for (int k = 0; k < strongTies.get(i).size(); k++) {
					for (int l = k + 1; l < strongTies.get(i).size(); l++) {						
						if (!hasEdge(strongTies.get(i).get(k), strongTies.get(i).get(l))) {
							ArrayList<Integer> pair = new ArrayList<Integer>();
							pair.add(strongTies.get(i).get(k));
							pair.add(strongTies.get(i).get(l));
							violations.put(i, pair);
							break;
						} 
					}
				} 
    		} 
    	}
    	System.out.println("number of violations: " + violations.size());
		System.out.println(violations.keySet()); //prints nodes that have violations
    }
    
    
    //finds and removes the node with the highest betweeness
    public static void girvNewman (Graph g) {
        int[] max = populateBetweeness(g);
        g.deleteEdge(max[1], max[2]);
        g.betweeness = new ArrayList<HashMap<Integer,Integer>>(g.getSize()); //size of adj list
        for (int i = 0; i < g.getSize(); i++) { //O(n) time to go through each of n nodes
            g.betweeness.add(new HashMap<Integer, Integer>()); //add adj list for each node
            Set<Integer> keys = g.totList.get(i).keySet();
            Iterator<Integer> keysIt = keys.iterator();
            for (int j = 0; j < keys.size(); j++) {
                g.betweeness.get(i).put(keysIt.next(), 0);
            }
        }
    }
  
    
    public static void main(String args[]) throws IOException {
        Graph cur = createGraphFile(new File("text.txt"));
//        girvNewman(cur);
//        girvNewman(cur);
        System.out.println(0 + " iter: ");
        cur.triadic(cur);
        cur.avgNeighOverlap(cur);
        for (int i = 1; i < 40; i++) {
            System.out.println(i + " iter: ");
            girvNewman(cur);
            cur.triadic(cur);
            cur.avgNeighOverlap(cur);
        }
    }
}