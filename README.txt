All source code and csv files required for the databases can be found at:
https://github.com/joshred/Honours-Project

//////////////////////////////////////////////////////////////////////////////////////
For v1 (smaller) databases:

Using the csv's in "Small csv.zip" load the data in MySQL
Use the following commands to load the data in Neo4j:

** Student Graph **

CREATE CONSTRAINT ON (student: Student) ASSERT student.StudentID IS UNIQUE
CREATE CONSTRAINT ON (subject: Subject) ASSERT subject.SubjectID IS UNIQUE
CREATE CONSTRAINT ON (course: Course) ASSERT course.CourseID IS UNIQUE

CREATE INDEX ON :Course(CourseName) // fast lookups on name
CREATE INDEX ON :Subject(SubjectName)

LOAD CSV WITH HEADERS FROM "file:///Students.csv" AS csvLine
CREATE (s:Student {StudentID: toInteger(csvLine.StudentID)})

LOAD CSV WITH HEADERS FROM "file:///Subjects.csv" AS csvLine
CREATE (sub:Subject {SubjectID: toInteger(csvLine.SubjectID), SubjectName: csvLine.SubjectName})

LOAD CSV WITH HEADERS FROM "file:///Courses.csv" AS csvLine
CREATE (c:Course {CourseID: toInteger(csvLine.CourseID), CourseName: csvLine.CourseName, Faculty: csvLine.Faculty, Credits: toInteger(csvLine.Credits)})

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///HSMarks.csv" AS csvLine
MATCH (s:Student {StudentID: toInteger(csvLine.StudentID)}), (sub: Subject {SubjectID: toInteger(csvLine.SubjectID)})
CREATE (s)-[:ENROLLED_IN {Mark: toInteger(csvLine.Mark)}]->(sub)

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///UCTMarks.csv" AS csvLine
MATCH (s:Student {StudentID: toInteger(csvLine.StudentID)}), (c: Course {CourseID: toInteger(csvLine.CourseID)})
CREATE (s)-[:ENROLLED_IN {Mark: toInteger(csvLine.Mark), Year: csvLine.Year}]->(c)

** Constraint Graph **
create (_1: Decision: Question {name: "4 Half 1st Year Courses?", code: "FB3.a"})
create (_2: Decision: Question {name: "3 Half 2nd 3rd Year Courses?", code: "FB3.b"})
create (_3: Decision: Question {name: "9 Full Courses?", code: "FB7.1.1", start: 1})
create (_4: Decision: Question {name: "6 Science Full Courses?", code: "FB7.1.2"})
create (_5: Decision: Question {name: "4 Full Senior Courses?", code: "FB7.2.1"})
create (_6: Decision: Question {name: "3 Full senior Science Courses?", code: "FB7.2.2"})
create (_7: Decision: Question {name: "2 Full 3rd Year Courses?", code: "FB7.2.3"})
create (_9: Decision: Question {name: "420 NQF Credits?", code: "FB7.7a"})
create (_10: Decision: Question {name: "276 NQF Science Credits?", code: "FB7.7b"})
create (_11: Decision: Question {name: "120 3rd Year NQF Credits?", code: "FB7.7c"})
create (_a: Decision: Question {name: "72 Credits per 1st Year Semester?", code: "FB3.a1"})
create (_b: Decision: Question {name: "72 Credits per 2nd, 3rd Year Semester?", code: "FB3.b1"})

create (_t: Decision: Terminal {name: "Meets Requirements"})
create (_f: Decision: Terminal {name: "Does Not Meet Requirements"})

create (_3)-[:Decision {direction: "left", value: 9, parameter: "fullcourses", description: "Too few full courses", answer: "No"}]->(_f)
create (_3)-[:Decision {direction: "right", value: 9, parameter: "fullcourses", description: "Enough full courses", answer: "Yes"}]->(_4)
create (_4)-[:Decision {direction: "left", value: 6, parameter: "sciFullCourses", description: "Too few science full courses", answer: "No"}]->(_f)
create (_4)-[:Decision {direction: "right", value: 6, parameter: "sciFullCourses", description: "Enough science full courses", answer: "Yes"}]->(_5)
create (_5)-[:Decision {direction: "left", value: 4, parameter: "seniorFullCourses", description: "Too few full senior courses", answer: "No"}]->(_f)
create (_5)-[:Decision {direction: "right", value: 4, parameter: "seniorFullCourses", description: "Enough full senior courses", answer: "Yes"}]->(_6)
create (_6)-[:Decision {direction: "left", value: 3, parameter: "seniorSciFullCourses", description: "Too few senior science full courses", answer: "No"}]->(_f)
create (_6)-[:Decision {direction: "right", value: 3, parameter: "seniorSciFullCourses", description: "Enough senior science full courses", answer: "Yes"}]->(_7)
create (_7)-[:Decision {direction: "left", value: 2, parameter: "thirdSciFullFourses", description: "Too few 3rd year full courses", answer: "No"}]->(_f)
create (_7)-[:Decision {direction: "right", value: 2, parameter: "thirdSciFullFourses", description: "Enough 3rd year full courses", answer: "Yes"}]->(_1)
create (_1)-[:Decision {direction: "left", value: 5, parameter: "firstYearHalfCourses", description: "Enough 1st year half courses", answer: "Yes"}]->(_2)
create (_1)-[:Decision {direction: "right", value: 5, parameter: "firstYearHalfCourses", description: "Too many first year half courses", answer: "No"}]->(_a)
create (_a)-[:Decision {direction: "left", value: 73, parameter: "firstYearHalfCoursesNQF", description: "Enough 1st year half course credits", answer: "Yes"}]->(_2)
create (_a)-[:Decision {direction: "right", value: 73, parameter: "firstYearHalfCoursesNQF", description: "Too many first year half course credits", answer: "No"}]->(_f)
create (_2)-[:Decision {direction: "left", value: 4, parameter: "seniorYearHalfCourses", description: "Enough senior half courses", answer: "Yes"}]->(_9)
create (_2)-[:Decision {direction: "right", value: 4, parameter: "seniorYearHalfCourses", description: "Too many senior half courses", answer: "No"}]->(_b)
create (_b)-[:Decision {direction: "left", value: 73, parameter: "seniorYearHalfCoursesNQF", description: "Enough 2nd year half course credits", answer: "Yes"}]->(_9)
create (_b)-[:Decision {direction: "right", value: 73, parameter: "seniorYearHalfCoursesNQF", description: "Too many senior year half course credits", answer: "No"}]->(_f)
create (_9)-[:Decision {direction: "right", value: 420, parameter: "totalNQF", description: "Enough NQF Credits", answer: "Yes"}]->(_10)
create (_9)-[:Decision {direction: "left", value: 420, parameter: "totalNQF", description: "Too few NQF Credits", answer: "No"}]->(_f)
create (_10)-[:Decision {direction: "right", value: 276, parameter: "sciNQF", description: "Enough NQF Science Credits", answer: "Yes"}]->(_11)
create (_10)-[:Decision {direction: "left", value: 276, parameter: "sciNQF", description: "Too few NQF ScienceCredits", answer: "No"}]->(_f)
create (_11)-[:Decision {direction: "right", value: 120, parameter: "thirdNQF", description: "Enough NQF 3rd Year Credits", answer: "Yes"}]->(_t)
create (_11)-[:Decision {direction: "left", value: 120, parameter: "thirdNQF", description: "Too few NQF 3rd Year Credits", answer: "No"}]->(_f)

match (csc:Major {MajorCode: "CSC"})
merge (csc)-[:REQUIRES {type: "Compulsory"}]->(:Course {CourseID: "CSC1015F"})
merge (csc)-[:REQUIRES {type: "Compulsory"}]->(:Course {CourseName: "CSC1016S"})
merge (csc)-[:REQUIRES {type: "Compulsory"}]->(:Course {CourseName: "CSC2001F"})
merge (csc)-[:REQUIRES {type: "Compulsory"}]->(:Course {CourseName: "CSC2002S"})
merge (csc)-[:REQUIRES {type: "Compulsory"}]->(:Course {CourseName: "INF2009F"})
merge (csc)-[:REQUIRES {type: "Compulsory"}]->(:Course {CourseName: "CSC3002F"})
merge (csc)-[:REQUIRES {type: "Compulsory"}]->(:Course {CourseName: "CSC3003S"})

merge (csc)-[:REQUIRES {type: "Alternate", Combination: 1}]->(:Course {CourseName: "MAM1004F"})
merge (csc)-[:REQUIRES {type: "Alternate", Combination: 1}]->(:Course {CourseName: "MAM1008S"})
merge (csc)-[:REQUIRES {type: "Alternate", Combination: 2}]->(:Course {CourseName: "MAM1000W"})

/////////////////////////////////////////////////////////////////////////////////////
To create the v2 (bigger) databases, use the "Big csv.zip" and the following commands:

in NEO4j:
Create in the following order:
1) courses then subjects then majors
2) roots then roots-> major
3) students then students-> root
4) student -mark-> subject (grades)
5) student -mark-> course (marks)

CREATE CONSTRAINT ON (student: Student) ASSERT student.StudentID IS UNIQUE
CREATE CONSTRAINT ON (subject: Subject) ASSERT subject.SubjectID IS UNIQUE
CREATE CONSTRAINT ON (course: Course) ASSERT course.CourseID IS UNIQUE
CREATE CONSTRAINT ON (major: Major) ASSERT major.MajorID IS UNIQUE
CREATE INDEX ON :Course(CourseName) // fast lookups on name
CREATE INDEX ON :Subject(SubjectName)
CREATE INDEX ON :Major(MajorCode)

//ADD
CREATE INDEX ON :Subject(SubjectName)
CREATE INDEX ON :Student:Root(CurriculumHash)

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///courses.csv" AS csvLine
CREATE (c:Course {CourseID: toInteger(csvLine.courseID), CourseName: csvLine.courseName, CourseDescr: csvLine.CourseDescr, Faculty: csvLine.faculty, Department: csvLine.department, NQFCredits: toInteger(csvLine.NQFCredits), Year: toInteger(csvLine.year), Semester: toInteger(csvLine.semester)})

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///majors.csv" AS csvLine
CREATE (m:Major {MajorID: toInteger(csvLine.MajorID), MajorCode: csvLine.MajorCode, Faculty: csvLine.Faculty})

//add subjects

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///roots.csv" AS csvLine
CREATE (root:Student:Root {StudentID: toInteger(csvLine.StudentID), Major1:csvLine.Major1, Major2:csvLine.Major2, CurriculumHash: toInteger(csvLine.CurriculumHash), CurriculumSize:toInteger(csvLine.CurriculumSize) })

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///roots.csv" AS csvLine
MATCH (s:Student:Root {StudentID: toInteger(csvLine.StudentID)}), (m1: Major {MajorCode: csvLine.Major1}), (m2: Major {MajorCode: csvLine.Major2})
CREATE (m1)<-[:TOOK]-(s)-[:TOOK]->(m2)

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///students.csv" AS csvLine
CREATE (s:Student {StudentID: toInteger(csvLine.StudentID), Major1: csvLine.Major1, Major2: csvLine.Major2})

//subjects
CALL apoc.periodic.iterate(
"LOAD CSV FROM 'file:///grades.csv' AS csvLine return csvLine",
"match (s:Student {StudentID: toInteger(csvLine[0])})
with s, csvLine 
match (subject: Subject {SubjectName: csvLine[1]})
create (s)-[:ENROLLED_IN {Mark: toInteger(csvLine[2])}]->(subject)", {batchSize:1000, iterateList:true});

//courses
CALL apoc.periodic.iterate(
"LOAD CSV FROM 'file:///marks.csv' AS csvLine return csvLine",
"match (s:Student {StudentID: toInteger(csvLine[0])})
with s, csvLine 
match (course: Course {CourseName: csvLine[1]})
create (s)-[:ENROLLED_IN {Mark: toInteger(csvLine[2])}]->(course)", {batchSize:1000, iterateList:true});

The prerequisites and constraint graph are inserted the same as above

/////////////////////////////////////////////////////////////////////////////////////////////////////////
in MySQL:

//create tables beforehand: courses, subjects, majors, students, marks grades
//no pk in marks or grades

//set local infile to true:

in cmd:
--local-infile=1 -uroot -ppassword "big uct data"

in workbench:
SET GLOBAL local_infile = 'ON'; 
    
SHOW VARIABLES LIKE 'local_infile';
SHOW VARIABLES LIKE "secure_file_priv";

put csvs in programdata folder:

run the following for each table & corresponding csv within cmd (for each table):

LOAD DATA LOCAL INFILE "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\marks.csv"    
INTO TABLE marks    
FIELDS TERMINATED by ', '   
LINES TERMINATED BY '\n'    
IGNORE 1 LINES;