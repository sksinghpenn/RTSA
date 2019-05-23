package com.lifotech.rtsa.storm.spouts;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;


/**
 * This is a KafKa consumer.
 * 
 * @author SK Singh
 * 
 */

public class RTSASpoutConsumer extends KafkaSpout {	
	
	public RTSASpoutConsumer(SpoutConfig spoutConf) {
		super(spoutConf);		
	}

	
	
}
