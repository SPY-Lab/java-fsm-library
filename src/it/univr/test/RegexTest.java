package it.univr.test;

import it.univr.fsm.equations.*;
import it.univr.fsm.machine.Automaton;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by andreaperazzoli on 04/03/17.
 */
public class RegexTest {

    @Test
    public void Test1(){
        RegularExpression result = new Comp(new GroundCoeff("a"),
                new Comp(new Star(new Or(new GroundCoeff("b"),new GroundCoeff("d"))),new GroundCoeff("c")));
        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0016");
        a.minimize();
        RegularExpression aregex = a.toRegex();

        aregex.equals(result);
        System.out.println(aregex);
        assertTrue("Expected: " + result.toString() + "\n" + a.toRegex().toString(),
                a.toRegex().equals(result));
    }

    @Test
    public void Test2(){
        RegularExpression result = new Comp(new GroundCoeff("a"),
                new Comp(new Star(new GroundCoeff("b")),new GroundCoeff("c")));
        Automaton a14 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0014");
        Automaton a15 = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0015");

        Automaton a = Automaton.concat(a14,a15);
        a.minimize();
        RegularExpression aregex = a.toRegex();
        System.out.println(aregex);
        assertTrue("Expected: " + result.toString() + "\n" + a.toRegex().toString(),
                a.toRegex().equals(result));

    }

    @Test
    public void Test3(){
        RegularExpression result = new Star(new Or(new GroundCoeff("a"), new GroundCoeff("b")));

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0017");
        a.minimizeHopcroft();

        RegularExpression aregex = a.toRegex();
        System.out.println(aregex);

        System.out.println();
        assertTrue("Expected: " + result.toString() + "\n" + a.toRegex().toString(),
                a.toRegex().equals(result));
    }

    @Test
    public void Test4(){
        RegularExpression result = new Or(new GroundCoeff("c"),new Comp(new Or(new GroundCoeff("a"),new Comp(new GroundCoeff("d"),new GroundCoeff("c"))),new Star(new GroundCoeff("b"))));

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0018");
        a.minimize();

        RegularExpression aregex = a.toRegex();
        aregex.equals(result);
        System.out.println(aregex);

        assertTrue("Expected: " + result.toString() + "\n" + aregex,
                a.toRegex().equals(result));
    }


}
