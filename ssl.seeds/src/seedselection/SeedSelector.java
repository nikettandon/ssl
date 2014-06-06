/********** input- sorted both sides similarity file(and its size) and unique words(its size)**/

package seedselection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import util.AutoMap;
import util.FileChunks;
import util.FileLines;
import util.Util;

/**
 * TODO (1):
 * 
 * <PRE>
 * // install cygwin
 * // read config file and use the params in the following commands.
 * java -jar GraphClustering.SeedSelection genClutoInput /path/to/data/ otherargs
 * /path/to/cluto cluster argstocluto inputfile outputfile
 * java -jar GraphClustering.SeedSelection genGraphForTopK /path/to/clutooutput/ otherargs
 * java -jar GraphClustering.SeedSelection genTopK /path/to/adjacencygraph/ otherargs
 * // TODO 4. otherargs e.g. seed scoring strategy.  
 * // TODO 5. eval (test data should be input) .. based on this , eval. prec/recall for topk seeds and noise
 * // TODO 6. random seeds as input VS selected seeds as labeled input to Label prop.
 * 
 * // TODO 7. Code repo. on bitbucket
 * </PRE>
 * 
 * @author shilpa212
 */
public class SeedSelector {
	private static int m = 13919;
	private static int n = 185002;
	private static String path = "";
	private static ArrayList<String> unique;

	public String score(double distiner, double intra) {
		double pd2, pd3;
		pd2 = distiner;
		pd3 = intra / (1 + distiner);

		return pd2 + "_" + pd3;
	}

	public static double sigmoid(double x) {
		return (1 / (1 + Math.pow(Math.E, (-1 * x))));
	}

	public static void fileinputread() {

		ArrayList<String> archunks = new ArrayList<String>();
		for (List<String> chunk : new FileChunks(path
				+ "/data/eval-data/sensitivity/sortedinput.txt", true, "\t")) {
			for (String s : chunk)
				archunks.add(s);
			archunks.add("====================");

		}
		util.Util.writeFile(path
				+ "/data/eval-data/sensitivity/bigFileChuncks.txt", archunks,
				false);

	}

	public static void clutoinput() throws IOException {
		unique = new ArrayList<String>();
		for (String uniqstr : new FileLines(path
				+ "/data/eval-data/sensitivity/uniqueword.txt")) {
			unique.add(uniqstr);
		}

		util.Util.writeFile(path
				+ "/data/eval-data/sensitivity/bigFileChunckscol.mat.clabel",
				unique, false);
		util.Util.writeFile(path
				+ "/data/eval-data/sensitivity/bigFileChuncksrow.mat.rlabel",
				unique, false);

		ArrayList<String> ar = new ArrayList<String>();
		BufferedWriter bufferedWriter = null;

		String rowlabel = "";
		try {

			File file = new File(path
					+ "/data/eval-data/sensitivity/bigFileChuncks.mat");

			if (!file.exists()) {
				file.createNewFile();

			}

			FileWriter fileWriter = new FileWriter(file);

			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(String.valueOf(m));
			bufferedWriter.write(" ");
			bufferedWriter.write(String.valueOf(m));
			bufferedWriter.write(" ");
			bufferedWriter.write(String.valueOf(n));
			bufferedWriter.write("\n");

			for (String str : new FileLines(path
					+ "/data/eval-data/sensitivity/bigFileChuncks.txt")) {
				if (!str.equals("====================")) {
					String[] token = str.split("\t");
					rowlabel = token[0];
					bufferedWriter.write(" ");
					int j = unique.indexOf(token[1]);
					if (j < m + 1)
						bufferedWriter.write(String.valueOf(j + 1));
					bufferedWriter.write(" ");
					double val = Double.parseDouble(token[2]);

					bufferedWriter.write(String.valueOf(val));
				} else {
					bufferedWriter.write("\n");
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		// Evaluator eval= new Evaluator();
		String command = args[0];
		path = args[1];
		SeedSelector st = new SeedSelector();
		vertexinfo vobj = new vertexinfo();
		String outFolder = path + "/data/eval-data/sensitivity/"; // args[1]

		switch (command) {
		case "genClutoInput":
			st.fileinputread();
			st.clutoinput();
			break;

		case "genTopK":
			unique = Util.readFileAsList(path
					+ "/data/eval-data/sensitivity/uniqueword.txt");
			// System.out.println(unique);
			vobj.initadj(unique, path);
			// vobj.initspectralmat(unique, path);
			vobj.initvertclust(unique, path);
			st.topK(vobj, outFolder);
			break;

		default:
			break;
		}

	}

	public void topK(vertexinfo vobj, String outFolder) {

		
		HashMap<String,ArrayList<String>> mClust=new HashMap<>();
		int points_in_clust=0;
		// String sc[] = score(vobj.distinterdegreeofvertex(j),		vobj.intradegreeofvertex(j)).split("_");
		//String str1="";
		//private static ArrayList<String>[] clustpoints = (ArrayList<String>[]) new ArrayList[30];
		for (String str : new FileLines("/home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/bigsolution.sol")) 
		{
			
			if(!mClust.containsKey(str))
			{
				ArrayList<String> ar= new ArrayList<>();
				String sc= score(vobj.distinterdegreeofvertex(points_in_clust),		vobj.intradegreeofvertex(points_in_clust));
				ar.add(String.valueOf(points_in_clust) + "_" + sc);
				mClust.put(str, ar);
			}
			else{
				ArrayList<String> ar1 = mClust.get(str);
				String sc= score(vobj.distinterdegreeofvertex(points_in_clust),		vobj.intradegreeofvertex(points_in_clust));
				ar1.add(String.valueOf(points_in_clust) + "_" + sc);

				mClust.put(str, ar1);
			}
			
			points_in_clust++;
		}
		
		Set<Entry<String, ArrayList<String>>> e=mClust.entrySet();
	Iterator<Entry<String, ArrayList<String>>> i=e.iterator();
	while(i.hasNext())
	{
		Map.Entry me = (Map.Entry)i.next();
		System.out.println("-------"+ me.getKey() + me.getValue());
		AutoMap<String, Double> mSeeds = new AutoMap<>();
		AutoMap<String, Double> mNoise = new AutoMap<>();
		for(int k=0;k<mClust.get(me.getKey()).size();k++)
		{
			ArrayList<String> ac=mClust.get(me.getKey());
			String ac1[]=ac.get(k).split("_");
			mSeeds.put(unique.get(Integer.parseInt(ac1[0])),Double.parseDouble(ac1[2]));
			//if (!unique.get(Integer.parseInt(ac1[0])).contains("#")) // TODO words are connected to senses.
				mNoise.put(unique.get(Integer.parseInt(ac1[0])), Double.parseDouble(ac1[1]));
		}
		printtopK(mSeeds, mNoise, outFolder);
		
	}
	ArrayList<String> topkSeedList= new ArrayList<>();
	ArrayList<String> topkNoiseList= new ArrayList<>();
	for (String str : new FileLines("/home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/topkseeds.txt"))
		topkSeedList.add(str);
	for (String str1 : new FileLines("/home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/topknoise.txt"))
		topkNoiseList.add(str1);
	
	
	try {
		System.out.println("\n\n\n\n\n\nTopKSeed evaluation: "
				+ Evaluator.evalAutoSeeds(topkSeedList, outFolder));
		System.out.println("TopKNoise evaluation: "
				+ Evaluator.evalNoiseSeeds(topkNoiseList, outFolder));
	} catch (Exception e1) {
		e1.printStackTrace();
	}
	
	}
	/*public void topK(vertexinfo vobj, String outFolder) {

		AutoMap<String, Double> mSeeds = new AutoMap<>();
		AutoMap<String, Double> mNoise = new AutoMap<>();
		for (int i = 0; i < unique.size(); i++) {
			String vertex = unique.get(i);

			String sc[] = score(vobj.distinterdegreeofvertex(i),
					vobj.intradegreeofvertex(i)).split("_");

			mSeeds.put(vertex, Double.parseDouble(sc[1]));
			//mNoise.put(vertex, Double.parseDouble(sc[0]));
			if (vertex.contains("#")) // TODO words are connected to senses.
				mNoise.put(vertex, Double.parseDouble(sc[0]));

		}
		printtopK(mSeeds, mNoise, outFolder);

	}*/

	private void printtopK(AutoMap<String, Double> mSeeds,
			AutoMap<String, Double> mNoise, String outFolder) {
		int topKSeeds = 10;
		int topKNoise = 10;
		outFolder = "/home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/";
		// TODO remove hardcoded outFolder.

		System.out.println("Top " + topKSeeds + " seeds... ");
		TreeMap<String, Double> sortedMSeeds = mSeeds.sortByValue();
		//Map<String, Integer> newMap = new TreeMap(Collections.reverseOrder());
		//newMap.putAll(myMap);
		//Collections.reverse(sortedMSeeds);
		List<String> topkSeedList = new ArrayList<>();

		for (Entry<String, Double> e : sortedMSeeds.entrySet()) {
			if (topKSeeds-- < 0)
				break;
			topkSeedList.add(e.getKey());
			System.out.println(topKSeeds + ". " + e.getKey());
		}

		if (!outFolder.endsWith(File.separator))
			outFolder += File.separator;

		Util.writeFile(outFolder + "topkseeds.txt", topkSeedList, true);

		List<String> topkNoiseList = new ArrayList<>();
		System.out.println("Top " + topKNoise + " noise seeds... ");
		TreeMap<String, Double> sortedMNoise = mNoise.sortByValue();
		for (Entry<String, Double> e : sortedMNoise.entrySet()) {
			if (topKNoise-- < 0)
				break;
			topkNoiseList.add(e.getKey());
			System.out.println(topKNoise + ". " + e.getKey());
		}
		

		
		Util.writeFile(outFolder + "topknoise.txt", topkNoiseList, true);
	}

}
