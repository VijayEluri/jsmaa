/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.smaa.jsmaa.gui;

import javax.swing.JFrame;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.simulator.SMAATRIResults;
import fi.smaa.jsmaa.simulator.SMAATRISimulation;

public class SMAATRISimulationBuilder extends BasicSimulationBuilder<SMAATRIModel, SMAATRIResults, SMAATRISimulation> {

	public SMAATRISimulationBuilder(SMAATRIModel model, GUIFactory factory, JFrame frame) {
		super(model, factory, frame);
		
		connectNameAdapters(model.getCategories(), this.model.getCategories());		
	}
	
	@Override
	public SMAATRISimulation generateSimulation() {
		return new SMAATRISimulation(model, RandomUtil.createWithFixedSeed(), ITERATIONS);	
	}
}
