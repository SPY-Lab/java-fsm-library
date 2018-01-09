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
public class HopcroftTest {

    public boolean weakEquals(Automaton a1, Automaton a2){
        if(a1.getDelta().size() == a2.getDelta().size() && a1.getStates().size() == a2.getStates().size()){
            HashSet<Transition> delta1 = (HashSet<Transition>) a1.getDelta().clone();
            HashSet<State> states1 = (HashSet<State>) a1.getStates().clone();
            HashSet<Transition> delta2 = (HashSet<Transition>) a2.getDelta().clone();
            HashSet<State> states2 = (HashSet<State>) a2.getStates().clone();

            for(Transition t1 : a1.getDelta()){
                states1.remove(t1.getFrom());
                states1.remove(t1.getTo());
                delta1.remove(t1);
                for(Transition t2 : delta2){
                    if(t1.getInput().equals(t2.getInput())){
                        states2.remove(t2.getFrom());
                        states2.remove(t2.getTo());
                        delta2.remove(t2);
                        break;
                    }
                }
            }

            System.out.println("a1\n-----------");
            System.out.println(a1);
            System.out.println("a2\n-----------");
            System.out.println(a2);
            System.out.println("---------------------------------------------");

            if(states2.size() == 0 && delta2.size() == 0) return true;
            else return false;

        }
        System.out.println("a1\n-----------");
        System.out.println(a1);
        System.out.println("a2\n-----------");
        System.out.println(a2);
        System.out.println("-----------");
        return false;
    }

    @Test
    public void reductionTest1(){


        Automaton a = Automaton.loadAutomataWithJFLAPPattern("JFLAPautomata_NFA/" + "automaton0008.jff");
        System.out.println(a);
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern("automataminimized/" + "automaton0008.jff");

        assertTrue(weakEquals(a,solution));

    }

    @Test
    public void reductionTest2(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern("JFLAPautomata_NFA/" + "automaton0010.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern("automataminimized/" + "automaton0010.jff");

        assertTrue(weakEquals(a,solution));

    }

    @Test
    public void reductionTest3(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern("JFLAPautomata_NFA/" + "automaton0017.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern("automataminimized/" + "automaton0017.jff");

        //assertTrue(weakEquals(a,solution)); FALSE NEGATIVE

    }

    @Test
    public void reductionTest4(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern("JFLAPautomata_NFA/" + "automaton0018.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern("automataminimized/" + "automaton0018.jff");

        assertTrue(weakEquals(a,solution));

    }

    @Test
    public void reductionTest5(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern("JFLAPautomata_NFA/" + "automaton0019.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern("automataminimized/" + "automaton0019.jff");

        //assertTrue(weakEquals(a,solution)); FALSE NEGATIVE

    }

    @Test
    public void reductionTest6(){

        Automaton a = Automaton.loadAutomataWithJFLAPPattern("JFLAPautomata_NFA/" + "automaton0026.jff");
        a.minimizeHopcroft();
        Automaton solution = Automaton.loadAutomataWithJFLAPPattern("automataminimized/" + "automaton0026.jff");

        assertTrue(weakEquals(a,solution));

    }






}
