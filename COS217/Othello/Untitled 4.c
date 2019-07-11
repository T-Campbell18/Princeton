#include <unistd.h>
#include <stdio.h>
#include<sys/wait.h>

int main()
{

int outfd[2];
int infd[2];

pipe(outfd); /* Where the parent is going to write to */
pipe(infd); /* From where parent is going to read */

if(!fork())
{

char *argv[]={ "./add1", NULL};

close(STDOUT_FILENO);
close(STDIN_FILENO);

dup2(outfd[0], STDIN_FILENO);
dup2(infd[1], STDOUT_FILENO);

close(outfd[0]); /* Not required for the child */
close(outfd[1]);
close(infd[0]);
close(infd[1]);


execvp(argv[0], argv);


}
else
{

char input[100];

close(outfd[0]); /* These are being used by the child */
close(infd[1]);

write(outfd[1], "0",2); /* Write to child’s stdin */

read(infd[0],input,100); /* Read from child’s stdout */

printf("%s",input);

close(outfd[1]);
close(infd[0]);

}

}