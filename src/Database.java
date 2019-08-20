
public interface Database {
	public String getCourseCounts(String curriculum);

	public boolean getMissingCourses(String curriculum);
	
	public boolean traverseTree(String counts);

	public String getSimilarCourseStudents(String curriculum, String minSimilarCourses);

	public String getSimilarMarkStudents(String similarCourseStudents);

	public void predictGrade(String similarMarkStudents);
	
	public void close();

}
