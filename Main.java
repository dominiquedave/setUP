/* Written by: Dave Dominique */
import java.io.*;
import java.util.*;

class pairs{
	private long parent;
	private long numkids;
	private long Height;

	public pairs(long n) {
		parent = n;
		numkids = 1; 
		Height = 1;
	}
	
	public long getParent() {
		return parent;
	}
	
	public void setParent(long i) {
		 parent = i;
	}
	
	public long getnumkids() {
		return numkids;
	}
	
	public void setnumkids(long i) {
		numkids = i;
	}
	
	public void incnumkids(long i) {
			numkids += i;
	}
	public void incHeight() {
		Height++;
	}
	
	public void decHeight() {
		Height--;
	}
	
	public long getHeight() {
		return Height;
	}
}


class disjointset{
	
	public pairs[] arr;
	public int tracker[];
	
	public disjointset(long n) {
		arr = new pairs[(int) n];
		tracker =new int[(int) n];
		
		for(int i = 0; i < n; i++) {
			arr[i] = new pairs(i);
			tracker[i] = 1;
		}
	}

	//USE SAME CODE FROM LAB 2
	// Returns the root node of the tree storing id.
	public long find(long id) {
			
			// I am the root of the tree)
			if (id == arr[(int) id].getParent()) 
					return id;
			// Find my parent's root.
			long res = find(arr[(int) id].getParent());

			// if res is not m existing parent, make it parent
		    if (res != arr[(int) id].getParent())
		    {
		      // Attach me directly to the root of my tree.
		      arr[(int) id].setParent(res);

		      arr[(int) res].decHeight(); //decrease height as id is leveled up
		    }
				return res;
		}
	
	// Performs union on two nodes
	public boolean union(long id1, long id2) {

				// Find the pairs of both nodes.
				long root1 = find(id1);
				long root2 = find(id2);

				// No union needed.
				if (root1 == root2)
					return false;

				// Attach tree 2 to tree 1 (tree one is the root)
				if (arr[(int) root1].getHeight() > arr[(int) root2].getHeight()) {
					arr[(int) root2].setParent(root1);
					
					tracker[(int) root2] = 0;
					arr[(int) root1].incnumkids(arr[(int) root2].getnumkids());
					
				}

				// Attach tree 1 to tree 2
				else if (arr[(int) root2].getHeight() > arr[(int) root1].getHeight() ) {
					arr[(int) root1].setParent(root2);
					
					tracker[(int) root1] = 0;
					arr[(int) root2].incnumkids(arr[(int) root1]. getnumkids());
				}

				// Same height case - just attach tree 2 to tree 1, adjust height.
				else {
					arr[(int) root2].setParent(root1);
					arr[(int) root1].incHeight();
					
					tracker[(int) root2] = 0;
					arr[(int) root1].incnumkids(arr[(int) root2]. getnumkids());
				}

				// We successfully did a union.
				return true;
			}

	// Just represents this object as a list of each node's parent.
	public String toString() {

				String ans = "";
				for (int i=0; i<arr.length; i++)
		    {
		      if (i == arr[i].getParent()) //print the height if the node is root
		        ans = ans + "(" + i + ", " + arr[i].getParent() +") ";
		      else
		        ans = ans + "(" + i + ", " + arr[i].getParent() + ") ";
		    }
				return ans;
		}
	}

//used to keep track of connected nodes 
class connects{
	public long x, y;
	
	public connects (long x, long y) {
		this.x =x;
		this.y = y;
	}
	
	public String toString() {
		return x+ " " + y;
	}
}

class Main {
	
	//helper function used to calculate connections
	public static long points(disjointset set) {
			long ret = 0;
			for(int i = 0; i < set.tracker.length; i++) {
				if(set.tracker[i] == 0) {
					continue;
				}
				ret+=(set.arr[i].getnumkids() * set.arr[i].getnumkids());
			}
			return ret;
	}
	
	//linear search helper function 
	public static boolean look(long id, long destroys[]) {
		
		for(int i=0; i < destroys.length; i++) {
			if(id == destroys[i])
				return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{ 
	    //Gather input
	    File file = new File("in.txt"); 
		Scanner myScan = new Scanner(file);
		
		long n, m, d;
		
		n = myScan.nextLong();
		m = myScan.nextLong();
		d = myScan.nextLong();
		
		disjointset set = new disjointset(n);
		connects array[] = new connects[(int) m];
		
		for(int i = 0; i < m; i++) {
			long a, b;
			a = myScan.nextLong();
			b = myScan.nextLong();
			
			array[i] = new connects(a-1,b-1);	
		}

		long destroys[] = new long [(int) d];
		
		
		for(int j =0; j < d; j++) {
			long k = myScan.nextLong();
			destroys[j] = k-1;
		}
		//end of building our data structues
		
		
	    //Use collected data to generate desired output
		long len = destroys.length;
		long [] print = new long [(int) (len +1)];
		long k =0;
	    
	    //partially assemble set without soon to be diconnections
		for(long i = 0; i < array.length-1; i++) {
		
			if ( look(i, destroys) ) {
			
					continue;
				}
			
			set.union(array[(int) i].x, array[(int) i].y);
		}
		
		//store current connections
		print[(int) k++] = points(set);
		
	    //finish building the skipped connections
		for(int i = destroys.length-1; i >= 0 ; i--) {
			long index = destroys[i];
			
			set.union(array[(int) index].x, array[(int) index].y);
			
			print[(int) k++] = points(set);
		}
				
		//print output holding numbers 
		for(int i = print.length - 1; i >= 0; i--) {
			System.out.println(print[i]);
		}
	
	}

}
