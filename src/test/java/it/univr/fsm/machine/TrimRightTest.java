package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class TrimRight {

    @Test
    public void trimTest001(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda   "));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("panda"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.trimRight(a);
        visualizeAutomaton.show(a, "a");
        visualizeAutomaton.show(resultR, "result");

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void trimTest002(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pan   "));
        set.add(Automaton.makeAutomaton("pa  "));
        set.add(Automaton.makeAutomaton("panda       "));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("panda"));
        r.add(Automaton.makeAutomaton("pan"));
        r.add(Automaton.makeAutomaton("pa"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.trimRight(a);

        Assert.assertEquals(resultR, result);
    }


}
