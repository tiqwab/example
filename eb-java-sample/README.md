```
# Prepare vpc and etc. by CloudFormation
$ aws cloudformation create-stack --stack-name EbJavaSampleStack --template-body file://cfn-template.yml

# Prepare sample application from AWS web console
...

# Initialize local eb project
$ eb init

# Create app
...

# Create Buildfile
# Gradle wrapper also can be used in addition to the gradle in the build environment.
# Do not execute `clean` task since recreated directories cannot be accessed by the proceeding user?
$ cat Buildfile
build: ./gradlew build

# Create Procfile
$ cat Procfile
web: java -Dserver.port=5000 -jar build/libs/eb-java-sample-0.0.1-SNAPSHOT.jar

# Deploy application
$ eb deploy
```
