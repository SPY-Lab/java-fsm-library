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
	
	@Test
	public void whileStatementTest009() {
		Automaton a = Automaton.makeRealAutomaton("while(x<5){x=1;y=2;}");
		
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
		delta.add(new Transition(q2, q3, "x = 1;"));
		delta.add(new Transition(q3, q4, "y = 2;"));
		delta.add(new Transition(q4, q1, ""));
		delta.add(new Transition(q1, q5, "!(x<5)"));
		

		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest010() {
		Automaton a = Automaton.makeRealAutomaton("while(x<5){x=1;x=2;x=3;}");
		
		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);		
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q2, q3, "x = 1;"));
		delta.add(new Transition(q3, q4, "x = 2;"));
		delta.add(new Transition(q4, q5, "x = 3;"));
		delta.add(new Transition(q5, q1, ""));
		delta.add(new Transition(q1, q6, "!(x<5)"));
		

		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest011() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("a=1;"), Automaton.makeRealAutomaton("b=2;"));
		
		a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("while(x<5){"), a), Automaton.makeRealAutomaton("}"));

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
		delta.add(new Transition(q11, q17, "a = 1;"));
		delta.add(new Transition(q11, q17, "b = 2;"));
		delta.add(new Transition(q17, q6, ""));

		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest012() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("a=1;"), Automaton.makeRealAutomaton("b=2;"));
		a = Automaton.concat(a , Automaton.union(Automaton.makeRealAutomaton("c=3;"), Automaton.makeRealAutomaton("d=4;")));
		a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("while(x<5){"), a), Automaton.makeRealAutomaton("}"));

		Automaton realResult = parser.reduceProgram(a);

		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q6, "!(x<5)"));
		delta.add(new Transition(q1, q3, "(x<5)"));
		delta.add(new Transition(q3, q4, "a = 1;"));
		delta.add(new Transition(q3, q4, "b = 2;"));
		delta.add(new Transition(q4, q5, "c = 3;"));
		delta.add(new Transition(q4, q5, "d = 4;"));
		delta.add(new Transition(q5, q1, ""));

		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest013() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("a=1;"), Automaton.makeRealAutomaton("aaa=1;"));
		
		a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("while(x<5){"), a), Automaton.makeRealAutomaton("}"));

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
		delta.add(new Transition(q11, q17, "a = 1;"));
		delta.add(new Transition(q11, q17, "aaa = 1;"));
		delta.add(new Transition(q17, q6, ""));

		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void whileStatementTest014() {
		
		Automaton five = Automaton.concat(Automaton.star("5"), Automaton.makeRealAutomaton(";"));
		Automaton a = Automaton.concat(Automaton.makeRealAutomaton("x=5"), five);
		Automaton b = Automaton.concat(Automaton.makeRealAutomaton("y=5"), five);
		
		a = Automaton.union(a,b);
		a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("while(x<5){"), a), Automaton.makeRealAutomaton("}"));

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
		delta.add(new Transition(q11, q17, "x = 5(5)*;"));
		delta.add(new Transition(q11, q17, "y = 5(5)*;"));
		delta.add(new Transition(q17, q6, ""));

		assertEquals(new Automaton(delta,states), realResult); 
	}
	

	@Test
	public void whileStatementTest015() {
		
		Automaton a = Automaton.makeRealAutomaton("while(x<5){while(x<6){x=1;}}");
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q1, q6, "!(x<5)"));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q2, q3, ""));
		delta.add(new Transition(q3, q4, "(x<6)"));
		delta.add(new Transition(q4, q5, "x = 1;"));
		delta.add(new Transition(q5, q3, ""));
		delta.add(new Transition(q3, q1, "!(x<6)"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void whileStatementTest016() {
		
		Automaton a = Automaton.makeRealAutomaton("while(x<5){while(x<6){x=1;x=2;}}");
		
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
		delta.add(new Transition(q1, q7, "!(x<5)"));
		delta.add(new Transition(q1, q2, "(x<5)"));
		delta.add(new Transition(q2, q3, ""));
		delta.add(new Transition(q3, q4, "(x<6)"));
		delta.add(new Transition(q4, q5, "x = 1;"));
		delta.add(new Transition(q5, q6, "x = 2;"));
		delta.add(new Transition(q6, q3, ""));
		delta.add(new Transition(q3, q1, "!(x<6)"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest017() {
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("while(x<5){x=1;y=2;}"), Automaton.makeRealAutomaton("whilea=2;"));
		
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
		delta.add(new Transition(q2, q3, "x = 1;"));
		delta.add(new Transition(q3, q4, "y = 2;"));
		delta.add(new Transition(q4, q1, ""));
		delta.add(new Transition(q1, q5, "!(x<5)"));
		delta.add(new Transition(q0, q5, "whilea = 2;"));


		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest018() {
		
		Automaton five = Automaton.concat(Automaton.star("5"), Automaton.makeRealAutomaton(";"));
		Automaton a = Automaton.concat(Automaton.makeRealAutomaton("x=5"), five);

		Automaton first = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("while(x<5){"), a), Automaton.makeRealAutomaton("}"));
		Automaton second = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("while(y<5){"), a), Automaton.makeRealAutomaton("}"));
		
		Automaton input = Automaton.union(first, second);
		
		Automaton realResult = parser.reduceProgram(input);

		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, true);
		State q4 = new State("q4", false, false);		
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, false);
		State q7 = new State("q7", false, false);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		states.add(q7);

		delta.add(new Transition(q0, q1, ""));
		delta.add(new Transition(q0, q2, ""));
		delta.add(new Transition(q1, q3, "!(y<5)"));
		delta.add(new Transition(q1, q4, "(y<5)"));
		delta.add(new Transition(q4, q5, "x = 5(5)*;"));
		delta.add(new Transition(q5, q1, ""));
		delta.add(new Transition(q2, q3, "!(x<5)"));
		delta.add(new Transition(q2, q7, "(x<5)"));
		delta.add(new Transition(q7, q6, "x = 5(5)*;"));
		delta.add(new Transition(q6, q2, ""));


		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void whileStatementTest019() {
		Automaton a = Automaton.makeRealAutomaton("while(x<5){if(x<7){x=1;}else{x=2;}}");

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);		
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, false);
		State q7 = new State("q7", false, false);
		State q8 = new State("q8", false, false);

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
		delta.add(new Transition(q1, q2, "!(x<5)"));
		delta.add(new Transition(q1, q3, "(x<5)"));
		delta.add(new Transition(q3, q4, ""));
		delta.add(new Transition(q4, q6, "!(x<7)"));
		delta.add(new Transition(q4, q5, "(x<7)"));
		

		delta.add(new Transition(q5, q7, "x = 1;"));
		delta.add(new Transition(q6, q7, "x = 2;"));
		delta.add(new Transition(q7, q8, ""));
		delta.add(new Transition(q8, q1, ""));
				
		assertEquals(new Automaton(delta,states), realResult); 
	}
}
