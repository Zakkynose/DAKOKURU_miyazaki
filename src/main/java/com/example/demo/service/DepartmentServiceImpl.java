package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	private final DepartmentRepository departmentRepository;
	
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}
	@Override
	public List<Department> findAllDepartments() {
	    return departmentRepository.findAll();  //すべての部署を取得
	}

	@Override
	public Optional<Department> findDepartmentById(Long id) {
		return departmentRepository.findById(id);  //IDで部署を取得
		
	}

	@Override
	public Department createDepartment(Department department) {
		department.setCreatedAt(LocalDateTime.now());  //作成日時を設定
		department.setUpdatedAt(LocalDateTime.now());  //更新日時を設定
		return departmentRepository.save(department);  //新規部署を作成
	}

	@Override
	public Department updateDepartment(Long id, Department departmentDetails) {
		
		 Department existingDepartment = departmentRepository.findById(id)
		            .orElseThrow(() -> new IllegalArgumentException("指定されたIDの部署が見つかりません: " + id));

		        // 更新時のバリデーション例：部署名の重複チェック
		        if (!existingDepartment.getNameJp().equals(departmentDetails.getNameJp()) ) {
		            throw new IllegalArgumentException("部署名 '" + departmentDetails.getNameJp() + "' は既に存在します。");
		        }

		        existingDepartment.setNameJp(departmentDetails.getNameJp()); // 部署名を更新
		        existingDepartment.setUpdatedAt(LocalDateTime.now()); // 更新日時を設定
		        return departmentRepository.save(existingDepartment); // データベースを更新
	}

	@Override
	public void deleteDepartment(Long id) {
		departmentRepository.deleteById(id); // IDで部署を削除
	}

	@Override
	public List<Department> searchDepartments(String keyword) {
		return departmentRepository.findByNameJpContaining(keyword);
	}

}
