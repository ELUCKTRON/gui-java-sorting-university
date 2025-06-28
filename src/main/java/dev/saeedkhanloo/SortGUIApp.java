package dev.saeedkhanloo;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.BorderFactory;

public class SortGUIApp extends JFrame {

  private static final int[] PREDEFINED_SIZES = {1_000, 10_000, 100_000};
  private static final String[] ALGO_NAMES = {"Maximum Selection", "Minimum Selection", "Bubble",
      "Bubble Improved", "Insertion Naive", "Insertion", "Merge", "Quick"};
  private static final int COLS = 1 + ALGO_NAMES.length;
  private static final int ROWS = 1 + PREDEFINED_SIZES.length;

  private static final int CELL_WIDTH = 150;
  private static final int CELL_HEIGHT = 28;

  private final Sort sorter = new Sort();
  private final JButton runButton;
  private final JPanel resultsPanel;

  private final JLabel[][] cellLabels = new JLabel[ROWS][COLS];

  public SortGUIApp() {
    setTitle("Sorting Benchmark GUI");
    setSize(1400, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    var topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    runButton = new JButton("Run Benchmark");
    topPanel.add(runButton);
    add(topPanel, BorderLayout.NORTH);

    resultsPanel = new JPanel(new GridLayout(ROWS, COLS));
    resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    var scrollPane = new JScrollPane(resultsPanel);
    add(scrollPane, BorderLayout.CENTER);

    addCell(0, 0, "Array Size", true, true);
    for (int c = 0; c < ALGO_NAMES.length; c++) {
      addCell(0, c + 1, ALGO_NAMES[c], true, c < ALGO_NAMES.length - 1);
    }

    for (int r = 0; r < PREDEFINED_SIZES.length; r++) {
      addCell(r + 1, 0, String.format("%,d", PREDEFINED_SIZES[r]), true, true);
      for (int c = 0; c < ALGO_NAMES.length; c++) {
        addCell(r + 1, c + 1, "", false, c < ALGO_NAMES.length - 1);
      }
    }

    runButton.addActionListener(this::onRunBenchmark);
    setVisible(true);
  }

  private void addCell(int row, int col, String text, boolean isHeader, boolean addRightBorder) {
    var label = new JLabel(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.CENTER);
    label.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
    label.setOpaque(true);

    if (isHeader) {
      label.setFont(label.getFont().deriveFont(Font.BOLD));
      label.setBackground(new Color(230, 230, 240));
    } else {
      label.setBackground(Color.WHITE);
    }

    int top = 0, left = 0, bottom = 1, right = addRightBorder ? 1 : 0;
    label.setBorder(new MatteBorder(top, left, bottom, right, Color.LIGHT_GRAY));
    resultsPanel.add(label);
    cellLabels[row][col] = label;
  }

  private void onRunBenchmark(ActionEvent e) {
    runButton.setEnabled(false);

    for (int r = 1; r < ROWS; r++) {
      for (int c = 1; c < COLS; c++) {
        cellLabels[r][c].setText("");
      }
    }

    // Run benchmarks sequentially in a SwingWorker
    new SwingWorker<Void, RowResult>() {
      @Override
      protected Void doInBackground() {
        for (int r = 0; r < PREDEFINED_SIZES.length; r++) {
          int size = PREDEFINED_SIZES[r];
          int[][] arrays = sorter.generateArrays(new int[] {size});
          String result = sorter.runBenchmark(arrays);
          String[] algoResults = parseAlgoResults(result);
          publish(new RowResult(r + 1, algoResults));
        }
        return null;
      }

      @Override
      protected void process(List<RowResult> results) {
        for (RowResult rr : results) {
          for (int c = 0; c < ALGO_NAMES.length; c++) {
            cellLabels[rr.rowIndex][c + 1]
                .setText(rr.algoResults[c] != null ? rr.algoResults[c] : "");
          }
        }
      }

      @Override
      protected void done() {
        runButton.setEnabled(true);
      }
    }.execute();
  }

  private String[] parseAlgoResults(String result) {
    String[] lines = result.split("\\n");
    String[] algoResults = new String[ALGO_NAMES.length];
    for (String line : lines) {
      for (int i = 0; i < ALGO_NAMES.length; i++) {
        if (line.startsWith(ALGO_NAMES[i])) {
          // Extract only the time part after the dash
          int dashIdx = line.indexOf('-');
          if (dashIdx != -1 && dashIdx + 1 < line.length()) {
            algoResults[i] = line.substring(dashIdx + 1).trim();
          }
        }
      }
    }
    return algoResults;
  }

  private static class RowResult {
    final int rowIndex;
    final String[] algoResults;

    RowResult(int rowIndex, String[] algoResults) {
      this.rowIndex = rowIndex;
      this.algoResults = algoResults;
    }
  }

  public static void launchApp() {
    SwingUtilities.invokeLater(SortGUIApp::new);
  }
}
