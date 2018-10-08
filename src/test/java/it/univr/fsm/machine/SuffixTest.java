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

    @Test
    public void SuffixTest007(){

    	
    	HashSet<State> states = new HashSet<>();
    	HashSet<Transition> delta = new HashSet<>();

    	State q0 = new State("q0", true, false);
    	State q1 = new State("q1", false, false);
    	State q2 = new State("q2", false, true);

    	states.add(q0);
    	states.add(q1);
    	states.add(q2);

    	delta.add(new Transition(q0, q1, "a"));
    	delta.add(new Transition(q1, q2, "b"));
    	delta.add(new Transition(q2, q2, "c"));
    	
    	Automaton a  = new Automaton(delta, states);
    	
    	HashSet<State> statesR = new HashSet<>();
    	HashSet<Transition> deltaR = new HashSet<>();

    	State q3 = new State("q3", true, false);
    	State q4 = new State("q4", false, true);

    	statesR.add(q3);
    	statesR.add(q4);

    	deltaR.add(new Transition(q3, q4, "b"));
    	deltaR.add(new Transition(q4, q4, "c"));
    	
    	Automaton r = new Automaton(deltaR, statesR);

        Automaton resultR = Automaton.su(a, 1);

        Assert.assertEquals(resultR, r);
    }

    @Test
    public void SuffixTest008(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("and"));
        set.add(Automaton.makeAutomaton("alpa"));
        set.add(Automaton.makeAutomaton("epa"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("nd"));
        r.add(Automaton.makeAutomaton("lpa"));
        r.add(Automaton.makeAutomaton("pa"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.su(a, 1);

        Assert.assertEquals(resultR, result);
    }

}
