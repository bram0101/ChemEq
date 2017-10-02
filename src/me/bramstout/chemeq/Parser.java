package me.bramstout.chemeq;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	public Reaction parse(String s) {
		StringIterator si = new StringIterator(s);
		List<Molecule> leftTerm = parseTerm(si);
		List<Molecule> rightTerm = parseTerm(si);
		List<Element> elements = new ArrayList<Element>();
		// TODO: Go through the terms, and collect the elements
		return new Reaction(leftTerm, rightTerm, elements);
	}

	private List<Molecule> parseTerm(StringIterator si) {
		List<Molecule> term = new ArrayList<Molecule>();
		while(si.hasNext()) {
			char c = si.peekNext();
			if(c == '=' || c == '-')
				break;
		}
		return term;
	}

}
