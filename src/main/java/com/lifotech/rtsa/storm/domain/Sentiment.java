package com.lifotech.rtsa.storm.domain;


/**
 * 
 * The domain class encapsulates sentiment relates attributes.
 * 
 * @author SK Singh
 *
 */
public class Sentiment {
	
	private String setimentText;
	
	/**
	 * 	the detected language that the source text was written in.
	 */
	private String language = "";
	
	
	/**
	 * success / failure status indicating whether the request was processed.
	 * Possible values:	 OK, ERROR
	 */	
	private String status = "";
	
	/**
	 * failure status information (sent only if "status" == "ERROR").
	 * Possible values:invalid-api-key, unsupported-text-language
	 */
	private String statusInfo ="";	
	
	/**
	 * Score: sentiment strength (0.0 == neutral)
	 */
	
	private double score = 0.0;
	
	/**
	 * Score Type: sentiment polarity: "positive", "negative", or "neutral"
	 * 1 : Mixed
	 * and 0: Neutral
	 */	
	private String scoreType = "";
	
	/**
	 * Mixed: whether sentiment is mixed (both positive and negative) (1 == mixed)
	 */
	private boolean isMixed = false;

	public String getSetimentText() {
		return setimentText;
	}

	public void setSetimentText(String setimentText) {
		this.setimentText = setimentText;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public boolean isMixed() {
		return isMixed;
	}

	public void setMixed(boolean isMixed) {
		this.isMixed = isMixed;
	}
	

	

}
