package us.hyalen.orders.service;

import us.hyalen.orders.Application;
import us.hyalen.orders.domain.Order;
import us.hyalen.orders.domain.OrderSpecification;
import us.hyalen.orders.domain.SearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@DataJpaTest
public class SearchServiceTest {
	@TestConfiguration
    static class SearchServiceTestContextConfiguration {
		@Bean
		public SearchService searchService() {
			return new SearchService();
		}
    }

    @Autowired
	private SearchService searchService;

	@Test
	public void when_ANonExistStatusIsGiven_then_NothingIsReturned() {
		List<Order> result = searchService.fetchOrdersByCriteria(new OrderSpecification(new SearchCriteria("status", ":", "NON_EXIST")));
		assertEquals(0, result.size());
	}

	@Test
	public void when_AValidStatusIsGiven_then_TheCorrectNumberOfOrdersIsReturned() {
		List<Order> result = searchService.fetchOrdersByCriteria(new OrderSpecification(new SearchCriteria("status", ":", "SHIPPED")));
		assertEquals(3, result.size());
	}

	@Test
	public void when_AValidDiscountValueIsGiven_then_TheCorrectNumberOfOrdersContainingAtMininumThisDiscountIsReturned() {
		List<Order> result = searchService.fetchOrdersByCriteria(new OrderSpecification(new SearchCriteria("discount", ">", "0")));
		assertEquals(2, result.size());
	}

	@Test
	public void when_AValidNumberOfProductsIsGiven_then_TheCorrectNumberOfOrdersContainingAtMinimumThisNumberOfProductsIsReturned() {
		List<Order> result = searchService.fetchOrdersByNumberOfValidProducts(2);
		assertEquals(3, result.size());
	}

	@Test
	public void when_AValidProductPriceIsGiven_then_TheCorrectNumberOfOrdersContainingAtMinimumThisPriceIsReturned() {
		List<Order> result = searchService.fetchOrdersByProductPrice(new BigDecimal(30));
		assertEquals(4, result.size());
	}
}