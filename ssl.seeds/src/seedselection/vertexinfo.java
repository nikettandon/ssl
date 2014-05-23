package seedselection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import util.FileLines;

public class vertexinfo {
	private static ArrayList<String>[] vertdetails;// cluster and vertex

	private static ArrayList<String>[] adj = (ArrayList<String>[]) new ArrayList[41335];

	public static void initadj(ArrayList<String> unique, String path)
			throws IOException {
		// int v=unique.size();
		String strneig = "";
		String line3 = "";
		BufferedReader brneighb = new BufferedReader(new FileReader(path
				+ "/data/bigFileChuncks.mat"));
		strneig = brneighb.readLine();
		// TODO 3. use FileLines
		for (int i = 0; i < unique.size(); i++) {
			strneig = brneighb.readLine();
			// vobj.initadj(i, strneig);
			adj[i] = new ArrayList<String>();

			String[] t = strneig.split(" ");

			for (int j = 1; j < t.length; j++) {
				adj[i].add(t[j]);
				j++;
			}
			//System.out.println(adj[i]);
		}
	}

	public static void initvertclust(ArrayList<String> unique, String path) {
		String line1 = "";
		String strclust = "";
		String line2 = "";

		boolean flag2 = true;

		for (String strread : new FileLines(path + "/data/bigsolution.sol")) {
			if (strread.length() == 0)
				flag2 = false;
			else
				line1 = line1 + strread + "\n";
		}

		String[] clustn = line1.split("\n");
		vertdetails = (ArrayList<String>[]) new ArrayList[unique.size()];
		for (int v = 0; v < unique.size(); v++) {
			vertdetails[v] = new ArrayList<String>();
		}

		for (int i = 0; i < unique.size(); i++) {
			String vertex = unique.get(i);

			String clust = clustn[i];

			vertdetails[i].add(vertex);
			vertdetails[i].add(clust);
			//System.out.println(vertdetails[i]);

		}
	}

	public static Iterable<String> adj(int v) {
		return adj[v];
	}

	// TODO 2. These functions go into a Vertex class
	public double degreeofvertex(int v) {
		return adj[v].size();
	}

	public double intradegreeofvertex(int v) {
		int intra_deg = 0;

		int clustnum = Integer.parseInt(vertdetails[v].get(1));

		for (String w : adj(v)) {

			if ((Integer.parseInt(vertdetails[Integer.parseInt(w) - 1].get(1)) == clustnum) && (Integer.parseInt(vertdetails[Integer.parseInt(w) - 1].get(1))!=-1)) {

				intra_deg++;
			}
		}
		return intra_deg;
	}

	public double distinterdegreeofvertex(int v) {
		int distinct = 0;
		ArrayList<String> arclust = new ArrayList<String>();
		int clustnum = Integer.parseInt(vertdetails[v].get(1));

		for (String w : adj(v)) {

			if ((Integer.parseInt(vertdetails[Integer.parseInt(w) - 1].get(1)) != clustnum) && (Integer.parseInt(vertdetails[Integer.parseInt(w) - 1].get(1))!=-1)) {


				arclust.add(vertdetails[Integer.parseInt(w) - 1].get(1));
			}
		}

		Collections.sort(arclust);
		for (int j = 1; j < arclust.size(); j++) {
			if (!arclust.get(j - 1).equals(arclust.get(j)))
				distinct++;
		}

		return distinct;
	}

}
