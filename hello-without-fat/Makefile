default: sample.img

sample.img: hello.bin
	dd if=/dev/zero of=$@ bs=1024 count=1440
	dd if=$< of=$@ bs=512 count=1 conv=notrunc

hello.bin: hello.nas
	nasm -o $@ $<

run: sample.img
	qemu-system-x86_64 \
		-cpu 486 \
		-rtc base=localtime \
		-drive file=$<,format=raw,index=0,if=floppy \
		-boot a

clean:
	rm -f *.img *.bin
