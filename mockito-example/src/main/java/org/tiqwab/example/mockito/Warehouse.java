package org.tiqwab.example.mockito;

public interface Warehouse {
	
	public int getInventory(String sku);
	public void remove(String sku, int qty);
	
}
