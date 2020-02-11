package ui;

import org.uqbar.arena.widgets.Label;
import org.uqbar.arena.widgets.Panel;
import org.uqbar.arena.widgets.TextBox;

public class LabeledTextBox {
	public LabeledTextBox(Panel form, String label, String propiedad) {
        new Label(form).setText(label);
        TextBox textBoxDesde = new TextBox(form);
        textBoxDesde.setWidth(200);
        textBoxDesde.bindValueToProperty(propiedad);
	}

}
