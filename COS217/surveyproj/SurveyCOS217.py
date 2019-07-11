#!/usr/bin/env python3

"""
The Introductory Survey for COS 217
Written by Iasonas Petras
"""

import getpass # To get the username under UNIX/Windows
import sys # To handle command line arguments
from datetime import date
import re
from collections import OrderedDict # To create dictionaries that maintain order of pairs (key, value)

def main():

	# Check for proper usage of script
	if len(sys.argv) != 1:
		print("Usage: ./SurveyCOS217.py")
		sys.exit(1)


	#Get the userid
	studentid = getpass.getuser()


	# Change if modifying the intro questions part
	NUM_INTRO_QUESTIONS = 3

	#Run the Intro Questions
	QuizIntro = SurveyIntro()
	if len(QuizIntro) < NUM_INTRO_QUESTIONS:
		print("Not all Intro Questions were answered")
		sys.exit(1)
	if len(QuizIntro) > NUM_INTRO_QUESTIONS:
		print("Not possible! Contact the Teaching Staff regarding Intro Questions")
		sys.exit(1)


	# Change if modifying the main questions part
	NUM_MAIN_QUESTIONS = 31


	#Run the Main Questions
	QuizMain = AskQuestions()
	if len(QuizMain) < NUM_MAIN_QUESTIONS:
		print("Answered %d, out of %d Questions\nNot all Main Questions were answered" % (len(QuizMain), NUM_MAIN_QUESTIONS))
		print(QuizMain)
		sys.exit(1)
	if len(QuizMain) > NUM_MAIN_QUESTIONS:
		print("Not possible! Contact the Teaching Staff regarding Main Questions")
		sys.exit(1)

	#Construct the Quiz Dictionary, containing all pairs of questions and answers
	Quiz = QuizIntro
	Quiz.update(QuizMain)

	#Write the questions and answers to a file
	NumberOfQnALines = WriteQnAToFile(Quiz,studentid)
	if NumberOfQnALines != 2*(NUM_INTRO_QUESTIONS + NUM_MAIN_QUESTIONS + 1): # + 2 for the "Student's username: /n studentid" saved on the file
		print("The Survey file does not contain all necessary questions and answers. Please restart or contact the Teaching Staff")
		sys.exit(1)

	sys.exit(0)



"""
Creates the file Survey
that stores the Quiz questions followed by the student provided answers to the questions
in the form of
question1
answer1
question2
answer2
... 
Returns the number of lines written on the file.
"""
def WriteQnAToFile(Quiz,studentid):
	
	#Open/Create the file with name fileName
	studentFile = open('survey', 'w')

	#Write the studentid in the file first
	studentFile.write('Student\'s username\n')
	studentFile.write(str(studentid)+'\n')
	studentFile.close()

	#Write the questions and answers to the file
	for q,a in Quiz.items():
		studentFile = open('survey', 'a')
		studentFile.write(q+'\n')
		studentFile.write(str(a)+'\n')
		studentFile.close()

	return 2 * (len(Quiz) + 1)

	

"""
The Introductory Questions of the Survey.
Returns the questions and answers in the form of a list {question, answer}:
[Major, Degree, Graduation Year]
"""
def SurveyIntro(): 

	# The list of answers to the Intro Questions
	Quiz = OrderedDict()

	print("Welcome to the Introductory Survey for COS 217\n")

	# Create regular expressions to match anything that does not contain numbers
#	remajor = re.compile(r'[a-zA-Z]+([\. ][a-zA-Z]*)*')
	remajor = re.compile(r'[^0-9]+')

	while True:
		print("Enter your Concentration (major): (Type N/A if you have not chosen a major yet)")
		Major = input()
		if Major == "N/A":
			break
		match = remajor.match(Major)
		if match:
			if match.end() == len(Major):
				break

		print("Concentration must not be empty or contain any numbers")

	Quiz['Concentration']= Major

	while True:
		print("Degree Sought: (Type N/A if you have not chosen a Degree yet)")
		Degree = input()
		if Degree == "N/A":
			break
		match = remajor.match(Degree)
		if match:
			if match.end() == len(Degree):
				break
		print("Degree must not be empty or contain any numbers")

	Quiz['Degree']= Degree

	while True:
		print("Academic Year of Graduation: \n\t Type \"N/A\" if not applicable. Otherwise type the number")
		GradYear = input()
		if GradYear.isdecimal():
			GradYear = int(GradYear)
			PotYearGraduation = [AcademicYear(), AcademicYear() + 1, AcademicYear() +2, AcademicYear() +3]
			if GradYear in PotYearGraduation:
				break
			else:
				print("Year of Graduation must be either %d, %d, %d, %d, or N/A" % (AcademicYear(), AcademicYear() + 1, AcademicYear() +2, AcademicYear() +3))
		else:
			if GradYear != 'N/A':
				print("If the question is not applicable, please type \"N/A\"")
			else:
				break

	Quiz['Academic Year of Graduation'] = GradYear

	return Quiz

	



"""
The main body of the survey questions. Returns all the answers the student
provided in the form of a Quiz dictionary: {question, answer}
"""
def AskQuestions():

	# The list with the answers the student provided
	Quiz = OrderedDict()

	# List of Main Questions
	Topic1Questions = ['Binary number system', 'Hexadecimal number system', 
	'Representation of signed integers (two\'s complement notation)']
	Topic2Questions = ['Fundamental commands (cd, ls, cat, etc.)', 'Redirection (< and >) and pipes ( | )',
	'Background processes via Ctrl-z']
	Topic3Questions = ['The emacs editor', 'The gcc compiler driver', 'The gdb debugger','The make project maintenance tool']
	Topic4Questions = ['Java Control structures (if, switch, for, while, do, break)', 'Methods', 'Java Arrays', 'Classes']
	Topic5Questions = ['C Control structures (if, switch, for, while, do, break)', 'Functions', 'C Arrays', 'Structures', 
	'Preprocessor directives (#include, #define, etc.)', 'Interface (.h) files', 'Pointers and pointer operators (* and &)', 
	'Dynamic memory mgmt. (malloc, calloc, realloc, free)', 'Void pointers','Function pointers', 'Abstract data types (ADTs)']

	# The list of accepted answers on each question.
	AcceptedAnswers = [0,1,2,3,4,5]

	print("Survey Questions. State your expertise in each topic. Use a 5-point scale, where 5 means \"I know this topic very well\" and 0 means \"I know nothing about this topic\".")

	# Topic 1: Number Systems

	print("\nTopic 1: Number Systems\n")

	for question in Topic1Questions:
		Quiz[question] = PromptQuestion("\t"+question+": ", AcceptedAnswers)

	# Topic 2: Unix Operating System
	Quiz['Topic 2: Unix Operating System, in general'] = PromptQuestion("\nTopic 2: Unix Operating System, in general: ", AcceptedAnswers)

	for question in Topic2Questions:
		Quiz[question] = PromptQuestion("\t"+question+": ", AcceptedAnswers)

	# Topic 3: GNU programming environment
	Quiz['Topic 3: GNU programming environment, in general'] = PromptQuestion("\nTopic 3: GNU programming environment, in general: ", AcceptedAnswers)

	for question in Topic3Questions:
		Quiz[question] = PromptQuestion("\t"+question+": ", AcceptedAnswers)

	# Topic 4: Java programming language
	Quiz['Topic 4: Java programming language, in general'] = PromptQuestion("\nTopic 4: Java programming language, in general: ", AcceptedAnswers)

	for question in Topic4Questions:
		Quiz[question] = PromptQuestion("\t"+question+": ", AcceptedAnswers)

	# Topic 5: C programming language
	Quiz['Topic 5: C programming language, in general'] = PromptQuestion("\nTopic 5: C programming language, in general: ", AcceptedAnswers)

	for question in Topic5Questions:
		Quiz[question] = PromptQuestion("\t"+question+": ", AcceptedAnswers)

	# Topic 6: x86-64 architecture
	Quiz['Topic 6: x86-64 architecture'] = PromptQuestion("\nTopic 6: x86-64 architecture: ", AcceptedAnswers)

	# Topic 7: x86-64 assembly language
	Quiz['Topic 7: x86-64 assembly language'] = PromptQuestion("\nTopic 7: x86-64 assembly language: ", AcceptedAnswers)
		
	return Quiz



""" 
Ask a question in the form of a string mystring. 
Return the answer. 
The answer must be an element of the list mylist
"""

def PromptQuestion(mystring, mylist):
	while True:
		answer = input(mystring)
		if answer.isdecimal() == True:
			answer = int(answer)
			if answer in mylist:
				return answer
			print("Answer must be %d, %d, %d, %d, %d, or %d" % (mylist[0], mylist[1], mylist[2], mylist[3], mylist[4], mylist[5],))
		else:
			print("Answer must be a number")

# Return the Current Academic Year
def AcademicYear():
	today = date.today()
	CurrentMonth = today.month
	CurrentYear = today.year

	if CurrentMonth in [9, 10, 11, 12]:
		return CurrentYear + 1
	else:
		return CurrentYear

if __name__ == '__main__':
	main()