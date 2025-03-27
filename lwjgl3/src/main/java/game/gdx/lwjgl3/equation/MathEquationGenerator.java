package game.gdx.lwjgl3.equation;

import java.util.Random;

public abstract class MathEquationGenerator implements EquationGenerator{
	protected final Random random = new Random();
	protected int operand1;
	protected int operand2;
	protected final String equationType;
	
	protected abstract int calculateResult();
	
	protected MathEquationGenerator(String equationType) {
		this.equationType = equationType;
	}
	
	@Override
	public String generateEquation() {
		generateOperands();
		return operand1 + " " + getOperator() + " " + operand2 + " = ?";
	}
	
	
	@Override
	public int getResult() {
		int result = calculateResult();
		return result;
	}
	
	@Override
	public int getOperand1() {
		return operand1;
	}
	
	@Override
	public int getOperand2() {
		return operand2;
	}
	
	@Override
	public String getOperator() {
		switch(equationType.toLowerCase()) {
		case "addition": return "+";
		case "subtraction": return "-";
		case "multiplication": return "x";
		case "division": return "/";
		default: return "";
	}
	}
	
	protected void generateOperands() {
		operand1 = random.nextInt(10) + 1;
		operand2 = random.nextInt(10) + 1;
 	}
	
}
