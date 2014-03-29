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
