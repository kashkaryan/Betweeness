# NETShw5

We created a Graph.java class to handle reading in the file, creating the graph, populating betweenness for edges, running the Girvan-Newman algorithm, finding the triadic closure violations, and finding the average neighborhood overlap. There are also methods contained in this class to find the number of outward neighbors for nodes, if two nodes share an edge, and to get the number of nodes in the graph, or the graph size. The Dijkstra.java class runs the Dijkstra algorithm to populate betweenness for edges. 

In creating the graph, we store it in an adjacency list, using an array list of maps where every index value represents a node, and the key of each map is an adjacent node, and the values are the edge weights (randomly assigned from 0-100). 

To find the triadic closure violations, we first assign strong and weak ties using the method assignTies and we assign edges with weights greater than 50 to be strong ties. These strong ties are stored in a separate adjacency list, using a map where the key is the node and the value is an array list of adjacent nodes. Within these array lists of strongly tied nodes, if two nodes do not share an edge, then they are added to a map of other nodes that violate triadic closure. The method triadic returns these nodes as a set.

To find the neighborhood overlap, neighOverlap finds the overlap for any two nodes, and avgNeighOverlap finds the overlap for all nodes in the graph and averages them.

