package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class IndexOfTest {

    @Test
    public void indexOfTest001() {

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("parco"));
        set.add(Automaton.makeAutomaton("salgo"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> search = new HashSet<>();
        search.add(Automaton.makeAutomaton("o"));
        Automaton searchFor = Automaton.union(search);

        Assert.assertEquals(Automaton.indexOf(a, searchFor), 4);

    }

    @Test
    public void indexOfTest002() {
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("parco"));
        set.add(Automaton.makeAutomaton("salgo"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> search = new HashSet<>();
        search.add(Automaton.makeAutomaton("a"));
        Automaton searchFor = Automaton.union(search);

        Assert.assertEquals(Automaton.indexOf(a, searchFor), 1);

    }

    @Test
    public void indexOfTest003() {
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("parco"));
        set.add(Automaton.makeAutomaton("salgo"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> search = new HashSet<>();
        search.add(Automaton.makeAutomaton("l"));
        Automaton searchFor = Automaton.union(search);

        Assert.assertEquals(Automaton.indexOf(a, searchFor), Integer.MAX_VALUE);

    }

    @Test
    public void indexOfTest004() {
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("parco"));
        set.add(Automaton.makeAutomaton("salgo"));
        set.add(Automaton.makeAutomaton("bianco"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> search = new HashSet<>();
        search.add(Automaton.makeAutomaton("a"));
        Automaton searchFor = Automaton.union(search);

        Assert.assertEquals(Automaton.indexOf(a, searchFor), Integer.MAX_VALUE);

    }

    @Test
    public void indexOfTest005() {
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("parco"));
        set.add(Automaton.makeAutomaton("salgo"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> search = new HashSet<>();
        search.add(Automaton.makeAutomaton("o"));
        search.add(Automaton.makeAutomaton("a"));
        Automaton searchFor = Automaton.union(search);

        Assert.assertEquals(Automaton.indexOf(a, searchFor), Integer.MAX_VALUE);

    }

    @Test
    public void indexOfTest006() {
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("abab"));
        Automaton a = Automaton.union(set);

        HashSet<Automaton> search = new HashSet<>();
        search.add(Automaton.makeAutomaton("a"));
        Automaton searchFor = Automaton.union(search);

        Assert.assertEquals(Automaton.indexOf(a, searchFor), 0);

    }



}

