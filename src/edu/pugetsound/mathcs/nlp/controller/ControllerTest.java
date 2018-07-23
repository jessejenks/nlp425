package edu.pugetsound.mathcs.nlp.controller;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class ControllerTest {
	/**
	 * Allows us to unit test input/output functionality
	 * This is the input stream which is set to a string
	 */
	private ByteArrayInputStream in;
	private ByteArrayOutputStream outputArray;
	private PrintStream outputStream;


	@Before
	public void setUp() throws Exception {
		String input = "Is Fluffy a cat?";
		in = new ByteArrayInputStream(input.getBytes());
		outputArray = new ByteArrayOutputStream();
		outputStream = new PrintStream(outputArray);
		Controller.setup(in, outputStream);
	}

	/**
	 * TODO: Use reflection to replace this with a loop over the
	 * instance variables (whatever they may be)
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testSetup() throws ClassNotFoundException {		
		assertNotNull(Controller.conversation);	
		assertNotNull(Controller.input);
		assertNotNull(Controller.analyzer);
		assertNotNull(Controller.brain);
		assertNotNull(Controller.kb);
		assertNotNull(Controller.decoder);
	}

	@Test
	public void testRespondToUser() {
		Utterance utt = new Utterance("hello");
		Controller.respondToUser(utt);		
		String printedResponse = outputArray.toString().trim();
		assertEquals("Agent: hello", printedResponse);
	}

	@Test
	public void testInitiateGreeting() {
		outputArray.reset();
		Controller.initiateGreeting();
		assertEquals(1, Controller.conversation.size());		
	}

}
