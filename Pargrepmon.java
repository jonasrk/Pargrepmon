import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

class Pargrepmon{

	public static void main(String[] args){
		
		System.out.println("Hello world!");
		
		//List<String> tokens = new ArrayList<String>();
		String[] parts = new String[4];
		
	    String content = null;
	    File file = new File(args[0]);
	    try {
	        FileReader reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        parts = (new String(chars)).split("\\s+");
			System.out.println("Current parts " + parts);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		System.out.println("Current parts " + parts);
		
	   String content2 = null;
	   File file2 = new File(args[1]);
	   try {
	       FileReader reader = new FileReader(file2);
	       char[] chars = new char[(int) file2.length()];
	       reader.read(chars);
	       content2 = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   
	   List<Wordcount> myList = new ArrayList<Wordcount>();
	   
	    for (int i = 0; i < parts.length; i++){
	   		Pattern pattern = Pattern.compile(parts[i]);
	   		Matcher matcher = pattern.matcher(content2);
	   		
	   		System.out.println("Current parts[i] " + parts[i]);
			Wordcount this_wordcount = new Wordcount();
			this_wordcount.word = parts[i];
			this_wordcount.count = 0;
	   		
	   		while (matcher.find()) {
				this_wordcount.count++;
	   		    System.out.println(pattern + " +1");
				
	   		}
			
			myList.add(this_wordcount);
	   		
	   	}
		
		
		System.out.println("My list is " + myList.toString());
		
		for(Wordcount this_wordcount : myList) //use for-each loop
		{
		    System.out.println(this_wordcount.word + ";" + this_wordcount.count);
		}
		
	
	}

}

class Wordcount {
  public String word;
  public int count;
}