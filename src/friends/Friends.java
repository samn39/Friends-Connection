package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

// javac -d bin src/friends/*.java src/structures/*.java to compile
// java -cp bin friends.FriendsApp graph_filename to execute
public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		//javac -d bin src/friends/*.java src/structures/*.java
		//java -cp bin friends.FriendsApp sampleGraph.txt
		/** COMPLETE THIS METHOD **/

		//if the person doesn't contain in the graph
		ArrayList<String> shortest = new ArrayList<String>();
		if(!g.map.containsKey(p1)||!g.map.containsKey(p2)){
			return shortest;
		}
		boolean[] marked = new boolean[g.members.length];
		int[] edgeTo = new int[g.members.length];
		int[] disTo = new int[g.members.length];
		Queue<Integer> q = new Queue<Integer>();

		//traverseing the graph using BFS
		q.enqueue(g.map.get(p1));
		marked[g.map.get(p1)] = true;
		disTo[g.map.get(p1)] = 0;

		while(!q.isEmpty()){
			int v = q.dequeue();
			Friend nextFriend = g.members[v].first;
				while(nextFriend!=null){
					if(!marked[nextFriend.fnum]){
						q.enqueue(nextFriend.fnum);
						marked[nextFriend.fnum]=true;
						edgeTo[nextFriend.fnum]= v;
						disTo[nextFriend.fnum]= disTo[v];
					}
					nextFriend = nextFriend.next;
				}		
			
			
		}

		//if(marked is not true, meaning there's no connection)
		if(marked[g.map.get(p2)]==false){
			return shortest;
		}

		//after traverse the whole array, inserting the path from p1 to p2 (starting backward because it's a stack)
		Stack<Integer> path = new Stack<Integer>();
		int start = g.map.get(p2);
		while(start!=g.map.get(p1)){
			path.push(start);
			start = edgeTo[start];
		}
		path.push(g.map.get(p1));
		
		//adding the path to the arraylist by popping
		while(!path.isEmpty()){
			shortest.add(g.members[path.pop()].name);
		}
	
		return shortest;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<ArrayList<String>> friendGroup = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		for(int i = 0; i < visited.length; i++){
		   visited[i] = true;
		}
		
		for(Person member : g.members){
		   if(visited[g.map.get(member.name)] && member.school != null && member.school.equals(school)){
				Queue<Integer> q = new Queue<>();
				ArrayList<String> clique = new ArrayList<>();
				int startIndex = g.map.get(member.name);
				visited[startIndex] = false;
		
				q.enqueue(startIndex);
				clique.add(member.name);
		
				while(!q.isEmpty()){
			 		int v = q.dequeue(); 
			 		Person p = g.members[v];
		
					for(Friend ptr=p.first; ptr != null; ptr = ptr.next){
			 	 		int fnum = ptr.fnum;
			  			Person fr = g.members[fnum];
			 			if(visited[fnum] && fr.school != null && fr.school.equals(school)){
			   				visited[fnum] = false;
			   				q.enqueue(fnum);
			  				clique.add(g.members[fnum].name);
			  			}
			 		}
				}
				friendGroup.add(clique);
		  	}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return friendGroup;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */


	 
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<String> connector = new ArrayList<>();
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length]; 
		if(g.members.length<=2){
			return connector;
		}

		for (Person member : g.members){
   			if (!visited[g.map.get(member.name)]){	
    			dfsnum = new int[g.members.length];
    			dfsConnector(g.map.get(member.name), g.map.get(member.name), g, visited, dfsnum, back, connector);
   			}
		}
		for (int i = 0; i < connector.size(); i++){
   			Friend ptr = g.members[g.map.get(connector.get(i))].first;
   			int count = 0;
   			while (ptr != null){
    			ptr = ptr.next;
    			count++;
   			}
 			if (count == 0 || count == 1){
    			connector.remove(i);
   			}
		}			
		for (Person member : g.members){
   			if ((member.first.next == null && !connector.contains(g.members[member.first.fnum].name))){
    			connector.add(g.members[member.first.fnum].name);
   			}
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return connector;
		
	}
	private static int count(int[] arr){
		int count = 0;
		for (int i = 0; i < arr.length; i++){
   			if (arr[i] != 0){
    			count++;
   			}
		}
		return count;
	}
	private static void dfsConnector(int v, int start, Graph g, boolean[] visited, int[] dfsnum, int[] back, ArrayList<String> answer){
		Person p = g.members[v];
		visited[g.map.get(p.name)] = true;
		int count = count(dfsnum) + 1;

		if (dfsnum[v] == 0 && back[v] == 0){
   			dfsnum[v] = count;
   			back[v] = dfsnum[v];
		}

		for (Friend ptr = p.first; ptr != null; ptr = ptr.next){
  			if (!visited[ptr.fnum]){
				dfsConnector(ptr.fnum, start, g, visited, dfsnum, back, answer);

				if (dfsnum[v] > back[ptr.fnum]){
    				back[v] = Math.min(back[v], back[ptr.fnum]);
    		} 
			else{
     
     			if (Math.abs(dfsnum[v] - back[ptr.fnum]) < 1 && Math.abs(dfsnum[v] - dfsnum[ptr.fnum]) <= 1 && back[ptr.fnum] == 1 && v == start){
      				continue;
     			}
				if (dfsnum[v] <= back[ptr.fnum] && (v != start || back[ptr.fnum] == 1)){ 
      				if (!answer.contains(g.members[v].name)){
       					answer.add(g.members[v].name);
      				}
    			}
    			}
   			} 
			else{
    			back[v] = Math.min(back[v], dfsnum[ptr.fnum]);
   			}
		}
		return;
	}
}









