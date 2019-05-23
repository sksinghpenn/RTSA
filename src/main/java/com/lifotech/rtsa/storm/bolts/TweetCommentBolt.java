package com.lifotech.rtsa.storm.bolts;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.User;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.lifotech.rtsa.sentiment.alchemy.AlchemySentimentIndexCalculator;
import com.lifotech.rtsa.storm.domain.Sentiment;
import com.lifotech.rtsa.storm.domain.TweetComment;
import com.lifotech.rtsa.storm.sentiment.AfinnResource;

/**
 * This is a Bolt class, responsible for creating domain object and adding sentiment index. 
 * 
 * @author SK Singh
 *
 */
public class TweetCommentBolt extends BaseRichBolt {

	// Creation of logger
	private static final Logger logger = LoggerFactory
			.getLogger(TweetCommentBolt.class);

	private static final long serialVersionUID = 1L;

	private OutputCollector outputCollector;

	private Set<String> affinWords;

	/*
	 * Prepares the Bolt
	 */
	public void prepare(Map map, TopologyContext context,
			OutputCollector collector) {
		this.outputCollector = collector;
		affinWords = AfinnResource.getAfinnWords();		
	}

	/*
	 * Sets up the output stream for the another bolt.
	 */
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("tweetComment", "clientID"));
	}

	/*
	 * Processes tuple; creates domain object and emits it on the output stream.
	 */
	public void execute(Tuple tuple) {

		final Status status = (Status) tuple.getValueByField("tweet");
		final String clientID = (String) tuple.getValueByField("clientID");

		String tweetText = status.getText();

		Sentiment sentiment = AlchemySentimentIndexCalculator
				.getSentimentIndex(tweetText);

		if (sentiment == null)
			return;

		long tweetID = status.getId();

		User user = status.getUser();

		String screenName = user.getScreenName();
		String userName = user.getName();
		Date tweetTime = status.getCreatedAt();

		TweetComment tweetFeed = new TweetComment();

		tweetFeed.setTweetID(tweetID);
		tweetFeed.setUserName(userName);
		tweetFeed.setScrenName(screenName);
		tweetFeed.setTweetText(tweetText);
		tweetFeed.setTweetTime(tweetTime);

		tweetFeed.setSource(status.getSource());

		// tweetFeed.setSentimentWordCountMap(sentimentWordCountMap);
		tweetFeed.setSentimenIndex(sentiment.getScore());
		logger.info(tweetFeed.toString());

		outputCollector.emit(new Values(tweetFeed, clientID));

	}



	/*
	 * Test method
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "one,two,three,four,\"five1,five2\", 	six ,seven,\"eight1,ei'ght2\",\"nine\",,eleven";

		String delim = "[\\s,\",']";
		String[] split = str.split(delim);

		System.out.println(Arrays.asList(split));

	}
}
