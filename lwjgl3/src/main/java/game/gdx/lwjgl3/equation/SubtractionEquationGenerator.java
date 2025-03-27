package game.gdx.lwjgl3.equation;

public class SubtractionEquationGenerator extends MathEquationGenerator {

	protected SubtractionEquationGenerator() {
		super("Subtraction");
	}

	@Override
	protected int calculateResult() {
		return operand1 - operand2;
	}
}