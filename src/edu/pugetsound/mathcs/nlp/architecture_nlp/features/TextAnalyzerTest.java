package edu.pugetsound.mathcs.nlp.architecture_nlp.features;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import edu.pugetsound.mathcs.nlp.architecture_nlp.brain.DialogueActTag;
import edu.pugetsound.mathcs.nlp.controller.Controller;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Punctuation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class TextAnalyzerTest {
	
	private TextAnalyzer analyzer;	
	private Conversation conversation;
	private String statement = "The cat ate the fish.";
	private String question = "What are you doing?";
	private String empty = "";
	private String greeting = "hello!";

	@Before
	public void setUp() throws Exception {
		analyzer = new TextAnalyzer(null);
		conversation = new Conversation();
	}

	@Test
	public void testPopulateHash(){
		assertTrue("Hash of greetings/closings not populated", analyzer.greetClose.size() > 0);
		assertTrue("Hash of standardized forms not populated", analyzer.standardizedForms.size() > 0);		
		assertTrue("Hash of greetings/closings does not contain 'hello'", analyzer.greetClose.containsKey("hello"));
		assertTrue("Hash of greetings/closings does not contain 'goodbye'", analyzer.greetClose.containsKey("goodbye"));	
		assertEquals(DialogueActTag.CONVENTIONAL_OPENING, analyzer.greetClose.get("hello"));
		assertEquals(DialogueActTag.CONVENTIONAL_CLOSING, analyzer.greetClose.get("goodbye"));		
		assertEquals("What are you doing?", analyzer.standardizedForms.get("what's up"));
	}
	
	
	@Test
	public void testGreetCloseShortCircuit(){
		Utterance utt = analyzer.analyze(greeting, conversation);		
		assertEquals(greeting, utt.utterance);
		assertEquals(1, utt.tokens.size());
		assertEquals(Punctuation.EXCLAMATION, utt.punct);
		assertFalse(utt.isPassive);
		assertNull(utt.constituencyParse);
		assertNull(utt.dependencyParse);
		assertEquals(0, utt.resolutions.size());
		assertNull(utt.firstOrderRep);
		assertEquals(DialogueActTag.CONVENTIONAL_OPENING, utt.daTag);		
		assertEquals(0, utt.subjects.size());
		assertEquals(0, utt.directObjects.size());
		assertNull(utt.rootConstituency);
		assertNull(utt.rootDependency);						
	}
		
	
	@Test
	public void testStatement() {
		Utterance utt = analyzer.analyze(statement, conversation);
		
		
		assertEquals(statement, utt.utterance);
		assertFalse(utt.isPassive);
		assertEquals(5, utt.tokens.size());
		assertEquals(Punctuation.PERIOD, utt.punct);
		assertEquals("(ROOT (S (NP (DT The) (NN cat)) (VP (VBD ate) (NP (DT the) (NN fish))) (. .)))", 
				utt.constituencyParse.toString());
		assertEquals(1, utt.subjects.size());
		assertEquals(1, utt.directObjects.size());
		assertEquals("cat", utt.subjects.get(0));
		assertEquals("fish", utt.directObjects.get(0));
		assertEquals("S", utt.rootConstituency);
		assertEquals("ate", utt.rootDependency);		
	}

	@Test
	public void testEmpty() {
		Utterance utt = analyzer.analyze(empty, conversation);		
		assertEquals(empty, utt.utterance);
		assertFalse(utt.isPassive);
		assertEquals(0, utt.tokens.size());
		assertEquals(Punctuation.UNKNOWN, utt.punct);		
		assertNull(utt.constituencyParse);
		assertNull(utt.dependencyParse);
		assertEquals(0, utt.subjects.size());
		assertEquals(0, utt.directObjects.size());
		assertNull(utt.rootConstituency);
		assertNull(utt.rootDependency);		
	}

	@Test
	public void testQuestion() {
		Utterance utt = analyzer.analyze(question, conversation);		
		assertEquals(question, utt.utterance);
		assertFalse(utt.isPassive);
		assertEquals(4, utt.tokens.size());
		assertEquals(Punctuation.QUEST_MARK, utt.punct);		
		assertEquals(
			"(ROOT (SBARQ (WHNP (WP What)) (SQ (VBP are) (NP (PRP you)) (VP (VBG doing))) (. ?)))", 
			utt.constituencyParse.toString());
		assertEquals(1, utt.subjects.size());
		assertEquals(1, utt.directObjects.size());
		assertEquals("you", utt.subjects.get(0));
		assertEquals("What", utt.directObjects.get(0));
		assertEquals("SBARQ", utt.rootConstituency);
		assertEquals("doing", utt.rootDependency);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUtteranceNull(){
		analyzer.analyze(null, conversation);		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConversationNull(){
		analyzer.analyze("",null);
	}
}
