package org.tiqwab.example.spring.mock;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tiqwab.example.spring.mock.bean.WarehouseAutowired;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MockExampleTest {

	@Configuration
	@ComponentScan
	public static class MockExampleConfig {
		@Mock
		private Warehouse warehouse;
		
		public MockExampleConfig() {
			MockitoAnnotations.initMocks(this);
		}
		
		// Definition of mock bean
		@Bean
		public Warehouse warehouse() {
			return warehouse;
		}
		
		// Definition of bean utilizing mock object
		@Bean
		public WarehouseUtilizer warehouseUtilizer() {
			WarehouseUtilizer utilizer = new WarehouseUtilizer();
			utilizer.setWarehouse(warehouse());
			return utilizer;
		}
	}
	
	private static final String TALISKER = "tarisker";
	@Autowired Warehouse warehouse;
	@Autowired WarehouseUtilizer utilizer;
	@Autowired WarehouseAutowired autowired;
	
	@Test
	public void testOrderIsFilledIfEnoughInWarehouse() throws Exception {
		// setup
		when(warehouse.getInventory(TALISKER)).thenReturn(50);
		Order order = new Order(TALISKER, 20);
		// execute
		order.fill(warehouse);
		// verify
		assertThat(order.isFilled(), is(true));
	}
	
	@Test
	public void testOrderDoesNotRemoveIfNotEnoughInWarehouse() throws Exception {
		// setup
		when(warehouse.getInventory(TALISKER)).thenReturn(10);
		Order order = new Order(TALISKER, 20);
		// execute
		order.fill(warehouse);
		// verify
		assertThat(order.isFilled(), is(false));
	}
	
	@Test
	public void testUtilizer() throws Exception {
		when(warehouse.getInventory(TALISKER)).thenReturn(10);
		assertThat(utilizer.getInventory(TALISKER), is(10));
	}
	
	@Test
	public void testAutowired() throws Exception {
		when(warehouse.getInventory(TALISKER)).thenReturn(10);
		assertThat(autowired.getInventory(TALISKER), is(10));
	}
	
}
