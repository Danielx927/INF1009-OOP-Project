package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnimation {
	private static final float FRAME_INTERVAL = 0.05f;
	private Animation<TextureRegion> anim;
	private Texture sheet;
	private SpriteBatch spr;
	private int emptyFramesOffset;
	
	float stateTime;
	
	public SpriteAnimation(Animation<TextureRegion> anim,  int offset) {
		this.emptyFramesOffset = offset;
		this.anim = anim;
	}
	
	public SpriteAnimation(FileHandle fileHandle, int cols, int rows, int offset) {
		spr = new SpriteBatch();
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
		
		anim = new Animation<TextureRegion>(FRAME_INTERVAL, frames); 
		stateTime = 0f;
		
	}
	
	public Animation<TextureRegion> getAnim() {
		return anim;
	}
	
	public int getOffset() {
		return emptyFramesOffset;
	}
	
	public void render(SpriteBatch b, Entity e, boolean looping) {
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		
		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = anim.getKeyFrame(stateTime, looping);
//		b.draw(currentFrame, e.getX() - e.getWidth()/2, e.getY() - e.getHeight()/2);
		b.draw(currentFrame, e.getX(), e.getY(), e.getWidth(), e.getHeight());

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

	
	public void dispose() {
		spr.dispose();
		sheet.dispose();
	}
		
}
