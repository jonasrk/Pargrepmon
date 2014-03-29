import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Thread;

class GrepThread extends Thread{
	CentralClass centralClass;
	
	public GrepThread(CentralClass centralClass, int thread_id) {
	       this.centralClass = centralClass;}
	
	@Override public void run(){
		centralClass.lookforit();}}