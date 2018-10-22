package it.univr.fsm.machine;


import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class StartsWithTest2 {

    @Test
    public void startsWithTest1(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("apanda"));
        set.add(Automaton.makeAutomaton("apan"));
        set.add(Automaton.makeAutomaton("ap"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("kyi"), Automaton.makeAutomaton("p")));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "1")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest2(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("dapanda"));
        set.add(Automaton.makeAutomaton("dapan"));
        set.add(Automaton.makeAutomaton("dap"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.makeAutomaton("p"));

        Assert.assertEquals(a.startsWith(sub, new Interval("2", "2")), new AbstractBoolean(1));
    }

    @Test
    public void startsWithTest3(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("kalpanda"));
        set.add(Automaton.makeAutomaton("nanpan"));
        set.add(Automaton.makeAutomaton("pon"));


        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("pan")));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "3")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest4(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("dan"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("pan")));

        Assert.assertEquals(a.startsWith(sub, new Interval("3", "4")), new AbstractBoolean(0));
    }

    @Test
    public void startsWithTest5(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("apan"));
        set.add(Automaton.makeAutomaton("apen"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.makeAutomaton("p"));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "1")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest6(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("k"), Automaton.makeAutomaton("ka")));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "1")), new AbstractBoolean(0));
    }

    @Test
    public void startsWithTest7(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("koala"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("pan"), Automaton.makeAutomaton("ko")));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "0")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest8(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.makeAutomaton("panda"));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "3")), new AbstractBoolean(0));
    }

    @Test
    public void startsWithTest9(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));
        set.add(Automaton.makeAutomaton("p"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("")));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "1")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest10(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(" "));
        set.add(Automaton.makeAutomaton(""));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.makeAutomaton(""));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "1")), new AbstractBoolean(1));
    }

    @Test
    public void startsWithTest11(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.makeAutomaton("a"));

        Assert.assertEquals(a.startsWith(sub, new Interval("2", "3")), new AbstractBoolean(0));
    }

    @Test
    public void startsWithTest12(){
        State q = new State("q0", true, true);
        HashSet<State> states = new HashSet<>();
        states.add(q);
        Transition t = new Transition(q, q, "a");
        HashSet<Transition> delta = new HashSet<>();
        delta.add(t);

        FA a = new FA(new Automaton (delta, states));
        FA sub = new FA(Automaton.makeAutomaton("a"));

        Assert.assertEquals(a.startsWith(sub, new Interval("3", "9")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest13(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("a"), Automaton.makeAutomaton("")));

        Assert.assertEquals(a.startsWith(sub, new Interval("0", "1")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest14(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("a"));

        State q = new State("q0", true, true);
        HashSet<State> states = new HashSet<>();
        states.add(q);
        Transition t = new Transition(q, q, "a");
        HashSet<Transition> delta = new HashSet<>();
        delta.add(t);


        FA a = new FA(Automaton.union(set));
        FA sub = new FA(new Automaton(delta, states));


        Assert.assertEquals(a.startsWith(sub, new Interval("0", "1")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest15(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pandapandapandapanda"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("pandapanda"), Automaton.makeAutomaton("panda")));

        Assert.assertEquals(a.startsWith(sub, new Interval("9", "9")), new AbstractBoolean(0));

    }

    @Test
    public void startsWithTest16(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));

        FA a = new FA(Automaton.union(set));
        FA sub = new FA(Automaton.union(Automaton.makeAutomaton("nda"), Automaton.makeAutomaton("a")));

        Assert.assertEquals(a.startsWith(sub, new Interval("2", "4")), new AbstractBoolean(2));
    }

    @Test
    public void startsWithTest17(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
    }


}