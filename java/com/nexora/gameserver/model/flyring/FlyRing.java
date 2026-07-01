package com.nexora.gameserver.model.flyring;

import com.nexora.gameserver.controllers.FlyRingController;
import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.geometry.Plane3D;
import com.nexora.gameserver.model.templates.flyring.FlyRingTemplate;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.idfactory.IDFactory;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.knownlist.PlayerAwareKnownList;

/**
 * @author xavier
 */
public class FlyRing extends VisibleObject {

	private final FlyRingTemplate template;
	private final Plane3D plane;

	public FlyRing(FlyRingTemplate template, int instanceId) {
		super(IDFactory.getInstance().nextId(), new FlyRingController(), null, null, World.getInstance().createPosition(template.getMap(),
			template.getCenter().getX(), template.getCenter().getY(), template.getCenter().getZ(), (byte) 0, instanceId), true);

		((FlyRingController) getController()).setOwner(this);
		this.template = template;
		Vector3f p1 = new Vector3f(template.getCenter().getX(), template.getCenter().getY(), template.getCenter().getZ());
		Vector3f p2 = new Vector3f(template.getP1().getX(), template.getP1().getY(), template.getP1().getZ());
		Vector3f p3 = new Vector3f(template.getP2().getX(), template.getP2().getY(), template.getP2().getZ());
		this.plane = new Plane3D(p1, p2, p3);
		setKnownlist(new PlayerAwareKnownList(this));
	}

	public boolean isCrossed(Vector3f oldPosition, Vector3f newPosition) {
		Vector3f intersection = plane.intersection(oldPosition, newPosition);
		return intersection != null && PositionUtil.isInRange(this, intersection.x, intersection.y, intersection.z, template.getRadius());
	}

	public FlyRingTemplate getTemplate() {
		return template;
	}

	@Override
	public String getName() {
		return template.getName();
	}

	public void spawn() {
		World.getInstance().spawn(this);
	}
}
