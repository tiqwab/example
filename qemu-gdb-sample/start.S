.text
.global start
start:
    .code16
    cli
loop:
    hlt
    jmp loop
