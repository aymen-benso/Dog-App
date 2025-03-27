package com.example.doggoo;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<model> items = new ArrayList<>();

    public void addItem(model item) {
        boolean itemExists = false;
        for (model existingItem : items) {
            if (existingItem.getName().equals(item.getName())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            items.add(item);
        }
    }

    public List<model> getItems() {
        return items;
    }


}
