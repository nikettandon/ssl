package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** An iterator over a file that returns chunks/blocks of the file at a time.
 * The boundary of a block is defined by the first column.  
 * @input inFile sample:
<PRE>
tandon niket 123
tandon anjali 22
tandon saket 123
mehra abc 11
mehra yyy 22
kapoor ab 1
</PRE>

 * @output Returns chunks one by one e.g.
<PRE> 
tandon niket 123
tandon anjali 22
tandon saket 123
====================
mehra abc 11
mehra yyy 22
====================
kapoor ab 1
====================
</PRE>
 * @author ntandon
 *
 */
public class FileChunks implements Iterable<List<String>> {

private BufferedReader br;
private String tsvSeparator;

/** 
 * 
 * @param inFile sample (boundary defined by first column):
<PRE>
tandon niket 123
tandon anjali 22
tandon saket 123
mehra abc 11
mehra yyy 22
kapoor ab 1
</PRE>
 * @param isSorted in must be set to true.
 * @param tsvSeparator e.g. column separator in input file.
 * @return Returns chunks one by one e.g.
<PRE> 
tandon niket 123
tandon anjali 22
tandon saket 123
====================
mehra abc 11
mehra yyy 22
====================
kapoor ab 1
====================
</PRE>
 */
public FileChunks(String inFile,boolean isSorted,String tsvSeparator) {
  try{
    if(!isSorted){
      // Collections.sort(inFile);// TODO 1: sort files.
      // can be too big to sort.
      throw new UnsupportedOperationException(
        "FileChunksBetween only operates on sorted files: inputfile " + inFile
          + " is not sorted");
    }

    br = new BufferedReader(new FileReader(inFile));
    this.tsvSeparator = tsvSeparator;
  } catch (FileNotFoundException e){
    e.printStackTrace();
  }
}



@Override public Iterator<List<String>> iterator(){
  return new Iterator<List<String>>() {
    List<String> lines = new ArrayList<>();
    List<String> bufferedLines = new ArrayList<>();
    String line = "";
    String prevFirstCol = "";
    boolean began = false;

    @Override public boolean hasNext(){
      try{
        boolean flag = true;
        lines = new ArrayList<>();

        /* In the prev. iteration, ensure if cursor moved to the next line. */
        lines.addAll(bufferedLines);
        bufferedLines = new ArrayList<>();

        while (flag){ // +NT
          line = br.readLine();

          /* Cursor moved to the last line, return the chunk already built. */
          boolean chunkHasData = (lines != null && lines.size() > 0);
          if(line == null && chunkHasData) return true;

          /* Cursor moved to the last line, and there is no chunk data. */
          else if(line == null && !chunkHasData) return false;

          String t[] = line.split(tsvSeparator);

          /* Determine if the next line's column 1 is same as prev line. */
          if(!began || (t.length > 0 && t[0].equals(prevFirstCol))){

            /* Within a block. */
            lines.add(line);
          } else{

            /* Cursor has already moved to the next line, buffer it. */
            bufferedLines = new ArrayList<>();

            /* Store the buffer's line to the next block */
            bufferedLines.add(line);
            flag = false;
          }

          prevFirstCol = t[0];
          if(!began) began = true;
        }
      } catch (IOException e){
        e.printStackTrace();
        lines = null;
        line = null;
      }
      return lines != null && lines.size() > 0;
    }

    @Override public List<String> next(){
      return lines;
    }

    @Override public void remove(){}
  };
}

}