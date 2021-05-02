#!/bin/bash

set -eu -o pipefail

# slapd runs in background
/usr/sbin/slapd -u ldap -h "ldapi:/// ldap:///"

# Import ldifs
# rootDN is "cn=Manager,dc=example,dc=com"
# rootPW is "password"
ldapadd -Y EXTERNAL -H ldapi:/// -f /ldif/add_rootpw.ldif
ldapmodify -x -D cn=config -w password -f /ldif/mod_dom.ldif
ldapadd -Y EXTERNAL -H ldapi:/// -f /ldif/add_adminpw.ldif
ldapadd -x -D "cn=Manager,dc=example,dc=com" -w password  -f /ldif/base.ldif
ldapadd -H ldapi:/// -f /etc/openldap/schema/cosine.ldif
ldapadd -H ldapi:/// -f /etc/openldap/schema/inetorgperson.ldif
ldapadd -H ldapi:/// -f /etc/openldap/schema/nis.ldif

tail -f /dev/null
