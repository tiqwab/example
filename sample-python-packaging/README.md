To run locally,

```
$ poetry install

$ poetry run hello
```

To build Docker image,

```
$ docker image build -t example/sample-python-packaging:latest \
    --build-arg package_version=0.0.1 .

$ docker container run --rm example/sample-python-packaging:latest
```
