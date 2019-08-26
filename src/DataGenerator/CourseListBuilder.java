package DataGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class CourseListBuilder {
	
	static HashMap<String, ArrayList<String>> COMcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> EBEcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> HUMcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> LAWcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> MEDcourses = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> SCIcourses = new HashMap<String, ArrayList<String>>();
	
	static HashMap<String, ArrayList<Integer>> COMcourseIDs = new HashMap<String, ArrayList<Integer>>();
	static HashMap<String, ArrayList<Integer>> EBEcourseIDs = new HashMap<String, ArrayList<Integer>>();
	static HashMap<String, ArrayList<Integer>> HUMcourseIDs = new HashMap<String, ArrayList<Integer>>();
	static HashMap<String, ArrayList<Integer>> LAWcourseIDs = new HashMap<String, ArrayList<Integer>>();
	static HashMap<String, ArrayList<Integer>> MEDcourseIDs = new HashMap<String, ArrayList<Integer>>();
	static HashMap<String, ArrayList<Integer>> SCIcourseIDs = new HashMap<String, ArrayList<Integer>>();
	
	public void getCourses() {
		String csvFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\courses.csv";
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	System.out.println("start");
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] course = line.split(",");
                
                if(course[3].equals("COM")) {
                	if(!COMcourses.containsKey(course[4])) {
                		COMcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		COMcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		COMcourses.get(course[4]).add(course[1]);
                		COMcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("EBE")) {
                	if(!EBEcourses.containsKey(course[4])) {
                		EBEcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		EBEcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		EBEcourses.get(course[4]).add(course[1]);
                		EBEcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("HUM")) {
                	if(!HUMcourses.containsKey(course[4])) {
                		HUMcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		HUMcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		HUMcourses.get(course[4]).add(course[1]);
                		HUMcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("LAW")) {
                	if(!LAWcourses.containsKey(course[4])) {
                		LAWcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		LAWcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		LAWcourses.get(course[4]).add(course[1]);
                		LAWcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("MED")) {
                	if(!MEDcourses.containsKey(course[4])) {
                		MEDcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		MEDcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		MEDcourses.get(course[4]).add(course[1]);
                		MEDcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("SCI")) {
                	if(!SCIcourses.containsKey(course[4])) {
                		SCIcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		SCIcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		SCIcourses.get(course[4]).add(course[1]);
                		SCIcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }

            }
            
            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}

	public static void main(String[] args) {
		
		String csvFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\courses.csv";
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	System.out.println("start");
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] course = line.split(",");
                
                if(course[3].equals("COM")) {
                	if(!COMcourses.containsKey(course[4])) {
                		COMcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		COMcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		COMcourses.get(course[4]).add(course[1]);
                		COMcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("EBE")) {
                	if(!EBEcourses.containsKey(course[4])) {
                		EBEcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		EBEcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		EBEcourses.get(course[4]).add(course[1]);
                		EBEcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("HUM")) {
                	if(!HUMcourses.containsKey(course[4])) {
                		HUMcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		HUMcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		HUMcourses.get(course[4]).add(course[1]);
                		HUMcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("LAW")) {
                	if(!LAWcourses.containsKey(course[4])) {
                		LAWcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		LAWcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		LAWcourses.get(course[4]).add(course[1]);
                		LAWcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("MED")) {
                	if(!MEDcourses.containsKey(course[4])) {
                		MEDcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		MEDcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		MEDcourses.get(course[4]).add(course[1]);
                		MEDcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }else if (course[3].equals("SCI")) {
                	if(!SCIcourses.containsKey(course[4])) {
                		SCIcourses.put(course[4], new ArrayList(Arrays.asList(course[1])));
                		SCIcourseIDs.put(course[4], new ArrayList(Arrays.asList(course[0])));
                	}else {
                		SCIcourses.get(course[4]).add(course[1]);
                		SCIcourseIDs.get(course[4]).add(Integer.parseInt(course[0]));
                	}
                }

            }
            
            System.out.println("Done");
            for (Entry<String, ArrayList<String>> pair : COMcourses.entrySet()) {
            	System.out.println(pair.getKey() + " "+ pair.getValue().toString());
        	}
            for (Entry<String, ArrayList<Integer>> pair : COMcourseIDs.entrySet()) {
            	System.out.println(pair.getKey() + " "+ pair.getValue().toString());
        	}

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        

	}

}
