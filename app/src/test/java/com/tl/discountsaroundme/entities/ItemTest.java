package com.tl.discountsaroundme.entities;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ItemTest {

    private Item item = new Item("Sweater", "Clothing",
            100, 25, "Description", "Picture", "Store");

    @Test
    public void getName() throws Exception {
        assertEquals("Sweater", item.getName());
    }

    @Test
    public void getType() throws Exception {
        assertEquals("Clothing", item.getType());
    }

    @Test
    public void getPrice() throws Exception {
        assertEquals(100.0, item.getPrice());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("Description", item.getDescription());
    }

    @Test
    public void getDiscount() throws Exception {
        assertEquals(25.0, item.getDiscount());
    }

    @Test
    public void getPicture() throws Exception {
        assertEquals("Picture", item.getPicture());
    }

    @Test
    public void getStore() throws Exception {
        assertEquals("Store", item.getStore());
    }

    @Test
    public void getFinalPrice() throws Exception {
        String priceString = "$" + item.getPrice();
        String discountString = String.valueOf(item.getDiscount());
        assertEquals("$75.00", item.getFinalPrice(priceString, discountString));
    }
}