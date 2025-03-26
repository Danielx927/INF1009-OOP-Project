package game.gdx.lwjgl3;

public class Hammer extends Tool {

	public Hammer(String t, float x, float y) {
		super(t, x, y);
	}
	
	@Override
	public void clickEvent() {
        this.setCurrentAnim("hammer_slam");
        IOManager.getInstance().playSound("hammerSmash", 20f);
    }

}
