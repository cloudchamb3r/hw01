package test.frame;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

public class PlaceHolderFocusListener implements FocusListener {
    private final JTextComponent textField;
    private final String palceholder;

    public PlaceHolderFocusListener(JTextComponent textField, String placeholder) {
        this.textField = textField;
        this.palceholder = placeholder;
    }

    @Override
    public void focusGained(FocusEvent e) {
        textField.setText("");
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (textField.getText().isEmpty()) {
            textField.setText(palceholder);
        }
    }

}
