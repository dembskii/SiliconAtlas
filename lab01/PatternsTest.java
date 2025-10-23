public class PatternsTest {

    public static void main(String[] args) {

        testGeneratePatternsFor3();

    }

    private static void testGeneratePatternsFor3() {
        String testName = "testGeneratePatternsFor3";
            String result = Patterns.generatePatterns(3);
            String expected = 
                "x\n" +
                "xx\n" +
                "xxx\n" +
                "xxx\n" +
                "xx\n" +
                "x\n" +
                "xxx\n" +
                " xx\n" +
                "  x\n" +
                "  x\n" +
                " xx\n" +
                "xxx\n";
            
            if (result.equals(expected)) {
                System.out.println("✓ " + testName + " - PASSED");
                
            } else {
                System.out.println("✗ " + testName + " - FAILED");
            }
        }
    }

