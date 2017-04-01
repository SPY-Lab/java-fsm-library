package it.univr.test;

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

            if(states2.size() == 0 && delta2.size() == 0) return true;

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

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0001");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));

    }

    @Test
    public void reductionTest2(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0002");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest3(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0003");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest4(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0004");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest5(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0005");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest6(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0006");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest7(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0007");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest8(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0008");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest9(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0009");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest10(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0010");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest11(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0011");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest12(){

        Automaton a = Automaton.loadAutomata("automata/" + "automaton0012");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest13(){

        Automaton a = Automaton.loadAutomataWithAlternatePattern("automata/"
                + "automaton0013");
        /*
        *   automaton0013 è il primo automa che rispecchia il pattern
        *   [state_name]([reject], [accept]) || [initial]
	    *       [state_name] Sym -> [state_to]
        *
         */

        System.out.println(a.automatonPrint());
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));

    }

    @Test
    public void reductionTest14(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("automata/" + "automaton0022");

        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));


    }

    @Test
    public void reductionTest15(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("automata/" + "automaton0021");

        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));

    }

    @Test
    public void reductionTest16(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("automata/" + "automaton0020");

        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));

    }

    @Test
    public void reductionTest17(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("automata/" + "automaton0019");

        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));

    }
    @Test
    public void reductionTest18(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("automata/" + "automaton0018");

        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertTrue(weakEquals(ahop,amoore));
        assertTrue(weakEquals(amoore,abrow));
        assertTrue(weakEquals(ahop,abrow));

    }




}
