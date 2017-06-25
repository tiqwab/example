#
# Cookbook:: zabbix-server
# Recipe:: default
#
# Copyright:: 2017, The Authors, All Rights Reserved.

# Register yum repository of zabbix
bash "zabbix repos" do
  user "root"
  code <<-EOS
  rpm -ivh http://repo.zabbix.com/zabbix/3.0/rhel/6/x86_64/zabbix-release-3.0-1.el6.noarch.rpm
  EOS
end

bash "yum update" do
  code "yum update -y"
end

# Install zabbix-server
package 'zabbix-server-mysql'
package 'zabbix-web-mysql'

# /etc/yum.repos.d/epel.repo
# enable=1
# priority=8
#
# /etc/yum.repos.d/zabbix.repo
# enable=1
# priority=6

