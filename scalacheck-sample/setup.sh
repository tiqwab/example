#!/bin/sh

set -eu

CONTAINER_NAME="scalacheck-mysql"

docker container ls -a --format '{{json .Names}}' | grep $CONTAINER_NAME > /dev/null 2>&1
if [ $? -eq 0 ]; then
  docker container stop $CONTAINER_NAME > /dev/null 2>&1
  docker container rm $CONTAINER_NAME > /dev/null 2>&1
fi

docker container run -d -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=password \
    --name $CONTAINER_NAME mysql:5.7

# Wait for setup
set +e
for i in $(seq 20)
do
    echo "Wait for MySQL..."
    sleep 5
    echo "SELECT 1" | mysql -uroot -h127.0.0.1 > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "MySQL ready"
        break
    fi
done
set -e

mysql -uroot -h127.0.0.1 <<EOF
CREATE DATABASE scalacheck DEFAULT CHARACTER SET utf8mb4;

GRANT ALL PRIVILEGES ON scalacheck.* TO tester@'%' IDENTIFIED BY 'password';

USE scalacheck;

CREATE TABLE USERS (
  id BIGINT NOT NULL PRIMARY KEY,
  name NVARCHAR(255) NOT NULL,
  age SMALLINT NOT NULL
);
EOF
