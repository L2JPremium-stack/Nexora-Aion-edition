package com.nexora.gameserver.dataholders.loadingutils.adapters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;

import com.nexora.gameserver.model.templates.item.ItemTemplate;

/**
 * @author Luno
 */
public class NpcEquipmentList {

	@XmlElement(name = "item")
	@XmlIDREF
	public ItemTemplate[] items;

}
