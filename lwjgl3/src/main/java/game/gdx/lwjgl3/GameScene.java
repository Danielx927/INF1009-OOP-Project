package game.gdx.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class GameScene extends Scene{
	private Label pointsLabel;
	private int score;
	
	public GameScene() {
		super("Main Game");
		this.score = 0;
	}
	
	@Override
	public void load() {
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont();
		pointsLabel = new Label(String.valueOf(score), labelStyle); //Convert String score to int
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub
		if (pointsLabel.getStyle().font != null) {
			pointsLabel.getStyle().font.dispose();
		}
	}
	public void increaseScore() {
		score++;
		pointsLabel.setText(String.valueOf(score));
	}
	
	public int getScore() {
		return score;
	}
}
