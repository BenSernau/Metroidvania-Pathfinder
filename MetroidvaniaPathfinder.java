import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;

/*

How to provide input:

1. Make a CNF file in the same directory as this.

2. Write integers in the file such that two integers occupy each line. You 
must leave one space between the two integers on the line. This program
will recognize zero as the root node.

3. Provide your CNF file as an argument when you run the program.

SUGGESTION: For an easily identifiable boss/exit node, have that be the highest number.



*/

class Node 
{
	int self = 0;
	boolean flagged = false;

	Node(int s)
	{
		self = s;
	}
}

class Edge
{
	Node nodeA, nodeB;

	Edge(Node a, Node b)
	{
		nodeA = a;
		nodeB = b;
	}
}

class MetroidvaniaPathfinder
{
	static ArrayList<Node> nodes = new ArrayList<Node>();
	static ArrayList<Edge> edges = new ArrayList<Edge>();

	private static void readEdges(String ef)
	{
		Edge currEdge = new Edge(new Node(0), new Node(0));
		int prev = 0, nodeNum = 0;
		String currLine = null;
		String[] currRow = new String[0];

		try
		{
			BufferedReader buffRead = new BufferedReader(new FileReader(ef));

			buffRead.mark(1000);

			while ((currLine = buffRead.readLine()) != null)
			{
				currRow = currLine.split(" ");
				nodeNum = Math.max(prev, (Math.max(Integer.parseInt(currRow[0]), Integer.parseInt(currRow[1]))));
				prev = nodeNum;
			}

			for (int i = 0; i < nodeNum + 1; i++)
			{
				nodes.add(new Node(i));
			}

			buffRead.reset();

			while ((currLine = buffRead.readLine()) != null)
			{
				currRow = currLine.split(" ");
				currEdge = new Edge(nodes.get(Integer.parseInt(currRow[0])), nodes.get(Integer.parseInt(currRow[1])));
				edges.add(currEdge);
			}

			buffRead.close();
		}

		catch(FileNotFoundException ex) 
		{
		    System.out.println("ERROR: The file cannot open.");                
		}

		catch(IOException ex) 
		{
			ex.printStackTrace();
		}	

		catch(IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}
	}

	private static void defineRoute()
	{
		Queue<Node> pathQueue = new LinkedList<Node>(); //Iterate through the graph with a queue.
		boolean allFlagged = false; //Track whether the search has reached all nodes.
		boolean showStart = true; //Decide whether to search rooms from the current node.
		Node rememberNode = new Node(0); //Store the most recent node with further adjacent nodes.

		nodes.get(0).flagged = true; //Flag the first node, which is always the one containing 0.

		pathQueue.add(nodes.get(0)); //Add the first node to the queue.

		while (!allFlagged) //While the search hasn't reached all nodes,
		{
			for (int i = 0; i < edges.size(); i++) //for every edge,
			{
				if (pathQueue.peek() == null) //if nothing is at the front of the queue,
				{
					System.out.println("\nERROR: UNREACHABLE NODE FOUND\n"); //throw an error.
					return;
				}

				else if (edges.get(i).nodeA.self == pathQueue.peek().self && !edges.get(i).nodeB.flagged) //if one of this edge's nodes is at the front of the queue and the search hasn't reached the opposite node, 
				{	
					if (rememberNode.self != pathQueue.peek().self) //if the last node with further adjacent nodes is not at the top of the queue,
					{
						System.out.println("\nMove (possibly across discovered rooms) from room " + Integer.toString(rememberNode.self) + " to room " + Integer.toString(pathQueue.peek().self)); //tell the user to move from rememberNode to the node at the top of the queue.
					}

					if (showStart) //if the user should search rooms from the current node,
					{
						System.out.print("\nFrom room " + Integer.toString(pathQueue.peek().self) + ", investigate the following adjacent room(s):"); //tell the user to investigate them
						showStart = false; //and change the boolean.
					}

					rememberNode = pathQueue.peek(); //update rememberNode to the current node.
					System.out.print(" " + Integer.toString(edges.get(i).nodeB.self)); //print the opposite node to search.
					edges.get(i).nodeB.flagged = true; //flag the opposite node.
					pathQueue.add(edges.get(i).nodeB); //add the opposite node to the queue.	
				}

				else if (edges.get(i).nodeB.self == pathQueue.peek().self && !edges.get(i).nodeA.flagged)
				{
					if (rememberNode.self != pathQueue.peek().self)
					{
						System.out.println("\nMove (possibly across discovered rooms) from room " + Integer.toString(rememberNode.self) + " to room " + Integer.toString(pathQueue.peek().self));
					}

					if (showStart)
					{
						System.out.print("\nFrom room " + Integer.toString(pathQueue.peek().self) + ", investigate the following adjacent room(s):");
						showStart = false;
					}

					rememberNode = pathQueue.peek();
					System.out.print(" " + Integer.toString(edges.get(i).nodeA.self));
					edges.get(i).nodeA.flagged = true;
					pathQueue.add(edges.get(i).nodeA);
				}
			}

			if (!showStart) //if the user needn't search rooms from the current node,
			{
				System.out.print("\n"); //print a line break in the terminal.
			}

			allFlagged = true; //change the boolean.

			for (int i = 0; i < nodes.size(); i++) //for all nodes,
			{
				if (!nodes.get(i).flagged) //if the search hasn't reached this node,
				{
					showStart = true; //change the boolean.
					allFlagged = false; //reset allFlagged to continue the search.
					pathQueue.remove(); //remove this node to backtrack.
					break; //break from the for loop.
				}
			}
		}

		System.out.println("\nDungeon clear!\n"); //Tell the user the dungeon is complete.
	}

	private static boolean nodesConsecutive() //Checks if the nodes are labeled consecutively.  See error message in this method for details.
	{
		boolean amongEdges;

		for (int i = 0; i < nodes.size(); i++)
		{
			amongEdges = false;

			for (int j = 0; j < edges.size(); j++)
			{
				if (nodes.get(i).self == edges.get(j).nodeA.self || nodes.get(i).self == edges.get(j).nodeB.self)
				{
					amongEdges = true;
					break;
				}
			}

			if (!amongEdges)
			{
				System.out.println("\nERROR: The numbers used to label nodes must be CONSECUTIVE. For example, you\ncan have a graph that consists exclusively of nodes 1, 2, 3, and 4, but\nyou can't have one that consists exclusively of nodes 1, 2, 3, and 17.\n");
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) 
	{
		Scanner reader = new Scanner(System.in);

		System.out.println("\nWelcome to the Metroidvania Pathfinder, my most practical invention.");
		System.out.println("\nHow to provide input:");
		System.out.println("\n1. Make a CNF file in the same directory as this.");
		System.out.println("\n2. Write integers in the file such that two integers occupy each line. You\nmust leave one space between the two integers on the line. This program\nwill recognize zero as the root node.");
		System.out.println("\n3. Provide your CNF file as an argument when you run the program.");
		System.out.println("\nSUGGESTION: For an easily identifiable boss/exit node, have that be the highest number.");
		System.out.println("\nPass the requisite CNF file to continue.");

		readEdges(reader.next());

		if (nodesConsecutive())
		{
			System.out.print("\nAlright, then.  Try this.  Unless you encounter obstructions in\nany of the rooms, you shouldn't need to take any detours.\n");
			defineRoute();
		}
	}
}
