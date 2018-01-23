package it.univr.fsm.machine;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

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
	
	
	@Test
	public void substringTest5() {

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();
		
		State q0 = new State("q0", true, true);

		states.add(q0);
		
		delta.add(new Transition(q0, q0, "a", ""));
		
		// a^*
		Automaton aStar = new Automaton(delta, states);
		
		
		Automaton result = Automaton.substring(aStar, 0, 3);
		
		HashSet<Automaton> automata = new HashSet<>();
		automata.add(Automaton.makeEmptyString());
		automata.add(Automaton.makeAutomaton("a"));
		automata.add(Automaton.makeAutomaton("aa"));
		automata.add(Automaton.makeAutomaton("aaa"));
		
		// "" | a | aa | aaa
		Automaton expectedResult = Automaton.union(automata);
		
		assertTrue(result.equals(expectedResult));
		
		
		
	}
}
