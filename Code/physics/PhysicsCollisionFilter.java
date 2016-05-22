package physics;

class PhysicsCollisionFilter {

	// Defines the different physics body categories. These are used for 
	// collision filtering.
	private final static short CATEGORY_WALL = 0x0001;
	private final static short CATEGORY_PREDATOR = 0x0002;
	private final static short CATEGORY_PREY = 0x0004;
	private final static short CATEGORY_PILL = 0x0008;
	private final static short CATEGORY_POWERUP_PREDATOR = 0x0016;
	private final static short CATEGORY_POWERUP_PREY = 0x0032;
	private final static short CATEGORY_DEBUG = 0x0064;
	
	// Masks are used in collision filtering. Define which physics bodies 
	// collide. E.g. Predators will collide with walls and prey, but not other 
	// predators or pills.
	private final static short MASK_WALL = CATEGORY_PREDATOR | CATEGORY_PREY | 
			CATEGORY_PILL | CATEGORY_POWERUP_PREDATOR | CATEGORY_POWERUP_PREY |
			CATEGORY_DEBUG;
	private final static short MASK_PREDATOR = CATEGORY_WALL | CATEGORY_PREY | 
			CATEGORY_POWERUP_PREDATOR;
	private final static short MASK_PREY = CATEGORY_WALL | CATEGORY_PILL | 
			CATEGORY_PREDATOR | CATEGORY_POWERUP_PREY;
	private final static short MASK_PILL = CATEGORY_WALL | CATEGORY_PREY;
	private final static short MASK_POWERUP_PREDATOR = CATEGORY_WALL | 
			CATEGORY_PREDATOR;
	private final static short MASK_POWERUP_PREY = CATEGORY_WALL | 
			CATEGORY_PREY;
	private final static short MASK_DEBUG = CATEGORY_WALL;
	
	public static short getCategory(PhysicsBodyType type) {
		
		switch (type) {
			case Debug:
				return CATEGORY_DEBUG;
			case Pill:
				return CATEGORY_PILL;
			case PowerUpPredator:
				return CATEGORY_POWERUP_PREDATOR;
			case PowerUpPrey:
				return CATEGORY_POWERUP_PREY;
			case Predator:
				return CATEGORY_PREDATOR;
			case Prey:
				return CATEGORY_PREY;
			case Walls:
				return CATEGORY_WALL;
			default:
				System.err.println("Unknown physics debug type.");
				return 0;
		}
		
	}
	
	public static short getMask(PhysicsBodyType type) {
		
		switch (type) {
			case Debug:
				return MASK_DEBUG;
			case Pill:
				return MASK_PILL;
			case PowerUpPredator:
				return MASK_POWERUP_PREDATOR;
			case PowerUpPrey:
				return MASK_POWERUP_PREY;
			case Predator:
				return MASK_PREDATOR;
			case Prey:
				return MASK_PREY;
			case Walls:
				return MASK_WALL;
			default:
				System.err.println("Unknown physics debug type.");
				return 0;
		}
		
	}
	
}
