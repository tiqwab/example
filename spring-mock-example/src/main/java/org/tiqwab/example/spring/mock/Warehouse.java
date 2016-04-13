package org.tiqwab.example.spring.mock;

public interface Warehouse {
	
	public int getInventory(String sku);
	public void remove(String sku, int qty);
	
}
