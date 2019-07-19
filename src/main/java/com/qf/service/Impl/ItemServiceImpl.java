package com.qf.service.Impl;

import com.qf.mapper.ItemMapper;
import com.qf.pojo.Item;
import com.qf.service.ItemService;
import com.qf.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    public PageInfo<Item> findItemByNameLikeAndLimit(String name, Integer page, Integer size) {
        //将PageInfo创建出来,并且封装list集合,再返回
        //1. 查询数据总条数.
        Long total = itemMapper.findCountByName(name);
        //2. 创建pageInfo对象.
        PageInfo<Item> pageInfo = new PageInfo<Item>(page,size,total);
        //3. 查询商品信息list.
        List<Item> list = itemMapper.findItemByNameLikeAndLimit(name, pageInfo.getOffset(), pageInfo.getSize());
        //4. 将list集合set到PageInfo中.
        pageInfo.setList(list);
        //5. 返回
        return pageInfo;
    }
    @Transactional
    public Integer save(Item item) {
        return itemMapper.save(item);
    }
}
