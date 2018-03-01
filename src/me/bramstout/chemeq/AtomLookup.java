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

import java.util.HashMap;
import java.util.Map;

/**
 * (NL) Het periodiek systeem. <br>
 * (EN) The periodic table.
 * 
 * @author Bram Stout
 *
 */

public enum AtomLookup {
	
	H(1, "H", 1.00794), He(2, "He", 4.002602), 
	Li(3, "Li", 6.941), Be(4, "Be", 9.012182), B(5, "B", 10.811), C(6, "C", 12.0107), N(7, "N", 14.0067), O(8, "O", 15.9994), F(9, "F", 18.998404), Ne(10, "Ne", 20.1797), 
	Na(11, "Na", 22.989769), Mg(12, "Mg", 24.305), Al(13, "Al", 26.981539), Si(14, "Si", 28.0855), P(15, "P", 30.973763), S(16, "S", 32.065), Cl(17, "Cl", 35.453), Ar(18, "Ar", 39.948), 
	K(19, "K", 39.0983), Ca(20, "Ca", 40.078), Sc(21, "Sc", 44.955914), Ti(22, "Ti", 47.867), V(23, "V", 50.9415), Cr(24, "Cr", 51.9961), Mn(25, "Mn", 54.938046), Fe(26, "Fe", 55.845), Co(27, "Co", 58.933193), Ni(28, "Ni", 58.6934), Cu(29, "Cu", 63.546), Zn(30, "Zn", 65.38), Ga(31, "Ga", 69.723), Ge(32, "Ge", 72.63), As(33, "As", 74.9216), Se(34, "Se", 78.96), Br(35, "Br", 79.904), Kr(36, "Kr", 83.798), 
	Rb(37, "Rb", 85.4678), Sr(38, "Sr", 87.62), Y(39, "Y", 88.90585), Zr(40, "Zr", 91.224), Nb(41, "Nb", 92.90638), Mo(42, "Mo", 95.96), Tc(43, "Tc", 98), Ru(44, "Ru", 101.07), Rh(45, "Rh", 102.9055), Pd(46, "Pd", 106.42), Ag(47, "Ag", 107.8682), Cd(48, "Cd", 112.411), In(49, "In", 114.818), Sn(50, "Sn", 118.71), Sb(51, "Sb", 121.76), Te(52, "Te", 127.6), I(53, "I", 126.90447), Xe(54, "Xe", 131.293), 
	Cs(55, "Cs", 132.90546), Ba(56, "Ba", 137.327), La(57, "La", 138.90547), Ce(58, "Ce", 140.116), Pr(59, "Pr", 140.90765), Nd(60, "Nd", 144.242), Pm(61, "Pm", 145), Sm(62, "Sm", 150.36), Eu(63, "Eu", 151.964), Gd(64, "Gd", 157.25), Tb(65, "Tb", 158.92535), Dy(66, "Dy", 162.5), Ho(67, "Ho", 164.93031), Er(68, "Er", 167.259), Tm(69, "Tm", 168.9342), Yb(70, "Yb", 173.054), Lu(71, "Lu", 174.9668), Hf(72, "Hf", 178.49), Ta(73, "Ta", 180.94788), W(74, "W", 183.84), Re(75, "Re", 186.207), Os(76, "Os", 190.23), Ir(77, "Ir", 192.217), Pt(78, "Pt", 195.084), Au(79, "Au", 196.96657), Hg(80, "Hg", 200.59), Tl(81, "Tl", 204.3833), Pb(82, "Pb", 207.2), Bi(83, "Bi", 208.9804), Po(84, "Po", 209), At(85, "At", 210), Rn(86, "Rn", 222), 
	Fr(87, "Fr", 223), Ra(88, "Ra", 226), Ac(89, "Ac", 227), Th(90, "Th", 232.03806), Pa(91, "Pa", 231.03587), U(92, "U", 238.02892), Np(93, "Np", 237), Pu(94, "Pu", 244), Am(95, "Am", 243), Cm(96, "Cm", 247), Bk(97, "Bk", 247), Cf(98, "Cf", 251), Es(99, "Es", 252), Fm(100, "Fm", 257), Md(101, "Md", 258), No(102, "No", 259), Lr(103, "Lr", 262), Rf(104, "Rf", 267), Db(105, "Db", 268), Sg(106, "Sg", 271), Bh(107, "Bh", 272), Hs(108, "Hs", 270), Mt(109, "Mt", 276), Ds(110, "Ds", 281), Rg(111, "Rg", 280), Cn(112, "Cn", 285), Nh(113, "Nh", 284), Fl(114, "Fl", 289), Mc(115, "Mc", 288), Lv(116, "Lv", 293), Ts(117, "Ts", 294), Og(118, "Og", 294);
	
	int id;
	String sid;
	double mass;
	
	AtomLookup(int id, String sid, double mass){
		this.id = id;
		this.sid = sid;
		this.mass = mass;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return sid;
	}
	
	public double getMass() {
		return mass;
	}
	
	private static Map<String, AtomLookup> name2Atom;
	
	static {
		name2Atom = new HashMap<String, AtomLookup>();
		for(AtomLookup atom : values()){
			name2Atom.put(atom.sid, atom);
		}
	}
	
	public static AtomLookup getFromName(String name) {
		return name2Atom.get(name);
	}
	
}
