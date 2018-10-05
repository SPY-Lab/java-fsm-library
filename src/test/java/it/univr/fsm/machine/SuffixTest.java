package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class SuffixTest{

    @Test
    public void SuffixTest001(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("marco"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("anda"));
        r.add(Automaton.makeAutomaton("arco"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.su(a, 1);

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void SuffixTest002(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandapanda"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("anda"));
        r.add(Automaton.makeAutomaton("andapanda"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.su(a, 1);

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void SuffixTest003(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("panda"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        r.add(Automaton.makeAutomaton("da"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.su(a, 3);
        System.out.println("Result --> " + resultR);

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void SuffixTest004(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pan"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.su(a, 6);
        System.out.println("Result --> " + resultR);

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void SuffixTest005(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandemonium"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        r.add(Automaton.makeAutomaton("onium"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.su(a, 6);

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void SuffixTest006(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandemonium"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.su(a, 100);

        Assert.assertEquals(resultR, result);
    }


}
