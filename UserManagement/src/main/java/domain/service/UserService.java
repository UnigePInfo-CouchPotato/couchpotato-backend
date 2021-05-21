package domain.service;

import domain.model.Users;

import java.util.List;

public interface UserService {

	List<Users> getAll();
	Users get(String username);
	Users get(int id);
	Long count();
	void update(Users user);
	boolean exists(int id);
	void create(Users user);
}