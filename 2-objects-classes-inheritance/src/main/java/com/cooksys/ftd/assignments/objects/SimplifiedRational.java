package com.cooksys.ftd.assignments.objects;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimplifiedRational implements IRational {

	private int numerator_simp = 0;
	private int denominator_simp = 1;

	/**
	 * Determines the greatest common denominator for the given values
	 *
	 * @param a
	 *            the first value to consider
	 * @param b
	 *            the second value to consider
	 * @return the greatest common denominator, or shared factor, of `a` and `b`
	 * @throws IllegalArgumentException
	 *             if a <= 0 or b < 0
	 */
	public static int gcd(int a, int b) throws IllegalArgumentException {
		if (a <= 0 || b < 0)
			throw new IllegalArgumentException();
		
		int val1 = a;
		int val2 = b;
		
		while(val1 != val2)
		{
			if (val1 > val2)
			{
				val1 = val1 - val2;
			}
			else
			{
				val2 = val2 - val1;
			}
			
		}
		System.out.println("gcd" + a +  "," + b + " = "+ val1);
		return val1;
		
		/*
		int gcd = 0;
		
		for (int i=1; i < a && i <= b; i++)
		{
			if (a%i==0 && b%i == 0)
			{
				gcd = i;
			}
			
		}
		
		return gcd;
		*/
		
	}

	/**
	 * Simplifies the numerator and denominator of a rational value.
	 * <p>
	 * For example: `simplify(10, 100) = [1, 10]` or: `simplify(0, 10) = [0, 1]`
	 *
	 * @param numerator
	 *            the numerator of the rational value to simplify
	 * @param denominator
	 *            the denominator of the rational value to simplify
	 * @return a two element array representation of the simplified numerator
	 *         and denominator
	 * @throws IllegalArgumentException
	 *             if the given denominator is 0
	 */
	public static int[] simplify(int numerator, int denominator) throws IllegalArgumentException {
		if (denominator == 0) {
			throw new IllegalArgumentException();
		//} else if (denominator == 1) {
			//return new int[] { numerator, 1 };
		}
		
		//if (numerator < 0 && denominator < 0) ;
		if (numerator == 0) return new int[]{0,denominator};
		
		int gcd = gcd(Math.abs(numerator), Math.abs(denominator));

		//if (gcd == 1)
			//return new int[] { 1, 1 };

		int[] frac = new int[2];

		frac[0] = numerator / gcd;
		frac[1] = denominator / gcd;

		// what if num == den? impossible?

		System.out.println("simplified: " + numerator + "/" + denominator + " to " + frac[0] + "/" + frac[1]);
		return frac;
	}

	/**
	 * Constructor for rational values of the type:
	 * <p>
	 * `numerator / denominator`
	 * <p>
	 * Simplification of numerator/denominator pair should occur in this method.
	 * If the numerator is zero, no further simplification can be performed
	 *
	 * @param numerator
	 *            the numerator of the rational value
	 * @param denominator
	 *            the denominator of the rational value
	 * @throws IllegalArgumentException
	 *             if the given denominator is 0
	 */
	public SimplifiedRational(int numerator, int denominator) throws IllegalArgumentException {
		if (denominator == 0)
			throw new IllegalArgumentException();
		
		if (numerator == 0){numerator_simp = numerator; denominator_simp = denominator;}
		
		int[] simplified = simplify(numerator, denominator);

		numerator_simp = simplified[0];
		denominator_simp = simplified[1];

	}

	/**
	 * @return the numerator of this rational number
	 */
	@Override
	public int getNumerator() {
		return numerator_simp;
	}

	/**
	 * @return the denominator of this rational number
	 */
	@Override
	public int getDenominator() {
		return denominator_simp;
	}

	/**
	 * Specializable constructor to take advantage of shared code between
	 * Rational and SimplifiedRational
	 * <p>
	 * Essentially, this method allows us to implement most of IRational methods
	 * directly in the interface while preserving the underlying type
	 *
	 * @param numerator
	 *            the numerator of the rational value to construct
	 * @param denominator
	 *            the denominator of the rational value to construct
	 * @return the constructed rational value (specifically, a
	 *         SimplifiedRational value)
	 * @throws IllegalArgumentException
	 *             if the given denominator is 0
	 */
	@Override
	public SimplifiedRational construct(int numerator, int denominator) throws IllegalArgumentException {
		if (denominator == 0)
			throw new IllegalArgumentException();

		return new SimplifiedRational(numerator, denominator);
	}

	/**
	 * @param obj
	 *            the object to check this against for equality
	 * @return true if the given obj is a rational value and its numerator and
	 *         denominator are equal to this rational value's numerator and
	 *         denominator, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IRational))
			return false;

		IRational given = (IRational) obj;

		//SimplifiedRational sr1 = construct(this.getNumerator(), this.getDenominator());
		//SimplifiedRational sr2 = construct(given.getNumerator(), given.getDenominator());

		if (((this.getNumerator() == given.getNumerator()) 
				&& (this.getDenominator() == given.getDenominator()))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * If this is positive, the string should be of the form
	 * `numerator/denominator`
	 * <p>
	 * If this is negative, the string should be of the form
	 * `-numerator/denominator`
	 *
	 * @return a string representation of this rational value
	 */
	@Override
    public String toString() {
    	
    	
    	if (denominator_simp < 0 && numerator_simp < 0)
    	{
    		return new String("" + numerator_simp*-1 + "/" + (denominator_simp*-1));
    	}
    	else if (denominator_simp < 0) 
    	{
        	return new String("" + numerator_simp*-1 + "/" + (denominator_simp*-1));
    	}
    	else 
    	{
    		return new String("" + numerator_simp + "/" + denominator_simp);
    	}
    }
}
