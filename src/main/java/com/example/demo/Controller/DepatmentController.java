package com.example.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DepatmentController {
	private final DepartmentService departmentService;

	//部署一覧画面
	 @GetMapping("/department/index")
	    public String showDepartmentList(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
	        List<Department> departments = departmentService.searchDepartments(keyword);
	        model.addAttribute("departments", departments);
	        model.addAttribute("keyword", keyword); 
	        return "department/list"; // 作成するHTMLファイル名
	    }
	// 部署新規登録画面
	@GetMapping("/department/create")
	public String showDepartmentCreateForm() {
		return "department/create"; // src/main/resources/templates/department/create.html を返す
	}

	// 部署編集・削除画面
	@GetMapping("/department/edit/{id}")
	public String showDepartmentEditForm(@PathVariable Long id) {
		return "department/edit";
	}
}
