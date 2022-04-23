/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#include "softwareStudent.h"

//Constructors 
SoftwareStudent::SoftwareStudent() :Student()
{
	studentDegree = Degree::SOFTWARE;
}

SoftwareStudent::SoftwareStudent(
	std::string studentId,
	std::string firstName,
	std::string lastName,
	std::string email,
	int age,
	int daysToComplete[],
	Degree studentDegree
) : Student(
	studentId,
	firstName,
	lastName,
	email,
	age,
	daysToComplete
)
{
	studentDegree = Degree::SOFTWARE;
}
// ---------------------------------------------------------|
// Prints student and Derived class print statement
void SoftwareStudent::print()
{
	this->Student::print();
	std::cout << "Software" << std::endl;
}
// ---------------------------------------------------------|

Degree SoftwareStudent::getDegreeProgram() { return Degree::SOFTWARE; }