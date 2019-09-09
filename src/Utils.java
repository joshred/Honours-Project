import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utils class which stores methods which manipulate input and output
 * @author Josh
 *
 */
public class Utils {
	
	/**
	 * Function to convert a Neo4j list to an Array representation
	 * @param in A Neo4j query result string
	 * @return The array version of a Neo4j list
	 */
	public static ArrayList<String> toList(String in){
		String withoutBrackets = in.replace("[", "").replace("]", "").replace(" ", ""); // remove brackets and whitespace
		ArrayList<String> newList = new ArrayList<String>(Arrays.asList(withoutBrackets.split(",")));
		newList.remove(new String(""));
		return newList;
	}
	
	/** 
	 * Formats a String[] array input curriculum correctly for insertion into queries
	 * @param c the input curriculum array of form a, b, c
	 * @return the formatted curriculum for insertion into queries of the form \"a\", \"b\", \"c\"
	 */
	public static String formatCurriculum(String[] c) {
		String temp = "\"" +  Arrays.toString(c).replace("[", "").replace("]", "") + "\"";
		return temp.replace(",", "\",\"").replace(" ", "");
	}
	
	/** 
	 * Formats an ArrayList<String> input curriculum correctly for insertion into queries
	 * @param c the input curriculum array of form a, b, c
	 * @return the formatted curriculum for insertion into queries of the form \"a\", \"b\", \"c\"
	 */
	public static String formatCurriculum(ArrayList<String> c) {
		String temp = "\"" +  c.toString().replace("[", "").replace("]", "") + "\"";
		return temp.replace(",", "\",\"").replace(" ", "");
	}
	
	/**
	 * Calculates (size of curriculum)/2 for the 50% cutoff
	 * @param crs the String user input of planned courses
	 * @return 50% of the length of the courses
	 */
	public static String similarCourseCutoff (String crs) {
		String[] courses = crs.replace(" ", "").split(",");
		ArrayList<String> lst = new ArrayList<String>(Arrays.asList(courses));
		int length = (int) lst.size()/2;
		return Integer.toString(length);
	}
	
	/**
	 * Function to calculate a student's average input mark
	 * @param list A list of marks
	 * @return The average of the list of marks
	 */
	public static int average(int[] list) {
		int sum = 0;
		for(int m : list) {
			sum = sum + m;
		}
		return sum/list.length;
	}
	
	/**
	 * Generates a string of a repeated character for a desired length 
	 * @param ch the character to be repeated
	 * @param len the number of times the character is to be repeated
	 * @return a string of a character repeated len times
	 */
	private static String fill(char ch, int len) {
	    StringBuilder sb = new StringBuilder(len);
	    for (int i = 0; i < len; i++) {
	        sb.append(ch);
	    }
	    return sb.toString();
	}
	
	/**
	 * Prints the contents of a String array contained in a neat box e.g.
	 * +------+
	 * | Text.|
	 * +------+
	 * @param in the array to print in a box
	 */
	public static String print(String[] in) {
		String output = "";
		int maxLength = 0;
		for(String s: in) {
			if(s.length() > maxLength) {
				maxLength = s.length();
			}
		}
		System.out.println("+" + fill('-', maxLength + 2) + "+");
		output += "+" + fill('-', maxLength + 2) + "+\r\n";
		for(String s : in) {
			System.out.println("| " + s + " " + fill(' ',maxLength - s.length())+"|");
			output += "| " + s + " " + fill(' ',maxLength - s.length())+"|\r\n";
		}
		System.out.println("+" + fill('-', maxLength + 2) + "+\r\n"); 
		output += "+" + fill('-', maxLength + 2) + "+\r\n";
		return output;
	}
}
