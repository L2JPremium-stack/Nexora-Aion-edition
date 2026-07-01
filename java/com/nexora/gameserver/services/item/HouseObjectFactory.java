package com.nexora.gameserver.services.item;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.ChairObject;
import com.nexora.gameserver.model.gameobjects.EmblemObject;
import com.nexora.gameserver.model.gameobjects.HouseObject;
import com.nexora.gameserver.model.gameobjects.JukeBoxObject;
import com.nexora.gameserver.model.gameobjects.MoveableObject;
import com.nexora.gameserver.model.gameobjects.NpcObject;
import com.nexora.gameserver.model.gameobjects.PassiveObject;
import com.nexora.gameserver.model.gameobjects.PictureObject;
import com.nexora.gameserver.model.gameobjects.PostboxObject;
import com.nexora.gameserver.model.gameobjects.StorageObject;
import com.nexora.gameserver.model.gameobjects.UseableItemObject;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.model.house.HouseRegistry;
import com.nexora.gameserver.model.templates.housing.HousingChair;
import com.nexora.gameserver.model.templates.housing.HousingEmblem;
import com.nexora.gameserver.model.templates.housing.HousingJukeBox;
import com.nexora.gameserver.model.templates.housing.HousingMoveableItem;
import com.nexora.gameserver.model.templates.housing.HousingNpc;
import com.nexora.gameserver.model.templates.housing.HousingPicture;
import com.nexora.gameserver.model.templates.housing.HousingPostbox;
import com.nexora.gameserver.model.templates.housing.HousingStorage;
import com.nexora.gameserver.model.templates.housing.HousingUseableItem;
import com.nexora.gameserver.model.templates.housing.PlaceableHouseObject;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.model.templates.item.actions.SummonHouseObjectAction;
import com.nexora.gameserver.utils.idfactory.IDFactory;

/**
 * @author Rolandas
 */
public final class HouseObjectFactory {

	/**
	 * For loading data from DB
	 */
	public static HouseObject<?> createNew(HouseRegistry registry, int objectId, int objectTemplateId) {
		PlaceableHouseObject template = DataManager.HOUSING_OBJECT_DATA.getTemplateById(objectTemplateId);
		if (template instanceof HousingChair)
			return new ChairObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingJukeBox)
			return new JukeBoxObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingMoveableItem)
			return new MoveableObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingNpc)
			return new NpcObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingPicture)
			return new PictureObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingPostbox)
			return new PostboxObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingStorage)
			return new StorageObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingUseableItem)
			return new UseableItemObject(registry, objectId, template.getTemplateId());
		else if (template instanceof HousingEmblem)
			return new EmblemObject(registry, objectId, template.getTemplateId());
		return new PassiveObject(registry, objectId, template.getTemplateId());
	}

	/**
	 * For transferring item from inventory to house registry
	 */
	public static HouseObject<?> createNew(House house, ItemTemplate itemTemplate) {
		Objects.requireNonNull(itemTemplate.getActions(), "template actions null");

		SummonHouseObjectAction action = itemTemplate.getActions().getHouseObjectAction();
		Objects.requireNonNull(action, "template actions miss SummonHouseObjectAction");

		int objectTemplateId = action.getTemplateId();
		HouseObject<?> obj = createNew(house.getRegistry(), IDFactory.getInstance().nextId(), objectTemplateId);
		int useDays = obj.getObjectTemplate().getUseDays();
		if (useDays > 0) {
			int expireEnd = (int) (System.currentTimeMillis() / 1000 + TimeUnit.DAYS.toSeconds(useDays));
			obj.setExpireTime(expireEnd);
		}
		return obj;
	}
}
