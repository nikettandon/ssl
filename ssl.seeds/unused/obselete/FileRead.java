package obselete;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

public class FileRead {
	private static final String NULL = null;
	private static HashMap<String, ArrayList<String>> adjwords;
	private static HashMap<String, ArrayList<String>> nodedetails;
	
	public FileRead(String s) {
       
        adjwords = new HashMap<String, ArrayList<String>>();

        String[] similarlist= s.split("\n");      

        for (int i=0;i<similarlist.length;i++){               
		String[] t=similarlist[i].split("\t");
		String word1=t[0]; 
		String word2=t[1];
		String score=t[2];
		
		ArrayList<String> v = new ArrayList<String>();
		if(adjwords.containsKey(word1)) {
			v = adjwords.get(word1);
		}
		v.add(word2+"_"+score);
		
		adjwords.put(word1, v);
		ArrayList<String> v1 = new ArrayList<String>();
		if(adjwords.containsKey(word2)) {
			v1 = adjwords.get(word2);
		}
		v1.add(word1+"_"+score);
		
		adjwords.put(word2, v1);
        }
		
	
            }
	public  int degreeofvertex(String v) {
        int degree = 0;
        java.util.Iterator<String> it2 = adjwords.keySet().iterator();
       // ArrayList<String> neig= new ArrayList<String>();
        while(it2.hasNext())
        {
        	if(it2.next()==v)
        	{
        		degree=adjwords.get(v).size();
        	}
        }
        return degree;
    }
 public int intradegreeofvertex(String v, ArrayList<String> al){
	int intra_deg=0;
	 String clustnum=""; 
	 String clustnumneg="";
	 java.util.Iterator<String> it2 = nodedetails.keySet().iterator();
	// java.util.Iterator<String> it3 = adjwords.keySet().iterator();
	// ArrayList<String> ar= new ArrayList<String>();
	 while(it2.hasNext())
	 {
		 if(it2.next()==v){
			 ArrayList<String> arr = nodedetails.get(v); 
			  clustnum= arr.get(0);
			  System.out.println("vertex cluster number"+clustnum);
			  
		 	}
		for(int i=0;i<al.size();i++)
		{
			if(it2.next()==al.get(i))
			{
				ArrayList<String> arr1 = nodedetails.get(al.get(i));
				clustnumneg= arr1.get(0);
				System.out.println("neigbors cluster number"+clustnumneg);
				if(clustnum.equals(clustnumneg)) intra_deg++;
			}
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
		nodedetails = new HashMap<String, ArrayList<String>>();
        //int testvert= Integer.parseInt(args[1]);       
		 BufferedReader br= new BufferedReader(new FileReader(args[0]));
		 String mat[][]= new String[15][15];
		
		 String inpstr=new String("");
		 
		 String str;
		 boolean flag1=true;
         while((str=br.readLine())!=null){
            
             if (str.length()==0) flag1=false;
             else inpstr=inpstr+str+"\n";
             } 
         FileRead d= new FileRead(inpstr);
         java.util.Iterator<String> it = adjwords.keySet().iterator();
         ArrayList<String> ar= new ArrayList<String>();
         java.util.Iterator<String> it1 = adjwords.keySet().iterator();
         java.util.Iterator<String> it2 = adjwords.keySet().iterator();
         for(int i=0;i<adjwords.size();i++)
         {
        	 for(int j=0;j<adjwords.size();j++)
        		 if(i==j) mat[i][j]="0";
        		 else mat[i][j]="100"; 
         }
         while(it.hasNext())
         {
        	 ar.add(it.next());
        	
         }
         System.out.println(ar);
         while(it1.hasNext())
         {
         if (it1.hasNext()) {
         String word1 = it1.next();
        
         int indx=ar.indexOf(word1);
         System.out.println(indx);
         ArrayList<String> arr = adjwords.get(word1);
         for(int k=0;k<arr.size();k++)
         {
        	 String s[]= arr.get(k).split("_");
        	 int indexneigh= ar.indexOf(s[0]);
        	 System.out.println("neigb index is "+ indexneigh);
        	 mat[indx][indexneigh]=s[1];
        	 System.out.println(mat[indx][indexneigh]);
        	
        	 
        	 
         }
         System.out.println("intradegree"+d.intradegreeofvertex(word1,arr));
              
         }
         }
         
         BufferedWriter bufferedWriter = null;
         try {
        	 
        	   File file = new File("data/newFile.csv");
        	         	         if(!file.exists()){
        	      file.createNewFile();
        	         }
        	  
        	         FileWriter fileWriter = new FileWriter(file);
        	         bufferedWriter = new BufferedWriter(fileWriter);
        	         BufferedReader br1= new BufferedReader(new FileReader("data/newFile.csv"));
        	        
         for(int i=0;i<adjwords.size();i++)
         {
        	 for(int j=0;j<adjwords.size();j++)
        	 {
        		 bufferedWriter.write((1.0-Double.parseDouble(mat[i][j]))+",");
        	 }
        	 bufferedWriter.write("\n");
         }
         } catch (IOException e) {
      	   e.printStackTrace();
      	  } finally {
      	   try {
      	    if (bufferedWriter != null){
      	     bufferedWriter.close();
      	    }
      	   } catch (IOException ex) {
      	    ex.printStackTrace();
      	   }
      	  }
      	   
      	 
	System.out.println(adjwords);
	BufferedReader br1= new BufferedReader(new FileReader("C:/Users/shilpa212/Documents/clustnum"));
	
	
String[] clust=br1.readLine().split(",");
System.out.println(clust.length);


for(int i=0;i<adjwords.size()-1;i++){
	if(it2.hasNext())
	{
		ArrayList<String> n = new ArrayList<String>();
		n.add(clust[i]);
		nodedetails.put(it2.next(), n);
	}
	
	
	
	
}
System.out.println(nodedetails);
	}
}

	
         
         
         
         
        /* for(int i=1;i<=adjwords.size();i++)
         {        	 
        	 for(int j=1;j<=adjwords.size();j++)
        	 {
        		if(it1.hasNext())
        		{
        		 String word1 = it1.next();
        		 System.out.println("key is" + word1);
        		 ArrayList<String> arr = adjwords.get(word1);
        		// System.out.println(arr);
        		 for (int k=0; k<arr.size(); k++)
        		 {
        		String s[]= arr.get(k).split("_");
        		//System.out.println("neighbor is" + s[0]);
        		//System.out.println(s[1]);
        		//System.out.println(mat[i][0]);
        		//System.out.println(mat[0][j]);
        		 if(mat[i][0]==word1 && mat[0][j]==s[0])
        			// if(i<=adjwords.size()&&j<=adjwords.size())
        			 mat[i][j]=s[1];
        		 
        		 	}
        		}
        	 }
         }*/
         
         
        
        	/* for(int j=0;j<=adjwords.size();j++)
        		 
        	 {
        		 if(set.iterator().hasNext())
                 {
                	 Map.Entry me = (Map.Entry)set.iterator().next();
                	 String uniwords= me.getKey().toString();
                	 //[] s= me.getValue().toString().split("_");
                	// bufferedWriter.write(me.getKey().toString()); 
                	// mat[i][0]=uniwords;
                	// System.out.println("init");
            		 mat[0][j]=uniwords;
            		 System.out.println(mat[0][j]);
            		 }
                 }
        		 
        		 
        		 
        	 
         
         for(int i=0;i<=adjwords.size();i++)
         {
        	 for(int j=0;j<=adjwords.size();j++)
        		 
        	 {
        		 if(set.iterator().hasNext())
                 {
                	 Map.Entry me = (Map.Entry)set.iterator().next();
                	// String uniwords= me.getKey().toString();
                	 //[] s= me.getValue().toString().split("_");
                	// bufferedWriter.write(me.getKey().toString()); 
                	// mat[i][0]=uniwords;
            		// mat[0][j]=uniwords;
            		 String q[]=me.getValue().toString().split("_");
            		 if(mat[0][j]==q[0])
            		 {
            			 
            			mat[i][j]=q[0];
            			            		 }
                 }
        		 
        		 System.out.println(mat[i][j]);
        		 
        	 }*/
       //  }
         
       // BufferedWriter bufferedWriter = null;
       /*  try {
        	 
        	   File file = new File("data/newFile.csv");
        	   //to append more data to the existing file change the line to
        	   //File file = new File("data/newFile.txt",true);
        	    
        	         //if file doesn't exists, then create it
        	         if(!file.exists()){
        	      file.createNewFile();
        	         }
        	  
        	         FileWriter fileWriter = new FileWriter(file);
        	         bufferedWriter = new BufferedWriter(fileWriter);
        	         BufferedReader br1= new BufferedReader(new FileReader("data/newFile.csv"));
        	        
        	         int uniqwords= adjwords.size();
        	         Set set = adjwords.entrySet();
        	         while(set.iterator().hasNext())
        	         {
        	        	 Map.Entry me = (Map.Entry)set.iterator().next();
        	        	 //[] s= me.getValue().toString().split("_");
        	        	 bufferedWriter.write(me.getKey().toString()); 
        	        	 
        	         }
        	         
        	       
        	         while(set.iterator().hasNext()) {
        	             Map.Entry me = (Map.Entry)set.iterator().next();
        	          
        	             String[] s= me.getValue().toString().split("_");
        	            while(br1.readLine().trim()!=NULL){
        	          if(br1.readLine().trim()==s[0])
        	             bufferedWriter.write(s[1]);
        	             else
        	            	 bufferedWriter.write("10000");
        	                    	          }
        	         }
        	         
        	                     	        	 
            	      //   bufferedWriter.newLine(); 
        	         //}
        	         
        	         bufferedWriter.write("My second line ");
        	         bufferedWriter.write("keeps going on...");
        	         bufferedWriter.newLine();
        	       
        	     
        	  } catch (IOException e) {
        	   e.printStackTrace();
        	  } finally {
        	   try {
        	    if (bufferedWriter != null){
        	     bufferedWriter.close();
        	    }
        	   } catch (IOException ex) {
        	    ex.printStackTrace();
        	   }
        	  }
        	   
        	 }
*/        // System.out.println(adjwords.size());



