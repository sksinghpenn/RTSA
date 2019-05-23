package com.lifotech.rtsa.sentiment.alchemy;

import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.likethecolor.alchemy.api.Client;
import com.likethecolor.alchemy.api.call.AbstractCall;
import com.likethecolor.alchemy.api.call.SentimentCall;
import com.likethecolor.alchemy.api.call.type.CallTypeText;
import com.likethecolor.alchemy.api.entity.Response;
import com.likethecolor.alchemy.api.entity.SentimentAlchemyEntity;
import com.likethecolor.alchemy.api.params.Params;

import com.lifotech.rtsa.storm.domain.Sentiment;
import com.lifotech.rtsa.storm.utils.AppConfig;
import com.lifotech.rtsa.storm.utils.AppConstant;

/**
 * The class responsible for finding the sentiment index of a tweet.
 * 
 * @author SK Singh
 *
 */
public final class AlchemySentimentIndexCalculator  {

	// Creation of logger
	private static final Logger logger = LoggerFactory.getLogger(AlchemySentimentIndexCalculator.class);

	/*
	 * The method returns sentiment index of a text.
	 * 
	 * @param text
	 */	
	public static Sentiment getSentimentIndex(String text) {

		Sentiment sentiment = new Sentiment();

		final String apiKey = AppConfig.getPropertyValue(AppConstant.ALCHEMY_API_KEY);
		final Client client = new Client(apiKey);

		Params params = new Params();
		logger.info(params.getOutputMode());

		final AbstractCall<SentimentAlchemyEntity> sentimentCall = new SentimentCall(
				new CallTypeText(text), params);
		Response<SentimentAlchemyEntity> sentimentResponse = null;
		try {
			sentimentResponse = client.call(sentimentCall);
		} catch (IOException e) {
			return null;
		}

		sentiment.setLanguage(sentimentResponse.getLanguage());
		
		sentiment.setStatus(sentimentResponse.getStatus().toString());
		
		sentiment.setStatusInfo(sentimentResponse.getStatusInfo());
		sentiment.setSetimentText(text);
		sentimentResponse.setUsage(sentimentResponse.getUsage());
		

		SentimentAlchemyEntity entity;
		final Iterator<SentimentAlchemyEntity> iter = sentimentResponse
				.iterator();
		while (iter.hasNext()) {
			entity = iter.next();
			sentiment.setMixed(entity.isMixed());
			
			sentiment.setScore(entity.getScore());
			sentiment.setScoreType(entity.getType().toString());
		}

		return sentiment;
	}
	
	/**
	 * Test method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final String apiKey = "7d8bb1934a228d97b843da397f7319917ac0636e";
		
		
		final Client client = new Client(apiKey);

		

		Params params = new Params();
		System.out.println(params.getOutputMode());

		final AbstractCall<SentimentAlchemyEntity> sentimentCall = new SentimentCall(
				new CallTypeText("Modi is doing bad  job"), params);
		final Response<SentimentAlchemyEntity> sentimentResponse = client
				.call(sentimentCall);

		System.out.println("Language: " + sentimentResponse.getLanguage());
		System.out.println("Status: " + sentimentResponse.getStatus());
		System.out.println("Status Info: " + sentimentResponse.getStatusInfo());
		System.out.println("Text: " + sentimentResponse.getText());
		System.out.println("Usage: " + sentimentResponse.getUsage());
		System.out.println("URL: " + sentimentResponse.getURL());

		SentimentAlchemyEntity entity;
		final Iterator<SentimentAlchemyEntity> iter = sentimentResponse
				.iterator();
		while (iter.hasNext()) { // there will only be one object
			entity = iter.next();
			System.out.println("isMixed: "
					+ (entity.isMixed() ? "true" : "false"));
			System.out.println("Score: " + entity.getScore());
			System.out.println("Type: " + entity.getType());
		}
	}

}
