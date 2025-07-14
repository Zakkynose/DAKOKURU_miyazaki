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
        // 部署名（日本語）の重複チェック
        if (departmentRepository.existsByNameJp(department.getNameJp())) {
            throw new IllegalArgumentException("部署名 '" + department.getNameJp() + "' は既に存在します。");
        }
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        return departmentRepository.save(department);
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
        // 削除対象が存在するか確認（任意だが推奨）
        if (!departmentRepository.existsById(id)) {
            throw new IllegalArgumentException("指定されたIDの部署が見つかりません: " + id);
        }
        departmentRepository.deleteById(id); // JpaRepositoryのdeleteById()を利用
    }


	@Override
	public List<Department> searchDepartments(String keyword) {
		return departmentRepository.findByNameJpContaining(keyword);
	}

}
