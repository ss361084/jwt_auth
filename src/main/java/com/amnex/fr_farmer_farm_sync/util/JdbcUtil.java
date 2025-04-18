package com.amnex.fr_farmer_farm_sync.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcUtil {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String findExistingRolesUsingJdbc(String combinations) {
        String sql = "SELECT coalesce(json_agg(jsonb_build_object('farmerId',farmer_id,'farmId',farm_id)),'[]') " +
                "FROM public.fr_farmer_farm_data " +
                "WHERE (farmer_id, farm_id) IN (" + combinations + ")";

        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
