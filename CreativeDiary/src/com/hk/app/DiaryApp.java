package com.hk.app;

import com.hk.Controllers.HomePageController;
import com.hk.Controllers.LockPageController;
import com.hk.Models.*;
import com.hk.Views.*;

public class DiaryApp {
	public static void main(String[] args) { 
		LoginScreenModel login = new LoginScreenModel();
		LoginScreen loginView = new LoginScreen();
		LockPageModel lock = new LockPageModel();
		LockPageScreen lockView = new LockPageScreen(loginView);
		LockPageController lockController = new LockPageController(loginView, login);
		while(true) {
		if(lockController.getLoginStatus()) {
			lockView.dispose();
			break;
		}
		}
		MenuModel menu = new MenuModel();
		MenuScreen menuView = new MenuScreen();
		DiaryContainerModel diaryCardModel = new DiaryContainerModel();
		DiaryContainerScreen diaryCard = new DiaryContainerScreen();
		HomePageModel home  = new HomePageModel();
		HomePageScreen homeView = new HomePageScreen(menuView, diaryCard);
		ReadWriteView readwriteScreen = new ReadWriteView();
		HomePageController homeController = new HomePageController(menu, menuView, diaryCardModel, diaryCard, readwriteScreen);
	}
}
