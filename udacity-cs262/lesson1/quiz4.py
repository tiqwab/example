import re

inputs = ['0', '1', '10', '11', '05', '9', 'Isak Dinesan']

for s in inputs:
    if re.match(r'^[0-9]$', s):
        print(s)
