#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<sys/types.h>
#include<string.h>
#include<sys/wait.h>
 
int main()
{
	// We use two pipes
	// First pipe to send input string from parent
	// Second pipe to send concatenated string from child
 
	int fd1[2];  // Used to store two ends of first pipe
	int fd2[2];  // Used to store two ends of second pipe
 
	char i[100];
	char j[100];
	pid_t p;
 
	if (pipe(fd1)==-1)
	{
		fprintf(stderr, "Pipe Failed" );
		return 1;
	}
	if (pipe(fd2)==-1)
	{
		fprintf(stderr, "Pipe Failed" );
		return 1;
	}
 
	p = fork();
 
	if (p < 0)
	{
		fprintf(stderr, "fork Failed" );
		return 1;
	}
 
	// Parent process
	else if (p > 0)
	{
		char concat_str[100];
 
		close(fd1[0]);  // Close reading end of first pipe
 
		// Write input string and close writing end of first
		// pipe.
		write(fd1[1], "0", 2);
		close(fd1[1]);
 
		// Wait for child to send a string
		wait(NULL);
 
		close(fd2[1]); // Close writing end of second pipe
 
		// Read string from child, print it and close
		// reading end.
		read(fd2[0], i, 100);
		printf("%s\n", i);
		close(fd2[0]);
	}
 
	// child process
	else
	{
		close(fd1[1]);  // Close writing end of first pipe

		// Read a string using first pipe
		char t[100];
		read(fd1[0], t, 100);
 		dup2(fd1[1], STDOUT_FILENO);
		// Concatenate a fixed string with it
		
		// Close both reading ends
		close(fd1[0]);
		close(fd2[0]);
 		strcat(t, "money");

		// Write concatenated string and close writing end
		write(fd2[1], t, 100);
		close(fd2[1]);
 
		exit(0);
	}
}