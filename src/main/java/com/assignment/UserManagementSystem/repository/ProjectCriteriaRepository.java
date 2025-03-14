package com.assignment.UserManagementSystem.repository;

import com.assignment.UserManagementSystem.dto.ProjectSearchCriteria;
import com.assignment.UserManagementSystem.model.Project;
import com.assignment.UserManagementSystem.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ProjectCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Project> searchProjects(ProjectSearchCriteria criteria, String userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        Join<Project, User> joinTable = root.join("user");

        List<Predicate> predicateList = new ArrayList<>();

        Predicate userPredicate = criteriaBuilder.equal(joinTable.get("id"), userId);
        predicateList.add(userPredicate);


        if (criteria.getDeleted() != null) {
            Predicate deletedPredicate = criteriaBuilder.equal(root.get("deleted"), criteria.getDeleted());
            predicateList.add(deletedPredicate);
        }

        if (criteria.getTitle() != null) {
            Predicate predicate = criteriaBuilder.like(root.get("title"), "%" + criteria.getTitle() + "%");
            predicateList.add(predicate);
        }

        if (criteria.getCreatedAfter() != null) {
            Predicate predicate = criteriaBuilder.greaterThan(root.get("createdAt"), criteria.getCreatedAfter());
            predicateList.add(predicate);
        }

        if (criteria.getCreatedBefore() != null) {
            Predicate predicate = criteriaBuilder.lessThan(root.get("createdAt"), criteria.getCreatedBefore());
            predicateList.add(predicate);
        }

        query.where(predicateList.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Project> searchProjectsByAdmin(ProjectSearchCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        List<Predicate> predicateList = new ArrayList<>();

        if (criteria.getDeleted() != null) {
            Predicate predicate = criteriaBuilder.equal(root.get("deleted"), criteria.getDeleted());
            predicateList.add(predicate);
        }

        if (criteria.getTitle() != null) {
            Predicate predicate = criteriaBuilder.like(root.get("title"), "%" + criteria.getTitle() + "%");
            predicateList.add(predicate);
        }

        if (criteria.getCreatedBefore() != null) {
            Predicate predicate = criteriaBuilder.lessThan(root.get("createdAt"), criteria.getCreatedBefore());
            predicateList.add(predicate);
        }

        if (criteria.getCreatedAfter() != null) {
            Predicate predicate = criteriaBuilder.greaterThan(root.get("createdAt"), criteria.getCreatedAfter());
            predicateList.add(predicate);
        }

        query.where(predicateList.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

}
