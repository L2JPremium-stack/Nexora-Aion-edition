package instance;

import com.nexora.gameserver.instance.handlers.InstanceID;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * @author Cheatkiller, Estrayl
 */
@InstanceID(301330000)
public class DanuarReliquaryInstance_L extends DanuarReliquaryInstance {

	public DanuarReliquaryInstance_L(WorldMapInstance instance) {
		super(instance);
	}

	@Override
	protected int getExitId() {
		return 730843;
	}

	@Override
	protected int getTreasureBoxId() {
		return 802183;
	}

}
