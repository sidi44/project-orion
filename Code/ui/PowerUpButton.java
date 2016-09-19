package ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import logic.powerup.PowerUp;
import logic.powerup.PowerUpFreeze;
import logic.powerup.PowerUpMagnet;
import logic.powerup.PowerUpSlowDown;
import logic.powerup.PowerUpSpeedUp;
import logic.powerup.PowerUpTeleport;
import logic.powerup.PowerUpVisitor;

public class PowerUpButton extends ImageButton implements PowerUpVisitor {

	// The UI skin
	private Skin skin;
	
	// Image button styles
	private final String STYLE_SPEEDUP = "powerup_speedup";
	private final String STYLE_SLOWDOWN = "powerup_slowdown";
	private final String STYLE_TELEPORT = "powerup_teleport";
	private final String STYLE_MAGNET = "powerup_magnet";
	private final String STYLE_FREEZE = "powerup_freeze";
	private final String STYLE_DEFAULT = "default";
	
	public PowerUpButton(Skin skin) {
		super(skin);
		this.skin = skin;
	}
	
	public void setPowerUp(PowerUp powerUp) {
		if (powerUp != null) {
			powerUp.accept(this);
		} else {
			removePowerUp();
		}
	}
	
	public void removePowerUp() {
		setImageStyle(STYLE_DEFAULT);
	}

	@Override
	public void visit(PowerUpSpeedUp powerUp) {
		setImageStyle(STYLE_SPEEDUP);
	}

	@Override
	public void visit(PowerUpSlowDown powerUp) {
		setImageStyle(STYLE_SLOWDOWN);
	}

	@Override
	public void visit(PowerUpFreeze powerUp) {
		setImageStyle(STYLE_FREEZE);
	}

	@Override
	public void visit(PowerUpMagnet powerUp) {
		setImageStyle(STYLE_MAGNET);
	}

	@Override
	public void visit(PowerUpTeleport powerUp) {
		setImageStyle(STYLE_TELEPORT);
	}

	private void setImageStyle(String imageStyle) {
		ImageButtonStyle current = getStyle();
		ImageButtonStyle style = skin.get(imageStyle, ImageButtonStyle.class);
		if (style != null && style != current) {
			setStyle(style);
		}
	}
	
}
