set breakpoint pending on
b Java_Sample4_checkInstance
set breakpoint pending auto
commands
echo print foo value\n
p /x foo
echo print memory where arr references\n
x /8xb foo
echo print memory where oop references\n
x /32xb ((long *) *$1)
end
