package me.bramstout.chemeq;

import java.util.ArrayList;
import java.util.List;

public class Molecule extends Element {

	private List<Element> elements;

	public Molecule(List<Element> elements, int factor) {
		super(elementsToString(elements), factor);
	}

	public List<Element> getElements() {
		return elements;
	}

	public boolean hasElement(Element e) {
		for (Element el : elements) {
			if (el.equals(e))
				return true;
			if (el instanceof Molecule)
				if (((Molecule) el).hasElement(e))
					return true;
		}
		return false;
	}

	public Element getFirstElement(String e) {
		for (Element el : elements)
			if (el.getData() == e)
				return el;
		return null;
	}

	public List<Element> getAllElements(String e) {
		List<Element> allElements = new ArrayList<Element>();
		for (Element el : elements)
			if (el.getData() == e)
				allElements.add(el);
		return allElements;
	}

	@Override
	public boolean equals(Object a) {
		if (!(a instanceof Molecule))
			return false;
		return super.equals(a);
	}

	private static String elementsToString(List<Element> elements) {
		StringBuilder sb = new StringBuilder();
		for (Element e : elements)
			sb.append(e.toString());
		return sb.toString();
	}

}
