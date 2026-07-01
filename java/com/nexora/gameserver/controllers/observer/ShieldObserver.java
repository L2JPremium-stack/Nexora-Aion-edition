package com.nexora.gameserver.controllers.observer;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.geometry.Point3D;
import com.nexora.gameserver.model.siege.FortressLocation;
import com.nexora.gameserver.model.templates.shield.ShieldPoint;
import com.nexora.gameserver.model.templates.shield.ShieldTemplate;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.WorldPosition;

/**
 * @author Wakizashi, Source
 */
public class ShieldObserver extends ActionObserver {

	private final FortressLocation location;
	private final Creature creature;
	private final ShieldTemplate shield;
	private final Point3D oldPosition;

	public ShieldObserver(FortressLocation location, ShieldTemplate shield, Creature creature) {
		super(ObserverType.MOVE);
		this.location = location;
		this.creature = creature;
		this.shield = shield;
		WorldPosition lastPos;
		if (creature instanceof Player player && (lastPos = player.getMoveController().getLastPositionFromClient()) != null) {
			this.oldPosition = new Point3D(lastPos.getX(), lastPos.getY(), lastPos.getZ());
		} else {
			this.oldPosition = new Point3D(creature.getX(), creature.getY(), creature.getZ());
		}
	}

	@Override
	public void moved() {
		ShieldPoint shieldCenter = shield.getCenter();
		boolean passedThrough = false;
		// only collide with upper half of sphere
		if (location.isUnderShield() && !(creature.getZ() < shieldCenter.getZ() && oldPosition.getZ() < shieldCenter.getZ())) {
			boolean wasInside = PositionUtil.isInRange(oldPosition.getX(), oldPosition.getY(), oldPosition.getZ(), shieldCenter.getX(), shieldCenter.getY(), shieldCenter.getZ(), shield.getRadius());
			boolean isInside = PositionUtil.isInRange(creature, shieldCenter.getX(), shieldCenter.getY(), shieldCenter.getZ(), shield.getRadius());
			passedThrough = wasInside != isInside;
		}

		if (passedThrough) {
			CollisionDieActor.kill(creature);
		} else {
			synchronized (oldPosition) {
				oldPosition.setX(creature.getX());
				oldPosition.setY(creature.getY());
				oldPosition.setZ(creature.getZ());
			}
		}
	}
}
