package ScenarioGenerator;

import java.util.ArrayList;

public abstract class PathGenerator {
	ActivityGraph graph = new ActivityGraph();
	long startTime, endTime;
	double coveragePercentage;
	
	public void loadActivityGraph(String fname){
		System.out.println("Path generation algorithm: "+this.getClass().getName());
		System.out.println("Reading activity diagram from file: "+fname);
		graph.readActivityGraphFromFile(fname);
		graph.setLevelNumbers();
	}
	protected PathGenerator(ActivityGraph Graph){
		graph=Graph;
		coveragePercentage=80.00;
	}
	public void setStartTime(){
		System.out.println("Start Time: "+System.currentTimeMillis());
		startTime=System.currentTimeMillis();
	}
	public void setEndTime(){
		endTime=System.currentTimeMillis();
		System.out.println("End Time: "+endTime);
		System.out.println("Total Time: "+(endTime-startTime)/1000.0+" seconds.");
	}
	public void setCoveragePercentage(double coverage){
		coveragePercentage=coverage;
	}
	public double getCoveragePercentage(){
		return coveragePercentage;
	}
	PathGenerator(){}
	public abstract String getPath();
	public abstract String[] getPathSet(int noOfPaths);
	
	public ArrayList<String> getDistinctPathSet(String[] PathSet){
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
	public ArrayList<String> getDistinctPaths(ArrayList<String> newPathSet, ArrayList<String> pathSet){
		ArrayList<String> resultPathSet = new ArrayList<String>();
		int index=0;
		for(int i=0;i<newPathSet.size();i++){
			boolean isUnique=true;
			for(int j=0; j<pathSet.size();j++){
				//System.out.println(PathSet[i]);
				//System.out.println((String)al.get(j));
				if(newPathSet.get(i).equals((String)pathSet.get(j)))
					isUnique=false;
			}
			if(isUnique){
				resultPathSet.add(index, newPathSet.get(i));
				index++;
				isUnique=true;
			}
		}
		return resultPathSet;
	}

}
