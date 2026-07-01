package com.nexora.gameserver.instance;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.commons.scripting.ScriptManager;
import com.nexora.commons.scripting.classlistener.AggregatedClassListener;
import com.nexora.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.nexora.gameserver.configs.main.InstanceConfig;
import com.nexora.gameserver.instance.handlers.GeneralInstanceHandler;
import com.nexora.gameserver.instance.handlers.InstanceHandler;
import com.nexora.gameserver.instance.handlers.InstanceID;
import com.nexora.gameserver.model.GameEngine;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * @author ATracer
 */
public class InstanceEngine implements GameEngine {

	private static final Logger log = LoggerFactory.getLogger(InstanceEngine.class);
	private final Map<Integer, Class<? extends InstanceHandler>> instanceHandlers = new HashMap<>();

	@Override
	public void init() {
		ScriptManager scriptManager = new ScriptManager();
		AggregatedClassListener acl = new AggregatedClassListener();
		acl.addClassListener(new OnClassLoadUnloadListener());
		acl.addClassListener(new InstanceHandlerClassListener());
		scriptManager.setGlobalClassListener(acl);
		scriptManager.load(InstanceConfig.HANDLER_DIRECTORY);
		log.info("Loaded " + instanceHandlers.size() + " instance handlers.");
	}

	public InstanceHandler getNewInstanceHandler(WorldMapInstance instance) {
		Class<? extends InstanceHandler> handlerClass = instanceHandlers.get(instance.getMapId());
		InstanceHandler instanceHandler = null;
		if (handlerClass != null) {
			try {
				instanceHandler = handlerClass.getDeclaredConstructor(WorldMapInstance.class).newInstance(instance);
			} catch (Exception ex) {
				log.warn("Can't instantiate instance handler for map " + instance.getMapId() + " (instanceId: " + instance.getInstanceId() + ')', ex);
			}
		}

		return instanceHandler != null ? instanceHandler : new GeneralInstanceHandler(instance);
	}

	final void addInstanceHandlerClass(Class<? extends InstanceHandler> handler) {
		InstanceID idAnnotation = handler.getAnnotation(InstanceID.class);
		if (idAnnotation != null) {
			instanceHandlers.put(idAnnotation.value(), handler);
		}
	}

	public static InstanceEngine getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final InstanceEngine instance = new InstanceEngine();
	}
}
