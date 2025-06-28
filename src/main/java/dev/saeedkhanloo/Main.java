package dev.saeedkhanloo;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
  public static void main(String[] args) throws UnsupportedLookAndFeelException,
      ClassNotFoundException, InstantiationException, IllegalAccessException {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    SortGUIApp.launchApp();
  }
}
