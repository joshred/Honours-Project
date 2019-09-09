import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.v1.StatementResult;

/**
 * A Neo4j database object containing all methods needed to query the larger Neo4j database with tree structure.
 * @author Josh
 */
public class BigNeo4jDatabase extends Neo4jDatabase {
	
	// Initialise extra variables required to support root queries
	HashMap<String, String> roots = new HashMap<String, String>();
	HashMap<String, String> rootsSimilarity = new HashMap<String, String>();
	String studentRoots, rootsSize;
	String root, similarity, intersection;

	/**
	 * Create a BigNeo4j database object
	 * @param year The year the student is about to start, this affects the algorithms & nodes queried
	 * @param major1 the user's primary major 
	 * @param major2 the user's secondary major
	 * @param curriculum a String of the user's input courses
	 * @param marks The user input marks which affect the LB and UB parameters of queries
	 */
	public BigNeo4jDatabase(String year, String major1, String major2 ,String curriculum, int[] marks) {
		super(year, major1, major2, curriculum, marks);
	}

	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#getCourseCounts()
	 */
	@Override
	public String getCourseCounts() {
		return super.getCourseCounts();
	}

	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#getMissingCourses()
	 */
	@Override
	public boolean getMissingCourses() {
		return super.getMissingCourses();
	}
	
	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#checkPrerequisites(java.lang.String)
	 */
	public void checkPrerequisites(String courses) {
		super.checkPrerequisites(courses);
	}

	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#checkCounts(java.lang.String)
	 */
	@Override
	public boolean checkCounts(String counts) {
		return super.checkCounts(counts);
	}
	
	/**
	 * Retrieves student roots with the same majors as the user
	 * @param major1 the user's primary major 
	 * @param major2 the user's secondary major
	 */
	public void getRootStudents(String major1, String major2) {
		// find roots with the same major combos
		try {
			StatementResult rootResult = session.run(
					"MATCH(m:Major {MajorCode: \""+ major1 +"\"})<-[:TOOK]-(sr:Student:Root)-[:TOOK]->(m2:Major {MajorCode: \""+ major2 +"\"})\r\n" + 
					"RETURN collect(sr.StudentID) AS roots, SIZE(collect(sr.StudentID)) as rootsSize");
		        
			while ( rootResult.hasNext() ) {
			// retrieve and store query result
		    Map<String,Object> resultTable = rootResult.next().asMap();
		    // create a new map of similar students and corresponding count
		    for (Entry<String,Object> pair : resultTable.entrySet()) {
		    	roots.put(pair.getKey().toString(), pair.getValue().toString());
		    	}
			}
			
			// output result
			studentRoots = roots.get("roots");
			rootsSize = roots.get("rootsSize");
			
			report += "Found " + rootsSize + " student clusters who enrolled in your majors.\r\n";
			Utils.print(new String [] {"Found " + rootsSize + " student clusters who enrolled in your majors."});
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error retreving major roots.");
		}
		
		// find most similar student root using jaccard 
		if(studentRoots != null) {
			try {
				StatementResult similarityResult = session.run(
					"MATCH (c:Course) where c.CourseName in ["+ super.curriculum +"]\r\n" + 
					"WITH id(c) AS inCourses\r\n" + 
					"MATCH (sr:Student:Root)-[:ENROLLED_IN]->(c2:Course) WHERE sr.StudentID in "+ studentRoots +"\r\n" + 
					"WITH inCourses, sr, id(c2) AS outCourses\r\n" + 
					"RETURN \r\n" + 
					"       sr.StudentID AS root,\r\n" + 
					"       algo.similarity.jaccard(collect(distinct inCourses), collect(distinct outCourses)) AS similarity,\r\n" + 
					"       size(apoc.coll.intersection(collect(distinct inCourses), collect(distinct outCourses))) as intersection\r\n" + 
					"ORDER BY similarity DESC limit 1");
				        
				while ( similarityResult.hasNext() ) {
				// get and store similar student result
			    Map<String,Object> resultTable = similarityResult.next().asMap();
			    for (Entry<String,Object> pair : resultTable.entrySet()) {
			    	rootsSimilarity.put(pair.getKey().toString(), pair.getValue().toString());
				   	}
				}
				
				// output results
				root = rootsSimilarity.get("root");
				similarity = rootsSimilarity.get("similarity");
				intersection = rootsSimilarity.get("intersection");
				
				report += "The cluster with the greatest similarity ("+ similarity.substring(0, Math.min(4, similarity.length())) +") shares "+ intersection +" courses with your curriculum.\r\n";
				Utils.print(new String [] {"The cluster with the greatest similarity ("+ similarity +") shares "+ intersection +" courses with your curriculum."});
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error retreving root similarity.");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see Neo4jDatabase#getSimilarCourseStudents(java.lang.String)
	 */
	@Override
	public String getSimilarCourseStudents(String minSimilarCourses) {
		//minSimilarCourses is not used
		report += "** Performance Prediction **\r\n";
		
		// first, get the most similar root student
		// then, get all children nodes to represent similar course students
		getRootStudents(super.major1, super.major2);
		try {
			StatementResult courseResult = session.run(
			        "MATCH (s:Student)-[:CHILD_OF]->(sr:Student:Root {StudentID: "+ root +"})\r\n" + 
			        "RETURN collect(s.StudentID) AS similarCourseStudents, " +
			        "size(collect(s)) AS similarCourseStudentsSize");
		        
			while ( courseResult.hasNext() ) {
			// get and store query result
		    Map<String,Object> resultTable = courseResult.next().asMap();
		    for (Entry<String,Object> pair : resultTable.entrySet()) {
		    	similarCourse.put(pair.getKey().toString(), pair.getValue().toString());
		    	}
			}
			
			// ouptut results
			similarCourseStudents = similarCourse.get("similarCourseStudents");
			similarCourseStudentsSize = similarCourse.get("similarCourseStudentsSize");
			
			report += "Found " + similarCourseStudentsSize + " past students who enrolled in similar courses to your curriculum.\r\n";
			Utils.print(new String [] {"Found " + similarCourseStudentsSize + " past students who enrolled in similar courses to your curriculum."});
			return similarCourseStudents;
		}catch (Exception e) {
			System.out.println("Error finding similar course students.");
			return null;
		}
	}

	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#getSimilarMarkStudents(java.lang.String)
	 */
	@Override
	public String getSimilarMarkStudents(String similarCourseStudents) {
		return super.getSimilarMarkStudents(similarCourseStudents);
	}

	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#predictGrade(java.lang.String)
	 */
	@Override
	public void predictGrade(String similarMarkStudents) {
		super.predictGrade(similarMarkStudents);
	}
	
	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#getReport()
	 */
	public String getReport() {
		return super.getReport();
	}

	/* (non-Javadoc) See Neo4jDatabase
	 * @see Neo4jDatabase#close()
	 */
	@Override 
	public void close() {
		super.close();
	}

}
