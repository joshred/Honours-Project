import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import DataGenerator.CourseListBuilder;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {
	
	private boolean yearIn, markIn, majorIn, courseIn, dbIn, choiceIn;

	private JFrame frame;
	private JTextField inputMarkField;
	private JTextArea courses;
	private JCheckBox constraintCheckBox;
	private JCheckBox predictCheckBox;
	JLabel feedback;
	
	Driver driver;
	
	static String curriculum = null;
	static String[] inputMajor = null;
	static String major1, major2 = null;
	static String nextYear = null;
	static int[] inputMarks = null;
	static String databaseType = null;
	static String choice = null;
	
	static ArrayList<String> majors;
	static ArrayList<String> m1;
	static ArrayList<String> m2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		CourseListBuilder.getCourses();
		Collections.sort(CourseListBuilder.ALLmajors);
		m1 = new ArrayList<String>(CourseListBuilder.ALLmajors);
		m2 = new ArrayList<String>(CourseListBuilder.ALLmajors);
		m1.add(0, "Major 1");
		m2.add(0, "Major 2");
		//cscCourses = new String[8];
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
	
	public void getInput() throws SQLException {
		System.out.println(yearIn + " " + markIn + " " + majorIn + " " + courseIn + " " + dbIn + " " + choiceIn + " ");
		if(yearIn == false ||  markIn == false || majorIn == false || courseIn == false || dbIn == false || choiceIn == false) {
			JOptionPane.showMessageDialog(null, "Some fields missing", "Error", JOptionPane.INFORMATION_MESSAGE);
		}else {
			//get curriculum
			String[] y1 = courses.getText().replace(" ", "").split(",");
			ArrayList<String> stringCurriculum = new ArrayList<String>();
			stringCurriculum.addAll(Arrays.asList(y1));
	
			curriculum = Utils.formatCurriculum(stringCurriculum);
			//get major
	//		if(major1 == null || major2 == null) {
	//			major1 = null; major2 = null;
	//		}
	//		
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
			
			System.out.println(curriculum + " " +  major1+ " " +  major2+ " " +  nextYear+ " " +  inputMarks+ " " +  databaseType+ " " +  choice);
			driver = new Driver(curriculum, major1, major2, nextYear, inputMarks, databaseType, choice);
			driver.start();
			JOptionPane.showMessageDialog(null, driver.getReport(), "Report", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void runQueries() {
		try {
			driver.start();
			JOptionPane.showMessageDialog(null, driver.getReport(), "Report", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 500);
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
		inputMarkField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				markIn = true;
			}
		});
		inputMarkField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
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
					yearIn = true;
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
					yearIn = true;
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
					yearIn = true;
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
		
		JComboBox<String> major1Box = new JComboBox<String>(new Vector<String>(m1));
		major1Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				major1 = major1Box.getSelectedItem().toString();
				majorIn = true;
			}
		});
		major1Box.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(major1Box, "4, 6, fill, default");
		
		JComboBox<String> major2Box = new JComboBox<String>(new Vector<String>(m2));
		major2Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				major2 = major2Box.getSelectedItem().toString();
				majorIn = true;
			}
		});
		major2Box.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(major2Box, "6, 6, fill, default");
		
		JLabel lblSelectYourFirst = new JLabel("(4) Enter your first year courses");
		lblSelectYourFirst.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(lblSelectYourFirst, "2, 8, left, top");
		
		courses = new JTextArea();
		courses.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				courseIn = true;
			}
		});
		courses.setFont(new Font("Tahoma", Font.PLAIN, 18));
		courses.setLineWrap(true);
		courses.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(courses);
		//this.add(scroll);
		frame.getContentPane().add(scroll, "4, 8, 5, 7, fill, default");
		courses.setColumns(10);
		
		JLabel courseExampleLabel = new JLabel("e.g. CSC1015F, MAM1000W, STA1006S...");
		courseExampleLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(courseExampleLabel, "2, 10, right, default");
		
		JLabel dbChoice = new JLabel("(5) Select your database");
		dbChoice.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(dbChoice, "2, 16, left, default");
		
		JRadioButton Neo4jSRadioButton = new JRadioButton("Neo4j (Small)");
		Neo4jSRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseType = "Neo4jSmall";
				dbIn = true;
			}
		});
		Neo4jSRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(Neo4jSRadioButton, "4, 16");
		
		JRadioButton MySQLRadioButton = new JRadioButton("MySQL (Small)");
		MySQLRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseType = "MySQL";
				dbIn = true;
			}
		});
		MySQLRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(MySQLRadioButton, "6, 16");
		
		JRadioButton Neo4jLRadioButton = new JRadioButton("Neo4j (Large)");
		Neo4jLRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseType = "Neo4jLarge";
				dbIn = true;
			}
		});
		Neo4jLRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(Neo4jLRadioButton, "8, 16");
		
		ButtonGroup dbgroup = new ButtonGroup();
		dbgroup.add(Neo4jSRadioButton);
		dbgroup.add(MySQLRadioButton);
		dbgroup.add(Neo4jLRadioButton);
		
		JLabel serviceLabel = new JLabel("(6) Select your service");
		serviceLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(serviceLabel, "2, 18");
		
		constraintCheckBox = new JCheckBox("Constraint Checking");
		constraintCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choiceIn = true;
			}
		});
		constraintCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(constraintCheckBox, "4, 18");
		
		predictCheckBox = new JCheckBox("Grade Prediction");
		predictCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choiceIn = true;
			}
		});
		predictCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(predictCheckBox, "6, 18");
		
		JButton generate = new JButton("Generate Report");
		generate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					getInput();
				} catch (SQLException e) {
					System.out.println("Couldn't create a db driver");
					e.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(generate, "6, 20");
		
		feedback = new JLabel("");
		feedback.setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.getContentPane().add(feedback, "4, 22, 5, 1, fill, default");
	}

}
