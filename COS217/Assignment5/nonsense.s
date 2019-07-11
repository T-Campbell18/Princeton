### --------------------------------------------------------------------
### nonsense.s
### Author: Bob Dondero
### --------------------------------------------------------------------
        .section ".bss"

cChar:
        .skip 1

### --------------------------------------------------------------------

        .section ".text"

        .globl  main
        .type   main,@function

main:
        movq 0x6bb12f, %rdi
        

        

        ## Skip over some memory so the assembler will translate the
        ## previous assembly language jmp instruction into the kind of
        ## machine language jmp instruction that contains a 4-byte
        ## (instead of a 1-byte) displacement.
        .skip 128

mylabel:
        
        ##  return 0
        movl    $0, %eax
        ret
