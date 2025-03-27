package game.gdx.lwjgl3.equation;

public class DivisionEquationGenerator extends MathEquationGenerator {

	protected DivisionEquationGenerator() {
		super("Division");
	}

	@Override
	protected int calculateResult() {
		return operand1 / operand2;
	}
	
	@Override
	protected void generateOperands() {
		operand2 = random.nextInt(9) + 1;
		operand1 = operand2 * (random.nextInt(10) + 1);
	}
}
