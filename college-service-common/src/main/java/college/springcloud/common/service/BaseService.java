package college.springcloud.common.service;

import college.springcloud.common.db.BbcMapper;
import college.springcloud.common.query.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 17:51
 * Version:V1.0
 * 我可不可以更近一步，这个Service 都不用实现。如果需要实现，直接实现接口
 */
public abstract class BaseService<T> implements IService<T> {

    @Autowired
    BbcMapper mapper;

    @Override
    public List<T> queryAll() {
        if (mapper == null) {
            return new ArrayList<>();
        }
        return mapper.selectAll();
    }

    public List<T> queryList(T entity) {
        return this.mapper.select(entity);
    }

    public T queryOne(T entity) {
        return (T) this.mapper.selectOne(entity);
    }

    public T queryById(Object id) {
        return (T) this.mapper.selectByPrimaryKey(id);
    }

    public List<T> queryByCriteriaOrigin(T t) {
        //个人觉得在此基础上，在做一个适配即可，没有必要再写一遍
        Example example = new Example(t.getClass());
        Example.Criteria criteria1 = example.createCriteria();
        /**
         * 如果第二参数是属性，第三个参数是值，会带来一个新的问题。如果多参数怎么处理
         * 所以干脆，第二个对象传对象。第一个对象传类型。
         */
        return null;
    }

    public List<T> queryByCriteria(Criteria<T, Object> criteria) {
        return this.mapper.selectBySql(criteria.buildSql());
    }

    public List<T> queryByExample(Example example) {
        return this.mapper.selectByExample(example);
    }

    public T queryOneByCriteria(Criteria<T, Object> criteria) {
        List<T> ts = this.mapper.selectBySql(criteria.buildSql());
        return ts.size() == 0 ? null : ts.get(0);
    }

    public int countByCriteria(Criteria<T, Object> criteria) {
        return 1;
//        return this.mapper.countBySql(criteria.buildCountSql());
    }

    public int create(T entity) {
        return this.mapper.insertSelective(entity);
    }

    public T saveAndReturn(T entity) {
        this.mapper.insertSelective(entity);
//        return this.mapper.selectByPrimaryKey(entity);
        return null;
    }

    public int createBatch(List<T> list) {
        return this.mapper.insertList(list);
    }

    public int updateAll(T entity) {
        return this.mapper.updateByPrimaryKey(entity);
    }

    public int updateNotNull(T entity) {
        return this.mapper.updateByPrimaryKeySelective(entity);
    }

    public int delete(T entity) {
        return this.mapper.delete(entity);
    }

    public int deleteById(Object id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    public int count(T entity) {
        return this.mapper.selectCount(entity);
    }
}
