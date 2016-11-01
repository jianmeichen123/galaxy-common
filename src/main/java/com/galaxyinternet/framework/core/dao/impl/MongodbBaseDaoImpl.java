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
public class MongodbBaseDaoImpl<T, ID> implements MongodbBaseDao<T, ID> {
	
	@Autowired
	protected MongoTemplate mongoTemplate;

	public void save(T bean) throws MongoDBException {
	    try {
			mongoTemplate.save(bean);
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB save error:" + bean.toString());
		}
	}
	
	public void deleteByCondition(T query) throws MongoDBException {
	    try {
			Query q = buildBaseQuery(query);
			mongoTemplate.remove(q, getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB delete error:" + query.toString());
		}
	}
	
	public void deleteById(ID id) throws MongoDBException {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			mongoTemplate.remove(query);
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB delete error:" + id.toString());
		}
	}

    public void updateById(ID id, T t) throws MongoDBException {
        try {
        	 Query query = new Query();
             query.addCriteria(Criteria.where("id").is(id));
             Update update = buildBaseUpdate(t);
             mongoTemplate.updateMulti(query, update, this.getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB update error:" + id.toString());
		}
    }

	public List<T> find(T t) throws MongoDBException {
		try {
			Query query = new Query();
			query=buildBaseQuery(t);
			return mongoTemplate.find(query, this.getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB find error:" + t.toString());
		}
	}

    public T findOne(T t) throws MongoDBException {
    	try {
    		Query query = new Query();
			query=buildBaseQuery(t);
			return mongoTemplate.findOne(query, this.getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB find error:" + t.toString());
		}
    }

    public T findOneById(ID id) throws MongoDBException {
        try {
			return mongoTemplate.findById(id, this.getEntityClass());
		} catch (Exception e) {
			throw new MongoDBException(e, "MongoDB find error:" + id.toString());
		}
    }


    /**
     * 通过反射的方式将T转换为org.springframework.data.mongodb.core.query.Query
     * @param t
     * @return
     */
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
    
    /**
     * 通过反射的方式将T转换为org.springframework.data.mongodb.core.query.Update
     * @param t
     * @return
     */
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

	/**
	 * 获取泛型实例的完全限定名
	 */
	@SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
	    return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
}
