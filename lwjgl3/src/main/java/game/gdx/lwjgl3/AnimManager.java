package game.gdx.lwjgl3;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;

public class AnimManager {
	private static AnimManager instance;
	private HashMap<String, HashMap<String, SpriteAnimation>> animHashMap;
	
	public AnimManager() {
		animHashMap = new HashMap<String, HashMap<String, SpriteAnimation>>();

    	this.populateAnimHashMap();
    }
    
    public static AnimManager getInstance() {
        if (instance == null) {
            instance = new AnimManager();
        }
        return instance;
    }
    
    private void populateAnimHashMap() {
    	HashMap<String, SpriteAnimation> mole_template = new HashMap<String, SpriteAnimation>();
    	SpriteAnimation mole_popup = new SpriteAnimation(Gdx.files.internal("spritesheets/mole_popup.png"), 3, 3, 2, 0.05f);
    	SpriteAnimation mole_die = new SpriteAnimation(Gdx.files.internal("spritesheets/mole_die.png"), 3, 3, 2, 0.05f);

    	HashMap<String, SpriteAnimation> hammer_template = new HashMap<String, SpriteAnimation>();
    	SpriteAnimation hammer_slam = new SpriteAnimation(Gdx.files.internal("spritesheets/hammer_slam.png"), 3, 2, 1, 0.07f);

    	
    	// Putting animations into their template
    	mole_template.put("mole_popup", mole_popup);
    	mole_template.put("mole_die", mole_die);
    	
    	hammer_template.put("hammer_slam", hammer_slam);

    	// Putting templates into the whole hashmap
    	animHashMap.put("mole_template", mole_template);
    	animHashMap.put("hammer_template", hammer_template);


    }
    
    public void dispose() {
    	for (String i : animHashMap.keySet()) {
    		for (String j : animHashMap.get(i).keySet())
    			animHashMap.get(i).get(j).dispose();
		}
    }
    
    public void useTemplate(Entity e, String templateKey) {
    	// Creating a deep copy of animations for each IO to use.
    	if (animHashMap.containsKey(templateKey)) {
    		HashMap<String, SpriteAnimation> copy = new HashMap<String, SpriteAnimation>();
    		for (String i : animHashMap.keySet()) {
    			if (i == templateKey)  {
    				for (String j: animHashMap.get(i).keySet()) {
    					copy.put(j,  new SpriteAnimation(animHashMap.get(i).get(j).getAnim(), animHashMap.get(i).get(j).getOffset()));
    				}
    				e.setAnims(copy);
    			}
			}
		}
	}
    
 
    

	
}
