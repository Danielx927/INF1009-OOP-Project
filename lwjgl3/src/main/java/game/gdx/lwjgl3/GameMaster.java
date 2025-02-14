package game.gdx.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameMaster extends ApplicationAdapter{
	private IOManager IOmgr;
	
	@Override
	public void create()
	{
		IOmgr = new IOManager();
	}
	
	@Override
	public void render()
	{
		ScreenUtils.clear(0, 0, 0.2f, 1);
		System.out.println("Hello srinithi!!");
		
		IOmgr.update();
	}
}
