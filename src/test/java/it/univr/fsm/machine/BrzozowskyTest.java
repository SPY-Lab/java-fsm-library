package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.CombinableMatcher;
import org.junit.Test;

import java.util.HashSet;

/**
 * Created by andreaperazzoli on 18/12/16.
 */
public class BrzozowskyTest {

	String path = "src/test/resources/";

    @Test
    public void reductionTest1(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0008.jff");
        a.minimize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0008.jff");

        assertTrue(a.equals(solution));
    }

    @Test
    public void reductionTest2(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path +"JFLAPautomata_NFA/automaton0010.jff");
        a.minimize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0010.jff");

        assertTrue(a.equals(solution));
    }

    @Test
    public void reductionTest3(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path +"JFLAPautomata_NFA/automaton0017.jff");
        a.minimize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0017.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void reductionTest4(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0018.jff");
        a.minimize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0018.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void reductionTest5() {

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path +"JFLAPautomata_NFA/automaton0019.jff");
        a.minimize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0019.jff");

        assertTrue(a.equals(solution));

    }

    @Test
    public void reductionTest6() {

        Automaton a = Automaton.loadAutomataWithJFLAPPattern(path + "JFLAPautomata_NFA/automaton0026.jff");
        a.minimize();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern(path + "automataminimized/automaton0026.jff");

        assertTrue(a.equals(solution));
    }
}
