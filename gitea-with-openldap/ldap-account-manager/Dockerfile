FROM centos:7

# ref. https://qiita.com/y-araki-qiita/items/6b2dcbf1a39a969d8024
RUN yum update -y && \
    yum install -y epel-release && \
    rpm -Uvh http://rpms.famillecollet.com/enterprise/remi-release-7.rpm && \
    yum install -y --enablerepo=remi,remi-php74 php php-ldap php-json php-zip php-xml php74-php-gd php74-php-gmp && \
    curl -L -o ldap-account-manager-7.4.0.fedora.1.noarch.rpm \
      https://sourceforge.net/projects/lam/files/LAM/7.4/ldap-account-manager-7.4-0.fedora.1.noarch.rpm/download && \
    rpm -ivh ldap-account-manager-7.4.0.fedora.1.noarch.rpm && \
    chown apache:apache -R /var/lib/ldap-account-manager && \
    echo "extension=/opt/remi/php74/root/lib64/php/modules/gmp.so" >> /etc/php.ini && \
    echo "extension=/opt/remi/php74/root/usr/lib64/php/modules/gd.so" >> /etc/php.ini

CMD ["/usr/sbin/httpd", "-DFOREGROUND"]
