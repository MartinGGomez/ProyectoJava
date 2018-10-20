package com.services.combat;

import com.actors.Enemy;
import com.actors.Player;
import com.actors.states.PlayerStates;
import com.screens.GameScreen;

public abstract class Combat {
	
	public static boolean canAttackToEnemy(Player player, Enemy enemy) {
		if ((player.direction.equals(PlayerStates.FRONT) || player.direction.equals(PlayerStates.STANDING_FRONT))
				&& ((enemy.body.getPosition().y + enemy.getHeight()) > player.body.getPosition().y)
				&& ((((enemy.body.getPosition().x - player.body.getPosition().x) < 0.15
						&& enemy.body.getPosition().x - player.body.getPosition().x > -0.15))
				|| ((enemy.body.getPosition().x - player.body.getPosition().x) > 0.15
						&& enemy.body.getPosition().x - player.body.getPosition().x < -0.15))) {
			return true;
		} else if ((player.direction.equals(PlayerStates.BACK) || player.direction.equals(PlayerStates.STANDING_BACK))
				&& (enemy.body.getPosition().y > player.body.getPosition().y)
				&& (((enemy.body.getPosition().x - player.body.getPosition().x) < 0.15
						&& enemy.body.getPosition().x - player.body.getPosition().x > -0.15))
				|| ((enemy.body.getPosition().x - player.body.getPosition().x) > 0.15
						&& enemy.body.getPosition().x - player.body.getPosition().x < -0.15)) {
			return true;
		} else if((player.direction.equals(PlayerStates.RIGHT) || player.direction.equals(PlayerStates.STANDING_RIGHT))
				&& (enemy.body.getPosition().x > player.body.getPosition().x)
				&& (((enemy.body.getPosition().y - player.body.getPosition().y) < 0.15
						&& enemy.body.getPosition().y - player.body.getPosition().y > -0.15))
				|| ((enemy.body.getPosition().y - player.body.getPosition().y) > 0.15
						&& enemy.body.getPosition().y - player.body.getPosition().y < -0.15)) {
			return true;
		} else if ((player.direction.equals(PlayerStates.LEFT) || player.direction.equals(PlayerStates.STANDING_LEFT))
				&& (enemy.body.getPosition().x < player.body.getPosition().x)
				&& (((enemy.body.getPosition().y - player.body.getPosition().y) < 0.15
						&& enemy.body.getPosition().y - player.body.getPosition().y > -0.15))
				|| ((enemy.body.getPosition().y - player.body.getPosition().y) > 0.15
						&& enemy.body.getPosition().y - player.body.getPosition().y < -0.15)) {
			return true;
		} else {
			return false;
		}
	}
	
}
