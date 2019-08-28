package DataGenerator;

import java.util.ArrayList;
import java.util.Random;

public class test {
	
	static Random rand = new Random();
	
	static int godelEncoding(int[] IDs){
		
		int[] firstPrimes = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71};
		int godelNum = 0;
		
		for (int i=0; i< IDs.length; i++){
			if (i == 0){
				godelNum = (int) Math.pow(firstPrimes[i], IDs[i]);
			}
			else{
				godelNum = godelNum*((int) Math.pow(firstPrimes[i], IDs[i]));
			}
		}
		return godelNum;
		
	}

	public static void main(String[] args) {
		CourseListBuilder.getCourses();
		String major = CourseListBuilder.getCOMmajor();
		System.out.println(major);
		ArrayList crs = CourseListBuilder.getCOMcourses(major);
		System.out.println(crs);
		ArrayList<Integer> num  = CourseListBuilder.getCOMCourseIDs(crs);
		System.out.println(num);
		System.out.println(Math.round(51 + rand.nextFloat() * (61 - 51)));

	}

}
