import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class MySQLDatabase implements Database {
	
	//Objects to connect to the db
	Connection conn;
	Statement st;
	
	//Variables passed to queries
	String major1;
	String major2;
	String curriculum;
	
	String nextYear;
	static String currentYear;
	static int averageMark;
	static String LB;
	static String UB;
	static String query;
	static String inputMark;
	static String predictedMark;
	
	//Variables for similarity queries/ output
	static String minSimilarCourses;
	static String similarCourseStudents;
	static String similarCourseStudentsSize;
	static String similarMarkStudents;
	static String similarMarkStudentsSize;
	static String GPA;
	static String standardDeviation;
	
	//variables used for constraint checking
	static int s1y1 ;
	static int s2y1 ;
	static int firstYearHalfCourses ;
	static int s1y2 ;
	static int s2y2 ; 
	static int s1y3 ; 
	static int s2y3 ; 
	static int seniorYearHalfCourses ;
	static int halfCourses ;
	static int fullCourses ;
	static int sciHalfCourses;
	static int sciFullCourses;
	static int seniorHalfCourses; 
	static int seniorFullCourses ;
	static int seniorSciHalfCourses ;
	static int seniorSciFullCourses ;
	static int thirdHalfCourses ;
	static int thirdFullCourses ;
	static int totalNQF ; 
	static int sciNQF ; 
	static int thirdNQF ; 
	static int s0y1NQF ;
	static int s1y1NQF ;
	static int s2y1NQF ; 
	static int s0y2NQF ;
	static int s1y2NQF ;  
	static int s2y2NQF ; 
	static int s0y3NQF ;
	static int s1y3NQF ;  
	static int s2y3NQF ; 
	static int firstYearHalfCoursesNQF;
	static int seniorYearHalfCoursesNQF ;
	
	//Lists to store query results
	static ArrayList<String> missingMustCourses = new ArrayList<String>();
	static ArrayList<String> coursesNotEnrolledIn = new ArrayList<String>();
	static ArrayList<String> combinationsNotEnrolledIn = new ArrayList<String>();
	static ArrayList<String> coursesEnrolledIn = new ArrayList<String>();
	static ArrayList<String> combinationsEnrolledIn = new ArrayList<String>();
	
	
	/**
	 * Create a new connection to a MySQL database and set parameters for the corresponding queries
	 * @param nextYear The year the student is about to start, this affects the algorithms & nodes queried
	 * @param curriculum2 
	 * @param major2 
	 * @param major1 
	 * @param inputMarks The user input marks which affect the LB and UB parameters of queries
	 * @throws SQLException
	 */
	public MySQLDatabase(String nextYear, String major1, String major2, String curriculum, int[] inputMarks) throws SQLException {
		//access the db
		try {
			String myDriver = "com.mysql.cj.jdbc.Driver";
			String myUrl = "jdbc:mysql://localhost:3306/UCT Data";
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, "root", "password");
			st = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error connecting to SQL database.");
		}
		
		//calculate average grade, LB, UB
		this.curriculum = curriculum;
		this.nextYear = nextYear;
		averageMark = average(inputMarks);
		LB = Integer.toString(averageMark - 5);
		UB = Integer.toString(averageMark + 5);
		
		//Set query parameters affected by year
		if(nextYear.equals("1")) {
			currentYear = "0";
			query = "NBT";
			inputMark = "NBT";
			predictedMark = "first year GPA";
		}else if(nextYear.equals("2")) {
			currentYear = "1";
			query = "GPA";
			inputMark = "first year GPA";
			predictedMark = "second year GPA";
		}else if(nextYear.equals("3")) {
			currentYear = "2";
			query = "GPA";
			inputMark = "second year GPA";
			predictedMark = "third year GPA";
		}
		
		//initalize result lists
		missingMustCourses = new ArrayList<String>();
		coursesNotEnrolledIn = new ArrayList<String>();
		combinationsNotEnrolledIn = new ArrayList<String>();
		coursesEnrolledIn = new ArrayList<String>();
		combinationsEnrolledIn = new ArrayList<String>();
	}
	
	/**
	 * Function to calculate a student's average input mark
	 * @param list A list of marks
	 * @return The average of the list of marks
	 */
	public int average(int[] list) {
		int sum = 0;
		for(int m : list) {
			sum = sum + m;
		}
		return sum/list.length;
	}
	
	//retrieve course counts using input curriculum
	@Override
	public String getCourseCounts() {
		//define the queries
		String s1y1query = "select count(CourseID) as s1y1 from courses where courseName in ("+ curriculum +") and (Semester = 0 or Semester = 1) and courses.Year = 1 ;";
		String s2y1query = "select count(CourseID) as s2y1 from courses where courseName in ("+ curriculum +") and (Semester = 0 or Semester = 2) and courses.Year = 1 ;";
		String s1y2query = "select count(CourseID) as s1y2 from courses where courseName in ("+ curriculum +") and (Semester = 0 or Semester = 1) and courses.Year = 2 ;";
		String s2y2query = "select count(CourseID) as s2y2 from courses where courseName in ("+ curriculum +") and (Semester = 0 or Semester = 2) and courses.Year = 2 ;";
		String s1y3query = "select count(CourseID) as s1y3 from courses where courseName in ("+ curriculum +") and (Semester = 0 or Semester = 1) and courses.Year = 3 ;";
		String s2y3query = "select count(CourseID) as s2y3 from courses where courseName in ("+ curriculum +") and (Semester = 0 or Semester = 2) and courses.Year = 3 ;";
		String halfCoursesquery = "select count(CourseID) as halfCourses from courses where courseName in ("+ curriculum +") and (Semester = 1 or Semester = 2) ;";
		String fullCoursesquery = "select count(CourseID) as fullCourses from courses where courseName in ("+ curriculum +") and (Semester = 0 ) ;";
		String sciHalfCoursesquery = "select count(CourseID) as sciHalfCourses from courses where courseName in ("+ curriculum +") and Faculty = \"SCI\" and (Semester = 1 or Semester = 2) ;";
		String sciFullCoursesquery = "select count(CourseID) as sciFullCourses from courses where courseName in ("+ curriculum +") and Faculty = \"SCI\" and Semester = 0  ;";
		String seniorSciHalfCoursesquery = "select count(CourseID) as seniorSciHalfCourses from courses where courseName in ("+ curriculum +") and (Semester = 1 or Semester = 2) and Faculty = \"SCI\" and (courses.Year = 2 or courses.Year = 3) ;";
		String seniorSciFullCoursesquery = "select count(CourseID) as seniorSciFullCourses from courses where courseName in ("+ curriculum +") and (Semester = 0) and Faculty = \"SCI\" and (courses.Year = 2 or courses.Year = 3) ;";
		String thirdHalfCoursesquery = "select count(CourseID) as thirdHalfCourses from courses where courseName in ("+ curriculum +") and (Semester = 1 or Semester = 2) and (courses.Year = 3) ;";
		String thirdFullCoursesquery = "select count(CourseID) as thirdFullCourses from courses where courseName in ("+ curriculum +") and (Semester = 0) and (courses.Year = 3) ;";
		String seniorHalfCoursesquery = "select count(CourseID) as seniorHalfCourses from courses where courseName in ("+ curriculum +") and (Semester = 1 or Semester = 2) and (courses.Year = 2 or courses.Year = 3) ;";
		String seniorFullCoursesquery = "select count(CourseID) as seniorFullCourses from courses where courseName in ("+ curriculum +") and (Semester = 0) and (courses.Year = 2 or courses.Year = 3) ;";
		
		String totalNQFquery = "select coalesce(sum(Credits), 0) as totalNQF from courses where CourseName in ("+ curriculum +") ;";
		String sciNQFquery = "select coalesce(sum(Credits), 0) as sciNQF from courses where CourseName in ("+ curriculum +") and Faculty = \"SCI\";";
		String thirdNQFquery = "select coalesce(sum(Credits), 0) as thirdNQF from courses where CourseName in ("+ curriculum +") and courses.Year = 3 ;";

		String s0y1NQFquery = "select coalesce(sum(Credits), 0) as s0y1NQF from courses where courseName in ("+ curriculum +") and Semester = 0 and courses.Year = 1 ;";
		String s1y1NQFquery = "select coalesce(sum(Credits), 0) as s1y1NQF from courses where courseName in ("+ curriculum +") and Semester = 1 and courses.Year = 1 ;";
		String s2y1NQFquery = "select coalesce(sum(Credits), 0) as s2y1NQF from courses where courseName in ("+ curriculum +") and Semester = 2 and courses.Year = 1 ;";
		String s0y2NQFquery = "select coalesce(sum(Credits), 0) as s0y2NQF from courses where courseName in ("+ curriculum +") and Semester = 0 and courses.Year = 2 ;";
		String s1y2NQFquery = "select coalesce(sum(Credits), 0) as s1y2NQF from courses where courseName in ("+ curriculum +") and Semester = 1 and courses.Year = 2 ;";
		String s2y2NQFquery = "select coalesce(sum(Credits), 0) as s2y2NQF from courses where courseName in ("+ curriculum +") and Semester = 2 and courses.Year = 2 ;";
		String s0y3NQFquery = "select coalesce(sum(Credits), 0) as s0y3NQF from courses where courseName in ("+ curriculum +") and Semester = 0 and courses.Year = 3 ;";
		String s1y3NQFquery = "select coalesce(sum(Credits), 0) as s1y3NQF from courses where courseName in ("+ curriculum +") and Semester = 1 and courses.Year = 3 ;";
		String s2y3NQFquery = "select coalesce(sum(Credits), 0) as s2y3NQF from courses where courseName in ("+ curriculum +") and Semester = 2 and courses.Year = 3 ;";
		
		//run queries and retrieve results
		try {
			ResultSet s1y1Result = st.executeQuery(s1y1query);
		    while (s1y1Result.next()) {
		    	s1y1 = (s1y1Result.getInt("s1y1"));
		    }
		    ResultSet s2y1Result = st.executeQuery(s2y1query);
		    while (s2y1Result.next()) {
		    	s2y1 = (s2y1Result.getInt("s2y1"));
		    }
		    ResultSet s1y2Result = st.executeQuery(s1y2query);
		    while (s1y2Result.next()) {
		    	s1y2 = (s1y2Result.getInt("s1y2"));
		    }
		    ResultSet s2y2Result = st.executeQuery(s2y2query);
		    while (s2y2Result.next()) {
		    	s2y2 = (s2y2Result.getInt("s2y2"));
		    }
		    ResultSet s1y3Result = st.executeQuery(s1y3query);
		    while (s1y3Result.next()) {
		    	s1y3 = (s1y3Result.getInt("s1y3"));
		    }
		    ResultSet s2y3Result = st.executeQuery(s2y3query);
		    while (s2y3Result.next()) {
		    	s2y3 = (s2y3Result.getInt("s2y3"));
		    }
		    
		    ResultSet halfCoursesResult = st.executeQuery(halfCoursesquery);
		    while (halfCoursesResult.next()) {
		    	halfCourses = (halfCoursesResult.getInt("halfCourses"));
		    }
		    ResultSet fullCoursesResult = st.executeQuery(fullCoursesquery);
		    while (fullCoursesResult.next()) {
		    	fullCourses = (halfCourses/2) + (fullCoursesResult.getInt("fullCourses"));
		    }
		    
		    ResultSet sciHalfCoursesResult = st.executeQuery(sciHalfCoursesquery);
		    while (sciHalfCoursesResult.next()) {
		    	sciHalfCourses = (sciHalfCoursesResult.getInt("sciHalfCourses"));
		    }
		    ResultSet sciFullCoursesResult = st.executeQuery(sciFullCoursesquery);
		    while (sciFullCoursesResult.next()) {
		    	sciFullCourses = (sciHalfCourses/2) + (sciFullCoursesResult.getInt("sciFullCourses"));
		    }
		    
		    ResultSet seniorHalfCoursesResult = st.executeQuery(seniorHalfCoursesquery);
		    while (seniorHalfCoursesResult.next()) {
		    	seniorHalfCourses = (seniorHalfCoursesResult.getInt("seniorHalfCourses"));
		    }
		    ResultSet seniorFullCoursesResult = st.executeQuery(seniorFullCoursesquery);
		    while (seniorFullCoursesResult.next()) {
		    	seniorFullCourses = (seniorHalfCourses/2) + (seniorFullCoursesResult.getInt("seniorFullCourses"));
		    }
		    
		    ResultSet seniorSciHalfCoursesResult = st.executeQuery(seniorSciHalfCoursesquery);
		    while (seniorSciHalfCoursesResult.next()) {
		    	seniorSciHalfCourses = (seniorSciHalfCoursesResult.getInt("seniorSciHalfCourses"));
		    }
		    ResultSet seniorSciFullCoursesResult = st.executeQuery(seniorSciFullCoursesquery);
		    while (seniorSciFullCoursesResult.next()) {
		    	seniorSciFullCourses = (seniorSciHalfCourses/2) + (seniorSciFullCoursesResult.getInt("seniorSciFullCourses"));
		    }
		    
		    ResultSet thirdHalfCoursesResult = st.executeQuery(thirdHalfCoursesquery);
		    while (thirdHalfCoursesResult.next()) {
		    	thirdHalfCourses = (thirdHalfCoursesResult.getInt("thirdHalfCourses"));
		    }
		    ResultSet thirdFullCoursesResult = st.executeQuery(thirdFullCoursesquery);
		    while (thirdFullCoursesResult.next()) {
		    	thirdFullCourses = (thirdHalfCourses/2) + (thirdFullCoursesResult.getInt("thirdFullCourses"));
		    }
		    
		    // NQF COUNTS
		    ResultSet totalNQFResult = st.executeQuery(totalNQFquery);
		    while (totalNQFResult.next()) {
		    	totalNQF =  (totalNQFResult.getInt("totalNQF"));
		    }
		    ResultSet sciNQFResult = st.executeQuery(sciNQFquery);
		    while (sciNQFResult.next()) {
		    	sciNQF =  (sciNQFResult.getInt("sciNQF"));
		    }
		    ResultSet thirdNQFResult = st.executeQuery(thirdNQFquery);
		    while (thirdNQFResult.next()) {
		    	thirdNQF =  (thirdNQFResult.getInt("thirdNQF"));
		    }
		    
		    ResultSet s0y1NQFResult = st.executeQuery(s0y1NQFquery);
		    while (s0y1NQFResult.next()) {
		    	s0y1NQF = (s0y1NQFResult.getInt("s0y1NQF"));
		    }
		    ResultSet s1y1NQFResult = st.executeQuery(s1y1NQFquery);
		    while (s1y1NQFResult.next()) {
		    	s1y1NQF = (s0y1NQF/2) + (s1y1NQFResult.getInt("s1y1NQF"));
		    }
		    ResultSet s2y1NQFResult = st.executeQuery(s2y1NQFquery);
		    while (s2y1NQFResult.next()) {
		    	s2y1NQF = (s0y1NQF/2) + (s2y1NQFResult.getInt("s2y1NQF"));
		    }
		    ResultSet s0y2NQFResult = st.executeQuery(s0y2NQFquery);
		    while (s0y2NQFResult.next()) {
		    	s0y2NQF = (s0y2NQFResult.getInt("s0y2NQF"));
		    }
		    ResultSet s1y2NQFResult = st.executeQuery(s1y2NQFquery);
		    while (s1y2NQFResult.next()) {
		    	s1y2NQF = (s0y2NQF/2) + (s1y2NQFResult.getInt("s1y2NQF"));
		    }
		    ResultSet s2y2NQFResult = st.executeQuery(s2y2NQFquery);
		    while (s2y2NQFResult.next()) {
		    	s2y2NQF = (s0y2NQF/2) + (s2y2NQFResult.getInt("s2y2NQF"));
		    }
		    ResultSet s0y3NQFResult = st.executeQuery(s0y3NQFquery);
		    while (s0y3NQFResult.next()) {
		    	s0y3NQF = (s0y3NQFResult.getInt("s0y3NQF"));
		    }
		    ResultSet s1y3NQFResult = st.executeQuery(s1y3NQFquery);
		    while (s1y3NQFResult.next()) {
		    	s1y3NQF = (s0y3NQF/2) + (s1y3NQFResult.getInt("s1y3NQF"));
		    }
		    ResultSet s2y3NQFResult = st.executeQuery(s2y3NQFquery);
		    while (s2y3NQFResult.next()) {
		    	s2y3NQF = (s0y3NQF/2) + (s2y3NQFResult.getInt("s2y3NQF"));
		    }
		    return "got counts";
		}catch (Exception e) {
			System.out.println("Error retrieving SQL course counts.");
			return "No Counts";
		}
	}
	
	//return true if any courses are missing from the curriculum
	@Override
	public boolean getMissingCourses() {
		//define the queries
		String missingMustCoursesquery = "select c.CourseName as missingMustCourses \r\n" + 
				"from courses c \r\n" + 
				"right join requires r on r.CourseID = c.CourseID and Combination = 0 \r\n" + 
				"and MajorID = \"CSC05\" \r\n" + 
				"where c.CourseName is not null and c.CourseName not in ("+ curriculum +");";
		String coursesNotEnrolledInquery = "select c.CourseName as coursesNotEnrolledIn, r.Combination as combinationsNotEnrolledIn\r\n" + 
				"from courses c \r\n" + 
				"right join requires r on r.CourseID = c.CourseID and Combination <> 0 \r\n" + 
				"and MajorID = \"CSC05\" \r\n" + 
				"where c.CourseName is not null and c.CourseName not in ("+ curriculum +");";
		String coursesEnrolledInquery = "select c.CourseName as coursesEnrolledIn, r.Combination as combinationsEnrolledIn\r\n" + 
				"from courses c \r\n" + 
				"right join requires r on r.CourseID = c.CourseID and Combination <> 0 \r\n" + 
				"and MajorID = \"CSC05\" \r\n" + 
				"where c.CourseName is not null and c.CourseName in ("+ curriculum +");";
		
		//run the Course queries
		try {
			
		    ResultSet missingMustCoursesResult = st.executeQuery(missingMustCoursesquery);
		    while (missingMustCoursesResult.next()) {
		    	missingMustCourses.add(missingMustCoursesResult.getString("missingMustCourses")); 
		    }
		    ResultSet coursesNotEnrolledInResult = st.executeQuery(coursesNotEnrolledInquery);
		    while (coursesNotEnrolledInResult.next()) {
		    	coursesNotEnrolledIn.add(coursesNotEnrolledInResult.getString("coursesNotEnrolledIn")); 
		    	combinationsNotEnrolledIn.add(coursesNotEnrolledInResult.getString("combinationsNotEnrolledIn")); 
		    }
		    ResultSet coursesEnrolledInResult = st.executeQuery(coursesEnrolledInquery);
		    while (coursesEnrolledInResult.next()) {
		    	coursesEnrolledIn.add(coursesEnrolledInResult.getString("coursesEnrolledIn")); 
		    	combinationsEnrolledIn.add(coursesEnrolledInResult.getString("combinationsEnrolledIn")); 
		    }
		} catch (Exception e) {
			System.out.println("Error retrieving SQL missing courses.");
			return true;
		}
		
		//check that compulsory courses are taken and at least some optional courses z
	    if (!missingMustCourses.isEmpty()) {
	    	System.out.println("Missing the following course(s): "+ missingMustCourses.toString());
	    	return true;
	    }if (combinationsEnrolledIn.isEmpty()) {
	    	System.out.println("You must enroll in one or more of the following course(s): "+ coursesNotEnrolledIn.toString());
	    	return true;
	    }
	    
	    // Check that all optional courses are taken in the correct combination
	    Collection<String> takenCombinations = combinationsEnrolledIn; // e.g. [1,2] taken courses from combinations 1 & 2
    	Collection<String> missingCombinations = combinationsNotEnrolledIn; // e.g. [1] missing courses from combinations 1
    	takenCombinations.retainAll(missingCombinations); // e.g. [1] intersection of two sets = 1, so there is a component missing 

	    if (takenCombinations.size() > 0) {
	    	String notEnrolledIn = (String) takenCombinations.toArray()[0];
	    	int indexOfCourse = combinationsNotEnrolledIn.indexOf(notEnrolledIn);
	    	String missingCourseCombination = coursesNotEnrolledIn.get(indexOfCourse);
	    	System.out.println("Missing the other course component: "+ missingCourseCombination);
	    	return true;
	    }
	    return false;
	}
	
	//return true if the counts pass all if-statements
	@Override
	public boolean checkCounts(String counts) {
    	System.out.println("+------------------------------------+");
    	if (fullCourses < 9) {
    		System.out.println("| Too few Full Courses");
    		return false;
    	}else {
    		System.out.println("| Enough Full Courses");
    	}
    	if (sciFullCourses < 6) {
    		System.out.println("| Too few Science Full Courses");
    		return false;
    	}else {
    		System.out.println("| Enough Science Full Courses");
    	}
    	if (seniorFullCourses < 4) {
    		System.out.println("| Too few Senior Full Courses");
    		return false;
    	}else {
    		System.out.println("| Enough Senior Full Courses");
    	}
    	if (seniorSciFullCourses < 3) {
    		System.out.println("| Too few Senior Science Full Courses");
    		return false;
    	}else {
    		System.out.println("| Enough Senior Science Full Courses");
    	}
    	if (thirdFullCourses < 2) {
    		System.out.println("| Too few Third Year Full Courses");
    		return false;
    	}else {
    		System.out.println("| Enough Third Year Full Courses");
    	}
    	if (s1y1 > 4 || s2y1 > 4) {
    		if (s1y1NQF > 72 || s2y1NQF > 72) {
    			System.out.println("| Too many First Year Half Courses");
    			return false;
    		}
    		System.out.println("| Enough First Year Half Courses");
    	}else {
    		System.out.println("| Enough First Year Half Courses");
    	}
    	if (s1y2 > 3 || s2y2 > 3 || s1y3 > 3 || s2y3 > 3) {
    		if (s1y2NQF > 72 || s2y2NQF > 72 || s1y3NQF > 72 || s2y3NQF > 72) {
    			System.out.println("| Too many Senior Year Half Courses");
    			return false;
    		}
    		System.out.println("| Enough Senior Year Half Courses");
    	}else {
    		System.out.println("| Enough Senior Year Half Courses");
    	}
    	if (totalNQF < 420) {
    		System.out.println("| Too few NQF credits");
    		return false;
    	}else {
    		System.out.println("| Enough NQF Credits");
    	}
    	if (sciNQF < 276) {
    		System.out.println("| Too few Science NQF credits");
    		return false;
    	}else {
    		System.out.println("| Enough Science NQF Credits");
    	}
    	if (thirdNQF < 120) {
    		System.out.println("| Too few Third Year NQF credits");
    		return false;
    	}else {
    		System.out.println("| Enough Third Year NQF Credits");
    	}
    	System.out.println("+------------------------------------+\r\n");
    	return true;
	}
	
	//retrieve list of students who have the same courses
	@Override
	public String getSimilarCourseStudents(String minSimilarCourses) {
		//define query to find students with 50% of the same courses
		String similarCourseQuery = 
				"select StudentID  from coursemarks cm \r\n" + 
				"inner join courses c on c.CourseID = cm.CourseID\r\n" + 
				"where c.CourseName in ("+ curriculum +")\r\n" + 
				"group by StudentID\r\n" + 
				"having count(StudentID) > "+ minSimilarCourses +";";
		try {
			// Find similar students which have taken at least 50% of the student's proposed courses
			
			// execute the query, and get a java resultset
		    ResultSet similarCourseResult = st.executeQuery(similarCourseQuery);
	        // iterate through the java resultset
		    String temp = "";
		    int count = 0;
		    while (similarCourseResult.next()) {
		    	temp = temp + similarCourseResult.getString("StudentID") + ",";
		    	count++;
		    }
		    
		    similarCourseStudents = temp.substring(0, temp.length() -1);
		    similarCourseStudentsSize = Integer.toString(count);

		    System.out.println("+----------------------------------------------------------------------------+");
			System.out.println("| Found " + similarCourseStudentsSize + " past students who enrolled in similar courses to your curriculum. |");
			System.out.println("+----------------------------------------------------------------------------+\r\n");
			return similarCourseStudents;
		}catch (Exception e) {
			System.out.println("Error finding SQL similar course students.");
			return null;
		}
	}

	//retrieve list of students who have the same marks
	@Override
	public String getSimilarMarkStudents(String similarCourseStudents) {
		// Using students from above, find the students with NBT marks in similar range to student's input mark
		String similarNBTMarkQuery = 
				"Select StudentID, Mark, SubjectID from subjectmarks sm\r\n" + 
				"where StudentID in ("+ similarCourseStudents +")\r\n" + 
				"and sm.SubjectID in (4,5,6)\r\n" + 
				"group by (StudentID)\r\n" + 
				"having avg(Mark) > "+ LB +" and avg(Mark) < "+ UB +";";
					      
		// Using students from above, find the students with average GPA marks in similar range to student's input mark
		String similarGPAQuery = 
				"Select StudentID from coursemarks cm \r\n" + 
				"right join Courses c on c.CourseID = cm.CourseID\r\n" + 
				"where StudentID in ("+ similarCourseStudents +")\r\n" + 
				"and c.Year = "+ currentYear +"\r\n" + 
				"group by (studentID) \r\n" + 
				"having avg(cm.Mark) > "+ LB +" and avg(cm.Mark) < "+ UB +";";
		try {
			ResultSet similarMarkResult;

			if(currentYear.equals("0")) {
				similarMarkResult = st.executeQuery(similarNBTMarkQuery);
			}else {
				similarMarkResult = st.executeQuery(similarGPAQuery);
			}
			
		    // iterate through the java resultset
		    String temp = "";
		    int count = 0;
		    while (similarMarkResult.next()) {
		    	temp = temp + similarMarkResult.getString("StudentID") + ",";
		    	count++;
		    }

		    similarMarkStudents = temp.substring(0, temp.length() -1);
		    similarMarkStudentsSize = Integer.toString(count);
		    
		    System.out.println("+------------------------------------------------------------------------------------------------------------+");
			System.out.println("| Of " + similarCourseStudentsSize + " students enrolled in similar courses, " + similarMarkStudentsSize + " students achieved a " + inputMark + " mark similar to you ("+ averageMark +").");
			System.out.println("+------------------------------------------------------------------------------------------------------------+\r\n");
			return similarMarkStudents;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error finding SQL similar mark students.");
			System.out.println(similarGPAQuery);
			return null;
		}
	}

	//retrieve GPA of similar students
	@Override
	public void predictGrade(String similarMarkStudents) {
		// Using the filtered list of students (similar course & marks), output their predicted next year performance
		String predictedMarkQuery = 
				"Select avg(Mark) as GPA, stddev(Mark) as standardDeviation from coursemarks cm \r\n" + 
				"right join Courses c on c.CourseID = cm.CourseID\r\n" + 
				"where StudentID in ("+ similarMarkStudents +")\r\n" + 
				"and c.Year = "+ this.nextYear +" ;";
		try {
			ResultSet predictedMarkResult = st.executeQuery(predictedMarkQuery);
			
		    // iterate through the java resultset
		    while (predictedMarkResult.next()) {
		    	GPA = predictedMarkResult.getString("GPA").substring(0, 2);
		    	standardDeviation = predictedMarkResult.getString("standardDeviation").substring(0, 2);
		    }
			
		    System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------+");
		    System.out.println("| The average "+ predictedMark +" of " + similarMarkStudentsSize + " students with a "+ inputMark +" and curriculum similar to you is " + GPA + " with a standard deviation of " + standardDeviation);
			System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------+");
			}catch (Exception e) {
			System.out.println("Error predicting SQL grade.");
		}
	}
	
	//close the db connection
	@Override
	public void close() throws SQLException {
		st.close();
	}

}
