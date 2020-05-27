package college.springcloud.common.query;

import college.springcloud.common.utils.Reflection;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.util.StringUtil;
import tk.mybatis.mapper.weekend.Fn;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: xuxianbei
 * Date: 2019/8/28
 * Time: 9:50
 * Version:V1.0
 * 其中第二参数不用Object的原因：最终返回界面如果是Object那么还要转换一次
 * 还不如直接用B
 */
@Data
public class Criteria<A, B> {
    private Criteria.Statement statement;
    //缓存对象类型和表实体的
    private static final Map<Class<?>, EntityTable> entityTableCache = new ConcurrentHashMap<>();

    /**
     * 这个方法在实际使用中会遇到多线程访问问题；很简单多个供应商并发这个对象，即造成多线程并发情况
     *
     * 可以参考1.8 concurrentHashMap initTable
     * @param entityClass
     */
    private void cacheEntityTable(Class<A> entityClass) {
        if (!entityTableCache.containsKey(entityClass)) {
            Table table = entityClass.getAnnotation(Table.class);
            //解决并发问题
            Map<String, String> fieldsMap = new ConcurrentHashMap<>();
            Map<String, String> fieldsMapDisplay = new LinkedHashMap<>();
            Field[] declareFields = entityClass.getDeclaredFields();
            //把对象的属性和表字段一一对应
            for (Field field : declareFields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    //这里没有约定
                    fieldsMap.put(field.getName(), column.name());
                    fieldsMapDisplay.put(field.getName(), column.name());
                } else {
                    // 驼峰命名
                    String s = StringUtil.convertByStyle(field.getName(), Style.camelhump);
                    fieldsMap.put(field.getName(), s);
                    fieldsMapDisplay.put(field.getName(), s);
                }
            }
            Criteria.EntityTable entityTable = new Criteria.EntityTable();
            entityTable.setTableName(table.name());
            entityTable.setFieldsMap(fieldsMap);
            entityTable.setFieldsMapDisplay(fieldsMapDisplay);
            entityTableCache.put(entityClass, entityTable);
        }
    }

    private Criteria() {

    }

    private Criteria(Class entityClass) {
        this.statement = new Criteria.Statement();
        //要经常增删？
        this.statement.criterions = new LinkedList();
        this.statement.clazz = entityClass;
    }

    //我觉得这个代码有问题。没办法拿到对象的属性，我觉得一个属性就可以了。实体类。
    // 我觉得这里的<A, B>是多余的
    public static <A, B> Criteria<A, B> of(Class<A> entityClass) {
        return new Criteria(entityClass);
    }

    //总感觉这里有问题，如果有升序，又有降序怎么处理？
    public Criteria<A, B> sortDesc(Fn<A, B>... fns) {
        this.statement.sortNames = college.springcloud.common.utils.Reflection.fnToFieldName(fns);
        this.statement.sortOrder = "desc";
        return this;
    }

    public Criteria<A, B> sort(Fn<A, B>... fns) {
        // 看来 Reflection.fnToFieldName 才是核心代码
        this.statement.sortNames = Reflection.fnToFieldName(fns);
        return this;
    }

    public Criteria<A, B> fields(Fn<A, B>... fns) {
        this.statement.fields = Reflection.fnToFieldName(fns);
        return this;
    }

    public Criteria<A, B> fieldsExcept(Fn<A, B>... fns) {
        Class clazz = this.statement.clazz;
        //这里有一个优化思路，就是先排序，减少循环次数；不过意义不大，除非达到百万级别调用次数
        //考研算法时候到了
        Field[] fields = clazz.getDeclaredFields();
        String[] exceptNames = Reflection.fnToFieldName(fns);
        List<String> list = new LinkedList<>();
        for (Field field : fields) {
            boolean notMatch = true;
            for (String exceptName : exceptNames) {
                if (field.getName().equals(exceptName)) {
                    notMatch = false;
                    break;
                }
            }
            if (notMatch) {
                list.add(field.getName());
            }
        }
        this.statement.fields = list.toArray(new String[]{});
        return this;
    }

    //我比较好奇为什么参数不是Criteria
    //因为参数是 Order::getForderPaymentId 其中Order必须实现serializable
    //其实是通过参数的方法名作为字段名

    /**
     * 作者的设计思路是这样，通过对象的属性名，来确定数据库字段对应的对象的值简单来说
     *
     * @param fn
     * @return
     * @Column(name = "forder_payment_id")
     * private String forderPaymentId;
     * 其中：forderPaymentId就是对象的属性名称。
     * 其实就是为了优化这样类似的代码：andIsNotNull("orderPaymentId", "1")
     * criteria1.andGreaterThan()
     * 这样如果对象的属性名称发生变化，这个值就自动变化了，符合设计思想
     */
    public Criteria<A, B> andIsNotNull(Fn<A, B> fn) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), "is not null", "and"));
        return this;
    }

    public Criteria<A, B> andEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "=", "and"));
        return this;
    }

    public Criteria<A, B> andGreaterThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, ">", "and"));
        return this;
    }

    public Criteria<A, B> andGreaterThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, ">=", "and"));
        return this;
    }

    public Criteria<A, B> andLessThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "<", "and"));
        return this;
    }

    public Criteria<A, B> andLessThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "<=", "and"));
        return this;
    }

    public Criteria<A, B> andIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), values, "in", "and"));
        return this;
    }

    public Criteria<A, B> andNotIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), values, "not in", "and"));
        return this;
    }

    public Criteria<A, B> andBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value1, value2, "between", "and"));
        return this;
    }

    public Criteria<A, B> andNotBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value1, value2, "not between", "and"));
        return this;
    }

    public Criteria<A, B> andLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "like", "and"));
        return this;
    }

    public Criteria<A, B> andLikeAll(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn),
                StringUtils.join("%", value.replaceAll("%", "\\%"), "%"), "like", "and"));
        return this;
    }

    public Criteria<A, B> andNotLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "not like", "and"));
        return this;
    }

    public Criteria<A, B> orIsNull(Fn<A, B> fn) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), "is null", "or"));
        return this;
    }

    public Criteria<A, B> orIsNotNull(Fn<A, B> fn) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), "is not null", "or"));
        return this;
    }

    public Criteria<A, B> orEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "=", "or"));
        return this;
    }

    public Criteria<A, B> orNotEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "<>", "or"));
        return this;
    }

    public Criteria<A, B> orGreaterThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, ">", "or"));
        return this;
    }

    public Criteria<A, B> orGreaterThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, ">=", "or"));
        return this;
    }

    public Criteria<A, B> orLessThan(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "<", "or"));
        return this;
    }

    public Criteria<A, B> orLessThanOrEqualTo(Fn<A, B> fn, Object value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "<=", "or"));
        return this;
    }

    public Criteria<A, B> orIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), values, "in", "or"));
        return this;
    }

    public Criteria<A, B> orNotIn(Fn<A, B> fn, Iterable values) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), values, "not in", "or"));
        return this;
    }

    public Criteria<A, B> orBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value1, value2, "between", "or"));
        return this;
    }

    public Criteria<A, B> orNotBetween(Fn<A, B> fn, Object value1, Object value2) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value1, value2, "not between", "or"));
        return this;
    }

    public Criteria<A, B> orLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "like", "or"));
        return this;
    }

    public Criteria<A, B> orNotLike(Fn<A, B> fn, String value) {
        this.statement.criterions.add(new Criteria.Criterion(Reflection.fnToFieldName(fn), value, "not like", "or"));
        return this;
    }

    public Criteria<A, B> page(Integer pageNum, Integer pageSize) {
        this.statement.pageNum = pageNum;
        this.statement.pageSize = pageSize;
        return this;
    }


    public String buildSql() {
        //解析Po把对象和表一一绑定;
        //把表名和字段名存储到entityTable中
        cacheEntityTable(statement.clazz);
        StringBuilder sql = new StringBuilder();
        sql.append(Criteria.SqlHelper.selectColumns(statement));
        sql.append(Criteria.SqlHelper.fromTable(statement));
        //这句代码的意思就是：让开发人员自己组建sql；我在思考一个问题这样是不是违背
        sql.append(Criteria.SqlHelper.whereClause(statement));
        sql.append(Criteria.SqlHelper.orderByClause(statement));
        //存在优化空间
        sql.append(Criteria.SqlHelper.limit(statement));
        return sql.toString();
    }


    public String buildCountSql() {
        cacheEntityTable(statement.clazz);
        StringBuilder sql = new StringBuilder("");
        sql.append(Criteria.SqlHelper.selectCount(this.statement));
        sql.append(Criteria.SqlHelper.fromTable(this.statement));
        sql.append(Criteria.SqlHelper.whereClause(this.statement));
        return sql.toString();
    }

    public static void main(String[] args) {
        Criteria<Statement, Statement> criteria = Criteria.of(Statement.class);
        System.out.print(StringUtil.convertByStyle("fstrName", Style.camelhump));
    }

    //这个应该是用来组织语句的
    @Data
    public static class Statement<A> {
        private Class<A> clazz;
        private List<Criteria.Criterion> criterions;
        private String[] fields;
        private String[] sortNames;
        private String sortOrder;
        private int pageNum;
        private int pageSize;
    }

    //当我看到这段代码，大概就猜出来，这里是怎么用的

    /**
     * Criteria.of(对象)得到他的Criteria
     * 然后通过填充Criterion来组装条件。
     */
    @Data
    public static class Criterion {
        private String property;
        private Object value;
        private Object secondValue;
        private String condition;
        private String andOr;
        private Criteria.Criterion.ValueType valueType;

        public Criterion() {

        }

        public Criterion(String property, String condition, String andOr) {
            this.property = property;
            this.condition = condition;
            this.andOr = andOr;
            this.valueType = Criteria.Criterion.ValueType.noValue;
        }


        public Criterion(String property, Object value, String condition, String andOr) {
            this.property = property;
            this.value = value;
            this.condition = condition;
            this.andOr = andOr;
            if (value instanceof Iterable) {
                this.valueType = Criteria.Criterion.ValueType.listValue;
            } else {
                this.valueType = Criteria.Criterion.ValueType.singleValue;
            }
        }

        public Criterion(String property, Object value1, Object value2, String condition, String andOr) {
            this.property = property;
            this.value = value1;
            this.secondValue = value2;
            this.condition = condition;
            this.andOr = andOr;
            this.valueType = Criteria.Criterion.ValueType.betweenValue;
        }

        public enum ValueType {
            noValue,
            singleValue,
            betweenValue,
            listValue;

            ValueType() {
            }
        }
    }

    @Data
    public static class EntityTable {
        private String tableName;
        private Map<String, String> fieldsMap;
        private Map<String, String> fieldsMapDisplay;
    }


    public static class SqlHelper {
        public static String selectColumns(Criteria.Statement statement) {
            StringBuilder selectColumns = new StringBuilder("select ");
            Criteria.EntityTable entityTable = entityTableCache.get(statement.clazz);
            //处理工具类生成多余的属性
            if (entityTable.getFieldsMap().containsKey("serialVersionUID")) {
                entityTable.getFieldsMap().remove("serialVersionUID");
            }
            if (statement.getFields() != null && statement.getFields().length > 0) {
                List<String> columns = new ArrayList();
                String[] fields = statement.fields;
                for (String field : fields) {
                    if (StringUtils.isNotBlank(entityTable.getFieldsMap().get(field))) {
                        columns.add(entityTable.getFieldsMap().get(field));
                    }
                }
                return selectColumns.append(String.join(",", columns)).toString();
            } else {

                Map<String, String> fieldsMap = entityTable.getFieldsMap();

                Iterator<Map.Entry<String, String>> iterator = fieldsMap.entrySet().iterator();
                List<String> columns = new LinkedList<>();
                while (iterator.hasNext()) {
                    columns.add(iterator.next().getValue());
                }
                return selectColumns.append(String.join(",", columns)).toString();
            }
        }

        public static String selectCount(Criteria.Statement statement) {
            return "select count(*)";
        }

        public static String fromTable(Criteria.Statement statement) {
            Criteria.EntityTable entityTable = entityTableCache.get(statement.clazz);
            return " from " + entityTable.tableName;
        }

        public static String orderByClause(Criteria.Statement statement) {
            if (statement.sortNames == null) {
                return "";
            } else {
                Criteria.EntityTable entityTable = Criteria.entityTableCache.get(statement.clazz);
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < statement.sortNames.length; ++i) {
                    if (i == 0) {
                        stringBuilder.append(" order by ");
                    }
                    //这段代码有问题 如果多个字段 这段代码完全失效，而且有点多余
                    // 我觉得用来校验更加好些  todo
                    stringBuilder.append(entityTable.getFieldsMap().get(statement.sortNames[i])).append(",");
                }

                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }

                if (statement.sortOrder != null) {
                    stringBuilder.append(" " + statement.sortOrder);
                }
                return stringBuilder.toString();
            }
        }

        public static String whereClause(Statement statement) {
            Criteria.EntityTable entityTable = Criteria.entityTableCache.get(statement.clazz);
            List<Criteria.Criterion> criterions = statement.criterions;
            StringBuilder resultSB = new StringBuilder();
            //这里谁创建，只能外部创建
            if (criterions.size() > 0) {
                for (Criteria.Criterion criterion : criterions) {
                    if (StringUtil.isEmpty(resultSB.toString())) {
                        resultSB.append(" where ");
                    } else {
                        resultSB.append(" " + criterion.andOr + " ");
                    }

                    if (criterion.valueType == Criteria.Criterion.ValueType.noValue) {
                        resultSB.append(entityTable.getFieldsMap().get(criterion.property) + " " + criterion.condition);
                    }

                    if (criterion.valueType == Criteria.Criterion.ValueType.singleValue) {
                        if (!(criterion.value instanceof Number) && !(criterion.value instanceof Boolean)) {
                            criterion.value = "'".concat(criterion.value.toString()).concat("'");
                        }
                        resultSB.append(entityTable.getFieldsMap().get(criterion.property) + " " + criterion.condition + " " + criterion.value);
                    }

                    if (criterion.valueType == Criteria.Criterion.ValueType.betweenValue) {
                        if (!(criterion.value instanceof Number) && !(criterion.value instanceof Boolean)) {
                            criterion.value = "'".concat(criterion.value.toString()).concat("'");
                        }

                        if (!(criterion.secondValue instanceof Number) && !(criterion.value instanceof Boolean)) {
                            criterion.secondValue = "'".concat(criterion.secondValue.toString()).concat("'");
                        }

                        resultSB.append(entityTable.getFieldsMap().get(criterion.property) + " " + criterion.condition + " " + criterion.value + " AND " + criterion.secondValue);
                    }

                    if (criterion.valueType == Criteria.Criterion.ValueType.listValue) {
                        Iterable iterable = (Iterable) criterion.value;
                        StringBuilder listItem = new StringBuilder();

                        Object o;
                        for (Iterator var8 = iterable.iterator(); var8.hasNext(); listItem.append(o).append(",")) {
                            o = var8.next();
                            if (!(o instanceof Number) && !(criterion.value instanceof Boolean)) {
                                o = "'".concat(o.toString()).concat("'");
                            }
                        }

                        if (listItem.length() > 0) {
                            listItem.deleteCharAt(listItem.length() - 1);
                        }
                        resultSB.append(entityTable.getFieldsMap().get(criterion.property) + " " + criterion.condition + " (" + listItem + ")");
                    }
                }
            }
            return resultSB.toString();
        }

        public static String limit(Statement statement) {
            if (statement.pageSize > 0) {
                Integer offset = statement.pageSize;
                if (statement.pageNum > 1) {
                    Integer limit = (statement.pageNum - 1) * statement.pageSize;
                    return " limit " + limit + "," + offset;
                } else {
                    return " limit " + offset;
                }
            } else {
                return "";
            }
        }
    }
}
