package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DeterminizeTest {
	
	String path = "src/test/resources/";


    @Test
    public void determinizeTest1(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0008.jff");
        a = a.determinize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automatadeterminized/automaton0008.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void determinizeTest2(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0010.jff");
        a = a.determinize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automatadeterminized/automaton0010.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void determinizeTest3(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0017.jff");
        a = a.determinize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automatadeterminized/automaton0017.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void determinizeTest4(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0018.jff");
        a = a.determinize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automatadeterminized/automaton0018.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void determinizeTest5(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0019.jff");
        a = a.determinize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automatadeterminized/automaton0019.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void determinizeTest6(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0026.jff");
        a = a.determinize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automatadeterminized/automaton0026.jff");

        assertTrue(a.equals(solution));
    }
}
