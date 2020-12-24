package ScenarioGenerator;

import java.util.ArrayList;
import java.util.Iterator;

import utils.PermutationGenerator;

public class LevelPermutationPathGenerator extends PathGenerator {

	public LevelPermutationPathGenerator(ActivityGraph graph) {
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
		this.setStartTime();
		ArrayList<String> Paths = new ArrayList<String>();
		// set level number to each activity in activity graph
		graph.setLevelNumbers();
		int maxLevels=graph.getMaxLevelNumber(); // get maximum level number
		String activityIds = "";
		System.out.println("Maximum Levels :"+maxLevels);
		//process activities at each level
		for(int i=0; i<maxLevels;i++){
			ArrayList<Activity> currentLevelActivity=graph.getElementAtLevel(i);
			//System.out.println("Elements at level "+i+" :"+currentLevelActivity);
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
		//System.out.println(Paths);
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
		//System.out.println(Paths);
		String[] result= new String[Paths.size()];
		result=Paths.toArray(result);
		this.setEndTime();
		return result;
	}
	public static void main(String[] args){
		ActivityGraph graph= new ActivityGraph();
		LevelPermutationPathGenerator lp = new LevelPermutationPathGenerator(graph);
		//lp.loadActivityGraph("src\\UniversityEnrollment.txt");
		//lp.loadActivityGraph("src\\activity1.txt");
		//lp.loadActivityGraph("src\\activity2.txt");
		//lp.loadActivityGraph("src\\activity3.txt");
		//lp.loadActivityGraph("src\\activity4.txt");
		//lp.loadActivityGraph("src\\activity5.txt");
		//lp.loadActivityGraph("src\\activity6.txt");
		//lp.loadActivityGraph("src\\activity7.txt");
		//lp.loadActivityGraph("src\\activity9.txt");
		//lp.loadActivityGraph("src\\AirPortCheckIn.txt");
		//lp.loadActivityGraph("src\\ATM.txt");
		//lp.loadActivityGraph("src\\OnlinePurchase.txt");
		//lp.loadActivityGraph("src\\OrderProcessing.txt");
		lp.loadActivityGraph("src\\UniversityEnrollment.txt");
		//lp.loadActivityGraph("src\\SDF.txt");
		int maxPaths=graph.getpathcount();
		//int maxPaths=10;
		System.out.println("Total distinct paths: "+maxPaths);
		//System.out.println(graph);
		String[] Paths=lp.getPathSet(10);
		for(int i=0; i<Paths.length;i++){
			System.out.println(Paths[i]);
		}
		ArrayList<String> UniquePaths=lp.getDistinctPathSet(Paths);
		System.out.println("\nLevel Permutation Unique Path set: ");
		System.out.println("Total Unique Paths : "+UniquePaths.size()+"\n");
		//for(int i=0; i<UniquePaths.size();i++){
		//	System.out.println(UniquePaths.get(i));
		//}
	}
}
