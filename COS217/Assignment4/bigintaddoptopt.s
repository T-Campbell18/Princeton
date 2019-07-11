### --------------------------------------------------------------------
### bigintaddoptopt.s
### Author: Tyler Campbell
### --------------------------------------------------------------------

	.equ FALSE, 0
	.equ TRUE, 1

	.equ MAX, 32768

	.equ LLENGTH, 0
	
### --------------------------------------------------------------------

	.section ".rodata"
	
### --------------------------------------------------------------------

	.section ".data"
	
### --------------------------------------------------------------------

	.section ".bss"
	
### --------------------------------------------------------------------

	.section ".text"

	###--------------------------------------------------------------------
	### Assign the sum of oAddend1 and oAddend2 to oSum.  oSum should be
  ### distinct from oAddend1 and oAddend2.  Return 0 (FALSE) if an
	### overflow occurred, and 1 (TRUE) otherwise.
	### int BigInt_add(BigInt_T oAddend1, BigInt_T oAddend2, BigInt_T oSum)
	###--------------------------------------------------------------------

	### Local vairables
	.equ LENGTH, %r12
	.equ INDEX, %r13       
	.equ SUM, %r14      
	
	### Parameters	
	.equ OA1, %rdi
	.equ OA2, %rsi
	.equ OSUM, %rdx

	.globl BigInt_add
	.type BigInt_add,@function

BigInt_add:
	pushq %r12 # Save callee-saved register
	pushq %r13 # Save callee-saved register 
	pushq %r14 # Save callee-saved register

	### if (lLength1 > lLength2) goto mem
	### lSumLength = oAddend1->lLength1
	movq LLENGTH(OA1), %rax
	movq %rax, LENGTH
	cmpq LLENGTH(OA2), %rax
	jg mem
	
	### lSumLength = oAddend1->lLength2
	movq LLENGTH(OA2), %rax
	movq %rax, LENGTH
	
mem:
	### if (lSumLength <= oSum->lLength ) goto preloop
	cmpq LENGTH, LLENGTH(OSUM)
	jle preloop

	pushq %rdi # Save caller-saved register
	pushq %rsi # Save caller-saved register
	pushq %rdx # Save caller-saved register

	### memset(oSum->aulDigits, 0, MAX_DIGITS * sizeof(unsigned long))
	movq	OSUM, %rdi
	addq	$8, %rdi
	movq	$MAX, %rdx
	imulq $8, %rdx
	movq	$0, %rsi
	call	memset

	popq %rdx # Restore caller-saved register
	popq %rsi # Restore caller-saved register
	popq %rdi # Restore caller-saved register

preloop:	
	### ulCarry = 0
	clc
	lahf
	
	### lIndex = 0
	movq $0, INDEX
	
	### goto test
	jmp test
	
loop:
	### ulSum = 0
	movq $0, SUM

	### ulSum += oAddend1->aulDigits[lIndex] 
	addq 8(OA1, INDEX, 8), SUM
	
	### ulSum += oAddend2->aulDigits[lIndex]
	sahf
	adcq 8(OA2, INDEX, 8), SUM
	lahf
	
	### oSum->aulDigits[lIndex] = ulSum
	movq SUM, 8(OSUM, INDEX, 8)

	### lIndex++
	incq INDEX
	
test:
	### if (lIndex < lSumLength) goto loop
	cmpq LENGTH, INDEX
	jl loop

	### if (carry != 1) goto end
	sahf
	jnc end
	
	### if (lSumLength != MAX_DIGITS) goto endif
	cmpq $MAX, LENGTH
	jne endif

	### return FALSE
	movq $FALSE, %rax

	popq %r14 # Restore callee-saved register
	popq %r13 # Restore callee-saved register
	popq %r12 # Restore callee-saved register
	ret

endif:
	### oSum->aulDigits[lSumLength] = 1;
	movq $1, 8(OSUM, LENGTH, 8)

	### lSumLength++
	incq LENGTH

end:
	### oSum->lLength = lSumLength
	movq LENGTH, LLENGTH(OSUM)
	
	### return TRUE
	movq $TRUE, %rax

	popq %r14 # Restore callee-saved register
	popq %r13 # Restore callee-saved register
	popq %r12 # Restore callee-saved register
	ret	
		