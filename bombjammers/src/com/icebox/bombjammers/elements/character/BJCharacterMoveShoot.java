package com.icebox.bombjammers.elements.character;

import java.io.File;

import com.icebox.bombjammers.elements.bomb.BJBomb;
import com.icebox.bombjammers.loaders.BJAnimationLoader;
import com.icebox.bombjammers.loaders.BJPropertiesLoader;
import com.ragebox.bombframework.bwt.BTAnimatedImage;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class BJCharacterMoveShoot extends BJCharacterMoveState {
	
	private boolean reverse;
	
	private int shootingTime;
	
	private BTAnimatedImage animationShoot;
	
	private Sound shoot;
	private double volEffects = Double.valueOf(BJPropertiesLoader.getInstance().
			getProperty(BJPropertiesLoader.VOLUME_EFFECTS))/100;

	protected BJCharacterMoveShoot(BJCharacter character,
			BJCharacterMoveContext context, boolean reverse) {
		super(character, context);
		
		this.reverse = reverse;
		
		BJAnimationLoader loader = BJAnimationLoader.getInstance();
		
		animationShoot = loader.getAnimation("shoot",
				character.getFileRepertory()+"sprite_sheet.png");
		
		if (reverse) {
			animationShoot.horizontalReverse();
		}
		
		shoot = TinySound.loadSound(new File("./res/sound/shoot.wav"));
	}
	
	@Override
	public void init() {
		animationShoot.init();
	}

	@Override
	public void activate() {
		animationShoot.reset();
		
		shootingTime = 300;
		
		shoot.play(volEffects);
	}

	@Override
	public void update(byte mask, int deltaTime) {
		animationShoot.update(deltaTime);
		
		if (shootingTime > 0) {
			shootingTime -= deltaTime;
		} else {
			context.changeContext(BJCharacterMoveContext.STILL, mask);
			character.changeMask(BJCharacter.HAS_BOMB);
			
			if (!reverse) {
				character.getBomb().changeMask(BJBomb.MOVING_EAST);
			} else {
				character.getBomb().changeMask(BJBomb.MOVING_WEST);
			}
		}
	}

	@Override
	public void render() {
		animationShoot.render();
	}

}
