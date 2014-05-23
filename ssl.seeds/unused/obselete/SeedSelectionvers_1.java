/********** input- sorted both sides similarity file(and its size) and unique words(its size)**/

package obselete;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import util.AutoMap;
import util.FileChunks;
import util.Util.Progress;

public class SeedSelectionvers_1 {
public static ArrayList<String> arr;
private static ArrayList<String>[] vertdetails;// cluster and vertex 
private static ArrayList<String>[] adj =
  (ArrayList<String>[]) new ArrayList[1299];

int V;
interface IScorer {
abstract public double score(int distinctInter,int intra);
}
/************* adjacency list******************/
public void initadj(int v,String s){

  adj[v] = new ArrayList<String>();

  String[] t = s.split(" ");

 // System.out.println("-------------------" + t[0]);

  for(int j = 1; j < t.length; j++){
    adj[v].add(t[j]);
    j++;
  }
 // System.out.println("mat ---------" + adj[v]);
}

public static Iterable<String> adj(int v){
  return adj[v];
}

public double degreeofvertex(int v){
  int degree = 0;
  for(String w: adj(v))
    degree++;
  return degree;

}

public double intradegreeofvertex(int v){
  int intra_deg = 0;

  int clustnum = Integer.parseInt(vertdetails[v].get(1));// ???????????????
  // System.out.println("clust "+clustnum);
  for(String w: adj(v)){

    if(Integer.parseInt(vertdetails[Integer.parseInt(w)-1].get(1)) == clustnum){

      intra_deg++;
    }
  }
  return intra_deg;
}

public double distinterdegreeofvertex(int v){
  int distinct = 0;
  ArrayList<String> arclust = new ArrayList<String>();
  int clustnum = Integer.parseInt(vertdetails[v].get(1));// ???????????????
  // System.out.println("clust "+clustnum);
  for(String w: adj(v)){

    if(Integer.parseInt(vertdetails[Integer.parseInt(w)-1].get(1)) != clustnum){

      arclust.add(vertdetails[Integer.parseInt(w)-1].get(1));
    }
  }

  Collections.sort(arclust);
  for(int j = 1; j < arclust.size(); j++){
    if(!arclust.get(j - 1).equals(arclust.get(j))) distinct++;
  }

  return distinct;
}

public static double sigmoid(double x){
  return (1 / (1 + Math.pow(Math.E, (-1 * x))));
}
 public static void fileinputread()
 {
	 BufferedWriter bufferedWriter = null;
	  
	  try{

	    File file = new File("./seeddata/bigFileChuncks5k.txt");
	    if(!file.exists()){
	      file.createNewFile();
	    }

	    FileWriter fileWriter = new FileWriter(file);
	    bufferedWriter = new BufferedWriter(fileWriter);
	    for(List<String> chunk: new FileChunks("./seeddata/biguniq5krev.txt", true, ",")){
	      for(String s: chunk)
	        bufferedWriter.write(s + "\n");
	      bufferedWriter.write("====================\n");
	    }

	  } catch (IOException e){
	    e.printStackTrace();
	  } finally{
	    try{
	      if(bufferedWriter != null){
	        bufferedWriter.close();
	      }
	    } catch (IOException ex){
	      ex.printStackTrace();
	    }
	  } 
 }
public static void clutoinput() throws IOException
{
	String str = "";
	  String uniqstr = "";
	   ArrayList<String> ar = new ArrayList<String>();
	  ArrayList<String> unique = new ArrayList<String>();
	  BufferedReader br =
	    new BufferedReader(new FileReader("./seeddata/bigFileChuncks5k.txt"));
	  BufferedReader br1 =
	    new BufferedReader(new FileReader("./seeddata/biguniquwwords5k.txt"));
	  while ((uniqstr = br1.readLine()) != null){
	    unique.add(uniqstr);

	  }
	  //System.out.println(unique);
	  BufferedWriter bufferedWriter = null;
	  BufferedWriter bufferedWriterrow = null;
	  BufferedWriter bufferedWritercol = null;
	  String rowlabel = "";
	  try{

	    File file = new File("./seeddata/bigFileChuncks5k.mat");
	    File filerow = new File("./seeddata/bigFileChuncksrow5k.mat.rlabel");
	    File filecol = new File("./seeddata/bigFileChunckscol5k.mat.clabel");
	    if(!file.exists() && !filerow.exists() && !filecol.exists()){
	      file.createNewFile();
	      filerow.createNewFile();
	      filecol.createNewFile();
	    }

	    FileWriter fileWriter = new FileWriter(file);
	    FileWriter fileWriterrow = new FileWriter(filerow);
	    FileWriter fileWritercol = new FileWriter(filecol);
	    bufferedWriter = new BufferedWriter(fileWriter);
	    bufferedWriterrow = new BufferedWriter(fileWriterrow);
	    bufferedWritercol = new BufferedWriter(fileWritercol);

	    // TODO keep writing lines to an arraylist.
	    // Util.writefile(path,arraylist,false)
	    // OR
	    Progress p = new Progress(10);
	    p.storageInit(".data/myoutFile", false);
	    p.store("abcd");
	    p.next();
	    // In the end
	    p.flush();

	    for(int i = 0; i < unique.size(); i++){
	      bufferedWritercol.write(unique.get(i));
	      bufferedWritercol.write("\n");
	    }
	    bufferedWriter.write("1299");
	    bufferedWriter.write(" ");
	    bufferedWriter.write("1299");
	    bufferedWriter.write(" ");
	    bufferedWriter.write("10049");
	    bufferedWriter.write("\n");

	    while ((str = br.readLine()) != null){

	      if(!str.equals("====================")){
	        String[] token = str.split(",");
	        rowlabel = token[0];
	        bufferedWriter.write(" ");
	        int j = unique.indexOf(token[1]);
	        if(j < 10049) bufferedWriter.write(String.valueOf(j+1));
	        bufferedWriter.write(" ");
	        double val = Double.parseDouble(token[2]);

	        bufferedWriter.write(String.valueOf(1.0 - val));
	      } else{
	        bufferedWriterrow.write(rowlabel);
	        bufferedWriterrow.write("\n");
	        bufferedWriter.write("\n");
	        continue;
	      }
	    }
	  } catch (IOException e){
	    e.printStackTrace();
	  } finally{
	    try{
	      if(bufferedWriter != null){
	        bufferedWriter.close();
	      }
	      if(bufferedWriterrow != null){
	        bufferedWriterrow.close();
	      }
	      if(bufferedWritercol != null){
	        bufferedWritercol.close();
	      }
	    } catch (IOException ex){
	      ex.printStackTrace();
	    }
	  }
	  
}

public static void main(String[] args) throws IOException{
  SeedSelectionvers_1 st = new SeedSelectionvers_1();
  st.fileinputread();
  st.clutoinput();
  

  
  /*****************arraylist of adjacency vertices************************************/
  String strneig = "";
  String line3 = "";
  BufferedReader brneighb =
    new BufferedReader(new FileReader("./seeddata/bigFileChuncks5k.mat"));
  strneig = brneighb.readLine();
  for(int i = 0; i < 1299; i++){
    strneig = brneighb.readLine();
    st.initadj(i, strneig);
  }
  
  /**************arraylist of vertex and cluster using solution.sol******************/
  String strread = "";
  String line1 = "";
  String strclust = "";
  String line2 = "";
  arr = new ArrayList<String>();
  BufferedReader brclust =
    new BufferedReader(new FileReader("./seeddata/solution.sol"));
  BufferedReader brrow =
    new BufferedReader(new FileReader("./seeddata/bigFileChuncksrow5k.mat.rlabel"));
  boolean flag2 = true;
  while ((strread = brclust.readLine()) != null){
    if(strread.length() == 0) flag2 = false;
    else line1 = line1 + strread + "\n";
  }

  while ((strclust = brrow.readLine()) != null){
    arr.add(strclust);
  }
  String[] clustn = line1.split("\n");
  vertdetails = (ArrayList<String>[]) new ArrayList[1299];
  for(int v = 0; v < arr.size(); v++){
    vertdetails[v] = new ArrayList<String>();
  }

  for(int i = 0; i < arr.size(); i++){
    String vertex = arr.get(i);

    String clust = clustn[i];

    vertdetails[i].add(vertex);
    vertdetails[i].add(clust);

  }
/***********************************************************/
  topK(st);

 }

/**
 * 
 * @param scorer
 * <PRE> 
 * scoreSeed(new IScorer() {
 * @param distinctInter 
 * @param intra 
    @Override public double score(int distinctInter,int intra){
      return sigmoid(intra);
    }
  });
  </PRE>
 * @return
 */
public static double scoreSeed(IScorer scorer,int distinctInter,int intra){
  // TODO:
  return scorer.score(distinctInter, intra);
}

public static void topK(SeedSelectionvers_1 st){

  AutoMap<String, Double> mSeeds = new AutoMap<>();
  AutoMap<String, Double> mNoise = new AutoMap<>();
  System.out.println(arr);

  for(int i = 0; i < arr.size(); i++){
    double pd1,pd2,pd3;
    String vertex = arr.get(i);
    pd2 = sigmoid(st.distinterdegreeofvertex(i));
    pd3 =
      sigmoid(st.intradegreeofvertex(i) / (1 + st.distinterdegreeofvertex(i)));
    
    mSeeds.put(vertex, pd3);
    mNoise.put(vertex, pd2);

  }

  printtopK(mSeeds, mNoise);

}

private static void printtopK(AutoMap<String, Double> mSeeds,
  AutoMap<String, Double> mNoise){
  int topKSeeds = 100;
  int topKNoise = 100;

  System.out.println("Top " + topKSeeds + " seeds... ");
  TreeMap<String, Double> sortedMSeeds = mSeeds.sortByValue();
  for(Entry<String, Double> e: sortedMSeeds.entrySet()){
    if(topKSeeds-- < 0) break;
    System.out.println(topKSeeds + ". " + e.getKey());
  }

  System.out.println("Top " + topKNoise + " noise seeds... ");
  TreeMap<String, Double> sortedMNoise = mNoise.sortByValue();
  for(Entry<String, Double> e: sortedMNoise.entrySet()){
    if(topKNoise-- < 0) break;
    System.out.println(topKNoise + ". " + e.getKey());
  }
}

}
