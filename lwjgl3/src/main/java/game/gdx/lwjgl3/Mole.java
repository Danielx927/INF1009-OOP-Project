package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mole extends InteractiveObject {
    private int points;
    public boolean wasHit = false;
    private int assignedAnswer;
    private boolean isCorrect;
    private NinePatch answerBox;
    private BitmapFont answerFont;

	public Mole(String t, float x, float y, float w, float h, int p, float d) {
		super(t, x, y, w, h, d);
        points = p;
        
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0.3f, 0.2f, 0.1f, 0.9f); // Brownish semi-transparent
        pix.fill();
        answerBox = new NinePatch(new Texture(pix), 1, 1, 1, 1);
        pix.dispose();

        answerFont = new BitmapFont(Gdx.files.internal("fonts/CharlemagneSTD_Size68.fnt"),
                Gdx.files.internal("fonts/CharlemagneSTD_Size68.png"), false);
        answerFont.getData().setScale(0.5f);
        answerFont.setColor(Color.WHITE);

        isCorrect = true;
	}
	
    public int getPoints() {
        return points;
    }

    public void setPoints(int p) {
        points = p;
    }
	
    public void setAnswerData(int answer, boolean isCorrect) {
        this.assignedAnswer = answer;
        this.isCorrect = isCorrect;
    }

    public int getAssignedAnswer() {
        return assignedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    
    @Override
    public void render(SpriteBatch b) {
        super.render(b);
        if (assignedAnswer != 0) {
            float boxX = getX() + getWidth() / 2 - 25;
            float boxY = getY() + getHeight() + 5;

            b.setColor(isCorrect ? Color.GREEN : Color.RED);
            answerBox.draw(b, boxX, boxY, 50, 30);
            b.setColor(Color.WHITE);

            String answer = String.valueOf(assignedAnswer);
            GlyphLayout layout = new GlyphLayout(answerFont, answer);
            answerFont.draw(b, answer,
                    boxX + 25 - layout.width / 2,
                    boxY + 20 + layout.height / 2);
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        answerFont.dispose(); // Dispose of the font to avoid memory leaks
    }
}
