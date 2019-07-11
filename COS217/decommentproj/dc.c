#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

/*
 * Princeton would prefer that we print the space upon entering the
 * comment as opposed to leaving it. idgaf
 */

typedef enum State { NORMAL, SLASH, STRING, CHAR, COMMENT } State;

int main(void)
{
	int current_line = 1, last_comment_line_start = -1;
	int c, last_c = 'a';
	State s = NORMAL;

	while ((c = getchar()) != EOF) {
		switch (s) {
			case NORMAL: {
				if ('/' == c) {
					s = SLASH;
					break;
				}

				putchar(c);

				if ('\n' == c) {
					++current_line;
				}

				if ('"' == c) {
					s = STRING;
				} else if ('\'' == c) {
					s = CHAR;
				}
				break;
			}
			case SLASH: {
				if ('*' == c) {
					last_comment_line_start = current_line;
					s = COMMENT;
				} else {
					putchar('/');
					putchar(c);
					s = NORMAL;
				}
				break;
			}

			case STRING: {
				putchar(c);

				if ('\n' == c) {
					++current_line;
				}

				if ('"' == c && '\\' != last_c) {
					s = NORMAL;
				}
				break;
			}

			case CHAR: {
				putchar(c);

				if ('\n' == c) {
					++current_line;
				}

				if ('\'' == c && '\\' != last_c) {
					s = NORMAL;
				}
				break;
			}

			case COMMENT: {
				if ('\n' == c) {
					++current_line;
					putchar('\n');
				} else if ('/' == c && '*' == last_c) {
					putchar(' ');
					s = NORMAL;
				}
			}
		}

		last_c = c;
	}

	if (COMMENT == s) {
		assert(last_comment_line_start != -1);
		fprintf(stderr, "Error: line %d, unterminated comment\n",
			   last_comment_line_start);
		return EXIT_FAILURE;
	}

	return EXIT_SUCCESS;
}