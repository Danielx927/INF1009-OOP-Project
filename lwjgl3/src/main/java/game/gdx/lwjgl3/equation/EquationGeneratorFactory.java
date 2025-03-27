package game.gdx.lwjgl3.equation;

import java.util.Random;

public final class EquationGeneratorFactory {
	private static final Random random = new Random();
	
	private EquationGeneratorFactory() {}
	
	public static EquationGenerator randomGenerator() {
		int choice = random.nextInt(4);
		switch(choice) {
			case 0: return new AdditionEquationGenerator();
			case 1: return new SubtractionEquationGenerator();
			case 2: return new MultiplicationEquationGenerator();
			case 3: return new DivisionEquationGenerator();
			default: return new AdditionEquationGenerator();
		}
	}
}
