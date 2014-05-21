package util;
/** Calculates time in millisecs for program execution.
 * 
 * @author ntandon
 * @version1.0
 * @since 15.01.2010 */
public class Timer {
public long mStartTime;
public double totalTime;

public Timer() {
  this.totalTime=0.0;
  this.mStartTime = System.currentTimeMillis();
}

public void reset(){
  this.mStartTime = System.currentTimeMillis();
}

public void reset(boolean resetTotalTime){
  if(resetTotalTime)  this.totalTime = 0.0;
  reset();
}

public double time(String msg){
  return time(msg, false);
}

public double time(String msg,boolean mute){
  long endTime = System.currentTimeMillis();
  double totalTime = (endTime - mStartTime) / 1000.0;
  if(!mute) System.out.println("  " + msg + "  done(" + totalTime + " sec.)");
  reset();
  return totalTime;
}

public double time(){
  long endTime = System.currentTimeMillis();
  double totalTime = (endTime - mStartTime) / 1000.0;
  System.out.println("  done(" + totalTime + " sec.)");
  reset();
  return totalTime;
}

public double timeSpan(){
  long endTime = System.currentTimeMillis();
  double totalTime = (endTime - mStartTime) / 1000.0;
  return totalTime;
}

public static void calculate(long startTime){
  long endTime = System.currentTimeMillis();
  double totalTime = (endTime - startTime) / 1000;
  System.out.println("\ndone (" + totalTime + " seconds)");
}

public static void calculate(long startTime,String message){
  long endTime = System.currentTimeMillis();
  double totalTime = (endTime - startTime) / 1000;
  System.out.println(message + totalTime);
}

public static void calculateMemory(){
  long free = Runtime.getRuntime().freeMemory();
  long max = Runtime.getRuntime().maxMemory();
  long tot = Runtime.getRuntime().totalMemory();
  System.out.println("\n\nTotal Memory snapshot:\nfree= " + free + "\t max= " + max + "\t total= " + tot);
}
}
