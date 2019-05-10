package it.univr.fsm.machine;

import org.junit.Assert;
import org.junit.Test;

public class IndexOfTest {
	
    private static final int NOT_PRESENT = -1;
    private static final int TOPINT = -2;
    
	@Test
    public void indexOfTest001() {
        Automaton automaton = Automaton.makeAutomaton("idea");
        Automaton search = Automaton.makeAutomaton("idea");

        Assert.assertEquals(Automaton.indexOf(automaton, search), 0);
    }

	@Test
    public void indexOfTest002() {
        Automaton automaton = Automaton.makeAutomaton("idea");
        Automaton search = Automaton.makeAutomaton("b");

        Assert.assertEquals(Automaton.indexOf(automaton, search), NOT_PRESENT);
    }
	
	@Test
    public void indexOfTest003() {
        Automaton automaton = Automaton.makeAutomaton("idea");
        Automaton search = Automaton.makeAutomaton("d");

        Assert.assertEquals(Automaton.indexOf(automaton, search), 1);
    }
	
	@Test
    public void indexOfTest004() {
        Automaton automaton = Automaton.makeAutomaton("idea");
        Automaton search = Automaton.makeAutomaton("i");

        Assert.assertEquals(Automaton.indexOf(automaton, search), 0);
    }


	@Test
    public void indexOfTest005() {
        Automaton automaton = Automaton.union(Automaton.makeAutomaton("adea"), Automaton.makeAutomaton("idea"));
        Automaton search = Automaton.makeAutomaton("d");

        Assert.assertEquals(Automaton.indexOf(automaton, search), 1);
    }

	@Test
    public void indexOfTest006() {
        Automaton automaton = Automaton.union(Automaton.makeAutomaton("abca"), Automaton.makeAutomaton("alca"));
        Automaton search = Automaton.makeAutomaton("ca");

        Assert.assertEquals(Automaton.indexOf(automaton, search), 2);
    }

	@Test
    public void indexOfTest007() {
        Automaton automaton = Automaton.union(Automaton.makeAutomaton("abcs"), Automaton.makeAutomaton("alcs"));
        Automaton search = Automaton.makeAutomaton("s");

        Assert.assertEquals(Automaton.indexOf(automaton, search), 3);
    }


	@Test
    public void indexOfTest008() {
        Automaton automaton = Automaton.union(Automaton.makeAutomaton("abc"), Automaton.makeAutomaton("bca"));
        Automaton search = Automaton.makeAutomaton("bc");

        Assert.assertEquals(Automaton.indexOf(automaton, search), TOPINT);
    }
}
