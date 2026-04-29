package com.project.hackhub;

import com.project.hackhub.observer.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class HackHubApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HackHubApplication.class, args);
		initializeListeners(context);
	}

	private static void initializeListeners(ConfigurableApplicationContext context) {
		EventManager eventManager = EventManager.getInstance();

		Map<String, EventListener> listeners = context.getBeansOfType(EventListener.class);
		for (EventListener listener : listeners.values()) {
			eventManager.addListenerToList(listener);
		}
	}
}
