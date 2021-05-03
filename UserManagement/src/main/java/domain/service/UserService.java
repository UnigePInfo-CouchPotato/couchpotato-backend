package domain.service;

import domain.model.Users;

import java.util.List;

public interface UserService {

	List<Users> getAll();
	Users get(String username);
	Users get(int id);
	Long count();
	void update(Users users);
	boolean exists(int id);

}