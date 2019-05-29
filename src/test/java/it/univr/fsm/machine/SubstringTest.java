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

        delta.add(new Transition(q0, q0, "a"));

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

    @Test
    public void substringTest6() {

        Automaton a = Automaton.makeAutomaton("a");
        Automaton b = Automaton.makeAutomaton("b");
        Automaton c = Automaton.makeAutomaton("c");
        HashSet<Automaton> automaton = new HashSet<>();
        automaton.add(a);
        automaton.add(b);
        automaton.add(c);

        Automaton d = Automaton.star(Automaton.union(automaton));

        Automaton automata = Automaton.concat(Automaton.union(automaton),d);

        HashSet<Automaton> aut = new HashSet<>();
        aut.add(Automaton.makeAutomaton("aa"));
        aut.add(Automaton.makeAutomaton("bb"));
        aut.add(Automaton.makeAutomaton("cc"));
        aut.add(Automaton.makeAutomaton("ab"));
        aut.add(Automaton.makeAutomaton("ac"));
        aut.add(Automaton.makeAutomaton("bc"));
        aut.add(Automaton.makeAutomaton("ba"));
        aut.add(Automaton.makeAutomaton("ca"));
        aut.add(Automaton.makeAutomaton("cb"));
        aut.add(Automaton.makeAutomaton("a"));
        aut.add(Automaton.makeAutomaton("b"));
        aut.add(Automaton.makeAutomaton("c"));


        Automaton expectedResult = Automaton.union(aut);
        assertTrue(Automaton.substring(automata, 0, 2).equals(expectedResult));
    }
}