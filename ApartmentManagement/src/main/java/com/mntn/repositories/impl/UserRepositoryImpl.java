package com.mntn.repositories.impl;

import com.mntn.pagination.PaginatedResponse;
import com.mntn.pojo.User;
import com.mntn.pojo.VehicleAccess;
import com.mntn.repositories.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean existsByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        q.setParameter("username", username);
        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public boolean existsByPhone(String phone) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(u) FROM User u WHERE u.phone = :phone", Long.class);
        q.setParameter("phone", phone);
        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        q.setParameter("email", email);
        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);

        try {
            return (User) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User getUserById(String id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findById", User.class);
        q.setParameter("id", id);

        return (User) q.getSingleResult();
    }

    @Override
    public User register(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);

        // flush đảm bảo dữ liệu được ghi ngay lập tức
        s.flush();
        return u;
    }

    @Override
    public User updateUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(u);
        s.flush();
        return u;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUsername(username);
        if (u == null) throw new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
        return this.passwordEncoder.matches(password, u.getPassword()) && u.getIsActive();
    }

//    @Override
//    public List<User> getUsers() {
//        Session s = this.factory.getObject().getCurrentSession();
//        Query q = s.createQuery("FROM User", User.class);
//        return q.getResultList();
//    }

    @Override
    public PaginatedResponse<User> getUsers(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root root = cq.from(User.class);
        cq.select(root);

        // query dữ liệu
        List<Predicate> predicates = buildPredicates(cb, root, params);
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }
        cq.orderBy(cb.desc(root.get("createdDate")));

        Query dataQuery = session.createQuery(cq);
        int page = params != null && params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int start = (page - 1) * PAGE_SIZE;
        dataQuery.setFirstResult(start);
        dataQuery.setMaxResults(PAGE_SIZE);

        List<User> data = dataQuery.getResultList();

        // query thông tin paginate
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, params);
        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(new Predicate[0]));
        }
        countQuery.select(cb.count(countRoot));

        Long totalItems = session.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        return new PaginatedResponse<>(data, totalItems, totalPages, page);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<User> root, Map<String, String> params) {
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.trim().isEmpty()) {
                String pattern = "%" + kw.trim().toLowerCase() + "%";

                // username
                Predicate usernamePred = cb.like(cb.lower(root.get("username")), pattern);

                // firstName + lastName
                Expression<String> fullName = cb.concat(cb.concat(root.get("firstName"), cb.literal(" ")), root.get("lastName"));
                Predicate fullNamePred = cb.like(cb.lower(fullName), pattern);

                // Gom lại bằng OR
                predicates.add(cb.or(usernamePred, fullNamePred));
            }

            String active = params.get("isActive");
            if (active != null && !active.trim().isEmpty()) {
                boolean isActive = Boolean.parseBoolean(active.trim());
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }
        }

        return predicates;
    }
}
