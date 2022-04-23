/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#include "securityStudent.h"

// Constructor
SecurityStudent::SecurityStudent() :Student()
{
	studentDegree = Degree::SECURITY;
}
SecurityStudent::SecurityStudent(
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
	studentDegree = Degree::SECURITY;
}
// ---------------------------------------------------------|
// Prints student and Derived class print statement
void SecurityStudent::print()
{
	this->Student::print();
	std::cout << "Security" << std::endl;
}
// ---------------------------------------------------------|
Degree SecurityStudent::getDegreeProgram() { return Degree::SECURITY; }