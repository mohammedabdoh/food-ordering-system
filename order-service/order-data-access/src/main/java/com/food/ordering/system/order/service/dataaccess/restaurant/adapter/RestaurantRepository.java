package com.food.ordering.system.order.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.restaurant.repository.RestaurantJpsRepository;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepositoryInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepository implements RestaurantRepositoryInterface {
    private final RestaurantJpsRepository restaurantJpsRepository;

    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepository(RestaurantJpsRepository restaurantJpsRepository, RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpsRepository = restaurantJpsRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpsRepository.findByRestaurantIdAndProductIdIn(
                restaurant.getId().getValue(), restaurantProducts
        );
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantToRestaurantEntity);
    }
}
