/* ShortestPaths.java

   Author: Shrey Patel
   
   This project includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
   java ShortestPaths
   
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
   java ShortestPaths file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
   <number of vertices>
   <adjacency matrix row 1>
   ...
   <adjacency matrix row n>
   
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
   
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   
*/

import java.util.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Iterator;


//Do not change the name of the ShortestPaths class
public class ShortestPaths{

	// number of vertices
	public static int n;
	//distance used to store the distance of vertex from a source
	static int [] distance;
	//stores the prev vertex, which will be used to back track path from source to dest
	static int [] prev;
	
    /* ShortestPaths(adj) 
       Given an adjacency list for an undirected, weighted graph, calculates and stores the
       shortest paths to all the vertices from the source vertex.
       
       The number of vertices is adj.length
       For vertex i:
         adj[i].length is the number of edges
         adj[i][j] is an int[2] that stores the j'th edge for vertex i, where:
           the edge has endpoints i and adj[i][j][0]
           the edge weight is adj[i][j][1] and assumed to be a positive integer
       
       All weights will be positive.
    */
    static void ShortestPaths(int[][][] adj, int source){
		
		n = adj.length;
		distance = new int[n];
		prev = new int[n];

		//Initialize all the distance to infinity
		for (int i = 0; i <n ; i++) {
			distance[i] = Integer.MAX_VALUE;
			prev[i] = -1;
		}

		//Initialize priority queue
		IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(n);

		distance[source] = 0;
		pq.insert(distance[source], 0);

		while(!pq.isEmpty()){

			int u = pq.delMin();
			
			for (int i=0; i<adj[u].length; i++){
				int v = adj[u][i][0];
				int edgeWeight = adj[u][i][1];
				int potentialWeight = distance[u] + edgeWeight;
				if(distance[v] > potentialWeight){
					distance[v] = potentialWeight;
					prev[v] = u;

					if(!pq.contains(v)){
						pq.insert(v, potentialWeight);
					} else {
						pq.decreaseKey(v, potentialWeight);
					}
				}
			}

		}
	}
	
	static void printPath(int dest, int retainedDest) 
	{ 
		// Base Case : If j is source 
		if (prev[dest] == - 1) 
			return;

		printPath(prev[dest], retainedDest); 
	
		System.out.printf("%d", dest);

		if (dest != retainedDest)
			System.out.printf(" --> ");

	}
    
    static void PrintPaths(int source){

		for (int i = 1; i < n; i++) 
		{ 
			System.out.printf("The path from %d to %d is: %d --> ", 
						source, i, source); 
			printPath(i, i);
			System.out.printf(" and the total distance is : %d\n", 
						distance[i]);
		} 

    }
	
    /* main()
       Contains code to test the ShortestPaths function. You may modify the
       testing code if needed, but nothing in this function will be considered
       during marking, and the testing process used for marking will not
       execute any of the code below.
    */
    public static void main(String[] args) throws FileNotFoundException{
	Scanner s;
	if (args.length > 0){
	    //If a file argument was provided on the command line, read from the file
	    try{
		s = new Scanner(new File(args[0]));
	    } catch(java.io.FileNotFoundException e){
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n",args[0]);
	}
	else{
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}
	
	int graphNum = 0;
	double totalTimeSeconds = 0;
	
	//Read graphs until EOF is encountered (or an error occurs)
	while(true){
	    graphNum++;
	    if(graphNum != 1 && !s.hasNextInt())
		break;
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();
	    int[][][] adj = new int[n][][];
	    
	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++){
		LinkedList<int[]> edgeList = new LinkedList<int[]>(); 
		for (int j = 0; j < n && s.hasNextInt(); j++){
		    int weight = s.nextInt();
		    if(weight > 0) {
			edgeList.add(new int[]{j, weight});
		    }
		    valuesRead++;
		}
		adj[i] = new int[edgeList.size()][2];
		Iterator it = edgeList.iterator();
		for(int k = 0; k < edgeList.size(); k++) {
		    adj[i][k] = (int[]) it.next();
		}
	    }
	    if (valuesRead < n * n){
		//System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		break;
	    }
	    
	    // output the adjacency list representation of the graph
	    // for(int i = 0; i < n; i++) {
	    // 	System.out.print(i + ": ");
	    // 	for(int j = 0; j < adj[i].length; j++) {
	    // 	    System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
	    // 	}
	    // 	System.out.print("\n");
	    // }
	    
	    long startTime = System.currentTimeMillis();
	    
	    ShortestPaths(adj, 0);
	    PrintPaths(0);
	    long endTime = System.currentTimeMillis();
	    totalTimeSeconds += (endTime-startTime)/1000.0;
	    
	    //System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
	}
	graphNum--;
	System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
    }
}
