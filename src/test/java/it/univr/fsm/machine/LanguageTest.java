package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LanguageTest{

    @Test
    public void languageTest001() {

        HashSet<String> result = new HashSet<>();
        String[] words = {"panda", "pandemonium", "pedante"};
        HashSet<Automaton> set = new HashSet<>();
        for(String s: words){
            set.add(Automaton.makeAutomaton(s));
        }



        Automaton a = Automaton.union(set);
        result = a.getLanguage();

        Assert.assertEquals(new HashSet(Arrays.asList(words)), result);

    }

    @Test
    public void languageTest002() {

        HashSet<String> result = new HashSet<>();
        String[] words = {"abcd", "edfg", "lsf"};
        HashSet<Automaton> set = new HashSet<>();
        for(String s: words){
            set.add(Automaton.makeAutomaton(s));
        }

        Automaton a = Automaton.union(set);
        result = a.getLanguage();

        Assert.assertEquals(new HashSet(Arrays.asList(words)), result);

    }

    @Test
    public void languageTest003() {

        HashSet<String> result = new HashSet<>();
        String[] words = {"panda", "mango", "pongo"};
        HashSet<Automaton> set = new HashSet<>();
        for(String s: words){
            set.add(Automaton.makeAutomaton(s));
        }

        Automaton a = Automaton.union(set);
        result = a.getLanguage();

        Assert.assertEquals(new HashSet(Arrays.asList(words)), result);

    }

    @Test
    public void languageTest004() {

        HashSet<String> result = new HashSet<>();
        String[] words = {"ab", "ab", "ab"};
        HashSet<Automaton> set = new HashSet<>();
        for(String s: words){
            set.add(Automaton.makeAutomaton(s));
        }

        Automaton a = Automaton.union(set);
        result = a.getLanguage();

        Assert.assertEquals(new HashSet(Arrays.asList(words)), result);

    }

    @Test
    public void languageTest005() {

        HashSet<String> result = new HashSet<>();
        String[] words = {"panda", "pan", "an"};
        HashSet<Automaton> set = new HashSet<>();
        for(String s: words){
            set.add(Automaton.makeAutomaton(s));
        }

        Automaton a = Automaton.union(set);
        result = a.getLanguage();

        Assert.assertEquals(new HashSet(Arrays.asList(words)), result);

    }

    @Test
    public void languageTest006() {

        HashSet<String> result = new HashSet<>();
        String[] words = {"bic", "mic", "tik"};
        HashSet<Automaton> set = new HashSet<>();
        for(String s: words){
            set.add(Automaton.makeAutomaton(s));
        }

        Automaton a = Automaton.union(set);
        result = a.getLanguage();

        Assert.assertEquals(new HashSet(Arrays.asList(words)), result);

    }


}
