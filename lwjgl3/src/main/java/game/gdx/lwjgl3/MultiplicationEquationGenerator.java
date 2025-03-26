package game.gdx.lwjgl3;

public class MultiplicationEquationGenerator extends MathEquationGenerator{

	protected MultiplicationEquationGenerator() {
		super("Multiplication");
	}
	
	@Override
	protected int calculateResult() {
		return operand1 * operand2;
	}
}
