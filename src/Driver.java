import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Driver application to incorporate all I/O and db functionality
 * 
 * @author Josh
 *
 */
public class Driver {

	// Variables for user input/ selection
	static Scanner s;
	static String[] inputCurriculum;
	static String[] inputMajor;
	static String major1, major2;
	static String nextYear;
	static int[] inputMarks;
	static String databaseType;
	static String choice;

	static boolean passedInput;
	static boolean missingCourses;
	static boolean passedRequirements;

	static Database db;

	// Variables passed between queries
	static String curriculum;
	static String similarCourseStudents;
	static String similarMarkStudents;
	static String similarStudents;

	/**
	 * Get user input in while loop to ensure correct format
	 */
	public static void getInput() {
		s = new Scanner(System.in);
		passedInput = false;
		while (!passedInput) {
			if (nextYear == null) {
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
					System.out.println(
							"Enter your NBT Academic Literacy, NBT Math and NBT Quantitative Literacy marks seperated by commas e.g. 65, 75, 50");
					inputMarks = new int[3];
				} else if (nextYear.equals("2")) {
					System.out.println("Enter your first year GPA (integer) e.g. 62");
					inputMarks = new int[1];
				} else {
					System.out.println("Enter your second year GPA (integer) e.g. 62");
					inputMarks = new int[1];
				}
				System.out.print(">");

				// try to convert String of input marks to an int[] array
				try {
					String[] temp = s.nextLine().replace(" ", "").split(",");
					for (int i = 0; i < inputMarks.length; i++) {
						inputMarks[i] = Integer.parseInt(temp[i]);
					}
				} catch (Exception e) {
					System.out.print("Invalid input mark(s). Please reenter.");
					inputMarks = null;
				}
			}
			if (nextYear != null && inputMarks != null && inputMajor == null) {
				System.out.println("Enter your the departments of your first and second major e.g. CSC, MAM ");
				System.out.print(">");

				try {
					inputMajor = s.nextLine().replace(" ", "").split(","); // convert to array
					major1 = inputMajor[0];
					major2 = inputMajor[1];
				} catch (Exception e) {
					System.out.print("Invalid major input. Please reenter.");
					inputMajor = null;
				}
			}
			if (nextYear != null && inputMarks != null && inputMajor != null && inputCurriculum == null) {
				System.out.println(
						"Enter your entire curriculum (list of all courses from year 1 to 3) e.g. CSC1015F, MAM1000W... ");
				System.out.print(">");
				inputCurriculum = s.nextLine().replace(" ", "").split(",");

				try {
					curriculum = Utils.formatCurriculum(inputCurriculum); // convert the format of the curriculum
				} catch (Exception e) {
					System.out.print("Invalid curriculum input. Please reenter.");
					inputCurriculum = null;
				}
			}
			if (nextYear != null && inputMarks != null && inputMajor != null && inputCurriculum != null
					&& databaseType == null) {
				System.out.println("Enter which database you want to use: Neo4j or MySQL");
				System.out.print(">");
				databaseType = s.nextLine();
				if (!databaseType.equals("Neo4j") && !databaseType.equals("MySQL")) {
					System.out.print("Invalid database choice. Please reenter.");
					databaseType = null;
				} else if (databaseType.equals("Neo4j")) {
					System.out.println("Select a database size: Small or Large");
					System.out.println("**The Small db uses real data and Large uses simulated data**");
					System.out.print(">");
					databaseType = databaseType + s.nextLine();
					if (!databaseType.startsWith("Neo4jS") && !databaseType.startsWith("Neo4js")
							&& !databaseType.startsWith("Neo4jL") && !databaseType.startsWith("Neo4jl")) {
						System.out.print("Invalid Neo4j database choice. Please reenter.");
						databaseType = null;
					}
				}
			}
			if (nextYear != null && inputMarks != null && inputMajor != null && inputCurriculum != null
					&& databaseType != null && choice == null) {
				System.out.println("Select your service: 1,2 or 3");
				System.out.println(" (1) Curriculum constraint checking (SCI)");
				System.out.println(" (2) Next year grade prediction");
				System.out.println(" (3) Curriculum constraint checking & Grade prediction");
				System.out.print(">");
				choice = s.nextLine();
				if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
					System.out.print("Invalid service choice. Please reenter.");
					choice = null;
				}
			}
			if (nextYear != null && inputMarks != null && inputMajor != null && inputCurriculum != null
					&& databaseType != null && choice != null) {
				passedInput = true;
			}
		}
	}

	/**
	 * Create a link to the specific database chosen by the user
	 */
	public static void createDatabase() {
		// Create a link to the databases
		if (databaseType.startsWith("Neo4jS") || databaseType.startsWith("Neo4js")) {
			try {
				db = new Neo4jDatabase(nextYear, major1, major2, curriculum, inputMarks);
			} catch (Exception e) {
				System.out.println("Error. Ensure the Neo4j database is running.");
			}
		} else if (databaseType.startsWith("Neo4jL") || databaseType.startsWith("Neo4jl")) {
			try {
				System.out.println("Ensure the correct Neo4j database is running.");
				db = new BigNeo4jDatabase(nextYear, major1, major2, curriculum, inputMarks);
			} catch (Exception e) {
				System.out.println("Error. Ensure the Neo4j database is running.");
			}
		} else {
			try {
				db = new MySQLDatabase(nextYear, major1, major2, curriculum, inputMarks);
			} catch (Exception e) {
				System.out.println("Error connecting to MySQL database.");
			}
		}
	}
	
	/**
	 * Method to find any missing courses and check course counts
	 */
 	public static void checkConstraints() {
		// Check Course Counts
		missingCourses = true;
		passedRequirements = false;

		System.out.println("Retrieving compulsory courses...\r\n");
		System.out.println("Checking compulsory course constraints...\r\n");

		// determine if any core/ alternate courses are missing
		missingCourses = db.getMissingCourses();

		// if there are no missing courses, get and check course counts
		if (!missingCourses) {
			Utils.print(new String[] { "Curriculum meets all the course requirements of the Major." });
			System.out.println("Retrieving course counts...\r\n");

			String counts = db.getCourseCounts();
			System.out.println("Checking course count constraints...\r\n");
			passedRequirements = db.checkCounts(counts);
		}

		// if course and count requirements met
		if (passedRequirements) {
			Utils.print(new String[] { "Meets Count Requirements." });
		} else {
			Utils.print(new String[] { "Does Not Meet Count Requirements." });
		}
	}

	/**
	 * Method which calls methods to find similar students to predict performance
	 * (grade)
	 */
	public static void predictGrade() {
		System.out.println("Finding similar students for grade prediction...\r\n");

		// get similar course students
		String similarCutOff = Utils.similarCourseCutoff(Arrays.toString(inputCurriculum)); // find 50% of the length of
																							// the input curriculum
		similarCourseStudents = db.getSimilarCourseStudents(similarCutOff);

		// get similar mark students
		if (similarCourseStudents != null) {
			similarMarkStudents = db.getSimilarMarkStudents(similarCourseStudents);
		}

		// predict mark using similar mark students
		if (similarMarkStudents != null) {
			db.predictGrade(similarMarkStudents);
		}
	}
		
	public Driver(String[] inputCurriculum, String major1, String major2, String nextYear, int[] inputMarks, String databaseType, String choice) {
		Driver.inputCurriculum = inputCurriculum; 
		Driver.major1 = major1;
		Driver.major2 = major2;
		Driver.nextYear = nextYear;
		Driver.inputMarks = inputMarks;
		Driver.databaseType = databaseType;
		Driver.choice = choice;
	}
	
	public void start() throws SQLException {
		createDatabase();

		//call the correct methods depending on the user's choice
		if (choice.equals("1")) {
			checkConstraints();
		} else if (choice.equals("2")) {
			predictGrade();
		} else if (choice.equals("3")) {
			checkConstraints();
			predictGrade();
		}

		// Close the connection to the db
		db.close();
	}
	
	/**
	 * Main method to facilitate I/O, create a connection to the desired database
	 * and call methods to check constraints and predict grades
	 * 
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		
		getInput();
		createDatabase();

		//call the correct methods depending on the user's choice
		if (choice.equals("1")) {
			checkConstraints();
		} else if (choice.equals("2")) {
			predictGrade();
		} else if (choice.equals("3")) {
			checkConstraints();
			predictGrade();
		}

		// Close the connection to the db
		db.close();
	}
}
