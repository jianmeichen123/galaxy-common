package com.galaxyinternet.framework.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.galaxyinternet.framework.core.enums.DbExecuteType;
import com.galaxyinternet.framework.core.model.Page;
import com.galaxyinternet.framework.core.model.PrimaryKeyObject;
import com.galaxyinternet.framework.core.query.Query;

/**
 * 通用基础DAO
 * 
 * @author keifer
 * @param <T>
 */
public interface BaseDao<T extends PrimaryKeyObject<ID>, ID extends Serializable> {

	/**
	 * 查询一个对象，如果返回的结果多于一个对象将会抛出TooManyResultsException
	 * 
	 * @param obj
	 *            查询对象，不能为null
	 * @return Mapper中映射的对象，继承自 T对象，一般是Vo对象
	 */
	public T selectOne(T query);

	/**
	 * 通过Id查询一个对象，如果id为null这会抛出IllegalArgumentException异常
	 * 
	 * @param id
	 *            主键，不能为null
	 * @return 结果对象，如果未找到返回null
	 */
	public T selectById(ID id);

	/**
	 * 查询对象列表
	 * 
	 * @param query
	 *            查询参数，如果未null则查询所有，相当于调用方法selectAll
	 * @return 结果对象列表
	 */
	public List<T> selectList(T query);

	/**
	 * 查询所有记录列表
	 * 
	 * @return List 结果列表
	 */
	public List<T> selectAll();

	/**
	 * 根据结果集中的一列作为key，将结果集转换成Map
	 * 
	 * @param <K>
	 *            返回Map的key类型
	 * @param <V>
	 *            返回Map的Value类型
	 * @param query
	 *            查询参数,如果为null则查询所有对象
	 * @param mapKey
	 *            返回结果List中‘mapKey’属性值作为Key (The property to use as key for each
	 *            value in the list.)
	 * @return Map 包含key属性值的Map对象
	 */
	public <K, V extends T> Map<K, V> selectMap(T query, String mapKey);

	/**
	 * <pre>
	 * 查询对象列表，注意：在给定非null的分页对象时该方法自动设置分页总记录数,如果query和pageable同时为null则查询所有
	 * </pre>
	 * 
	 * @param query
	 *            查询参数
	 * @param pageable
	 *            分页对象
	 * @return List 根据分页对象查询的分页结果列表
	 */
	public List<T> selectList(T query, Pageable pageable);

	/**
	 * <pre>
	 * 查询对象列表，注意：在给定非null的分页对象时该方法自动设置分页总记录数,如果query和pageable同时为null则查询所有
	 * </pre>
	 * 
	 * @param query
	 *            查询参数
	 * @param pageInfo
	 *            分页对象
	 * @return Page 信息方便前台显示
	 */
	public Page<T> selectPageList(T query, Pageable pageable);

	/**
	 * 根据结果集中的一列作为key，将结果集转换成Map
	 * 
	 * @param <K>
	 *            返回Map的key类型
	 * @param <V>
	 *            返回Map的Value类型
	 * @param query
	 *            查询参数
	 * @param mapKey
	 *            返回结果List中‘mapKey’属性值作为Key (The property to use as key for each
	 *            value in the list.)
	 * @param page
	 *            分页对象
	 * @return Map containing key pair data.
	 */
	public <K, V extends T> Map<K, V> selectMap(T query, String mapKey, Pageable pageable);

	/**
	 * 查询总记录数
	 * 
	 * @return long 记录总数
	 */
	public Long selectCount();

	/**
	 * 查询记录数
	 * 
	 * @param query
	 *            查询对象，如果为null，则查询对象总数
	 * @return long 记录总数
	 */
	public Long selectCount(T query);

	/**
	 * 添加对象,如果要添加的对象没有设置Id或者Id为空字符串或者是空格，则添加数据之前会调用 generateId()方法设置Id
	 * 
	 * @param entity
	 *            要实例化的实体，不能为null
	 * @return 受影响的结果数
	 */
	public ID insert(T entity);

	/**
	 * 删除对象
	 * 
	 * @param entity
	 *            要删除的实体对象，不能为null
	 * @return int 受影响结果数
	 */
	public int delete(T query);

	/**
	 * 根据Id删除对象
	 * 
	 * @param id
	 *            要删除的ID，不能为null
	 * @return int 受影响结果数
	 */
	public int deleteById(ID id);

	/**
	 * 删除所有
	 * 
	 * @return int 受影响结果数
	 */
	public int deleteAll();

	/**
	 * 更新对象，对象必须设置ID
	 * 
	 * @param entity
	 *            实体的Id不能为null
	 * @return int 受影响结果数
	 */
	public int updateById(T entity);

	/**
	 * 更新对象中已设置的字段，未设置的字段不更新
	 * 
	 * @param entity
	 *            要更新的实体对象，不能为null，切ID必须不为null
	 * @return int 受影响结果数
	 */
	public int updateByIdSelective(T entity);

	/**
	 * 根据id，批量删除记录，如果传入的列表为null或为空列表则直接返回
	 * 
	 * @param idList
	 *            批量删除ID列表
	 */
	public void deleteByIdInBatch(List<ID> idList);

	/**
	 * 批量插入，如果为空列表则直接返回
	 * 
	 * @param entityList
	 *            需要批量插入的实体对象列表
	 */
	public void insertInBatch(List<T> entityList);

	/**
	 * 批量更新，该方法根据实体ID更新已设置的字段，未设置的字段不更新
	 * 
	 * @param entityList
	 *            批量更新的实体对象列表
	 */
	public void updateInBatch(List<T> entityList);
	
	/**
	 * 该方法可以完成CRUD的所有操作，但是不支持分页
	 * 
	 * @param clazz
	 *            实体对象的class对象
	 * @param type
	 *            数据库执行的类型:SELECT,UPDATE,INSERT,DELETE
	 * @param sqlId
	 *            mybatis对应的mapper.xml中的sql唯一号
	 * @param params
	 *            对应sql传入的参数,一般是Map
	 */
	public <V extends T> List<V> executeSql(DbExecuteType type, String sqlId, Object params);
	@SuppressWarnings("rawtypes")
	public List runSql(DbExecuteType type, String sqlId, Object params);
	
	/**
	 * 查询单个记录
	 * 
	 * @param sqlId
	 *            mybatis的mapper.xml中定义的sql脚本的id
	 * @return V 查询的结果对象
	 */
	public <V extends T> V selectOne(String sqlId, T query);
	
	/**
	 * 查询所有记录列表
	 * 
	 * @param sqlId
	 *            mybatis的mapper.xml中定义的sql脚本的id
	 * @return List 结果列表
	 */
	public <V extends T> List<V> selectAll(String sqlId, T query);
	
	/**
	 * <pre>
	 * 查询对象列表，注意：在给定非null的分页对象时该方法自动设置分页总记录数,如果query和pageable同时为null则查询所有
	 * </pre>
	 * 
	 * @param SqlId
	 *            sql的id值
	 * @param query
	 *            查询参数
	 * @param pageInfo
	 *            分页对象
	 * @return Page 信息方便前台显示
	 */
	public <V extends T> Page<V> selectPageList(String sqlId, T query, Pageable pageable);
	
	public <V extends T> Page<V> selectPageList(Query query);
	
}
