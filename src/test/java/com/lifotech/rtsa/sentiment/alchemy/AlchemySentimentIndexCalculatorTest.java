package com.lifotech.rtsa.sentiment.alchemy;

import com.lifotech.rtsa.storm.domain.Sentiment;
import org.junit.Test;

public class AlchemySentimentIndexCalculatorTest {

    @Test(expected = NullPointerException.class)
    public void testGetSentimentIndex() {
        Sentiment sentiment = AlchemySentimentIndexCalculator.getSentimentIndex("I love you");


        sentiment.getScore();

    }
}
