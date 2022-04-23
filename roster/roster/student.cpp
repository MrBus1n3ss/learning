/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#include "student.h"

//Constructors
Student::Student()
{
	this->studentId = "";
	this->firstName = "";
	this->lastName = "";
	this->email = "";
	this->age = 0;
	for (int i = 0; i < NUM_DAY_ARRAY_SIZE; i++) this->daysToComplete[i] = 0;
}

Student::Student(
	std::string studentId,
	std::string firstName,
	std::string lastName,
	std::string email,
	int age,
	int daysToComplete[]
)
{
	this->studentId = studentId;
	this->firstName = firstName;
	this->lastName = lastName;
	this->email = email;
	this->age = age;
	for (int i = 0; i < NUM_DAY_ARRAY_SIZE; i++) this->daysToComplete[i] = daysToComplete[i];
}

//Setters
void Student::setStudentId(std::string studentId) { this->studentId = studentId; }
void Student::setFirstName(std::string firstName) { this->firstName = firstName; }
void Student::setLastName(std::string lastName) { this->lastName = lastName; }
void Student::setEmail(std::string email) { this->email = email; }
void Student::setAge(int age) { this->age = age; }
void Student::setDaysToComplete(int daysToComplete[])
{
	for (int i = 0; i < NUM_DAY_ARRAY_SIZE; i++)
		this->daysToComplete[i] = daysToComplete[i];
}

//Getters
std::string Student::getStudentId() { return studentId; }
std::string Student::getFirstName() { return firstName; }
std::string Student::getLastName() { return lastName; }
std::string Student::getEmail() { return email; }
int Student::getAge() { return age; }
int* Student::getDaysToComplete() { return daysToComplete; }

//Print all info about a student
void Student::print()
{
	std::cout << "\t"
		"Student ID: " << studentId << "\t" <<
		"First Name: " << firstName << "\t" <<
		"Last Name: " << lastName << "\t" <<
		"Email: " << email << "\t" <<
		"Age: " << age << "\t" <<
		"Days In Corse: {" << daysToComplete[0] << ", " <<
		daysToComplete[1] << ", " <<
		daysToComplete[2] << "} " <<
		"\t";
}

//Deconstructor
Student::~Student() {}