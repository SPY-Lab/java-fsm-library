package it.univr.fsm.machine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReverseTest {


	@Test
	public void reverseTest001() {
		Automaton a = Automaton.makeAutomaton("aaa");
		assertEquals(a, Automaton.reverse(Automaton.reverse(a)));
	}

	@Test
	public void reverseTest002() {
		Automaton a = Automaton.makeAutomaton("");
		assertEquals(a, Automaton.reverse(Automaton.reverse(a)));
	}

	@Test
	public void reverseTest003() {
		Automaton a = Automaton.union(Automaton.makeAutomaton("aaa"), Automaton.makeAutomaton("bbb"));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
	}

	@Test
	public void reverseTest004() {
		Automaton a = Automaton.star(Automaton.makeAutomaton("a"));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
	}

	@Test
	public void reverseTest005() {
		Automaton a = Automaton.star(Automaton.union(Automaton.makeAutomaton("a"), Automaton.makeAutomaton("b")));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
	}
}
