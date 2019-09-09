import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

/**
 * A Neo4j database object containing all methods needed to query Neo4j
 * @author Josh
 */
public class Neo4jDatabase implements Database {

	// Objects used to connect to the db
	Driver driver;
	Session session;

	// Variables passed to queries
	String major1;
	String major2;
	String curriculum;
	String year;
	static int averageMark;
	static String LB;
	static String UB;
	static String algorithm;
	static String inputMark;
	static String predictedMark;
	static String similarNode;
	static String performanceNode;
	static String similarityCutoff;
	static String similarityData;

	// Maps to store query results
	static Map<String, Integer> countsTable; // Stores the results from the count query on student graph
	static Map<String, ArrayList<String>> missingMustCourses; // Stores the results from the missing compulsory courses
																// query on student graph
	static Map<String, ArrayList<String>> missingOptionalCourses; // Stores the results from the missing optional
																	// courses query on student graph
	static Map<String, ArrayList<String>> takenOptionalCourses; // Stores the results from the missing optional courses
																// query on student graph
	static Map<String, ArrayList<String>> constraintTreeResult; // Stores the results from the query on the constraint
																// tree
	static Map<String, String> similarCourse; // Stores the similar course students
	static Map<String, String> similarMark; // Stores the similar mark students
	static Map<String, String> filteredMark; // Stores the similar mark students filtered according to mark
	static Map<String, String> predictedPerformance; // Stores the GPA and std dev results

	// Variables used for similarity queries input/ output
	static String minSimilarCourses;
	static String similarCourseStudents;
	static String similarCourseStudentsSize;
	static String similarMarkStudents;
	static String similarMarkStudentsSize;
	static String filteredMarkStudents;
	static String filteredMarkStudentsSize;

	// String to store system output
	static String report;

	/**
	 * Create a new connection to Neo4j database and set parameters for the corresponding queries 
	 * @param year The year the student is about to start, this affects the algorithms & nodes queried
	 * @param major1 the user's primary major 
	 * @param major2 the user's secondary major
	 * @param curriculum a String of the user's input courses
	 * @param marks The user input marks which affect the LB and UB parameters of queries
	 */
	public Neo4jDatabase(String year, String major1, String major2, String curriculum, int[] marks) {
		// Create a connection
		driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
		session = driver.session();

		report = "";

		this.major1 = major1;
		this.major2 = major2;
		this.curriculum = curriculum;
		this.year = year;

		// Set average mark and LB, UB
		averageMark = Utils.average(marks);
		LB = Integer.toString(averageMark - 5);
		UB = Integer.toString(averageMark + 5);

		// Set query parameters which depend on year of registration
		if (year.equals("1")) {
			algorithm = "euclidean";
			inputMark = "NBT";
			predictedMark = "first year GPA";
			similarNode = ":Subject {Faculty: \"NBT\"}";
			performanceNode = ":Course {Year: 1}";
			similarityCutoff = "18";
			similarityData = "WITH {item:id(s), weights: collect(coalesce(r.Mark, algo.NaN()))} as userData\r\n";
		} else if (year.equals("2")) {
			algorithm = "cosine";
			inputMark = "first year GPA";
			predictedMark = "second year GPA";
			similarNode = ":Course {Year: 1}";
			performanceNode = ":Course {Year: 2}";
			similarityCutoff = "10";
			similarityData = "WITH s, avg(r.Mark) as average\r\n"
					+ "WITH s, average, {item:id(s), weights: collect(average)} as userData\r\n";
		} else if (year.equals("3")) {
			algorithm = "cosine";
			inputMark = "second year GPA";
			predictedMark = "third year GPA";
			similarNode = ":Course {Year: 2}";
			performanceNode = ":Course {Year: 3}";
			similarityCutoff = "10";
			similarityData = "WITH s, avg(r.Mark) as average\r\n"
					+ "WITH s, average, {item:id(s), weights: collect(average)} as userData\r\n";
		}

		// Initialise result tables
		countsTable = new HashMap<String, Integer>();
		missingMustCourses = new HashMap<String, ArrayList<String>>();
		missingOptionalCourses = new HashMap<String, ArrayList<String>>();
		takenOptionalCourses = new HashMap<String, ArrayList<String>>();
		constraintTreeResult = new HashMap<String, ArrayList<String>>();
		similarCourse = new HashMap<String, String>();
		similarMark = new HashMap<String, String>();
		filteredMark = new HashMap<String, String>();
		predictedPerformance = new HashMap<String, String>();
	}

	/*
	 * (non-Javadoc) Check CSC courses are taken in correct order
	 * 
	 * @see Database#checkPrerequisites(java.lang.String)
	 */
	public void checkPrerequisites(String courses) {
		// For all user input courses, extract those which are computer science
		ArrayList<String> userCourses = new ArrayList<String>(
				Arrays.asList(courses.replace("\"", "").replace(" ", "").split(",")));
		ArrayList<String> cscCourses = new ArrayList<String>();
		for (int i = 0; i < userCourses.size(); i++) {
			if (userCourses.get(i).startsWith("CSC")) {
				cscCourses.add(userCourses.get(i));
			}
		}

		// Compare the order of computer science courses the student is taking to the required course order
		String[] prereqArray = { "CSC1015F", "CSC1016S", "CSC2001F", "CSC2002S", "CSC2003S", "CSC3002F", "CSC3003S" };
		ArrayList<String> prereq = new ArrayList<String>(Arrays.asList(prereqArray));
		if (!prereq.equals(cscCourses)) {
			report += ("Pre-requisitie courses taken out of order.\r\n");
			Utils.print(new String[] { "Pre-requisitie courses taken out of order." });
		}
	}

	/*
	 * (non-Javadoc) Retrieve course counts using input curriculum
	 * 
	 * @see Database#getCourseCounts()
	 */
	public String getCourseCounts() {
		// get course & NQF counts
		try {
			StatementResult result = session.run("MATCH (c: Course)\r\n" + "WHERE c.CourseName IN [" + curriculum
					+ "]\r\n" + "WITH c, collect(c) as courses\r\n" + "RETURN  \r\n"
					+ "sum(size([f in courses where f.Semester = 0 and f.Year = 1])) as s0y1, \r\n"
					+ "sum(size([f in courses where f.Semester = 1 and f.Year = 1])) as s1y1, \r\n"
					+ "sum(size([f in courses where f.Semester = 2 and f.Year = 1])) as s2y1,\r\n"
					+ "sum(size([f in courses where f.Semester = 0 and f.Year = 2])) as s0y2, \r\n"
					+ "sum(size([f in courses where f.Semester = 1 and f.Year = 2])) as s1y2, \r\n"
					+ "sum(size([f in courses where f.Semester = 2 and f.Year = 2])) as s2y2,\r\n"
					+ "sum(size([f in courses where f.Semester = 0 and f.Year = 3])) as s0y3, \r\n"
					+ "sum(size([f in courses where f.Semester = 1 and f.Year = 3])) as s1y3, \r\n"
					+ "sum(size([f in courses where f.Semester = 2 and f.Year = 3])) as s2y3,\r\n"
					+ "sum(size([f in courses where (f.Semester = 1 or f.Semester = 2)])) as halfcourses, \r\n"
					+ "sum(size([f in courses where f.Semester = 0])) as fullcourses,\r\n"
					+ "sum(size([f in courses where f.Faculty = \"SCI\" AND (f.Semester = 1 or f.Semester = 2)])) as sciHalfCourses, \r\n"
					+ "sum(size([f in courses where f.Faculty = \"SCI\"AND f.Semester = 0])) as sciFullCourses, \r\n"
					+ "sum(size([f in courses where f.Faculty = \"SCI\" and (f.Semester = 1 or f.Semester = 2) and (f.Year = 2 or f.Year = 3)])) as seniorSciHalfCourses, \r\n"
					+ "sum(size([f in courses where f.Faculty = \"SCI\"AND f.Semester = 0 and (f.Year = 2 or f.Year = 3)])) as seniorSciFullFourses, \r\n"
					+ "sum(size([f in courses where f.Faculty = \"SCI\" and (f.Semester = 1 or f.Semester = 2) and f.Year = 3])) as thirdSciHalfCourses, \r\n"
					+ "sum(size([f in courses where f.Faculty = \"SCI\"AND f.Semester = 0 and f.Year = 3])) as thirdSciFullFourses, \r\n"
					+ "sum(size([f in courses where (f.Semester = 1 or f.Semester = 2) and (f.Year = 2 OR f.Year = 3)])) as seniorHalfCourses, \r\n"
					+ "sum(size([f in courses where (f.Semester = 0 and (f.Year = 2 OR f.Year = 3))])) as seniorFullCourses, \r\n"
					+ "sum(reduce (acc1 = 0, tot IN extract(n in courses | n.NQFCredits) | acc1 + tot)) AS totalNQF, \r\n"
					+ "sum(reduce (acc2 = 0, tot IN extract(n in [k in courses where k.Faculty = \"SCI\"] | n.NQFCredits) | acc2 + tot)) AS sciNQF, \r\n"
					+ "sum(reduce (acc3 = 0, tot IN extract(n in [k in courses where k.Year = 3] | n.NQFCredits) | acc3 + tot)) AS thirdNQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 0 and f.Year = 1] | n.NQFCredits) | acc + tot)) as s0y1NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 1 and f.Year = 1] | n.NQFCredits) | acc + tot)) as s1y1NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 2 and f.Year = 1] | n.NQFCredits) | acc + tot)) as s2y1NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 0 and f.Year = 2] | n.NQFCredits) | acc + tot)) as s0y2NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 1 and f.Year = 2] | n.NQFCredits) | acc + tot)) as s1y2NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 2 and f.Year = 2] | n.NQFCredits) | acc + tot)) as s2y2NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 0 and f.Year = 3] | n.NQFCredits) | acc + tot)) as s0y3NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 1 and f.Year = 3] | n.NQFCredits) | acc + tot)) as s1y3NQF,\r\n"
					+ "sum(reduce (acc = 0, tot IN extract(n in [f in courses where f.Semester = 2 and f.Year = 3] | n.NQFCredits) | acc + tot)) as s2y3NQF");

			while (result.hasNext()) {
				// get the counts from the db query
				Map<String, Object> resultTable = result.next().asMap();

				// create a new map of counts
				for (Entry<String, Object> pair : resultTable.entrySet()) {
					countsTable.put(pair.getKey().toString(), Integer.valueOf(pair.getValue().toString()));
				}
			}
			// extract and store counts
			int s1y1 = (int) countsTable.get("s1y1") + (int) countsTable.get("s0y1"); // 1
			int s2y1 = (int) countsTable.get("s2y1") + (int) countsTable.get("s0y1"); // 1
			int firstYearHalfCourses = Math.max(s1y1, s2y1);
			int s1y2 = (int) countsTable.get("s1y2") + (int) countsTable.get("s0y2"); // 2
			int s2y2 = (int) countsTable.get("s2y2") + (int) countsTable.get("s0y2"); // 2
			int s1y3 = (int) countsTable.get("s1y3") + (int) countsTable.get("s0y3"); // 2
			int s2y3 = (int) countsTable.get("s2y3") + (int) countsTable.get("s0y3"); // 2
			int seniorYearHalfCourses = Math.max(s1y2, Math.max(s2y2, Math.max(s1y3, s2y3)));
			int fullcourses = ((int) countsTable.get("halfcourses") / 2) + (int) countsTable.get("fullcourses"); // 3
			int sciFullCourses = ((int) countsTable.get("sciHalfCourses") / 2)
					+ (int) countsTable.get("sciFullCourses"); // 4
			int seniorFullCourses = ((int) countsTable.get("seniorHalfCourses") / 2)
					+ (int) countsTable.get("seniorFullCourses"); // 5
			int seniorSciFullCourses = ((int) countsTable.get("seniorSciHalfCourses") / 2)
					+ (int) countsTable.get("seniorSciFullFourses"); // 6
			int thirdSciFullFourses = ((int) countsTable.get("thirdSciHalfCourses") / 2)
					+ (int) countsTable.get("thirdSciFullFourses"); // 7
			int totalNQF = (int) countsTable.get("totalNQF");
			int sciNQF = (int) countsTable.get("sciNQF");
			int thirdNQF = (int) countsTable.get("thirdNQF");
			int s1y1NQF = (int) countsTable.get("s1y1NQF") + ((int) countsTable.get("s0y1NQF") / 2);
			int s2y1NQF = (int) countsTable.get("s2y1NQF") + ((int) countsTable.get("s0y1NQF") / 2);
			int s1y2NQF = (int) countsTable.get("s1y2NQF") + ((int) countsTable.get("s0y2NQF") / 2);
			int s2y2NQF = (int) countsTable.get("s2y2NQF") + ((int) countsTable.get("s0y2NQF") / 2);
			int s1y3NQF = (int) countsTable.get("s1y3NQF") + ((int) countsTable.get("s0y3NQF") / 2);
			int s2y3NQF = (int) countsTable.get("s2y3NQF") + ((int) countsTable.get("s0y3NQF") / 2);
			int firstYearHalfCoursesNQF = Math.max(s1y1NQF, s2y1NQF);
			int seniorYearHalfCoursesNQF = Math.max(s1y2NQF, Math.max(s2y2NQF, Math.max(s1y3NQF, s2y3NQF)));

			// create the counts to be passed to constraint checking graph
			return "{fullcourses: " + fullcourses + ", sciFullCourses: " + sciFullCourses + ", seniorFullCourses: "
					+ seniorFullCourses + ", seniorSciFullCourses: " + seniorSciFullCourses + ", thirdSciFullFourses: "
					+ thirdSciFullFourses + ", firstYearHalfCourses: " + firstYearHalfCourses
					+ ", seniorYearHalfCourses: " + seniorYearHalfCourses + ", totalNQF: " + totalNQF + ", sciNQF: "
					+ sciNQF + ", thirdNQF: " + thirdNQF + ", firstYearHalfCoursesNQF: " + firstYearHalfCoursesNQF
					+ ", seniorYearHalfCoursesNQF: " + seniorYearHalfCoursesNQF + "}";

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error retreving course counts.");
			return null;
		}
	}

	/*
	 * (non-Javadoc) Return true if any courses are missing from the curriculum
	 * 
	 * @see Database#getMissingCourses()
	 */
	public boolean getMissingCourses() {
		report += "** Degree Requirements **\r\n";
		// get compulsory courses not taken
		try {
			StatementResult result = session.run("MATCH (m: Major)-[:REQUIRES {type: \"Compulsory\"}]->(c: Course)\r\n"
					+ "WITH c, collect(c.CourseName) AS courses\r\n" + "UNWIND courses AS MustCourses\r\n"
					+ "WITH MustCourses, [" + curriculum + "] as cur //put planned curriculum here\r\n"
					+ "WITH MustCourses, cur, [n in MustCourses where not n in cur] as missing\r\n"
					+ "UNWIND (missing) AS MissingCourses\r\n"
					+ "RETURN collect(MissingCourses) as missingMustCourses");

			while (result.hasNext()) {
				// get the missing courses from the db query
				Map<String, Object> resultTable = result.next().asMap();

				// store the missing course result
				for (Entry<String, Object> pair : resultTable.entrySet()) {
					missingMustCourses.put(pair.getKey(), Utils.toList(pair.getValue().toString()));
				}
			}

			// get alternate courses not taken
			StatementResult result2 = session.run("MATCH (m: Major)-[:REQUIRES {type: \"Alternate\"}]->(c: Course)\r\n"
					+ "WITH c, collect(c.CourseName) as courses\r\n" + "UNWIND courses as OptionalCourses\r\n"
					+ "WITH OptionalCourses, [" + curriculum + "] as cur\r\n"
					+ "WITH OptionalCourses, cur, [n in OptionalCourses where not n in cur] as missing\r\n"
					+ "UNWIND(missing) as MissingCourses\r\n" + "WITH MissingCourses, collect(MissingCourses) as mc\r\n"
					+ "MATCH ()-[r:REQUIRES]->(c: Course) where c.CourseName in mc\r\n"
					+ "RETURN collect(r.Combination) as combinationsNotEnrolledIn, collect(c.CourseName) as coursesNotEnrolledIn");
			while (result2.hasNext()) {
				// get the results from the db query
				Map<String, Object> resultTable = result2.next().asMap();
				// store the alternate course result
				for (Entry<String, Object> pair : resultTable.entrySet()) {
					missingOptionalCourses.put(pair.getKey(), Utils.toList(pair.getValue().toString()));
				}
			}
			
			// get alternate courses enrolled in
			StatementResult result3 = session.run("match (m: Major)-[:REQUIRES {type: \"Alternate\"}]->(c: Course)\r\n"
					+ "with c, collect(c.CourseName) as courses\r\n" + "unwind courses as OptionalCourses\r\n"
					+ "with OptionalCourses, [" + curriculum + "] as cur\r\n"
					+ "with OptionalCourses, cur, [n in OptionalCourses where  n in cur] as taken\r\n"
					+ "unwind(taken) as TakenCourses\r\n" + "with TakenCourses, collect(TakenCourses) as tc\r\n"
					+ "match ()-[r:REQUIRES]->(c: Course) where c.CourseName in tc\r\n"
					+ "return collect(r.Combination) as combinationsEnrolledIn, collect(c.CourseName) as coursesEnrolledIn");

			while (result3.hasNext()) {
				// get the results from the db query
				Map<String, Object> resultTable = result3.next().asMap();
				// store the alternate course result
				for (Entry<String, Object> pair : resultTable.entrySet()) {
					takenOptionalCourses.put(pair.getKey(), Utils.toList(pair.getValue().toString()));
				}
			}

			// Check if student is missing any compulsory courses
			if (!missingMustCourses.get("missingMustCourses").isEmpty()) {
				report += "Missing the following course(s): " + missingMustCourses.get("missingMustCourses") + "\r\n";
				Utils.print(new String[] {
						"Missing the following course(s): " + missingMustCourses.get("missingMustCourses") });
				return true;
			}
			
			// Check if student is missing any alternate courses
			if (takenOptionalCourses.get("combinationsEnrolledIn").isEmpty()) {
				report += "You must enroll in one or more of the following course(s): "
						+ missingOptionalCourses.get("coursesNotEnrolledIn") + "\r\n";
				Utils.print(new String[] { "You must enroll in one or more of the following course(s): "
						+ missingOptionalCourses.get("coursesNotEnrolledIn") });
				return true;
			}
			
			Collection<String> takenCombinations = takenOptionalCourses.get("combinationsEnrolledIn"); 
			// e.g. [1,2] taken courses from combinations 1 & 2
			Collection<String> missingCombinations = missingOptionalCourses.get("combinationsNotEnrolledIn"); 
			// e.g. [1] missing courses from combinations 1
			takenCombinations.retainAll(missingCombinations); 
			// e.g. [1] intersection of takenCombinations and missingCombinations = [1], so there is a course component of [1] missing

			// Check that all optional courses are taken in the correct combination
			if (takenCombinations.size() > 0) {
				String notEnrolledIn = (String) takenCombinations.toArray()[0];
				int indexOfCourse = missingOptionalCourses.get("combinationsNotEnrolledIn").indexOf(notEnrolledIn);
				String missingCourseCombination = missingOptionalCourses.get("coursesNotEnrolledIn").get(indexOfCourse);
				report += "Missing the other course component: " + missingCourseCombination + "\r\n";
				Utils.print(new String[] { "Missing the other course component: " + missingCourseCombination });
				return true;
			}

			return false;
		} catch (Exception e) {
			System.out.println("Error retreving missing courses.");
			return true;
		}
	}

	/* (non-Javadoc) Return true if the terminal of the constraint checking tree is "Meets Requirements"
	 * @see Database#checkCounts(java.lang.String)
	 */
	public boolean checkCounts(String counts) {
		// send the counts to the constraint checking tree
		StatementResult result = session.run("WITH " + counts + " as curriculum\r\n"
				+ "MATCH p = (q:Decision:Question {start: 1})-[d:Decision*]->(t:Decision:Terminal)\r\n"
				+ "WHERE ALL(r in relationships(p) WHERE \r\n"
				+ "        (r.direction='left' AND curriculum[r.parameter]<r.value) OR // Left variant\r\n"
				+ "        (r.direction='right' AND curriculum[r.parameter]>=r.value) // Right variant\r\n"
				+ "      )\r\n"
				+ "WITH p, t, extract(n IN relationships(p)| n.description) AS extractedDescriptions\r\n"
				+ "RETURN t.name AS Terminal, extractedDescriptions");
		while (result.hasNext()) {
			// get result from the db query
			Map<String, Object> resultTable = result.next().asMap();

			// store query results
			for (Entry<String, Object> pair : resultTable.entrySet()) {
				constraintTreeResult.put(pair.getKey(), Utils.toList(pair.getValue().toString()));
			}
		}

		// Output the decisions made along the constraint path
		for (String d : constraintTreeResult.get("extractedDescriptions")) {
			report += d + "\r\n";
		}
		Utils.print(constraintTreeResult.get("extractedDescriptions").toArray(new String[0]));

		if (constraintTreeResult.get("Terminal").toString().equals("[DoesNotMeetRequirements]")) {
			return false;
		} else {
			return true;
		}
	}

	/* (non-Javadoc) Retrieve list of students who have the same courses
	 * @see Database#getSimilarCourseStudents(java.lang.String)
	 */
	public String getSimilarCourseStudents(String minSimilarCourses) {
		report += "** Performance Prediction **\r\n";
		// find students enrolled in similar courses
		try {
			StatementResult courseResult = session.run("MATCH (s:Student)-[:ENROLLED_IN]->(c:Course)\r\n"
					+ "WHERE c.CourseName IN [" + curriculum + "] \r\n"
					+ "WITH {item:id(s), categories: collect(id(c))} as userData\r\n"
					+ "WITH collect(userData) as data\r\n" + "CALL algo.similarity.jaccard.stream(data)\r\n"
					+ "YIELD item1, item2, count1, count2, intersection, similarity\r\n" + "WHERE intersection > "
					+ minSimilarCourses + "\r\n"
					+ "RETURN apoc.coll.union(collect(algo.asNode(item1).StudentID),collect(algo.asNode(item2).StudentID)) AS similarCourseStudents, "
					+ "size(apoc.coll.union(collect(algo.asNode(item1).StudentID),collect(algo.asNode(item2).StudentID)) ) AS similarCourseStudentsSize");

			while (courseResult.hasNext()) {
				// get the list of similar students according to courses
				Map<String, Object> resultTable = courseResult.next().asMap();

				// create a new map of similar students and corresponding count
				for (Entry<String, Object> pair : resultTable.entrySet()) {
					similarCourse.put(pair.getKey().toString(), pair.getValue().toString());
				}
			}
			
			// output results 
			similarCourseStudents = similarCourse.get("similarCourseStudents");
			similarCourseStudentsSize = similarCourse.get("similarCourseStudentsSize");

			report += "Found " + similarCourseStudentsSize
					+ " past students who enrolled in similar courses to your curriculum.\r\n";
			Utils.print(new String[] { "Found " + similarCourseStudentsSize
					+ " past students who enrolled in similar courses to your curriculum." });
			return similarCourseStudents;
		} catch (Exception e) {
			System.out.println("Error finding similar course students.");
			return null;
		}
	}

	/* (non-Javadoc) Retrieve list of students who have the same marks
	 * @see Database#getSimilarMarkStudents(java.lang.String)
	 */
	public String getSimilarMarkStudents(String similarCourseStudents) {
		// Euclid only used explicitly for 1st years, otherwise, skip to filteredmarkresult
		if (year.equals("1")) {
			try {
				StatementResult studentResult = session.run("MATCH (s:Student)-[r:ENROLLED_IN]->(c" + similarNode
						+ ")\r\n" + "WHERE s.StudentID IN " + similarCourseStudents + "\r\n" + similarityData
						+ "WITH collect(userData) as data\r\n"
						+ "CALL algo.similarity.euclidean.stream(data, {similarityCutoff: " + similarityCutoff
						+ "})\r\n" + "YIELD item1, item2, count1, count2, similarity\r\n"
						+ "RETURN apoc.coll.union(collect(algo.asNode(item1).StudentID),collect(algo.asNode(item2).StudentID)) AS similarMarkStudents, "
						+ "size(apoc.coll.union(collect(algo.asNode(item1).StudentID),collect(algo.asNode(item2).StudentID))) AS similarMarkStudentsSize");

				while (studentResult.hasNext()) {
					// get the list of similar students according to marks
					Map<String, Object> resultTable = studentResult.next().asMap();

					// create a new map of similar students and corresponding count
					for (Entry<String, Object> pair : resultTable.entrySet()) {
						similarMark.put(pair.getKey().toString(), pair.getValue().toString());
					}
				}

				similarMarkStudents = similarMark.get("similarMarkStudents");
				similarMarkStudentsSize = similarMark.get("similarMarkStudentsSize");

			} catch (Exception e) {
				System.out.println("Error finding similar mark students");
				return null;
			}
		} else {
			similarMarkStudents = similarCourseStudents;
		}
		try {
			// Using students from above, find the students with average marks in similar
			// range to student's input mark
			StatementResult filteredMarkResult = session.run("MATCH (s:Student)-[r:ENROLLED_IN]->(c" + similarNode
					+ ")\r\n" + "WHERE s.StudentID IN " + similarMarkStudents + "\r\n"
					+ "WITH  [s.StudentID, toInteger(avg(r.Mark))] as map\r\n"
					+ "WITH map, [n in collect(map) where n[1] > " + LB + " and n[1] < " + UB + "] as filtered\r\n"
					+ "WITH filtered, extract(f in filtered | f[0]) as s\r\n" + "UNWIND(s) as students\r\n"
					+ "RETURN collect(students) as filteredMarkStudents, size(collect(students)) as filteredMarkStudentsSize");

			while (filteredMarkResult.hasNext()) {
				// get the list of similar students filtered according to student imput marks
				Map<String, Object> resultTable = filteredMarkResult.next().asMap();

				// create a new map of similar students and corresponding count
				for (Entry<String, Object> pair : resultTable.entrySet()) {
					filteredMark.put(pair.getKey().toString(), pair.getValue().toString());
				}
			}
			
			// output results
			filteredMarkStudents = filteredMark.get("filteredMarkStudents");
			filteredMarkStudentsSize = filteredMark.get("filteredMarkStudentsSize");

			report += "Of " + similarCourseStudentsSize + " students enrolled in similar courses, "
					+ filteredMarkStudentsSize + " students achieved a " + inputMark + " mark similar to you ("
					+ averageMark + ")\r\n";
			Utils.print(new String[] { "Of " + similarCourseStudentsSize + " students enrolled in similar courses, "
					+ filteredMarkStudentsSize + " students achieved a " + inputMark + " mark similar to you ("
					+ averageMark + ")" });
			return filteredMarkStudents;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error finding similar mark students");
			return null;
		}
	}

	/* (non-Javadoc) Retrieve GPA of similar students
	 * @see Database#predictGrade(java.lang.String)
	 */
	public void predictGrade(String similarMarkStudents) {
		// get the marks of the similarMarkStudents
		try {
			StatementResult GPAResult = session.run("MATCH (s:Student)-[r:ENROLLED_IN]->(c" + performanceNode + ")\r\n"
					+ "WHERE s.StudentID IN " + filteredMarkStudents + "\r\n"
					+ "RETURN toInteger(avg(r.Mark)) as GPA, toInteger(stDev(r.Mark)) as standardDeviation");

			while (GPAResult.hasNext()) {
				// get the GPA of similar students
				Map<String, Object> resultTable = GPAResult.next().asMap();

				for (Entry<String, Object> pair : resultTable.entrySet()) {
					predictedPerformance.put(pair.getKey().toString(), pair.getValue().toString());
				}
			}
			
			// output results
			String GPA = predictedPerformance.get("GPA");
			String standardDeviation = predictedPerformance.get("standardDeviation");

			report += "The average " + predictedMark + " of " + filteredMarkStudentsSize + " students with a "
					+ inputMark + " and curriculum similar to you is " + GPA + " with a standard deviation of "
					+ standardDeviation + ".\r\n";
			Utils.print(new String[] { "The average " + predictedMark + " of " + filteredMarkStudentsSize
					+ " students with a " + inputMark + " and curriculum similar to you is " + GPA
					+ " with a standard deviation of " + standardDeviation + "." });
		} catch (Exception e) {
			System.out.println("Error predicting grade.");
		}
	}

	/* (non-Javadoc) Close the db connection
	 * @see Database#close()
	 */
	@Override
	public void close() {
		// close student data graph
		session.close();
		driver.close();

	}

	/* (non-Javadoc) Return the database report
	 * @see Database#getReport()
	 */
	@Override
	public String getReport() {
		return report;
	}
}
