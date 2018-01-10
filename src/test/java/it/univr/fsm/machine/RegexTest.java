package it.univr.fsm.machine;

import it.univr.fsm.equations.*;
import it.univr.fsm.machine.Automaton;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Created by andreaperazzoli on 04/03/17.
 */
public class RegexTest {

	String path = "src/test/resources/";

    @Test
    public void test1(){
        RegularExpression result = new Comp(new GroundCoeff("a"),
                new Comp(new Star(new Or(new GroundCoeff("b"),new GroundCoeff("d"))),new GroundCoeff("c")));
        Automaton a = Automaton.loadAutomata(path + "automata/automaton0016");
        a.minimize();
        RegularExpression aregex = a.toRegex();

        aregex.equals(result);
        System.out.println(aregex);
        assertTrue("Expected: " + result.toString() + "\n" + a.toRegex().toString(),
                a.toRegex().equals(result));
    }

    @Test
    public void test2(){
        RegularExpression result = new Comp(new GroundCoeff("a"),
                new Comp(new Star(new GroundCoeff("b")),new GroundCoeff("c")));
        Automaton a14 = Automaton.loadAutomata(path + "automata/automaton0014");
        Automaton a15 = Automaton.loadAutomata(path + "automata/automaton0015");

        Automaton a = Automaton.concat(a14,a15);
        a.minimize();
        RegularExpression aregex = a.toRegex();
        System.out.println(aregex);
        assertTrue("Expected: " + result.toString() + "\n" + a.toRegex().toString(),
                a.toRegex().equals(result));

    }

    @Test
    public void test3(){
        RegularExpression result = new Star(new Or(new GroundCoeff("a"), new GroundCoeff("b")));

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern(path + "automata/automaton0017");
        a.minimizeHopcroft();

        RegularExpression aregex = a.toRegex();
        System.out.println(aregex);

        System.out.println();
        assertTrue("Expected: " + result.toString() + "\n" + a.toRegex().toString(),
                a.toRegex().equals(result));
    }

    @Test
    public void test5(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern(path + "automata/automaton0019");
       a.minimizeHopcroft();

        System.out.println(a.toRegex());

    }

    @Test
    public void test6(){
        // (a+b)*
        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern(path + "automata/automaton0020");
        a.minimize();

        RegularExpression aRegex = a.toRegex();
        System.out.println(aRegex);


    }

    @Test
    public void test7(){
        // b*a(a+bb*a)*bb*
        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern(path + "automata/automaton0021");
        a.minimize();

        RegularExpression aRegex = a.toRegex();
        System.out.println(aRegex);

    }

    @Test
    public void test8(){

        Automaton a = Automaton.loadAutomataWithFSM2RegexPattern(path + "automata/automaton0022");
        a.minimize();

        RegularExpression aRegex = a.toRegex();
        System.out.println(aRegex);
    }
}
