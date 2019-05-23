package com.lifotech.rtsa.storm.spouts;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.lifotech.rtsa.storm.utils.AppConfig;
import com.lifotech.rtsa.storm.utils.AppConstant;

/**
 * This is Spout component of the Storm cluster. The Spout class is the source
 * of stream. It emits the event(tuple)(Tweet) on SpoutOutputCollector(output
 * stream) for the Bolt component.
 * 
 * @author SK Singh
 * 
 */
public class RTSASpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(RTSASpout.class);

	private TwitterStream twitterStream;
	private LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<Status>();
	private SpoutOutputCollector outputCollector;
	private String[] twitterHandles;
	private String clientID = "";

	public RTSASpout(String[] twitterHandles, String clientID) {
		this.twitterHandles = twitterHandles;
		this.clientID = clientID;
	}

	/**
	 * The method sets up the Spout in terms of setting up twitter filter.
	 */
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

		this.outputCollector = collector;
		this.twitterStream = getTwitterStream();

		FilterQuery filterQuery = new FilterQuery();

		filterQuery.track(twitterHandles);

		twitterStream.addListener(this.new TwitterStreamSpoutStatusListener());
		twitterStream.filter(filterQuery);

	}

	/**
	 * The method reads the next tuple from the queue
	 */
	public void nextTuple() {
		Status status = queue.poll();

		if (status == null) {
			// let the thread sleep
			Utils.sleep(1000);
		} else {
			outputCollector.emit(new Values(status, clientID));
		}
	}

	/**
	 * The method declares output stream where Spout emits the result for the
	 * the Bolt.
	 */
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("tweet", "clientID"));
	}

	@Override
	public final void close() {
		twitterStream.cleanUp();
		twitterStream.shutdown();
	}

	@Override
	public final void ack(final Object id) {

	}

	@Override
	public final void fail(final Object id) {

	}

	private TwitterStream getTwitterStream() {
		ConfigurationBuilder configBuilder = new ConfigurationBuilder();

		String oAuthAccessToken = AppConfig.getPropertyValue(AppConstant.OAUTH_ACCESS_TOKEN);
		configBuilder.setOAuthAccessToken(oAuthAccessToken);

		String oAuthAccessTokenSecret = AppConfig.getPropertyValue(AppConstant.OAUTH_ACCESS_TOKEN_SECRET);
		configBuilder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);

		String oAuthConsumerKey = AppConfig.getPropertyValue(AppConstant.OAUTH_CONSUMER_KEY);
		configBuilder.setOAuthConsumerKey(oAuthConsumerKey);

		String oAuthConsumerSecret = AppConfig.getPropertyValue(AppConstant.OAUTH_CONSUMER_SECRET);
		configBuilder.setOAuthConsumerSecret(oAuthConsumerSecret);

		TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(configBuilder.build());

		TwitterStream stream = twitterStreamFactory.getInstance();

		return stream;

	}

	/**
	 * This is inner class implements StatusListener interface.
	 * 
	 * @author SK Singh
	 * 
	 */
	private class TwitterStreamSpoutStatusListener implements StatusListener {

		public void onException(Exception e) {
			logger.error(e.getMessage());
		}

		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			logger.info(statusDeletionNotice.toString());

		}

		public void onScrubGeo(long arg0, long arg1) {

		}

		public void onStallWarning(StallWarning stallWarning) {
			logger.error(stallWarning.toString());

		}

		public void onStatus(Status status) {
			queue.offer(status);
		}

		public void onTrackLimitationNotice(int arg0) {

		}

	}

	/**
	 * This is just a test method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String[] keywords = { "@Modi", "@NarendraModi", "@PMOIndia" };
		String clientID = "1001";
		RTSASpout twitterStreamSpout = new RTSASpout(keywords, clientID);
		TwitterStream twitterStream = twitterStreamSpout.getTwitterStream();
		logger.info("twitterStream " + twitterStream);

		FilterQuery filterQuery = new FilterQuery();

		filterQuery.track(keywords);

		twitterStream.addListener(twitterStreamSpout.new TwitterStreamSpoutStatusListener());
		twitterStream.filter(filterQuery);
	}
}
