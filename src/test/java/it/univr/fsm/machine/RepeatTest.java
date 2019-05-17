package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class RepeatTest {

    @Test
    public void repeatTest001(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("alpha"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("alphaalpha"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 2);
        Assert.assertEquals(resultR, result);

    }

    @Test
    public void repeatTest002(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("alpha"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("alphaalphaalpha"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 3);
        Assert.assertEquals(resultR, result);

    }

    @Test
    public void repeatTest003(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("la"));
        set.add(Automaton.makeAutomaton("al"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("lalala"));
        r.add(Automaton.makeAutomaton("alalal"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 3);
        Assert.assertEquals(resultR, result);

    }

    @Test
    public void repeatTest004(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("la"));
        set.add(Automaton.makeAutomaton("al"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("lalala"));
        r.add(Automaton.makeAutomaton("alalal"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 3);
        Assert.assertTrue(resultR.equals(result));

    }

    @Test
    public void repeatTest005(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("alpha"));
        set.add(Automaton.makeAutomaton("gamma"));
        Automaton a = Automaton.union(set);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();
        State q0 = new State("q0", true, true);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, false);
        State q3 = new State("q3", false, false);
        State q4 = new State("q4", false, false);
        State q5 = new State("q5", false, true);
        State q6 = new State("q6", false, false);
        State q7 = new State("q7", false, false);
        State q8 = new State("q8", false, false);
        State q9 = new State("q9", false, false);
        State q10 = new State("q10", false, true);
        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);
        statesR.add(q3);
        statesR.add(q4);
        statesR.add(q5);
        statesR.add(q6);
        statesR.add(q7);
        statesR.add(q8);
        statesR.add(q9);
        statesR.add(q10);
        deltaR.add(new Transition(q0, q1, "a"));
        deltaR.add(new Transition(q1, q2, "l"));
        deltaR.add(new Transition(q2, q3, "p"));
        deltaR.add(new Transition(q3, q4, "h"));
        deltaR.add(new Transition(q4, q5, "a"));
        deltaR.add(new Transition(q5, q1, "a"));
        deltaR.add(new Transition(q0, q6, "g"));
        deltaR.add(new Transition(q6, q7, "a"));
        deltaR.add(new Transition(q7, q8, "m"));
        deltaR.add(new Transition(q8, q9, "m"));
        deltaR.add(new Transition(q9, q10, "a"));
        deltaR.add(new Transition(q10, q6, "g"));

        Automaton result = new Automaton(deltaR, statesR);

        Automaton resultR = Automaton.repeat(a, Integer.MAX_VALUE);
        Assert.assertEquals(resultR, result);

    }

    @Test
    public void repeatTest006(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("bug"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("bugbugbugbug"));
        r.add(Automaton.makeAutomaton("catcatcatcat"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 4);
        Assert.assertTrue(resultR.equals(result));
    }

    @Test
    public void repeatTest007(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("do"));
        set.add(Automaton.makeAutomaton("mi"));
        Automaton a = Automaton.union(set);

        HashSet<State> statesR = new HashSet<>();
        HashSet<Transition> deltaR = new HashSet<>();
        State q0 = new State("q0", true, true);
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, true);
        State q3 = new State("q3", false, false);
        State q4 = new State("q4", false, true);

        statesR.add(q0);
        statesR.add(q1);
        statesR.add(q2);
        statesR.add(q3);
        statesR.add(q4);

        deltaR.add(new Transition(q0, q1, "d"));
        deltaR.add(new Transition(q1, q2, "o"));
        deltaR.add(new Transition(q2, q1, "d"));
        deltaR.add(new Transition(q0, q3, "m"));
        deltaR.add(new Transition(q4, q3, "m"));
        deltaR.add(new Transition(q3, q4, "i"));


        Automaton result = new Automaton(deltaR, statesR);

        Automaton resultR = Automaton.repeat(a, Integer.MAX_VALUE);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest008(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("do"));
        set.add(Automaton.makeAutomaton("mi"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("dodo"));
        r.add(Automaton.makeAutomaton("mimi"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 2);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest009(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("do"));
        set.add(Automaton.makeAutomaton("mi"));
        Automaton a = Automaton.union(set);

        Automaton result = Automaton.makeEmptyString();

        Automaton resultR = Automaton.repeat(a, 0);

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest010(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("do"));
        set.add(Automaton.makeAutomaton("mi"));
        Automaton a = Automaton.union(set);

        Automaton result = Automaton.makeEmptyLanguage();

        Automaton resultR = Automaton.repeat(a, -2);

        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest011(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("do"));
        set.add(Automaton.makeAutomaton("re"));
        set.add(Automaton.makeAutomaton("mi"));
        Automaton a = Automaton.union(set);

        Automaton result = Automaton.makeEmptyString();

        Automaton resultR = Automaton.repeat(a, 0);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest012(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();
        State q0 = new State("q0", true, true);
        states.add(q0);
        delta.add(new Transition(q0, q0, "a"));

        Automaton a = new Automaton(delta, states);

        Automaton result = Automaton.makeEmptyLanguage();

        Automaton resultR = Automaton.repeat(a, -1);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest013(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();
        State q0 = new State("q0", true, true);
        states.add(q0);
        delta.add(new Transition(q0, q0, "a"));
        Automaton a = new Automaton(delta, states);

        Automaton result = Automaton.union(a, Automaton.makeEmptyString());

        Automaton resultR = Automaton.repeat(a, Integer.MAX_VALUE);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest014(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("bug"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("cat"));
        r.add(Automaton.makeAutomaton("bug"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 1);
        Assert.assertTrue(resultR.equals(result));
    }

    @Test
    public void repeatTest015(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("bug"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("bugbugbug"));
        r.add(Automaton.makeAutomaton("catcatcat"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 3);
        Assert.assertTrue(resultR.equals(result));
    }

    @Test
    public void repeatTest016(){
        HashSet<State> states = new HashSet<>();
        HashSet<Transition> delta = new HashSet<>();
        State q0 = new State("q0", true, true);
        states.add(q0);
        delta.add(new Transition(q0, q0, "a"));
        Automaton a = new Automaton(delta, states);

        Automaton resultR = Automaton.repeat(a, 6);
        Assert.assertEquals(resultR, a);
    }

    @Test
    public void repeatTest017(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("c"));
        set.add(Automaton.makeAutomaton("bug"));
        set.add(Automaton.makeAutomaton("b"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("catcatcat"));
        r.add(Automaton.makeAutomaton("ccc"));
        r.add(Automaton.makeAutomaton("bugbugbug"));
        r.add(Automaton.makeAutomaton("bbb"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 3);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest018(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("a"));
        set.add(Automaton.makeAutomaton("b"));
        set.add(Automaton.makeAutomaton("c"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("aaaaa"));
        r.add(Automaton.makeAutomaton("bbbbb"));
        r.add(Automaton.makeAutomaton("ccccc"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 5);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest019(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("bug"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 0);
        Assert.assertEquals(resultR,result);
    }

    @Test
    public void repeatTest020(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("bug"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("bugbug"));
        r.add(Automaton.makeAutomaton("catcat"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 2);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest021(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("bug"));
        Automaton a = Automaton.union(set);

        Automaton result = Automaton.makeEmptyLanguage();

        Automaton resultR = Automaton.repeat(a, -10);
        Assert.assertEquals(resultR, result);
    }

    @Test
    public void repeatTest022(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("cat"));
        set.add(Automaton.makeAutomaton("ca"));
        set.add(Automaton.makeAutomaton("c"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("catcat"));
        r.add(Automaton.makeAutomaton("caca"));
        r.add(Automaton.makeAutomaton("cc"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.repeat(a, 2);
        Assert.assertEquals(resultR, result);
    }
}
