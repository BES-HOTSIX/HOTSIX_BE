package com.example.hotsix_be.locations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/*
@Component
@RequiredArgsConstructor
@Transactional
@Order(3)
public class LocationInit implements ApplicationRunner {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String query = "SHOW INDEXES FROM food_location";
        List<Map<String, Object>> indexes = jdbcTemplate.queryForList(query);

        if (indexes.stream()
                .noneMatch(index -> "location".equals(index.get("Column_name")) && "SPATIAL".equals(index.get("Index_type")))) {
            jdbcTemplate.execute("CREATE SPATIAL INDEX location ON food_location(location)");
        }
    }
}
*/