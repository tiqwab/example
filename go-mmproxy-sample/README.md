Sample project for [go-mmproxy](https://github.com/path-network/go-mmproxy).

```
$ docker-compose up -d --build

# Run bash in client container
$ docker container exec -it <client_container> /bin/bash 

# Connect to haproxy
$ nc -v 172.21.0.3 8080

# Accept the same message as we sent
abcdefg
abcdefg

$ exit

# Check server logs to see the client IP
$ docker-compose logs
```
