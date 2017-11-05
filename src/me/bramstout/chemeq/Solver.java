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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Solver {

	public Reaction solve(Reaction reaction) {

		List<Equation> equations = new ArrayList<Equation>();
		Map<Integer, Double> solvedTerms = new HashMap<Integer, Double>();

		for (Element e : reaction.getElements()) {
			for (int i = 0; i < reaction.getLeftTerm().size(); i++) {
				Molecule m = reaction.getLeftTerm().get(i);

				if (!m.hasElement(e))
					continue;

				if (m.getFactor() > 0)
					solvedTerms.put(i, Double.valueOf(m.getFactor()));

				Term resultTerm = new Term(i, m.getElementFactor(e));

				List<Term> terms = new ArrayList<Term>();

				for (int j = 0; j < reaction.getRightTerm().size(); j++) {
					Molecule rm = reaction.getRightTerm().get(j);
					if (rm.hasElement(e))
						terms.add(new Term(reaction.getLeftTerm().size() + j,
								rm.getElementFactor(e) / resultTerm.getFactor()));
				}

				for (int k = 0; k < reaction.getLeftTerm().size(); k++) {
					Molecule lm = reaction.getLeftTerm().get(k);
					if (lm.hasElement(e) && k != i)
						terms.add(new Term(k, lm.getElementFactor(e) * -1 / resultTerm.getFactor()));
				}

				resultTerm.setFactor(1);

				Equation eq = new Equation(resultTerm, terms);

				boolean shouldAdd = true;
				for (Equation equation : equations) {
					if (equation.equals(eq)) {
						shouldAdd = false;
						break;
					}
				}
				if (shouldAdd)
					equations.add(eq);
			}

			for (int i = 0; i < reaction.getRightTerm().size(); i++) {
				Molecule m = reaction.getRightTerm().get(i);

				if (!m.hasElement(e))
					continue;

				if (m.getFactor() > 0)
					solvedTerms.put(reaction.getLeftTerm().size() + i, Double.valueOf(m.getFactor()));

				Term resultTerm = new Term(reaction.getLeftTerm().size() + i, m.getElementFactor(e));

				List<Term> terms = new ArrayList<Term>();

				for (int j = 0; j < reaction.getLeftTerm().size(); j++) {
					Molecule lm = reaction.getLeftTerm().get(j);
					if (lm.hasElement(e))
						terms.add(new Term(j, lm.getElementFactor(e) / resultTerm.getFactor()));
				}

				for (int k = 0; k < reaction.getRightTerm().size(); k++) {
					Molecule rm = reaction.getRightTerm().get(k);
					if (rm.hasElement(e) && k != i)
						terms.add(new Term(reaction.getLeftTerm().size() + k,
								rm.getElementFactor(e) * -1 / resultTerm.getFactor()));
				}

				resultTerm.setFactor(1);

				Equation eq = new Equation(resultTerm, terms);

				boolean shouldAdd = true;
				for (Equation equation : equations) {
					if (equation.equals(eq)) {
						shouldAdd = false;
						break;
					}
				}
				if (shouldAdd)
					equations.add(eq);
			}
		}

		if (equations.size() == 0)
			return reaction.copy();

		if (solvedTerms.isEmpty()) {
			boolean firstEq = true;

			for (int i = 0; i < equations.size(); i++) {
				Equation eq = equations.get(i);
				if (firstEq && eq.getTerms().size() == 1) {
					solvedTerms.put(eq.getResultTerm().getId(), 1.0D);
					firstEq = false;
					break;
				}
			}

			// We couldn't find an equation with just one term. Just going to set the first
			// equation to 1.0.
			if (firstEq) {
				Equation eq = equations.get(0);
				solvedTerms.put(eq.getResultTerm().getId(), 1.0D);
			}
		}

		equationsLoop: for (int i = 0; i < equations.size(); i++) {
			Equation eq = equations.get(i);

			if (solvedTerms.containsKey(eq.getResultTerm().getId()))
				continue;

			for (Term term : eq.getTerms())
				if (!solvedTerms.containsKey(term.getId()))
					continue equationsLoop;

			double result = 0;

			for (Term term : eq.getTerms()) {
				result += term.getFactor() * solvedTerms.get(term.getId()).doubleValue();
			}

			solvedTerms.put(eq.getResultTerm().getId(), result);

			i = -1;
		}

		double[] factors = new double[solvedTerms.size()];

		int ii = 0;

		List<Integer> keyOrder = new ArrayList<Integer>();

		for (Entry<Integer, Double> entry : solvedTerms.entrySet()) {

			factors[ii++] = entry.getValue();

			keyOrder.add(entry.getKey());
		}

		double lcm = MathUtil.getLCM(factors);

		for (Integer c : keyOrder) {
			solvedTerms.put(c, solvedTerms.get(c) * lcm);
		}

		Reaction result = reaction.copy();

		for (int i = 0; i < result.getLeftTerm().size(); i++) {
			if (solvedTerms.containsKey(Integer.valueOf(i)))
				result.getLeftTerm().get(i).setFactor((int) solvedTerms.get(Integer.valueOf(i)).doubleValue());
		}

		for (int i = 0; i < result.getRightTerm().size(); i++) {
			int c = i + result.getLeftTerm().size();
			if (solvedTerms.containsKey(Integer.valueOf(c)))
				result.getRightTerm().get(i).setFactor((int) solvedTerms.get(Integer.valueOf(c)).doubleValue());
		}

		return result;
	}

}
