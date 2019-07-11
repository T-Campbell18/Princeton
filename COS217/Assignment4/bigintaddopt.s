### --------------------------------------------------------------------
### bigintaddopt.s
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
	
	### Local Variable
	.equ LARGER, %r13
	
	### Parameters
	.equ L1, %rdi
	.equ L2, %rsi
		
	.type BigInt_larger, @function
		
	BigInt_larger:		
		push %r13 # Save callee-saved register
			
		### if !(lLength1 < lLength2) goto else
		cmpq L2, L1
		jle else
			
		### lLarger = lLength1
		movq L1, LARGER
		jmp end
			
	else:
		### lLarger = lLength2
		movq L2, LARGER
		jmp end
			
	end:
		### return lLarger;
		movq LARGER, %rax
		popq %r13 # Restore caller-saved register
		ret
		
	###--------------------------------------------------------------------
	### Assign the sum of oAddend1 and oAddend2 to oSum.  oSum should be
  ### distinct from oAddend1 and oAddend2.  Return 0 (FALSE) if an
	### overflow occurred, and 1 (TRUE) otherwise.
	### int BigInt_add(BigInt_T oAddend1, BigInt_T oAddend2, BigInt_T oSum)
	###--------------------------------------------------------------------
			
	### Local Variables
	.equ LENGTH, %r12
	.equ INDEX, %r13
	.equ SUM, %r14
	.equ CARRY, %r15
	
	### Parameters	
	.equ OA1, %rdi
	.equ OA2, %rsi
	.equ OSUM, %rdx
	
	.globl BigInt_add
	.type BigInt_add, @function
	
	BigInt_add:
		### declare local variables
		pushq	%r12 # Save callee-saved register
		pushq	%r13 # Save callee-saved register
		pushq	%r14 # Save callee-saved register
		pushq	%r15 # Save callee-saved register
		
		pushq %rdi # Save caller-saved register            
		pushq %rsi # Save caller-saved register   
		pushq %rdx # Save caller-saved register
		
		### lSumLength = BigInt_larger(oAddend1->lLength, oAddend2->lLength)
		movq LLENGTH(OA1), %rdi
		movq LLENGTH(OA2), %rsi
		call BigInt_larger
		movq %rax, LENGTH
		
		popq %rdx # Restore caller-saved register           
		popq %rsi # Restore caller-saved register          
		popq %rdi # Restore caller-saved register
		
		### if !(oSum->lLength < lSumLength) goto preloop
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
		### ulCarry = 0 lIndex = 0
		movq $0, CARRY
		movq $0, INDEX
	
	forloop:
		### loop condition ! lIndex < lSumLength goto endloop
		cmpq INDEX, LENGTH
		jle endloop
		
		### ulSum = ulCarry; ulCarry = 0
		movq CARRY, SUM
		movq	$0, CARRY
	
		### ulSum += oAddend1->aulDigits[lIndex]
		movq 8(OA1, INDEX, 8), %rax
		addq %rax, SUM
		
		### if !(ulSum < oAddend1->aulDigits[lIndex]) goto endif1
		cmpq %rax, SUM
		jae endif1
		
		### ulCarry = 1
		movq $1, CARRY
		
	endif1:
		### ulSum += oAddend2->aulDigits[lIndex]
		movq 8(OA2, INDEX, 8), %rax
		addq %rax, SUM
		
		### if !(ulSum < oAddend2->aulDigits[lIndex]) goto endif2
		cmpq %rax, SUM
		jae endif2
		
		### ulCarry = 1
		movq $1, CARRY
	
	endif2:
		### oSum->aulDigits[lIndex] = ulSum    
		movq SUM, 8(OSUM, INDEX, 8)
		
		### lIndex++
		incq INDEX
		
		jmp forloop
		
	endloop:
		### if (ulCarry != 1) goto end2    
		cmpq $1, CARRY
		jne end2
		
		### if (lSumLength != MAX_DIGITS) goto endif
		cmpq $MAX, LENGTH
		jne endif
		
		### return FALSE
		movq $FALSE, %rax
		
		popq %r15 # Restore callee-saved register
		popq %r14 # Restore callee-saved register
		popq %r13 # Restore callee-saved register
		popq %r12 # Restore callee-saved register
		ret
		
	endif:
		### oSum->aulDigits[lSumLength] = 1
		movq $1, 8(OSUM, LENGTH, 8)
		
		### lSumLength++
		incq LENGTH
			
	end2:	
		### oSum->lLength = lSumLength
		movq LENGTH, LLENGTH(OSUM)
		
		### return TRUE
		movq $TRUE, %rax
		
		popq %r15 # Restore callee-saved register
		popq %r14 # Restore callee-saved register
		popq %r13 # Restore callee-saved register
		popq %r12 # Restore callee-saved register
		ret
		