package org.tiqwab.example.mockito;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.Before;
import org.junit.Test;

public class OrderStateTest {
	
	private static final String TALISKER = "tarisker";
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testOrderIsFilledIfEnoughInWarehouse() throws Exception {
		// setup
		Warehouse warehouse = mock(Warehouse.class);
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
		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getInventory(TALISKER)).thenReturn(10);
		Order order = new Order(TALISKER, 20);
		
		// execute
		order.fill(warehouse);
		
		// verify
		assertThat(order.isFilled(), is(false));
	}

}
