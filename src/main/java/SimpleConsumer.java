import java.util.Properties;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class SimpleConsumer {
	
	public SimpleConsumer(String arg) {
		// TODO Auto-generated constructor stub
		int id = 0;
		if(arg.length() == 0){
	         System.out.println("Enter topic name");
	         return;
	      }
	      //Kafka consumer configuration settings
	      String topicName = arg.toString();
	      Properties props = new Properties();
	      
	      props.put("bootstrap.servers", "localhost:9092");
	      props.put("group.id", "test");
	      props.put("enable.auto.commit", "false");
	      //props.put("auto.offset.reset", "true");
	      //the consumer automatically triggers offset commits periodically according to the interval configured with 
	      props.put("auto.commit.interval.ms", "1000");
	      props.put("session.timeout.ms", "30000");
	      props.put("key.deserializer", 
	         "org.apache.kafka.common.serialization.StringDeserializer");
	      props.put("value.deserializer", 
	         "org.apache.kafka.common.serialization.StringDeserializer");
	      KafkaConsumer<String, String> consumer = new KafkaConsumer
	         <String, String>(props);
	      
	      //Kafka Consumer subscribes list of topics here.
	      //consumer.subscribe(Arrays.asList(topicName));
	      consumer.subscribe(Arrays.asList("test","test2"));
	      
	      //print the topic name
	      System.out.println("Subscribed to topic " + topicName);
	      int i = 0;
	      
	      try {
		      while (true) {
		         ConsumerRecords<String, String> records = consumer.poll(100);
		         for (ConsumerRecord<String, String> record : records) {
		         // print the offset,key and value for the consumer records.
		         System.out.printf("offset = %d, key = %s, value = %s\n", 
		            record.offset(), record.key(), record.value());
		         
		         Map<String, Object> data = new HashMap<>();
		         data.put("partition", record.partition());
		         data.put("offset", record.offset());
		         data.put("value", record.value());
		         data.put("key", record.key());
		         System.out.println(record.offset() + ": " + data);
		         }
		         
		        
		      }
	      }catch(Exception e) {
	    	  e.printStackTrace();
	      }finally {
	    	  try {
		        	 //The easiest way to handle commits manually is with the synchronous commit API. when enable.auto.commit is false
		        	/*Obviously committing after every message is probably not a great 
	    		  	 *idea for most use cases since the processing thread has to block 
	    		  	 *for each commit request to be returned from the server. 
	    		  	 *This would kill throughput. A more reasonable approach might be 
	    		  	 *to commit after every N messages where N can be tuned for better performance.  
	    		   	*/
	    		  	consumer.commitSync();
		         }catch (CommitFailedException e) {
		        	 e.printStackTrace();
		         }
	    	  consumer.close();
	      }
	}
	
	
   public static void main(String[] args) throws Exception {
	  String arg = "test";
      new SimpleConsumer(arg);
   }
}