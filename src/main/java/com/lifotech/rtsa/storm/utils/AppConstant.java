package com.lifotech.rtsa.storm.utils;

/**
 * 
 * The class acts as a placeholder for application constants.
 * 
 * @author SK Singh
 * 
 */

public class AppConstant {
	private AppConstant() {
	};

	public static final String OAUTH_ACCESS_TOKEN = "OAUTH_ACCESS_TOKEN";
	public static final String OAUTH_ACCESS_TOKEN_SECRET = "OAUTH_ACCESS_TOKEN_SECRET";
	public static final String OAUTH_CONSUMER_KEY = "OAUTH_CONSUMER_KEY";
	public static final String OAUTH_CONSUMER_SECRET = "OAUTH_CONSUMER_SECRET";

	public static final String ALCHEMY_API_KEY = "ALCHEMY_API_KEY";

	public static final String TOPOLOGY_NAME = "RTSATopology";

	public static final String APP_CONFIG_FILE_NAME = "AppConfig.properties";
	
	
	public static final String RTSA_SPOUT = "rtsaSpout";
	
	public static final String TWEET_COMMENT_BOLT  = "tweetCommentBolt";
	
	public static final String TWEET_COMMENT_PERSISTENCE_BOLT = "TweetCommentPersistenceBolt";
	

}
