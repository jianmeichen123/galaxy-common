package com.galaxyinternet.framework.core.mongodb;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 
 *
 * @Description: 用于操作mongodb的通用接口
 * @author keifer
 * @date 2016年4月14日
 * @param <T>
 * @param <PK>
 */
public interface BaseRepository<T extends MongoEntity<ID>, ID extends Serializable> extends MongoRepository<T, ID> {

}
