import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Thread;

class Pargrepmon{
	public static void main(String[] args){
	    try{
		try{
			
		String[] search_strings = new String[1];
		String second_input_string = null;
		List<Wordcount> output_list = new ArrayList<Wordcount>();
		
		//Read input and detect number of cpus
	    File first_input_file = new File(args[0]);

		FileReader first_file_reader = new FileReader(first_input_file);
	    char[] first_file_char_array = new char[(int) first_input_file.length()];
	    first_file_reader.read(first_file_char_array);
	    search_strings = (new String(first_file_char_array)).split("\\s+");
		first_file_reader.close();
	    
		File first_input_file2 = new File(args[1]);
		FileReader second_file_reader = new FileReader(first_input_file2);
		char[] second_file_char_array = new char[(int) first_input_file2.length()];
		second_file_reader.read(second_file_char_array);
		second_input_string = new String(second_file_char_array);
		second_file_reader.close();
		
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
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		for(Wordcount this_wordcount : output_list)
		{
			writer.println(this_wordcount.word + ";" + this_wordcount.count);
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
	int number_of_processors;
	int current_search_string_number = 0;
	
	public CentralClass(String[] search_strings, String second_input_string, List<Wordcount> output_list, int number_of_processors){
		this.search_strings = search_strings;
		this.second_input_string = second_input_string;
		this.output_list = output_list;
		this.number_of_processors = number_of_processors;
	}
    
	void lookforit(int thread_id){
        // get string to search for
		String current_search_string;
		while((current_search_string = getStringToSearchFor()) != "END_STRING"){
        
		// look for the string in buffer
		Wordcount this_wordcount = lookForTheStringInBuffer(current_search_string);
			
		// write string to result list
		writeStringToResultList(this_wordcount);
		
		}}
		
	synchronized String getStringToSearchFor(){
		if (current_search_string_number < search_strings.length){
			current_search_string_number++;
			return search_strings[current_search_string_number-1];
		}
		else return "END_STRING";
	}
	
	synchronized void writeStringToResultList(Wordcount this_wordcount){
		output_list.add(this_wordcount);
	}
	
	Wordcount lookForTheStringInBuffer(String current_search_string){
		Wordcount this_wordcount = new Wordcount();
		Pattern pattern = Pattern.compile(current_search_string);
		Matcher matcher = pattern.matcher(second_input_string);
		this_wordcount.word = current_search_string;
		this_wordcount.count = 0;
		while ( matcher.find() ) {
			this_wordcount.count++;}
		return this_wordcount;
	}
}

class Wordcount {
  public String word;
  public int count;
}