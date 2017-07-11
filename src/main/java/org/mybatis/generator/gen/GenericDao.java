package org.mybatis.generator.gen;

import java.util.List;
import java.util.Map;

/**
 * 所有自定义Dao的顶级接口, 封装常用的增删查改操作,
 * 可以通过Mybatis Generator Maven 插件自动生成Dao,
 * 也可以手动编码,然后继承GenericDao 即可.
 * <p/>
 * Model : 代表数据库中的表 映射的Java对象类型
 * PK :代表对象的主键类型
 *
 */
public interface GenericDao<Model, PK> {
	/**
	插入记录，字段不为null的属性才会插入
	**/
	int insertSelective(Model t);
	
    /**
     * 插入对象
     *
     * @param model 对象
     */
    int insert(Model model);

    /**
     * 更新对象
     *
     * @param model 对象
     */
    int updateByPrimaryKey(Model model);

    /**
	更新记录，字段不为null的属性才会更新
	**/
	public int updateByPrimaryKeySelective(Model t);
	
    /**
     * 通过主键, 删除对象
     *
     * @param id 主键
     */
    int deleteByPrimaryKey(PK id);
   
    
    
    /**
     * 通过主键, 查询对象
     *
     * @param id 主键
     * @return
     */
    Model selectByPrimaryKey(PK id);
    
    
    
	
	
    
}