package it.univr.fsm.machine;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SubstringTest {


	@Test
	public void substringTest1() { 
		Automaton a = Automaton.makeAutomaton("a");
		
		Automaton result = Automaton.substring(a, 0, 1);
		
		assertTrue(result.equals(Automaton.makeAutomaton("a")));
	}
	
	@Test
	public void substringTest2() { 
		Automaton a = Automaton.makeAutomaton("a");
		
		Automaton result = Automaton.substring(a, 0, 0);

		assertTrue(result.equals(Automaton.makeEmptyString()));
	}
	
	@Test
	public void substringTest3() { 
		Automaton a = Automaton.makeAutomaton("a");
		Automaton b = Automaton.makeAutomaton("b");
		
		Automaton aorb = Automaton.union(a, b);
		
		Automaton result = Automaton.substring(aorb, 0, 1);

		assertTrue(result.equals(aorb));
	}
	
	@Test
	public void substringTest4() { 
		Automaton a = Automaton.makeAutomaton("abc");
		Automaton b = Automaton.makeAutomaton("def");
		
		Automaton aorb = Automaton.union(a, b);

		Automaton result = Automaton.substring(aorb, 1, 2);
		Automaton expectedResult = Automaton.union(Automaton.makeAutomaton("b"), Automaton.makeAutomaton("e"));
		
		assertTrue(result.equals(expectedResult));
	}
}
