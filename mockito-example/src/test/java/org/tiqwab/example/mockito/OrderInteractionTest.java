package org.tiqwab.example.mockito;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.Before;
import org.junit.Test;

public class OrderInteractionTest {

	private static final String TALISKER = "tarisker";
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testFillingRemovesInventoryIfInStock() throws Exception {
		// setup
		Order order = new Order(TALISKER, 20);
		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getInventory(TALISKER)).thenReturn(50);
		
		// execute
		order.fill(warehouse);
		
		// verify
		assertThat(order.isFilled(), is(true));
		verify(warehouse, times(1)).getInventory(TALISKER);
		verify(warehouse, times(1)).remove(TALISKER, 20);
	}
	
	@Test
	public void testFillingDoesNotRemoveIfNotEnoughInStock() throws Exception {
		// setup
		Order order = new Order(TALISKER, 20);
		Warehouse warehouse = mock(Warehouse.class);
		when(warehouse.getInventory(TALISKER)).thenReturn(10);
		
		// execute
		order.fill(warehouse);
		
		// verify
		assertThat(order.isFilled(), is(false));
		verify(warehouse, times(1)).getInventory(TALISKER);
		verify(warehouse, never()).remove(eq(TALISKER), anyInt());
	}
	
}
