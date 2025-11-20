package com.todo.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.todo.app.model.Account;


@Aspect
@Component
public class CheckLoginAspect {

	@Autowired
	Account account;

	// 未ログインの場合ログインページにリダイレクト
	@Around("execution(* com.todo.app.controller.TodoController.*(..))")
	public Object checkLogin(ProceedingJoinPoint jp) throws Throwable {

		if (account == null || account.getUserName() == null
				|| account.getUserName().length() == 0) {
			System.err.println("ログインしていません!");
			// リダイレクト先を指定する
			// パラメータを渡すことでログインControllerで
			// 個別のメッセージをThymeleafに渡すことも可能
			return "redirect:/login?error=notLoggedIn";
		}
		// Controller内のメソッドの実行
		return jp.proceed();
	}
	
	// 未ログインの場合管理者ログインページにリダイレクト
	@Around("execution(* com.todo.app.controller.admin.*ManagementController.*(..))")
	public Object checkAdminLogin(ProceedingJoinPoint jp) throws Throwable {

		if (account == null || account.getUserName() == null
				|| account.getUserName().length() == 0) {
			System.err.println("ログインしていません!");
			// リダイレクト先を指定する
			// パラメータを渡すことでログインControllerで
			// 個別のメッセージをThymeleafに渡すことも可能
			return "redirect:/admin/login?error=notLoggedIn";
		}
		// Controller内のメソッドの実行
		return jp.proceed();
	}
}
