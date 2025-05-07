package com.jovana.notesbynona.validation;

import com.jovana.notesbynona.entity.review.Review;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;

public class ReviewSpecification {
    public static Specification<Review> hasRating(Integer rating) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("rating"), rating);
    }
    public static Specification<Review> hasComments() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.isNotNull(root.get("comment")),
                        criteriaBuilder.notEqual(root.get("comment"), "")
                );
    }
    public static Specification<Review> hasPerfumeId(Long perfumeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("perfume").get("id"), perfumeId);
    }
    public static Specification<Review> sortBy(String property, boolean ascending) {
        return (root, query, criteriaBuilder) -> {
            String fieldName = getFieldName(Review.class, property);
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
