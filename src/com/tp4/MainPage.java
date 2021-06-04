package com.tp4;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainPage {
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    private JLabel infixLabel;
    private JTextField infixField;
    private JLabel postfixLabel;
    private JLabel resultLabel;
    private JLabel errorLabel;
    private JLabel postfixText;
    private JLabel resultText;
    private JLabel errorText;

    private final Evaluator evaluator = new Evaluator();


    public MainPage() {
        infixField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                evaluateInfixField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                evaluateInfixField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                evaluateInfixField();
            }
        });
    }

    private void evaluateInfixField() {
        String infixExpression = infixField.getText();
        evaluator.setInfixExpression(infixExpression);

        postfixText.setText(evaluator.getPostfixExpression());
        resultText.setText(evaluator.getResult());
        errorText.setText(evaluator.getErrorList());
    }


}
