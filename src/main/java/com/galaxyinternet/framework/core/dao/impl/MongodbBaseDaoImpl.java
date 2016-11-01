package com.galaxyinternet.framework.core.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.galaxyinternet.framework.core.dao.MongodbBaseDao;
import com.galaxyinternet.framework.core.exception.MongoDBException;
import com.galaxyinternet.framework.core.utils.MongoDB.QueryField;
public class MongodbBaseDaoImpl<T> implements MongodbBaseDao<T> {
	
	@Autowired
	protected MongoTemplate mongoTemplate;

	//保存一个对象到mongodb
	public void save(T bean) throws MongoDBException {
	    try {
			mongoTemplate.save(bean);
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB save error:" + bean.toString());
		}
	}
	
	// 根据对象的属性删除
	public void deleteByCondition(T query) throws MongoDBException {
	    try {
			Query q = buildBaseQuery(query);
			mongoTemplate.remove(q, getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB delete error:" + query.toString());
		}
	}
	
	// 根据id删除对象
	public <ID> void deleteById(ID id) throws MongoDBException {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			mongoTemplate.remove(query);
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB delete error:" + id.toString());
		}
	}

	

    // 通过条件查询更新数据
	public void update(T query, T bean) throws MongoDBException {
		try {
			Query q = buildBaseQuery(query);
			Update update = buildBaseUpdate(bean);
			mongoTemplate.updateMulti(q, update, this.getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB update error:" + query.toString());
		}
	}

	// 根据id进行更新
    public void updateById(String id, T t) throws MongoDBException {
       
        try {
        	 Query query = new Query();
             query.addCriteria(Criteria.where("id").is(id));
             Update update = buildBaseUpdate(t);
             mongoTemplate.updateMulti(query, update, this.getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB update error:" + id.toString());
		}
    }

	// 通过条件查询实体(集合)
	public List<T> find(T t) {
		Query query = new Query();
		query=buildBaseQuery(t);
		return mongoTemplate.find(query, this.getEntityClass());
	}

	public List<T> findByCondition(T t) {
	    Query query = buildBaseQuery(t);
	    query=buildBaseQuery(t);
	    return mongoTemplate.find(query, getEntityClass());
	}

    // 通过一定的条件查询一个实体
    public T findOne(T t) {
    	 Query query = buildBaseQuery(t);
 	    query=buildBaseQuery(t);
        return mongoTemplate.findOne(query, this.getEntityClass());
    }


    // 通过ID获取记录
    public T get(String id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    // 通过ID获取记录,并且指定了集合名(表的意思)
    public T get(String id, String collectionName) {
        return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
    }

    // 根据vo构建查询条件Query
    private Query buildBaseQuery(T t) {
        Query query = new Query();

	    Field[] fields = t.getClass().getDeclaredFields();
	    for (Field field : fields) {
	        field.setAccessible(true);
	        try {
                Object value = field.get(t);
                if (value != null) {
                    QueryField queryField = field.getAnnotation(QueryField.class);
                    if (queryField != null) {
                        query.addCriteria(queryField.type().buildCriteria(queryField, field, value));
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
	    }
        return query;
    }

    private Update buildBaseUpdate(T t) {
        Update update = new Update();

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if (value != null) {
                   update.set(field.getName(), value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return update;
    }

	// 获取需要操作的实体类class
	@SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
	    return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }


}
