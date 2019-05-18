### How to setup

```
$ vagrant up

$ vagrant ssh

# --- in VM ---

$ sudo apt-get update

# install X server
# x11-apps is optional. xclock can be used as test.
$ sudo apt-get install xserver-xorg x11-apps

# prepare display manager
$ sudo apt-get install cmake gcc xorg-dev libgtk-3.0 libgtk-3-dev libpam-dev
$ cd /vagrant
$ cmake .
$ make
$ sudo cp display_manager_sample /usr/local/bin/
$ sudo cp gui.ui /usr/local/bin/

# prepare dwm (a simple window manager)
$ git clone https://github.com/cdown/dwm
$ cd dwm
$ make install

# prepare .xinitrc
$ echo "exec dwm" > ~/.xinitrc

# prepare PAM setting
$ sudo cp display_manager /etc/pam.d/

# prepare systemd setting
$ sudo cp my-display-manager.service /lib/systemd/system/
$ sudo systemctl enable my-display-manager

# then `vagrant reload` and display manager will show up
```
