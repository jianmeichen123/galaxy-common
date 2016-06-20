package com.galaxyinternet.framework.core.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.galaxyinternet.framework.core.constants.SqlId;
import com.galaxyinternet.framework.core.dao.BaseDao;
import com.galaxyinternet.framework.core.enums.DbExecuteType;
import com.galaxyinternet.framework.core.exception.DaoException;
import com.galaxyinternet.framework.core.id.IdGenerator;
import com.galaxyinternet.framework.core.model.Page;
import com.galaxyinternet.framework.core.model.PrimaryKeyObject;
import com.galaxyinternet.framework.core.query.Query;
import com.galaxyinternet.framework.core.utils.BeanUtils;
import com.galaxyinternet.framework.core.utils.GSONUtil;

/**
 * 基础Dao接口实现类，实现改类的子类必须设置泛型类型
 * 
 * @author Keifer
 */
public abstract class BaseDaoImpl<T extends PrimaryKeyObject<ID>, ID extends Serializable> implements BaseDao<T, ID> {

	@Autowired(required = true)
	protected SqlSession sqlSessionTemplate;

	public static final String SQLNAME_SEPARATOR = ".";

	/**
	 * @fields sqlNamespace SqlMapping命名空间
	 */
	private String sqlNamespace = getDefaultSqlNamespace();

	/**
	 * 获取泛型类型的实体对象类全名
	 */
	protected String getDefaultSqlNamespace() {
		Class<?> genericClass = BeanUtils.getGenericClass(this.getClass());
		return genericClass == null ? null : genericClass.getName();
	}

	/**
	 * 获取SqlMapping命名空间
	 */
	public String getSqlNamespace() {
		return sqlNamespace;
	}

	/**
	 * 设置SqlMapping命名空间。 以改变默认的SqlMapping命名空间， 不能滥用此方法随意改变SqlMapping命名空间。
	 */
	public void setSqlNamespace(String sqlNamespace) {
		this.sqlNamespace = sqlNamespace;
	}

	/**
	 * 将SqlMapping命名空间与给定的SqlMapping名组合在一起。
	 * 
	 * @param sqlName
	 *            SqlMapping名
	 * @return 组合了SqlMapping命名空间后的完整SqlMapping名
	 */
	protected String getSqlName(String sqlName) {
		return sqlNamespace + SQLNAME_SEPARATOR + sqlName;
	}

	/**
	 * 生成全局唯一主键。
	 * 
	 * @param entity
	 *            要持久化的对象
	 */
	protected Long generateId() {
		return IdGenerator.generateId(BeanUtils.getGenericClass(this.getClass()));
	}

	@Override
	public T selectOne(T query) {
		Assert.notNull(query);
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			return sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT), params);
		} catch (Exception e) {
			throw new DaoException(String.format("查询一条记录出错！语句：%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	@Override
	public T selectById(ID id) {
		Assert.notNull(id);
		try {
			return sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_BY_ID), id);
		} catch (Exception e) {
			throw new DaoException(String.format("根据ID查询对象出错！语句：%s", getSqlName(SqlId.SQL_SELECT_BY_ID)), e);
		}
	}

	@Override
	public List<T> selectList(T query) {
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			return sqlSessionTemplate.selectList(getSqlName(SqlId.SQL_SELECT), params);
		} catch (Exception e) {
			throw new DaoException(String.format("查询对象列表出错！语句：%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	@Override
	public List<T> selectAll() {
		try {
			return sqlSessionTemplate.selectList(getSqlName(SqlId.SQL_SELECT));
		} catch (Exception e) {
			throw new DaoException(String.format("查询所有对象列表出错！语句：%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	@Override
	public <K, V extends T> Map<K, V> selectMap(T query, String mapKey) {
		Assert.notNull(mapKey, "[mapKey] - must not be null!");
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			return sqlSessionTemplate.selectMap(getSqlName(SqlId.SQL_SELECT), params, mapKey);
		} catch (Exception e) {
			throw new DaoException(String.format("查询对象Map时出错！语句：%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	/**
	 * 设置分页
	 * 
	 * @param pageInfo
	 *            分页信息
	 * @return SQL分页参数对象
	 */
	protected RowBounds getRowBounds(Pageable pageable) {
		RowBounds bounds = RowBounds.DEFAULT;
		if (null != pageable) {
			bounds = new RowBounds(pageable.getOffset(), pageable.getPageSize());
		}
		return bounds;
	}

	/**
	 * 获取分页查询参数
	 * 
	 * @param query
	 *            查询对象
	 * @param pageable
	 *            分页对象
	 * @return Map 查询参数
	 */
	protected Map<String, Object> getParams(T query, Pageable pageable) {
		Map<String, Object> params = BeanUtils.toMap(query, getRowBounds(pageable));
		if (pageable != null && pageable.getSort() != null) {
			String sorting = pageable.getSort().toString();
			params.put("sorting", sorting.replace(":", ""));
			String str=(String)params.get("sorting");
			if(str.contains("---")){
				
				params.put("sorting", str.replace("---", ":"));
			}
		
		}

		return params;
	}

	@Override
	public List<T> selectList(T query, Pageable pageable) {
		try {
			return sqlSessionTemplate.selectList(getSqlName(SqlId.SQL_SELECT), getParams(query, pageable));
		} catch (Exception e) {
			throw new DaoException(String.format("根据分页对象查询列表出错！语句:%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	@Override
	public Page<T> selectPageList(Query query) {
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			params.put("offset", query.getOffset());
			params.put("limit", query.getPageSize());
			List<T> contentList = sqlSessionTemplate.selectList(getSqlName(SqlId.SQL_SELECT),params);
			System.err.println("contentList==>>"+GSONUtil.toJson(contentList));
			return new  Page<T>(contentList, null, this.selectQueryCount(query));
		} catch (Exception e) {
			throw new DaoException(String.format("根据分页对象查询列表出错！语句:%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	
	@Override
	public Page<T> selectPageList(T query, Pageable pageable) {
		try {
			List<T> contentList = sqlSessionTemplate.selectList(getSqlName(SqlId.SQL_SELECT),
					getParams(query, pageable));
			return new  Page<T>(contentList, pageable, this.selectCount(query));
		} catch (Exception e) {
			throw new DaoException(String.format("根据分页对象查询列表出错！语句:%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}
	
	@Override
	public <K, V extends T> Map<K, V> selectMap(T query, String mapKey, Pageable pageable) {
		try {
			return sqlSessionTemplate.selectMap(getSqlName(SqlId.SQL_SELECT), getParams(query, pageable), mapKey);
		} catch (Exception e) {
			throw new DaoException(String.format("根据分页对象查询列表出错！语句:%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	@Override
	public Long selectCount() {
		try {
			return sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_COUNT));
		} catch (Exception e) {
			throw new DaoException(String.format("查询对象总数出错！语句：%s", getSqlName(SqlId.SQL_SELECT_COUNT)), e);
		}
	}

	@Override
	public Long selectCount(T query) {
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			return sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_COUNT), params);
		} catch (Exception e) {
			throw new DaoException(String.format("查询对象总数出错！语句：%s", getSqlName(SqlId.SQL_SELECT_COUNT)), e);
		}
	}

	public Long selectQueryCount(Query query) {
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			return sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_COUNT), params);
		} catch (Exception e) {
			throw new DaoException(String.format("查询对象总数出错！语句：%s", getSqlName(SqlId.SQL_SELECT_COUNT)), e);
		}
	}
	
	@Override
	@Transactional
	public ID insert(T entity) {
		Assert.notNull(entity);
		try {
			/*ID id = entity.getId();
			if (null == id) {
				if (StringUtils.isBlank(stringId)) {
					entity.setId((ID) generateId());
				}
			}*/
			appendCreatedTime(entity);
			sqlSessionTemplate.insert(getSqlName(SqlId.SQL_INSERT), entity);
			return entity.getId();
		} catch (Exception e) {
			throw new DaoException(String.format("添加对象出错！语句：%s", getSqlName(SqlId.SQL_INSERT)), e);
		}
	}

	@Override
	@Transactional
	public int delete(T query) {
		Assert.notNull(query);
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			return sqlSessionTemplate.delete(getSqlName(SqlId.SQL_DELETE), params);
		} catch (Exception e) {
			throw new DaoException(String.format("删除对象出错！语句：%s", getSqlName(SqlId.SQL_DELETE)), e);
		}
	}

	@Override
	@Transactional
	public int deleteById(ID id) {
		Assert.notNull(id);
		try {
			return sqlSessionTemplate.delete(getSqlName(SqlId.SQL_DELETE_BY_ID), id);
		} catch (Exception e) {
			throw new DaoException(String.format("根据ID删除对象出错！语句：%s", getSqlName(SqlId.SQL_DELETE_BY_ID)), e);
		}
	}

	@Override
	@Transactional
	public int deleteAll() {
		try {
			return sqlSessionTemplate.delete(getSqlName(SqlId.SQL_DELETE));
		} catch (Exception e) {
			throw new DaoException(String.format("删除所有对象出错！语句：%s", getSqlName(SqlId.SQL_DELETE)), e);
		}
	}

	@Override
	@Transactional
	public int updateById(T entity) {
		Assert.notNull(entity);
		appendUpdatedTime(entity);
		try {
			return sqlSessionTemplate.update(getSqlName(SqlId.SQL_UPDATE_BY_ID), entity);
		} catch (Exception e) {
			throw new DaoException(String.format("根据ID更新对象出错！语句：%s", getSqlName(SqlId.SQL_UPDATE_BY_ID)), e);
		}
	}

	@Override
	@Transactional
	public int updateByIdSelective(T entity) {
		Assert.notNull(entity);
		appendUpdatedTime(entity);
		try {
			return sqlSessionTemplate.update(getSqlName(SqlId.SQL_UPDATE_BY_ID_SELECTIVE), entity);
		} catch (Exception e) {
			throw new DaoException(String.format("根据ID更新对象某些属性出错！语句：%s", getSqlName(SqlId.SQL_UPDATE_BY_ID_SELECTIVE)),
					e);
		}
	}

	@Override
	@Transactional
	public void deleteByIdInBatch(List<ID> idList) {
		if (idList == null || idList.isEmpty())
			return;
		for (ID id : idList) {
			this.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void updateInBatch(List<T> entityList) {
		if (entityList == null || entityList.isEmpty())
			return;
		for (T entity : entityList) {
			this.updateByIdSelective(entity);
		}
	}

	@Override
	@Transactional
	public void insertInBatch(List<T> entityList) {
		if (entityList == null || entityList.isEmpty())
			return;
		for (T entity : entityList) {
			this.insert(entity);
		}
	}

	@Override
	public <V extends T> List<V> executeSql(DbExecuteType type, String sqlId, Object params) {
		String sqlName = sqlNamespace + SQLNAME_SEPARATOR + sqlId;
		List<V> results = null;
		try {
			switch (type) {
			case INSERT:
				sqlSessionTemplate.insert(sqlName, params);
				break;
			case UPDATE:
				sqlSessionTemplate.update(sqlName, params);
				break;
			case DELETE:
				sqlSessionTemplate.delete(sqlName, params);
				break;
			default:
				results = sqlSessionTemplate.selectList(sqlName, params);
				break;
			}
		} catch (Exception e) {
			throw new DaoException(String.format("执行" + type.getDescription() + "出错！语句：%s", sqlName), e);
		}
		return results;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List runSql(DbExecuteType type, String sqlId, Object params) {
		String sqlName = sqlNamespace + SQLNAME_SEPARATOR + sqlId;
		try {
			switch (type) {
			case INSERT:
				sqlSessionTemplate.insert(sqlName, params);
				break;
			case UPDATE:
				sqlSessionTemplate.update(sqlName, params);
				break;
			case DELETE:
				sqlSessionTemplate.delete(sqlName, params);
				break;
			default:
				return sqlSessionTemplate.selectList(sqlName, params);
			}
		} catch (Exception e) {
			throw new DaoException(String.format("执行" + type.getDescription() + "出错！语句：%s", sqlName), e);
		}
		return null;
	}

	@Override
	public <V extends T> V selectOne(String sqlId, T query) {
		Assert.notNull(query);
		String sqlName = "";
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			sqlName = query.getClass().getSuperclass().getName() + SQLNAME_SEPARATOR + sqlId;
			return sqlSessionTemplate.selectOne(sqlName, params);
		} catch (Exception e) {
			throw new DaoException(String.format("查询一条记录出错！语句：%s", sqlName), e);
		}
	}

	@Override
	public <V extends T> List<V> selectAll(String sqlId, T query) {
		try {
			Map<String, Object> params = BeanUtils.toMap(query);
			return sqlSessionTemplate.selectList(getSqlName(sqlId), params);
		} catch (Exception e) {
			throw new DaoException(String.format("查询所有对象列表出错！语句：%s", getSqlName(SqlId.SQL_SELECT)), e);
		}
	}

	@Override
	public <V extends T> Page<V> selectPageList(String sqlId, T query, Pageable pageable) {
		String sqlName = "";
		try {
			sqlName = query.getClass().getSuperclass().getName() + SQLNAME_SEPARATOR + sqlId;
			List<V> contentList = sqlSessionTemplate.selectList(sqlName, getParams(query, pageable));
			return new Page<V>(contentList, pageable, this.selectCount(query));
		} catch (Exception e) {
			throw new DaoException(String.format("根据分页对象查询列表出错！语句:%s", sqlName), e);
		}
	}

	private final void appendCreatedTime(T entity) {
		entity.setCreatedTime(new Date().getTime());
	}

	private final void appendUpdatedTime(T entity) {
		entity.setUpdatedTime(new Date().getTime());
	}
	
	
}
