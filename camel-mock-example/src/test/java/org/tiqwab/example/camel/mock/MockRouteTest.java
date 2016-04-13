package org.tiqwab.example.camel.mock;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;

import org.tiqwab.example.camel.mock.MockRouteTest.MockBodySetterBean;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.Beans;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CamelCdiRunner.class)
@Beans(alternatives=MockBodySetterBean.class)
public class MockRouteTest {
	
	@EndpointInject(uri="mock:result")
	protected MockEndpoint resultEndpoint;
	@Produce(uri="direct:start")
	protected ProducerTemplate producerTemplate;
	
	@Named("bodySetterBean")
	public static class BodySetterBean {
		public String set() {
			return "ThisIsActualBean";
		}
	}
	@Alternative
	@Named("bodySetterBean")
	public static class MockBodySetterBean {
		public String set() {
			return "ThisIsMockBean";
		}
	}
	
	@Test
	public void testMockBean() throws Exception {
		// expect
		resultEndpoint.expectedBodiesReceived("ThisIsMockBean");
		// execute
		producerTemplate.sendBody("start");
		// verify
		resultEndpoint.assertIsSatisfied();
	}
	
	public static class MockRouteConfig extends RouteBuilder {
		@Override
		public void configure() throws Exception {
			from("direct:start")
			.bean("bodySetterBean")                            // Call bean by String
			//.bean(BodySetterBean.class)                      // Call bean by Class -> Cannot be mocked
			//.transform().simple("${bean:bodySetterBean}")    // Call bean by simple expression
			.to("mock:result")
			;
		}
	}	
}
