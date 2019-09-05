import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import DataGenerator.CourseListBuilder;

import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class GUI {

	private JFrame frame;
	private JTextField inputMarkField;
	private JTextField y1courses;
	private JCheckBox constraintCheckBox;
	private JCheckBox predictCheckBox;
	
	static String[] inputCurriculum = null;
	static String[] inputMajor = null;
	static String major1, major2 = null;
	static String nextYear = null;
	static int[] inputMarks = null;
	static String databaseType = null;
	static String choice = null;
	
	static ArrayList<String> majors;
	static ArrayList<String> m1;
	static ArrayList<String> m2;
	static ArrayList<String> courses;
	private JTextField y2courses;
	private JTextField y3courses;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		CourseListBuilder.getCourses();
		m1 = new ArrayList<String>(CourseListBuilder.ALLmajors);
		m2 = new ArrayList<String>(CourseListBuilder.ALLmajors);
		m1.add(0, "Major 1");
		m2.add(0, "Major 2");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	
	public void getInput() {
		//get curriculum
		String[] y1 = y1courses.getText().split(",");
		String[] y2 = y2courses.getText().split(",");
		String[] y3 = y3courses.getText().split(",");
		ArrayList<String> stringCurriculum = new ArrayList<String>();
		stringCurriculum.addAll(Arrays.asList(y1));
		stringCurriculum.addAll(Arrays.asList(y2));
		stringCurriculum.addAll(Arrays.asList(y3));
		inputCurriculum = stringCurriculum.toArray(new String[0]);
		
		//get major
		if(major1.equals("Major 1") || major2.equals("Major 2")) {
			major1 = null; major2 = null;
		}
		
		//get next Year
		if(nextYear.equals("1")) {
			inputMarks = new int[3];
		}else {
			inputMarks = new int[1];
		}
		
		//get marks
		String[] stringMarks = inputMarkField.getText().split(",");
		System.out.println(Arrays.toString(stringMarks));
		for(int i = 0 ; i <stringMarks.length; i++) {
			inputMarks[i] = Integer.parseInt(stringMarks[i]);
		}
		
		//get db type
		
		//get choice
		if(constraintCheckBox.isSelected()) {
			choice = "1";
		}
		if(predictCheckBox.isSelected()) {
			choice = "2";
		}
		if(constraintCheckBox.isSelected() && predictCheckBox.isSelected()) {
			choice = "3";
		}
		
		System.out.println(inputCurriculum.toString() + "\r\n" + major1 + " " + major2 + nextYear.toString() + "\r\n" + Arrays.asList(inputMarks).toString() +"\r\n" + databaseType + "\r\n" + choice);
		
		if(nextYear == null || inputMarks == null || major1 == null || major2 == null || inputCurriculum == null
			|| databaseType == null || choice == null) {
			System.out.println("not done");
			JOptionPane.showMessageDialog(null, "Some Fields Missing");
		}else if (nextYear != null && inputMarks != null && inputMajor != null && inputCurriculum != null
				&& databaseType != null && choice != null) {
			System.out.println("done");
			System.out.println(inputCurriculum + "\r\n" + major1 + " " + major2 + nextYear + "\r\n" + inputMarks +"\r\n" + databaseType + "\r\n" + choice);
			//Driver driver = new Driver(inputCurriculum, major1, major2, nextYear, inputMarks, databaseType, choice);
//			try {
//				driver.start();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1010, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel yearChoice = new JLabel("(1) Which year are you registering for?");
		yearChoice.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(yearChoice, "2, 2, left, default");
		
		JLabel inputMarkLabel = new JLabel("(2) Enter your marks");
		inputMarkLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(inputMarkLabel, "2, 4, left, default");
		
		inputMarkField = new JTextField();
		inputMarkField.setEditable(true);
		inputMarkField.setText("");
		frame.getContentPane().add(inputMarkField, "4, 4, fill, default");
		inputMarkField.setColumns(10);
		
		JRadioButton firstRadioButton = new JRadioButton("First Year");
		firstRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(firstRadioButton.isSelected()) {
					inputMarkLabel.setText("(2) Enter your NBT marks e.g. 65, 75, 50");
					nextYear = "1";
				}
			}
		});
		firstRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(firstRadioButton, "4, 2, fill, default");
		
		JRadioButton secondRadioButton = new JRadioButton("Second Year");
		secondRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(secondRadioButton.isSelected()) {
					inputMarkLabel.setText("(2) Enter your 1st year GPA e.g. 65");
					nextYear = "2";
				}
			}
		});
		secondRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(secondRadioButton, "6, 2, fill, default");
		
		JRadioButton thirdRadioButton = new JRadioButton("Third Year");
		thirdRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(thirdRadioButton.isSelected()) {
					inputMarkLabel.setText("(2) Enter your 2nd year GPA e.g. 65");
					nextYear = "3";
				}
			}
		});
		thirdRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(thirdRadioButton, "8, 2, fill, top");
		
		ButtonGroup yeargroup = new ButtonGroup();
		yeargroup.add(firstRadioButton);
		yeargroup.add(secondRadioButton);
		yeargroup.add(thirdRadioButton);
		
		JLabel majorLabel = new JLabel("(3) Select the department of your major(s)");
		majorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(majorLabel, "2, 6, left, center");
		
		JComboBox major1Box = new JComboBox(new Vector(m1));
		major1Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				major1 = major1Box.getSelectedItem().toString();
			}
		});
		major1Box.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(major1Box, "4, 6, fill, default");
		
		JComboBox major2Box = new JComboBox(new Vector(m2));
		major2Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				major2 = major2Box.getSelectedItem().toString();
			}
		});
		major2Box.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(major2Box, "6, 6, fill, default");
		
		JLabel lblSelectYourFirst = new JLabel("(4.1) Enter your first year courses");
		lblSelectYourFirst.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblSelectYourFirst, "2, 8, left, top");
		
		y1courses = new JTextField();
		y1courses.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(y1courses, "4, 8, fill, default");
		y1courses.setColumns(10);
		
		JLabel courseExampleLabel = new JLabel("e.g. CSC1015F, MAM1000W, STA1006S...");
		courseExampleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(courseExampleLabel, "6, 8, 3, 1, fill, default");
		
		JLabel lblSelectYourSecond = new JLabel("(4.2) Enter your second year courses");
		lblSelectYourSecond.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelectYourSecond.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblSelectYourSecond, "2, 10, left, default");
		
		y2courses = new JTextField();
		y2courses.setFont(new Font("Tahoma", Font.PLAIN, 18));
		y2courses.setColumns(10);
		frame.getContentPane().add(y2courses, "4, 10, fill, default");
		
		JLabel lblSelectYourThird = new JLabel("(4.3) Enter your third year courses");
		lblSelectYourThird.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblSelectYourThird, "2, 12, left, default");
		
		y3courses = new JTextField();
		y3courses.setFont(new Font("Tahoma", Font.PLAIN, 18));
		y3courses.setColumns(10);
		frame.getContentPane().add(y3courses, "4, 12, fill, default");
		
		JLabel dbChoice = new JLabel("(5) Select your database");
		dbChoice.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(dbChoice, "2, 14, left, default");
		
		JRadioButton Neo4jSRadioButton = new JRadioButton("Neo4j (Small)");
		Neo4jSRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseType = "Neo4jSmall";
			}
		});
		Neo4jSRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(Neo4jSRadioButton, "4, 14");
		
		JRadioButton MySQLRadioButton = new JRadioButton("MySQL (Small)");
		MySQLRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseType = "MySQL";
			}
		});
		MySQLRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(MySQLRadioButton, "6, 14");
		
		JRadioButton Neo4jLRadioButton = new JRadioButton("Neo4j (Large)");
		Neo4jLRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseType = "Neo4jLarge";
			}
		});
		Neo4jLRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(Neo4jLRadioButton, "8, 14");
		
		ButtonGroup dbgroup = new ButtonGroup();
		dbgroup.add(Neo4jSRadioButton);
		dbgroup.add(MySQLRadioButton);
		dbgroup.add(Neo4jLRadioButton);
		
		JLabel serviceLabel = new JLabel("(6) Select your service");
		serviceLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(serviceLabel, "2, 16");
		
		constraintCheckBox = new JCheckBox("Constraint Checking");
		constraintCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		constraintCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(constraintCheckBox, "4, 16");
		
		predictCheckBox = new JCheckBox("Grade Prediction");
		predictCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(predictCheckBox, "6, 16");
		
		JButton generate = new JButton("Generate Report");
		generate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getInput();
				
			}
		});
		frame.getContentPane().add(generate, "6, 18");
		
		JLabel report = new JLabel("Report");
		frame.getContentPane().add(report, "4, 20, 5, 7");
	}

}
