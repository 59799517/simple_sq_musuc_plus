package com.sqmusicplus.config.webconfig.base;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqmusicplus.config.webconfig.entity.SymbolPlus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 通用方法
 *
 * @author SQ
 */
public class SQBaseController<E, S>  {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public S sService;

    /**
     * 通用添加方法
     *
     * @param entity 添加对象
     */
    public boolean add(E entity) {
        return ((IService) sService).save(entity);
    }

    /**
     * 根据主键删除方法
     *
     * @param id 主键id
     */
    public boolean del(Integer id) {
        return ((IService) sService).removeById(id);
    }

    public boolean del(String uuid) {
        return ((IService) sService).removeById(uuid);
    }

    /**
     * 根据主键删除方法(批量)
     *
     * @param ids 主键id
     */
    public boolean del_by_id_list(List<Integer> ids) {
        return ((IService) sService).removeByIds(ids);
    }

    public boolean del(QueryWrapper<E> queryWrapper) {
        return ((IService) sService).remove(queryWrapper);
    }

    public boolean del(Map map) {
        SymbolPlus symbolPlus = BaseSelectSymbol.select_Analysis(map);
        //不允许删除全部表格
        if (symbolPlus.getQueryWrapper().isEmptyOfWhere()) {
            return false;
        }
        return ((IService) sService).remove(symbolPlus.getQueryWrapper());
    }


    /**
     * 更新方法 根据主键
     *
     * @param entity 更改后的对象
     */
    public boolean update(E entity) {
        boolean b = ((IService) sService).updateById(entity);
        return b;
    }

    public boolean update(UpdateWrapper<E> updateWrapper) {
        boolean b = ((IService) sService).update(updateWrapper);
        return b;
    }

    public boolean update(HashMap<String, String> map) {
        UpdateWrapper updateWrapper = BaseSelectSymbol.update_Analysis(map);
        return update(updateWrapper);
    }

    /**
     * 根据主键更新
     *
     * @param entityList
     * @return
     */
    public boolean base_list_update_by_id(List<E> entityList) {
        boolean b = ((IService) sService).updateBatchById(entityList);
        return b;
    }

    /**
     * 使用生成自动修改
     *
     * @param updateWrapper
     * @return
     */
    public boolean base_list_update_by_id(UpdateWrapper<E> updateWrapper) {
        boolean update = ((IService) sService).update(updateWrapper);
        return update;
    }

    /**
     * 逻辑删除 暂停使用以前的保持和现在一致
     *
     * @param idlist id列表
     * @return
     */
    @Deprecated
    public boolean base_list_false_delete_by_id(List idlist) {
//        UpdateWrapper<T> tUpdateWrapper = new UpdateWrapper<>();
//        tUpdateWrapper.eq(true,"flag",0);
        boolean b = ((IService) sService).removeByIds(idlist);
        return b;
    }

    /**
     * @param pageindex 那一夜
     * @param pagesize  每页长度
     * @param ascs      正序字段,分割可多个
     * @param desc      正序字段,分割可多个
     * @return
     */
    public IPage<E> base_get_list_by_page(Long pageindex, Long pagesize, String ascs, String desc) {

        String[] ascs_array = null;
        String[] desc_array = null;
        if (ascs != null && ascs.equals("")) {
            ascs_array = ascs.split(",");
        }
        if (desc != null && desc.equals("")) {
            desc_array = desc.split(",");
        }

        Page<E> pageinfo = new Page<>(pageindex, pagesize);
        if (ascs_array != null) {
            pageinfo.addOrder(OrderItem.ascs(ascs_array));
        }
        if (desc_array != null) {
            pageinfo.addOrder(OrderItem.descs(desc_array));
        }
        IPage page = ((IService) sService).page(pageinfo, new QueryWrapper<E>());
        List records = page.getRecords();
        System.out.println(records);
        return page;
    }


    /**
     * @param queryWrapper 条件构造器
     * @param pageindex    那一夜
     * @param pagesize     每页长度
     * @param ascs         正序字段,分割可多个
     * @param desc         正序字段,分割可多个
     */
    public IPage<E> base_get_list_by_page(QueryWrapper queryWrapper, Long pageindex, Long pagesize, String ascs, String desc) {
        String[] ascs_array = null;
        String[] desc_array = null;


        if (ascs != null && !ascs.equals("")) {
            ascs_array = ascs.split(",");
        }
        if (desc != null && !desc.equals("")) {
            desc_array = desc.split(",");
        }
        if (ascs_array != null) {
            queryWrapper.orderByAsc(ascs_array);
        }
        if (desc_array != null) {
            queryWrapper.orderByDesc(desc_array);
        }
        IPage page;
        //查询全部的
        if (pagesize == -1) {
            List list = ((IService) sService).list(queryWrapper);
            page = new Page();
            page.setTotal(list.size()).setRecords(list).setSize(list.size()).setPages(1).setCurrent(1);
        } else {
            Page<E> pageinfo = new Page<>(pageindex, pagesize);
            page = ((IService) sService).page(pageinfo, queryWrapper);

        }
        return page;

    }

    public IPage<E> base_get_list_by_page(SymbolPlus symbolPlus) {
        return this.base_get_list_by_page(symbolPlus.getQueryWrapper(), symbolPlus.getPageindex(), symbolPlus.getPagesize(), symbolPlus.getAscs(), symbolPlus.getDesc());
    }

    public IPage<E> base_get_list_by_page(HashMap map) throws RuntimeException {
        try {
            SymbolPlus symbolPlus = BaseSelectSymbol.select_Analysis(map);
            IPage<E> IPage = base_get_list_by_page(symbolPlus);
            return IPage;
        } catch (Exception e) {
            throw new RuntimeException("请检查字段是否正确");
        }
    }
//    /**
//     * 通用的对外方法
//     *
//     * @param map
//     * @return
//     */
//    public ResponseBean del_by_id(Map<String, String> map) {
//
//        boolean id = del(Integer.parseInt(map.get("id")));
//        if (id) {
//            return new ResponseBean(0, "Success", id);
//        }
//        return new ResponseBean(803, "Specified object cannot be found", false);
//    }

    public S getsService() {
        return sService;
    }

    public void setsService(S sService) {
        this.sService = sService;
    }

    /**
     * 根据条件产生图表所需要的数据
     *
     * @param queryWrapper
     * @return
     */
    public Map<String, List<Object>> chart(QueryWrapper queryWrapper, String ascs, String desc, String field) {
        Map<String, List<Object>> rest = new HashMap<String, List<Object>>();
        String[] ascs_array = null;
        String[] desc_array = null;

        if (ascs != null && !ascs.equals("")) {
            ascs_array = ascs.split(",");
        }
        if (desc != null && !desc.equals("")) {
            desc_array = desc.split(",");
        }


        if (ascs_array != null) {
            queryWrapper.orderByAsc(ascs_array);
        }
        if (desc_array != null) {
            queryWrapper.orderByDesc(desc_array);
        }
        List<Map<String, Object>> list = ((IService) sService).listMaps(queryWrapper);
        //只查询指定字段
        Set<String> keySet = null;
        if (field != null && !field.equals("") && field.length() > 1) {
            String[] split = field.split(",");
            keySet = new HashSet<>(Arrays.asList(split));

        } else {
            keySet = list.get(0).keySet();

        }
        //查询全部字段 @第一种方法
//        for (String s : keySet) {
//            ArrayList<Object> templist = new ArrayList<>();
//            for (Map<String, Object> stringObjectMap : list) {
//                templist.add(stringObjectMap.get(s));
//            }
//            rest.put(s,templist);
//
//        }
        //第二种
        for (Map<String, Object> stringObjectMap : list) {
            for (String s : keySet) {
                List<Object> objects = null;
                if (rest.get(s) == null) {
                    objects = new ArrayList<>();
                } else {
                    objects = rest.get(s);
                }
                objects.add(stringObjectMap.get(s));
                rest.put(s, objects);

//                if (rest.get(s)!=null){
//                    List<Object> objects = rest.get(s);
//                    objects.add(stringObjectMap.get(s));
//                    rest.put(s,objects);
//                }else{
//                    ArrayList<Object> objects = new ArrayList<>();
//                    objects.add(stringObjectMap.get(s));
//                    rest.put(s,objects);
//                }
            }
        }
        return rest;
    }

    /**
     * @param symbolPlus
     * @return
     */

    public Map<String, List<Object>> chart(SymbolPlus symbolPlus) {
        return this.chart(symbolPlus.getQueryWrapper(), symbolPlus.getAscs(), symbolPlus.getDesc(), symbolPlus.getField());
    }

    public QueryWrapper<E> createQueryWrapper() {
        return new QueryWrapper<E>();
    }

    public UpdateWrapper<E> createUpdateWrapper() {
        return new UpdateWrapper<E>();
    }



}
