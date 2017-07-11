package org.mybatis.generator.gen;

import java.util.List;
import java.util.Map;



/**
 * GenericService的实现类, 其他的自定义 ServiceImpl, 继承自它,可以获得常用的增删查改操作,
 * 未实现的方法有 子类各自实现
 * <p/>
 * Model : 代表数据库中的表 映射的Java对象类型
 * PK :代表对象的主键类型
 *
 */
public abstract class GenericServiceImpl<Model, PK> implements GenericService<Model, PK> {

    /**
     * 定义成抽象方法,由子类实现,完成dao的注入
     *
     * @return GenericDao实现类
     */
    public abstract GenericDao<Model, PK> getDao();

    /**
     * 插入对象
     *
     * @param model 对象
     */
    public int insert(Model model) {
        return getDao().insert(model);
    }

    /**
     * 更新对象
     *
     * @param model 对象
     */
    public int update(Model model) {
        return getDao().updateByPrimaryKey(model);
    }

    /**
     * 通过主键, 删除对象
     *
     * @param id 主键
     */
    public int delete(PK id) {
        return getDao().deleteByPrimaryKey(id);
    }

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键
     * @return
     */
    public Model selectById(PK id) {
        return getDao().selectByPrimaryKey(id);
    }

	public int updateByPrimaryKey(Model model) {
		return getDao().updateByPrimaryKey(model);
	}
	
	/**
     * 选择更新，对象属性不为空则更新
     * @param model 对象
     */
	public int updateBySelective(Model model) {
	    return getDao().updateByPrimaryKeySelective(model);
	}
	
	/**
	 * 根据字键删除
	 */
	public int deleteByPrimaryKey(PK id) {
	    return this.getDao().deleteByPrimaryKey(id);
	}

	/**
	 * 根据主键查询
	 */
	public Model selectByPrimaryKey(PK id) {
		return this.getDao().selectByPrimaryKey(id);
	}

	


}