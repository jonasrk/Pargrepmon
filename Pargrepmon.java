import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Thread;

class Pargrepmon{
	static String[] search_strings = new String[1];
	static String first_input_string = null;
	static String second_input_string = null;
	static List<Wordcount> output_list = new ArrayList<Wordcount>();

	public static void main(String[] args){
		//Read input
	    File first_input_file = new File(args[0]);
	    try{
		try{
	    FileReader reader = new FileReader(first_input_file);
	    char[] chars = new char[(int) first_input_file.length()];
	    reader.read(chars);
	    search_strings = (new String(chars)).split("\\s+");
		reader.close();
	    
		File first_input_file2 = new File(args[1]);
		FileReader reader2 = new FileReader(first_input_file2);
		char[] chars2 = new char[(int) first_input_file2.length()];
		reader2.read(chars2);
		second_input_string = new String(chars2);
		reader2.close();
		
		int processors = Runtime.getRuntime().availableProcessors();
		
		//Spawn threads and grep
		CentralClass centralclass = new CentralClass(search_strings, second_input_string, output_list, processors);
		Thread[] t = new Thread[processors];
		
		for(int i=0; i < processors; i++) {
			t[i] = new GrepThread(centralclass, i);
			t[i].start();
		}
		
		for(int i=0; i < processors; i++) {
			t[i].join();
		}
		
	   	//generate output	
		System.out.println("output_list is " + output_list.toString());
		
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		for(Wordcount this_wordcount : output_list)
		{
			System.out.println("writing " + this_wordcount.word + " to first_input_file");
		    writer.println(this_wordcount.word + ";" + this_wordcount.count);
			System.out.println("wrote " + this_wordcount.count);
			
		}
		
		writer.close();
		
    	} catch (InterruptedException e) {
        e.printStackTrace();
    	}
		} catch (IOException e) {
        e.printStackTrace();
    	}
	
	}
	
}
	
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

class CentralClass{
	
	String[] search_strings;
	String second_input_string;
	List<Wordcount> output_list;
	boolean list_in_use = false;
	int processors;
	
	public CentralClass(String[] search_strings, String second_input_string, List<Wordcount> output_list, int processors){
		this.search_strings = search_strings;
		this.second_input_string = second_input_string;
		this.output_list = output_list;
		this.processors = processors;
	}
    
	synchronized void lookforit(int thread_id){
        // get string to search for
        // look for the string in buffer
		
		for (int i = 0; i < search_strings.length; i++){
			
			if (i%processors == thread_id){
			
			System.out.println("In GrepThread " + thread_id + " - part: " + search_strings[i]);
			Pattern pattern = Pattern.compile(search_strings[i]);
			Matcher matcher = pattern.matcher(second_input_string);
			Wordcount this_wordcount = new Wordcount();
			this_wordcount.word = search_strings[i];
			this_wordcount.count = 0;
		
			while (matcher.find()) {
				System.out.println("In GrepThread " + thread_id + "FOUND: " + this_wordcount.word);
				this_wordcount.count++;
		    
			}
			
			output_list.add(this_wordcount);
		
		}}
		
        // write string to result list
    }
}

class Wordcount {
  public String word;
  public int count;
}