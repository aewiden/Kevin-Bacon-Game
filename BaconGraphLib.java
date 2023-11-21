import java.util.*;

public class BaconGraphLib {
    private static int pathLength;

    // bfs on a graph given a source vertex
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
        // creates variables for queue, set, and a BFS graph
        Queue<V> queue = new LinkedList<>();
        Graph<V,E> pathTree = new AdjacencyMapGraph<V,E>();
        // adds the source to the queue to begin the bfs
        queue.add(source);
        // adds the source to the set of visited vertices and inserts it into the bfs graph
        pathTree.insertVertex(source);
        // while there are entries in the queue, loops through neighbors
        while (!queue.isEmpty()){
            // removes the current vertex from the queue
            V u = queue.remove();
            // for each neighbor of the current vertex, bfs search
            for (V neighbor: g.outNeighbors(u)){
                // if the neighbor is not visited, add it to the queue and insert edges accordingly
                if (!pathTree.hasVertex(neighbor)){
                    queue.add(neighbor);
                    // adds and updates the bfs graph to reflect the shortest path
                    pathTree.insertVertex(neighbor);
                    pathTree.insertDirected(neighbor, u, g.getLabel(u, neighbor));
                }
            }
        }
        return pathTree;
    }

    public static <V, E> List<V> getPath(Graph<V, E> tree, V v) {
        // creates an array list to track the path
        ArrayList<V> path = new ArrayList<V>();
        // checks to make sure that the vertex exists in the tree
        if(!tree.hasVertex(v)) {
            return path;
        }
        // sets the current vertex to the one given (the goal)
        V current = v;
        // while the current vertex exists has parents
        while(tree.outDegree(current) != 0) {
            // add the vertex to the front of the path
            path.add(0, current);
            // for each out neighbor of the vertex, set current to that out neighbor
            for(V vertex : tree.outNeighbors(current)) {
                // sets current to the vertex that led to the goal
                current = vertex;
            }
        }
        // print and return the path
        path.add(0, current);
        System.out.println(path);
        return path;
    }

    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
        // creates sets to track vertices and missing vertices
        HashSet<V> missing = new HashSet<V>();
        HashSet<V> vertices = new HashSet<V>();

        // adds all vertices in the subgraph to the set
        for(V vertex : subgraph.vertices()) {
            vertices.add(vertex);
        }

        // goes through each vertex in the graph
        for(V vertex : graph.vertices()) {
            // if the vertices set does not contain the current vertex, add it to the set of missing ones
            if(!vertices.contains(vertex)) {
                missing.add(vertex);
            }
        }
        // returns the set of missing vertices
        return missing;
    }

    // calculates average separation recursively
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
        // variables to keep track of the number of vertices and the path length
        pathLength = 0;
        int i = tree.numVertices()-1;
        if(i <= 0) return 0;
        // calculates average separation as the quotient of the number of vertices and each separation
        else {
            // recursively calls the helper to calculate the depth
            separationHelper(tree, root, 0);
            return pathLength/i;
        }
    }

    // recursively calculates the separations of the vertices
    public static <V,E> void separationHelper(Graph<V,E> tree, V root, int path) {
        // update pathLength accordingly
        pathLength += path;
        // loops through each neighbor of the vertex and calculates the path, updating the path parameter accordingly
        for(V vertex : tree.inNeighbors(root)) {
            separationHelper(tree, vertex, path + 1);
        }
    }

}
