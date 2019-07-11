/*********************************************************************/
/* decomment                                                         */
/* Tyler Campbell                                                    */
/* Precept: P05                                                      */
/* performs a subset of the de-comment job of the C preprocessor     */
/*********************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

/* enumertaed types for different states*/
typedef enum {NORMAL, SLASH, STRING, CHAR, COMMENT, STAR, 
  STRINGESCAPE, CHARESCAPE} State;

/* normal state (no special case) accepts the current character c, 
returns the approaite case and/or prints c to stdout
increases line number by accepting pointer int if needed */  
static int normal(int c, int *line)
{
  if (c == '/')
    return SLASH;
  putchar(c);
  if (c == '\n')
    ++*line;
  if (c == '"')
    return STRING;
  if (c == '\'')
    return CHAR;
  return NORMAL;  
}

/* state represents possible start of comment, character c is
character after the slash, if c is not a * then prints / and c
to stdout, returns approiate state, adjust last with the current
line number if comment has started */
static int slash(int c, int *last, int line)
{
  if (c == '*')
  {
    *last = line;
    putchar(' ');
    return COMMENT;
  }
  if (c == '/')
  {
    putchar('/');
    return SLASH;
  }
  putchar('/');
  putchar(c);
  if (c == '"')
    return STRING;
  if (c == '\'')
    return CHAR;
  return NORMAL;
}

/* state when in a string literal, accepts and prints to stdout the 
current character c and returns the appropriate State
increases line number by accepting pointer int if needed */
static int stringlit(int c, int *line)
{
  putchar(c);
  if (c == '\n')
    ++*line;
  if (c == '"')
    return NORMAL;
  if (c == '\\') 
    return STRINGESCAPE;
  return STRING;  
}

/* state when in a character literal, accepts and prints to stdout the 
current character c and returns the appropriate State 
increases line number by accepting pointer int if needed */
static int charlit(int c, int *line)
{
  putchar(c);
  if (c == '\n')
    ++*line;
  if ('\'' == c)
    return NORMAL;
  if (c == '\\') 
    return CHARESCAPE;
  return CHAR;  
}

/* State when a escape char has been reached in string or character
literal, accepts and prints to stdout the current character c
returns appropriate State based on the given state, that indicates
if it is a char or string */
static int escape(int c, State s)
{
  putchar(c);
  if (s == CHARESCAPE)
    return CHAR;
  return STRING;  
    
}

/* state when in a comment, accepts the current character c, 
prints to stdout if the character is a newline, and returns 
the appropriate State, prints space at start of comment
increases line number by accepting pointer int if needed */
static int comment(int c, int *line)
{
  if (c == '\n')
  {
    putchar(c);
    ++*line;
  }
  if (c == '*')
    return STAR;
  return COMMENT;  
}

/* state for possible ending of comment, accepts current character c
returns apporiate state prints a space to stdout if the comment has 
been terminated 
increases line number by accepting pointer int if needed */
static int star(int c, int *line)
{
  if (c == '\n')
  {
    putchar(c);
    ++*line;
  }
  if (c == '/')
    return NORMAL;
  else if (c == '*')
    return STAR;
  else  
    return COMMENT; 
}

/* reads text from stdin and removes comments, 
prints to stderr if a comment is unfinshed 
handles special cases for certain states at EOF*/
int main(void) 
{
  int c;
  /* current line number*/
  int line = 1;
  /* last comment line number*/
  int last = -1;
  State state = NORMAL;
  
  while ((c = getchar()) != EOF)
  {
    switch (state) 
    {
      case NORMAL: 
        state = normal(c, &line);
        break;
        
      case STRING:
        state = stringlit(c, &line);
        break;
        
      case CHAR:
        state = charlit(c, &line);
        break;  
      
      case STRINGESCAPE:
        state = escape(c, STRINGESCAPE);
        break;
        
      case CHARESCAPE:
        state = escape(c, CHARESCAPE);
        break;  
      
      case SLASH: 
        state = slash(c, &last, line); 
        break;
        
      case COMMENT: 
        state = comment(c, &line);
        break;
        
      case STAR: 
        state = star(c, &line); 
        break;
    }
  }
  if (state == SLASH)
    putchar('/');
  if (state == COMMENT || state == STAR)
  {
    assert(last >= 0);
    fprintf(stderr, "Error: line %d: unterminated comment\n", last);
    return EXIT_FAILURE;
  }
  return EXIT_SUCCESS;
}