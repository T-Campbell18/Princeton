### -- RODATA
	.section ".rodata"
	
### -- DATA
	.section ".data"
	
### -- BSS
	.section ".bss"
	
### -- TEST
	.section ".text"

	## --------------------------------------------
	## int BigInt_larger(int iLength1, int iLength2)
	## Return the larger of iLength1 and iLength2
	## --------------------------------------------

	## Formal parameter offsets:
	.equ ILENGTH1,  8
	.equ ILENGTH2,  12

	## Local variable offsets:
	.equ ILARGER,   -4

	.type BigInt_larger, @function
	
BigInt_larger:
	## prologue
	pushq %rbp
	movl %esp, %ebp

	## int iLarger;
	subl $4, %esp
	
	## if !(iLength1 > iLength2) goto else1
	movl ILENGTH1(%ebp), %ecx
	cmpl ILENGTH2(%ebp), %ecx
	jle else1
	## iLarger = iLength1;
	movl %ecx, ILARGER(%ebp)
	## return iLarger;
	movl ILARGER(%ebp), %eax
	## epilogue
	movl %ebp, %esp
	popq %rbp
	ret
	
else1:
	## iLarger = iLength2;
	movl ILENGTH2(%ebp), %edx
	movl %edx, ILARGER(%ebp)
	## return iLarger;
	movl ILARGER(%ebp), %eax
	## epilogue
	movl %ebp, %esp
	popq %rbp
	ret

	## --------------------------------------------------------
	## int BigInt_add(BigInt_T oAddend1, BigInt_t 
	##                oAddend2, BigInt_t oSum)
	## Assign the sum of oAddend1 and oAddend2 to oSum. Return
	## 0 (FALSE) if an overflow occurred and 1 (TRUE) otherwise.
	## --------------------------------------------------------

	## Formal parameter offsets:
	.equ OADDEND1,    8
	.equ OADDEND2,    12
	.equ OSUM,        16
	## Local variable offsets:
	.equ UICARRY,     -4
	.equ UISUM,       -8
	.equ I,           -12
	.equ ISUMLENGTH,  -16
	.equ MAX_DIGITS,  32768
	.equ TRUE,        1
	.equ FALSE,       0
       
	.type BigInt_add, @function

BigInt_add:
	## prologue
	pushq %rbp
	movl %esp, %ebp

	## push local vars (UICARRY = 0)
	pushq $0
	subl $12, %esp

	## iSumLength = BigInt_larger(OA1->iLength, OA2->iLength);
	movl OADDEND2(%ebp), %ecx
	movl OADDEND1(%ebp), %edx
	pushq (%ecx)
	pushq (%edx)
	call BigInt_larger
	movl %eax, ISUMLENGTH(%ebp)
     
	## for (i=0; i<iSumLength; i++) goto endfloop1
	movl $0, I(%ebp)        
floop1: 
	movl ISUMLENGTH(%ebp), %ecx
	cmpl I(%ebp), %ecx
	jle endfloop1
	incl I(%ebp)

	## uiSum = uiCarry; uiCarry = 0;
	movl UICARRY(%ebp), %edx
	movl %edx, UISUM(%ebp)
	movl $0, UICARRY(%ebp)

	## uiSum += OA1->auiDigits[i]
	movl OADDEND1(%ebp), %ecx
	movl I(%ebp), %edx
	movl 4(%ecx, %edx, 4), %eax
	addl %eax, UISUM(%ebp)

	## if !(uiSum < OA1->auiDigits[i]) goto endif2
	cmpl %eax, UISUM(%ebp)
	jae endif2
	## uiCarry = 1;
	movl $1, UICARRY(%ebp)

endif2:
	## uiSum += OA2->auiDigits[i]
	movl OADDEND2(%ebp), %ecx
	movl I(%ebp), %edx
	movl 4(%ecx, %edx, 4), %eax
	addl %eax, UISUM(%ebp)
	
	## if !(uiSum < OA2->auiDigits[i]) goto endif3
	cmpl %eax, UISUM(%ebp)
	jae endif3
	##  uiCarry = 1;
	movl $1, UICARRY(%ebp)
	
endif3:      
	## oSum->auiDigits[i] = uiSum
	movl OSUM(%ebp), %ecx
	movl I(%ebp), %edx
	movl UISUM(%ebp), %eax
	movl %eax, 4(%ecx, %edx, 4)

endfloop1:      
	## if !(uiCarry == 1) goto endif4
	cmpl $1, UICARRY(%ebp)
	jne endif4
	## if !(iSumLength == MAX_DIGITS) goto endif5
	cmpl $MAX_DIGITS, ISUMLENGTH(%ebp)
	jne endif5
	## return FALSE
	movl %ebp, %esp
	popq %ebp
	movl $FALSE, %eax
	ret
	
endif5: 
	## oSum->auiDigits[iSumLength] = 1;
	movl OSUM(%ebp), %ecx
	movl ISUMLENGTH(%ebp), %edx
	movl $1, 4(%ecx, %edx, 4)
	## iSumLength++
	incl ISUMLENGTH(%ebp)
       
endif4:
	## oSum->iLength = iSumLength;
	movl OSUM(%ebp), %ecx
	movl ISUMLENGTH(%ebp), %edx
	movl %edx, (%ecx)

	## return TRUE;
	movl %ebp, %esp
	popq %rbp
	movl $TRUE, %eax
	ret