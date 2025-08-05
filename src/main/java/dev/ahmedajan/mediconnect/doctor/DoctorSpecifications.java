package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.doctor.dto.DoctorSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class DoctorSpecifications {

    public static Specification<DoctorProfile> buildSpecification(DoctorSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getSpecialization())) {
                predicates.add(cb.like(
                        cb.lower(root.get("specialization")),
                        "%" + criteria.getSpecialization().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(criteria.getLocation())) {
                predicates.add(cb.like(
                        cb.lower(root.get("location")),
                        "%" + criteria.getLocation().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(criteria.getFullName())) {
                String nameTerm = "%" + criteria.getFullName().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("user").get("firstName")), nameTerm),
                        cb.like(cb.lower(root.get("user").get("lastName")), nameTerm),
                        cb.like(
                                cb.lower(cb.concat(
                                        root.get("user").get("firstName"),
                                        cb.concat(" ", root.get("user").get("lastName"))
                                )),
                                nameTerm
                        )
                ));
            }

            if (criteria.getMinRating() != null) {
                predicates.add(cb.ge(root.get("rate"), criteria.getMinRating()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
