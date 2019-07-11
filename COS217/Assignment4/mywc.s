### --------------------------------------------------------------------
### mywc.s
### Author: Tyler Campbell
### --------------------------------------------------------------------

.equ TRUE, 1
.equ FALSE, 0
.equ EOF, -1

	.section ".rodata"
	
formatStr:
	.string "%7d%8d%8d\n"

### --------------------------------------------------------------------

	.section ".data"
	
lLineCount:
	.long 0
lWordCount:
	.long 0
lCharCount:
	.long 0
iInWord:
	.long FALSE

### --------------------------------------------------------------------

	.section ".bss"
	
iChar:
	.skip 4
return:
	.skip 4	

### --------------------------------------------------------------------

	.section ".text"
	
	.globl main
	.type main, @function
	
	main:
		pushq %rbp
		movq %rsp, %rbp
		
	while:
		### if ((iChar = getChar()) == EOF) goto end
		call getchar
		movl %eax, iChar
		cmpl $EOF, iChar         
		je end
		
		### lCharCount++;
		incl lCharCount
		
		### if(!isspace(iChar)) goto else 
		movq iChar, %rdi
		call isspace
		cmpl $FALSE, %eax
		je else
		
	  ### if(iInWord) lWordCount++; iInWord = FALSE;
		cmpl $TRUE, iInWord
		jne endelse
		incl lWordCount
		movl $FALSE, iInWord
		jmp endelse
		
	else:
		### if(!iInWord) iInWord = TRUE
		cmpl $FALSE, iInWord
		jne endelse
		movl $TRUE, iInWord	
		
	endelse:
		### if(iChar == '\n') lLineCount++ 
		cmpl $'\n', iChar
		jne while
		incl lLineCount
		jmp while
		
	end:
		### if (iInWord) lWordCount++; 
		cmpl $TRUE, iInWord
		jne print
		incl lWordCount
		
	print:
		
		### printf("%7ld %7ld %7ld\n", lLineCount, lWordCount, lCharCount)
		movq $formatStr, %rdi
		movq lLineCount, %rsi
		movq lWordCount, %rdx
		movq lCharCount, %rcx
		movl $0, %eax
    call printf

		### return 0 
		movl $0, %eax
		addq	$16, %rsp
		movq %rbp, %rsp
		popq %rbp
		ret
					