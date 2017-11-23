### Store Messages

`POST /topics/:name`

Request:

```json
{
    "body": "This is test message",
    "timestamp_millis": 1511410418169
}
```

Response:

```json
{
    "id": "1"
}
```

### Get Message

`GET /topics/:name/:id`

```json
{
    "id": "1",
    "body": "This is test message",
    "timestamp_millis": 1511410418169 
}
```

### List Messages

`GET /topics/:name`

```json
{
    "messages": [
      {
          "id": "1",
          "body": "This is test message",
          "timestamp_millis": 1511410418169 
      },
      {
          "id": "2",
          "body": "This is test message too",
          "timestamp_millis": 1511410418170 
      }
  ]
}
```
