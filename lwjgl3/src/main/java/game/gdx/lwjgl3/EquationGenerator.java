package game.gdx.lwjgl3;

public interface EquationGenerator {
	String generateEquation();
	int getResult();
	int getOperand1();
	int getOperand2();
	String getOperator();
}
