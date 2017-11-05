/*
 * MIT License
 * 
 * Copyright (c) 2017 Bram Stout
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.bramstout.chemeq;

import java.math.BigInteger;

public class MathUtil {

	/**
	 * (NL) Deze methode geeft de waarde van de noemer van de meest vereenvoudigste breuk die de waarde weergeeft. <br>
	 * (EN) This method returns the value of the denominator of the most reduced fraction that represents the value.
	 * @param value (NL) De waarde als kommagetal. (EN) The value as a decimal number.
	 * @return (NL) De noemer. (EN) The denominator.
	 */
	public static int approximateDivisor(double value) {
		final double EPSILON = .000001d;

		int n = 1;
		int d = 1;
		double fraction = n / d;

		while (Math.abs(fraction - value) > EPSILON) {
			if (fraction < value) {
				n++;
			} else {
				d++;
				n = (int) Math.round(value * d);
			}

			fraction = n / (double) d;
		}

		return d;
	}

	/**
	 * (NL) Geeft de grootste gemeenschappelijke deler. <br>
	 * (EN) Returns the greatest common devisor.
	 * @param a (NL) Een waarde. (EN) A value.
	 * @param b (NL) Een andere waarde. (EN) Another value.
	 * @return (NL) De grootste gemeenschappelijke deler. (EN) The greatest common divisor.
	 */
	private static long gcd(long a, long b) {
		BigInteger bi1 = BigInteger.valueOf(a);
		return bi1.gcd(BigInteger.valueOf(b)).intValue();
	}

	/**
	 * (NL) Geeft de kleinste gemeenschappelijke veelvoud weer. <br>
	 * (EN) Returns the lowest common multiple.
	 * @param a (NL) Een waarde. (EN) A value.
	 * @param b (NL) Een andere waarde. (EN) Another value.
	 * @return (NL) De kleinste gemeenschappelijke veelvoud. (EN) The lowest common multiple.
	 */
	private static long lcm(long a, long b) {
		return a * (b / gcd(a, b));
	}

	/***
	 * (NL) Geeft de kleinste gemeenschappelijke veelvoud van een reeks getallen weer. <br>
	 * (EN) Returns the lowest common multiple of a series of numbers.
	 * @param input (NL) De reeks getallen. (EN) The series of numbers.
	 * @return (NL) De kleinste gemeenschappelijke veelvoud. (EN) The lowest common multiple.
	 */
	private static long lcm(long[] input) {
		long result = input[0];
		for (int i = 1; i < input.length; i++)
			result = lcm(result, input[i]);
		return result;
	}

	/***
	 * (NL) Geeft de kleinste gemeenschappelijke veelvoud van een reeks kommagetallen weer. <br>
	 * (EN) Returns the lowest common multiple of a series of decimal numbers.
	 * @param input (NL) De reeks getallen. (EN) The series of numbers.
	 * @return (NL) De kleinste gemeenschappelijke veelvoud. (EN) The lowest common multiple.
	 */
	public static long getLCM(double... a) {
		long[] divisors = new long[a.length];

		for (int i = 0; i < a.length; i++) {
			divisors[i] = approximateDivisor(a[i]);
		}

		return lcm(divisors);
	}

}
