package com.cooksys.ftd.assignments.objects;

public class Launcher {
	public static void main(String[] args)
	{

		SimplifiedRational sr1 = new SimplifiedRational(24,12);
		SimplifiedRational sr2 = new SimplifiedRational(0,3);
		
		//Rational r1 = new Rational(24,12);
		//Rational r2 = new Rational(2,1);
		
		/*
		 * sr2.invert() throws IllegalArgumentException from gcd method 
		* before it can execute invert(), where it would otherwise
		* throw IllegalStateException because numerator becoming denominator
		* would be 0. This causes the final test from the simplifiedrational
		* test suite to fail.
		*/
		
		System.out.println(sr1.equals(sr2));
	}
}
