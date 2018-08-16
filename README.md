# Slack Post Lambda Function Sample 

### Environment Variable

Please setting `$AWS_PROFILE`

### Packaging

```
$ sbt assembly
```

### Setting Slack API Token 

Please modify serverless.yml

### Deploy

```
$ sls deploy -v
```

### Invoke

```
$ sls invoke -f sample -l
```

### Tips

##### Sending a Message to an Amazon SQS Queue from CLI

```
$ aws sqs send-message --queue-url <queue-url> --message-body '{"message":"hello"}'
```

##### Receiving a Message to an Amazon SQS Queue from CLI

```
$ aws sqs receive-message --queue-url <queue-url>
```
