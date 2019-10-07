package it.univr.parsing;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class IfStatementTest {
	
	AbstractParser parser = new AbstractParser();

	
	@Test
	public void ifStatementTest001() {
		Automaton a = Automaton.makeRealAutomaton("if(x<5){x=1;x=2;}else{x=3;x=4;}");

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
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q2, q4, "x = 1;"));
		delta.add(new Transition(q4, q6, "x = 2;"));
		delta.add(new Transition(q3, q5, "x = 3;"));
		delta.add(new Transition(q5, q6, "x = 4;"));
		delta.add(new Transition(q6, q7, ""));
		
		
		assertEquals(new Automaton(delta,states), (realResult)); 
	}
	

	@Test
	public void ifStatementTest002() {
		Automaton a = Automaton.makeRealAutomaton("if(x<5){x=1;x=2;}else{x=3;x=4;}y=1;");

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
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q2, q4, "x = 1;"));
		delta.add(new Transition(q4, q6, "x = 2;"));
		delta.add(new Transition(q3, q5, "x = 3;"));
		delta.add(new Transition(q5, q6, "x = 4;"));
		delta.add(new Transition(q6, q7, ""));
		delta.add(new Transition(q7, q8, "y = 1;"));
		
		
		assertEquals(new Automaton(delta,states), (realResult)); 
	}
	

	@Test
	public void ifStatementTest003() {
		Automaton a = Automaton.makeRealAutomaton("a=1;if(x<5){x=1;x=2;}else{x=3;x=4;}y=1;");

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q9 = new State("q9", true, false);
		State q0 = new State("q0", false, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, false);
		State q7 = new State("q7", false, false);
		State q8 = new State("q8", false, true);

		states.add(q0);
		states.add(q9);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		states.add(q7);
		states.add(q8);

		delta.add(new Transition(q9, q0, "a = 1;"));
		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q2, q4, "x = 1;"));
		delta.add(new Transition(q4, q6, "x = 2;"));
		delta.add(new Transition(q3, q5, "x = 3;"));
		delta.add(new Transition(q5, q6, "x = 4;"));
		delta.add(new Transition(q6, q7, ""));
		delta.add(new Transition(q7, q8, "y = 1;"));
		
		
		assertEquals(new Automaton(delta,states), (realResult)); 
	}
	
	@Test
	public void ifStatementTest004() {
		Automaton a = Automaton.makeRealAutomaton("if(x<5){x=1;}else{");
		a = Automaton.concat(a, Automaton.union(Automaton.makeAutomaton("x=2;}"), Automaton.makeAutomaton("x=3;}")));

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q2, q4, "x = 1;"));
		delta.add(new Transition(q3, q4, "x = 3;"));
		delta.add(new Transition(q3, q4, "x = 2;"));
		delta.add(new Transition(q4, q5, ""));
		
		assertEquals(new Automaton(delta,states), (realResult)); 
	}
	
	@Test
	public void ifStatementTest006() {
		Automaton a = Automaton.makeRealAutomaton("if(x<5){x=1;}else{x=2;}");
		Automaton b = Automaton.makeRealAutomaton("if(y<5){x=1;}else{x=2;}");

		Automaton c = Automaton.union(a,b);
		
		Automaton realResult = parser.reduceProgram(c);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q1, q2, "(y<5)"));
		delta.add(new Transition(q1, q3, "!(y<5)"));
		delta.add(new Transition(q2, q4, "x = 1;"));
		delta.add(new Transition(q3, q4, "x = 2;"));
		delta.add(new Transition(q4, q5, ""));
		
		assertEquals(new Automaton(delta,states), (realResult)); 
	}
	
	@Test
	public void ifStatementTest007() {
		Automaton a = Automaton.makeRealAutomaton("if(x<5){x=1;}else{x=2;}");
		Automaton b = Automaton.makeRealAutomaton("if(y<5){x=1;}else{x=2;}");
		Automaton c = Automaton.makeRealAutomaton("a=1;");

		Automaton d = Automaton.union(a,b,c);
		
		Automaton realResult = parser.reduceProgram(d);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q1, q2, "(y<5)"));
		delta.add(new Transition(q1, q3, "!(y<5)"));
		delta.add(new Transition(q2, q4, "x = 1;"));
		delta.add(new Transition(q3, q4, "x = 2;"));
		delta.add(new Transition(q4, q5, ""));
		delta.add(new Transition(q0, q5, "a = 1;"));
		
		assertEquals(new Automaton(delta,states), (realResult)); 
	}
	
	@Test
	public void ifStatementTest005() {
		Automaton a = Automaton.makeRealAutomaton("if(x<5){if(x<6){x=1;}else{x=2;}}else{if(x<7){x=3;}else{x=4;}}");

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
		State q8 = new State("q8", false, false);
		State q9 = new State("q9", false, false);
		State q10 = new State("q10", false, false);
		State q11 = new State("q11", false, false);
		State q12 = new State("q12", false, false);
		State q13 = new State("q13", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		states.add(q7);
		states.add(q8);
		states.add(q9);
		states.add(q10);
		states.add(q11);
		states.add(q12);
		states.add(q13);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q2, q4, ""));
		delta.add(new Transition(q3, q5, ""));
		delta.add(new Transition(q4, q6, "(x<6)"));
		delta.add(new Transition(q4, q7, "!(x<6)"));
		delta.add(new Transition(q5, q8, "(x<7)"));
		delta.add(new Transition(q5, q9, "!(x<7)"));
		delta.add(new Transition(q6, q10, "x = 1;"));
		delta.add(new Transition(q7, q10, "x = 2;"));
		delta.add(new Transition(q8, q11, "x = 3;"));
		delta.add(new Transition(q9, q11, "x = 4;"));
		delta.add(new Transition(q10, q12, ""));
		delta.add(new Transition(q11, q12, ""));
		delta.add(new Transition(q12, q13, ""));
		
		assertEquals(new Automaton(delta,states), (realResult)); 
	}


	@Test
	public void ifStatementTest008() {
		Automaton five = Automaton.concat(Automaton.star("5"), Automaton.makeRealAutomaton(";"));
		Automaton a = Automaton.concat(Automaton.makeRealAutomaton("x=5"), five);
		
		a = Automaton.concat(Automaton.makeRealAutomaton("if(x<5){"), a);
		a = Automaton.concat(a, Automaton.makeRealAutomaton("}else{x=1;}"));

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
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q1, q3, "!(x<5)"));
		delta.add(new Transition(q2, q4, ""));
		delta.add(new Transition(q4, q6, "x = 5(5)*;"));
		delta.add(new Transition(q3, q5, ""));
		delta.add(new Transition(q5, q6, "x = 1;"));
		delta.add(new Transition(q6, q7, ""));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
}
