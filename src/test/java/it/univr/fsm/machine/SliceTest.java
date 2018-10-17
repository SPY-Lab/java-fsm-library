package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class SliceTest {

    @Test
    public void sliceTest001() {

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("marco"));
        Automaton a = Automaton.union(set);

        long start = 0;

        Automaton resultR = Automaton.slice(a, start);
        Assert.assertEquals(resultR, a);

    }

    @Test
    public void sliceTest002(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("arco"));
        set.add(Automaton.makeAutomaton("marco"));
        Automaton a = (Automaton.union(set));

        long start = -1;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("o"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);
        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest003(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("koala"));
        Automaton a = Automaton.union(set);

        long start = -3;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("nda"));
        r.add(Automaton.makeAutomaton("ala"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);
        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest004(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        Automaton a = Automaton.union(set);

        long start = -3;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("nda"));
        r.add(Automaton.makeAutomaton("pan"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest005(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        Automaton a = Automaton.union(set);

        long start = -6;

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, a);

    }

    @Test
    public void sliceTest006(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandemonium"));
        Automaton a = Automaton.union(set);


        long start = 6;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("onium"));
        r.add(Automaton.makeEmptyString());
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest007(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandas"));
        set.add(Automaton.makeAutomaton("pandaros"));
        Automaton a = Automaton.union(set);

        long start = 2;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("nda"));
        r.add(Automaton.makeAutomaton("ndas"));
        r.add(Automaton.makeAutomaton("ndaros"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest008(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandas"));
        set.add(Automaton.makeAutomaton("pandaros"));
        Automaton a = Automaton.union(set);

        long start = -2;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("da"));
        r.add(Automaton.makeAutomaton("as"));
        r.add(Automaton.makeAutomaton("os"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest009(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandas"));
        set.add(Automaton.makeAutomaton("pandaros"));
        Automaton a = Automaton.union(set);

        long start = -5;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeAutomaton("panda"));
        r.add(Automaton.makeAutomaton("andas"));
        r.add(Automaton.makeAutomaton("daros"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest0010(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        Automaton a = Automaton.union(set);

        long start = 6;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest011(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton(""));
        Automaton a = Automaton.union(set);

        long start = 6;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

    @Test
    public void sliceTest012(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));
        set.add(Automaton.makeAutomaton("manda"));
        set.add(Automaton.makeAutomaton("koala"));
        Automaton a = Automaton.union(set);

        long start = 1;

        HashSet<Automaton> r = new HashSet<>();
        r.add(Automaton.makeEmptyString());
        r.add(Automaton.makeAutomaton("an"));
        r.add(Automaton.makeAutomaton("anda"));
        r.add(Automaton.makeAutomaton("oala"));
        Automaton result = Automaton.union(r);

        Automaton resultR = Automaton.slice(a, start);

        Assert.assertEquals(resultR, result);

    }

}
