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

    	Automaton exp = Automaton.explodeAutomaton(a);

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

    @Test
    public void SuffixTest009(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, false);
        State q3 = new State("q3", false, true);
        State q4 = new State("q4", false, false);
        State q5 = new State("q5", false, false);

        states.add(q0);
        states.add(q1);
        states.add(q2);
        states.add(q3);
        states.add(q4);
        states.add(q5);

        delta.add(new Transition(q0, q1, "a"));
        delta.add(new Transition(q1, q2, "b"));
        delta.add(new Transition(q2, q2, "c"));
        delta.add(new Transition(q2, q3, "d"));
        delta.add(new Transition(q0, q4, "g"));
        delta.add(new Transition(q4, q4, "h"));
        delta.add(new Transition(q4, q5, "i"));
        delta.add(new Transition(q5, q3, "l"));
        delta.add(new Transition(q1, q4, "e"));
        delta.add(new Transition(q5, q2, "f"));

        Automaton a  = new Automaton(delta, states);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();

        State q00 = new State("q0", true, false);
        State q01 = new State("q1", false, false);
        State q02 = new State("q2", false, false);
        State q03 = new State("q3", false, false);
        State q04 = new State("q4", false, true);

        statesR.add(q00);
        statesR.add(q01);
        statesR.add(q02);
        statesR.add(q03);
        statesR.add(q04);

        deltaR.add(new Transition(q0, q1, "b"));
        deltaR.add(new Transition(q1, q1, "c"));
        deltaR.add(new Transition(q1, q4, "d"));
        deltaR.add(new Transition(q0, q2, "h"));
        deltaR.add(new Transition(q0, q2, "e"));
        deltaR.add(new Transition(q2, q2, "h"));
        deltaR.add(new Transition(q2, q3, "i"));
        deltaR.add(new Transition(q0, q3, "i"));
        deltaR.add(new Transition(q3, q1, "f"));
        deltaR.add(new Transition(q3, q4, "l"));

        Automaton r = new Automaton(deltaR, statesR);

        Automaton resultR = Automaton.su(a, 1);

        Assert.assertEquals(resultR, r);
    }

    @Test
    public void SuffixTest010(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, false);
        State q3 = new State("q3", false, true);

        states.add(q0);
        states.add(q1);
        states.add(q2);
        states.add(q3);

        delta.add(new Transition(q0, q1, "a"));
        delta.add(new Transition(q1, q2, "b"));
        delta.add(new Transition(q2, q3, "c"));
        delta.add(new Transition(q3, q1, "d"));

        Automaton a  = new Automaton(delta, states);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();

        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);
        statesR.add(q3);

        deltaR.add(new Transition(q0, q2, "b"));
        deltaR.add(new Transition(q2, q3, "c"));
        deltaR.add(new Transition(q3, q1, "d"));
        deltaR.add(new Transition(q1, q2, "b"));

        Automaton r = new Automaton(deltaR, statesR);

        Automaton resultR = Automaton.su(a, 1);

        Assert.assertEquals(resultR, r);
    }

    @Test
    public void SuffixTest011(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, false);
        State q3 = new State("q3", false, true);

        states.add(q0);
        states.add(q1);
        states.add(q2);
        states.add(q3);

        delta.add(new Transition(q0, q1, "a"));
        delta.add(new Transition(q1, q2, "b"));
        delta.add(new Transition(q2, q3, "c"));
        delta.add(new Transition(q3, q1, "d"));

        Automaton a  = new Automaton(delta, states);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();

        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);
        statesR.add(q3);

        deltaR.add(new Transition(q0, q3, "c"));
        deltaR.add(new Transition(q2, q3, "c"));
        deltaR.add(new Transition(q3, q1, "d"));
        deltaR.add(new Transition(q1, q2, "b"));

        Automaton r = new Automaton(deltaR, statesR);

        Automaton resultR = Automaton.su(a, 2);

        Assert.assertEquals(resultR, r);
    }

    @Test
    public void SuffixTest012(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, false);
        State q3 = new State("q3", false, true);

        states.add(q0);
        states.add(q1);
        states.add(q2);
        states.add(q3);

        delta.add(new Transition(q0, q1, "a"));
        delta.add(new Transition(q1, q2, "b"));
        delta.add(new Transition(q2, q3, "c"));
        delta.add(new Transition(q3, q1, "d"));

        Automaton a  = new Automaton(delta, states);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();

        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);
        statesR.add(q3);

        deltaR.add(new Transition(q0, q1, "d"));
        deltaR.add(new Transition(q2, q3, "c"));
        deltaR.add(new Transition(q3, q1, "d"));
        deltaR.add(new Transition(q1, q2, "b"));

        Automaton r = new Automaton(deltaR, statesR);
        r = Automaton.union(r, Automaton.makeEmptyString());

        Automaton resultR = Automaton.su(a, 3);

        Assert.assertEquals(resultR, r);
    }

    @Test
    public void SuffixTest013(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, false);
        State q3 = new State("q3", false, true);

        states.add(q0);
        states.add(q1);
        states.add(q2);
        states.add(q3);

        delta.add(new Transition(q0, q1, "a"));
        delta.add(new Transition(q1, q2, "b"));
        delta.add(new Transition(q2, q3, "c"));
        delta.add(new Transition(q3, q1, "d"));

        Automaton a  = new Automaton(delta, states);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();

        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);
        statesR.add(q3);

        deltaR.add(new Transition(q0, q2, "b"));
        deltaR.add(new Transition(q2, q3, "c"));
        deltaR.add(new Transition(q3, q1, "d"));
        deltaR.add(new Transition(q1, q2, "b"));

        Automaton r = new Automaton(deltaR, statesR);
        r = Automaton.union(r, Automaton.makeEmptyString());

        Automaton resultR = Automaton.su(a, 4);

        Assert.assertEquals(resultR, r);
    }

    @Test
    public void SuffixTest014(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, true);

        states.add(q0);
        states.add(q1);
        states.add(q2);

        delta.add(new Transition(q0, q1, "a"));
        delta.add(new Transition(q1, q1, "b"));
        delta.add(new Transition(q1, q2, "c"));

        Automaton a  = new Automaton(delta, states);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();

        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);

        deltaR.add(new Transition(q0, q1, "b"));
        deltaR.add(new Transition(q1, q1, "b"));
        deltaR.add(new Transition(q1, q2, "c"));
        deltaR.add(new Transition(q0, q2, "c"));

        Automaton r = new Automaton(deltaR, statesR);

        Automaton resultR = Automaton.su(a, 1);

        Assert.assertEquals(resultR, r);
    }

    @Test
    public void SuffixTest015(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();

        State q0 = new State("q0", true, false);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, true);

        states.add(q0);
        states.add(q1);
        states.add(q2);

        delta.add(new Transition(q0, q1, "a"));
        delta.add(new Transition(q1, q1, "b"));
        delta.add(new Transition(q1, q2, "c"));

        Automaton a  = new Automaton(delta, states);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();

        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);

        deltaR.add(new Transition(q0, q1, "b"));
        deltaR.add(new Transition(q1, q1, "b"));
        deltaR.add(new Transition(q1, q2, "c"));
        deltaR.add(new Transition(q0, q2, "c"));

        Automaton r = new Automaton(deltaR, statesR);
        r = Automaton.union(r, Automaton.makeEmptyString());

        Automaton resultR = Automaton.su(a, 2);

        Assert.assertEquals(resultR, r);
    }
}
