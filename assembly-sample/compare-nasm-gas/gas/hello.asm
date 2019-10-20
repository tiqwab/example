// To create a object file:
// as --32 -o $@ $<

.global hello

.text
hello:
  push  %ebp
  movl  %esp, %ebp
  push  $msg
  call  print_msg
  addl  $4, %esp
  pop   %ebp
  ret

.global msg

.data
msg:
  .ascii "hello"
