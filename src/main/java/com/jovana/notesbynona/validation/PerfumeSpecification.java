package com.jovana.notesbynona.validation;

import com.jovana.notesbynona.entity.perfume.Perfume;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PerfumeSpecification {
    public static Specification<Perfume> hasGender(String genderName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("perfumeGender").get("genderName"), genderName.toUpperCase());
    }

    public static Specification<Perfume> hasMinPrice(Long minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

   public static Specification<Perfume> hasPerfumeNotes(List<String> notes) {
       return (root, query, criteriaBuilder) -> {
           if (notes == null || notes.isEmpty()) {
               return criteriaBuilder.conjunction();
           }
           query.distinct(true);

           List<String> upperCaseNotes = notes.stream()
                   .map(String::toUpperCase)
                   .toList();

           Join<Object, Object> baseNotesJoin = root.join("baseNotes", JoinType.LEFT);
           Join<Object, Object> middleNotesJoin = root.join("middleNotes", JoinType.LEFT);
           Join<Object, Object> topNotesJoin = root.join("topNotes", JoinType.LEFT);

           List<Predicate> predicates = new ArrayList<>();

           for (String note : upperCaseNotes) {
               Predicate base = criteriaBuilder.equal(criteriaBuilder.upper(baseNotesJoin.get("noteName")), note);
               Predicate middle = criteriaBuilder.equal(criteriaBuilder.upper(middleNotesJoin.get("noteName")), note);
               Predicate top = criteriaBuilder.equal(criteriaBuilder.upper(topNotesJoin.get("noteName")), note);

               predicates.add(criteriaBuilder.or(base, middle, top));
           }
           return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
       };
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
