package game.gdx.lwjgl3;

import java.util.Random;


public class MathEquationGenerator {
	private static int integer2;
	private static final Random rng = new Random();
	private static final char[] OPERATORS = {'+', '-', '*', '/'};
	
	public static void generateEquation(int scale) {
		int result = rng.nextInt(10 * scale) + 1;
		integer2 = rng.nextInt(10) + 1;
		char operator = OPERATORS[rng.nextInt(OPERATORS.length)];
		int integer1 = calculateInt1(result, operator);
		System.out.println(integer1 + " " + operator + " " + integer2 + " = " + result);
	}
	
	public static void setInteger2(int i) {
		integer2 = i;
	}
	
	public static boolean isPrime(int num) {
		for (int i = 2; i * i <= num; i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	private static int calculateInt1(int result, char operator) {
		switch (operator) {
		case '+' : return result - integer2;
		case '-' : return result + integer2;
		case '*' : 
			if (isPrime(result)) {
				setInteger2(1);
				return result;
			}
			
			if (result % integer2 != 0) {
				setInteger2(integer2 - (result % integer2));
			} return result / integer2;
		case '/' : return result * integer2;
		}
		return 0;
	}
}
