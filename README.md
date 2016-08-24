This connector uses the twitter streaming api to listen for status update messages and 
convert them to a Kafka Connect struct on the fly. The goal is to match as much of the 
Twitter Status object as possible.

# Configuration

| Name                            | Description                                       | Type     | Default | Valid Values | Importance |
|---------------------------------|---------------------------------------------------|----------|---------|--------------|------------|
| filter.keywords                 | Twitter keywords to filter for.                   | list     |         |              | high       |
| kafka.delete.topic              | Kafka topic to write delete events to.            | string   |         |              | high       |
| kafka.status.topic              | Kafka topic to write the statuses to.             | string   |         |              | high       |
| process.deletes                 | Should this connector process deletes.            | boolean  |         |              | high       |
| twitter.oauth.accessToken       | OAuth access token                                | password |         |              | high       |
| twitter.oauth.accessTokenSecret | OAuth access token secret                         | password |         |              | high       |
| twitter.oauth.consumerKey       | OAuth consumer key                                | password |         |              | high       |
| twitter.oauth.consumerSecret    | OAuth consumer secret                             | password |         |              | high       |
| twitter.debug                   | Flag to enable debug logging for the twitter api. | boolean  | false   |              | low        |

## Example

```
name=TwitterSourceConnector
tasks.max=1
connector.class=io.confluent.kafka.connect.twitter.TwitterSourceConnector
twitter.oauth.consumerKey=<insert your value>
twitter.oauth.consumerSecret=<insert your value>
twitter.oauth.accessToken=<insert your value>
twitter.oauth.accessTokenSecret=<insert your value>
filter.keywords=olympics,san jose,kafka
kafka.status.topic=twitter
kafka.delete.topic=asdf
process.deletes=false
```

# Running in development

```
mvn clean package
export CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')"
$CONFLUENT_HOME/bin/connect-standalone connect/connect-avro-docker.properties config/TwitterSourceConnector.properties
```
