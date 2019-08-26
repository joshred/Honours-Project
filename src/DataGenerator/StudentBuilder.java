package DataGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class StudentBuilder {
	
	static int godelEncoding(ArrayList<Integer> IDs){
		
		int[] firstPrimes = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71};
		int godelNum = 0;
		
		for (int i=0; i< IDs.size(); i++){
			if (i == 0){
				godelNum = (int) Math.pow(firstPrimes[i], IDs.get(i));
			}
			else{
				godelNum = godelNum*((int) Math.pow(firstPrimes[i], IDs.get(i)));
			}
		}
		return godelNum;
		
	}

	public static void main(String[] args) throws IOException {
		
		CourseListBuilder.getCourses();
		
		String fileName = "stu.csv";
		
		String str = "Hello";
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    writer.write("ClusterSize, Major1, Major2, Hash, Courses\r\n");
	     
	    
		
		int numStudents = 0;
		Random rand = new Random();
		
		while(numStudents < 1000000) {
			int clusterSize = rand.nextInt(270) + 50;
			float faculty1 = rand.nextFloat();
			float faculty2 = rand.nextFloat();
			String major1; String major2;
			ArrayList<String> curriculum = new ArrayList<String>();
			ArrayList<Integer> courseNums = new ArrayList<Integer>();
			
			if( faculty1 < 0.31) {
				//com
				major1 = CourseListBuilder.getCOMmajor();
				ArrayList<String> courses = CourseListBuilder.getCOMcourses(major1); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getCOMCourseIDs(courses));
			}else if (faculty1 < 0.48) {
				//ebe
				major1 = CourseListBuilder.getEBEmajor(); 
				ArrayList<String> courses = CourseListBuilder.getEBEcourses(major1); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getEBECourseIDs(courses));
			}else if (faculty1 < 0.78) {
				//hum
				major1 = CourseListBuilder.getHUMmajor(); 
				ArrayList<String> courses = CourseListBuilder.getHUMcourses(major1); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getHUMCourseIDs(courses));
			}else if (faculty1 < 0.79) {
				//law
				major1 = CourseListBuilder.getLAWmajor(); 
				ArrayList<String> courses = CourseListBuilder.getLAWcourses(major1); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getLAWCourseIDs(courses));
			}else if (faculty1 < 0.89) {
				//med
				major1 = CourseListBuilder.getMEDmajor(); 
				ArrayList<String> courses = CourseListBuilder.getMEDcourses(major1); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getMEDCourseIDs(courses));
			}else {
				//sci
				major1 = CourseListBuilder.getSCImajor(); 
				ArrayList<String> courses = CourseListBuilder.getSCIcourses(major1); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getSCICourseIDs(courses));
			}
			
			if( faculty2 < 0.31) {
				//com
				major2 = CourseListBuilder.getCOMmajor();
				ArrayList<String> courses = CourseListBuilder.getCOMcourses(major2); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getCOMCourseIDs(courses));
			}else if (faculty2 < 0.48) {
				//ebe
				major2 = CourseListBuilder.getEBEmajor(); 
				ArrayList<String> courses = CourseListBuilder.getEBEcourses(major2); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getEBECourseIDs(courses));
			}else if (faculty2 < 0.78) {
				//hum
				major2 = CourseListBuilder.getHUMmajor(); 
				ArrayList<String> courses = CourseListBuilder.getHUMcourses(major2); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getHUMCourseIDs(courses));
			}else if (faculty2 < 0.79) {
				//law
				major2 = CourseListBuilder.getLAWmajor(); 
				ArrayList<String> courses = CourseListBuilder.getLAWcourses(major2); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getLAWCourseIDs(courses));
			}else if (faculty2 < 0.89) {
				//med
				major2 = CourseListBuilder.getMEDmajor(); 
				ArrayList<String> courses = CourseListBuilder.getMEDcourses(major2); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getMEDCourseIDs(courses));
			}else {
				//sci
				major2 = CourseListBuilder.getSCImajor(); 
				ArrayList<String> courses = CourseListBuilder.getSCIcourses(major2); 
				curriculum.addAll(courses);
				courseNums.addAll(CourseListBuilder.getSCICourseIDs(courses));
			}
			
			Collections.sort(courseNums);
			int hash = courseNums.hashCode();
			writer.append(clusterSize +", "+ major1 +", "+ major2 +", "+ Math.abs(hash) +", "+ curriculum +"\r\n");
			//System.out.println();

			numStudents = numStudents + clusterSize;
		}
		writer.close();
		System.out.println(numStudents);

	}

}
