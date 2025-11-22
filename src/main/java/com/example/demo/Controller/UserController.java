package com.example.demo.Controller;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.entity.Name;
import com.example.demo.entity.User;
import com.example.demo.form.UserForm;
import com.example.demo.service.NameService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final NameService nameService;

    @GetMapping("/user/create")
    public String create(Model model) {
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new UserForm());
        }
        return "users/create";
    }

    @PostMapping("/user/store")
    public String store(@Validated @ModelAttribute("userForm") UserForm form,
            BindingResult result, RedirectAttributes ra) {
        //メールアドレスの重複チェック
        Optional<User> existingEmail = userService.findByEmail(form.getEmail());
        if (existingEmail.isPresent()) {
            result.rejectValue("email", "duplicate.email", "メールアドレスは既に存在しています。");
        }
        Optional<User> existingEmployeeNo = userService.findByEmployeeNo(form.getEmployeeNo());
        if (existingEmployeeNo.isPresent()) {
            result.rejectValue("employeeNo", "duplicate.employeeNo", "社員番号が既に存在しています。");
        }
        //  グループバリデーションチェック：旧姓 
        boolean hasAnyOldName = (form.getOlnJp() != null && !form.getOlnJp().isEmpty()) ||
                                (form.getOlnJpHira() != null && !form.getOlnJpHira().isEmpty()) ||
                                (form.getOlnJpKata() != null && !form.getOlnJpKata().isEmpty()) ||
                                (form.getOlnEn() != null && !form.getOlnEn().isEmpty());
        // 入力された項目があれば、全て必須とする
        if (hasAnyOldName) {
            String errorMessage = "旧姓の各欄に一つでも入力があった場合は必須です。";          
            // エラーメッセージを表示したいフィールドにエラーを紐づける
            if (form.getOlnJp() == null || form.getOlnJp().isEmpty()) {
                result.rejectValue("olnJp", "group.required.oln", errorMessage);
            }
            if (form.getOlnJpHira() == null || form.getOlnJpHira().isEmpty()) {
                result.rejectValue("olnJpHira", "group.required.oln", errorMessage);
            }
            if (form.getOlnJpKata() == null || form.getOlnJpKata().isEmpty()) {
                result.rejectValue("olnJpKata", "group.required.oln", errorMessage);
            }
            if (form.getOlnEn() == null || form.getOlnEn().isEmpty()) {
                result.rejectValue("olnEn", "group.required.oln", errorMessage);
            }
        }
        // グループバリデーションチェック：ミドルネーム 
        boolean hasAnyMiddleName = (form.getMnJp() != null && !form.getMnJp().isEmpty()) ||
                                   (form.getMnJpHira() != null && !form.getMnJpHira().isEmpty()) ||
                                   (form.getMnJpKata() != null && !form.getMnJpKata().isEmpty()) ||
                                   (form.getMnEn() != null && !form.getMnEn().isEmpty());
        if (hasAnyMiddleName) {
            String errorMessage = "ミドルネームの各欄に一つでも入力があった場合は必須です。";
            if (form.getMnJp() == null || form.getMnJp().isEmpty()) {
                result.rejectValue("mnJp", "group.required.mn", errorMessage);
            }
            if (form.getMnJpHira() == null || form.getMnJpHira().isEmpty()) {
                result.rejectValue("mnJpHira", "group.required.mn", errorMessage);
            }
            if (form.getMnJpKata() == null || form.getMnJpKata().isEmpty()) {
                result.rejectValue("mnJpKata", "group.required.mn", errorMessage);
            }
            if (form.getMnEn() == null || form.getMnEn().isEmpty()) {
                result.rejectValue("mnEn", "group.required.mn", errorMessage);
            }
        }
        // バリデーションエラー/重複チェックエラー/グループチェックエラーがあればフォームに戻る 
        if (result.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.userForm", result);
            ra.addFlashAttribute("userForm", form);
            return "redirect:/user/create";
        }
        // Userエンティティの作成とデータのセット
        User user = new User();
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setEmployeeNo((form.getEmployeeNo()));
        user.setJoiningDate(form.getJoiningDate());
        userService.save(user); // Userの保存 (IDが自動生成される)
        
        // Nameエンティティの作成とデータのセット
        Name name = new Name();
        name.setFnJp(form.getFnJp());
        name.setFnJpHira(form.getFnJpHira());
        name.setFnJpKata(form.getFnJpKata());
        name.setFnEn(form.getFnEn());
        name.setLnJp(form.getLnJp());
        name.setLnJpHira(form.getLnJpHira());
        name.setLnJpKata(form.getLnJpKata());
        name.setLnEn(form.getLnEn());
        name.setOlnJp(form.getOlnJp());
        name.setOlnJpHira(form.getOlnJpHira());
        name.setOlnJpKata(form.getOlnJpKata());
        name.setOlnEn(form.getOlnEn());
        name.setMnJp(form.getMnJp());
        name.setMnJpHira(form.getMnJpHira());
        name.setMnJpKata(form.getMnJpKata());
        name.setMnEn(form.getMnEn());
        name.setEnglishNotation(Optional.ofNullable(form.getEnglishNotation()).orElse(false));
        name.setUser(user); 
        nameService.save(name); // Nameの保存
        //  成功時のリダイレクトを追加します 
        ra.addFlashAttribute("successMessage", "ユーザーの登録に成功しました。");
        return "redirect:/user/index"; 
    }
    
    @PostMapping("/user/update")
    public String update(@Validated @ModelAttribute("userForm") UserForm form,
            BindingResult result, RedirectAttributes ra) {
        
        Optional<User> existingEmail = userService.findByEmail(form.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(form.getId())) {
            result.rejectValue("email", "duplicate.email", "メールアドレスは既に存在しています。");
        }

        // 社員番号の重複チェック
        if (form.getEmployeeNo() != null && "".equals(form.getEmployeeNo())){
            Optional<User> existingEmployeeNo = userService.findByEmployeeNo(form.getEmployeeNo());
            if (existingEmployeeNo.isPresent() && !existingEmployeeNo.get().getId().equals(form.getId())) {
                result.rejectValue("employeeNo", "duplicate.employeeNo", "社員番号が既に存在しています。");
            }
        }
        if (result.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.userForm", result);
            ra.addFlashAttribute("userForm", form);
            String redirectUrl = UriComponentsBuilder
                    .fromPath("/user/edit/{userId}")
                    .buildAndExpand(form.getId())
                    .toUriString();
            return "redirect:" + redirectUrl;
        }
        
        System.out.println(form.getId());
        User user = userService.findById(form.getId()).orElse(new User());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setEmployeeNo((form.getEmployeeNo()));
        user.setJoiningDate(form.getJoiningDate());
        userService.save(user);
        
        Name name = nameService.findByUserId(form.getId());
        name.setFnJp(form.getFnJp());
        name.setFnJpHira(form.getFnJpHira());
        name.setFnJpKata(form.getFnJpKata());
        name.setFnEn(form.getFnEn());
        name.setLnJp(form.getLnJp());
        name.setLnJpHira(form.getLnJpHira());
        name.setLnJpKata(form.getLnJpKata());
        name.setLnEn(form.getLnEn());
        name.setOlnJp(form.getOlnJp());
        name.setOlnJpHira(form.getOlnJpHira());
        name.setOlnJpKata(form.getOlnJpKata());
        name.setOlnEn(form.getOlnEn());
        name.setMnJp(form.getMnJp());
        name.setMnJpHira(form.getMnJpHira());
        name.setMnJpKata(form.getMnJpKata());
        name.setMnEn(form.getMnEn());
        name.setEnglishNotation(Optional.ofNullable(form.getEnglishNotation()).orElse(false));
        nameService.save(name);
        
        ra.addFlashAttribute("successMessage", "ユーザーの更新に成功しました。");
        return "redirect:/user/index";
    }
   
    @PostMapping("/user/destroy")
    public String destroy(@ModelAttribute("userForm") UserForm form) {
        userService.deleteById(form.getId());
        return "redirect:/user/index";
    }
}