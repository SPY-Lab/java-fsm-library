package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 * Created by andreaperazzoli on 22/12/16.
 */
public class RunTests {

	
	String path = "src/test/resources/automata/";

    @Test
    public void runTest1() {

        Automaton a = Automaton.loadAutomata(path + "automaton0001");
        a.minimize();

        assertTrue(a.run("'a'"));
        assertFalse(a.run("ciao"));
        assertFalse(a.run("FFFFFF"));
    }

    @Test
    public void runTest3(){

        Automaton a = Automaton.loadAutomata(path + "automaton0003");
        a.minimize();

        assertFalse(a.run("0"));
    }

    @Test
    public void runTest4(){

        Automaton a = Automaton.loadAutomata(path + "automaton0004");
        a.minimize();

        assertTrue(a.run("01"));
        assertFalse(a.run("10111101"));
    }

    @Test
    public void runTest9(){
        Automaton a = Automaton.loadAutomata(path + "automaton0009");
        a.minimize();

        assertTrue(a.run("101"));
        assertTrue(a.run("1110001"));
        assertTrue(a.run("111000100"));
        assertTrue(a.run("11100011"));
        assertFalse(a.run("11111"));
    }
    
    @Test
    public void runTest10(){
        Automaton a = Automaton.loadAutomata(path + "automaton0010");
        a.minimize();

        assertTrue(a.run("11122"));
        assertTrue(a.run("33"));
        assertFalse(a.run("32"));
    }

    @Test
    public void runTest11(){
        Automaton a = Automaton.loadAutomata(path + "automaton0011");
        a.minimize();

        assertTrue(a.run("aaaaaaaaaa"));
        assertTrue(a.run("abbaaa"));
        assertFalse(a.run("abbaaab"));

    }

    @Test
    public void runTest12(){
        Automaton a = Automaton.loadAutomata(path + "automaton0012");
        a.minimize();

        assertTrue(a.run("bbbbbbbbbbb"));
        assertTrue(a.run("ababbbbb"));
        assertFalse(a.run("aaaaaaa"));

    }
}
