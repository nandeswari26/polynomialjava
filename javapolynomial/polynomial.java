import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;

public class PolynomialSecret {

    static class Point {
        int x;
        BigInteger y;
        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder input = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            input.append(line);
        }

        String json = input.toString().replaceAll("\\s+", "");

        int k = extract(json, "\"k\":(\\d+)");
        int highestIndex = findMaxIndex(json);

        List<Point> points = parsePoints(json);

        BigInteger result = compute(points, k, highestIndex);

        System.out.println(result);
    }

    static int extract(String text, String regex) {
        Matcher m = Pattern.compile(regex).matcher(text);
        if(m.find()) return Integer.parseInt(m.group(1));
        return -1;
    }

    static int findMaxIndex(String json) {
        Matcher m = Pattern.compile("\"(\\d+)\"\\:").matcher(json);
        int max = 0;
        while(m.find()) max = Math.max(max, Integer.parseInt(m.group(1)));
        return max;
    }

    static List<Point> parsePoints(String json) {
        List<Point> list = new ArrayList<>();
        Matcher m = Pattern.compile("\"(\\d+)\":\\{\"base\":\"(\\d+)\",\"value\":\"([0-9A-Za-z]+)\"")
                .matcher(json);

        while(m.find()) {
            int x = Integer.parseInt(m.group(1));
            int base = Integer.parseInt(m.group(2));
            String value = m.group(3);
            BigInteger y = new BigInteger(value, base);
            list.add(new Point(x,y));
        }

        list.sort(Comparator.comparingInt(a -> a.x));
        return list;
    }

    static BigInteger compute(List<Point> points, int k, int targetX) {
        List<Point> used = points.subList(0, k);
        BigInteger result = BigInteger.ZERO;

        for(int i = 0; i < used.size(); i++){
            BigInteger num = used.get(i).y;
            BigInteger den = BigInteger.ONE;

            for(int j = 0; j < used.size(); j++){
                if(i != j){
                    num = num.multiply(BigInteger.valueOf(targetX - used.get(j).x));
                    den = den.multiply(BigInteger.valueOf(used.get(i).x - used.get(j).x));
                }
            }
            result = result.add(num.divide(den));
        }
        return result;
    }
}
