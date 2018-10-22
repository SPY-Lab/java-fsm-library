package it.univr.fsm.machine;


import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class StartsWithTest2 {

    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private static final int TOPBOOL = -1;
    
    @Test
    public void startsWithTest1(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("apanda"));
        set.add(Automaton.makeAutomaton("apan"));
        set.add(Automaton.makeAutomaton("ap"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("kyi"), Automaton.makeAutomaton("p"));

        Assert.assertEquals(Automaton.startsWith(a, sub, 1), TOPBOOL);
    }

    @Test
    public void startsWithTest2(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("dapanda"));
        set.add(Automaton.makeAutomaton("dapan"));
        set.add(Automaton.makeAutomaton("dap"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("p");

        Assert.assertEquals(Automaton.startsWith(a, sub, 2), TRUE);
    }

    @Test
    public void startsWithTest3(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("kalpanda"));
        set.add(Automaton.makeAutomaton("nanpan"));
        set.add(Automaton.makeAutomaton("pon"));


        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("pan"));

        Assert.assertEquals(Automaton.startsWith(a, sub, 3), TOPBOOL);
    }

    @Test
    public void startsWithTest4(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("dan"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("pan"));

        Assert.assertEquals(Automaton.startsWith(a, sub, 4), FALSE);
    }

    @Test
    public void startsWithTest5(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("apan"));
        set.add(Automaton.makeAutomaton("apen"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("p");

        Assert.assertEquals(Automaton.startsWith(a, sub, 1), TRUE);
    }

    @Test
    public void startsWithTest6(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("k"), Automaton.makeAutomaton("ka"));

        Assert.assertEquals(Automaton.startsWith(a, sub, 2), FALSE);
    }

    @Test
    public void startsWithTest7(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("koala"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("pan"), Automaton.makeAutomaton("ko"));

        Assert.assertEquals(Automaton.startsWith(a, sub, 0), TOPBOOL);
    }

    @Test
    public void startsWithTest8(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("panda");

        Assert.assertEquals(Automaton.startsWith(a, sub, 0), FALSE);
    }

    @Test
    public void startsWithTest9(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton(""));

        Assert.assertEquals(Automaton.startsWith(a, sub, 0), TOPBOOL);
    }

    @Test
    public void startsWithTest10(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(" "));
        set.add(Automaton.makeAutomaton(""));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("");

        Assert.assertEquals(Automaton.startsWith(a, sub, 1), TRUE);
    }

    @Test
    public void startsWithTest11(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(a, sub, 2), FALSE);
    }

    @Test
    public void startsWithTest12(){
        State q = new State("q0", true, true);
        HashSet<State> states = new HashSet<>();
        states.add(q);
        Transition t = new Transition(q, q, "a");
        HashSet<Transition> delta = new HashSet<>();
        delta.add(t);

        Automaton a = new Automaton (delta, states);
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(a, sub, 8), TOPBOOL);
    }

    @Test
    public void startsWithTest13(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("a"), Automaton.makeAutomaton(""));

        Assert.assertEquals(Automaton.startsWith(a, sub, 0), TOPBOOL);
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


        Automaton a = Automaton.union(set);
        Automaton sub = new Automaton(delta, states);


        Assert.assertEquals(Automaton.startsWith(a, sub, 1), TOPBOOL);
    }

    @Test
    public void startsWithTest15(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pandapandapandapanda"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("pandapanda"), Automaton.makeAutomaton("panda"));

        Assert.assertEquals(Automaton.startsWith(a, sub, 9), FALSE);

    }

    @Test
    public void startsWithTest16(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("nda"), Automaton.makeAutomaton("a"));

        Assert.assertEquals(Automaton.startsWith(a, sub, 4), TOPBOOL);
    }


}