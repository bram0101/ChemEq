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

	private static long gcd(long a, long b) {
		BigInteger bi1 = BigInteger.valueOf(a);
		return bi1.gcd(BigInteger.valueOf(b)).intValue();
	}

	private static long lcm(long a, long b) {
		return a * (b / gcd(a, b));
	}

	private static long lcm(long[] input) {
		long result = input[0];
		for (int i = 1; i < input.length; i++)
			result = lcm(result, input[i]);
		return result;
	}

	public static long getLCM(double... a) {
		long[] divisors = new long[a.length];

		for (int i = 0; i < a.length; i++) {
			divisors[i] = approximateDivisor(a[i]);
		}

		return lcm(divisors);
	}

}
