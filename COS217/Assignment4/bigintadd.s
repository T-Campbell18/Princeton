### --------------------------------------------------------------------
### bigintadd.s
### Author: Tyler Campbell
### --------------------------------------------------------------------

	.equ FALSE, 0
	.equ TRUE, 1

	.equ MAX, 32768

	.equ LLENGTH, 0
	
	.section ".rodata"

### --------------------------------------------------------------------

	.section ".data"

### --------------------------------------------------------------------

	.section ".bss"
	
### --------------------------------------------------------------------

	.section ".text"
	
	###-------------------------------------------------------------------
	### Return the larger of lLength1 and lLength2.
	### static long BigInt_larger(long lLength1, long lLength2)
	###-------------------------------------------------------------------
	
	### Local Variables
	.equ LARGER, 0
	
	### Parameters
	.equ L1, 8
	.equ L2, 16
	
	.equ BYTECOUNT, 24
	
	.type BigInt_larger, @function
	
	BigInt_larger:
		### Save parameters to the stack.
		pushq %rdi
		pushq %rsi

		### long lLarger
		subq $8, %rsp
		
		### if !(lLength1 < lLength2) goto else
		movq	L1(%rsp), %rcx
		cmpq	L2(%rsp), %rcx
		jle else
		
		### lLarger = lLength1;
		movq	L1(%rsp), %rax
		movq	%rax, LARGER(%rsp)
		jmp end
		
	else:
		### lLarger = lLength2;
		movq	L2(%rsp), %rax
		movq	%rax, LARGER(%rsp)
		jmp end
		
	end:
		### return lLarger;
		movq LARGER(%rsp), %rax
		addq $BYTECOUNT, %rsp
		ret
		
	###--------------------------------------------------------------------
	### Assign the sum of oAddend1 and oAddend2 to oSum.  oSum should be
  ### distinct from oAddend1 and oAddend2.  Return 0 (FALSE) if an
	### overflow occurred, and 1 (TRUE) otherwise.
	### int BigInt_add(BigInt_T oAddend1, BigInt_T oAddend2, BigInt_T oSum)
	###--------------------------------------------------------------------		
		
	### Local Variables
	.equ LENGTH, 0
	.equ INDEX, 8
	.equ SUM, 16
	.equ CARRY, 24
	
	### Parameters
	.equ OSUM, 32
	.equ OAEND2, 40
	.equ OAEND1, 48
	
	.equ BYTECOUNT, 56
	
	.globl BigInt_add
	.type BigInt_add, @function
	
	BigInt_add:
		
		### Save parameters to the stack.
		pushq %rdi
		pushq %rsi
		pushq %rdx

		### declare local varibles 
		subq $32, %rsp
		
		### lSumLength = BigInt_larger(oAddend1->lLength, oAddend2->lLength)
		movq OAEND1(%rsp), %rcx
		movq LLENGTH(%rcx), %rcx
		movq OAEND2(%rsp), %rdx
		movq LLENGTH(%rdx), %rdx
		movq %rcx, %rdi
		movq %rdx, %rsi
		call BigInt_larger
		movq %rax, LENGTH(%rsp)
		
		### if !(oSum->lLength < lSumLength) goto preloop
		movq OSUM(%rsp), %rcx
		movq LLENGTH(%rcx), %rcx
		cmpq LENGTH(%rsp), %rcx
		jle preloop
		
		### memset(oSum->aulDigits, 0, MAX_DIGITS * sizeof(unsigned long))
		movq	OSUM(%rsp), %rcx
		addq	$8, %rcx
		movq	$MAX, %rdx
		imulq $8, %rdx
		movq	$0, %rsi
		movq	%rcx, %rdi
		call	memset
		
	preloop:
		### ulCarry = 0; lIndex = 0;
		movq $0, CARRY(%rsp)
		movq $0, INDEX(%rsp)
	
	forloop:
		### loop condition ! lIndex < lSumLength goto endloop
		movq LENGTH(%rsp), %rcx
		cmpq INDEX(%rsp), %rcx
		jle endloop
		
		### ulSum = ulCarry; ulCarry = 0
		movq CARRY(%rsp), %rcx
		movq %rcx, SUM(%rsp)
		movq $0, CARRY(%rsp)
	
		### ulSum += oAddend1->aulDigits[lIndex]
		movq OAEND1(%rsp), %rcx
		movq INDEX(%rsp), %rdx
		movq 8(%rcx, %rdx, 8), %rax
		addq %rax, SUM(%rsp)
		
		### if !(ulSum < oAddend1->aulDigits[lIndex]) goto endif1
		cmpq %rax, SUM(%rsp)
		jae endif1
		
		### ulCarry = 1;
		movq $1, CARRY(%rsp)
		
	endif1:
		### ulSum += oAddend2->aulDigits[lIndex]
		movq OAEND2(%rsp), %rcx
		movq INDEX(%rsp), %rdx
		movq 8(%rcx, %rdx, 8), %rax
		addq %rax, SUM(%rsp)
		
		### if !(ulSum < oAddend2->aulDigits[lIndex]) goto endif2
		cmpq %rax, SUM(%rsp)
		jae endif2
		
		### ulCarry = 1;
		movq $1, CARRY(%rsp)
	
	endif2:
		### oSum->aulDigits[lIndex] = ulSum    
		movq OSUM(%rsp), %rcx
		movq INDEX(%rsp), %rdx
		movq SUM(%rsp), %rax
		movq %rax, 8(%rcx, %rdx, 8)
		
		### lIndex++
		incq INDEX(%rsp)
		
		jmp forloop
		
	endloop:
		### if (ulCarry != 1) goto end2    
		cmpq $1, CARRY(%rsp)
		jne end2
		
		### if (lSumLength != MAX_DIGITS) goto endif
		cmpq $MAX, LENGTH(%rsp)
		jne endif
		
		### return FALSE
		movq $FALSE, %rax
		addq $BYTECOUNT, %rsp
		ret
		
	endif:
		### oSum->aulDigits[lSumLength] = 1
		movq OSUM(%rsp), %rcx
		movq LENGTH(%rsp), %rdx
		movq $1, 8(%rcx, %rdx, 8)
		
		### lSumLength++
		incq LENGTH(%rsp)
			
	end2:	
		### oSum->lLength = lSumLength
		movq OSUM(%rsp), %rcx
		movq LENGTH(%rsp), %rdx
		movq %rdx, LLENGTH(%rcx)
		
		### return true
		movq $TRUE, %rax
		addq $BYTECOUNT, %rsp
		ret
			