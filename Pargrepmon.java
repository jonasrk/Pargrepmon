import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Thread;

class Pargrepmon{
	
	static String[] parts = new String[1];
	
    static String content = null;
	static String content2 = null;
	
	static List<Wordcount> myList = new ArrayList<Wordcount>();

	public static void main(String[] args){
		
		
	    File file = new File(args[0]);
	    try {
	        FileReader reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        parts = (new String(chars)).split("\\s+");
			reader.close();
	    
		
		
		File file2 = new File(args[1]);
		FileReader reader2 = new FileReader(file2);
		    char[] chars2 = new char[(int) file2.length()];
		    reader2.read(chars2);
		    content2 = new String(chars2);
		    reader2.close();
			
			int processors = Runtime.getRuntime().availableProcessors();
			for(int i=0; i < processors; i++) {
				Thread t = new GrepThread(parts, content2, myList, i, processors);
				t.start();
			}
	   
			System.out.println("myList is " + myList.toString());
		
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		for(Wordcount this_wordcount : myList)
		{
			System.out.println("writing " + this_wordcount.word + " to file");
		    writer.println(this_wordcount.word + ";" + this_wordcount.count);
			System.out.println("wrote " + this_wordcount.count);
			
		}
		
		writer.close();
		
    } catch (IOException e) {
        e.printStackTrace();
    }
		
	
	}
	
}
	
class GrepThread extends Thread{
	String[] parts;
	String content2;
	List<Wordcount> myList;
	int thread_id, processors;
	
	public GrepThread(String[] parts, String content2, List<Wordcount> myList, int thread_id, int processors) {
	       this.parts = parts;
		   this.content2 = content2;
		   this.myList = myList;
		   this.thread_id = thread_id;
		   this.processors = processors;
	   }
	
	@Override public void run(){
		System.out.println("In GrepThread");
		System.out.println("Thread " + thread_id + " here!");
	        // get string to search for
	        // look for the string in buffer
			
			for (int i = 0; i < parts.length; i++){
				
				if (i%processors == thread_id){
				
				System.out.println("In GrepThread - part: " + parts[i]);
				Pattern pattern = Pattern.compile(parts[i]);
				Matcher matcher = pattern.matcher(content2);
				Wordcount this_wordcount = new Wordcount();
				this_wordcount.word = parts[i];
				this_wordcount.count = 0;
			
				while (matcher.find()) {
					this_wordcount.count++;
			    
				}
			
				myList.add(this_wordcount);
			
			}}
			
	        // write string to result list
	}

}

class Wordcount {
  public String word;
  public int count;
}