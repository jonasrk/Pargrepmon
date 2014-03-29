import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Thread;

class GrepThread extends Thread{
	int thread_id;
	CentralClass centralClass;
	
	public GrepThread(CentralClass centralClass, int thread_id) {
	       this.centralClass = centralClass;
		   this.thread_id = thread_id;
	   }
	
	@Override public void run(){
		centralClass.lookforit(thread_id);
	}

}