/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#pragma once
#include <iostream>
#include <iomanip>
#include <string>

#include "degree.h"


class Student {
public:
	//Constants
	int static const NUM_DAY_ARRAY_SIZE = 3;

	//Constructors
	Student();
	Student(
		std::string studentId,
		std::string firstName,
		std::string lastName,
		std::string email,
		int age,
		int daysToComplete[]
	);

	// Setters
	void setStudentId(std::string studentId);
	void setFirstName(std::string firstName);
	void setLastName(std::string lastName);
	void setEmail(std::string email);
	void setAge(int age);
	void setDaysToComplete(int daysToComplete[]);

	// Getters
	std::string getStudentId();
	std::string getFirstName();
	std::string getLastName();
	std::string getEmail();
	int getAge();
	int* getDaysToComplete();

	// Virtual
	virtual Degree getDegreeProgram() = 0;
	virtual void print() = 0;

	// Deconstructor
	~Student();

	// Allows for the sub classes to get to these
protected:
	std::string studentId;
	std::string firstName;
	std::string lastName;
	std::string email;
	int age;
	int daysToComplete[3];
	Degree studentDegree;

};
