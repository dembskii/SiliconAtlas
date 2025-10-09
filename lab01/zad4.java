import java.util.Scanner;

public class zad4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = 0;
        while (true) {
            System.out.print("Podaj liczbę naturalną n: ");
            if (scanner.hasNextInt()) {
                n = scanner.nextInt();
                if (n > 0) break;
            } else {
                scanner.next(); // ignoruj niepoprawne dane
            }
            System.out.println("Niepoprawne dane. Spróbuj ponownie.");
        }
        // Wzór 1
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) System.out.print("x");
            System.out.println();
        }
        // Wzór 2
        for (int i = n; i >= 1; i--) {
            for (int j = 0; j < i; j++) System.out.print("x");
            System.out.println();
        }
        // Wzór 3
        for (int i = n; i >= 1; i--) {
            for (int j = 0; j < n - i; j++) System.out.print(" ");
            for (int j = 0; j < i; j++) System.out.print("x");
            System.out.println();
        }
        // Wzór 4
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n - i; j++) System.out.print(" ");
            for (int j = 0; j < i; j++) System.out.print("x");
            System.out.println();
        }
    }
}
