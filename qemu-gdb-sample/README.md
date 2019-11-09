Sample codes to debug a program running in QEMU.

## Files

- `.gdbinit`
  - Used when starting GDB
- `base.img`
  - MBR-formatted image where a compiled program is copied
- `start.S`
  - Sample program

## How to try

```
# Start QEMU with gdb debug setup
$ make qemu

# Start GDB (execute in another terminal)
$ make gdb
```
