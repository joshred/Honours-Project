import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.v1.StatementResult;

public class BigNeo4jDatabase extends Neo4jDatabase {
	
	HashMap<String, String> roots = new HashMap<String, String>();
	HashMap<String, String> rootsSimilarity = new HashMap<String, String>();
	String studentRoots, rootsSize;
	String root, similarity, intersection;

	public BigNeo4jDatabase(String year, String major1, String major2 ,String curriculum, int[] marks) {
		super(year, major1, major2, curriculum, marks);
	}

	@Override
	public String getCourseCounts() {
		return super.getCourseCounts();
	}

	@Override
	public boolean getMissingCourses() {
		return super.getMissingCourses();
	}
	public void checkPrerequisites(String courses) {
		super.checkPrerequisites(courses);
	}

	@Override
	public boolean checkCounts(String counts) {
		return super.checkCounts(counts);
	}
	
	public void getRootStudents(String major1, String major2) {
		try {
			StatementResult rootResult = session.run(
					"MATCH(m:Major {MajorCode: \""+ major1 +"\"})<-[:TOOK]-(sr:Student:Root)-[:TOOK]->(m2:Major {MajorCode: \""+ major2 +"\"})\r\n" + 
					"RETURN collect(sr.StudentID) AS roots, SIZE(collect(sr.StudentID)) as rootsSize");
		        
			while ( rootResult.hasNext() ) {
			// get the list of similar students according to courses
		    Map<String,Object> resultTable = rootResult.next().asMap();

		    // create a new map of similar students and corresponding count
		    for (Entry<String,Object> pair : resultTable.entrySet()) {
		    	roots.put(pair.getKey().toString(), pair.getValue().toString());
		    	}
			}
				   
			studentRoots = roots.get("roots");
			rootsSize = roots.get("rootsSize");
			
			report += "Found " + rootsSize + " student clusters who enrolled in your majors.\r\n";
			Utils.print(new String [] {"Found " + rootsSize + " student clusters who enrolled in your majors."});
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error retreving major roots.");
				//return null;
		}
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
				// get the list of similar students according to courses
			    Map<String,Object> resultTable = similarityResult.next().asMap();

				// create a new map of similar TODO
			    for (Entry<String,Object> pair : resultTable.entrySet()) {
			    	rootsSimilarity.put(pair.getKey().toString(), pair.getValue().toString());
				   	}
				}
					
				root = rootsSimilarity.get("root");
				similarity = rootsSimilarity.get("similarity");
				intersection = rootsSimilarity.get("intersection");
				
				report += "The cluster with the greatest similarity ("+ similarity +") shares "+ intersection +" courses with your curriculum.\r\n";
				Utils.print(new String [] {"The cluster with the greatest similarity ("+ similarity +") shares "+ intersection +" courses with your curriculum."});
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error retreving root similarity.");
			}
		}
	}
	
	@Override
	public String getSimilarCourseStudents(String minSimilarCourses) {
		//minSimilarCourses not used
		report += "** Performance Prediction **\r\n";
		getRootStudents(super.major1, super.major2);
		try {
			StatementResult courseResult = session.run(
			        "MATCH (s:Student)-[:CHILD_OF]->(sr:Student:Root {StudentID: "+ root +"})\r\n" + 
			        "RETURN collect(s.StudentID) AS similarCourseStudents, " +
			        "size(collect(s)) AS similarCourseStudentsSize");
		        
			while ( courseResult.hasNext() ) {
			// get the list of similar students according to courses
		    Map<String,Object> resultTable = courseResult.next().asMap();

		    // create a new map of similar students and corresponding count
		    for (Entry<String,Object> pair : resultTable.entrySet()) {
		    	similarCourse.put(pair.getKey().toString(), pair.getValue().toString());
		    	}
			}
				   
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

	@Override
	public String getSimilarMarkStudents(String similarCourseStudents) {
		return super.getSimilarMarkStudents(similarCourseStudents);
	}

	@Override
	public void predictGrade(String similarMarkStudents) {
		super.predictGrade(similarMarkStudents);
	}
	
	public String getReport() {
		return super.getReport();
	}

	@Override
	public void close() {
		super.close();
		
	}

}
