import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.state.KeyValueStore;

public class KafkaWordCountApplication {

	public KafkaWordCountApplication() {
		// TODO Auto-generated constructor stub
	
	}
	
	public static void main(final String[] args) throws Exception {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
 
        StreamsBuilder builder = new StreamsBuilder();
        
        //1. Stream from Kafka
        KStream<String, String> wordCountInput = builder.stream("word-count-topic");//name of the topic as parameter
        //2. Map values to lowercase
        KTable<String, Long> wordCounts =  wordCountInput.mapValues(value -> value.toLowerCase())
	        //can be alternatively written as 
	        // .mapValues(String::toLowerCase()
	        //3. flatmap value split by space
	        .flatMapValues(lowercasedTextLine -> Arrays.asList(lowercasedTextLine.split(" ")))
	        //4. Select key to apply key (we discard the old key)
	        .selectKey((ignoredKey, word) -> word)
	        //5. group by key before aggregation
	        .groupByKey()
	        //6. count occurences
	        .count("Counts");
        
        //7.send to topic
        wordCounts.to(Serdes.String(), Serdes.Long(), "word-count-output");
        
        KafkaStreams  streams = new KafkaStreams(builder.build(), config);
        streams.start();
        //KStream<String, String> textLines = builder.stream("TextLinesTopic");
        
        /*KTable<String, Long> wordCounts = textLines
            .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
            .groupBy((key, word) -> word)
            .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
        wordCounts.toStream().to("WordsWithCountsTopic", Produced.with(Serdes.String(), Serdes.Long()));*/
        
 
        //KafkaStreams streams = new KafkaStreams(builder.build(), config);
        //streams.start();
    }
}
