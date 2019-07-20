set breakpoint pending on
b Java_Sample2_callArrayLength
set breakpoint pending auto
commands
echo print arr value\n
p /x arr
echo print memory where arr references\n
x /32xb arr
echo print memory where oop references\n
x /32xb ((long *) *$1)
end
