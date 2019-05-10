package it.univr.fsm.machine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ReverseTest {


	/*@Test
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
		assertEquals(Automaton.intersection(a, Automaton.complement(r)), Automaton.makeEmptyLanguage());
	}

	@Test
	public void reverseTest004() {
		Automaton a = Automaton.star(Automaton.makeAutomaton("a"));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
		assertEquals(Automaton.intersection(a, Automaton.complement(r)), Automaton.makeEmptyLanguage());
	}

	@Test
	public void reverseTest005() {
		Automaton a = Automaton.star(Automaton.union(Automaton.makeAutomaton("a"), Automaton.makeAutomaton("b")));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
		assertEquals(Automaton.intersection(a, Automaton.complement(r)), Automaton.makeEmptyLanguage());
	}
	
	@Test
	public void reverseTest006() {
		Automaton a = Automaton.star(Automaton.union(Automaton.makeAutomaton("a"), Automaton.makeAutomaton("a")));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
		assertEquals(Automaton.intersection(a, Automaton.complement(r)), Automaton.makeEmptyLanguage());

	}
	

	@Test
	public void reverseTest007() {
		Automaton a = Automaton.star(Automaton.union(Automaton.makeAutomaton("a"), Automaton.makeAutomaton("b"), Automaton.makeAutomaton("ccc")));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
		assertEquals(Automaton.intersection(a, Automaton.complement(r)), Automaton.makeEmptyLanguage());
	}
	
	@Test
	public void reverseTest008() {
		Automaton a = Automaton.star(Automaton.union(Automaton.makeAutomaton(""), Automaton.makeAutomaton(""), Automaton.makeAutomaton("")));

		Automaton r = Automaton.reverse(a);
		r.minimize();
		r = Automaton.reverse(r);
		r.minimize();
		assertEquals(a, r);
		assertEquals(Automaton.intersection(a, Automaton.complement(r)), Automaton.makeEmptyLanguage());
	}*/
}
