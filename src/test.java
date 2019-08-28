
public class test {

	public static void main(String[] args) {
		int[] marks = new int[1];
		marks[0] = 65;
		String curric = "\"CSC1015F\", \"CSC1016S\", \"INF2009F\", \"CSC2001F\", \"CSC2002S\", \"CSC2003S\", \"CSC3002F\", \"CSC3003S\", \"MAM1000W\", \"MAM1019H\", \"MAM2000W\", \"MAM3000W\"";;
		BigNeo4jDatabase db = new BigNeo4jDatabase("2", "CSC", "MAM", curric, marks);
		String crs = db.getSimilarCourseStudents("");
		String mark = db.getSimilarMarkStudents(crs);
		db.predictGrade(mark);
		db.close();
	}

}
