package it.univr.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class WhileStatementTest {
	AbstractParser parser = new AbstractParser();

	@Test
	public void whileStatementTest001() {
		Automaton a = Automaton.makeRealAutomaton("while(x<5){x=x+1;}");
		assertTrue(!parser.mayReduceWhile(a, a.getInitialState()).isEmpty()); 
	}

	@Test
	public void whileStatementTest002() {
		Automaton a = Automaton.makeRealAutomaton("awhile(x<5){x=x+1;}");
		assertTrue(parser.mayReduceWhile(a, a.getInitialState()).isEmpty()); 
	}

	@Test
	public void whileStatementTest003() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("while(x<5){x=x+1;}"), Automaton.makeRealAutomaton("x=x+1;"));
		assertTrue(!parser.mayReduceWhile(a, a.getInitialState()).isEmpty()); 
	}

	@Test
	public void whileStatementTest004() {
		Automaton a = Automaton.makeRealAutomaton("while(x<5){x=x+1;}");

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q6 = new State("q6", false, false);
		State q11 = new State("q11", false, false);
		State q17 = new State("q17", false, false);
		State q18 = new State("q1", false, true);

		states.add(q0);
		states.add(q18);
		states.add(q11);
		states.add(q6);
		states.add(q17);

		delta.add(new Transition(q0, q6, ""));
		delta.add(new Transition(q6, q18, "!(x<5)"));
		delta.add(new Transition(q6, q11, "(x<5)"));
		delta.add(new Transition(q11, q17, "x = x+1;"));
		delta.add(new Transition(q17, q6, ""));

		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void whileStatementTest005() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=x+1;") ,Automaton.makeRealAutomaton("while(x<5){x=x+1;}"));

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q6 = new State("q6", false, false);
		State q11 = new State("q11", false, false);
		State q17 = new State("q17", false, false);
		State q18 = new State("q1", false, true);

		states.add(q0);
		states.add(q18);
		states.add(q11);
		states.add(q6);
		states.add(q17);

		delta.add(new Transition(q0, q6, ""));
		delta.add(new Transition(q0, q18, "x = x+1;"));
		delta.add(new Transition(q6, q18, "!(x<5)"));
		delta.add(new Transition(q6, q11, "(x<5)"));
		delta.add(new Transition(q11, q17, "x = x+1;"));
		delta.add(new Transition(q17, q6, ""));

		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest006() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("while(y<6){y=y+1;}") ,Automaton.makeRealAutomaton("while(x<5){x=x+1;}"));

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);		
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, false);
		State q7 = new State("q7", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		states.add(q7);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q0, q4, ""));
		delta.add(new Transition(q1, q2, "(y<6)"));
		delta.add(new Transition(q1, q7, "!(y<6)"));
		delta.add(new Transition(q2, q3, "y = y+1;"));
		delta.add(new Transition(q3, q1, ""));
		

		delta.add(new Transition(q4, q5, "(x<5)"));
		delta.add(new Transition(q4, q7, "!(x<5)"));
		delta.add(new Transition(q5, q6, "x = x+1;"));
		delta.add(new Transition(q6, q4, ""));
		
	
		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void whileStatementTest007() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("while(y<6){y=y+1;}") ,Automaton.makeRealAutomaton("while(x<5){x=x+1;}"));

		a = Automaton.concat(a, Automaton.makeRealAutomaton("a=a+1;"));
		
		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);		
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, false);
		State q7 = new State("q7", false, false);
		State q8 = new State("q8", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		states.add(q7);
		states.add(q8);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q0, q4, ""));
		delta.add(new Transition(q1, q2, "(y<6)"));
		delta.add(new Transition(q1, q7, "!(y<6)"));
		delta.add(new Transition(q2, q3, "y = y+1;"));
		delta.add(new Transition(q3, q1, ""));
		

		delta.add(new Transition(q4, q5, "(x<5)"));
		delta.add(new Transition(q4, q7, "!(x<5)"));
		delta.add(new Transition(q5, q6, "x = x+1;"));
		delta.add(new Transition(q6, q4, ""));
		

		delta.add(new Transition(q7, q8, "a = a+1;"));

		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void whileStatementTest008() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("while(y<6){y=y+1;}") ,Automaton.makeRealAutomaton("while(x<5){x=x+1;}"));

		a = Automaton.concat(a, Automaton.union(Automaton.makeRealAutomaton("a=a+1;"), Automaton.makeRealAutomaton("a=b+1;")));
		
		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);		
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, false);
		State q7 = new State("q7", false, false);
		State q8 = new State("q8", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		states.add(q7);
		states.add(q8);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q0, q4, ""));
		delta.add(new Transition(q1, q2, "(y<6)"));
		delta.add(new Transition(q1, q7, "!(y<6)"));
		delta.add(new Transition(q2, q3, "y = y+1;"));
		delta.add(new Transition(q3, q1, ""));
		

		delta.add(new Transition(q4, q5, "(x<5)"));
		delta.add(new Transition(q4, q7, "!(x<5)"));
		delta.add(new Transition(q5, q6, "x = x+1;"));
		delta.add(new Transition(q6, q4, ""));
		

		delta.add(new Transition(q7, q8, "a = a+1;"));
		delta.add(new Transition(q7, q8, "a = b+1;"));

		assertEquals(new Automaton(delta,states), realResult); 
	}
}
