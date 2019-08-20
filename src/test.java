
public class test {

	public static void main(String[] args) {
		int[] marks = new int[1];
		marks[0] = 65;
		Neo4jDatabase db = new Neo4jDatabase("2", marks);
		db.getCourseCounts("\"ACC1006F\", \"CSC1015F\", \"MAM1000W\", \"ECO1010F\", \"STA1006S\", "
				+ "\"CSC1016S\", \"ECO1011S\", \"CSC2001F\", \"INF2009F\", \"INF2006F\", \"STA2020F\", "
				+ "\"STA2030S\",\"CSC2002S\", \"CSC2003S\", \"CSC3002F\", \"STA3030F\", \"CSC3003S\", \"STA3036S\""
				);

	}

}
