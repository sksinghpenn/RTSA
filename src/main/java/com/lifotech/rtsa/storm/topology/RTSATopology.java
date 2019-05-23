package com.lifotech.rtsa.storm.topology;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import com.lifotech.rtsa.storm.bolts.TweetCommentBolt;
import com.lifotech.rtsa.storm.bolts.TweetCommentHBasePersistenceBolt;
import com.lifotech.rtsa.storm.spouts.RTSASpout;
import com.lifotech.rtsa.storm.utils.AppConstant;

/**
 * The class is the main topology class, which knows about the spout and the
 * bolts.
 * 
 * @author SK Singh
 */
public final class RTSATopology {

	private static final Logger logger = LoggerFactory.getLogger(RTSATopology.class);

	private RTSATopology() {
	}

	/**
	 * The main method which starts the Storm cluster.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static final void main(final String[] args) {
		String clientID = args[0];
		logger.info("clientID " + clientID);

		List<String> twitterHandleList = getTwitterHandleList(args);

		try {
			final Config config = new Config();
			config.setMessageTimeoutSecs(120);
			config.setDebug(false);

			TopologyBuilder topologyBuilder = new TopologyBuilder();

			String[] twitterHandles = twitterHandleList.toArray(new String[(twitterHandleList.size())]);

			// Setup Spout
			topologyBuilder.setSpout(AppConstant.RTSA_SPOUT, new RTSASpout(twitterHandles, clientID));

			// add bolt to Spout
			topologyBuilder.setBolt(AppConstant.TWEET_COMMENT_BOLT, new TweetCommentBolt())
					.shuffleGrouping("rtsaSpout");

			topologyBuilder.setBolt(AppConstant.TWEET_COMMENT_PERSISTENCE_BOLT, new TweetCommentHBasePersistenceBolt())
					.shuffleGrouping(AppConstant.TWEET_COMMENT_BOLT);

			config.setMaxTaskParallelism(10);

			final LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology(AppConstant.TOPOLOGY_NAME, config, topologyBuilder.createTopology());

		} catch (Exception e) {
			logger.error("The RTSA cluster could be not started");
			logger.error(e.getMessage());
		}
	}

	private static List<String> getTwitterHandleList(final String[] args) {
		List<String> twitterHandleList = new ArrayList<String>();

		for (int i = 1; i < args.length; i++) {

			twitterHandleList.add(args[i]);
		}
		return twitterHandleList;
	}
}