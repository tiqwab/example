#!/bin/bash

echo '0.0.0.0/0' > /work/path-prefixes.txt
ip rule add from 127.0.0.1/8 iif lo table 123
ip route add local 0.0.0.0/0 dev lo table 123

/usr/bin/supervisord
