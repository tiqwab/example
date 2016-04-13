package org.tiqwab.example.camel.mock;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = CamelSpringDelegatingTestContextLoader.class
    )
//@MockEndpoints("direct:next|direct:prev")      // 正規表現でMock化したいRouteの指定。このRouteは実行される。
@MockEndpointsAndSkip("direct:next|direct:prev") // この場合実行されない。いわばStub的。
public class MockSpringTest {

	@Produce(uri="direct:start")
	ProducerTemplate producerTemplate;
	@EndpointInject(uri="mock:direct:next")
	MockEndpoint mockNextEndpoint;
	@EndpointInject(uri="mock:direct:prev")
	MockEndpoint mockPrevEndpoint;
	
	@DirtiesContext
	@Test
	public void testMockRoute() throws Exception {
		// expect
		mockPrevEndpoint.expectedBodiesReceived("Start");
		mockNextEndpoint.expectedBodiesReceived("Mocked");
		// execute
		producerTemplate.sendBody("Start");
		// verify
		mockPrevEndpoint.assertIsSatisfied();
		mockNextEndpoint.assertIsSatisfied();
	}
	
	@Configuration
	public static class MockRouteConfig extends SingleRouteCamelConfiguration {
		@Override
		public RouteBuilder route() {
			return new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("direct:start")
					.to("direct:prev")
					.transform().simple("Mocked")
					.to("direct:next")
					;
				}
			};
		}
		// 複数Routeを定義したい場合、こちらが便利。
		/*
		@Override
		public List<RouteBuilder> routes() {
			return null;
		}
		*/
	}
	
}
