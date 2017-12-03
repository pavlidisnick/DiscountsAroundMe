package com.tl.discountsaroundme.firebase_data;

import com.google.firebase.database.FirebaseDatabase;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FirebaseDatabase.class)
public class DiscountsManagerTest {

    private DiscountsManager discountsManager = new DiscountsManager();

    @Before
    public void setup() throws Exception {
        ArrayList<Item> itemList = new ArrayList<Item>() {{
            new Item("abc", "Clothing", 23, 35, "Empty", "Empty", "abcStore");
            new Item("cde", "Technology", 23, 40, "Empty", "Empty", "abcStore");
            new Item("qwerty", "Food", 23, 45, "Empty", "Empty", "cdeStore");
        }};
        discountsManager.showTopDiscounts(itemList);

        ItemViewAdapter itemViewAdapter = mock(ItemViewAdapter.class);
        discountsManager.setAdapter(itemViewAdapter);
    }

//    @Test
//    public void getTopDiscounts() throws Exception {
//        discountsManager.getTopDiscounts(40);
//
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//        for (Item item : itemList) {
//            assert item.getDiscount() >= 40;
//        }
//    }
//
//    @Test
//    public void getTopDiscounts1() throws Exception {
//        discountsManager.getTopDiscounts(41);
//
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//        assert itemList.get(0).getType().equals("Food");
//    }
//
//    @Test
//    public void getTopDiscounts2() throws Exception {
//        discountsManager.getTopDiscounts(45);
//
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//        assert itemList.get(0).getType().equals("Food");
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void getTopDiscounts3() throws Exception {
//        discountsManager.getTopDiscounts(46);
//
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//        itemList.get(0);
//    }
//
//    @Test
//    public void getDiscountsByName1() throws Exception {
//        discountsManager.getDiscountsByName("abc");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        assert itemList.get(0).getName().equals("abc");
//        assert itemList.get(0).getName().equals("cde");
//    }
//
//    @Test
//    public void getDiscountsByName2() throws Exception {
//        discountsManager.getDiscountsByName("c");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        assert itemList.get(0).getName().equals("abc");
//        assert itemList.get(0).getName().equals("cde");
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void getDiscountsByName3() throws Exception {
//        discountsManager.getDiscountsByName("f");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        itemList.get(0);
//    }
//
//    @Test
//    public void getDiscountsByName4() throws Exception {
//        discountsManager.getDiscountsByName("er");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        assert itemList.get(0).getName().equals("qwerty");
//    }

    @Test
    public void getDiscountNames() throws Exception {
        ArrayList<String> list = discountsManager.getDiscountNames();
        assert list.get(0).equals("abc");
        assert list.get(0).equals("cde");
        assert list.get(0).equals("qwerty");
    }

//    @Test
//    public void getDiscountsByCategory1() throws Exception {
//        discountsManager.getDiscountsByCategory("g");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        assert itemList.get(0).getType().equals("Clothing");
//        assert itemList.get(0).getType().equals("Technology");
//    }
//
//    @Test
//    public void getDiscountsByCategory2() throws Exception {
//        discountsManager.getDiscountsByCategory("o");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        assert itemList.get(0).getType().equals("Clothing");
//        assert itemList.get(0).getType().equals("Technology");
//        assert itemList.get(0).getType().equals("Food");
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void getDiscountsByCategory3() throws Exception {
//        discountsManager.getDiscountsByCategory("w");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        itemList.get(0);
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void getDiscountsByCategory4() throws Exception {
//        discountsManager.getDiscountsByCategory("ty");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        itemList.get(0);
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void getDiscountsByCategory5() throws Exception {
//        discountsManager.getDiscountsByCategory("t  y");
//        ArrayList<Item> itemList = discountsManager.getShowingItems();
//
//        itemList.get(0);
//    }

    @Test
    public void getTopDiscountsByStore1() throws Exception {
        ArrayList<Item> itemList = discountsManager.getTopDiscountsByStore("abcStore", 40);

        assert itemList.get(0).getDiscount() == 40;
    }

    @Test
    public void getTopDiscountsByStore2() throws Exception {
        ArrayList<Item> itemList = discountsManager.getTopDiscountsByStore("abcStore", 40);

        assert itemList.get(0).getDiscount() != 35;
    }

    @Test
    public void getTopDiscountsByStore3() throws Exception {
        ArrayList<Item> itemList = discountsManager.getTopDiscountsByStore("abcStore", 45);

        assert itemList.get(0) == null;
    }

    @Test
    public void getTopDiscountsByStore4() throws Exception {
        ArrayList<Item> itemList = discountsManager.getTopDiscountsByStore("Store", 30);

        assert itemList.get(0) == null;
    }

    @Test
    public void getTopDiscountsByStore5() throws Exception {
        ArrayList<Item> itemList = discountsManager.getTopDiscountsByStore("abcStore", 30);

        assert itemList.get(0).getDiscount() == 40;
    }

    @Test
    public void getTopItemByStore() throws Exception {
        Item item = discountsManager.getTopItemByStore("abcStore");

        assert item.getDiscount() == 40;
    }
}