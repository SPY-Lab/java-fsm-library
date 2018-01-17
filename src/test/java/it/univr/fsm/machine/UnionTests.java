package it.univr.fsm.machine;

/**
 * Created by andreaperazzoli on 24/01/17.
 */

import it.univr.fsm.machine.Automaton;
import org.junit.Test;
import static org.junit.Assert.assertTrue;


import java.util.HashSet;


public class UnionTests {

    @Test
    public void UnionTest1() {
        Automaton a1 = Automaton.makeAutomaton("a");
        Automaton a2 = Automaton.makeAutomaton("b");

        Automaton union = Automaton.union(a1,a2);


        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();
        
        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, true);

        states.add(q0);
        states.add(q1);
        
        delta.add(new Transition(q0, q1, "a", ""));
        delta.add(new Transition(q0, q1, "b", ""));
        
        Automaton expectedResult = new Automaton(delta, states);
        
        assertTrue(expectedResult.equals(union));
    }


}
