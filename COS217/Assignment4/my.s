	.section	__TEXT,__text,regular,pure_instructions
	.macosx_version_min 10, 12
	.globl	_main
	.p2align	4, 0x90
_main:                                  ## @main
	.cfi_startproc
## BB#0:
	pushq	%rbp
Lcfi0:
	.cfi_def_cfa_offset 16
Lcfi1:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Lcfi2:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movl	$0, -4(%rbp)
LBB0_1:                                 ## =>This Inner Loop Header: Depth=1
	callq	_getchar
	movl	%eax, _iChar(%rip)
	cmpl	$-1, %eax
	je	LBB0_12
## BB#2:                                ##   in Loop: Header=BB0_1 Depth=1
	movq	_lCharCount(%rip), %rax
	addq	$1, %rax
	movq	%rax, _lCharCount(%rip)
	
	movl	_iChar(%rip), %edi
	callq	_isspace
	cmpl	$0, %eax
	je	LBB0_6
	
## BB#3:                                ##   in Loop: Header=BB0_1 Depth=1
	cmpl	$0, _iInWord(%rip)
	je	LBB0_5
## BB#4:                                ##   in Loop: Header=BB0_1 Depth=1
	movq	_lWordCount(%rip), %rax
	addq	$1, %rax
	movq	%rax, _lWordCount(%rip)
	movl	$0, _iInWord(%rip)
LBB0_5:                                 ##   in Loop: Header=BB0_1 Depth=1
	jmp	LBB0_9
LBB0_6:                                 ##   in Loop: Header=BB0_1 Depth=1
	cmpl	$0, _iInWord(%rip)
	jne	LBB0_8
## BB#7:                                ##   in Loop: Header=BB0_1 Depth=1
	movl	$1, _iInWord(%rip)
LBB0_8:                                 ##   in Loop: Header=BB0_1 Depth=1
	jmp	LBB0_9
LBB0_9:                                 ##   in Loop: Header=BB0_1 Depth=1
	cmpl	$10, _iChar(%rip)
	jne	LBB0_11
## BB#10:                               ##   in Loop: Header=BB0_1 Depth=1
	movq	_lLineCount(%rip), %rax
	addq	$1, %rax
	movq	%rax, _lLineCount(%rip)
LBB0_11:                                ##   in Loop: Header=BB0_1 Depth=1
	jmp	LBB0_1
LBB0_12:
	cmpl	$0, _iInWord(%rip)
	je	LBB0_14
## BB#13:
	movq	_lWordCount(%rip), %rax
	addq	$1, %rax
	movq	%rax, _lWordCount(%rip)
LBB0_14:
	leaq	L_.str(%rip), %rdi
	movq	_lLineCount(%rip), %rsi
	movq	_lWordCount(%rip), %rdx
	movq	_lCharCount(%rip), %rcx
	movb	$0, %al
	callq	_printf
	xorl	%r8d, %r8d
	movl	%eax, -8(%rbp)          ## 4-byte Spill
	movl	%r8d, %eax
	addq	$16, %rsp
	popq	%rbp
	retq
	.cfi_endproc

.zerofill __DATA,__bss,_iChar,4,2       ## @iChar
.zerofill __DATA,__bss,_lCharCount,8,3  ## @lCharCount
.zerofill __DATA,__bss,_iInWord,4,2     ## @iInWord
.zerofill __DATA,__bss,_lWordCount,8,3  ## @lWordCount
.zerofill __DATA,__bss,_lLineCount,8,3  ## @lLineCount
	.section	__TEXT,__cstring,cstring_literals
L_.str:                                 ## @.str
	.asciz	"%7ld %7ld %7ld\n"


.subsections_via_symbols
