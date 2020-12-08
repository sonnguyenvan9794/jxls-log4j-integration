package org.jxls.util;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.jxls.template.SimpleExporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SPI creator factory
 */
public class ServiceFactoryDefaultImpl implements ServiceFactory {
	private static final Logger LOGGER = LogManager.getLogger(SimpleExporter.class);
	
    @Override
    public <T> T createService(Class<T> interfaceClass, T defaultImpl) {
        final T service;
        final Iterator<T> iterator = ServiceLoader.load(interfaceClass).iterator();
        if (iterator.hasNext()) {
            service = iterator.next();
        } else {
            service = defaultImpl;
        }
        LOGGER.debug("SPI {} => {}", interfaceClass.getName(), service);
        LOGGER.info("you may change the SPI on file: META-INF/services/{}", interfaceClass.getName());
        return service;
    }
}
