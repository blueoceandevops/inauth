package us.hyalen.orders.service;

import us.hyalen.orders.domain.Order;
import us.hyalen.orders.domain.SearchCriteria;
import us.hyalen.orders.repository.OrderRepository;
import us.hyalen.orders.domain.OrderSpecificationBuilder;
import us.hyalen.orders.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> searchCriteria(String criteria) {
        OrderSpecificationBuilder builder = new OrderSpecificationBuilder();

        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(criteria + ",");

        while (matcher.find()) {
            SearchCriteria searchCriteria = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));

            if(searchCriteria.getKey() != null)
                if(searchCriteria.getKey().equals("price"))
                    return fetchOrdersByProductPrice(new BigDecimal(searchCriteria.getValue()));
                else if(searchCriteria.getKey().equals("products"))
                    return fetchOrdersByNumberOfValidProducts(Integer.parseInt(searchCriteria.getValue()));
                else
                    builder.with(searchCriteria);
        }

        return fetchOrdersByCriteria(builder.build());
    }

    public List<Order> fetchOrdersByCriteria(Specification<Order> spec) {
        return orderRepository.findAll(spec);
    }

    public List<Order> fetchOrdersByProductPrice(BigDecimal price) {
        return orderRepository.findByProductsPriceGreaterThan(price).stream().distinct().collect(Collectors.toList());
    }

    public List<Order> fetchOrdersByNumberOfValidProducts(int size) {
        return Utils.fetchOrdersByNumberOfValidProducts(orderRepository.findByProductsPriceGreaterThan(new BigDecimal("0")).stream().distinct().collect(Collectors.toList()), size);
    }
}