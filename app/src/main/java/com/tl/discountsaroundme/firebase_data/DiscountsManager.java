package com.tl.discountsaroundme.firebase_data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.entities.Item;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DiscountsManager {
    private ItemViewAdapter adapter;
    private ArrayList<Item> allItems = new ArrayList<>();
    private ArrayList<Item> showingItems = new ArrayList<>();

    public void setAdapter(ItemViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void getTopDiscounts(int discountThreshold) {
        showingItems.clear();

        for (Item item : allItems) {
            if (item.getDiscount() >= discountThreshold)
                showingItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Invoked on search, based on the searchQuery it changes the list
     *
     * @param searchQuery the search String for discounts
     */
    public void getDiscountsByName(String searchQuery) {
        searchQuery = searchQuery.toUpperCase();
        showingItems.clear();

        for (Item item : allItems) {
            String name = item.getName().toUpperCase();
            if (name.contains(searchQuery))
                showingItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * @return the names of the discounted items
     */
    public ArrayList<String> getDiscountNames() {
        ArrayList<String> discountNamesList = new ArrayList<>();
        for (Item item : allItems)
            discountNamesList.add(item.getName());
        return discountNamesList;
    }

    /**
     * Checks the category of every item and changes accordingly
     * Invoked by the categories buttons onClickListener
     *
     * @param category the item category which is also the category button text
     */
    public void getDiscountsByCategory(String category) {
        category = category.toUpperCase();
        showingItems.clear();

        for (Item item : allItems) {
            String type = item.getType().toUpperCase();
            if (type.contains(category))
                showingItems.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Checks if all the items and returns a list of items based on the store they belong
     * Also it checks categoryItems
     *
     * @param store the store you want to receive it's items
     * @return an array of items belonging on the store
     */
    public ArrayList<Item> getTopDiscountsByStore(String store, int discountThreshold) {
        store = store.toUpperCase().trim();
        ArrayList<Item> storeItems = new ArrayList<>();

        for (Item item : allItems) {
            String storeName = item.getStore().toUpperCase().trim();
            if (store.equals(storeName) && item.getDiscount() >= discountThreshold)
                storeItems.add(item);
        }
        return storeItems;
    }

    public Item getTopItemByStore(String store) {
        double max = 0;
        Item topItem = null;

        for (Item item : allItems) {
            if (store.equals(item.getStore()) && item.getDiscount() >= max) {
                max = item.getDiscount();
                topItem = item;
            }
        }
        return topItem;
    }

    /**
     * @param firebaseDatabase FirebaseDatabase instance
     */
    public void fillListWithDiscounts(final FirebaseDatabase firebaseDatabase) {
        DatabaseReference mDBDiscountItems = firebaseDatabase.getReference("/items");

        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null) {
                        allItems.add(item);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * @param firebaseDatabase FirebaseDatabase instance
     */
    public void showTopDiscountsAndNotify(final FirebaseDatabase firebaseDatabase, final int discountThreshold, final String userId) {
        DatabaseReference mDBDiscountItems = firebaseDatabase.getReference("/items");

        mDBDiscountItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showingItems.clear();
                allItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null) {
                        allItems.add(item);
                        isItemInCart(firebaseDatabase, item, userId);

                        if (item.getDiscount() >= discountThreshold)
                            showingItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void isItemInCart(FirebaseDatabase firebaseDatabase, final Item item, String userId) {
        String dbPath = "/user_references/shopping_cart/" + userId;
        DatabaseReference shoppingCartRef = firebaseDatabase.getReference(dbPath);

        shoppingCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isInCart = dataSnapshot.hasChild(item.getId());
                item.setInCart(isInCart);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void sortItemsAlphabetically() {
        Collections.sort(showingItems, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return item1.getName().compareTo(item2.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortItemsByDiscount() {
        Collections.sort(showingItems, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return (int) (item2.getDiscount() - item1.getDiscount());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortItemsByPriceAsc() {
        Collections.sort(showingItems, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return (int) (item1.getPrice() - item2.getPrice());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void sortItemsByPriceDesc() {
        Collections.sort(showingItems, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return (int) (item2.getPrice() - item1.getPrice());
            }
        });
        adapter.notifyDataSetChanged();
    }

    void showTopDiscounts(ArrayList<Item> itemList) {
        allItems = itemList;
    }

    public ArrayList<Item> getShowingItems() {
        return showingItems;
    }

    public void changeDiscountThreshold(int threshold) {
        getTopDiscounts(threshold);
        adapter.notifyDataSetChanged();
    }
}
