[Silhouette](https://www.silhouette.rocks/) sample application.

```
# Start application
$ sbt run

# Sign up
$ curl http://localhost:9000/signup -d "{\"email\": \"alice@sample.com\", \"password\": \"alice\"}"
registrationToken: RegistrationTokenId(nj7cyDz2m0kMLBe0zHFqI0LjqoV5gJq9)

# Confirm sign up with registration token
$ curl http://localhost:9000/confirmSignup/nj7cyDz2m0kMLBe0zHFqI0LjqoV5gJq9
confirmed signup

# Access secured content (but reject)
$ curl -w %{http_code} http://localhost:9000/securedContent
401

# Login
$ curl -v http://localhost:9000/login -d "{\"email\": \"alice@sample.com\", \"password\": \"alice\"}"
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 9000 (#0)
> POST /login HTTP/1.1
> Host: localhost:9000
> User-Agent: curl/7.55.1
> Accept: */*
> Content-Length: 50
> Content-Type: application/x-www-form-urlencoded
> 
* upload completely sent off: 50 out of 50 bytes
< HTTP/1.1 200 OK
< X-Auth-Token: 2df447828787ae558bdf64005ebf679c87a7fe783f8a7f4e5dcd77c0835d7c987fb55fa5c2c4beb4a3852f9175a830fdffd7804d1f84b969b1240038ad6ebe51a35dc94b205d8eb31e9db0dcd7bc16a65a567568b070d7e6e50989743f66f7f6bfcfd80a73c49816c5a61133cb60071ee86451317b8ba755321eac14ecacfb44
< Content-Length: 15
< Content-Type: text/plain; charset=utf-8
< Date: Mon, 11 Sep 2017 02:21:12 GMT
< 
* Connection #0 to host localhost left intact

# Access secured content
$ curl -H "X-Auth-Token: 2df447828787ae558bdf64005ebf679c87a7fe783f8a7f4e5dcd77c0835d7c987fb55fa5c2c4beb4a3852f9175a830fdffd7804d1f84b969b1240038ad6ebe51a35dc94b205d8eb31e9db0dcd7bc16a65a567568b070d7e6e50989743f66f7f6bfcfd80a73c49816c5a61133cb60071ee86451317b8ba755321eac14ecacfb44" http://localhost:9000/securedContent
secured content!

# Logout
$ curl -H "X-Auth-Token: 2df447828787ae558bdf64005ebf679c87a7fe783f8a7f4e5dcd77c0835d7c987fb55fa5c2c4beb4a3852f9175a830fdffd7804d1f84b969b1240038ad6ebe51a35dc94b205d8eb31e9db0dcd7bc16a65a567568b070d7e6e50989743f66f7f6bfcfd80a73c49816c5a61133cb60071ee86451317b8ba755321eac14ecacfb44" http://localhost:9000/logout
completed logout
```
