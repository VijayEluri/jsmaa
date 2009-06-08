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

package fi.smaa.jsmaa.gui;

import javax.swing.JComponent;

import nl.rug.escher.common.gui.LayoutUtil;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.common.gui.IntervalFormat;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;

public class CriteriaListView implements ViewBuilder {
	
	private SMAAModel model;
	
	public CriteriaListView(SMAAModel model) {
		this.model = model;
	}

	@SuppressWarnings("unchecked")
	public JComponent buildPanel() {
		
		FormLayout layout = new FormLayout(
				"pref, 3dlu, pref, 3dlu, center:pref, 3dlu, pref",
				"p, 3dlu, p" );
		
		int fullWidth = 7;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Criteria", cc.xyw(1, 1, fullWidth));
		builder.addLabel("Name", cc.xy(1, 3));
		builder.addLabel("Type", cc.xy(3, 3));
		builder.addLabel("Scale", cc.xy(5, 3));
		builder.addLabel("Ascending", cc.xy(7, 3));

		int row = 5;

		for (Criterion c : model.getCriteria()) {
			LayoutUtil.addRow(layout);
			
			PresentationModel<Criterion> pm = new PresentationModel<Criterion>(c);
			builder.add(BasicComponentFactory.createLabel(
					pm.getModel(Criterion.PROPERTY_NAME)),
					cc.xy(1, row)
					);

			builder.add(BasicComponentFactory.createLabel(
					pm.getModel(Criterion.PROPERTY_TYPELABEL)),
					cc.xy(3, row)
					);
			if (c instanceof CardinalCriterion) {
				CardinalCriterion cardCrit = (CardinalCriterion) c;
				PresentationModel<CardinalCriterion> cpm = new PresentationModel<CardinalCriterion>(cardCrit);
				builder.add(BasicComponentFactory.createLabel(
						cpm.getModel(CardinalCriterion.PROPERTY_SCALE),
						new IntervalFormat()),
						cc.xy(5, row)
						);
				builder.add(BasicComponentFactory.createCheckBox(
						cpm.getModel(CardinalCriterion.PROPERTY_ASCENDING), null),
						cc.xy(7, row)
						);
			}
	
			row += 2;
		}
		
		return builder.getPanel();
	}

}