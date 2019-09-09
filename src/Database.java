import java.sql.SQLException;

/**
 * The interface to represent all the methods a database object would require
 * @author Josh
 *
 */
public interface Database {
	
	/**
	 * Checks that the CSC courses taken by a student are taken in the correct order
	 * @param courses A String of user input courses
	 */
	public void checkPrerequisites(String courses);
	
	/**
	 * Retrieves a list/ dictionary of counts of specific types of courses e.g. number of full courses
	 * @param curriculum The list of courses the counts are queried from
	 * @return A list of counts to be checked against their limits
	 */
	public String getCourseCounts();

	/**
	 * Determines if the curriculum is missing any alternate or compulsory courses
	 * @param curriculum The list of courses to be checked
	 * @return true if any courses are missing, false otherwise
	 */
	public boolean getMissingCourses();
	
	/**
	 * Determines if any course counts violate their limits
	 * @param counts a dictionary of counts to be checked/ passed to Constraint Checking tree
	 * @return true if all counts are in order, false otherwise
	 */
	public boolean checkCounts(String counts);

	/**
	 * Retrieves a list of student numbers of students who have at least 50% the same courses in common as the user
	 * @param curriculum The list of courses to find courses in common
	 * @param minSimilarCourses The minimum number of courses to have in common (50% of input curriculum)
	 * @return A list of students who are similar according to courses
	 */
	public String getSimilarCourseStudents(String minSimilarCourses);

	/**
	 * Retrieves a list of student numbers of students who have similar marks as the user input marks
	 * @param similarCourseStudents A list of students within which to find students who are similar according to marks
	 * @return A list of students who are similar according to courses and marks
	 */
	public String getSimilarMarkStudents(String similarCourseStudents);

	/**
	 * Determines the average GPA and std deviation of marks of a list of students
	 * @param similarMarkStudents The list of students used to find average GPA 
	 */
	public void predictGrade(String similarMarkStudents);
	
	/**
	 * Gets the database "report" i.e. any feedback from constraint checks & grade prediction
	 * @return
	 */
	public String getReport();
	
	/**
	 * Closes any connections to any database
	 * @throws SQLException
	 */
	public void close() throws SQLException;

}
