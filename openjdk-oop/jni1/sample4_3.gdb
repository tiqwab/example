set breakpoint pending on
b Java_Sample4_checkInstance
set breakpoint pending auto
commands
echo print Universe::_narrow_oop._base
p /x Universe::_narrow_oop._base
echo print Universe::_narrow_oop._shift
p /x Universe::_narrow_oop._shift
end
