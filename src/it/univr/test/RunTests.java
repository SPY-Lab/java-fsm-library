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
import java.util.Scanner;

/**
 * Created by andreaperazzoli on 22/12/16.
 */
public class RunTests {

    @Test
    public void runTest1(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0001");
        a.minimizeHopcroft();

        assertTrue(a.run("'a'"));
        assertFalse(a.run("ciao"));
        assertFalse(a.run("FFFFFF"));

    }

    @Test
    public void runTest3(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0003");
        a.minimizeHopcroft();

       // assertTrue(a.run("01"));
        assertFalse(a.run("0"));

    }

    @Test
    public void runTest4(){

        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0004");
        a.minimizeHopcroft();

        assertTrue(a.run("01"));
        assertFalse(a.run("10111101"));

    }

    @Test
    public void runTest9(){
        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0009");
        a.minimizeHopcroft();

        assertTrue(a.run("101"));
        assertTrue(a.run("1110001"));
        assertTrue(a.run("111000100"));
        assertTrue(a.run("11100011"));
        assertFalse(a.run("11111"));
    }

    @Test
    public void runTest10(){
        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0010");
        a.minimizeHopcroft();

        assertTrue(a.run("11122"));
        assertTrue(a.run("33"));
        assertFalse(a.run("32"));

    }


    @Test
    public void runTest11(){
        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0011");
        a.minimizeHopcroft();

        assertTrue(a.run("aaaaaaaaaa"));
        assertTrue(a.run("abbaaa"));
        assertFalse(a.run("abbaaab"));

    }

    @Test
    public void runTest12(){
        Automaton a = Automaton.loadAutomata("/Users/andreaperazzoli/Desktop/SPY/java-fsm-library/automata/" + "automaton0012");
        a.minimizeHopcroft();

        assertTrue(a.run("bbbbbbbbbbb"));
        assertTrue(a.run("ababbbbb"));
        assertFalse(a.run("aaaaaaa"));

    }







}
