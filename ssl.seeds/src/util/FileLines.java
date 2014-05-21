package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileLines implements Iterable<String> {

	private BufferedReader br;

	public FileLines(String inFile) {
		try {
			br = new BufferedReader(new FileReader(inFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			String line = "";

			@Override
			public boolean hasNext() {
				try {
					line = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					line = null;
				}
				return line != null;
			}

			@Override
			public String next() {
				return line;
			}

			@Override
			public void remove() {
			}
		};
	}

}
