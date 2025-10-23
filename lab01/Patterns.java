public class Patterns {
    public static String generatePatterns(int n) {
        StringBuilder sb = new StringBuilder();
        // Wzór 1
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) sb.append('x');
            sb.append('\n');
        }
        // Wzór 2
        for (int i = n; i >= 1; i--) {
            for (int j = 0; j < i; j++) sb.append('x');
            sb.append('\n');
        }
        // Wzór 3
        for (int i = n; i >= 1; i--) {
            for (int j = 0; j < n - i; j++) sb.append(' ');
            for (int j = 0; j < i; j++) sb.append('x');
            sb.append('\n');
        }
        // Wzór 4
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n - i; j++) sb.append(' ');
            for (int j = 0; j < i; j++) sb.append('x');
            sb.append('\n');
        }
        return sb.toString();
    }
}
