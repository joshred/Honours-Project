import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {
	
	static String NBTInputStatement = "Enter your NBT Academic Literacy, NBT Math and NBT Quantitative Literacy marks seperated by commas e.g. 65, 75, 50";
	static String firstGPAInputStatement = "Enter your first year GPA (integer) e.g. 62";
	static String secondGPAInputStatement = "Enter your second year GPA (integer) e.g. 62";

	
	static String[] inputCurriculum;
	static String nextYear;
	static int[] inputMarks;
	static String databaseType;
	
	static boolean passedInput;
	
	static String curriculum;
	
	public static String formatCurriculum(String[] c) {
		String temp = "\"" +  Arrays.toString(c).replace("[", "").replace("]", "") + "\"";
		return temp.replace(",", "\",\"").replace(" ", "");
	}
	
	public static void main(String[] args) {

		//Get User input
		Scanner s = new Scanner(System.in);
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
					System.out.println(NBTInputStatement);	
					inputMarks = new int[3];
				}else if (nextYear.equals("2")){
					System.out.println(firstGPAInputStatement);
					inputMarks = new int[1];
				}else {
					System.out.println(secondGPAInputStatement);
					inputMarks = new int[1];
				}
				System.out.print(">");
				
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
					curriculum = formatCurriculum(inputCurriculum);
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
				System.out.println(nextYear);
				System.out.println(Arrays.toString(inputMarks));
				System.out.println(curriculum);
				System.out.println(databaseType);
			}
		}
		
		//Check Constraints
		if(databaseType.equals("Neo4j")) {
			Neo4jDatabase db = new Neo4jDatabase(nextYear);
			
		}
		
		
	}
}
