import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Provides access to Dijkstra's algorithm for a weighted graph.
 */
final public class Dijkstra {
    private Dijkstra() {}

    /**
     * Computes the shortest path between two nodes in a weighted graph.
     * Input graph is guaranteed to be valid and have no negative-weighted edges.
     *
     * @param g   the weighted graph to compute the shortest path on
     * @param src the source node
     * @param tgt the target node
     * @return an empty list if there is no path from {@param src} to {@param tgt}, otherwise an
     * ordered list of vertices in the shortest path from {@param src} to {@param tgt},
     * with the first element being {@param src} and the last element being {@param tgt}.
     */
    public static List<Integer> getShortestPath(Graph g, int src, int tgt) {
        int[] parent = dijkstraParent(g, src); //parent pointers to backtrack
        List<Integer> shortestPath = new ArrayList<Integer>(); //path to update
        backtrack(src, parent,tgt,shortestPath); //backtrack from src
        return shortestPath;
    }
    
    static int[] dijkstraParent(Graph g, int src) {
        int[] parent = new int[g.getSize()]; //parent pointers
        int[] distance = new int[g.getSize()]; //distance estimate
        boolean[] done = new boolean[g.getSize()]; //finish vertices to not visit again
        Arrays.fill(parent, -1); //initialize all parents to -1, takes O(n)
        Arrays.fill(distance, Integer.MAX_VALUE); //initialize all dis estimate to max, takes O(n)
        
        distance[src] = 0; //start with start at 0
        
        //make priQ and add start with distance estimate as key
        BinaryMinHeap<Integer, Integer> priQ = new BinaryMinHeapImpl<Integer, Integer>();
        priQ.add(0, src);
        
        //Dijkstra's part, takes O(mlgn) as at most m decrease keys
        while (!priQ.isEmpty()) {
            int smallest = priQ.extractMin().value;
            done[smallest] = true; //update the min vertex is done
            Set<Integer> adjOfSmall = g.outNeighbors(smallest); //get adj nodes
            Iterator<Integer> adjIt = adjOfSmall.iterator();
            //for each adj, make sure not done and new distance is smaller than old dis estimate
            while (adjIt.hasNext()) {
                int adjNext = adjIt.next();
                if (!done[adjNext] && distance[adjNext] > (distance[smallest] + 
                        g.getWeight(smallest, adjNext))) {
                    int newDist = distance[smallest] + g.getWeight(smallest, adjNext);
                    if (priQ.containsValue(adjNext)) { //if already in priQ, update O(lgn)
                        priQ.decreaseKey(adjNext, newDist);
                    } else {
                        priQ.add(newDist, adjNext); //else, add as new value O(lgn)
                    }
                    distance[adjNext] = newDist;
                    parent[adjNext] = smallest;
                }
            }
        }
        return parent;
    }
    
    //backtrack, stop when reach start. Empty if doesn't have parent, else recurse until reaches end
    static void backtrack(int src, int[] parents, int cur, List<Integer> shortestPath) {
        if (cur == src) {
            shortestPath.add(0,src);
            return;
        } else if (parents[cur] == -1) {
            shortestPath = new ArrayList<Integer>();
            return;
        } else {
            shortestPath.add(0,cur);
            backtrack(src, parents, parents[cur], shortestPath);
        }
    }
}
