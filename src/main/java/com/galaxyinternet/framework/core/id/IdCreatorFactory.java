package com.galaxyinternet.framework.core.id;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

/**
 * @author kaihu
 */
public final class IdCreatorFactory {
	private static Logger logger = Logger.getLogger(IdCreatorFactory.class);

	private static ConcurrentMap<Integer, IdCreator> timeIdCreatorMap = new ConcurrentHashMap<Integer, IdCreator>();

	/**
	 * 
	 * @param idcIndex
	 *            // IDC 索引
	 * @return
	 */
	public static IdCreator getTimeIdCreator(Integer idcIndex) {

		if (idcIndex == null) {
			throw new RuntimeException("idcIndex muse have value");
		}

		IdCreator idCreator = timeIdCreatorMap.get(idcIndex);
		if (idCreator == null) {

			IdCreator timeIdCreator = new TimeIdCreator(idcIndex);

			idCreator = timeIdCreatorMap.putIfAbsent(idcIndex, timeIdCreator);
			if (idCreator == null) {
				idCreator = timeIdCreator;
				if (logger.isDebugEnabled()) {
					logger.debug("create new IdCreator , put it to cache ok");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("get idCreator from cache ok");
			}
		}
		return idCreator;

	}
}
