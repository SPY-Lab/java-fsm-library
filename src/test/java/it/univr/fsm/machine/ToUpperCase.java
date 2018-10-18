package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class ToUpperCase{

    public class toUpperCaseTest {
        
    }
        @Test
        public void toUpperCaseTest001() {
            Automaton automaton = Automaton.makeAutomaton("panda");
            Automaton result = Automaton.makeAutomaton("PANDA");
            Assert.assertEquals(Automaton.toUpperCase(automaton), result);
        }

        @Test
        public void toUpperCaseTest002() {
            Automaton automaton = Automaton.makeAutomaton("Panda");
            automaton = Automaton.union(automaton, Automaton.makeAutomaton("koalA"));

            Automaton result = Automaton.makeAutomaton("PANDA");
            result = Automaton.union(result, Automaton.makeAutomaton("KOALA"));

            Assert.assertEquals(Automaton.toUpperCase(automaton), result);
        }

        @Test
        public void toUpperCaseTest003() {
            Automaton automaton = Automaton.makeAutomaton("Panda #$%");
            automaton = Automaton.union(automaton, Automaton.makeAutomaton("PANDA!!!koalA!!!"));

            Automaton result = Automaton.makeAutomaton("PANDA #$%");
            result = Automaton.union(result, Automaton.makeAutomaton("PANDA!!!KOALA!!!"));

            automaton = Automaton.toUpperCase(automaton);

            Assert.assertEquals(automaton, result);
        }

        @Test
        public void toUpperCaseTest004() {
            HashSet<State> states = new HashSet<>();
            HashSet<Transition> delta = new HashSet<>();
            State q0 = new State("q0", true, true);
            states.add(q0);
            delta.add(new Transition(q0, q0, "a"));

            Automaton automaton = new Automaton(delta, states); // Automaton: a*
            automaton = Automaton.union(automaton, Automaton.makeAutomaton("aKOALa!"));

            delta.remove(new Transition(q0, q0, "a"));
            delta.add(new Transition(q0, q0, "A"));
            Automaton result = new Automaton(delta, states); // Automaton: a*
            result = Automaton.union(result, Automaton.makeAutomaton("AKOALA!"));

            Assert.assertEquals(Automaton.toUpperCase(automaton), result);
        }
}
