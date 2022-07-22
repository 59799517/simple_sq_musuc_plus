package com.sqmusicplus.config.webconfig.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sqmusicplus.config.webconfig.entity.SymbolPlus;
import com.sqmusicplus.utils.DateUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.Timestamp;
import java.util.Map;

/**
 * 自动生成查询对象
 *
 * @param <T>
 */
public class BaseSelectSymbol<T> {
    //全like查询
    public static final String LIKE = "%";
    //相等查询
    public static final String EQUAL = "e";
    //大于
    public static final String BE_MORE_THAN = ">";
    //小于
    public static final String BE_LESS_THAN = "<";
    //大于等于
    public static final String BE_MORE_THAN_EQUAL = ">=";
    //小于等于
    public static final String BE_LESS_THAN_EQUAL = "<=";
    //区间
    public static final String BETWEEN = "b";
    //不在区间
    public static final String NOT_BETWEEN = "nb";
    //前like查询
    public static final String BEFORE_LIKE = "%-";
    //后like查询
    public static final String END_LIKE = "-%";
    //in查询
    public static final String IN = "i";
    //not in查询
    public static final String NOT_IN = "ni";
    //not null 查询
    public static final String NOT_NULL = "nn";
    //不等于
    public static final String NOT_EQUAL = "ne";
    //not Like
    public static final String NOT_LIKE = "n%";
    //前not Like
    public static final String BEFORE_NOT_LIKE = "n%-";
    //后not Like
    public static final String END_NOT_LIKE = "n-%";


    /**
     * 接收json映射后map的数值
     * 默认{“字段名”：“参数值”}是使用相等查询
     * in或者not in{“字段名|i”：“参数值1,参数值2,参数值3”}
     * 普通{“字段名|查询方法”：“参数值”}
     * 时间段或者数值段查询{“字段名|b”：“参数值1|参数值2”}
     *
     * @param code
     */
    public static QueryWrapper Analysis(Map code) {
        QueryWrapper tQueryWrapper = new QueryWrapper();
        return Analysis(code, tQueryWrapper);
    }

    public static QueryWrapper Analysis(Map code, QueryWrapper queryWrapper) {
        for (Object o : code.keySet()) {
            String key = o.toString();
            String[] split = key.split("\\|");
            if (split[0].equalsIgnoreCase("pageindex") || split[0].equalsIgnoreCase("pagesize") || split[0].equalsIgnoreCase("ascs") || split[0].equalsIgnoreCase("desc")) {
                continue;
            }
            if (isEmpty(code.get(key).toString())) {
                continue;
            }

            if (split.length > 1) {
                //参数值为空的跳过
                switch (split[1]) {
                    case LIKE:
                        queryWrapper.like(split[0], code.get(key));
                        break;
                    case EQUAL:
                        queryWrapper.eq(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN:
                        queryWrapper.gt(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN:
                        queryWrapper.lt(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN_EQUAL:
                        queryWrapper.ge(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN_EQUAL:
                        queryWrapper.le(split[0], code.get(key));
                        break;
                    case BETWEEN:
                        String[] split1 = code.get(key).toString().split("\\|");
                        queryWrapper.between(split[0], split1[0], split1[1]);
                        break;
                    case NOT_BETWEEN:
                        String[] split2 = code.get(key).toString().split("\\|");
                        queryWrapper.notBetween(split[0], split2[0], split2[1]);
                        break;
                    case BEFORE_LIKE:
                        queryWrapper.likeLeft(split[0], code.get(key));
                        break;
                    case END_LIKE:
                        queryWrapper.likeRight(split[0], code.get(key));
                        break;
                    case IN:
                        queryWrapper.in(split[0], code.get(key).toString().split(","));
                        break;
                    case NOT_IN:
                        queryWrapper.notIn(split[0], code.get(key).toString().split(","));
                        break;
                    case NOT_NULL:
                        queryWrapper.isNotNull(split[0]);
                        break;
                    case NOT_EQUAL:
                        queryWrapper.ne(split[0], code.get(key));
                        break;
                    case NOT_LIKE:
                        queryWrapper.notLike(split[0], code.get(key));
                        break;
                    case BEFORE_NOT_LIKE:
                        queryWrapper.notLike(split[0], "%" + code.get(key));
                        break;
                    case END_NOT_LIKE:
                        queryWrapper.notLike(split[0], code.get(key) + "%");
                        break;
                    default:
                        throw new RuntimeException("json parameter error(传递字符串错误)");
                }
            } else {

                queryWrapper.eq(key, code.get(key));
            }
        }
        return queryWrapper;
    }

    /**
     * 通用修改对象生成(没有默认比较方法)
     * 使用方法（字段名称后带|则为查询条件不带则为修改条件）
     * {
     * “id|e”：“2”，
     * “name”:"新名字“
     * }
     *
     * @param code
     * @return
     */
    public static UpdateWrapper update_Analysis(Map code) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        for (Object o : code.keySet()) {
            String key = o.toString();
            String[] split = key.split("\\|");
            if (isEmpty(code.get(key).toString())) {
                continue;
            }
            if (split.length > 1) {
                switch (split[1]) {
                    case LIKE:
                        updateWrapper.like(split[0], code.get(key));
                        break;
                    case EQUAL:
                        updateWrapper.eq(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN:
                        updateWrapper.gt(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN:
                        updateWrapper.lt(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN_EQUAL:
                        updateWrapper.ge(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN_EQUAL:
                        updateWrapper.le(split[0], code.get(key));
                        break;
                    case BETWEEN:
                        String[] split1 = code.get(key).toString().split("\\|");
                        updateWrapper.between(split[0], split1[0], split1[1]);
                        break;
                    case NOT_BETWEEN:
                        String[] split2 = code.get(key).toString().split("\\|");
                        updateWrapper.notBetween(split[0], split2[0], split2[1]);
                        break;
                    case BEFORE_LIKE:
                        updateWrapper.likeLeft(split[0], code.get(key));
                        break;
                    case END_LIKE:
                        updateWrapper.likeRight(split[0], code.get(key));
                        break;
                    case IN:
                        updateWrapper.in(split[0], code.get(key));
                        break;
                    case NOT_IN:
                        updateWrapper.notIn(split[0], code.get(key));
                        break;
                    case NOT_NULL:
                        updateWrapper.isNotNull(split[0]);
                        break;
                    case NOT_EQUAL:
                        updateWrapper.ne(split[0], code.get(key));
                        break;
                    case NOT_LIKE:
                        updateWrapper.notLike(split[0], code.get(key));
                        break;
                    case BEFORE_NOT_LIKE:
                        updateWrapper.notLike(split[0], "%" + code.get(key));
                        break;
                    case END_NOT_LIKE:
                        updateWrapper.notLike(split[0], code.get(key) + "%");
                        break;
                    default:
                        throw new RuntimeException("json parameter error(传递字符串错误)");
                }

            } else {
                updateWrapper.set(key, code.get(key));
            }


        }

        return updateWrapper;
    }


    public static SymbolPlus select_Analysis(Map code) {
        QueryWrapper tQueryWrapper = new QueryWrapper();
        SymbolPlus symbolPlus = new SymbolPlus();
        for (Object o : code.keySet()) {
            String key = o.toString();
            String[] split = key.split("\\|");
            if (split[0].equalsIgnoreCase("pageindex") || split[0].equalsIgnoreCase("pagesize") || split[0].equalsIgnoreCase("ascs") || split[0].equalsIgnoreCase("desc")) {
                if (split[0].equalsIgnoreCase("pageindex")) {
                    symbolPlus.setPageindex(Long.parseLong(code.get(split[0]).toString()));
                } else if (split[0].equalsIgnoreCase("pagesize")) {
                    symbolPlus.setPagesize(Long.parseLong(code.get(split[0]).toString()));
                } else if (split[0].equalsIgnoreCase("ascs")) {
                    symbolPlus.setAscs(code.get(split[0]).toString());
                } else if (split[0].equalsIgnoreCase("desc")) {
                    symbolPlus.setDesc(code.get(split[0]).toString());
                }
                continue;
            }
            if (isEmpty(code.get(key).toString())) {
                continue;
            }
            if (NumberUtils.isNumber(code.get(key).toString())) {
                code.put(key, Integer.parseInt(code.get(key).toString()));
            }
            if (split.length > 1) {
                //参数值为空的跳过
                switch (split[1]) {
                    case LIKE:
                        tQueryWrapper.like(split[0], code.get(key));
                        break;
                    case EQUAL:
                        tQueryWrapper.eq(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN:
                        tQueryWrapper.gt(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN:
                        tQueryWrapper.lt(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN_EQUAL:
                        tQueryWrapper.ge(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN_EQUAL:
                        tQueryWrapper.le(split[0], code.get(key));
                        break;
                    case BETWEEN:
                        String[] split1 = code.get(key).toString().split("\\|");
                        tQueryWrapper.between(split[0], new Timestamp((DateUtils.parseDate(split1[0]).getTime())), new Timestamp((DateUtils.parseDate(split1[1]).getTime())));
                        break;
                    case NOT_BETWEEN:
                        String[] split2 = code.get(key).toString().split("\\|");
                        tQueryWrapper.notBetween(split[0], split2[0], split2[1]);
                        break;
                    case BEFORE_LIKE:
                        tQueryWrapper.likeLeft(split[0], code.get(key));
                        break;
                    case END_LIKE:
                        tQueryWrapper.likeRight(split[0], code.get(key));
                        break;
                    case IN:
                        tQueryWrapper.in(split[0], code.get(key).toString().split(","));
                        break;
                    case NOT_IN:
                        tQueryWrapper.notIn(split[0], code.get(key).toString().split(","));
                        break;
                    case NOT_NULL:
                        tQueryWrapper.isNotNull(split[0]);
                        break;
                    case NOT_EQUAL:
                        tQueryWrapper.ne(split[0], code.get(key));
                        break;
                    case NOT_LIKE:
                        tQueryWrapper.notLike(split[0], code.get(key));
                        break;
                    case BEFORE_NOT_LIKE:
                        tQueryWrapper.notLike(split[0], "%" + code.get(key));
                        break;
                    case END_NOT_LIKE:
                        tQueryWrapper.notLike(split[0], code.get(key) + "%");
                        break;
                    default:
                        throw new RuntimeException("json parameter error(传递字符串错误)");
                }
            } else {
                tQueryWrapper.eq(key, code.get(key));
            }
        }
        symbolPlus.setQueryWrapper(tQueryWrapper);
        return symbolPlus;
    }

    /**
     * 图表查询对象生成
     *
     * @param code
     * @return
     */
    public static SymbolPlus chart_Analysis(Map code) {
        QueryWrapper tQueryWrapper = new QueryWrapper();
        SymbolPlus symbolPlus = new SymbolPlus();
        for (Object o : code.keySet()) {
            String key = o.toString();
            String[] split = key.split("\\|");
            if (split[0].equalsIgnoreCase("field") || split[0].equalsIgnoreCase("ascs") || split[0].equalsIgnoreCase("desc")) {
                if (split[0].equalsIgnoreCase("field")) {
                    symbolPlus.setField(code.get(split[0]).toString());
                } else if (split[0].equalsIgnoreCase("ascs")) {
                    symbolPlus.setAscs(code.get(split[0]).toString());
                } else if (split[0].equalsIgnoreCase("desc")) {
                    symbolPlus.setDesc(code.get(split[0]).toString());
                }
                continue;
            }
            if (isEmpty(code.get(key).toString())) {
                continue;
            }

            if (split.length > 1) {
                //参数值为空的跳过
                switch (split[1]) {
                    case LIKE:
                        tQueryWrapper.like(split[0], code.get(key));
                        break;
                    case EQUAL:
                        tQueryWrapper.eq(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN:
                        tQueryWrapper.gt(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN:
                        tQueryWrapper.lt(split[0], code.get(key));
                        break;
                    case BE_MORE_THAN_EQUAL:
                        tQueryWrapper.ge(split[0], code.get(key));
                        break;
                    case BE_LESS_THAN_EQUAL:
                        tQueryWrapper.le(split[0], code.get(key));
                        break;
                    case BETWEEN:
                        String[] split1 = code.get(key).toString().split("\\|");
                        tQueryWrapper.between(split[0], split1[0], split1[1]);
                        break;
                    case NOT_BETWEEN:
                        String[] split2 = code.get(key).toString().split("\\|");
                        tQueryWrapper.notBetween(split[0], split2[0], split2[1]);
                        break;
                    case BEFORE_LIKE:
                        tQueryWrapper.likeLeft(split[0], code.get(key));
                        break;
                    case END_LIKE:
                        tQueryWrapper.likeRight(split[0], code.get(key));
                        break;
                    case IN:
                        tQueryWrapper.in(split[0], code.get(key).toString().split(","));
                        break;
                    case NOT_IN:
                        tQueryWrapper.notIn(split[0], code.get(key).toString().split(","));
                        break;
                    case NOT_NULL:
                        tQueryWrapper.isNotNull(split[0]);
                        break;
                    case NOT_EQUAL:
                        tQueryWrapper.ne(split[0], code.get(key));
                        break;
                    case NOT_LIKE:
                        tQueryWrapper.notLike(split[0], code.get(key));
                        break;
                    case BEFORE_NOT_LIKE:
                        tQueryWrapper.notLike(split[0], "%" + code.get(key));
                        break;
                    case END_NOT_LIKE:
                        tQueryWrapper.notLike(split[0], code.get(key) + "%");
                        break;
                    default:
                        throw new RuntimeException("json parameter error(传递字符串错误)");
                }
            } else {
                tQueryWrapper.eq(key, code.get(key));
            }
        }
        symbolPlus.setQueryWrapper(tQueryWrapper);
        return symbolPlus;
    }


    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

}
