/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#include "networkStudent.h"

// Constructor
NetworkStudent::NetworkStudent() :Student()
{
	studentDegree = Degree::NETWORK;
}

NetworkStudent::NetworkStudent(
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
	studentDegree = Degree::NETWORK;
}
// ---------------------------------------------------------|
// Prints student and Derived class print statement
void NetworkStudent::print()
{
	this->Student::print();
	std::cout << "Network" << std::endl;
}
// ---------------------------------------------------------|
Degree NetworkStudent::getDegreeProgram() { return Degree::NETWORK; }
