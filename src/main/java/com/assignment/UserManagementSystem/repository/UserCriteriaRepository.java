package com.assignment.UserManagementSystem.repository;

import com.assignment.UserManagementSystem.dto.UserSearchCriteria;
import com.assignment.UserManagementSystem.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> searchUsers(UserSearchCriteria searchCriteria) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicateList = new ArrayList<>();

        if (searchCriteria.getName() != null) {
            Predicate predicate = criteriaBuilder.like(root.get("name"), "%" + searchCriteria.getName() + "%");
            predicateList.add(predicate);
        }

        if (searchCriteria.getNrcNumber() != null) {
            Predicate predicate = criteriaBuilder.like(root.get("nrcNumber"), "%" + searchCriteria.getNrcNumber() + "%");
            predicateList.add(predicate);
        }

        if (searchCriteria.getPhoneNumber() != null) {
            Predicate predicate = criteriaBuilder.like(root.get("phoneNumber"), "%" + searchCriteria.getPhoneNumber() + "%");
            predicateList.add(predicate);
        }

        if (searchCriteria.getRole() != null) {
            Predicate predicate = criteriaBuilder.like(root.get("role"), "%" + searchCriteria.getRole() + "%");
            predicateList.add(predicate);
        }

        query.where(predicateList.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

}
