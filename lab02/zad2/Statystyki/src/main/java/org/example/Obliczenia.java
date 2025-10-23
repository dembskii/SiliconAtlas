package org.example;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Obliczenia {
    public static void obliczStatystyki(double[] dane) {
        DescriptiveStatistics stats = new DescriptiveStatistics();

        for (double wartosc : dane) {
            stats.addValue(wartosc);
        }

        double srednia = stats.getMean();
        double odchylenie = stats.getStandardDeviation();
        double wariancja = stats.getVariance();

        System.out.println("--- Wyniki Obliczeń Statystycznych ---");
        System.out.println("Średnia arytmetyczna: " + srednia);
        System.out.println("Odchylenie standardowe: " + odchylenie);
        System.out.println("Wariancja: " + wariancja);
    }
}
