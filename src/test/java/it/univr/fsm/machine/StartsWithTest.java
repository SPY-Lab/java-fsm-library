package it.univr.fsm.machine;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class StartsWithTest {

    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private static final int TOPBOOL = -1;


    @Test
    public void startsWithTest001(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("pan"));


        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest002(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("p");

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest003(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("pon"));


        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("pan"));

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest004(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("dan"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton("pan"));

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest005(){
        HashSet<Automaton> set = new HashSet<>();
        
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("pen"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("p");

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest006(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("k"), Automaton.makeAutomaton("ka"));

        Assert.assertEquals(Automaton.startsWith(a, sub), FALSE);
    }

    @Test
    public void startsWithTest007(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("koala"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("pan"), Automaton.makeAutomaton("ko"));

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest008(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pan"));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("panda");

        Assert.assertEquals(Automaton.startsWith(a, sub), FALSE);
    }

    @Test
    public void startsWithTest009(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));
        set.add(Automaton.makeAutomaton("p"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("p"), Automaton.makeAutomaton(""));

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest010(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(" "));
        set.add(Automaton.makeAutomaton(""));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("");

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest011(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(a, sub), FALSE);
    }

    @Test
    public void startsWithTest012(){
        State q = new State("q0", true, true);
        HashSet<State> states = new HashSet<>();
        states.add(q);
        Transition t = new Transition(q, q, "a");
        HashSet<Transition> delta = new HashSet<>();
        delta.add(t);

        Automaton a = new Automaton (delta, states);
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest013(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("a"), Automaton.makeAutomaton(""));

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest014(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("a"));

        State q = new State("q0", true, true);
        HashSet<State> states = new HashSet<>();
        states.add(q);
        Transition t = new Transition(q, q, "a");
        HashSet<Transition> delta = new HashSet<>();
        delta.add(t);

        Automaton a = Automaton.union(set);
        Automaton sub = new Automaton(delta, states);

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest015(){

        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("pandapandapandapanda"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("pandapanda"), Automaton.makeAutomaton("panda"));

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);

    }

    @Test
    public void startsWithTest016(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("nda"), Automaton.makeAutomaton("a"));

        Assert.assertEquals(Automaton.startsWith(a, sub), FALSE);
    }

    @Test
    public void startsWithTest017(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandapanda"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("panda");


        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest018(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("panda"));
        set.add(Automaton.makeAutomaton("pandapanda"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("pandapanda");

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest019(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("ciao"));
        set.add(Automaton.makeAutomaton("radar"));
        set.add(Automaton.makeAutomaton("itopinonavevanonipoti"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("c");

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest020(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("ciao"));
        set.add(Automaton.makeAutomaton("radar"));
        set.add(Automaton.makeAutomaton("itopinonavevanonipoti"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("d");

        Assert.assertEquals(Automaton.startsWith(a, sub), FALSE);
    }

    @Test
    public void startsWithTest021(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("marnie"));
        set.add(Automaton.makeAutomaton("marty"));
        set.add(Automaton.makeAutomaton("mary"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("mar");

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest022(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("marnie"));
        set.add(Automaton.makeAutomaton("marty"));
        set.add(Automaton.makeAutomaton("mary"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("mar"), Automaton.makeAutomaton("m"));

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest023(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("marnie"));
        set.add(Automaton.makeAutomaton("marty"));
        set.add(Automaton.makeAutomaton("mary"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("marn"), Automaton.makeAutomaton("m"));

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest024(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton(""));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("");

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest025(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("nda"));
        set.add(Automaton.makeAutomaton("ndaa"));
        set.add(Automaton.makeAutomaton("a"));


        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("nda"), Automaton.makeAutomaton("a"));

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }


    @Test
    public void startsWithTest026(){
        HashSet<Automaton> set = new HashSet<>();
        set.add(Automaton.makeAutomaton("nda"));

        Automaton a = Automaton.union(set);
        Automaton sub = Automaton.makeAutomaton("nda");

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }

    @Test
    public void startsWithTest027(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("aaa"), Automaton.star(Automaton.makeAutomaton("a")));
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(a, sub), TRUE);
    }


    @Test
    public void startsWithTest028(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("n"), Automaton.star(Automaton.makeAutomaton("a")));
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(a, sub), FALSE);
    }
    
    @Test
    public void startsWithTest029(){
        Automaton a = Automaton.star(Automaton.makeRealAutomaton("a"));
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(a, sub), TOPBOOL);
    }

    @Test
    public void startsWithTest030(){
        Automaton a = Automaton.star(Automaton.makeRealAutomaton("a"));
        Automaton sub = Automaton.makeAutomaton("b");

        Assert.assertEquals(Automaton.startsWith(a, sub), FALSE);
    }
    
    @Test
    public void startsWithTest031(){
        Automaton a = Automaton.star(Automaton.makeRealAutomaton("a"));
        Automaton b = Automaton.star(Automaton.makeRealAutomaton("b"));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.makeAutomaton("b");

        Assert.assertEquals(Automaton.startsWith(c, sub), TOPBOOL);
    }
    

    @Test
    public void startsWithTest032(){
        Automaton a = Automaton.concat(Automaton.star(Automaton.makeRealAutomaton("a")),Automaton.makeAutomaton("a"));
        Automaton b = Automaton.concat(Automaton.star(Automaton.makeRealAutomaton("b")),Automaton.makeAutomaton("b"));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.makeAutomaton("b");

        Assert.assertEquals(Automaton.startsWith(c, sub), TOPBOOL);
    }
    

    @Test
    public void startsWithTest033(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("a"), Automaton.star(Automaton.makeRealAutomaton("a")));
        Automaton b = Automaton.concat(Automaton.makeAutomaton("b"), Automaton.star(Automaton.makeRealAutomaton("b")));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.makeAutomaton("b");

        Assert.assertEquals(Automaton.startsWith(c, sub), TOPBOOL);
    }
    
    @Test
    public void startsWithTest034(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("aa"), Automaton.star(Automaton.makeRealAutomaton("a")));
        Automaton b = Automaton.concat(Automaton.makeAutomaton("ab"), Automaton.star(Automaton.makeRealAutomaton("b")));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.makeAutomaton("a");

        Assert.assertEquals(Automaton.startsWith(c, sub), TRUE);
    }
    
    @Test
    public void startsWithTest035(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("aa"), Automaton.star(Automaton.makeRealAutomaton("a")));
        Automaton b = Automaton.concat(Automaton.makeAutomaton("ab"), Automaton.star(Automaton.makeRealAutomaton("b")));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.makeAutomaton("b");

        Assert.assertEquals(Automaton.startsWith(c, sub), FALSE);
    }
    
    @Test
    public void startsWithTest036(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("ab"), Automaton.star(Automaton.makeRealAutomaton("a")));
        Automaton b = Automaton.concat(Automaton.makeAutomaton("ab"), Automaton.star(Automaton.makeRealAutomaton("b")));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.makeAutomaton("ab");

        Assert.assertEquals(Automaton.startsWith(c, sub), TRUE);
    }
    
    @Test
    public void startsWithTest037(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("ab"), Automaton.star(Automaton.makeRealAutomaton("a")));
        Automaton b = Automaton.concat(Automaton.makeAutomaton("ab"), Automaton.star(Automaton.makeRealAutomaton("b")));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("ab"), Automaton.makeAutomaton("a"));

        Assert.assertEquals(Automaton.startsWith(c, sub), TRUE);
    }
    

    @Test
    public void startsWithTest038(){
        Automaton a = Automaton.concat(Automaton.makeAutomaton("ab"), Automaton.star(Automaton.makeRealAutomaton("a")));
        Automaton b = Automaton.concat(Automaton.makeAutomaton("aa"), Automaton.star(Automaton.makeRealAutomaton("b")));
        
        Automaton c = Automaton.union(a, b);
        Automaton sub = Automaton.union(Automaton.makeAutomaton("ab"), Automaton.makeAutomaton("aa"));

        Assert.assertEquals(Automaton.startsWith(c, sub), TOPBOOL);
    }
}