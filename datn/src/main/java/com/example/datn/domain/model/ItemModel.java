package com.example.datn.domain.model;

import com.example.datn.domain.entity.Item;
import com.example.datn.domain.entity.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemModel {
    long itemID;
    Type type;
    String size;
    int quantity;

    public static ItemModel convertFromItem(Item item, Type type) {
        ItemModel itemModel = new ItemModel();
        itemModel.setItemID(item.getItemID());
        itemModel.setType(type);
        itemModel.setSize(item.getSize());
        itemModel.setQuantity(item.getQuantity());
        return itemModel;
    }
}
