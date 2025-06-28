package dev.saeedkhanloo;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Sort {

  public long[] nsToShortTime(long ns) {
    long seconds = ns / 1_000_000_000L;
    long millis = (ns % 1_000_000_000L) / 1_000_000L;
    long nanos = ns % 1_000_000L;
    return new long[]{seconds, millis, nanos};
  }

  public int[][] generateArrays(int[] sizes) {
    int[][] arrays = new int[sizes.length][];
    for (int i = 0; i < sizes.length; i++) {
      arrays[i] = new int[sizes[i]];
      for (int j = 0; j < sizes[i]; j++) {
        arrays[i][j] = ThreadLocalRandom.current().nextInt(1, 10000);
      }
    }
    return arrays;
  }

  public String runBenchmark(int[][] arrays) {
    StringBuilder output = new StringBuilder();
    output.append("\nLegend: seconds.milliseconds,nanoseconds ns\n");

    for (int[] original : arrays) {
      output.append("\nSorting ").append(original.length).append("-sized array:\n");
      StringBuilder sb = new StringBuilder();

      long start, end;
      long[] result;

      start = System.nanoTime();
      maxSelectionSort(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Maximum Selection - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      start = System.nanoTime();
      minSelectionSort(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Minimum Selection - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      start = System.nanoTime();
      bubbleSort(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Bubble - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      start = System.nanoTime();
      bubbleSortImproved(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Bubble Improved - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      start = System.nanoTime();
      insertionSortNaive(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Insertion Naive - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      start = System.nanoTime();
      insertionSort(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Insertion - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      start = System.nanoTime();
      mergeSort(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Merge - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      start = System.nanoTime();
      quickSort(Arrays.copyOf(original, original.length));
      end = System.nanoTime();
      result = nsToShortTime(end - start);
      sb.append(String.format("Quick - %d.%03d,%06d ns\n", result[0], result[1], result[2]));

      output.append(sb.toString());
    }

    return output.toString();
  }

  public void maxSelectionSort(int[] list) {
    for (int i = list.length - 1; i >= 0; i--) {
      int maxindex = 0;
      for (int j = 0; j <= i; j++) {
        if (list[j] > list[maxindex]) maxindex = j;
      }
      int c = list[i];
      list[i] = list[maxindex];
      list[maxindex] = c;
    }
  }

  public void minSelectionSort(int[] list) {
    for (int i = 0; i <= list.length - 1; i++) {
      int minindex = i;
      for (int j = i + 1; j <= list.length - 1; j++) {
        if (list[j] < list[minindex]) minindex = j;
      }
      int c = list[i];
      list[i] = list[minindex];
      list[minindex] = c;
    }
  }

  public void bubbleSort(int[] list) {
    for (int i = list.length - 1; i >= 0; i--) {
      for (int j = 0; j <= i - 1; j++) {
        if (list[j] > list[j + 1]) {
          int s = list[j + 1];
          list[j + 1] = list[j];
          list[j] = s;
        }
      }
    }
  }

  public void bubbleSortImproved(int[] list) {
    int z = list.length - 1;
    while (z >= 1) {
      int u = -1;
      for (int j = 0; j <= z - 1; j++) {
        if (list[j] > list[j + 1]) {
          int s = list[j + 1];
          list[j + 1] = list[j];
          list[j] = s;
          u = j;
        }
      }
      z = u;
    }
  }

  public void insertionSortNaive(int[] list) {
    for (int i = 1; i <= list.length - 1; i++) {
      int j = i;
      while (j > 0 && list[j - 1] > list[j]) {
        int s = list[j];
        list[j] = list[j - 1];
        list[j - 1] = s;
        j = j - 1;
      }
    }
  }

  public void insertionSort(int[] list) {
    for (int i = 1; i <= list.length - 1; i++) {
      if (list[i - 1] > list[i]) {
        int m = list[i];
        list[i] = list[i - 1];
        int j = i - 2;
        while (j >= 0 && list[j] > m) {
          list[j + 1] = list[j];
          j = j - 1;
        }
        list[j + 1] = m;
      }
    }
  }

  public void mergeSort(int[] list) {
    int[] b = Arrays.copyOf(list, list.length);
    ms(b, list, 0, list.length);
  }

  private void merge(int[] b, int[] a, int u, int m, int v) {
    int k = u, i = u, j = m;
    while (i < m && j < v) {
      if (b[i] <= b[j]) a[k++] = b[i++];
      else a[k++] = b[j++];
    }
    while (i < m) a[k++] = b[i++];
    while (j < v) a[k++] = b[j++];
    for (k = u; k < v; k++) b[k] = a[k];
  }

  private void ms(int[] a, int[] b, int u, int v) {
    if ((v - u) > 1) {
      int m = (u + v) / 2;
      ms(a, b, u, m);
      ms(a, b, m, v);
      merge(b, a, u, m, v);
    }
  }

  public void quickSort(int[] list) {
    quick(list, 0, list.length - 1);
  }

  private void quick(int[] A, int f, int l) {
    if (f < l) {
      int q = partition(A, f, l);
      quick(A, f, q - 1);
      quick(A, q + 1, l);
    }
  }

  private int partition(int[] A, int f, int l) {
    int i = ThreadLocalRandom.current().nextInt((l - f) + 1) + f;
    int s = A[l];
    A[l] = A[i];
    A[i] = s;

    i = f;
    while (i < l && A[i] <= A[l]) i++;
    if (i < l) {
      int j = i + 1;
      while (j < l) {
        if (A[j] < A[l]) {
          int v = A[j];
          A[j] = A[i];
          A[i] = v;
          i++;
        }
        j++;
      }
      int z = A[l];
      A[l] = A[i];
      A[i] = z;
    }
    return i;
  }
}
