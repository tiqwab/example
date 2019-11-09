.PHONY: default clean

default: start.img

start.o: start.S
	cc -o $@ -nostdinc -c -g $<

start: start.o
	ld -o $@ -e start -N -Ttext 0x7c00 $<

start.img: start base.img
	cp base.img $@
	objcopy -O binary -j .text -S start tmp
	dd if=tmp of=$@ bs=510 count=1 conv=notrunc
	rm tmp

qemu: start.img
	qemu-system-i386 -drive file=$<,index=0,media=disk,format=raw -S -gdb tcp::26000

gdb: .gdbinit
	gdb -n -x $<

clean:
	rm -rf start.o start start.img tmp
