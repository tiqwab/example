FROM python:3

WORKDIR /app

# add our user and group first to make sure their IDs get assigned consistently, regardless of whatever dependencies get added
RUN groupadd -r sample && useradd -r -g sample sample

# install gosu
RUN set -eux; \
	apt-get update; \
	apt-get install -y gosu; \
	rm -rf /var/lib/apt/lists/*; \
# verify that the binary works
	gosu nobody true

COPY main.py ./
COPY data/ ./data/

RUN chown -R sample:sample ./data

CMD ["gosu", "sample", "python3", "main.py"]
