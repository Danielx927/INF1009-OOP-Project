package game.gdx.lwjgl3.equation;

public class AdditionEquationGenerator extends MathEquationGenerator{

	protected AdditionEquationGenerator() {
		super("Addition");
	}

	@Override
	protected int calculateResult() {
		return operand1 + operand2;
	}
}
