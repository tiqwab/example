```
# Prepare VM
$ vagrant up

# Provision VM
$ make

# Edit hosts
$ sudo vi /etc/hosts
...
192.168.33.10 growi.example.com

# Then access to https://growi.example.com
# (or access to https://192.168.33.10 without editing /etc/hosts)
```
