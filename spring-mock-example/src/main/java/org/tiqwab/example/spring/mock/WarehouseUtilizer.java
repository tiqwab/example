package org.tiqwab.example.spring.mock;

import lombok.Getter;
import lombok.Setter;

public class WarehouseUtilizer {

	@Getter
	@Setter
	private Warehouse warehouse;
	
	public int getInventory(String sku) {
		return warehouse.getInventory(sku);
	}
	
}
