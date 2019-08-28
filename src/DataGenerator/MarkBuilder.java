package DataGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MarkBuilder {
	
	private static AtomicLong idCounter = new AtomicLong();
	
	static Random rand = new Random();
	
	static String[] NBTnames = {"NBTAL", "NBTM", "NBTQL"};
	
	public static ArrayList<String> toList(String in){
		String withoutBrackets = in.replace("[", "").replace("]", "").replace("$", ","); // remove brackets and whitespace
		ArrayList<String> newList = new ArrayList<String>(Arrays.asList(withoutBrackets.split(",")));
		//System.out.println(in);
		//System.out.println(newList);
		newList.remove(new String(""));
		return newList;
	}

	public static String createID()
	{
	    return String.valueOf(idCounter.getAndIncrement());
	}
	
	public static void writeFile(String fileName, String content) {

	    try (Writer writer = new BufferedWriter(new FileWriter(fileName, true)))
	    {
	        writer.append((String) content+"\r\n");
	        writer.close();
	    } catch (UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    } catch (FileNotFoundException e)
	    {
	        e.printStackTrace();
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public static void getStudents() throws FileNotFoundException, IOException {
		String csvFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\studentClusters.csv";
		
		String studentsFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\students.csv";
		String rootsFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\roots.csv";
		String marksFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\marks.csv";
		String gradesFile = "C:\\Users\\Josh\\Documents\\UCT\\Honours\\Project\\Bonus DB\\grades.csv";

        
		String line = "";

        writeFile(studentsFile, "StudentID, Major1, Major2, CurriculumHash, CurriculumSize");
        writeFile(rootsFile, "StudentID, Major1, Major2, CurriculumHash, CurriculumSize");
        writeFile(marksFile, "StudentID, CourseName, Mark");
        writeFile(gradesFile, "StudentID, NBTName, Mark");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	System.out.println("start");
        	//line = br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] cluster = line.replace(" ", "").split(",");
                int numStudents = Integer.parseInt(cluster[0]);
                System.out.println(numStudents);
                String major1 = cluster[1];
                String major2 = cluster[2];
                String curriculumHash = cluster[3];
                int curriculumSize = Integer.parseInt(cluster[4]);
                ArrayList<String> curriculum = toList(cluster[5]);
                
                //make all students in cluster
                for(int i = 0; i <numStudents; i ++) {
                	//studentsWriter = new BufferedWriter(new FileWriter(studentsFile));
                	String studentID = createID();
                	if(i == 0) {
                		writeFile(rootsFile, studentID + ", " + major1 + ", " + major2 + ", " + curriculumHash + ", " + curriculumSize);
                	}else {
                		//studentsWriter.append(studentID + ", " + major1 + ", " + major2 + ", " + curriculumHash + ", " + curriculumSize);
                		//studentsWriter.close();
                		writeFile(studentsFile, studentID + ", " + major1 + ", " + major2 + ", " + curriculumHash + ", " + curriculumSize);
                	}
                	
                	float mark1 = rand.nextFloat();
                	float mark2 = rand.nextFloat();
                	int min1 = 0; int min2 = 0;
                	int max1 = 0; int max2 = 0;
                	
                	if(mark1 < 0.34) {
                		min1 = 0;
                		max1 = 51;
                	}else if(mark1 < 0.55) {
                		min1 = 51;
                		max1 = 61;
                	}else if(mark1 < 0.84) {
                		min1 = 61;
                		max1 = 71;
                	}else if (mark1 < 0.98) {
                		min1 = 71;
                		max1 = 81;
                	}else {
                		min1 = 81;
                		max1 = 101;
                	}
                	
                	if(mark2 < 0.22) {
                		min2 = 0;
                		max2 = 51;
                	}else if(mark2 < 0.37) {
                		min2 = 51;
                		max2 = 61;
                	}else if(mark2 < 0.57) {
                		min2 = 61;
                		max2 = 71;
                	}else if (mark2 < 0.81) {
                		min2 = 71;
                		max2 = 81;
                	}else if (mark2 < 0.96){
                		min2 = 81;
                		max2 = 91;
                	}else {
                		min2 = 91;
                		max2 = 101;
                	}
                	
                	//add marks
                	for(int j = 0; j < curriculumSize; j++) {
                		int mark = min1 + rand.nextInt(max1 - min1);
                		writeFile(marksFile, studentID + ", " + curriculum.get(j) + ", " + mark);
                	}
                	
                	//add NBTs
                	for(int k = 0; k <3; k++) {
                		int mark = min2 + rand.nextInt(max2 - min2);
                		writeFile(gradesFile, studentID + ", " + NBTnames[k] + ", " + mark);
                	}
                }
                
            }
        }
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		getStudents();
		System.out.println("Done");

	}

}
