package po.service.impl;

import java.sql.SQLException;

import config.UserRole;
import exception.BusinessException;
import po.User;
import po.Dao.UserDao;
import po.Dao.Impl.UserDaoImpl;
import po.service.UserService;

public class UserServiceImpl implements UserService{

	private final UserDao userDao = new UserDaoImpl();

    // 輔助方法：執行註冊前的通用檢查
    private void preCheck(User user) {
        if (user.getAccountName() == null || user.getAccountName().trim().isEmpty() ||
            user.getDisplayName() == null || user.getDisplayName().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new BusinessException("註冊資料不完整，請檢查帳號、密碼和姓名。");
        }
    }

    @Override
    public void registerMember(User user) throws BusinessException {
        preCheck(user);

        try {
            if (userDao.isAccountExist(user.getAccountName())) {
                throw new BusinessException("註冊失敗：此帳號已被使用。");
            }
            user.setRole(UserRole.MEMBER); 
            userDao.insert(user);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，會員註冊失敗。", e);
        }
    }
    
    @Override
    public void addClerk(User user) throws BusinessException {
        preCheck(user);

        try {
            if (userDao.isAccountExist(user.getAccountName())) {
                throw new BusinessException("新增員工失敗：此帳號已被使用。");
            }
            user.setRole(UserRole.CLERK); 
            userDao.insert(user);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，新增員工失敗。", e);
        }
    }
    
    @Override
    public User login(String accountName, String password) throws BusinessException {
   
        if (accountName == null || accountName.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new BusinessException("帳號或密碼不可為空。");
        }
        
        try {
            User user = userDao.findByAccountAndPassword(accountName, password);
            if (user == null) {
                throw new BusinessException("登入失敗：帳號或密碼錯誤。");
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，登入失敗。", e);
        }
    }
}

