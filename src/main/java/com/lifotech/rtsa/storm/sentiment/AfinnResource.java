package com.lifotech.rtsa.storm.sentiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a class responsible for providing sentiment score to a text.
 *  
 * @author SK Singh
 *
 */
public class AfinnResource {

	//Creation of logger
	static Logger logger = Logger.getLogger("myLogger");

	private static Map<String, Integer> AFINN_WORD_SENTIMENT_MAP;
	private static Set<String> AFINN_WORDS;

	private static int LENGTH_OF_LEAST_LETTER_SENTIMENT_WORD;

	private AfinnResource() {
	}

	/**
	 * The method returns map of sentiment words and their sentiment index.
	 * 
	 * @return {@link Map} of word and sentiment index
	 */
	public static Map<String, Integer> getAfinnWordSentimentMap() {
		if (AFINN_WORD_SENTIMENT_MAP == null) {
			buildAfinnResource();
		}
		return AFINN_WORD_SENTIMENT_MAP;
	}

	/**
	 * The method set of distinct AFINN words.
	 * 
	 * @return {@ Set} of unique words
	 */
	public static Set<String> getAfinnWords() {
		if (AFINN_WORD_SENTIMENT_MAP == null) {
			buildAfinnResource();
			AFINN_WORDS = AFINN_WORD_SENTIMENT_MAP.keySet();
		}
		return AFINN_WORDS;
	}

	/**
	 * The method builds Afinn resource
	 */
	public static synchronized void buildAfinnResource() {		
		AFINN_WORD_SENTIMENT_MAP = new HashMap<String, Integer>();
		BufferedReader buffReader;
		try {
			
			InputStream inputStream =AfinnResource.class.getClassLoader().getResourceAsStream("AFINN/AFINN-111.txt");
				
			buffReader = new BufferedReader(new InputStreamReader(inputStream));
					
			String line = null;

			while ((line = buffReader.readLine()) != null) {

				String[] split = line.trim().split("\\t");		
				
				AFINN_WORD_SENTIMENT_MAP.put(split[0].trim(), Integer.valueOf(split[1].trim()));
			}

		} catch (FileNotFoundException e) {
			  throw new RuntimeException(e);
		} catch (NumberFormatException e) {
			throw new RuntimeException("ERROR: Number format exception.");
		} catch (IOException e) {
			  throw new RuntimeException(e);
		}
	}

	/**
	 * The method gets length of the Afinn word
	 * 
	 * @return length of word 
	 */
	public synchronized static int getLengthOfSmallestAffinWord() {
		if (LENGTH_OF_LEAST_LETTER_SENTIMENT_WORD == 0) {
			if (AFINN_WORD_SENTIMENT_MAP == null) {
				buildAfinnResource();
			}
			lengthOfSmallestAffinWord();
		}
		return LENGTH_OF_LEAST_LETTER_SENTIMENT_WORD;
	}

	/**
	 * Sets of length of least letter word 
	 */
	private static void lengthOfSmallestAffinWord() {

		Set<String> keys = AFINN_WORD_SENTIMENT_MAP.keySet();

		int leastLength = 0;

		for (String str : keys) {
			int len = str.length();
			if (len < leastLength) {
				leastLength = len;
			}
		}

		LENGTH_OF_LEAST_LETTER_SENTIMENT_WORD = leastLength;
	}

	/**
	 * This is main method
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, Integer> map = getAfinnWordSentimentMap();

		logger.log(Level.INFO, map.toString());
	}
}
