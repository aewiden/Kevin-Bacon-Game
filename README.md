# Kevin Bacon Game
## Avery Widen (aewiden)

# Background
In this problem set you will write code for social network analysis, and you will use it in variations on the Kevin Bacon game.

In the Kevin Bacon game, the vertices are actors and the edge relationship is "appeared together in a movie". The goal is to find the shortest path between two actors. Traditionally the goal is to find the shortest path to Kevin Bacon, but we'll allow anybody to be the center of the acting universe. We'll also do some analyses to help find better Bacons. Since "degree" means the number of edges to/from a vertex, I'll refer to the number of steps away as the "separation" rather than the common "degrees of separation".

The easiest way to play the Kevin Bacon game is to do a breadth-first search (BFS), as covered in lecture. This builds a tree of shortest paths from every actor who can reach Kevin Bacon back to Kevin Bacon. More generally, given a root, BFS builds a shortest-path tree from every vertex that can reach the root back to the root. It is a tree where every vertex points to its parent, and the parent is the next vertex in a shortest path to the root. For the purposes of this assignment, we will store the tree as a directed graph. Once the tree is constructed, we can find the vertex for an actor of interest, and follow edges back to the root, tracking movies (edge labels) and actors (vertices) along the way.

# Inputs
Datasets are provided in bacon.zip, both a simple set of test files for the graph illustrated above, and a large set of actor-movie data (thanks to Brad Miller at Luther College). The three main files, actors.txt, movies.txt, and movie-actors.txt are large — 9,235 actors, 7,067 movies, and 21,370 movie-actor pairs, resulting in 32,337 edges. So while you are developing your program use the smaller versions actorsTest.txt, moviesTest.txt, movie-actorsTest.txt.

The files are all formatted the same way. Each line has two quantities separated by a "|". In the actors file the quantities are actorID and actorName. In the movies file they are movieID and movieName. In the movies-actors file they are movieID and actorID, indicating that the actor associated with actorID appeared in the movie associated with movieID.

Use the file contents to build a graph whose vertices are the actor names (not IDs), and whose edges are labeled with sets of the movie names (again, not IDs) in which actors appeared together. So an edge is created when two actors costar, its label is a set of strings, and each movie in which they costar is added to the edge label. You can assume that no movie appears twice in the movies file and that no actor appears twice in the actors file.

You may find it useful to create maps for mapping IDs to actor names and IDs to movie names. You can also use a map to figure out which actors appeared in each movie, and can use that information to add the appropriate edges to the graph. This may take a little thought, but try it by hand on the small data set given above.

A piece of advice about processing the lines of the input files. For each line of each file, you will need to find the string to the left of the pipe symbol and the string to the right of the pipe symbol. I suggest using the familiar split method that we've used before for String objects. One catch here is that the pipe character is a special character that you will need to escape.

# The Game Interface
You may devise your own interface for playing the game, and need not follow the structure of the sample solution above. But you do need to support the following functionality:

  - change the center of the acting universe to a valid actor
  - find the shortest path to an actor from the current center of the universe
  - find the number of actors who have a path (connected by some number of steps) to the current center
  - find the average path length over all actors who are connected by some path to the current center
In addition, your program should help you find other possible Bacons, according to two different criteria:

  - degree (number of costars). This is the same as in the short assignment.
  - average separation (path length) when serving as center of the universe. This simply requires looping over all the actors one by one. Consider each one as the center of the      universe, find the average path length using the method above, and store it away for sorting. So just a few lines of code. It could take some time, but under a minute on my machine with the sample solution. Since not all actors are in the Bacon universe (have a path to him), you can limit the result to those that are.

Java notes re the command-line interface. The info retrieval lecture demonstrated the use of Scanner to get a line of input. Also note that Integer.parseInt will extract an int from a String (throwing an exception if the format is invalid).

Credit to Dartmouth CS10.
