public class GCD {
    public static void main (String[] args) {
        gcd(4, 7); //1
        gcd(4, 12); //4
        gcd(34, 51); //17
    }
    public static int gcd(int a, int b) {
        if(a<0 || b<0) {
            return gcd(Math.abs(a), Math.abs(b));
        }
        if(b == 0) return a;
        return gcd(b, a%b);
    }
}