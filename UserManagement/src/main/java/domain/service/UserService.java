package domain.service;

import domain.model.User;

import java.util.List;

public interface UserService {

	List<User> getAll();
	User get(String username);
	User get(int id);
	Long count();
	void update(User user);
	boolean exists(int id);

}