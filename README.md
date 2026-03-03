## Overview

This desktop application converts a video game’s “dungeons” to unweighted, undirected graphs, evaluating traversal strategies with breadth-first search (BFS) and depth-first search (DFS). The tool verbally describes both types of search to instruct the user and counts the number of iterations for both implementations.

## Problem Definition

In terms of fundamental design, a game’s dungeons are little beyond unweighted, undirected graphs. There are two primary options to visit all nodes in such a simple graph: depth-first search (DFS) or breadth-first search (BFS). 

To follow DFS is to move as far as possible in one direction before reaching a dead end, retreating to the most recent fork in paths, picking a different direction, and repeating the process until there are no more nodes to visit.

To follow BFS is to search all adjacent nodes before advancing to the next node with more adjacent nodes, repeating the process until there are no more nodes to visit.

The application implements both, counting the number of iterations for each.

## Input Format

This application receives as input a graph file of no more than 1,000 characters. Each line in the file contains two distinct numbers separated by a space, representing an edge.

Traversal begins at the node containing 0. In the event of invalid input, the application instructs the user accordingly.

## Error Handling

The application validates the input file’s name, catching IOExceptions and IllegalArgumentExceptions during parsing if the file contents are malformed (e.g., extra line breaks, more than two integers on a single line, etc). As for unreachable nodes or nodes that lead only to themselves, no error reporting is necessary. In such cases, the search simply ignores any nodes that are unreachable from node 0.

## Graph Construction Model

Beside the actual pathfinder, the program also contains a node class. Each node object has an integer ID, a boolean to determine whether the search has reached the node, and an ArrayList of neighboring nodes.

To construct a graph, the program instantiates nodes with consecutive integer values up to the greatest integer in the graph file, then rereads the file to discern all unique neighbors for each node. From here, the program may begin the search. 

## BFS Algorithm

defineRouteBFS() outlines the BFS path in the terminal for the user, calculating the number of iterations before the search concludes. Note that the number of iterations reflects the iterations of the implementation rather than the algorithm. 

First, this method declares five variables: pathQueue stores all nodes leading to further adjacent nodes, currNode notes the current node from which to search, currMsg stores the current message to the user, displayMsg determines whether to print currMsg in the terminal, and iterations stores the current number of iterations.

Initializing currNode to node 0, the method begins the search by flagging the first node and adding that node to pathQueue. While pathQueue contains elements, the method increases the number of iterations, sets currNode to whatever node pathQueue removes, updates currMsg with the current node, and sets displayMsg to false.

Also, for every node neighboring the current node, the method increases the number of iterations, and if the search has not flagged the neighbor, sets displayMsg to true, flags the neighbor, adds the neighbor to pathQueue, and updates currMsg with the neighbor’s ID. If the message should appear in the terminal, the method prints the message. This process repeats inside the while loop until pathQueue is empty.

Finally, the method unflags all nodes to reset the search, displaying the number of iterations the method needed to complete the process.

## DFS Algorithm

defineRouteDFS() outlines the DFS path in the terminal for the user, calculating the number of iterations before the search concludes. Note that the number of iterations reflects the iterations of the implementation rather than the algorithm.

First, this method declares four variables: pathStack stores all nodes leading to further adjacent nodes, currNode notes the current node from which to search, shouldPop determines whether pathStack should pop its top element, and iterations stores the current number of iterations.

Initializing currNode to node 0, the method begins the search by flagging the first node, pushing that node onto pathStack, and notifying the user that the search has begun. While pathStack contains elements, the method increases the number of iterations, sets shouldPop to true, and sets the current node to the element at the top of the stack.

Also, for every node neighboring the current node, the method increases the number of iterations, and if the search has not flagged the neighbor, flags the neighbor, sets the current node to the neighbor as the method pushes that neighbor onto the stack, sets shouldPop to false, and prints the appropriate message for the user. If shouldPop is true, the method pops the top element from pathStack. This process repeats inside the while loop until pathStack is empty.

Finally, the method unflags all nodes to reset the search, displaying the number of iterations the method needed to complete the process.

## Constraints and Assumptions

Though this application conducts both BFS and DFS, the ideal option truly depends on the dungeon. BFS performs better in shallow graphs with high branching – like dungeons in video games. In such graphs, level-by-level exploration with BFS obviates the shortest route to a search object. DFS may be more memory-efficient in deeper, narrower graphs.

## Example Input

One of the demo graph files (map0.graph) in the available folder on GitHub looks like this:
<i>

0 1

2 0

1 11

2 3

4 3

3 5

5 6

6 7

5 9

2 0

3 4

5 8

6 7

6 7

6 7

3 10

10 11

11 12

13 7

14 4

5 15

15 17

16 15

15 14
</i>

Duplicate edges remain to demonstrate the application’s ability to handle such an error.

## Example Output

The application’s evaluation of this graph unfolds as follows:
<i>

Alright, then.  Try this.  Unless you encounter obstructions in
any of the rooms, you shouldn't need to take any detours.

BFS results in:

From 0, clear: 1 2

From 2, clear: 3

From 3, clear: 4 5 10

Move (possibly across discovered rooms) to 10. From there, clear: 11

Move (possibly across discovered rooms) to 11. From there, clear: 12

Move (possibly across discovered rooms) to 5. From there, clear: 6 9 8 15

Move (possibly across discovered rooms) to 15. From there, clear: 17 16 14

Move (possibly across discovered rooms) to 6. From there, clear: 7

Move (possibly across discovered rooms) to 7. From there, clear: 13

Dungeon clear in 56 iterations!

DFS results in:

Starting from 0...

Move to 1

Move to 11

Move to 10

Move (possibly across discovered rooms) to: 3

Move (possibly across discovered rooms) to: 2

Move (possibly across discovered rooms) to: 4

Move (possibly across discovered rooms) to: 14

Move (possibly across discovered rooms) to: 15

Move (possibly across discovered rooms) to: 5

Move (possibly across discovered rooms) to: 6

Move (possibly across discovered rooms) to: 7

Move (possibly across discovered rooms) to: 13

Move (possibly across discovered rooms) to: 9

Move (possibly across discovered rooms) to: 8

Move (possibly across discovered rooms) to: 17

Move (possibly across discovered rooms) to: 16

Move (possibly across discovered rooms) to: 12

Dungeon clear in 108 iterations!
</i>

The application iterates through the graph in accordance with BFS and DFS, listing nodes in traversal order and displaying adjacent candidates at each step.

## Limitations

The application lacks quantitative performance analysis beyond iteration counting. A more robust implementation would measure execution time and memory consumption, detailing the degree by which one search type is more efficient.

In terms of performance for both types of search, space complexity is O(V + E) because the program stores multiple nodes and edges as well as a stack of nodes, and time complexity is also O(V + E) because the current implementation processes each node and each edge only once during traversal.

## Future Improvements

If the application received much larger graph files as input, users could track the number of moves each search makes, thereby obviating the best option or confirming that there would have been no difference either way. An especially advanced version of this tool would store results across multiple input files for more detailed comparisons.
