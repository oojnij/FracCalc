//Leah Yi
import java.util.*;
public class FracCalc {
    //This is the main statement that appears all the time
    //It allows the header "Enter: " to appear
    //It also allows us to quit the program
    //The code includes salutations when you give up on it
    public static void main (String[] args) {
        Scanner console = new Scanner(System.in);
        String quit = "quit";
        System.out.print("Enter: ");
        String input = console.nextLine();
        while(!input.equals(quit)) {
            String test = processCommand(input);
            System.out.println(test);
            System.out.print("Enter: ");
            input = console.nextLine();
        }
        System.out.print("See you later!");
    }
    //frac op frac
    public static String processExpression(String input) {
        String frac1 = input.substring(0, input.indexOf(" "));
        String op = input.substring(input.indexOf(" ") + 1, input.indexOf(" ") + 2);
        String frac2 = input.substring(input.indexOf(" ") + 3);
        return calculateFraction(frac1, frac2, op);
    }
    //performs calculations
    public static String calculateFraction(String frac1, String frac2, String op) {
        String frac1vals = processFraction(frac1);
        String frac2vals = processFraction(frac2);
        int intA1 = Integer.parseInt(frac1vals.substring(frac1vals.indexOf("e") + 1, frac1vals.indexOf("N")));
        int intB1 = Integer.parseInt(frac1vals.substring(frac1vals.indexOf("m") + 1, frac1vals.indexOf("D")));
        int intC1 = Integer.parseInt(frac1vals.substring(frac1vals.indexOf("n") + 1));
        if(frac1.equals("0")) {
            intB1 = 0;
        }
        int intA2 = Integer.parseInt(frac2vals.substring(frac2vals.indexOf("e") + 1, frac2vals.indexOf("N")));
        int intB2 = Integer.parseInt(frac2vals.substring(frac2vals.indexOf("m") + 1, frac2vals.indexOf("D")));
        int intC2 = Integer.parseInt(frac2vals.substring(frac2vals.indexOf("n") + 1));
        if(frac2.equals("0")) {
            intB2 = 0;
        }
        if(frac1.contains("_")) {
            if(intA1 < 0) {
                intB1 = (intA1 * intC1) - intB1;
            } else if (intA1 > 0) {
                intB1 = (intA1 * intC1) + intB1;
            }
        }
        if(frac2.contains("_")) {
            if(intA2 < 0) {
                intB2 = (intA2 * intC2) - intB2;
            } else if (intA2 > 0) {
                intB2 = (intA2 * intC2) + intB2;
            }
        }
        if(intB1 == 1 && intC1 == 1) {
            intB1 = intA1;
        }
        if(intB2 == 1 && intC2 == 1) {
            intB2 = intA2;
        }
        String answer = "";
        int newB = 0;
        int newC = 0;
        if(op.equals("/")) {
            newB = intB1 * intC2;
            newC = intC1 * intB2;
            if(newB < 0 && newC < 0) {
                newB = Math.abs(newB);
                newC = Math.abs(newC);
            }
        } else if (op.equals("*")) {
            newB = intB1 * intB2;
            newC = intC1 * intC2;
            if(newB < 0 && newC < 0) {
                newB = Math.abs(newB);
                newC = Math.abs(newC);
            }
        } else if(op.equals("+") || op.equals("-")) {
            intB2 = intB2 * intC1;
            intC1 = intC1 * intC2;
            intB1 = intB1 * intC2;
            intC2 = intC1;
            newC = intC2;
            if(op.equals("+")) {
                newB = intB1 + intB2;
            } else if(op.equals("-")) {
                newB = intB1 - intB2;
            }
        }
        int gcd = gcd(newB, newC);
        if(gcd!=1) {
            newB = newB/gcd;
            newC = newC/gcd;
        }
        if(newB == newC) {
            answer = 1 + "";
        } else if(newC == 1) {
            answer = newB + "";
        } else {
            answer = newB + "/" + newC;
            if(Math.abs(newB) > newC) {
                answer = sad(newB, newC);
            }
        }
        return answer;
    }
    //Helper methods for calculation
    public static int gcd(int g, int h) {
        if(g < 0 || h < 0) {
            return gcd(Math.abs(g), Math.abs(h));
        }
        if(h == 0) return g;
        return gcd(h, g%h);
    }
    public static String sad(int b, int c) {
        int a = b/c;
        b = Math.abs(b) % c;
        return a+"_"+b+"/"+c;
    }
    //returns #/# proper or impropr fractions
    public static String processFraction(String frac2) {
        String a = "";
        String b = "";
        String c = "";
        if(frac2.contains("_")) {
            a = frac2.substring(0, frac2.indexOf("_"));;
            frac2 = frac2.substring(frac2.indexOf("_") + 1);
            b = frac2.substring(0, frac2.indexOf("/"));
            c = frac2.substring(frac2.indexOf("/") + 1);
        } else if (frac2.contains("/")) {
            a = "0";
            b = frac2.substring(0, frac2.indexOf("/"));
            c = frac2.substring(frac2.indexOf("/") +1);
        } else {
            a = frac2;
            b = "1";
            c = "1";
        }
        if((b.contains("-"))&&(c.contains("-"))) {
            b = b.replace("-", "");
            c = c.replace("-", "");
        } else if((c.contains("-"))&&!(b.contains("-"))) {
            b = "-" + b;
            c = c.replace("-", "");
        }
        return "Whole" + a + "Num" + b+ "Den" + c;
    }
    //prints the help statement
    public static String processCommand(String input) {
        String help = "help";
        if(!UnitTestRunner.processCommand(input)) {
            if(input.equals(help)) {
                return help();
            } return processExpression(input);
        } else {
            return "";
        }
    }
    //This prints a helpful statement
    public static String help() {
        return ("Helpful text: This program can now correctly operate fractions \nEnter \"quit\" to end the program \nYou can now operate any function: addition \nsubtraction, multiplication, and division \nto any whole number, fraction or mixed number \nThis will also simplify the answer immediately");
    }
}