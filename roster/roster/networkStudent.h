/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#include "degree.h"
#include "student.h"


class NetworkStudent : public Student
{
public:
	//Constructors
	NetworkStudent();
	NetworkStudent(
		std::string StudentId,
		std::string FirstName,
		std::string LastName,
		std::string Email,
		int Age,
		int daysToComplete[3],
		Degree studentDegree
	);

	// Pass from the super class
	void print();
	Degree getDegreeProgram();

	//Deconstructor
	~NetworkStudent();
};