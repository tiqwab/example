#
# Cookbook:: zabbix-server
# Recipe:: default
#
# Copyright:: 2017, The Authors, All Rights Reserved.

#
# This cookbook was tested only for CentOS Linux release 7.3.1611 (Core)
#

# Set timezone
timezone = "#{node['zabbix-server']['timezone']}"
bash 'set_timezone' do
    code <<-EOH
        timedatectl set-timezone #{timezone}
    EOH
end

# Disable SELinux
execute "disable selinux" do
    only_if "which selinuxenabled && selinuxenabled"
    command "setenforce 0"
    action :run
    notifies :create, "template[/etc/selinux/config]"
end

template '/etc/selinux/config' do
    source 'selinux/config.erb'
    owner 'root'
    group 'root'
    mode '0644'
    action :nothing
end

# Register a repository of zabbix 3.2
# `not_if` executes a given string as shell command (or code block as ruby script).
# `not_if` prevents a resource from executing when the condition is true.
# https://docs.chef.io/resource_common.html#guards
remote_file '/tmp/zabbix-release-3.2-1.el7.noarch.rpm' do
    source "http://repo.zabbix.com/zabbix/3.2/rhel/7/x86_64/zabbix-release-3.2-1.el7.noarch.rpm"
    not_if 'rpm -qa | grep -q "zabbix-release-3.2-1.el7.noarch"'
    action :create
    notifies :install, "rpm_package[zabbix]", :immediately
end

rpm_package "zabbix" do
    source '/tmp/zabbix-release-3.2-1.el7.noarch.rpm'
    action :install
end

# Install zabbix-agent
# https://docs.chef.io/resource_package.html
package %w(zabbix-server-mysql zabbix-web-mysql zabbix-get) do
    action :install
end

# Set timezone of zabbix web interface
template '/etc/httpd/conf.d/zabbix.conf' do
    source 'httpd/conf.d/zabbix.conf.erb'
    owner 'root'
    group 'root'
    mode '0644'
    action :create
end

# Set database configuration of zabbix server
template '/etc/zabbix/zabbix_server.conf' do
    source 'zabbix/zabbix_server.conf.erb'
    owner 'root'
    group 'zabbix'
    mode '0644'
    action :create
end

# Start and enable zabbix-server
service 'zabbix-server' do
    action [:enable, :start]
end

service 'httpd' do
    action [:enable, :start]
end
