package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.ScreenUtils;

public class IOManager {
	private Vector2 touchPos = new Vector2();
    private Sound hammerSound = Gdx.audio.newSound(Gdx.files.internal("hammerMiss.mp3"));
    
	public void update() {
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
    		System.out.println(touchPos);
    		hammerSound.play();
//            viewport.unproject(touchPos);
//            bucketSprite.setCenterX(touchPos.x);
        }
	}
}
