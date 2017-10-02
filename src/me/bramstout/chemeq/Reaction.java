package me.bramstout.chemeq;

import java.util.List;

public class Reaction {

	private List<Molecule> leftTerm;
	private List<Molecule> rightTerm;
	private List<Element> elements;

	public Reaction(List<Molecule> leftTerm, List<Molecule> rightTerm, List<Element> elements) {
		super();
		this.leftTerm = leftTerm;
		this.rightTerm = rightTerm;
		this.elements = elements;
	}

	public List<Molecule> getLeftTerm() {
		return leftTerm;
	}

	public void setLeftTerm(List<Molecule> leftTerm) {
		this.leftTerm = leftTerm;
	}

	public List<Molecule> getRightTerm() {
		return rightTerm;
	}

	public void setRightTerm(List<Molecule> rightTerm) {
		this.rightTerm = rightTerm;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < leftTerm.size(); i++) {
			sb.append(leftTerm.get(i).toString());
			if(i < leftTerm.size() - 1)
				sb.append(" + ");
		}
		sb.append(" = ");
		for(int i = 0; i < rightTerm.size(); i++) {
			sb.append(rightTerm.get(i).toString());
			if(i < rightTerm.size() - 1)
				sb.append(" + ");
		}
		return sb.toString();
	}

}
