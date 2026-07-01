package com.nexora.gameserver.model.templates.globaldrops;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.TribeClass;

/**
 * @author AionCool
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GlobalDropTribe")
public class GlobalDropTribe {

	@XmlAttribute(name = "tribe", required = true)
	protected TribeClass tribe;

	public TribeClass getTribe() {
		return tribe;
	}
}
