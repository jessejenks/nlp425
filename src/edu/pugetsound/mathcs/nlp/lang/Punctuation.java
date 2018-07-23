package edu.pugetsound.mathcs.nlp.lang;

import java.util.regex.Pattern;

/**
 * Represents the ending punctuation mark of an utterance
 * @author alchambers
 */
public enum Punctuation {
	QUEST_MARK("?"),
	PERIOD("."),
	EXCLAMATION("!"),
	ELLIPSIS("..."),
	UNKNOWN("");
	
	private String punct;
	
	Punctuation(String punct){
		this.punct = punct;
	}	
	
	public String toString(){
		return punct;
	}	
}
