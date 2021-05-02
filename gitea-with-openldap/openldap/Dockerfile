FROM centos:7

# Install OpenLDAP
RUN yum update -y && \
    yum install -y openldap openldap-clients openldap-devel openldap-servers && \
    cp -a /usr/share/openldap-servers/DB_CONFIG.example /var/lib/ldap/DB_CONFIG

COPY docker-entrypoint.sh /usr/local/bin/

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
