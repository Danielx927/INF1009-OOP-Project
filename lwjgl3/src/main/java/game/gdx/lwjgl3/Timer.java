package game.gdx.lwjgl3;

public class Timer {
	private long startTime;
	private long elapsedTime;
	private boolean isRunning;
	
	public Timer() {
		reset();
	}
	
	public void start() {
		if (!isRunning) {
			startTime = System.currentTimeMillis();
			isRunning = true;
		}
	}
	
	public void stop() {
		if (isRunning) {
			elapsedTime += System.currentTimeMillis() - startTime;
			isRunning = false;
		}
	}
	
	public void reset() {
		startTime = 0;
		elapsedTime = 0;
		isRunning = false;
	}
	
	public long getElapsedTime() {
		if (isRunning) {
			return elapsedTime + (System.currentTimeMillis() - startTime);
		}
		return elapsedTime;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
}
