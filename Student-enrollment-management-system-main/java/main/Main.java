package main;

import landingPageGui.LogInPage;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new LogInPage().setVisible(true);
        });
    }
}
