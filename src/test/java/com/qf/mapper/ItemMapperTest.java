package com.qf.mapper;

import com.qf.AcTest;
import com.qf.pojo.Item;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ItemMapperTest extends AcTest {

    @Autowired
    private ItemMapper itemMapper;
    @Test
    public void findCountByName() {
        Long count = itemMapper.findCountByName(null);
        System.out.println(count);
    }

    @Test
    public void findItemByNameLikeAndLimit() {
        List<Item> list = itemMapper.findItemByNameLikeAndLimit(null, 0, 5);

        for (Item item : list) {
            System.out.println(item);
        }
    }

    @Test
    @Transactional
    public void save(){
        Item item = new Item();
        item.setName("xxxx");
        item.setPrice(new BigDecimal(1.1));
        item.setProductionDate(new Date());
        item.setDescription("yyyyyyyyyyyyy");
        item.setPic("zzzzzzzzzzzz");

        Integer count = itemMapper.save(item);

        assertEquals(new Integer(1),count);
    }

}