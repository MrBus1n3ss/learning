/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#pragma once

#include <iostream>
#include <string>


#include "degree.h"
#include "student.h"
#include "securityStudent.h"
#include "softwareStudent.h"
#include "networkStudent.h"

void errorMessage(int errorNumber);  //prints errors


class Roster {
public:
	int static const NUM_DAY_ARRAY_SIZE = 3;
	//Constructor
	Roster();
	Roster(int index);

	void parseThenAdd(std::string row); //parses the table then sends to the add
	//Adds a student
	void add(
		std::string studentID,
		std::string firstName,
		std::string lastName,
		std::string emailAddress,
		int age,
		int daysInCourse1,
		int daysInCourse2,
		int daysInCourse3,
		Degree degreeProgram
	);
	void remove(std::string studentID); //removes a student
	void printAll(); //prints all students
	void printDaysInCourse(std::string studentID); //Prints students days in course
	void printInvalidEmails(); //Finds invalid emails
	void printByDegreeProgram(int degreeProgram); //Prints Students by Degree

	//Deconstructor
	~Roster();
private:
	Student** classRoster;
	int capacity;
	int lastIndex;
};
