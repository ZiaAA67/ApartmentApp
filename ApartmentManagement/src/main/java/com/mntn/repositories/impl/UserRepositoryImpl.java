package com.mntn.repositories.impl;

import com.mntn.pojo.User;
import com.mntn.repositories.UserRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);

        return (User) q.getSingleResult();
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
        return this.passwordEncoder.matches(password, u.getPassword()) && u.getIsActive();
    }

    @Override
    public List<User> getUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User", User.class);
        return q.getResultList();
    }
}
