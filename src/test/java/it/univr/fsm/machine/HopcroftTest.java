package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by andreaperazzoli on 18/12/16.
 */
public class HopcroftTest {

	String path = "src/test/resources/";

    @Test
    public void reductionTest1(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0008.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0008.jff");

        assertTrue(a.equals(solution));
    }

    @Test
    public void reductionTest2(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0010.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0010.jff");

        assertTrue(a.equals(solution));
    }

    @Test
    public void reductionTest3(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0017.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0017.jff");

        assertTrue(a.equals(solution));
    }

    @Test
    public void reductionTest4(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0018.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0018.jff");

        assertTrue(a.equals(solution));
    }

    @Test
    public void reductionTest5(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0019.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0019.jff");

        assertTrue(a.equals(solution));
    }

    @Test
    public void reductionTest6(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0026.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0026.jff");

        assertTrue(a.equals(solution));
    }
}
