package com.example.demo.form;

import java.time.LocalDate;

//import com.example.demo.validation.GroupRequired;
import com.example.demo.validation.UniqueEmail;
//import com.example.demo.validation.UserGroupRequired;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
//@UserGroupRequired({ 
    //@GroupRequired(fields = {"olnJp", "olnJpHira", "olnJpKata", "olnEn"}, message = "旧姓は入力する項目をすべて入力してください。"),
    //@GroupRequired(fields = {"mnJp", "mnJpHira", "mnJpKata", "mnEn"}, message = "ミドルネームは入力する項目をすべて入力してください。")
//})
public class UserForm implements ValidationGroups {

    private Long id;

    @NotBlank(message = "名前(正式表示)を入力してください。")
    @Size(max = 255, message = "名前（正式表示）は1文字以上、255文字以内で入力してください。")
    private String fnJp;

    @NotBlank(message = "名前(ひらがな)を入力してください。")
    @Pattern(regexp = "^[\\u3040-\\u309F\\u30FC\\u3000]+$", message = "名前（ひらがな）は全角ひらがなで入力してください。") // 追加
    @Size(max = 255, message = "名前（ひらがな）は1文字以上、255文字以内で入力してください。")
    private String fnJpHira;

    @NotBlank(message = "名前(カタカナ)を入力してください。")
    @Pattern(regexp = "^[\\u30a0-\\u30ff\\u30fc\\u3000]+$", message = "名前（カタカナ）は全角カタカナで入力してください。") // 追加
    @Size(max = 255, message = "名前（カタカナ）は1文字以上、255文字以内で入力してください。")
    private String fnJpKata;

    @NotBlank(message = "名前(英語)を入力してください。")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "名前（英語）は半角英字で入力してください。") // ★追加
    @Size(max = 255, message = "名前（英語）は1文字以上、255文字以内で入力してください。")
    private String fnEn;


    @NotBlank(message = "姓(正式表示)を入力してください。")
    @Size(max = 255, message = "姓（正式表示）は1文字以上、255文字以内で入力してください。")
    private String lnJp;

    @NotBlank(message = "姓(ひらがな)を入力してください。")
    @Pattern(regexp = "^[\\u3040-\\u309F\\u30FC\\u3000]+$", message = "姓（ひらがな）は全角ひらがなで入力してください。") // 追加
    @Size(max = 255, message = "姓（ひらがな）は1文字以上、255文字以内で入力してください。")
    private String lnJpHira;

    @NotBlank(message = "姓(カタカナ)を入力してください。")
    @Pattern(regexp = "^[\\u30a0-\\u30ff\\u30fc\\u3000]+$", message = "姓（カタカナ）は全角カタカナで入力してください。") // 追加
    @Size(max = 255, message = "姓（カタカナ）は1文字以上、255文字以内で入力してください。")
    private String lnJpKata;

    @NotBlank(message = "姓(英語)を入力してください。")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "姓（英語）は半角英字で入力してください。") // 追加
    @Size(max = 255, message = "姓（英語）は1文字以上、255文字以内で入力してください。")
    private String lnEn;

    // 旧姓の入力が必要な場合にチェック
    @Pattern(regexp = "^$|^[\\u3040-\\u309F\\u30fc\\u3000]+$", message = "旧姓(ひらがな)は全角ひらがなで入力してください。")
    @Size(max = 255, message = "旧姓（ひらがな）は1文字以上、255文字以内で入力してください。")
    private String olnJpHira;

    @Pattern(regexp = "^$|^[\\u30a0-\\u30ff\\u30fc\\u3000]+$", message = "旧姓(カタカナ)は全角カタカナで入力してください。")
    @Size(max = 255, message = "旧姓（カタカナ）は1文字以上、255文字以内で入力してください。")
    private String olnJpKata;

    @Pattern(regexp = "^$|^[a-zA-Z0-9]+$", message = "旧姓(英語)は半角英字で入力してください。")
    @Size(max = 255, message = "旧姓（英語）は1文字以上、255文字以内で入力してください。")
    private String olnEn;

    // ミドルネーム
    @Pattern(regexp = "^$|^[\\u3040-\\u309F\\u30fc\\u3000]+$", message = "ミドルネーム(ひらがな)は全角ひらがなで入力してください。")
    @Size(max = 255, message = "ミドルネーム（ひらがな）は1文字以上、255文字以内で入力してください。")
    private String mnJpHira;

    @Pattern(regexp = "^$|^[\\u30a0-\\u30ff\\u30fc\\u3000]+$", message = "ミドルネーム(カタカナ)は全角カタカナで入力してください。")
    @Size(max = 255, message = "ミドルネーム（カタカナ）は1文字以上、255文字以内で入力してください。")
    private String mnJpKata;

    @Pattern(regexp = "^$|^[a-zA-Z0-9]+$", message = "ミドルネーム(英語)は半角英字で入力してください。")
    @Size(max = 255, message = "ミドルネーム（英語）は1文字以上、255文字以内で入力してください。")
    private String mnEn;
    
    @Size(max = 255, message = "旧姓（正式表示）は1文字以上、255文字以内で入力してください。")
    private String olnJp;
   
    @Size(max = 255, message = "ミドルネーム（正式表示）は1文字以上、255文字以内で入力してください。")
    private String mnJp;
   

    @NotBlank(message = "メールアドレスを入力してください。")
    @Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "メールアドレスは正しい形式で入力してください。")
    @Size(max = 255, message = "メールアドレスは1文字以上、255文字以内で入力してください。")
    @UniqueEmail
    private String email;

    @NotBlank(message = "パスワードを入力してください。")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "パスワードは半角英数字で入力してください。") // 追加
    @Size(min = 8, max = 255, message = "パスワードは8文字以上255文字以内で入力してください。")
    private String password;

    @NotBlank(message = "社員番号を入力してください。")
    @Pattern(regexp = "^[0-9]+$", message = "社員番号は半角数字で入力してください。")
    @Size(max = 10, message = "社員番号は1桁以上10桁以内で入力してください。")
    private String employeeNo;
    private Long currentEmployeeNo;

    @NotNull(message = "入社日を入力してください。")
    private LocalDate joiningDate;
    private Boolean englishNotation;

    // 旧姓フィールドのいずれかが入力された場合は、olnJp も必須
    //@AssertTrue(message = "旧姓の名前(正式表示)を入力してください。")
    //public boolean isOldNameValid() {
        //if ((olnJpHira != null && !olnJpHira.isEmpty()) ||
                //(olnJpKata != null && !olnJpKata.isEmpty()) ||
                //(olnEn != null && !olnEn.isEmpty())) {
            //return olnJp != null && !olnJp.isEmpty();
        //}
        //return true;
    //}

}
