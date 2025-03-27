package game.gdx.lwjgl3.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.gdx.lwjgl3.Entity;

public class SpriteAnimation {
	private Animation<TextureRegion> anim;
	private Texture sheet;
	private int emptyFramesOffset;
	private float stateTime;
	
	public SpriteAnimation(Animation<TextureRegion> anim,  int offset) {
		this.emptyFramesOffset = offset;
		this.anim = anim;
	}
	
	public SpriteAnimation(FileHandle fileHandle, int cols, int rows, int offset, float frameInterval) {
		sheet = new Texture(fileHandle);
		emptyFramesOffset = offset + 1;
		
		TextureRegion[][] tmp = TextureRegion.split(sheet,
				sheet.getWidth() / cols,
				sheet.getHeight() / rows);
		
		TextureRegion[] frames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		
		anim = new Animation<TextureRegion>(frameInterval, frames); 
		stateTime = 0f;
		
	}
	
	public Animation<TextureRegion> getAnim() {
		return anim;
	}
	
	public int getOffset() {
		return emptyFramesOffset;
	}
	
	public boolean isAnimationFinished() {
//		System.out.print("\n");
//		System.out.print("Keyframe Index: " + anim.getKeyFrameIndex(stateTime));
//		System.out.print("\n");
//		System.out.print("Keyframe Length:" + anim.getKeyFrames().length);
		if (anim.getKeyFrameIndex(stateTime) == (anim.getKeyFrames().length - emptyFramesOffset)) {
			return true;
		}
		return false;
	}
	
	public void reset() {
	    stateTime = 0;
	}
	
	public void render(SpriteBatch b, Entity e, boolean looping) {
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		
		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = anim.getKeyFrame(stateTime, looping);
//		System.out.println("Frame Width: " + currentFrame.getRegionWidth() + " | Expected: " + e.getWidth());
//		System.out.println("Frame Height: " + currentFrame.getRegionHeight() + " | Expected: " + e.getHeight());
//		
		b.draw(currentFrame, e.getX(), e.getY(), e.getWidth(), e.getHeight());
	}


	
	public void dispose() {
		if (this.sheet != null) sheet.dispose();
	}
		
}
