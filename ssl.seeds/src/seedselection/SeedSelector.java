/********** input- sorted both sides similarity file(and its size) and unique words(its size)**/

package seedselection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
	private static int m = 41335;
	private static int n = 347072;
	private static String path = "";
	private static ArrayList<String> unique;

	public String score(double distiner, double intra) {
		double pd2, pd3;
		pd2 = sigmoid(distiner);
		pd3 = sigmoid(intra / (1 + distiner));

		return pd2 + "_" + pd3;
	}

	public static double sigmoid(double x) {
		return (1 / (1 + Math.pow(Math.E, (-1 * x))));
	}

	public static void fileinputread() {

		ArrayList<String> archunks = new ArrayList<String>();
		for (List<String> chunk : new FileChunks(path
				+ "/data/bigoutputsorted.txt", true, ",")) {
			for (String s : chunk)
				archunks.add(s);
			archunks.add("====================");

		}
		util.Util.writeFile(path + "/data/bigFileChuncks.txt", archunks, false);

	}

	public static void clutoinput() throws IOException {
		unique = new ArrayList<String>();
		for (String uniqstr : new FileLines(path + "/data/biguniquewords.txt")) {
			unique.add(uniqstr);
		}

		util.Util.writeFile(path + "/data/bigFileChunckscol.mat.clabel",
				unique, false);
		util.Util.writeFile(path + "/data/bigFileChuncksrow.mat.rlabel",
				unique, false);

		ArrayList<String> ar = new ArrayList<String>();
		BufferedWriter bufferedWriter = null;

		String rowlabel = "";
		try {

			File file = new File(path + "/data/bigFileChuncks.mat");

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

			for (String str : new FileLines(path + "/data/bigFileChuncks.txt")) {
				if (!str.equals("====================")) {
					String[] token = str.split(",");
					rowlabel = token[0];
					bufferedWriter.write(" ");
					int j = unique.indexOf(token[1]);
					if (j < m + 1)
						bufferedWriter.write(String.valueOf(j + 1));
					bufferedWriter.write(" ");
					double val = Double.parseDouble(token[2]);

					bufferedWriter.write(String.valueOf(1.0 - val));
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
		String command = args[0];
		path = args[1];
		SeedSelector st = new SeedSelector();
		vertexinfo vobj = new vertexinfo();
		String outFolder = path + "/data/"; // args[1]

		switch (command) {
		case "genClutoInput":
			st.fileinputread();
			st.clutoinput();
			break;

		case "genTopK":
			unique = Util.readFileAsList(path + "/data/biguniquewords.txt");
			System.out.println(unique);
			vobj.initadj(unique, path);
			vobj.initvertclust(unique, path);
			st.topK(vobj, outFolder);
			break;

		default:
			break;
		}

	}

	public void topK(vertexinfo vobj, String outFolder) {

		AutoMap<String, Double> mSeeds = new AutoMap<>();
		AutoMap<String, Double> mNoise = new AutoMap<>();
		for (int i = 0; i < unique.size(); i++) {
			String vertex = unique.get(i);

			String sc[] = score(vobj.distinterdegreeofvertex(i),
					vobj.intradegreeofvertex(i)).split("_");

			mSeeds.put(vertex, Double.parseDouble(sc[1]));
			if (!vertex.contains("#")) // TODO words are connected to senses.
				mNoise.put(vertex, Double.parseDouble(sc[0]));

		}
		printtopK(mSeeds, mNoise, outFolder);

	}

	private void printtopK(AutoMap<String, Double> mSeeds,
			AutoMap<String, Double> mNoise, String outFolder) {
		int topKSeeds = 100;
		int topKNoise = 100;

		System.out.println("Top " + topKSeeds + " seeds... ");
		TreeMap<String, Double> sortedMSeeds = mSeeds.sortByValue();
		List<String> topkSeedList = new ArrayList<>();
		for (Entry<String, Double> e : sortedMSeeds.entrySet()) {
			if (topKSeeds-- < 0)
				break;
			topkSeedList.add(e.getKey());
			System.out.println(topKSeeds + ". " + e.getKey());
		}
		if (!outFolder.endsWith(File.pathSeparator))
			outFolder += outFolder + File.pathSeparator;
		Util.writeFile(outFolder + "topkseeds", topkSeedList, false);

		List<String> topkNoiseList = new ArrayList<>();
		System.out.println("Top " + topKNoise + " noise seeds... ");
		TreeMap<String, Double> sortedMNoise = mNoise.sortByValue();
		for (Entry<String, Double> e : sortedMNoise.entrySet()) {
			if (topKNoise-- < 0)
				break;
			topkNoiseList.add(e.getKey());
			System.out.println(topKNoise + ". " + e.getKey());
		}

		Util.writeFile(outFolder + "topknoise", topkNoiseList, false);
	}

}
