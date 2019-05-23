package com.lifotech.rtsa.storm.bolts;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.lifotech.rtsa.storm.domain.TweetComment;

/**
 * The class is resposible for dealing with MongoDB persistence store.
 * 
 * @author SK Singh
 *
 */
public class TweetCommentMongoDBPersistenceBolt extends BaseRichBolt {
	// Creation of logger
	private static final Logger logger = LoggerFactory
			.getLogger(TweetCommentMongoDBPersistenceBolt.class);

	private static final long serialVersionUID = 1L;

	private MongoOperations mongoOperation;
	private final String collectionName = "TweetComment";

	/**
	 * The method prepares the bolt.
	 */
	public void prepare(Map map, TopologyContext context,
			OutputCollector collector) {
		ApplicationContext springContext = new ClassPathXmlApplicationContext(
				"Spring-Config.xml");
		mongoOperation = (MongoOperations) springContext
				.getBean("mongoTemplate");
		
	}

	/**
	 * The method declares output fields.
	 */
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("tweetCommentPersitent"));
	}

	/**
	 * Process each tuple.
	 */
	public void execute(Tuple tuple) {

		TweetComment tweetFeed = (TweetComment) tuple
				.getValueByField("tweetComment");

		logger.info("TweetCommentPersistenceBolt->tweetFeed " + tweetFeed);

		mongoOperation.save(tweetFeed, collectionName);

	}

}
