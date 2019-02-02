  ORG 0x7c00

entry:
  MOV AX,0
  MOV SS,AX
  MOV SP,0x7c00
  MOV DS,AX
  MOV ES,AX
  MOV SI,msg ; load an address of msg

putloop: ; print character one by one
  MOV AL,[SI]
  ADD SI,1
  CMP AL,0
  JE fin
  MOV AH,0x0e
  MOV BX,15
  INT 0x10
  JMP putloop

fin:
  HLT
  JMP fin

msg:
  DB 0x0a, 0x0a
  DB "Hello World"
  DB 0x0a
  DB 0

  TIMES 0x1fe-($-$$) DB 0 ; (0x7dfe + 2) - 0x7c00 = 0x200 = 512 bytes. 2 is the below DB bytes.
  DB 0x55, 0xaa ; the end of boot sector must be 0x55, 0xaa
