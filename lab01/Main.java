import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Wybierz zadanie:");
            System.out.println("1) Greeting");
            System.out.println("2) Armstrong check");
            System.out.println("3) Planet age");
            System.out.println("4) Patterns");
            System.out.println("5) Book demo");
            System.out.println("0) Wyjscie");
            System.out.print("Twój wybór: ");
            int choice = -1;
            if (scanner.hasNextInt()) choice = scanner.nextInt(); else scanner.next();
            switch (choice) {
                case 0:
                    System.out.println("Koniec.");
                    return;
                case 1:
                    System.out.println(Greeting.getGreeting());
                    break;
                case 2:
                    System.out.print("Podaj liczbę do sprawdzenia: ");
                    int n = scanner.hasNextInt() ? scanner.nextInt() : 0;
                    System.out.println(Armstrong.isArmstrong(n));
                    break;
                case 3:
                    System.out.print("Podaj sekundy: ");
                    double s = scanner.hasNextDouble() ? scanner.nextDouble() : 0;
                    System.out.print("Podaj nazwę planety: ");
                    String p = scanner.hasNext() ? scanner.next() : "ziemia";
                    try {
                        PlanetAge.Planet planet = PlanetAge.Planet.fromString(p);
                        double wiek = PlanetAge.obliczWiekNaPlanecie(s, planet);
                        System.out.printf("Wiek na %s: %.2f lat\n", p, wiek);
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Podaj n dla wzorów: ");
                    int m = scanner.hasNextInt() ? scanner.nextInt() : 0;
                    System.out.print(Patterns.generatePatterns(m));
                    break;
                case 5:
                    System.out.print(BookDemo.runDemo());
                    break;
                default:
                    System.out.println("Nieprawidłowy wybór.");
            }
            System.out.println();
        }
    }
}
