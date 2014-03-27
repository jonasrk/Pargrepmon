import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

class Pargrepmon{

	public static void main(String[] args){
		
		System.out.println("Hello world!");
		
		List<String> tokens = new ArrayList<String>();
		
	    String content = null;
	    File file = new File(args[0]);
	    try {
	        FileReader reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        tokens.add(new String(chars));
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
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
		
		Iterator<String> iterator = tokens.iterator();
			while (iterator.hasNext()) {
				Pattern pattern = Pattern.compile(iterator.next());
				Matcher matcher = pattern.matcher(content2);
				
				while (matcher.find()) {
				    System.out.println(pattern + " +1");
				}
				
			}
		
	
	}

}