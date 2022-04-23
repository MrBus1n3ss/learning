/*
		Created by: John Richardson
	Student number: 000605735
			 email: jric138@wgu.edu
			 class: C867
*/
#include "degree.h"
#include "student.h"


class SoftwareStudent : public Student
{
public:
	//Constructors
	SoftwareStudent();
	SoftwareStudent(
		std::string initStudentId,
		std::string initFirstName,
		std::string initLastName,
		std::string initEmail,
		int initAge,
		int daysToComplete[3],
		Degree studentDegree
	);

	// Pass from the super class
	void print();
	Degree getDegreeProgram();

	//Deconstructor
	~SoftwareStudent();
};