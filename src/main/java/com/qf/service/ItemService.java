package com.qf.service;

import com.qf.pojo.Item;
import com.qf.util.PageInfo;

public interface ItemService {
    PageInfo<Item> findItemByNameLikeAndLimit(String name, Integer page, Integer size);
    Integer save(Item item);
}
