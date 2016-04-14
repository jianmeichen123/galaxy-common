package com.galaxyinternet.framework.core.mongodb;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

/**
 * 
 *
 * @Description: 用于操作mongodb的通用接口
 * @author keifer
 * @date 2016年4月14日
 * @param <T>
 * @param <PK>
 */
public interface BaseRepository<T extends MongoModel, PK extends Serializable> extends CrudRepository<T, PK> {

}
