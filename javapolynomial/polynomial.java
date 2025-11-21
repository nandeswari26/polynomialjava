import java.io.FileReader;
import java.math.BigInteger;
import java.util.Map;
import com.google.gson.Gson;

public class PolynomialSecret {
    
    public static void main(String[] args) throws Exception {

        Gson gson = new Gson();
        Map<String, Object> jsonMap = gson.fromJson(new FileReader("testcase2.json"), Map.class);

        Map<String, Double> keys = (Map<String, Double>) jsonMap.get("keys");
        int k = keys.get("k").intValue();

        // Store x and y values
        BigInteger[] X = new BigInteger[k];
        BigInteger[] Y = new BigInteger[k];

        int index = 0;
        for (int i = 1; i <= k; i++) {
            Map<String, String> obj = (Map<String, String>) jsonMap.get(String.valueOf(i));
            int base = Integer.parseInt(obj.get("base"));
            String value = obj.get("value");
            X[index] = BigInteger.valueOf(i);
            Y[index] = new BigInteger(value, base); // Base Conversion
            index++;
        }

        System.out.println("Constant C = " + lagrangeInterpolation(X, Y, k));
    }

    // Lagrange Interpolation to find f(0), the constant term
    public static BigInteger lagrangeInterpolation(BigInteger[] x, BigInteger[] y, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            // Numerator and Denominator as BigInteger
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    num = num.multiply(x[j].negate());
                    den = den.multiply(x[i].subtract(x[j]));
                }
            }

            // term = y[i] * (num/den)
            BigInteger term = y[i].multiply(num).divide(den);
            result = result.add(term);
        }
        return result;
    }
}
