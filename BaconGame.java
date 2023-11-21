import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


/**
 * Creates and properly maps actors to movies and movies to actors from read files
 *
 * @author Avery Widen
 * @author Olivia Sasse
 */


public class BaconGame {
    private GraphTraversal<String, HashSet<String>> traversal;
    private Graph<String, Set<String>> baconGraph;
    private HashMap<Integer, String> movies;
    private HashMap<Integer, String> actors;
    private HashMap<Integer, ArrayList<Integer>> actorToMovie;
    private HashMap<Integer, ArrayList<Integer>> movieToActor;

    // constructor
    public BaconGame() throws Exception {
        // instantiates graph traversal
        traversal = new GraphTraversal<String, HashSet<String>>();
        // instantiates the movie and actor maps by reading the given files
        movies = read("inputs/bacon/movies.txt");
        actors = read("inputs/bacon/actors.txt");
        // instantiate the actorToMovie and movieToActor maps through a different reading process
        actorToMovie = actorToMovie("inputs/bacon/movie-actors.txt");
        movieToActor = movieToActor("inputs/bacon/movie-actors.txt");

    }

    // helper method to read the given files and put them into a map
    public HashMap<Integer, String> read(String path) throws Exception {
        // creates a BufferedReader to process the path name
        BufferedReader rdr = new BufferedReader(new FileReader(path));
        // creates a map to put the information from the file into
        HashMap<Integer, String> result = new HashMap<Integer, String>();
        // sets the current line
        String current = rdr.readLine();
        // while there is a line to read, process the information in that line
        while (current != null) {
            // creates an array of strings within the line based on the character that splits up the information "|"
            String[] values = current.split("\\|");
            // if the map does not contain this information already, put it in the map
            if (!result.containsKey(Integer.parseInt(values[0]))) {
                // parses the value to turn it into an integer, sets the other part of the map to the following string
                result.put(Integer.parseInt(values[0]), values[1]);
            }
            // moves the current line to read the next line
            current = rdr.readLine();
        }
        // returns the map of keys and values from the file given
        return result;
    }


    // helper method to make actor to movie map
    public HashMap<Integer, ArrayList<Integer>> actorToMovie(String path) throws Exception {
        // creates a BufferedReader to process the path name
        BufferedReader rdr = new BufferedReader(new FileReader(path));
        // creates a map to put the information from the file into
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        // sets the current line
        String current = rdr.readLine();
        // while there is a line to read, process the information in that line
        while (current != null) {
            // creates an array of strings within the line based on the character that splits up the information "|"
            String[] values = current.split("\\|");
            // if the map does not contain this information already, put it in the map
            if (!result.containsKey(Integer.parseInt(values[0]))) {
                // parses the value to turn it into an integer, sets the other part of the map to the following string
                result.put(Integer.parseInt(values[0]), new ArrayList<Integer>());
                result.get(Integer.parseInt(values[0])).add(Integer.parseInt(values[1]));
            } else {
                // if the actor is already in the map, add that movie to its list of movie values
                result.get(Integer.parseInt(values[0])).add(Integer.parseInt(values[1]));
            }
            // moves the current line to read the next line
            current = rdr.readLine();
        }
        // returns the actor to movie map of keys and values from the file given
        return result;
    }


    // helper method to make movie to actor map
    public static HashMap<Integer, ArrayList<Integer>> movieToActor(String path) throws Exception {
        // creates a BufferedReader to process the path name
        BufferedReader rdr = new BufferedReader(new FileReader(path));
        // creates a map to put the information from the file into
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
        // sets the current line
        String current = rdr.readLine();
        // while there is a line to read, process the information in that line
        while (current != null) {
            // creates an array of strings within the line based on the character that splits up the information "|"
            String[] values = current.split("\\|");
            // if the map does not contain this information already, put it in the map
            if (!result.containsKey(Integer.parseInt((values[0])))) {
                // parses the value to turn it into an integer, sets the other part of the map to the following string
                result.put(Integer.parseInt(values[0]), new ArrayList<>());
                result.get(Integer.parseInt(values[0])).add(Integer.parseInt(values[1]));
            } else {
                // if the movie is already in the map, add that actor to its list of actor values
                result.get(Integer.parseInt(values[0])).add(Integer.parseInt(values[1]));
            }
            // moves the current line to read the next line
            current = rdr.readLine();
        }
        // returns the map of keys and values from the file given
        return result;
    }

    // creates a graph to track the connections between actors
    public Graph<String, Set<String>> createGraph() {
        // instantiates an adjacency map graph to track these connections
        Graph<String, Set<String>> connections = new AdjacencyMapGraph<String, Set<String>>();
        // loops through the integer associated with each value
        for(Integer actor : actors.keySet()) {
            connections.insertVertex(actors.get(actor));
        }
        // loops through each movie in movies list
        for(Integer movie : movieToActor.keySet()) {
            // loops through the cast of this movie
            for (Integer actor1: movieToActor.get(movie)){
                for (Integer actor2: movieToActor.get(movie)){
                    // if the actors are not the same, insert edges connecting them labeled with the movie in which they costar
                    if (actor1 != actor2){
                        if (!connections.hasEdge(actors.get(actor1), actors.get(actor2))){
                            connections.insertDirected(actors.get(actor1), actors.get(actor2), new HashSet<>());
                        }
                        // gets the label of their shared movie
                        connections.getLabel(actors.get(actor1), actors.get(actor2)).add(movies.get(movie));
                    }
                }

            }
        }
        return connections;
    }



    // method to play the Kevin Bacon game
    public void playGame(String actor, Graph<String, Set<String>> graph) {
        // description of key commands given
        System.out.println("Commands:\n" +
                "c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\n" +
                "d <low> <high>: list actors sorted by degree, with degree between low and high\n" +
                "i: list actors with infinite separation from the current center\n" +
                "p <name>: find path from <name> to current center of the universe\n" +
                "s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\n" +
                "u <name>: make <name> the center of the universe\n" +
                "q: quit game");
        // creates a scanner to prompt the user for key inputs
        Scanner scn = new Scanner(System.in);
        String start = actor;
        // establishes the center of the universe, starts as Kevin Bacon
        System.out.println(start + " is now the center of the acting universe, connected to " + (BaconGraphLib.bfs(graph, start).numVertices() - 1) +
                " actors with average separation " + BaconGraphLib.averageSeparation(BaconGraphLib.bfs(graph, start), start));
        String task = scn.next();
        // if the task is not to quit, continue
        while (!task.equals("q")) {
            System.out.print(" > ");
            task = scn.nextLine();
            // if the task is c, sort the actors by how far they are from the center of the universe
            if (task.equals("c")) {
                ArrayList<String> centers = new ArrayList<String>();
                for (String vertex : actors.values()) {
                    centers.add(vertex);
                }
                System.out.println("Actors sorted by optimal separation:");
                String finalStart = start;
                centers.sort((n1, n2) -> BaconGraphLib.getPath(BaconGraphLib.bfs(graph, finalStart), n2).size() - BaconGraphLib.getPath(graph, finalStart).size());
                System.out.println(centers);
            // if the task is d, print a list of actors within input degrees
            } else if (task.equals("d")) {
                // prompt user for minimum and maximum degrees
                System.out.println("Enter the minimum degree: ");
                int low = scn.nextInt();
                System.out.println("Enter the maximum degree: ");
                int high = scn.nextInt();

                // if the high and low do not make sense, quit
                if (high < low) {
                    return;
                }
                // array list and graph to track actors within degrees and the bfs graph
                ArrayList<String> actorList = new ArrayList<String>();
                Graph<String, Set<String>> pathTree = BaconGraphLib.bfs(graph, start);
                // for each actor in the tree, add the actor to the array list
                for (String vertex : pathTree.vertices()) {
                    actorList.add(vertex);
                }
                // sort the actor list by degrees
                actorList.sort((n1, n2) -> pathTree.inDegree(n2) - pathTree.inDegree(n1));

                // print the actors within the given degrees
                System.out.print("Actors sorted from low degree to high degree: ");
                for (int i = 0; i < actorList.size(); i++) {
                    if (graph.inDegree(actorList.get(i)) >= low && graph.inDegree(actorList.get(i)) <= high) {
                        System.out.println(actorList.get(i) + " : " + graph.inDegree(actorList.get(i)));
                    }
                }
            // if the task is i, print the actors that have infinite separation from the center
            } else if (task.equals("i")) {
                // calls missing vertices on the bfs'd graph
                System.out.println(BaconGraphLib.missingVertices(graph, BaconGraphLib.bfs(graph, start)));
            // if the task is p, print the path from the user input actor to the center
            } else if (task.equals("p")) {
                // prompts the user for an actor name
                System.out.println("Enter the name for the desired path: ");
                String name = scn.nextLine();
                // creates an array list to track the actors on the path
                ArrayList<String> list = (ArrayList<String>) BaconGraphLib.getPath(BaconGraphLib.bfs(graph, actor), name);
                // prints the actor's degree
                System.out.println(name + "'s degree is " + (list.size() - 1));
                // loops through the actors in the path and prints their name and the movie that connects them to the center
                System.out.println("Visual representation of the path: ");
                for(int i = 0; i < list.size(); i++) {
                    if(i + 1 < list.size()) {
                        System.out.print(list.get(i) + " was in " + graph.getLabel(list.get(i), list.get(i+1)) + " with " + list.get(i+1));
                    }
                }
            // if the task is s, print the actors with the inputted separation from the center
            } else if(task.equals("s")) {
                // prompts the user for minimum and maximum separations from the center
                System.out.println("Enter the minimum separation: ");
                int low = scn.nextInt();
                System.out.println("Enter the maximum separation: ");
                int high = scn.nextInt();
                // handles if the high and low do not make sense
                if(high < low) {
                    return;
                }
                // creates a map to track the actor's distance from the center
                else {
                    HashMap<Integer, Set<String>> actorDistance = new HashMap<Integer, Set<String>>();
                    // loops through each actor in the map
                    for(String current : actors.values()) {
                        // creates a set to track the actors within the separation values
                        HashSet<String> actorList = new HashSet<String>();
                        // gets the path length for the actor
                        int i = BaconGraphLib.getPath(BaconGraphLib.bfs(graph, start), current).size();
                        // if the path length is between the input values, add the actor to the list and put them in the map
                        if(i >= low && i <= high) {
                            actorList.add(current);
                            actorDistance.put(i, actorList);
                        }
                    }
                    // for each actor in the set, print their name
                    for(Set<String> actorSet : actorDistance.values()) {
                        for(String act : actorSet) {
                            System.out.println(act);
                        }
                    }
                    System.out.println("These actors are within the provided separation values.");
                }
            // if the task is u, reset the center of the universe
            } else if (task.equals("u")) {
                task = scn.next();
                // handles if the person does not exist in the map
                while (!graph.hasVertex(task)) {
                    System.out.println("Invalid input.");
                    task = scn.next();
                }
                // resets the center to the input value
                start = task;
                // prints a message with the new center
                System.out.println(start + " is the new center of the acting universe, connected to " + (BaconGraphLib.bfs(graph, start).numVertices() - 1) +
                        " actors with average separation " + BaconGraphLib.averageSeparation(BaconGraphLib.bfs(graph, start), start));
            }
        }
    }

    // method to run the game
    public static void main(String[] args) throws Exception {
        // instantiates objects for the game and the graph
        BaconGame game = new BaconGame();
        Graph gr = game.createGraph();
        // plays the game with the center starting as Kevin Bacon
        game.playGame("Kevin Bacon", gr);
    }
}