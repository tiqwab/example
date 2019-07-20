# this is the custom built jdk
# JAVA=java
JAVA=~/workspace/openjdk11u/build/linux-x86_64-normal-server-fastdebug/jdk/bin/java 

CFLAGS=-I /usr/lib/jvm/java-11-openjdk/include -I /usr/lib/jvm/java-11-openjdk/include/linux -g
OBJS=sample1.o sample2.o sample3.o sample4.o
BUILT_LIBRARY_NAME=libsample.so

.SUFFIXES: .c .o .java .class

default: run

.c.o:
	gcc $(CFLAGS) -c $<

.java.class:
	javac $<

$(BUILT_LIBRARY_NAME): $(OBJS)
	gcc -shared -g -o $@ $^

run_sample1: Sample1.class $(BUILT_LIBRARY_NAME)
	echo --- $< ---
	$(JAVA) -Djava.library.path=. Sample1

run_sample2: Sample2.class $(BUILT_LIBRARY_NAME)
	echo --- $< ---
	$(JAVA) -Djava.library.path=. Sample2

run_sample3: Sample3.class $(BUILT_LIBRARY_NAME)
	echo --- $< ---
	$(JAVA) -Djava.library.path=. Sample3

run_sample4: Sample4.class $(BUILT_LIBRARY_NAME)
	echo --- $< ---
	$(JAVA) -Djava.library.path=. Sample4

run: run_sample1 run_sample2 run_sample3 run_sample4

debug_sample1:
	gdb --args $(JAVA) -Djava.library.path=. Sample1

debug_sample2:
	gdb -x 'sample2.gdb' --args $(JAVA) -Djava.library.path=. Sample2

debug_sample3:
	gdb --args $(JAVA) -Djava.library.path=. Sample3

debug_sample4:
	gdb -x 'sample4.gdb' --args $(JAVA) -Djava.library.path=. Sample4

debug_sample4_2:
	gdb -x 'sample4_2.gdb' --args $(JAVA) -Djava.library.path=. -Xmx512m -Xms512m Sample4

debug_sample4_3:
	gdb -x 'sample4_3.gdb' --args $(JAVA) -Djava.library.path=. -Xmx4g -Xms4g Sample4

clean:
	rm -f *.class *.o
	rm -f $(BUILT_LIBRARY_NAME)
