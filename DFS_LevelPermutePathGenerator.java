package ScenarioGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import utils.PermutationGenerator;

public class DFS_LevelPermutePathGenerator extends PathGenerator {

	public DFS_LevelPermutePathGenerator(ActivityGraph graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPathSet(int noOfPaths) {
		//this.setStartTime();
		String path = "";	
		 // DFS uses Stack data structure
		 Stack s = new Stack();
		// set level number to each activity in activity graph
			graph.setLevelNumbers();
		// array to store result paths
		ArrayList<String> Paths = new ArrayList<String>();
		String activityIds = ""; //need in level permutation to store activityIds
		//Start DFS algorithm
		 Activity temp =graph.getActivity(graph.getRootNode()); //get root node of the tree
		 s.push(temp); // push root node on Stack
		 temp.setVisited(true); // set visited flag true
		 //System.out.println(temp);
		 //get activityId to store in result Paths
		 int value=temp.getId();
		 char charValue=(char) (value+97); 
		 activityIds+=charValue;
		 Paths.add(activityIds);
		 while(!s.isEmpty()){
			 Activity n=(Activity)s.peek(); // get top element on stack
			 //check if top element of stack is Fork node
			 if(n.getType().equals("Fork")){
				 //Start level Permute search up to next Join node
				 //get level number of the fork activity
				 int nodeid=n.getId();
				 int currentLevel=graph.getLevelNumber(nodeid);
				 ArrayList<Activity> currentLevelActivity=graph.getElementAtLevel(currentLevel);
				 n.setVisited(true); // set visited flag true
				 //add fork to paths
				 	//value=n.getId();
					//charValue=(char) (value+97); 
					//activityIds+=charValue;
					//Paths.add(activityIds);
				 //System.out.println(n);
				 while(!joinInLevel(currentLevelActivity)){
					 //increase level number and get activities at this level
					 currentLevel++;
					 currentLevelActivity=graph.getElementAtLevel(currentLevel);
						//take permurtation of the activities at each level 
						//collect activity ids in the string and take permutation of the string rather than activities
						activityIds="";
						Iterator itr=currentLevelActivity.iterator();
						while(itr.hasNext()){
							Activity temp1 = (Activity) itr.next();
							//System.out.println(temp);
							value=temp1.getId();
							charValue=(char) (value+97); 
							activityIds+=charValue;
							temp1.setVisited(true);
						}
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
						}else{//append the permutation of acitivities
							/*if(activityIds.length()==1){
								for(int k=0;k<Paths.size();k++){
									String newpath=Paths.get(k)+activityIds;
									Paths.set(k,newpath);
								}
							}*/
							// take permutation of the activityIds at this level
							PermutationGenerator pg= new PermutationGenerator(activityIds);
							pg.permute();
							//get permuted strings
							ArrayList<String> permutations = pg.getPermutations();
							//System.out.println(permutations);
							//System.out.println(Paths);
							int psize=permutations.size();
							// append new paths at end of Paths
							//check if Paths size is less than permutation size
							// if less then add space with old path in Paths
							int pathsize=Paths.size();
							//if(pathsize<psize){
								for(int l=0,k=0; l<(psize*pathsize)-pathsize;l++,k++){
									String oldpath=Paths.get(k%pathsize);
									Paths.add(oldpath);
								}
							//}
							//System.out.println(Paths);
							// add new permuted activiyIds at end of Paths
							//pathsize=Paths.size();
							for(int l=0; l<psize;l++){
								for(int k=0 ;k <pathsize;k++){
									int index=k+l*pathsize;
									String oldpath=Paths.get(index)+permutations.get(l);
									Paths.set(index,oldpath);
								}
							}
							
						}
				 }// while(!joinInLevel(currentLevelActivity))
				 //push join node on stack
				 temp=getJoinInLevel(currentLevelActivity);
				 s.push(temp);
				 n=(Activity)s.peek(); // get top element on stack
				 // start the DFS search				 
			 }//if(n.getType().equals("Fork"))

			 Activity child=graph.getRandomUnvisitedChildNode(n);
			 if(child!=null)
			 {
				 child.visited=true;
				 //System.out.println(child);
				 activityIds="";//clear pervious activityIds
				 value=child.getId();
				 charValue=(char) (value+97); 
				 activityIds+=charValue;
				 //append this activityId to all Paths
				 int pathsize=Paths.size(); // get no of paths available
					for(int k=0 ;k <pathsize;k++){
						String oldpath=Paths.get(k)+activityIds;
						Paths.set(k,oldpath);
					}
				 //Paths.add(activityIds);
				 s.push(child);
			 }
			 else
			 {
				 s.pop();
			 }
			 if(n.getType().equals("End") || n.getType().equals("Flow End"))
				 break;	
		 }
		 graph.clearNodes();
			//replace charcters with activity ids
			int pathcount= Paths.size();
			for(int i=0;i<pathcount;i++){
				//read each path 
				String oldPath=Paths.get(i);
				String newPath="";
				//replace each character in oldpaht by activity_id in newpath.
				for(int j=0; j<oldPath.length();j++){
					int chr=oldPath.charAt(j);
					Activity act=graph.getActivity(chr-97);
					if(j==0)
						newPath+=act.getName();
					else
						newPath+="-"+act.getName();
				}
				Paths.set(i,newPath);
			}
			String[] result= new String[Paths.size()];
			result=Paths.toArray(result);
			//this.setEndTime();
			return result;
	}

	private Activity getJoinInLevel(ArrayList<Activity> currentLevelActivity) {
		Activity result=null;
		Iterator itr=currentLevelActivity.iterator();
		while(itr.hasNext()){
			Activity temp1 = (Activity) itr.next();
			if(temp1.getType().equals("Join"))
				result=temp1;
		}
		return result;
	}
	
	private boolean joinInLevel(ArrayList<Activity> currentLevelActivity) {
		boolean result=false;
		Iterator itr=currentLevelActivity.iterator();
		while(itr.hasNext()){
			Activity temp1 = (Activity) itr.next();
			if(temp1.getType().equals("Join"))
				result=true;
		}
		return result;
	}

	
	public ArrayList<String> getPaths(int noOfPaths, int noOfRuns) {
		setStartTime();
		String [] pathSet= new String[noOfPaths];
		ArrayList<String> UniquePaths = new ArrayList<String>();
		for(int j=0; j< noOfRuns; j++){
			String[] Paths=this.getPathSet(noOfPaths);
			ArrayList<String> newPathSet=this.getDistinctPathSet(Paths);
			ArrayList<String> newPaths=this.getDistinctPaths(newPathSet, UniquePaths);
			int count =newPaths.size();
			UniquePaths.addAll(newPaths);
			System.out.println("new Paths found in "+j+" run :"+count);
			for(int i=0; i<newPaths.size();i++){
				System.out.println(newPaths.get(i));
			}
		}
		setEndTime();
		return UniquePaths;
	}

	public static void main(String[] args){
		ActivityGraph graph= new ActivityGraph();
		DFS_LevelPermutePathGenerator dfs_lp = new DFS_LevelPermutePathGenerator(graph);
		//dfs_lp.loadActivityGraph("src\\bank.txt");
		//dfs_lp.loadActivityGraph("src\\activity1.txt");
		//dfs_lp.loadActivityGraph("src\\activity2.txt");
		//dfs_lp.loadActivityGraph("src\\activity3.txt");
		//dfs_lp.loadActivityGraph("src\\activity4.txt");
		//dfs_lp.loadActivityGraph("src\\activity5.txt");
		//dfs_lp.loadActivityGraph("src\\activity6.txt");
		//dfs_lp.loadActivityGraph("src\\activity7.txt");
		//dfs_lp.loadActivityGraph("src\\activity8.txt");
		//dfs_lp.loadActivityGraph("src\\activity9.txt");
		//dfs_lp.loadActivityGraph("src\\AirPortCheckIn.txt");
		//dfs_lp.loadActivityGraph("src\\ATM.txt");
		//dfs_lp.loadActivityGraph("src\\OnlinePurchase.txt");
		//dfs_lp.loadActivityGraph("src\\OrderProcessing.txt");
		//dfs_lp.loadActivityGraph("src\\UniversityEnrollment.txt");
		//dfs_lp.loadActivityGraph("src\\SDF.txt");
		//dfs_lp.loadActivityGraph("src\\SFF.txt");
		//dfs_lp.loadActivityGraph("src\\NDFD.txt");
		//dfs_lp.loadActivityGraph("src\\NDFF.txt");
		//dfs_lp.loadActivityGraph("src\\GraphicsUtility.txt");
		//dfs_lp.loadActivityGraph("src\\AirPortCheckIn_New.txt");
		//dfs_lp.loadActivityGraph("src\\ATM_New.txt");
		//dfs_lp.loadActivityGraph("src\\ATM_New1.txt");
		//dfs_lp.loadActivityGraph("src\\BankTransaction_New.txt");
		//dfs_lp.loadActivityGraph("src\\DiningPhilosopher_New.txt");
		dfs_lp.loadActivityGraph("src\\LockProblem.txt");
		//int maxPaths=graph.getpathcount();
		int maxPaths=25;
		System.out.println("Total distinct paths: "+maxPaths);
		//System.out.println(graph);
		//String path=rdfs_bfs.getPath();
		//System.out.println("\nRandom DFS_BFS Path: "+path);
	
	/*String []Paths=rdfs_bfs.getPathSet(maxPaths);
	System.out.println("\nRandom DFS_BFS Path set: ");
	for(int i=0; i<maxPaths;i++){
		System.out.println(Paths[i]);
	}
	ArrayList<String> UniquePaths=rdfs_bfs.getDistinctPathSet(Paths);
	System.out.println("\nRandom DFS_BFS Unique Path set: ");
	for(int i=0; i<UniquePaths.size();i++){
		System.out.println(UniquePaths.get(i));
	}*/
		ArrayList<String> UniquePaths=dfs_lp.getPaths(maxPaths,5);
		System.out.println("\nDFS_LevelPermute Unique Path set: ");
		System.out.println("Total Unique Paths : "+UniquePaths.size()+"\n");
		for(int i=0; i<UniquePaths.size();i++){
			System.out.println((i+1)+")"+UniquePaths.get(i)+"\n");
		}

	}

}
