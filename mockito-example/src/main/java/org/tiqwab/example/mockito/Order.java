package org.tiqwab.example.mockito;

import lombok.Getter;

public class Order {

	@Getter
	private String sku;
	@Getter
	private int qty;
	@Getter
	private boolean isFilled;
	
	public Order(String sku, int qty) {
		this.sku = sku;
		this.qty = qty;
		this.isFilled = false;
	}
	
	public void fill(Warehouse warehouse) {
		int inventory = warehouse.getInventory(this.sku);
		if (this.qty <= inventory) {
			this.isFilled = true;
			warehouse.remove(this.sku, this.qty);
		}
	}
	
}
