package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.sale.Sale;

public class ItemSaleRepository {
    private ItemRepository itemRepository = ItemRepository.getInstance();
    private  static ItemSaleRepository itemSaleRepository = new ItemSaleRepository();
    private SaleRepository saleRepository = SaleRepository.getInstance();

    private ItemSaleRepository() {
    }

    public static ItemSaleRepository getInstance(){
        return itemSaleRepository;
    }

    public void removeItemFromSales(Item item){
        saleRepository.getSales().removeIf(sale -> sale.getItem().equals(item));
    }

    public void updateItemInSales(Item old,Item item){
        Sale sale = saleRepository.getSales().stream().filter(sale1 -> sale1.getItem().equals(old)).findAny().get();
        sale.setItem(item);
    }

    public Item getItem(int id){
        return itemRepository.getItems().stream().filter(item1 -> item1.getId() == id).findAny().get();
    }
}
