package DataGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class CourseListBuilder {
	
	static Random rand = new Random();
	
	static ArrayList<String> COMmajors = new ArrayList<String>();
	static ArrayList<String> EBEmajors = new ArrayList<String>();
	static ArrayList<String> HUMmajors = new ArrayList<String>();
	static ArrayList<String> LAWmajors = new ArrayList<String>();
	static ArrayList<String> MEDmajors = new ArrayList<String>();
	static ArrayList<String> SCImajors = new ArrayList<String>();
	public static ArrayList<String> ALLmajors = new ArrayList<String>();

	
	static HashMap<String, ArrayList<String>> COMcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> EBEcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> HUMcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> LAWcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> MEDcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> SCIcourses = new HashMap<String, ArrayList<String>>();
	
	static HashMap<String, Integer> COMcourseIDs = new HashMap<String, Integer>();
	static HashMap<String, Integer> EBEcourseIDs = new HashMap<String, Integer>();
	static HashMap<String, Integer> HUMcourseIDs = new HashMap<String, Integer>();
	static HashMap<String, Integer> LAWcourseIDs = new HashMap<String, Integer>();
	static HashMap<String, Integer> MEDcourseIDs = new HashMap<String, Integer>();
	static HashMap<String, Integer> SCIcourseIDs = new HashMap<String, Integer>();

	
	public static void getCourses() {
		String csvFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\courses.csv";
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	System.out.println("start");
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] course = line.split(",");
                
                if(course[3].equals("COM")) {
                	if(!COMcourses.containsKey(course[4])) {
                		COMmajors.add(course[4]);
                		COMcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		COMcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}else {
                		COMcourses.get(course[4]).add(course[1]);
                		COMcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("EBE")) {
                	if(!EBEcourses.containsKey(course[4])) {
                		EBEmajors.add(course[4]);
                		EBEcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		EBEcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}else {
                		EBEcourses.get(course[4]).add(course[1]);
                		EBEcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("HUM")) {
                	if(!HUMcourses.containsKey(course[4])) {
                		HUMmajors.add(course[4]);
                		HUMcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		HUMcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}else {
                		HUMcourses.get(course[4]).add(course[1]);
                		HUMcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("LAW")) {
                	if(!LAWcourses.containsKey(course[4])) {
                		LAWmajors.add(course[4]);
                		LAWcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		LAWcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}else {
                		LAWcourses.get(course[4]).add(course[1]);
                		LAWcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("MED")) {
                	if(!MEDcourses.containsKey(course[4])) {
                		MEDmajors.add(course[4]);
                		MEDcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		MEDcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}else {
                		MEDcourses.get(course[4]).add(course[1]);
                		MEDcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("SCI")) {
                	if(!SCIcourses.containsKey(course[4])) {
                		SCImajors.add(course[4]);
                		SCIcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		SCIcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}else {
                		SCIcourses.get(course[4]).add(course[1]);
                		SCIcourseIDs.put(course[1], Integer.parseInt(course[0]));
                	}
                }

            }
            ALLmajors.addAll(COMmajors);
            ALLmajors.addAll(EBEmajors);
            ALLmajors.addAll(HUMmajors);
            ALLmajors.addAll(LAWmajors);
            ALLmajors.addAll(MEDmajors);
            ALLmajors.addAll(SCImajors);
            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}

	public static void main(String[] args) {
         
	}

	public static String getCOMmajor() {
		return COMmajors.get(rand.nextInt(COMmajors.size()));
	}
	public static String getEBEmajor() {
		return EBEmajors.get(rand.nextInt(EBEmajors.size()));
	}
	public static String getHUMmajor() {
		return HUMmajors.get(rand.nextInt(HUMmajors.size()));
	}
	public static String getLAWmajor() {
		return LAWmajors.get(rand.nextInt(LAWmajors.size()));
	}
	public static String getMEDmajor() {
		return MEDmajors.get(rand.nextInt(MEDmajors.size()));
	}
	public static String getSCImajor() {
		return SCImajors.get(rand.nextInt(SCImajors.size()));
	}
	
	public static ArrayList<String> getCOMcourses(String major){
		int maxCourses = COMcourses.get(major).size();
		int max = Math.min(10, maxCourses);
		int min = Math.min(3, maxCourses);
		
		int totalItems = rand.nextInt(max-min) + min;
		
		ArrayList<String> courses = new ArrayList<String>(); 
        for (int i = 0; i < totalItems; i++) { 
            int randomIndex = rand.nextInt(maxCourses); 
            // add element in temporary list 
            courses.add(COMcourses.get(major).get(randomIndex)); 
        } 
        return courses;
	}
	
	public static ArrayList<String> getEBEcourses(String major){
		int maxCourses = EBEcourses.get(major).size();
		int max = Math.min(10, maxCourses);
		int min = Math.min(3, maxCourses);
		
		int totalItems = rand.nextInt(max-min) + min;
		
		ArrayList<String> courses = new ArrayList<String>(); 
        for (int i = 0; i < totalItems; i++) { 
            int randomIndex = rand.nextInt(maxCourses); 
            // add element in temporary list 
            courses.add(EBEcourses.get(major).get(randomIndex)); 
        } 
        return courses;
	}
	
	public static ArrayList<String> getHUMcourses(String major){
		int maxCourses = HUMcourses.get(major).size();
		int max = Math.min(10, maxCourses);
		int min = Math.min(3, maxCourses);
		
		int totalItems = rand.nextInt(max-min) + min;
		
		ArrayList<String> courses = new ArrayList<String>(); 
        for (int i = 0; i < totalItems; i++) { 
            int randomIndex = rand.nextInt(maxCourses); 
            // add element in temporary list 
            courses.add(HUMcourses.get(major).get(randomIndex)); 
        } 
        return courses;
	}
	
	public static ArrayList<String> getLAWcourses(String major){
		int maxCourses = LAWcourses.get(major).size();
		int max = Math.min(10, maxCourses);
		int min = Math.min(3, maxCourses);
		
		int totalItems = rand.nextInt(max-min) + min;
		
		ArrayList<String> courses = new ArrayList<String>(); 
        for (int i = 0; i < totalItems; i++) { 
            int randomIndex = rand.nextInt(maxCourses); 
            // add element in temporary list 
            courses.add(LAWcourses.get(major).get(randomIndex)); 
        } 
        return courses;
	}
	
	public static ArrayList<String> getMEDcourses(String major){
		int maxCourses = MEDcourses.get(major).size();
		int max = Math.min(10, maxCourses);
		int min = Math.min(3, maxCourses);
		
		int totalItems = rand.nextInt(max-min) + min;
		
		ArrayList<String> courses = new ArrayList<String>(); 
        for (int i = 0; i < totalItems; i++) { 
            int randomIndex = rand.nextInt(maxCourses); 
            // add element in temporary list 
            courses.add(MEDcourses.get(major).get(randomIndex)); 
        } 
        return courses;
	}
	
	public static ArrayList<String> getSCIcourses(String major){
		int maxCourses = SCIcourses.get(major).size();
		int max = Math.min(10, maxCourses);
		int min = Math.min(3, maxCourses);
		
		int totalItems = rand.nextInt(max-min) + min;
		
		ArrayList<String> courses = new ArrayList<String>(); 
        for (int i = 0; i < totalItems; i++) { 
            int randomIndex = rand.nextInt(maxCourses); 
            // add element in temporary list 
            courses.add(SCIcourses.get(major).get(randomIndex)); 
        } 
        return courses;
	}
	
	public static ArrayList<Integer> getCOMCourseIDs (ArrayList<String> courses){
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		for(String c : courses) {
			IDs.add(COMcourseIDs.get(c));
		}
		return IDs;
	}

	public static ArrayList<Integer> getEBECourseIDs(ArrayList<String> courses) {
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		for(String c : courses) {
			IDs.add(EBEcourseIDs.get(c));
		}
		return IDs;
	}

	public static ArrayList<Integer> getHUMCourseIDs(ArrayList<String> courses) {
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		for(String c : courses) {
			IDs.add(HUMcourseIDs.get(c));
		}
		return IDs;
	}

	public static ArrayList<Integer> getLAWCourseIDs(ArrayList<String> courses) {
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		for(String c : courses) {
			IDs.add(LAWcourseIDs.get(c));
		}
		return IDs;
	}

	public static ArrayList<Integer> getMEDCourseIDs(ArrayList<String> courses) {
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		for(String c : courses) {
			IDs.add(MEDcourseIDs.get(c));
		}
		return IDs;
	}

	public static ArrayList<Integer> getSCICourseIDs(ArrayList<String> courses) {
		ArrayList<Integer> IDs = new ArrayList<Integer>();
		for(String c : courses) {
			IDs.add(SCIcourseIDs.get(c));
		}
		return IDs;
	}

}
