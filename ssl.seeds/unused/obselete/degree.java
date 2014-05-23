package obselete;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;




public class degree {
	private static int V;
	private int E;
	private static ArrayList<Integer>[] adj;
	private static ArrayList<Double>[] vertdetails;

	public degree(int V, String s) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (ArrayList<Integer>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<Integer>();
        }

        String[] edgelist= s.split("\n");      
        for (int i=0;i<edgelist.length;i++){               
		String[] t=edgelist[i].split("\t");
	int v= Integer.parseInt(t[0])-1;
		int size=t.length;
                
		for(int j=1;j<size;j++) {
                    addEdge(v,Integer.parseInt(t[j])-1);
                                      }		
            }    

       
    }

	public  void initclust(int V, String s)
	{
		
		if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        
        vertdetails = (ArrayList<Double>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            vertdetails[v] = new ArrayList<Double>();
        }

        String[] vertlist= s.split("\n");      
        for (int i=0;i<vertlist.length;i++){               
		String[] t1=vertlist[i].split("\t");
		double vertex= Double.parseDouble(t1[0])-1;
		double clust=Double.parseDouble(t1[1]);
    	 	double stddev=Double.parseDouble(t1[3]);
    	double mean=Double.parseDouble(t1[2]);
    	double centredist=Double.parseDouble(t1[4]);
    	vertdetails[i].add(vertex);
		vertdetails[i].add(clust);
		vertdetails[i].add(mean);
		vertdetails[i].add(stddev);
	vertdetails[i].add(centredist);
        }
	}
		
		
		
	

	 public void addEdge(int v, int w) {
	        if (v < 0 || v >= V) throw new IndexOutOfBoundsException();
	        if (w < 0 || w >= V) throw new IndexOutOfBoundsException();
	        E++;
	        adj[v].add(w);
	        adj[w].add(v);
	    }
	 public static Iterable<Integer> adj(int v) {
	        if (v < 0 || v >= V) throw new IndexOutOfBoundsException();
	        return adj[v];
	    }
	 public static Iterable<Double> vertdetails(int v) {
	        if (v < 0 || v >= V) throw new IndexOutOfBoundsException();
	        return vertdetails[v];
	    }

	 public  int degreeofvertex(int v) {
	        int degree = 0;
	        for (int w : adj(v)) degree++;
	        return degree;
	    }
	 public int intradegreeofvertex(int v){
		int intra_deg=0;
		
		int clustnum=vertdetails[v].get(1).intValue();//???????????????
		System.out.println("clust "+clustnum);
	    	for(int w:adj(v))
	        {
	    		
	    		if(vertdetails[w].get(1).intValue()==clustnum) {
	    			
	    			intra_deg++;
	    		}
	    	}
	    	return intra_deg;
	 }
	 public static double phi(double x, double mu, double sigma) {
	        return phi((x - mu) / sigma) / sigma;
	    }
	 public static double phi(double x) {
	        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
	    }
	 public static double sigmoid(double x) {
		    return (1/( 1 + Math.pow(Math.E,(-1*x))));
		  }




public static void main(String args[]) throws IOException {
        
	 BufferedReader br= new BufferedReader(new FileReader(args[0]));
         int numofvertices=Integer.parseInt(br.readLine());
         br.readLine();
         

         String degstr=new String("");
         String str, str1;
         String str11=new String("");
         double  pd1, pd2, pd3;

         boolean flag=true;
         while(flag==true){
             str=br.readLine();
             if (str.length()==0) flag=false;
             else degstr=degstr+str+"\n";
             }         
        // System.out.println(degstr);
         degree d= new degree(numofvertices, degstr);
         
         boolean flag1=true;
         while((str=br.readLine())!=null){
            // str1=br.readLine();
             if (str.length()==0) flag1=false;
             else str11=str11+str+"\n";
             } 
        // System.out.println(str11);
         d.initclust(numofvertices,str11);
	
	
	for(int j=0;j<V;j++)
	{
		System.out.println("adjacency"+ adj[j].toString());
		System.out.println("cluster"+ vertdetails[j].toString());
	}
	
	
	
	for(int i=1;i<=V;i++){
		if (vertdetails.length < 2) continue;
		System.out.println(vertdetails[i-1].get(2));
		System.out.println(vertdetails[i-1].get(3));
		System.out.println(vertdetails[i-1].get(4));
		pd1=phi(vertdetails[i-1].get(4),vertdetails[i-1].get(2),vertdetails[i-1].get(3));
	
	
	
		pd2=sigmoid(d.degreeofvertex(i-1)-d.intradegreeofvertex(i-1));
	
	
			pd3=((sigmoid(d.degreeofvertex(i-1)-d.intradegreeofvertex(i-1)))*(1-phi(vertdetails[i-1].get(4),vertdetails[i-1].get(2),vertdetails[i-1].get(3))));
	System.out.println("Boundaryseed is "+ pd1 + "Noise is "+ pd2 + "central seed is" + pd3 + "max is "+ Math.max(pd1,Math.max(pd2, pd3)));
	}


}
}

