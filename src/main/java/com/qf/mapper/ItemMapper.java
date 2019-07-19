package com.qf.mapper;

import com.qf.pojo.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {

    Long findCountByName(@Param("name")String name);

    List<Item> findItemByNameLikeAndLimit(@Param("name")String name,
                                          @Param("offset")Integer offset,
                                          @Param("size")Integer size);

    Integer save(Item item);
}
