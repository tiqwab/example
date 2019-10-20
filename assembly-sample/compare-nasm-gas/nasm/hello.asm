; To create a object file:
; nasm -f elf32 -o hello.obj hello.asm

extern print_msg

segment .text
  global hello
hello:
  push ebp
  mov  ebp, esp
  push msg
  call print_msg
  add esp, 4
  pop  ebp
  ret

segment .data
  msg db "hello", 0
