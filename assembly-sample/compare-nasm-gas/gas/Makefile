.PHONY: default clean

default: main

hello.obj: hello.asm
	as --32 -o $@ $<

main: main.c hello.obj
	cc -o $@ -m32 $^

clean:
	rm -rf hello.obj main
