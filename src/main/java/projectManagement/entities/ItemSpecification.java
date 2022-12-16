package projectManagement.entities;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

//This code defines a Specification interface, which is a functional interface that allows you to define a query for filtering items saved in a JPA repository.
// The toPredicate method takes in a Root object, which represents the root of the entity being queried, a CriteriaQuery object,
// which represents the complete query, and a CriteriaBuilder object, which is used to construct the query.
// In the toPredicate method, we create a list of Predicate objects, which represent the individual conditions that we want to apply to the query.
// Then, we add a Predicate to the list for each property that we want to filter by.
// In this example, we are filtering by the name, description, and price properties of the Item entity.
//Finally, we use the and method of the CriteriaBuilder object to combine all of the Predicate objects in the list into a single Predicate,
// which represents the complete filter specification.

public class ItemSpecification implements Specification<Item> {
    private final Item filter;

    public ItemSpecification(Item filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getAssignedToUser() != null) {
            predicates.add(criteriaBuilder.equal(root.get("assignedToUser"), filter.getAssignedToUser()));
        }

        if (filter.getDescription() != null) {
            predicates.add(criteriaBuilder.equal(root.get("description"), filter.getDescription()));
        }

        if (filter.getDueDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get("dueDate"), filter.getDueDate()));
        }

        if (filter.getTitle() != null) {
            predicates.add(criteriaBuilder.equal(root.get("title"), filter.getTitle()));
        }

        if (filter.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getImportance() != null) {
            predicates.add(criteriaBuilder.equal(root.get("importance"), filter.getImportance()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
