import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Driver application to incorporate all I/O and db functionality
 * @author Josh
 *
 */
public class Driver {
	
	//Variables for user input/ selection
	static Scanner s;
	static String[] inputCurriculum;
	static String nextYear;
	static int[] inputMarks;
	static String databaseType;
	
	static boolean passedInput;
	static boolean missingCourses;
	static boolean passedRequirements;
	
	static Database db;
	
	//Variables passed between queries
	static String curriculum;
	static String similarCourseStudents;
	static String similarMarkStudents;
	static String similarStudents;
	
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
	
	/**
	 * Main method to facilitate I/O, create a connection to the desired database and call methods to check constraints and predict grades
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {

		//Get User input in while loop to ensure correct format.
		s = new Scanner(System.in);
		passedInput = false;
		while(!passedInput) {
			if (nextYear== null) {
				System.out.println("Enter the which year you are registering for: 1, 2 or 3");
				System.out.print(">");
				nextYear = s.nextLine().toString();
				
				if (!nextYear.equals("1") && !nextYear.equals("2") && !nextYear.equals("3")) {
					System.out.print("Invalid year selection. Please reenter.");
					nextYear = null;
				}
			}
			if (nextYear != null && inputMarks == null) {
				if (nextYear.equals("1")) {
					System.out.println("Enter your NBT Academic Literacy, NBT Math and NBT Quantitative Literacy marks seperated by commas e.g. 65, 75, 50");	
					inputMarks = new int[3];
				}else if (nextYear.equals("2")){
					System.out.println("Enter your first year GPA (integer) e.g. 62");
					inputMarks = new int[1];
				}else {
					System.out.println("Enter your second year GPA (integer) e.g. 62");
					inputMarks = new int[1];
				}
				System.out.print(">");
				
				//try to convert String of input marks to an int[] array
				try {
					String[] temp = s.nextLine().replace(" ", "").split(",");
				    for (int i = 0; i < inputMarks.length; i++) {
				        inputMarks[i] = Integer.parseInt(temp[i]);
				    }
		        }catch (Exception e) {
			        System.out.print("Invalid input mark(s). Please reenter.");
			        inputMarks = null;
			    }
			}
			if (nextYear != null && inputMarks != null && inputCurriculum == null) {
				System.out.println("Enter your entire curriculum (list of all courses from year 1 to 3) e.g. CSC1015F, MAM1000W... ");
				System.out.print(">");
				inputCurriculum = s.nextLine().replace(" ", "").split(",");
				
				try {
					curriculum = formatCurriculum(inputCurriculum); //convert the format of the curriculum
				}catch (Exception e) {
			        System.out.print("Invalid curriculum input. Please reenter.");
			        inputCurriculum = null;
			    }
			}
			if (nextYear != null && inputMarks != null && inputCurriculum != null && databaseType == null) {
				System.out.println("Enter which database you want to use: Neo4j or MySQL");
				System.out.print(">");
				databaseType = s.nextLine();
				if (!databaseType.equals("Neo4j") && !databaseType.equals("MySQL") ) {
					System.out.print("Invalid database choice. Please reenter.");
					databaseType = null;
				}
			}
			if (nextYear != null && inputMarks != null && inputCurriculum != null && databaseType != null) {
				passedInput = true;
			}
		}
		
		//Create a link to the databases
		if(databaseType.equals("Neo4j")) {
			try {
				db = new Neo4jDatabase(nextYear, inputMarks);
			}catch (Exception e) {
				System.out.println("Error. Ensure the Neo4j database is running.");
			}
		}else {
			try {
				db = new MySQLDatabase(nextYear, inputMarks);
			}catch (Exception e) {
				System.out.println("Error connecting to MySQL database.");
			}
		}
		
		//Check Course Counts
		missingCourses = true;
		passedRequirements = false;
		
		System.out.println("Retrieving compulsory courses...\r\n");
		System.out.println("Checking compulsory course constraints...\r\n");
		missingCourses = db.getMissingCourses(curriculum);
		
		//if there are no missing courses, get and check course counts
		if(!missingCourses) {
			System.out.println("+------------------------------------------------------------+");
			System.out.println("| Curriculum meets all the course requirements of the Major. |");
			System.out.println("+------------------------------------------------------------+\r\n");
			System.out.println("Retrieving course counts...\r\n");
			
			String counts = db.getCourseCounts(curriculum);
			System.out.println("Checking course count constraints...\r\n");
			passedRequirements = db.checkCounts(counts);
		}
		
		//if course and count requirements met, find similar students and predict grade
		if(passedRequirements) {
	    	System.out.println("+--------------------------+");
	    	System.out.println("| Meets Count Requirements |");
	    	System.out.println("+--------------------------+\r\n");
	    	System.out.println("Finding similar students for grade prediction...\r\n");
	    	
	    	//get similar course students
			similarCourseStudents = db.getSimilarCourseStudents(curriculum, similarCourseCutoff(Arrays.toString(inputCurriculum)));
			
			//get similar mark students
			if(similarCourseStudents != null) {
				
				similarMarkStudents = db.getSimilarMarkStudents(similarCourseStudents);
			}
			
			//predict mark using similar mark students
			if(similarMarkStudents != null) {
				db.predictGrade(similarMarkStudents);
			}
		}else {
			System.out.println("+-----------------------------------+");
	    	System.out.println("| Does Not Meet Count Requirements. |");
	    	System.out.println("+-----------------------------------+\r\n");
		}
    	
		//Close the connection to the db
		db.close();
		
	}
}
