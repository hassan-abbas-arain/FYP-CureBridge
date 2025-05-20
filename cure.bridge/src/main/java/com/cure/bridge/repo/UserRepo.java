package com.cure.bridge.repo;

import com.cure.bridge.entity.User;
import com.cure.bridge.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    public List<User> findAllByRoleIn(List<Role> role);
    public List<User> findByRole(Role role);
    public User findByEmail(String email);

}
