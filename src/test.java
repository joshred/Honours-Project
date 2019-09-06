import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
	
	public static void checkPrerequisites(String courses) {
		
		ArrayList<String> userCourses = new ArrayList<String> (Arrays.asList(courses.replace("\"", "").replace(" ", "").split(",")));
		ArrayList<String> cscCourses = new ArrayList<String>();
		for (int i = 0; i < userCourses.size(); i ++) {
			if(userCourses.get(i).startsWith("CSC")) {
				cscCourses.add(userCourses.get(i));
			}
		}
		System.out.println(cscCourses);
		//System.out.println(userCourses);
		String[] prereqArray = {"CSC1015F", "CSC1016S", "CSC2001F", "CSC2002S", "CSC2003S", "CSC3002F", "CSC3003S"};
		ArrayList<String> prereq = new ArrayList<String>( Arrays.asList(prereqArray));
		if (!prereq.equals(cscCourses)){
			System.out.println( "Pre-requisitie courses taken out of order.\r\n");
		}else {
			System.out.println( "Fine");

		}
		System.out.println(cscCourses);
		System.out.println(prereq);
	}

	public static void main(String[] args) {
//		int[] marks = new int[1];
//		marks[0] = 65;
//		String curric = "\"CSC1015F\", \"CSC1016S\", \"INF2009F\", \"CSC2001F\", \"CSC2002S\", \"CSC2003S\", \"CSC3002F\", \"CSC3003S\", \"MAM1000W\", \"MAM1019H\", \"MAM2000W\", \"MAM3000W\"";;
//		BigNeo4jDatabase db = new BigNeo4jDatabase("2", "CSC", "MAM", curric, marks);
//		String crs = db.getSimilarCourseStudents("");
//		String mark = db.getSimilarMarkStudents(crs);
//		db.predictGrade(mark);
//		db.close();
		checkPrerequisites("\"ACC1006F\", \"CSC1015F\", \"MAM1000W\", \"ECO1010F\", \"STA1006S\", \"CSC1016S\", \"ECO1011S\", \"CSC2001F\", \"INF2009F\", \"INF2006F\", \"STA2020F\", \"STA2030S\",\"CSC2002S\", \"CSC2003S\", \"CSC3002F\", \"STA3030F\", \"CSC3003S\", \"STA3036S\"");
		
	}

}
