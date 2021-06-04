package com.tp4;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Infix -> Postfix Evaluator");
        MainPage mainPage = new MainPage();
        frame.setContentPane(mainPage.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
