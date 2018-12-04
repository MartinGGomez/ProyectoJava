package com.services.combat;

import com.actors.Character;
import com.actors.Enemy;
import com.actors.Player;
import com.actors.states.PlayerStates;
import com.screens.GameScreen;

public abstract class Combat {
	
	public static boolean canAttackToEnemy(Player player, Character enemy) {
		System.out.println("player x " + player.getX() + " - enemy  x " + enemy.getX());
		if ((player.direction.equals(PlayerStates.FRONT) || player.direction.equals(PlayerStates.STANDING_FRONT))
				&& ((enemy.getY() + enemy.getHeight()) > player.getY())
				&& ((((enemy.getX() - player.getX()) < 0.40
						&& enemy.getX() - player.getX() > -0.40))
				|| ((enemy.getX() - player.getX()) > 0.40
						&& enemy.getX() - player.getX() < -0.40))) {
			return true;
		} else if ((player.direction.equals(PlayerStates.BACK) || player.direction.equals(PlayerStates.STANDING_BACK))
				&& (enemy.getY() > player.getY())
				&& (((enemy.getX() - player.getX()) < 0.40
						&& enemy.getX() - player.getX() > -0.40))
				|| ((enemy.getX() - player.getX()) > 0.40
						&& enemy.getX() - player.getX() < -0.40)) {
			return true;
		} else if((player.direction.equals(PlayerStates.RIGHT) || player.direction.equals(PlayerStates.STANDING_RIGHT))
				&& (enemy.getX() > player.getX())
				&& (((enemy.getY() - player.getY()) < 0.40
						&& enemy.getY() - player.getY() > -0.40))
				|| ((enemy.getY() - player.getY()) > 0.40
						&& enemy.getY() - player.getY() < -0.40)) {
			return true;
		} else if ((player.direction.equals(PlayerStates.LEFT) || player.direction.equals(PlayerStates.STANDING_LEFT))
				&& (enemy.getX() < player.getX())
				&& (((enemy.getY() - player.getY()) < 0.40
						&& enemy.getY() - player.getY() > -0.40))
				|| ((enemy.getY() - player.getY()) > 0.40
						&& enemy.getY() - player.getY() < -0.40)) {
			return true;
		} else {
			return false;
		}
	}
	
}
