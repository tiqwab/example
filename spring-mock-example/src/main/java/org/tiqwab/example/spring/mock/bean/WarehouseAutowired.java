package org.tiqwab.example.spring.mock.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tiqwab.example.spring.mock.Warehouse;

@Component
public class WarehouseAutowired {

	@Autowired
	private Warehouse warehouse;
	
	public int getInventory(String sku) {
		return warehouse.getInventory(sku);
	}
	
}
