public class zad2 {
    public static void main(String[] args) {
        int n = 153; // przykładowa liczba, możesz zmienić na dowolną
        System.out.println(isArmstrong(n));
    }

    public static boolean isArmstrong(int n) {
        int temp = n;
        int digits = 0;
        while (temp > 0) {
            digits++;
            temp /= 10;
        }
        temp = n;
        int sum = 0;
        while (temp > 0) {
            int digit = temp % 10;
            sum += Math.pow(digit, digits);
            temp /= 10;
        }
        return sum == n;
    }
}
