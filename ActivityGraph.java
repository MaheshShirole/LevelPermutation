package ScenarioGenerator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import utils.Factorial;
import utils.PermutationGenerator;



public class ActivityGraph {
	private Map<Integer, Activity> nodes ;
	private LinkedList<Integer> [] edges;
	@SuppressWarnings("unchecked")
	private Hashtable activities;  
	private int numOfVertices=0;
	private int maxLevelNumber;
	private int maxPaths=0;
	
	private int rootNode;
	
	@SuppressWarnings("unchecked")
	public 	ActivityGraph(){
			numOfVertices=0;
			nodes = new HashMap<Integer, Activity>();
			//edges = (LinkedList<Integer>[]) new LinkedList[numOfVertices];
			activities = new Hashtable();
			
	}
	
	public int getRootNode(){
		return rootNode;
	}
	@SuppressWarnings("unchecked")
	public void addNode(String umlid, Activity node){
		node.setId(numOfVertices);
		//System.out.println(node.getType());
		if(node.getType().equals("Start"))
			rootNode=numOfVertices;
		nodes.put(numOfVertices, node);
		//System.out.println("Node "+node);
		
		activities.put(umlid, numOfVertices);
		numOfVertices++;
		//edges[numOfVertices]= new LinkedList<Integer>();
	}
	@SuppressWarnings("unchecked")
	public void initializeEdgeSet(){
		edges = (LinkedList<Integer>[]) new LinkedList[numOfVertices];
		for(int i=0;i<numOfVertices;i++){
			edges[i]= new LinkedList<Integer>();
			
		}
	}
	
	public void addEdge(String Source, String Destination){
		//System.out.println("Source:" + Source+"==>"+activities.get(Source));
		//System.out.println("Destination:" + Destination+"==>"+(Integer)activities.get(Destination));
		int source = (Integer) activities.get(Source);
		int destination = (Integer) activities.get(Destination);
		edges[source].addLast(destination);
		Activity temp =this.getActivity(destination);
		temp.addPredecessor(source);
	}
	public String toString(){
		StringBuffer s = new StringBuffer();
		
		s.append("Graph G(Vertices, Edges): \n");
		s.append("Vertices {\t");
		for(int i=0;i<numOfVertices;i++){
			s.append(nodes.get(i));
			if(i<numOfVertices-1)
				s.append(",\n\t\t");
		}
		s.append("\n\t}\nEdges\t{");
		String ed="";
		for(int i=0;i<numOfVertices;i++){
			int vertices=0;
			for(int j=0;j< edges[i].size();j++){
				ed=ed+"("+i+","+edges[i].get(j)+")";
				vertices++;
				if(j<edges[i].size()-1)
					ed=ed+",";
			}
			if(i<numOfVertices-1)
				if(vertices>0)
					ed=ed+",\n\t";
		}
		
		s.append(ed+"}\n");
		s.append("Vertices predesessors {\t");
		for(int i=0;i<numOfVertices;i++){
			//s.append(nodes.get(i));
			Activity temp=nodes.get(i);
			s.append(temp);
			s.append(temp.getPredecessor());
			if(i<numOfVertices-1)
				s.append(",\n\t\t");
		}
		
		return s.toString();
	}
	
	Activity getUnvisitedChildNode(Activity n){
		//System.out.print(n);
		int i =n.getId(); // get id of the activity n
		//System.out.println("Inside Unvisited node");
		//System.out.println("Node n Id="+i);
		//System.out.println("number of edged of Node n ="+edges[i].size());
		for(int j=0;j< edges[i].size();j++){
			Activity child = nodes.get(edges[i].get(j)); //get child node of the node n
		//	System.out.println("Child Node of Node n "+child);
		//	System.out.println("child Node visited status="+child.getVisited());
			if(!child.getVisited()){
				return child;
			}
		}
		return null;
	}
	//Utility methods for clearing visited property of node
	 public void clearNodes()
	 {
		 for(int i=0;i<numOfVertices;i++){
			 Activity node = nodes.get(i); //get each node of the tree
			 node.setVisited(false); // set all visited flag to false
			 node.clearVisitCount(); // clear visit count to zero.
		 }
		 
	 }
	//BFS traversal 
	 @SuppressWarnings("unchecked")
	public void bfs()
	 {
	 
		 //	BFS uses Queue data structure
		 Queue q=new LinkedList();
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 q.add(temp);// add root node in the queue
		 temp.setVisited(true); // set visited flag true
		 System.out.println(temp);
		 while(!q.isEmpty()){
			 //System.out.println("Queue not empty");
			 Activity n=(Activity)q.remove();
			 Activity child=null;
			 while((child=getUnvisitedChildNode(n))!=null)
			 {
				 //System.out.println("Inside child");
				 child.setVisited(true); // set visited flag true
				 System.out.println(child);
				 q.add(child);
			 }
		 }
		 this.clearNodes();
	 }
	 
	//DFS traversal 
	 @SuppressWarnings("unchecked")
	public void dfs()
	 {
		 //DFS uses Stack data structure
		 Stack s=new Stack();
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 s.push(temp);// add root node in the Stack
		 temp.setVisited(true); // set visited flag true
		 System.out.println(temp);
		 while(!s.isEmpty())
		 {
			 Activity n=(Activity)s.peek();
			 Activity child=getUnvisitedChildNode(n);
			 if(child!=null)
			 {
				 child.visited=true;
				 System.out.println(child);
				 s.push(child);
			 }
			 else
			 {
				 s.pop();
			 }
		 }
		 this.clearNodes();
	 }
	 
		//retospective DFS traversal
	 /*
	  * This algotithm traverse path usign DFS 
	  * then from final node it pop each node 
	  * upto node which has at least one unvisited node 
	  * and start traversing till all nodes visited. 
	  */
	 @SuppressWarnings("unchecked")
	public void rettospective_dfs()
	 {
		 //DFS uses Stack data structure
		 Stack s=new Stack();
		 Stack dataStack = new Stack();
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 s.push(temp);// add root node in the Stack
		 dataStack.push(temp);
		 temp.setVisited(true); // set visited flag true
		 System.out.println(temp);
		 while(!s.isEmpty())
		 {
			 Activity n=(Activity)s.peek();
			 Activity child=getUnvisitedChildNode(n);
			 if(child!=null)
			 {
				 child.visited=true;
				 System.out.println(child);
				 s.push(child);
				 dataStack.push(child);
			 }
			 else
			 {
				 s.pop();
			 }
		 }
		 this.clearNodes();
	 }

	 // DFS-BFS traversal for concurrent nodes from fork-join pair
	 // This algorithm run DFS search up to Fork node then BFS search upto Join node and repeat
	 @SuppressWarnings("unchecked")
	public void dfs_bfs(){
		 // DFS uses Stack data structure
		 Stack s = new Stack();
		 //BFS uses queue data structure
		 Queue q=new LinkedList();
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 s.push(temp); // push root node on Stack
		 temp.setVisited(true); // set visited flag true
		 System.out.println(temp);
		 while(!s.isEmpty()){
			 Activity n=(Activity)s.peek(); // get top element on stack
			 //check if top element of stack is Fork node
			 if(n.getType().equals("Fork")){
				 //Start BFS search up to next Join node
				 q.add(n);// add root node in the queue
				 n.setVisited(true); // set visited flag true
				 //System.out.println(n);
				 while(!q.isEmpty()){
					 //System.out.println("Queue not empty");
					 n=(Activity)q.remove();
					 Activity child=null;
					 while((child=getUnvisitedChildNode(n))!=null)
					 {
						 //System.out.println("Inside child");
						 child.setVisited(true); // set visited flag true
						 System.out.println(child);
						 //check if child node is Join
						 if(!child.getType().equals("Join")){
							 q.add(child);
						 }else{
							 n=child;
						 }
					 }
				 }
				 
				 // start the DFS search				 
			 }
 
			 Activity child=getUnvisitedChildNode(n);
			 if(child!=null)
			 {
				 child.visited=true;
				 System.out.println(child);
				 s.push(child);
			 }
			 else
			 {
				 s.pop();
			 }
			 if(n.getType().equals("End"))
				 break;	
		 }
		 this.clearNodes();
	 }
	 // random selesction of unvisited child 
		Activity getRandomUnvisitedChildNode(Activity n){
			//System.out.print(n);
			int i =n.getId(); // get id of the activity n
			//System.out.println("Inside Unvisited node");
			//System.out.println("Node n Id="+i);
			//System.out.println("number of edged of Node n ="+edges[i].size());
			int outNodes=edges[i].size();
			for(int j=0;j<outNodes*5;j++){
				int randomNeighbour;
				Random randomGen= new Random();
				randomNeighbour=randomGen.nextInt(edges[i].size());
				//System.out.println(randomNeighbour);
				Activity child = nodes.get(edges[i].get(randomNeighbour)); //get child node of the node n
			//	System.out.println("Child Node of Node n "+child);
			//	System.out.println("child Node visited status="+child.getVisited());
				if(!child.getVisited()){
					return child;
				}
			}
			return null;
		}
	 
	//Random DFS traversal select the neighbour randomly from available neighbours 
	 @SuppressWarnings("unchecked")
	public void randomDfs()
	 {
		 //DFS uses Stack data structure
		 Stack s=new Stack();
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 s.push(temp);// add root node in the queue
		 temp.setVisited(true); // set visited flag true
		 System.out.println(temp);
		 while(!s.isEmpty())
		 {
			 Activity n=(Activity)s.peek();
			 Activity child=getRandomUnvisitedChildNode(n);
			 if(child!=null)
			 {
				 child.visited=true;
				 System.out.println(child);
				 s.push(child);
			 }
			 else
			 {
				 s.pop();
			 }
		 }
		 this.clearNodes();
	 }

	 //Modified DFS1 traversal
	 //This algorith use visited count to select the next node to explore
	 //Next node will be the node with minimum visite count
	 @SuppressWarnings("unchecked")
	public void modified_dfs1()
	 {
		 //DFS uses Stack data structure
		 Stack s=new Stack();
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 s.push(temp);// add root node in the queue
		 temp.setVisited(true); // set visited flag true
		 temp.setVisitCount(); // increase visit count by one
		 System.out.println(temp);
		 while(!s.isEmpty())
		 {
			 Activity n=(Activity)s.peek();
			 Activity child=getLessVisitedChildNode(n);
			 if(child!=null)
			 {
				 child.setVisited(true);
				 child.setVisitCount();
				 System.out.println(child);
				 s.push(child);
			 }
			 else
			 {
				 s.pop();
			 }
			 if(child.getType().equals("End"))
				 break;			 
		 }
		 this.clearNodes();
	 }
	public Activity getLessVisitedChildNode(Activity n){
			int i =n.getId(); // get id of the activity n
			int count =10000;
			int lastchild=-1;
			
			for(int j=0;j< edges[i].size();j++){
				Activity child1 = nodes.get(edges[i].get(j)); //get child node of the node n
				if(!child1.getVisited()){
					if(child1.getVisitCount()< count){
						//System.out.println("Node: "+child1+" Count : "+child1.getVisitCount());
						lastchild=j;
						count=child1.getVisitCount();
						//System.out.println("Lastchild :"+lastchild);
					}
					
				}
			}
			if(lastchild>=0){
					Activity temp=nodes.get(edges[i].get(lastchild)); 
					//System.out.println("Final selected Node: "+temp+" Count : "+temp.getVisitCount());
					return temp;
			}
			return null;
	}

	 //Modified DFS2 traversal
	 //This algorith selects a branch on the basis of algorithm run count
	 //at each decision point it mask bits and get appropriate branch to select.
	 @SuppressWarnings("unchecked")
	public void modified_dfs2(int run)
	 {
		 //DFS uses Stack data structure
		 Stack s=new Stack();
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 s.push(temp);// add root node in the queue
		 temp.setVisited(true); // set visited flag true
		 System.out.println(temp);
		 while(!s.isEmpty())
		 {
			 Activity n=(Activity)s.peek();
			 // find the id of activity and number of branches out from it
			 int id =n.getId(); // get id of the activity n
			 int branchOut=edges[id].size();
			 int branchNo=0;
			 // if branches are more than one then select appropriate branch on basis of run
			 if(branchOut>1){
				// System.out.println("Branch count :"+branchOut);
				 // find number of bits required to represent branchOut
				 int bits=(int) (Math.log(branchOut)/Math.log(2));
				// System.out.println("Bits reauirted to represent branchout "+bits);
				 branchNo= run & bits;
				// System.out.println("Bits reauirted to represent branchNo "+branchNo);
				 // shift run by these many bits to right
				 run = run>>bits;
				// System.out.println("Run Number "+run);
			 }
			 Activity child=nodes.get(edges[id].get(branchNo)); //get child node of the node n
			 //Activity child=getNextChildNode(n, run);
			 if(child!=null)
			 { 
				 child.setVisited(true);
				 System.out.println(child);
				 s.push(child);
			 }
			 else
			 {
				 s.pop();
			 }
			 if(child.getType().equals("End"))
				 break;			 
		 }
		 this.clearNodes();
	 }
	 
	 // CQS search
	 @SuppressWarnings("unchecked")
	public String cqs(){
		 // First phase : create concurrent queues.
		 Queue main_queue=new LinkedList(); // main queue
		 Queue [] fork_queue=null;
		 Activity temp = nodes.get(rootNode); //get root node of the tree
		 main_queue.add(temp);// add root node in the queue
		 temp.setVisited(true); // set visited flag true
		// System.out.println(temp);
		 Activity child=null;
		 Activity n=temp;
		 while((child=getUnvisitedChildNode(n))!=null)
		 {
			 if(child.getType().equals("Action"))
				 main_queue.add(child);
			 if(child.getType().equals("End"))
				 main_queue.add(child);
			 if(child.getType().equals("Decision")){
				 temp=child;
				 //System.out.println("my data "+temp);
				 // add true part to the queue UPTO MERGER NODE
				 while((child=getUnvisitedChildNode(n))!=null && !(child.getType().equals("Merge"))){
					 main_queue.add(child);
					// System.out.println(child);
					 child.setVisited(true);
					 n=child;
				 }
				 main_queue.add(child); //add merge node
				// System.out.println(child);
				 // add false part to the queue UPTO MERGER NODE
				 n=temp;
				 n.setVisited(false);
				 while((child=getUnvisitedChildNode(n))!=null && !(child.getType().equals("Merge"))){
					 main_queue.add(child);
					// System.out.println(child);
					 child.setVisited(true);
					 n=child;
				 }
				 main_queue.add(child); //add merge node
			 }
			 
			 // fork node create new concurrent queues.
			if(child.getType().equals("Fork")){
				 temp=child;
				 main_queue.add(child); // insert fork node in main queue.
				 int i =child.getId(); // get id of the activity n
				 //System.out.println("my data "+temp+" No of fork out:"+edges[i].size());
				 fork_queue =new Queue[edges[i].size()];
				 for(int j=0;j<edges[i].size();j++){
					 fork_queue[j]= new LinkedList();
					 n=temp;
					 n.setVisited(false);
					 while((child=getUnvisitedChildNode(n))!=null && !(child.getType().equals("Join"))){
						 fork_queue[j].add(child);
						 //System.out.print(child);
						 child.setVisited(true);
						 n=child;
					 }
				 }
				 
				 main_queue.add(child); // insert join node in main queue.

			}
			 //System.out.println(child);
			 n=child;
		 }
		// System.out.println(main_queue);
		// System.out.println(fork_queue[0]);
		// System.out.println(fork_queue[1]);
		 
		 // Second Phase generate search path
		// System.out.println("\nPath Generated by the CQS algorithm");
		 String path="";
		 //System.out.println(" ");
		 while(!main_queue.isEmpty()){
			 //System.out.println("Queue not empty");
			 Activity test=(Activity)main_queue.remove();
			 String type= test.getType();
			 // print action nodes
			 if(type.equals("Action")||type.equals("Start")){
				 //System.out.print(test.getName()+"-");
				 path=path+test.getName()+"-";
			 }
			 if(type.equals("End")){
				 //System.out.print(test.getName()+"-");
				 path=path+test.getName();
			 }

			 // print decision node approapriate part of main que on basis of PreCondition
			 if(type.equals("Decision")){
				 if(test.getPreCondition().equals("true")){
					 // print activities up to merge and then skip activities up to merge
					 //System.out.print(test+" : "+test.getPreCondition()); // print decision node
					 //System.out.print(test.getType()+"-"); // print decision node
					 path=path+test.getName()+"-";
					 while(!main_queue.isEmpty() && !type.equals("Merge")){
						 test=(Activity)main_queue.remove();
						 type= test.getType();
						// System.out.print(test.getName()+"-");
						 path=path+test.getName()+"-";
					 }
					 // Skip activities in false part
					 while(!main_queue.isEmpty() && !type.equals("Merge")){
						 test=(Activity)main_queue.remove();
						 type= test.getType();
					 }
				 }else{
				 // skip activities up to merge and then print activities up to merge  
				 //System.out.print(test.getType()+"-"+" : "+test.getPreCondition()); // print decision node
					 //System.out.print(test.getName()+"-"); // print decision node
					 path=path+test.getName()+"-";
				 // Skip activities in false part
				 while(!main_queue.isEmpty() && !type.equals("Merge")){
					 test=(Activity)main_queue.remove();
					 type= test.getType();
				 }
				 //System.out.println(test.getType());
				 type="abc";
				 // print activities up-to Merger
				 while(!main_queue.isEmpty() && !type.equals("Merge")){
					 //System.out.println(test.getType());
					 test=(Activity)main_queue.remove();
					 type= test.getType();
					 //System.out.print(test.getName()+"-");
					 path=path+test.getName()+"-";
				 }
			 	}// end else
			 }//end if decision
			 // print fork nodes from each queue 
			 if(type.equals("Fork")){
				 //System.out.print(test.getName()+"-");
				 path=path+test.getName()+"-";
				 //System.out.print("Number of Fork Queues : "+fork_queue.length);
				 Random randomGenerator = new Random(); 
				 int current_queue=randomGenerator.nextInt(fork_queue.length);
				 boolean all_qs_over=false;
				 while(!all_qs_over){
					 while(!fork_queue[current_queue].isEmpty()){
					 //print activity from each queue selected randomly.
						 String lastNodeType=type;
						 test=(Activity)fork_queue[current_queue].remove();
						 type= test.getType();
						 //System.out.print(test+" : "+current_queue);
						 //System.out.print(test.getName()+"-");
						 path=path+test.getName()+"-";
						 // if next node is of type send signal then switch the queue
						 Activity a = (Activity)fork_queue[current_queue].peek();
						 if(a!=null && a.getType().equals("Send Signal")){
							// System.out.print("Switch-");
							 path=path+"Switch-";
							 break;
						 }
						 //if last node is of type receive signal then switch the queue
						if(lastNodeType.equals("Receive Signal")){
							 //System.out.print("Switch-");
							 path=path+"Switch-";
							 break;
						}
						// if next node is Unlock
						 if(a!=null && a.getType().equals("Unlock")){
							 //System.out.print("Switch-");
							 path=path+"Switch-";
							 break;
						 }

					 }
					 all_qs_over=true;
					 // check if all queues are empty
					 for(int j=0; j<fork_queue.length;j++){
						 if(!fork_queue[j].isEmpty())
							 all_qs_over=false;
					 }
					 //select randomly any queue other than current queue
					 int lastCurrent_queue=current_queue;
					 //System.out.println("Last Current queue : "+lastCurrent_queue);
					 while(lastCurrent_queue==current_queue){
						 current_queue=randomGenerator.nextInt(fork_queue.length);
						 //System.out.println("Current queue : "+current_queue);
					 }
				 }
			 }
			 //print Join Node
			 if(type.equals("Join")){
				 //System.out.print(test.getName()+"-");
				 path=path+test.getName()+"-";
			 }
			 

		 }// while main_queue not empty
		 System.out.println("Path:"+path);
		return path;
	 }

public void readActivityGraphFromFile(String fname){
	//ActivityGraph ag= new ActivityGraph(); // read activity diagram from file into this Activitygraph
	
	//System.out.println("Name:"+fname);
	//read file
	try {
		FileInputStream fstream = new FileInputStream(fname);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
	
		String strLine;
		//	Read Activitis from file 
		strLine = br.readLine();
		String [] st1 = strLine.split("#");
		int activities=Integer.parseInt(st1[0]);
		maxPaths=Integer.parseInt(st1[3]);
		//System.out.println("Acivity count "+strLine+" "+activities);
		while (activities > 0 && (strLine = br.readLine()) != null)   {
			// Print the content on the console
			//System.out.println (strLine);
			String [] st = strLine.split("#");
			int type= Integer.parseInt(st[2]);
			Activity a= new Activity(st[0],st[1],type);
			this.addNode(st[0], a);
			activities--;
		}
		// Read edges from file
		strLine = br.readLine();
		st1 = strLine.split("#");
		int edges=Integer.parseInt(st1[0]);
		//System.out.println("Acivity count "+strLine+" "+edges);
		this.initializeEdgeSet(); // generate linked list for each node to store edgeset
		//ag.addEdge("xyz", "abc");
		while (edges > 0 && (strLine = br.readLine()) != null)   {
			// Print the content on the console
			//System.out.println (strLine);
			String [] st = strLine.split("#");
			this.addEdge(st[0], st[1]);
			edges--;
		}
		//Close the input stream
		in.close();
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public int getMaxPaths(){
	return maxPaths;
}
public int getpathcount(){
	int count=1;
	//System.out.println("Nodes size:"+nodes.size());
	for(int i=0;i<nodes.size();i++){
		//System.out.println("Edges from node"+i+" are "+edges[i].size());
		Activity a1=nodes.get(i);
		// decision node divert control flow
		if(a1.getType().equals("Decision"))
				count*=edges[i].size();
		//System.out.println(a1.getName());
		// fork node start concurrent execution (n1+n2)!/n1!*n2!
		//yet to not complete
		if(a1.getType().equals("Fork")){
			// create activity counter for each thread
			int [] thread= new int [edges[i].size()]; 
			int sum=0;
			int product=1;
			// traverse each fork edge upto join to get activity count
			for(int k=0; k< thread.length;k++){
				thread[k]=0;
				Activity fork=this.getActivity(i);
				Activity child=getUnvisitedChildNode(fork);
				while(!child.getType().equals("Join")){
					child.setVisited(true);
					thread[k]+=1; // add activity count
					child=getUnvisitedChildNode(child);
					//System.out.println(child.getName());
				}
				//System.out.println("thread activiy count "+thread[k]);
				sum+=thread[k];
				product*=Factorial.getFactorial(thread[k]);
			}
			//System.out.println("Sum: "+sum);
			//System.out.println("Product: "+product);
			int interleaving=Factorial.getFactorial(sum)/product;
			//System.out.println("Total thread Interleaving:"+interleaving);
			count*=interleaving;
		}
		
	}
	//System.out.println("Total distinct paths: "+count);
	this.clearNodes();
	return count;
}
//Set Level Numbers to activities in Activity Graph
@SuppressWarnings("unchecked")
public void setLevelNumbers()
{
	// Use BFS like algorithm to set level numbers 
	 //	BFS uses Queue data structure
	 Queue q=new LinkedList();
	 int levelNumber=0;
	 Activity temp = nodes.get(rootNode); //get root node of the tree
	 q.add(temp);// add root node in the queue
	 temp.setVisited(true); // set visited flag true
	 temp.setlevelNumber(levelNumber);
	 //levelNumber++;
	 //System.out.println(temp+" level :"+temp.getlevelNumber());
	 int forkOutNodeCount=0;
	 while(!q.isEmpty()){
		 //System.out.println("Queue not empty");
		 Activity n=(Activity)q.remove();
		 levelNumber=n.getlevelNumber()+1;
		 Activity child=null;
		 int nodeAdjacentCount=getAdjacencyCount(n);
		 while(((child=getUnvisitedChildNode(n))!=null)&& nodeAdjacentCount >0 )
		 {
			 //System.out.println("Inside child");
			 nodeAdjacentCount--; 
			 if((child.getType().equals("Fork"))){
				 forkOutNodeCount=getAdjacencyCount(child);
				 //System.out.println("Fork Adjacent node Count: "+forkOutNodeCount);
			 }
			 
			 if((child.getType().equals("Join"))){
				 forkOutNodeCount--;
				 //System.out.println("Fork Adjacent node Count: "+forkOutNodeCount);
				 if(forkOutNodeCount<=0){
					 child.setVisited(true); // set visited flag true
					 child.setlevelNumber(levelNumber);
					// System.out.println(child+" level :"+child.getlevelNumber());
					 q.add(child);
				 }
				//System.out.println(child+" level :"+child.getlevelNumber());
			 }else{
				 child.setVisited(true); // set visited flag true
				 child.setlevelNumber(levelNumber);
				 //System.out.println(child+" level :"+child.getlevelNumber());
				 q.add(child);
				 
			 }
				 
		 }

	 }
	 this.maxLevelNumber=levelNumber;
	 this.clearNodes();
}
public void setLevels()
{
	// Use BFS like algorithm to set level numbers 
	 //	BFS uses Queue data structure
	 Queue q=new LinkedList();
	 int levelNumber=0;
	 Activity temp = nodes.get(rootNode); //get root node of the tree
	 q.add(temp);// add root node in the queue
	 temp.setVisited(true); // set visited flag true
	 temp.setlevelNumber(levelNumber);
	 //levelNumber++;
	 //System.out.println(temp+" level :"+temp.getlevelNumber());
	 while(!q.isEmpty()){
		 //System.out.println("Queue not empty");
		 Activity n=(Activity)q.remove();
		 levelNumber=n.getlevelNumber()+1;
		 Activity child=null;
		 while((child=getUnvisitedChildNode(n))!=null)
		 {
			 //System.out.println("Inside child");
			 child.setVisited(true); // set visited flag true
			 child.setlevelNumber(levelNumber);
			 //System.out.println(child+" level :"+child.getlevelNumber());
			 q.add(child);
		 }

	 }
	 this.maxLevelNumber=levelNumber;
	 this.clearNodes();
}

public int getAdjacencyCount(Activity child) {
	return edges[child.getId()].size();
}

public int getLevelNumber(int nodeid) {
	Activity temp = nodes.get(nodeid); //get node at id
	return temp.getlevelNumber();
}
	private void levelPurmutation() {
		ArrayList<String> Paths = new ArrayList<String>();
		// set level number to each activity in activity graph
		this.setLevelNumbers();
		int maxLevels=this.getMaxLevelNumber(); // get maximum level number
		String activityIds = "";
		System.out.println("Maximum Levels :"+maxLevels);
		//process activities at each level
		for(int i=0; i<maxLevels;i++){
			ArrayList<Activity> currentLevelActivity=this.getElementAtLevel(i);
			System.out.println("Elements at level "+i+" :"+currentLevelActivity);
				//take permurtation of the activities at each level 
				//collect activity ids in the string and take permutation of the string rather than activities
				activityIds="";
				Iterator itr=currentLevelActivity.iterator();
				while(itr.hasNext()){
					Activity temp = (Activity) itr.next();
					//System.out.println(temp);
					int value=temp.getId();
					char charValue=(char) (value+97); 
					activityIds+=charValue;
				}
				//System.out.println(activityIds.toString());
				//add root node in the path
				if(i==0){
					Paths.add(activityIds);
				}else
				{
					//if single activity at this level then append this activity id to all paths
					if(activityIds.length()==1){
						//System.out.println(":"+activityIds);
						int itr1= Paths.size();
						for(int k =0; k<itr1;k++){
							String newPath=Paths.get(k)+activityIds;
							//System.out.println("="+newPath);
							Paths.set(k,newPath);
						
						}
						//System.out.println(": "+Paths);
					}else{
						if(activityIds.length()==1){
							for(int k=0;k<Paths.size();k++){
								String newpath=Paths.get(k)+activityIds;
								Paths.set(k,newpath);
							}
						}
						// take permutation of the activityIds at this level
						PermutationGenerator pg= new PermutationGenerator(activityIds);
						pg.permute();
						//get permuted strings
						ArrayList<String> permutations = pg.getPermutations();
						//System.out.println(permutations);
						//System.out.println(Paths);
						int psize=permutations.size();
						// append new paths at end of Paths
						int pathsize=Paths.size();
						for(int l=0; l<psize-1;l++){
							for(int k=0 ;k <pathsize;k++){
								String oldpath=Paths.get(k);
								Paths.add(oldpath);
							}
						}
						//System.out.println(Paths);
						// add new permuted activiyIds at end of Paths
						for(int l=0; l<psize;l++){
							for(int k=0 ;k <pathsize;k++){
								int index=k+l*pathsize;
								String oldpath=Paths.get(index)+permutations.get(l);
								Paths.set(index,oldpath);
							}
						}
						
					}

				}
		}
		System.out.println(Paths);
		//replace charcters with activity ids
		int pathcount= Paths.size();
		for(int i=0;i<pathcount;i++){
			//read each path 
			String oldPath=Paths.get(i);
			String newPath="";
			//replace each character in oldpaht by activity_id in newpath.
			for(int j=0; j<oldPath.length();j++){
				int chr=oldPath.charAt(j);
				Activity act=this.getActivity(chr-97);
				if(j==0)
					newPath+=act.getName();
				else
					newPath+="-"+act.getName();
			}
			Paths.set(i,newPath);
		}
		System.out.println(Paths);
	}

	ArrayList<Activity> getElementAtLevel(int level) {
		ArrayList<Activity> result = new ArrayList<Activity>();
		for(int i =0; i< nodes.size();i++){
			Activity temp = nodes.get(i); 
			if(temp.getlevelNumber()==level){
				result.add(temp);
			}
		}
		return result;
	}

	int getMaxLevelNumber() {
		return  this.maxLevelNumber;
	}

	public static void main(String args[]){
		ActivityGraph ag= new ActivityGraph();
		//ag.readActivityGraphFromFile("src\\bankSequence.txt");
		//ag.readActivityGraphFromFile("src\\DiningPhilosopher.txt");
		ag.readActivityGraphFromFile("src\\TicketReservation.txt");
		//ag.readActivityGraphFromFile("src\\DesktopStartup.txt");
		//ag.readActivityGraphFromFile("src\\MapRendering.txt");
		//int paths=ag.getpathcount();
		ag.setLevelNumbers();
		System.out.println(ag);
		//System.out.println(ag.getRootNode());
		//System.out.println("BFS Path");
		//ag.bfs();
/*		for(int i=0;i<paths;i++){
			System.out.println("\nRandom DFS Path :"+i);
			ag.randomDfs();
			}
		
		for(int i=0;i<paths;i++){
		System.out.println("\nModified DFS1 Path :"+i);
		ag.modified_dfs1();
		}
		
		for(int i=0;i<paths;i++){
			System.out.println("\nModified DFS2 Path :"+i);
			ag.modified_dfs2(i);
			}
*/
/*		System.out.println("\n DFS_BFS Path :");
		ag.dfs_bfs();*/
		
		System.out.println("\nCQS Path");
		String [] pathSet= new String[100];
		for(int i=0;i<100;i++){
			pathSet[i]=ag.cqs();
		ag.clearNodes();
		}
		ArrayList<String> UniquePaths=getDistinctPathSet(pathSet);
		System.out.println("\nCQS Unique Path set: ");
		for(int i=0; i<UniquePaths.size();i++){
			System.out.println(UniquePaths.get(i));
		}

		//System.out.println("\n Level Permutation Path");
		//ag.levelPurmutation();
		
		//System.out.println("\n DFS Path");
		//ag.rettospective_dfs();
		
		
	}
	public static ArrayList<String> getDistinctPathSet(String[] PathSet){
		ArrayList<String> al= new ArrayList<String>();
		int index=0;
		for(int i=0;i<PathSet.length;i++){
			boolean isUnique=true;
			for(int j=0; j<al.size();j++){
				//System.out.println(PathSet[i]);
				//System.out.println((String)al.get(j));
				if(PathSet[i]==null)
					continue;
				if(PathSet[i].equals((String)al.get(j)))
					isUnique=false;
				
			}
			if(isUnique){
				if(PathSet[i]==null)
					continue;
				al.add(index, PathSet[i]);
				index++;
				isUnique=true;
			}
		}
		return al;
	}

	public int getActivityCount() {
		return nodes.size();
	}

	public LinkedList<Integer> getAdjacentNodes(int sourceActivity) {
		return edges[sourceActivity];
	}

	public Activity getActivity(int destinationActivity) {
		return nodes.get(destinationActivity);
	}
	
	public int getActivityId(String activityName){
		int result=Integer.MAX_VALUE;
		for(int i=0;i<getActivityCount();i++){
			Activity tempActivity=getActivity(i);
			if(tempActivity.getName().equals(activityName)){
				result=tempActivity.getId();
				break;
			}
		}
		return result;
	}
	public ArrayList<Activity> getPredecessor(Activity act){
		ArrayList<Activity> result = new ArrayList<Activity>();
		ArrayList<Activity> temp = new ArrayList<Activity>();
		//System.out.println(act);
		// get activity level number
		int levelNum= act.getlevelNumber();
		//System.out.println(levelNum);
		//get previous level acivity list
		temp=this.getElementAtLevel(levelNum-1);
		// for each node in previous level check its successor is act
		Iterator<Activity> itr = temp.iterator();
    	while(itr.hasNext()){
    		Activity tempActivity=itr.next();
    		//System.out.println(tempActivity);
    		//get adjacent nodes of this node
    		LinkedList<Integer> adjacentNodes= this.getAdjacentNodes(tempActivity.getId());
    		if(adjacentNodes.contains(act.getId())){
    			result.add(tempActivity);
    		}
    	}
    	//System.out.println(result);
		return result;
	}
}
