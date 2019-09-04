import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
	
	/** 
	 * Formats an input curriculum correctly for insertion into queries
	 * @param c the input curriculum array of form a, b, c
	 * @return the formatted curriculum for insertion into queries of the form \"a\", \"b\", \"c\"
	 */
	public static String formatCurriculum(String[] c) {
		String temp = "\"" +  Arrays.toString(c).replace("[", "").replace("]", "") + "\"";
		return temp.replace(",", "\",\"").replace(" ", "");
	}
	
	/**
	 * Calculates (size of curriculum)/2 for the 50% cutoff
	 * @param crs the String user input of planned courses
	 * @return 50% of the length of the courses
	 */
	public static String similarCourseCutoff (String crs) {
		String[] courses = crs.split(" ");
		ArrayList<String> lst = new ArrayList<String>(Arrays.asList(courses));
		int length = (int) lst.size()/2;
		return Integer.toString(length);
	}
	
	private static String fill(char ch, int len) {
	    StringBuilder sb = new StringBuilder(len);
	    for (int i = 0; i < len; i++) {
	        sb.append(ch);
	    }
	    return sb.toString();
	}
	
	public static void print(String[] in) {
		int maxLength = 0;
		for(String s: in) {
			if(s.length() > maxLength) {
				maxLength = s.length();
			}
		}
		System.out.println("+" + fill('-', maxLength + 2) + "+");
		for(String s : in) {
			System.out.println("| " + s + " " + fill(' ',maxLength - s.length())+"|");
		}
		System.out.println("+" + fill('-', maxLength + 2) + "+\r\n"); 
	}
}