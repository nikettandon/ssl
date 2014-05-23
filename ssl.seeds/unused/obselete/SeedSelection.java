package obselete;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.FileChunks;
import util.FileLines;

public class SeedSelection {
	public static String mat[][] = new String[5500][5500];
	public static int size;
	public static ArrayList<String> arr;
	private static ArrayList<String>[] vertdetails;

	public SeedSelection(String s) {
		String[] t = s.split("\n");
		size = t.length;
		arr = new ArrayList<String>();
		for (int k = 0; k < size; k++) {
			arr.add(t[k]);
		}
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				if (i == j)
					mat[i][j] = "0";
				else
					mat[j][i] = "100";
			}
		}

	}

	public static void distmatrix(String tk) {
		String tokens[] = tk.split("\t");
		int indx = arr.indexOf(tokens[0]);
		int indxy = arr.indexOf(tokens[1]);
		mat[indx][indxy] = tokens[2];
		BufferedWriter bufferedWriter = null;
		try {

			File file = new File("data/distMatrix5k.csv");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					bufferedWriter.write(mat[i][j] + ",");
				}
				bufferedWriter.write("\n");
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

	public int degreeofvertex(String v) {
		int degree = 0;
		int indx = arr.indexOf(v);
		for (int i = 0; i < arr.size(); i++) {
			if ((mat[indx][i] != "100" && mat[indx][i] != "0")
					|| (mat[i][indx] != "100" && mat[i][indx] != "0")) {
				degree++;
			}
		}
		return degree;
	}

	public int intradegreeofvertex(String v) {
		int intra_deg = 0;
		ArrayList<String> arclust= new ArrayList<String>();
		int indx = arr.indexOf(v);
		String prevclust;
		int distinct=0;
		String clustnum = vertdetails[indx].get(1);
		for (int i = 0; i < arr.size() - 1; i++)// ---------------------
		{
			if ((mat[indx][i] != "100" && mat[indx][i] != "0")
					|| (mat[i][indx] != "100" && mat[i][indx] != "0")) {
				if (vertdetails[i].get(1).equals(clustnum)) {
					intra_deg++;
				}
			}
			
		}

		return intra_deg;
	}
	public int distinterdegreeofvertex(String v) {
		
		ArrayList<String> arclust= new ArrayList<String>();
		int indx = arr.indexOf(v);
		
		int distinct=0;
		String clustnum = vertdetails[indx].get(1);
		for (int i = 0; i < arr.size(); i++)// ---------------------
		{
			if ((mat[indx][i] != "100" && mat[indx][i] != "0")
					|| (mat[i][indx] != "100" && mat[i][indx] != "0")) {
				if (!vertdetails[i].get(1).equals(clustnum)) { 
					arclust.add(vertdetails[i].get(1));
					
					
					Collections.sort(arclust);
					for(int j=1;j<arclust.size();j++)
					{
						if(!arclust.get(j-1).equals(arclust.get(j))) distinct++;
					}
					
				}
			}
			
			
		}
		

		return distinct;
	}

	public static double phi(String x, String mu, String sigma) {
		return phi((Double.parseDouble(x) - Double.parseDouble(mu))
				/ Double.parseDouble(sigma) / Double.parseDouble(sigma));
	}

	public static double phi(double x) {
		return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
	}

	public static double sigmoid(double x) {
		return (1 / (1 + Math.pow(Math.E, (-1 * x))));
	}

	public static double distfromcentre(String cent, String clust) {
		double dist = Double.parseDouble(cent) - Double.parseDouble(clust);
		return dist;
	}

	// public static double meanofclust(String n){

	// }

	/*public static void mainfile(String[] args) {
		for (String line : new FileLines("./data/biginput5k.txt")) {
			System.out.println("--> "+line);
		}

	}*/
	//public static void mainfi(String[] args) {
		//String[] t={"answer","family_electrophoridae","sleeve"};
		
	//	for(List<String> line :new FileChunks("C:/Users/shilpa212/Desktop/inp.txt", true))
		//				System.out.println("--> "+ line);
		//}

	

	public static void main(String args[]) throws IOException {

		String str = "";
		String str1 = "";
		String str2 = "";
		String inpstr = "";
		String line = "";
		String line1 = "";
		//String[][] m1 = new String[100][100];
		BufferedReader br = new BufferedReader(new FileReader(
				"./data/biguniquwwords5k.txt"));
		boolean flag = true;
		while ((str = br.readLine()) != null) {
			if (str.length() == 0)
				flag = false;
			else
				inpstr = inpstr + str + "\n";
		}
		SeedSelection ss = new SeedSelection(inpstr);
		BufferedReader br1 = new BufferedReader(new FileReader(
				"./data/biginput5k.txt"));
		boolean flag1 = true;
		while ((str1 = br1.readLine()) != null) {
			if (str1.length() == 0)
				flag1 = false;
			else
				line = str1;
			ss.distmatrix(line);
		}
		BufferedReader br2 = new BufferedReader(new FileReader(
				"C:/Users/shilpa212/Documents/hclust5k"));
		boolean flag2 = true;
		while ((str2 = br2.readLine()) != null) {
			if (str2.length() == 0)
				flag2 = false;
			else
				line1 = line1 + str2 + "\n";
		}
		String[] clustn = line1.split(",|\n");
		System.out.println(clustn.length);
		System.out.println("arraylist is" + arr.size());

		vertdetails = (ArrayList<String>[]) new ArrayList[50];
		for (int v = 0; v < arr.size(); v++) {
			vertdetails[v] = new ArrayList<String>();
		}

		System.out.println(arr);
		// vertdetails[0].add(arr.get(0));
		// vertdetails[0].add("1");
		for (int i = 0; i < arr.size(); i++) { // -------------------------
			String vertex = arr.get(i);

			// int k=i-1;
			String clust = clustn[i];
			
			vertdetails[i].add(vertex);
			vertdetails[i].add(clust);
			

		}
		for (int i = 0; i < arr.size(); i++) {
			double pd1, pd2, pd3;
			
			String vertex = arr.get(i);
			pd2 = sigmoid(ss.distinterdegreeofvertex(vertex));
			pd3 = sigmoid(ss.intradegreeofvertex(vertex)
					/ss.distinterdegreeofvertex(vertex));
			System.out.println("--------------------------");
			System.out.println(vertdetails[i]);
			System.out.println("degree" + ss.degreeofvertex(vertex));
			System.out.println("Intradegree" + ss.intradegreeofvertex(vertex));
			System.out.println("Interdegree" + (ss.degreeofvertex(vertex)-ss.intradegreeofvertex(vertex)));
			System.out.println("distinct inter degree" + ss.distinterdegreeofvertex(vertex));
			System.out.println("Noise pdf is" + pd2);
			System.out.println("Seed Selection pdf is" + pd3);
		}

		// System.out.println( ss.intradegreeofvertex("answer"));

	}

}
