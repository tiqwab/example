DOMAIN := growi.example.com
SSL_DIR := ssl

default: provision

provision: $(SSL_DIR)/$(DOMAIN).crt $(SSL_DIR)/$(DOMAIN).key
	ANSIBLE_HOST_KEY_CHECKING=False ansible-playbook -i hosts site.yaml \
							  --extra-vars "ssl_key=$(SSL_DIR)/$(DOMAIN).key" \
							  --extra-vars "ssl_crt=$(SSL_DIR)/$(DOMAIN).crt" \
							  --extra-vars "server_domain=$(DOMAIN)"

$(SSL_DIR)/$(DOMAIN).crt:
	@mkdir -p $(SSL_DIR)
	openssl genrsa -out $(SSL_DIR)/$(DOMAIN).key 4096
	openssl req -new -sha256 -out $(SSL_DIR)/$(DOMAIN).csr -key $(SSL_DIR)/$(DOMAIN).key -config ssl.conf
	openssl x509 -req -sha256 -days 3650 \
		-in $(SSL_DIR)/$(DOMAIN).csr -signkey $(SSL_DIR)/$(DOMAIN).key -out $(SSL_DIR)/$(DOMAIN).crt \
		-extensions req_ext -extfile ssl.conf

clean:
	rm -rf $(SSL_DIR)

.PHONY: default provision cert clean
