package it.univr.fsm.config;

public class Config {

	public static int WIDENING = 4;
	public static int GEN = 0;
	public static boolean PRETTY_PRINT = false;
	public static boolean AUTOMATON_PRINT = true;

    public static void incrementGen() {
        GEN++;
    }
	
    public static int getGen() {
        return GEN;
    }
    
	/*
	 * Constant strings for regular expression generation 
	 */
	public static final String iff ="} if (";
	public static final String rpar = "}";
	public static final String semicolon = ";";
	public static final String g = "g";
	public static final String var = " var ";
	public static final String rand_while =" =rand(); while (";
	public static final String rand_if =" =rand(); if (";
	public static final String equals1 = "==1){";
	public static final String equals2 = "==2){";

}
