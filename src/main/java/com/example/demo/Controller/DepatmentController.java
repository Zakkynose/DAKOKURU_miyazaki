package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DepatmentController {
	private final DepartmentService departmentService;

	//部署一覧画面
	@GetMapping("/department/index")
	public String showDepartmentList(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
		List<Department> departments = departmentService.searchDepartments(keyword);
		if (keyword == null || keyword.trim().isEmpty()) {
			// キーワードがない場合は全ての部署を取得
			departments = departmentService.findAllDepartments(); // または departmentService.getAllDepartments() など、既存の全件取得メソッド
		} else {
			// キーワードがある場合は検索を実行
			departments = departmentService.searchDepartments(keyword);
		}
		model.addAttribute("departments", departments);
		model.addAttribute("keyword", keyword);
		return "department/list"; // 作成するHTMLファイル名
	}

	// 部署新規登録画面
	@GetMapping("/department/create")
	 public String showDepartmentCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "department/create"; // HTMLテンプレートのパス
	}
	
	@PostMapping("/department/store")
    public String storeDepartment(
            @Valid @ModelAttribute Department department, // フォームデータをDepartmentオブジェクトにバインドし、バリデーションを実行
            BindingResult result, // バリデーション結果を受け取る
            Model model,
            RedirectAttributes redirectAttributes // リダイレクト時にメッセージを渡すため
    ) {
        // バリデーションエラーがある場合
        if (result.hasErrors()) {
            model.addAttribute("department", department); // 入力値を保持してフォームに戻す
            // エラーメッセージはThymeleafのth:errorsで表示される
            return "department/create"; // フォームのHTMLテンプレートに戻る
        }

        try {
            // サービス層の作成メソッドを呼び出す
            departmentService.createDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage", "登録しました。");
            return "redirect:/department/index"; // 成功したら一覧画面にリダイレクト
        } catch (IllegalArgumentException e) {
            // サービス層からのビジネスロジックエラー（例：部署名重複）
            model.addAttribute("errorMessage", e.getMessage()); // エラーメッセージをModelに追加
            model.addAttribute("department", department); // 入力値を保持してフォームに戻す
            return "department/create"; // エラー表示のため、再度フォームへ
        }
    }

	// 部署編集・削除画面
	@GetMapping("/department/edit/{id}")
	public String showDepartmentEditForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes // RedirectAttributes を追加
    ) {
        // IDから部署情報を取得
        Optional<Department> departmentOptional = departmentService.findDepartmentById(id);

        if (departmentOptional.isEmpty()) {
            // 指定されたIDの部署が見つからなかった場合
            redirectAttributes.addFlashAttribute("errorMessage", "指定されたID（" + id + "）の部署は見つかりませんでした。");
            return "redirect:/department/index"; // 一覧画面にリダイレクト
        }

        // 部署が見つかった場合
        model.addAttribute("department", departmentOptional.get());
        return "department/edit";
    }
	 @DeleteMapping("/department/delete/{id}") // DELETEリクエストを受け付ける
	    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        try {
	            departmentService.deleteDepartment(id); // サービス層の削除メソッドを呼び出す
	            redirectAttributes.addFlashAttribute("successMessage", "削除しました。");
	            return "redirect:/department/index"; // 削除成功後、一覧画面へリダイレクト
	        } catch (Exception e) { // 削除に失敗した場合（例: 関連データが存在する場合など）
	            redirectAttributes.addFlashAttribute("errorMessage", "削除に失敗しました: " + e.getMessage());
	            return "redirect:/department/edit/" ; // 編集画面に戻す
	        }
	    }
	}

