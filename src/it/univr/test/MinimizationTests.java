package it.univr.test;

import it.univr.fsm.machine.Automaton;
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
public class MinimizationTests {

    @Test
    public void reductionTest1(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0001");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);


    }

    @Test
    public void reductionTest2(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0002");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest3(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0003");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest4(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0004");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest5(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0005");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest6(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0006");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest7(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0007");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest8(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0008");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest9(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0009");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest10(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0010");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest11(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0011");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest12(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0012");
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();
        Automaton amoore = a.clone();
        amoore.minimizeMoore();
        Automaton abrow = a.clone();
        abrow.minimize();


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest13(){

        Automaton a = Automaton.loadAutomataWithAlternatePattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/"
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


        assertEquals("Hopcroft minimization is not equal Moore's one", ahop, amoore);
        assertEquals("Moore minimization is not equal Brow's one", amoore, abrow);
        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest14(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0022");

        Automaton abrow = a.clone();
        abrow.minimize();
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();

        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest15(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0021");

        Automaton abrow = a.clone();
        abrow.minimize();
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();

        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest16(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0020");

        Automaton abrow = a.clone();
        abrow.minimize();
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();

        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }

    @Test
    public void reductionTest17(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0019");

        Automaton abrow = a.clone();
        abrow.minimize();
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();

        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }
    @Test
    public void reductionTest18(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0018");

        Automaton abrow = a.clone();
        abrow.minimize();
        Automaton ahop = a.clone();
        ahop.minimizeHopcroft();

        assertEquals("Hopcroft minimization is not equal Brow's one", ahop, abrow);

    }




}
