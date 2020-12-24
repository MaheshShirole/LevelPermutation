package ScenarioGenerator;

import java.util.ArrayList;
import java.util.Stack;

public class RandomStandardDFS extends PathGenerator {

	public RandomStandardDFS (ActivityGraph graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getPath() {
		String path = "";	
		 //DFS uses Stack data structure
		 Stack s=new Stack();
		 Activity temp =graph.getActivity(graph.getRootNode()); //get root node of the tree
		 s.push(temp);// add root node in the Stack
		 temp.setVisited(true); // set visited flag true
		 //System.out.println(temp);
		 path+=temp.getName();
		 while(!s.isEmpty())
		 {
			 Activity n=(Activity)s.peek();
			 Activity child=null;
			 if(n.getType().equals("Decision")&&n.getVisitCount()>1){
				 child=graph.getUnvisitedChildNode(n);
			 	//System.out.println(n.getVisitCount());
			 }
			 else{
			  child=graph.getRandomUnvisitedChildNode(n);
			 }
			 
			 //System.out.println(child);
			 if(child!=null)
			 {
				 if(!child.getType().equals("Decision")){
				 child.visited=true;
				 }else
				 {
					 child.setVisitCount();	 
					 //System.out.println(child+" "+child.getVisitCount());
					 if(child.getVisitCount()>1 ){
						 child.visited=true; 
						 //child.clearVisitCount();
						 //System.out.println(child+" "+child.getVisitCount());
					 }
				 }
				 //System.out.println(child);
				 path+="-"+child.getName();
				 s.push(child);
			 }
			 else
			 {
				 s.pop();
				 //System.out.println("last poped item "+s.pop());
			 }
		 }
		 graph.clearNodes();

		return path;
	}

	@Override
	public String[] getPathSet(int noOfPaths) {
		setStartTime();
		String [] pathSet= new String[noOfPaths];
		for(int i=0;i<noOfPaths;i++){
			String path =this.getPath();
			if(path!=null)
			pathSet[i]=path;
		}
		setEndTime();
		return pathSet;
	}
	public ArrayList<String> getPaths(int noOfPaths, int noOfRuns) {
		setStartTime();
		String [] pathSet= new String[noOfPaths];
		ArrayList<String> UniquePaths = new ArrayList<String>();
		for(int j=0; j< noOfRuns; j++){
			for(int i=0;i<noOfPaths;i++){
				pathSet[i]=this.getPath();
			}
			ArrayList<String> newPathSet=this.getDistinctPathSet(pathSet);
			ArrayList<String> newPaths=this.getDistinctPaths(newPathSet, UniquePaths);
			int count =newPaths.size();
			UniquePaths.addAll(newPaths);
			//System.out.println("new Paths found in "+j+" run :"+count);
			//for(int i=0; i<newPaths.size();i++){
				//System.out.println(newPaths.get(i));
			//}
		}
		setEndTime();
		return UniquePaths;
	}

	public static void main(String[] args){
		ActivityGraph graph= new ActivityGraph();
		RandomDFSPathGenerator rdfs = new RandomDFSPathGenerator(graph);
		//rdfs.loadActivityGraph("src\\bank.txt");
		//rdfs.loadActivityGraph("src\\activity1.txt");
		//rdfs.loadActivityGraph("src\\activity2.txt");
		//rdfs.loadActivityGraph("src\\activity3.txt");
		//rdfs.loadActivityGraph("src\\activity4.txt");
		//rdfs.loadActivityGraph("src\\activity5.txt");
		//rdfs.loadActivityGraph("src\\activity6.txt");
		//rdfs.loadActivityGraph("src\\activity7.txt");
		//rdfs.loadActivityGraph("src\\AirPortCheckIn.txt");
		//rdfs.loadActivityGraph("src\\ATM.txt");
		//rdfs.loadActivityGraph("src\\OnlinePurchase.txt");
		//rdfs.loadActivityGraph("src\\OrderProcessing.txt");
		//rdfs.loadActivityGraph("src\\UniversityEnrollment.txt");
		//rdfs.loadActivityGraph("src\\activity8.txt");
		//rdfs.loadActivityGraph("src\\activity9.txt");
		//rdfs.loadActivityGraph("src\\SDD.txt");
		//rdfs.loadActivityGraph("src\\SDI.txt");
		//rdfs.loadActivityGraph("src\\NDD.txt");
		//rdfs.loadActivityGraph("src\\NDI.txt");
		rdfs.loadActivityGraph("src\\SDF.txt");
		int maxPaths=graph.getpathcount();
		System.out.println("Total distinct paths: "+maxPaths);
		//int maxPaths=10;
		//System.out.println(graph);
		//String path=rdfs.getPath();
		//System.out.println("\nDFS Path: "+path);
/*		String []Paths=rdfs.getPathSet(maxPaths);
		System.out.println("\nRandom DFS Path set: ");
		for(int i=0; i<maxPaths;i++){
			System.out.println(Paths[i]);
		}
		ArrayList<String> UniquePaths=rdfs.getDistinctPathSet(Paths);
		System.out.println("\nRandom DFS Unique Path set: ");
		for(int i=0; i<UniquePaths.size();i++){
			System.out.println(UniquePaths.get(i));
		}*/
		ArrayList<String> UniquePaths=rdfs.getPaths(maxPaths,5);
		System.out.println("\nRandom DFS Unique Path set: ");
		System.out.println("Total Unique Paths : "+UniquePaths.size()+"\n");
		for(int i=0; i<UniquePaths.size();i++){
			System.out.println(UniquePaths.get(i));
		}

	}
	

}
