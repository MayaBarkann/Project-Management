package projectManagement.entities;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import projectManagement.controller.entities.FilterItemDTO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
This class is used for defining a query for filtering Items
 */
@AllArgsConstructor
public class ItemSpecification implements Specification<Item> {
    FilterItemDTO filter;
    @NonNull
    private Long boardId;

    //todo: check again about board being null - throw maybe exception?

    /***
     * This method creates a query for filtering Items objects that match all given properties. We create a list of Predicate objects
     * (that represents the conditions we want to apply to the query). For each property we want to filter by, we add a Predicate to the list.
     * We use the CriteriaBuilder object to combine all of the Predicates objects in the list into a single one
     * @param root - represents the item that being queried
     * @param query - represents the complete filter query
     * @param criteriaBuilder - used for constructing the query
     * @return Predicate of all properties and their values we want to filter by
     */
    @Override
    public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(this.boardId == null){
            //todo
        }
        predicates.add(criteriaBuilder.equal(root.get("board"), this.boardId));

        if (this.filter.getAssignedToId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("assignedToUser"), this.filter.getAssignedToId()));
        }

        if (this.filter.getDueDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get("dueDate"), this.filter.getDueDate()));
        }

        if (this.filter.getType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("type"), this.filter.getType()));
        }

        if (this.filter.getCreatorId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("creator"), this.filter.getCreatorId()));
        }

        if (this.filter.getImportance() != null) {
            predicates.add(criteriaBuilder.equal(root.get("importance"), this.filter.getImportance()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
