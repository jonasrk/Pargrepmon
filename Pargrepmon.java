import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Thread;

class Pargrepmon{
	public static void main(String[] args){
		String[] search_strings = new String[1];
		String first_input_string = null;
		String second_input_string = null;
		List<Wordcount> output_list = new ArrayList<Wordcount>();
		
		//Read input and detect number of cpus
	    File first_input_file = new File(args[0]);
	    try{
		try{
	    FileReader reader = new FileReader(first_input_file);
	    char[] first_file_char_array = new char[(int) first_input_file.length()];
	    reader.read(first_file_char_array);
	    search_strings = (new String(first_file_char_array)).split("\\s+");
		reader.close();
	    
		File first_input_file2 = new File(args[1]);
		FileReader reader2 = new FileReader(first_input_file2);
		char[] second_file_char_array = new char[(int) first_input_file2.length()];
		reader2.read(second_file_char_array);
		second_input_string = new String(second_file_char_array);
		reader2.close();
		
		int number_of_processors = Runtime.getRuntime().availableProcessors();
		
		//Spawn threads and grep
		CentralClass centralclass = new CentralClass(search_strings, second_input_string, output_list, number_of_processors);
		Thread[] t = new Thread[number_of_processors];
		for(int i=0; i < number_of_processors; i++) {
			t[i] = new GrepThread(centralclass, i);
			t[i].start();
		}
		
		for(int i=0; i < number_of_processors; i++) {
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
	int number_of_processors;
	
	public CentralClass(String[] search_strings, String second_input_string, List<Wordcount> output_list, int number_of_processors){
		this.search_strings = search_strings;
		this.second_input_string = second_input_string;
		this.output_list = output_list;
		this.number_of_processors = number_of_processors;
	}
    
	synchronized void lookforit(int thread_id){
        // get string to search for
        // look for the string in buffer
		
		for (int i = 0; i < search_strings.length; i++){
			
			if (i%number_of_processors == thread_id){
			
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