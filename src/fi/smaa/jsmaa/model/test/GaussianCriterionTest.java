/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

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

package fi.smaa.jsmaa.model.test;

import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.common.Interval;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.GaussianCriterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;

public class GaussianCriterionTest {
	
	private CardinalCriterion<GaussianMeasurement> crit;
	private List<Alternative> alts;
	
	@Before
	public void setUp() {
		crit = new GaussianCriterion("crit");
		alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
	} 
	
	@Test
	public void testSetMeasurements() {
		crit.setAlternatives(alts);
		HashMap<Alternative, GaussianMeasurement> oldVal 
			= new HashMap<Alternative, GaussianMeasurement>();
		oldVal.put(alts.get(0), new GaussianMeasurement());
		oldVal.put(alts.get(1), new GaussianMeasurement());
		HashMap<Alternative, GaussianMeasurement> newVal = generateMeas2Alts();
		JUnitUtil.testSetter(crit, GaussianCriterion.PROPERTY_MEASUREMENTS, oldVal, newVal);
	}

	private HashMap<Alternative, GaussianMeasurement> generateMeas2Alts() {
		HashMap<Alternative, GaussianMeasurement> meas
			= new HashMap<Alternative, GaussianMeasurement>();
		meas.put(alts.get(0), new GaussianMeasurement(0.0, 1.0));
		meas.put(alts.get(1), new GaussianMeasurement(1.0, 0.0));
		return meas;
	}
	
	@Test
	public void testGetTypeLabel() {
		assertEquals("Gaussian", crit.getTypeLabel());
	}
	
	@Test
	public void testGetScale() {
		crit.setAlternatives(alts);
		crit.setMeasurements(generateMeas2Alts());
		assertEquals(new Interval(-1.96, 1.96), crit.getScale());
	}
	
	@Test
	public void testGetNullScale() {
		Interval scale = crit.getScale();
		assertEquals(new Interval(0.0, 0.0), scale);
	}
	

	@Test
	public void testSetMeasurementsFiresScaleChange() {		
		crit.setAlternatives(alts);
		crit.setMeasurements(generateMeas2Alts());
		Interval expVal = crit.getScale();
		PropertyChangeListener mock = JUnitUtil.mockListener(crit, CardinalCriterion.PROPERTY_SCALE, null, expVal);
		crit.addPropertyChangeListener(mock);
		crit.setMeasurements(generateMeas2Alts());
		verify(mock);
	}
		
}