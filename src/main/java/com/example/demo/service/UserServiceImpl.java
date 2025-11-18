package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public Optional<User> findById(Long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public void deleteById(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Optional<User> findByEmployeeNo(String employeeNo) {
		// 必須チェックや形式チェックはController/UserFormで済んでいる前提
		try {
			Long longEmployeeNo = Long.valueOf(employeeNo);
			// UserRepository が findByEmployeeNo(Long) を要求する場合
			return userRepository.findByEmployeeNo(longEmployeeNo);
		} catch (NumberFormatException e) {
			// NumberFormatExceptionが発生した場合は、見つからないとして空のOptionalを返すなど
			return Optional.empty();
		}
	}
}