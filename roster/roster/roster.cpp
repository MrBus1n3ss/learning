/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#include "roster.h"


// Constructors ---------------------------------------------|
Roster::Roster()
{
	this->capacity = 0;
	this->lastIndex = -1;
	this->classRoster = nullptr;
}


Roster::Roster(int capacity)
{
	this->capacity = capacity;
	this->lastIndex = -1;
	this->classRoster = new Student * [capacity];
}
// ---------------------------------------------------------|
/*
parseTableInfo[0] Student ID
parseTableInfo[1] First Name
parseTableInfo[2] Last Name
parseTableInfo[3] Email
parseTableInfo[4] Age
parseTableInfo[5] Number of Days 1
parseTableInfo[6] Number of Days 2
parseTableInfo[7] Number of Days 3
parseTableInfo[8] Degree Type

Parse the long strings then sends them to Roster::add
*/
void Roster::parseThenAdd(std::string row)
{
	std::string parseTableInfo[9]; // Create an Array to store the values in
	int parseTableCount = 0; // Index for the Array
	Degree degreeType; // Enum for Degree Type
	int pos = 0; // Position of the comma
	if (lastIndex < capacity)
	{
		lastIndex++;
		//Get parseTableInfo[0] and move to next comma ---------|
		pos = row.find(",");
		parseTableInfo[parseTableCount] = row.substr(0, pos);
		parseTableCount++;
		std::string newRowStr = row.substr(pos + 1);
		//------------------------------------------------------|
		//Get parseTableInfo[1] - [8] --------------------------|
		while (pos != -1)
		{
			pos = newRowStr.find(',');
			parseTableInfo[parseTableCount] = newRowStr.substr(0, pos);
			newRowStr = newRowStr.substr(pos + 1);
			parseTableCount++;
		}
		//------------------------------------------------------|
		if (parseTableInfo[8] == "SOFTWARE") degreeType = Degree::SOFTWARE;
		else if (parseTableInfo[8] == "SECURITY") degreeType = Degree::SECURITY;
		else if (parseTableInfo[8] == "NETWORK") degreeType = Degree::NETWORK;
		else std::cout << "Unknown Type" << std::endl;
		add(parseTableInfo[0],
			parseTableInfo[1],
			parseTableInfo[2],
			parseTableInfo[3],
			stoi(parseTableInfo[4]),
			stoi(parseTableInfo[5]),
			stoi(parseTableInfo[6]),
			stoi(parseTableInfo[7]),
			degreeType
		);
	}
}

//Adds a studnet to the class roster by the degree type
void Roster::add(
	std::string studentID,
	std::string firstName,
	std::string lastName,
	std::string emailAddress,
	int age,
	int daysInCourse1,
	int daysInCourse2,
	int daysInCourse3,
	Degree degreeProgram
)
{
	int daysInCorseArray[Roster::NUM_DAY_ARRAY_SIZE];
	daysInCorseArray[0] = daysInCourse1;
	daysInCorseArray[1] = daysInCourse2;
	daysInCorseArray[2] = daysInCourse3;
	switch (degreeProgram)
	{
	case Degree::SECURITY:
	{
		classRoster[lastIndex] = new SecurityStudent(
			studentID,
			firstName,
			lastName,
			emailAddress,
			age,
			daysInCorseArray,
			degreeProgram
		);
		break;
	}
	case Degree::NETWORK:
	{
		classRoster[lastIndex] = new NetworkStudent(
			studentID,
			firstName,
			lastName,
			emailAddress,
			age,
			daysInCorseArray,
			degreeProgram
		);
		break;
	}
	case Degree::SOFTWARE:
	{
		classRoster[lastIndex] = new SoftwareStudent(
			studentID,
			firstName,
			lastName,
			emailAddress,
			age,
			daysInCorseArray,
			degreeProgram
		);
		break;
	}
	default:
	{
		std::cout << "Unknown Degree" << std::endl;
		break;
	}
	}
}

//Print all students in the class roster
void Roster::printAll()
{
	if (lastIndex > 0)
	{
		for (int i = 0; i <= lastIndex; i++)
			classRoster[i]->print();
	}
	else
		errorMessage(2);
}

//Removes a student for the class roster
void Roster::remove(std::string studentID)
{
	bool isStudent = false;
	for (int i = 0; i < lastIndex; i++)
	{
		if (studentID == classRoster[i]->getStudentId())
		{
			isStudent = true;
			//classRosterArray.erase(classRosterArray.begin() + i );
			delete this->classRoster[i];
			this->classRoster[i] = this->classRoster[lastIndex];
			lastIndex--;
		}
	}
	if (isStudent == false)
	{
		errorMessage(1);
	}
	else Roster::printAll();
}

//Prints the Days in a course for a Student
void Roster::printDaysInCourse(std::string studentID)
{
	bool isStudent = false;
	for (int i = 0; i < lastIndex; i++)
	{
		if (studentID == classRoster[i]->getStudentId())
		{
			classRoster[i]->print();
			isStudent = true;
		}
	}
	if (isStudent == false)
		errorMessage(1);
}

//Note: A valid email should include an at sign ('@') and period ('.') and should not include a space (' ').
void Roster::printInvalidEmails()
{
	std::cout << std::endl << "Emails are not correctly formatted" << std::endl << std::endl;
	for (int i = 0; i < lastIndex; i++)
	{
		std::string str = classRoster[i]->getEmail();
		if (str.find('@') == std::string::npos || str.find('.') == std::string::npos || str.find(' ') != std::string::npos)
			std::cout << "Student ID: " << classRoster[i]->getStudentId() << ": " << str << std::endl;

	}
	std::cout << std::endl;
}

//Prints all by Degree type
void Roster::printByDegreeProgram(int degreeProgram)
{
	for (int i = 0; i <= lastIndex; i++)
	{
		if (this->classRoster[i]->getDegreeProgram() == (Degree)degreeProgram)
			this->classRoster[i]->print();
	}
}

//Deconstructor
Roster::~Roster()
{
	for (int i = 0; i <= lastIndex; i++)
	{
		delete this->classRoster[i];
	}
	delete classRoster;
}


//------------------------------------------------------------------------------------------------------|
// My Functions
//------------------------------------------------------------------------------------------------------|
//Error messages throughout the script
void errorMessage(int errorNumber)
{
	switch (errorNumber) {
	case 1: std::cout << "ERROR " + std::to_string(errorNumber) + ": Unable to find studentId" << std::endl;
		break;
	case 2: std::cout << "ERROR " + std::to_string(errorNumber) + ": Class Roster is empty" << std::endl;
		break;
	}
}
//------------------------------------------------------------------------------------------------------|


int main() {
	//Author Information -----------------------------------------------|
	std::string line = "----------------------------------------|";
	std::cout << std::endl << line << std::endl;
	std::cout << "    Created by: John Richardson" << std::endl;
	std::cout << "Student number: 000605735" << std::endl;
	std::cout << "         email: jric138@wgu.edu" << std::endl;
	std::cout << "         class: C867" << std::endl;
	std::cout << line << std::endl << std::endl;
	//------------------------------------------------------------------|

	int numStudents = 5;

	//Table Information ------------------------------------------------|
	const std::string studentData[] =
	{
		"A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
		"A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
		"A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
		"A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY",
		"A5,John,Richardson,jric138@wgu.edu,34,10,34,53,SOFTWARE"
	};
	//------------------------------------------------------------------|
	// Parse Table and add to Roster -----------------------------------|
	Roster* roster = new Roster(numStudents);

	for (int i = 0; i < numStudents; i++)
	{
		roster->parseThenAdd(studentData[i]);
	}
	//------------------------------------------------------------------|
	std::cout << "Print all Students" << std::endl;
	roster->printAll();
	std::cout << "Invalid Emails" << std::endl;
	roster->printInvalidEmails();
	std::cout << "Days in Corse" << std::endl;
	roster->printDaysInCourse("A3");
	std::cout << "Sort by Degree" << std::endl;
	roster->printByDegreeProgram((int)Degree::SOFTWARE);
	std::cout << "Remove Student" << std::endl;
	roster->remove("A3");
	roster->remove("A4");
	return 0;
}
