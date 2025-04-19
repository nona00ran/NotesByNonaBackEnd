package com.jovana.notesbynona.validation;

import com.jovana.notesbynona.entity.perfume.Perfume;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;

public class PerfumeSpecification {
    public static Specification<Perfume> hasGender(String genderName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("perfumeGender").get("genderName"), genderName.toUpperCase());
    }

    public static Specification<Perfume> hasMinPrice(Long minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Perfume> hasMaxPrice(Long maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Perfume> sortBy(String property, boolean ascending) {
        return (root, query, criteriaBuilder) -> {
            String fieldName = getFieldName(Perfume.class, property);
            Order order;
            order = ascending
                    ? criteriaBuilder.asc(root.get(fieldName))
                    : criteriaBuilder.desc(root.get(fieldName));
            query.orderBy(order);
            return criteriaBuilder.conjunction();
        };
    }

    private static String getFieldName(Class<?> clazz, String property) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase(property)) {
                return field.getName();
            }
        }
        throw new IllegalArgumentException("Invalid property: " + property);
    }
}
