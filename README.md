# Introduction

This connector uses the twitter streaming api to listen for status update messages and
convert them to a Kafka Connect struct on the fly. The goal is to match as much of the
Twitter Tweet object as possible.

# Configuration

## TwitterSourceConnector

This Twitter Source connector is used to pull data from Twitter in realtime.

```properties
name=connector1
tasks.max=1
connector.class=com.github.jcustenborder.kafka.connect.twitter.TwitterSourceConnector
# Set these required values
twitter.bearerToken=
kafka.tweets.topic=
# And optionally these values
filter.rule=
tweet.fields=
```

| Name                | Description                                                                                                                                                                                                                                                                                                            | Type     |
|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| twitter.bearerToken | OAuth2 Bearer token with at least Elevated Twitter API access level                                                                                                                                                                                                                                                    | password |
| kafka.tweets.topic  | Kafka topic to write the tweets to.                                                                                                                                                                                                                                                                                    | string   |
| filter.rule         | Filtering rules (see https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule for details).                                                                                                                                                                                     | string   |
| tweet.fields        | Fields that will be returned for tweet. To fetch all fields, use: attachments,author_id,context_annotations,conversation_id,created_at,edit_controls,edit_history_tweet_ids,<br/>entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,reply_settings,<br/>source,text,withheld | string   |

# Schemas

Schema is almost the same as `#/components/schemas/Tweet` json schema included in https://api.twitter.com/2/openapi.json - 
the only difference is that it is translated to Kafka connect schema. See `com.github.jcustenborder.kafka.connect.twitter.TweetConverter`
for details.

# Running in development

```
mvn clean package
export CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')"
$CONFLUENT_HOME/bin/connect-standalone connect/connect-avro-docker.properties config/TwitterSourceConnector.properties
```
