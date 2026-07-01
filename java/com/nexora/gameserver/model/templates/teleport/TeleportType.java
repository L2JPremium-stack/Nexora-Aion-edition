package com.nexora.gameserver.model.templates.teleport;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlType(name = "type")
@XmlEnum
public enum TeleportType {
	REGULAR,
	FLIGHT
}
