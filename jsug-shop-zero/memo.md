### redis

```
# Prepare docker image for redis

# redis の Docker Image を取得
> docker image pull redis:alpine
# `--rm` を指定しつつコンテナ作成
# redis は `/data` にデータを保存するような設定になっているので、それを考慮して `-v` の指定
> docker container run --rm -v $(pwd)/data:/data redis:alpine
```

```
# redis-cli

# set
> set key1 val1
> set key2 val2
# get
> get key1
> get key2
# delete
> del key1
# list
> keys *
> keys key*
```
