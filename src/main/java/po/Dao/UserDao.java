package po.Dao;

import java.sql.SQLException;

import po.User;

public interface UserDao {
	
	void insert(User user) throws SQLException;
    
    /** R: 登入驗證 - 依帳號密碼查找使用者 */
    User findByAccountAndPassword(String accountName, String password) throws SQLException;
    
    /** R: 檢查帳號是否已存在 (用於註冊前檢查) */
    boolean isAccountExist(String accountName) throws SQLException; 
}
