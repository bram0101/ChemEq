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

/**
 * (NL) Deze klas probeert een reactievergelijking gelijk te maken. <br>
 * (EN) This class tries to solve a chemical reaction.
 * 
 * @author Bram Stout
 *
 */
public class Solver {

	/**
	 * (NL) Los de reactievergelijking op. <br>
	 * (EN) Solve the chemical reaction.
	 * 
	 * @param reaction
	 *            (NL) De vergelijking om op te lossen. (EN) The reaction to solve.
	 * @return (NL) De opgeloste vergelijking. (EN) The solved reaction.
	 */
	public Reaction solve(Reaction reaction) {
		/*
		 * (NL) Het oplossen wordt in meerdere stappen gedaan. Eerst maken wij
		 * vergelijkingen die de verhoudingen tussen verschillende moleculen weergeeft.
		 * De vergelijkingen worden zo opgeschreven dat er maar ��n molecuul aan de
		 * linker kant staat. Wanneer alle vergelijking klaar zijn, worden ze opgelost.
		 * Eerst wordt er ��n molecuul naar 1.0 gezet. Hierdoor kunnen de vergelijkingen
		 * die dat ene molecuul alleen maar nodig hebben, opgelost worden. Dan kunnen de
		 * vergelijkingen die, die moleculen nodig hebben, worden opgelost, enzovoort.
		 * Daarna hebben wij veel kommagetallen. Deze moeten wij naar hele getallen
		 * zetten. Daarvoor rekenen wij de laagste gemeenschappelijke veelhoud. Wij
		 * kunnen alle getallen daarme vermenigvuldigen om een heel getal te krijgen.
		 * (EN) Solving it, is done in multiple stages. First, we make equations of the
		 * ratios between the molecules. The equations are written in such a way that
		 * only one molecule is on the left side. When that is done, they are solved.
		 * First, one molecule is set to 1.0. Then any equations that then can be
		 * solved, are solved. The equations that then can be solved are solved, and
		 * this goes on until all equations are solved, if possible. Then we have a
		 * bunch of decimal numbers. These need to be converted to whole numbers. This
		 * is done by finding the lowest common multiple. All numbers are multiplied by
		 * that, and we get whole numbers.
		 */

		// (NL) De variabelen om de vergelijkingen en opgeloste vergelijkingen op te
		// slaan.
		// (EN) The variables for the equations and solved equations.
		List<Equation> equations = new ArrayList<Equation>();
		Map<Integer, Double> solvedTerms = new HashMap<Integer, Double>();

		// (NL) Maak voor elke atoomsoort, voor elke molecuul met die atoomsoort, een
		// vergelijking.
		// (EN) Create for every type of atom, for every molecule with that type, an
		// equation.
		for (Element e : reaction.getElements()) {
			// (NL) De moleculen staan in twee aparte lijsten opgeslagen. Dus wij gaan door
			// de lijsten apart heen.
			// (EN) The molecules are saved in two different lists. So we iterate over them
			// seperately.
			for (int i = 0; i < reaction.getLeftTerm().size(); i++) {
				// (NL) De molecuul waar wij nu mee bezig zijn.
				// (EN) The molecule that we are working with right now.
				Molecule m = reaction.getLeftTerm().get(i);

				// (NL) Als die molecuul het huidige atoomsoort niet heeft, kunnen we hem
				// overslaan.'
				// (EN) If the current molecule does not have the atom type, we skip it.
				if (!m.hasElement(e))
					continue;

				// (NL) Als er geen co�ffici�nt was aangegeven, dan is die -1. Als er wel een is
				// aangegeven, dan slaan wij die alvast op, en hoeven wij minder vergelijkingen
				// op te lossen.
				// (EN) If there was no coefficient supplied, it is noted as -1. If there was
				// one, we are going to save it as solved. This way, we can skip it later on.
				if (m.getFactor() > 0)
					solvedTerms.put(i, Double.valueOf(m.getFactor()));

				// (NL) De term die wij willen oplossen. Wij gebruiken nu de element factor om
				// de andere termen alvast er door te delen. Later wordt dit 1.
				// (EN) The term that we want to solve. We use the element factor to divide
				// other terms by it. Later this will become 1.
				Term resultTerm = new Term(i, m.getElementFactor(e));

				// (NL) De termen aan de rechter kant van de vergelijking.
				// (EN) The terms on the right side of the equation.
				List<Term> terms = new ArrayList<Term>();

				// (NL) Het idee gaat zo: alle atomen van dezelfde soort bij elkaar aan de
				// linker kant, zijn gelijk aan de hoeveelheid atomen van dezelfde soort bij
				// elkaar, aan de rechter kant. Om aan de linker kant maar ��n molecuul te
				// krijgen, moeten wij de andere moleculen aan de linker kant naar rechts
				// zetten, maar dan met -1.0 vermenigvuldigen, omdat wij het moeten aftrekken.
				// Wij delen door de factor van de linker kant zodat die uiteindelijk 1.0 is,
				// zoals wij willen.
				// (EN) The idea goes like this: all atoms of the same kind on the left side,
				// together are equal to the amount of atoms of the same kind on the right side
				// together. To get only one molecule on the left side, we need to subtract the
				// other molecules on the left side from the right side. Basically add them to
				// the right side, but multiply by -1.0 so that they substract. We divide by the
				// factor of the left side, so that the factor of the left side will equal to
				// 1.0, as we want.

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

				// (NL) Maak het object aan en kijk of die vergelijking niet al erin zit. Zo
				// niet, dan voegen wij hem toe.
				// (EN) Create the object, and check if the equation is not already in the list.
				// If not, we add it to the list.
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

			// (NL) Precies hetzelfde wordt nu gedaan als bij de for loop hiervoor. In dit
			// geval doen wij het bij de rechter kant en is alles dus omgekeerd.
			// (EN) Exactly the same is done here as with the for loop before. In this case,
			// we do it on the right side en it is thus reversed.
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

		// (NL) Als wij geen vergelijkingen hebben kunnen maken, dan geven wij gewoon de
		// reactie terug zonder aanpassingen.
		// (EN) If we could not have made any equations, we just return the input
		// reaction without any changes.
		if (equations.size() == 0)
			return reaction.copy();

		// (NL) Als er nog geen vergelijking is opgelost, moeten wij ergens beginnen. We
		// proberen eerst een vergelijking te vinden met maar ��n ander molecuul aan de
		// rechter kant. Deze zetten wij dan als 1.0. Hierdoor hebben wij direct al twee
		// moleculen opgelost in plaats van ��n, Als dat niet lukt, dan zetten wij
		// gewoon een molecuul als 1.0. Dit zal toch geen oplossing geven, maar het is
		// beter dan niks.
		// (EN) If no equation is solved yet, we start by setting one equation to 1.0.
		// We try to find an equation with only one term on the right. This way we have
		// an actual start. If none was found, we just set one equation to 1.0. It still
		// would not be able to solve it then, but we still do it.
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

			// (NL) We konden geen vergelijking met maar ��n molecuul aan de rechter kant
			// finden. Dus we zetten gewoon een vergelijking naar 1.0.
			// (EN) We couldn't find an equation with just one term. Just going to set the
			// first
			// equation to 1.0.
			if (firstEq) {
				Equation eq = equations.get(0);
				solvedTerms.put(eq.getResultTerm().getId(), 1.0D);
			}
		}

		// (NL) Wij gaan nu langs elke vergelijking.
		// (EN) We iterate over every equation.
		equationsLoop: for (int i = 0; i < equations.size(); i++) {
			Equation eq = equations.get(i);

			// (NL) Als de molecuul al is opgelost, dan slaan wij deze over.
			// (EN) If the molecule is already solvest, we just skip this one.
			if (solvedTerms.containsKey(eq.getResultTerm().getId()))
				continue;

			// (NL) Als er nog een term is, die nog niet is opgelost dan slaan wij deze over
			// totdat alle termen wel zijn opgelost.
			// (EN) If there is still a term that is not solved, we skip this until all the
			// terms are solved.
			for (Term term : eq.getTerms())
				if (!solvedTerms.containsKey(term.getId()))
					continue equationsLoop;

			// (NL) Het antwoord van de vergelijking.
			// (EN) The result of the equation.
			double result = 0;

			// (NL) Ga langs alle termen en tel het antwoord bij het resultaat op.
			// (EN) Iterate over all terms and add the answer to the result.
			for (Term term : eq.getTerms()) {
				result += term.getFactor() * solvedTerms.get(term.getId()).doubleValue();
			}

			// (NL) Sla het resultaat op.
			// (EN) Save the result.
			solvedTerms.put(eq.getResultTerm().getId(), result);

			// (NL) Omdat er weer een molecuul is opgelost, beginnen wij weer bij de eerste
			// vergelijking. Het kan zijn dat er een vergelijking met een eerdere index van
			// deze die wij nu wel kunnen oplossen. 'i' wordt -1 omdat aan het eind, er 1
			// bij opgeteld wordt, en wij willen dat het 0 is wanneer die dan weer opnieuw
			// bezig gaat.
			// (EN) Because another molecule has been solved, the start over again to the
			// first equation, because it could be that an equation can be solved now and
			// not before. We set it to -1 since at the end 1 is added when it goes back up
			// again, and we want it to be 0 when we are at the top again.
			i = -1;
		}

		// (NL) Bij sommige reacties zijn er nog een paar moleculen die niet opgelost
		// zijn. Dat kan bijvoorbeeld moleculen zijn die eigenlijk niet meedoen aan de
		// reactie, of moleculen die enkel nodig zijn om elektronen op te nemen of af te
		// geven. Wat deze moleculen in gemeen hebben, is dat er maar ��n aan beide
		// kanten staan. Dus moeten wij kijken of er nog enige moleculen zijn die niet
		// opgelost
		// zijn.
		// (EN) With some reactions, there are a few molecules that are not solved yet.
		// The molecules could be molecules that do not actually contribute to the
		// reaction, of molecules that are only needed to take or give elektrons. What
		// these molecules have in common, is that on both sides there is only one of
		// those molecules.
		for (int i = 0; i < equations.size(); i++) {
			Equation eq = equations.get(i);

			// (NL) Als de molecuul al is opgelost, dan slaan wij deze over.
			// (EN) If the molecule is already solvest, we just skip this one.
			if (solvedTerms.containsKey(eq.getResultTerm().getId()))
				continue;

			// (NL) Wij willen juist de moleculen waar er maar ��n aan beide kanten
			// voorkomen. Anders slaan wij ze over. Zo een vergelijking ziet er dan uit als
			// 'a = c'. Dus er is maar ��n term.
			// (EN) We only want the molecules that occur once on both sides. Else we skip
			// them. An equation like that looks like 'a = c', so there is only one term.
			if (eq.getTerms().size() != 1)
				continue;

			// (NL) Als de molecuul dezelfde lading heeft, dan kan de molecuul net zo goed
			// weg gaan, en kunnen we het elke co�ffici�nt geven die we willen. Wij zetten
			// het dan gewoon naar ��n. Als het een andere lading heeft, dan speelt het mee
			// met de rest van de vergelijking en hangt de co�ffici�nt af van de rest en
			// moeten wij die oplossen.
			// (EN) If the molecule has the same charge, the molecules can be removed
			// without any changes to the rest. So we can just set the coefficient to one.
			// If the charges differ, we need to solve the coefficients as they are affected
			// by the rest.
			int chargeA = eq.getResultTerm().getId() < reaction.getLeftTerm().size()
					? reaction.getLeftTerm().get(eq.getResultTerm().getId()).getCharge()
					: reaction.getRightTerm().get(eq.getResultTerm().getId() - reaction.getLeftTerm().size())
							.getCharge();
			int chargeB = eq.getTerms().get(0).getId() < reaction.getLeftTerm().size()
					? reaction.getLeftTerm().get(eq.getTerms().get(0).getId()).getCharge()
					: reaction.getRightTerm().get(eq.getTerms().get(0).getId() - reaction.getLeftTerm().size())
							.getCharge();
			if (chargeA == chargeB) {
				// (NL) De ladingen zijn hetzelfde, dus de co�ffici�nten worden ��n. Wij doen
				// ook alvast de andere term.
				// (EN) De charges are the same, so the coefficients will become one. The also
				// set the other term.
				solvedTerms.put(eq.getResultTerm().getId(), 1.0);
				solvedTerms.put(eq.getTerms().get(0).getId(), 1.0);
			} else {
				// (NL) De ladingen zijn anders. Uiteindelijk moeten de ladingen aan beide
				// kanten gelijk zijn. De volgende formule wordt gebruikt: "factor(chargeA -
				// chargeB) = chargesRight - chargesLeft".
				// (EN) The charges differ. In the end, the charges on both sides need to equal.
				// De following formula will be used: "factor(chargeA - chargeB) = chargesRight
				// - chargesLeft".

				// (NL) Wij willen alle ladingen links optellen. De twee moleculen die wij juist
				// willen weten slaan we over.
				// (EN) We want to add up all the charges on the left side. The two molecules we
				// want to solve, we skip.
				double chargesLeft = 0;
				for (int j = 0; j < reaction.getLeftTerm().size(); j++) {
					if (j == eq.getResultTerm().getId() || j == eq.getTerms().get(0).getId()
							|| !solvedTerms.containsKey(j))
						continue;
					Molecule lm = reaction.getLeftTerm().get(j);
					chargesLeft += lm.getCharge() * solvedTerms.get(j).doubleValue();
				}
				// (NL) Nu doen wij hetzelfde voor de rechter kant.
				// (EN) Now we do the same for the right side.
				double chargesRight = 0;
				for (int k = 0; k < reaction.getRightTerm().size(); k++) {
					if (k == eq.getResultTerm().getId() || k == eq.getTerms().get(0).getId()
							|| !solvedTerms.containsKey(reaction.getLeftTerm().size() + k))
						continue;
					Molecule lm = reaction.getRightTerm().get(k);
					chargesRight += lm.getCharge() * solvedTerms.get(reaction.getLeftTerm().size() + k).doubleValue();
				}
				// (NL) Nu hoeven wij enkel maar de formule in te vullen. We delen ook door
				// (chargeA - chargeB) om de factor te krijgen, wat de co�ffici�nt is. We hoeven
				// geen zorgen te maken over delen door nul. chargeA en chargeB zijn niet
				// gelijk, dus kan het niet nul worden.
				// (EN) Now we just have to fill in the formula. We divide by (chargeA -
				// chargeB) to get the factor, which is the coefficient. We do not have to worry
				// about a divide by zero, as we now chargeA and chargeB are not the same and
				// thus cannot be zero.
				double chargeResult = (chargesRight - chargesLeft) / ((double) (chargeA - chargeB));
				// (NL) Als laatste slaan wij het op.
				// (EN) Finally we save the results.
				solvedTerms.put(eq.getResultTerm().getId(), chargeResult);
				solvedTerms.put(eq.getTerms().get(0).getId(), chargeResult);
			}
		}

		// (NL) Wij willen nu de laagste gemeenschappelijke veelvoud berekenen van alle
		// getallen. Eerst moeten wij alle co�ffici�nten die wij weten opslaan naar een
		// lijst zodat wij het kunnen gebruiken in de hulp klas. Wij
		// slaan ook op bij welke molecuul elk getal hoort doormiddel van een lijst die
		// dezelfde volgorde heeft.
		// (EN) We want the lowest common multiple of all the numbers. First we need to
		// save all the known coefficients to a list to be able to send it to the
		// utility class. We save which molecule each number belongs to in a list with
		// the same order.
		double[] factors = new double[solvedTerms.size()];

		int ii = 0;

		List<Integer> keyOrder = new ArrayList<Integer>();

		for (Entry<Integer, Double> entry : solvedTerms.entrySet()) {

			factors[ii++] = entry.getValue();

			keyOrder.add(entry.getKey());
		}

		// (NL) Bereken de laagste gemeenschappelijke veelhoud en vermenigvuldig elke
		// co�ffici�nt hiermee.
		// (EN) Calculate the lowest common multiple and multiply it with all known
		// co�fficients.
		double lcm = MathUtil.getLCM(factors);

		for (Integer c : keyOrder) {
			solvedTerms.put(c, solvedTerms.get(c) * lcm);
		}

		// (NL) Kopie�r de reactievergelijking en sla de berekende co�ffici�nten op.
		// (EN) Copy the chemical reaction and save all the calculated coefficients.
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

		// (NL) Misschien een controle toevoegen om te kijken of de solver correct is.
		// Zo niet, dan geven we de originele reactievergelijking terug.
		// (EN) Maybe do a check to see if the solver is correct. If not, then we return
		// the original reaction.

		// (NL) Geef het resultaat terug.
		// (EN) Return the result.
		return result;
	}

}
