package com.jovana.notesbynona.validation;

import com.jovana.notesbynona.entity.perfume.Perfume;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PerfumeSpecification {
   public static Specification<Perfume> hasGenders(List<String> genderNames) {
       return (root, query, criteriaBuilder) -> {
           if (genderNames == null || genderNames.isEmpty()) {
               return criteriaBuilder.conjunction();
           }

           List<String> upperCaseGenderNames = genderNames.stream()
                   .map(String::toUpperCase)
                   .toList();

           return root.get("perfumeGender").get("genderName").in(upperCaseGenderNames);
       };
   }
    public static Specification<Perfume> hasBrands(List<String> brandNames) {
        return (root, query, criteriaBuilder) -> {
            if (brandNames == null || brandNames.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<String> upperCaseBrandNames = brandNames.stream()
                    .map(String::toUpperCase)
                    .toList();

            return root.get("perfumeBrand").get("brandName").in(upperCaseBrandNames);
        };
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

    /*public static Specification<Perfume> sortBy(String property, boolean ascending) {
        return (root, query, criteriaBuilder) -> {
            String fieldName = getFieldName(Perfume.class, property);
            Order order;
            order = ascending
                    ? criteriaBuilder.asc(root.get(fieldName))
                    : criteriaBuilder.desc(root.get(fieldName));
            query.orderBy(order);
            return criteriaBuilder.conjunction();
        };
    }*/
    public static Specification<Perfume> sortBy(String property, boolean ascending) {
        return (root, query, criteriaBuilder) -> {
            String fieldName = getFieldName(Perfume.class, property);

            Expression<Integer> nullCase =
                    criteriaBuilder.<Integer>selectCase()
                            .when(criteriaBuilder.isNull(root.get(fieldName)), 1)
                            .otherwise(0);

            Order nullOrder = criteriaBuilder.asc(nullCase);
            Order valueOrder = ascending
                    ? criteriaBuilder.asc(root.get(fieldName))
                    : criteriaBuilder.desc(root.get(fieldName));

            query.orderBy(nullOrder, valueOrder);
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
