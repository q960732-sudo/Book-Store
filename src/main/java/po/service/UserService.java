package po.service;

import exception.BusinessException;
import po.User;

public interface UserService {
	//處理顧客會員註冊的業務邏輯。
    void registerMember(User user) throws BusinessException;

    //處理員工新增的業務邏輯。
    void addClerk(User user) throws BusinessException;
    
    // 登入功能
    User login(String accountName, String password) throws BusinessException;
}
