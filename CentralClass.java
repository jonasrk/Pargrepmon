import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Thread;

class CentralClass{
	
	String[] search_strings;
	String second_input_string;
	List<Wordcount> output_list;
	int number_of_processors;
	int current_search_string_number = 0;
	boolean list_in_use, current_num_in_use = false;
	
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
		writeStringToResultList(this_wordcount);}}
		
	synchronized String getStringToSearchFor(){
		while (current_num_in_use){try {this.wait();} catch(InterruptedException e){e.printStackTrace();}}
		current_num_in_use = true;
		if (current_search_string_number < search_strings.length){
			int return_string_number = current_search_string_number;
			current_search_string_number++;
			current_num_in_use = false;
			this.notify();
			return search_strings[return_string_number];
		} else {
		current_num_in_use = false;
		this.notify();
		return "END_STRING";}
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
	
	synchronized void writeStringToResultList(Wordcount this_wordcount){
		while (list_in_use){try {this.wait();} catch(InterruptedException e){e.printStackTrace();}}
		list_in_use = true;
		output_list.add(this_wordcount);
		list_in_use = false;
		this.notify();
	}
}