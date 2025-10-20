package po.Dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import po.User;
import po.Dao.UserDao;
import util.DbConnection;

public class UserDaoImpl implements UserDao{

	private static final String SQL_INSERT = 
	        "INSERT INTO User (account_name, display_name, password, role) VALUES (?, ?, ?, ?)";
	    private static final String SQL_FIND_BY_ACCOUNT_AND_PASSWORD = 
	        "SELECT user_id, account_name, display_name, password, role FROM User WHERE account_name = ? AND password = ?";
	    private static final String SQL_CHECK_ACCOUNT = 
	        "SELECT user_id FROM User WHERE account_name = ?";

	    private User mapResultSetToUser(ResultSet rs) throws SQLException {
	        User user = new User();
	        user.setUserId(rs.getInt("user_id"));
	        user.setAccountName(rs.getString("account_name"));
	        user.setDisplayName(rs.getString("display_name"));
	        user.setPassword(rs.getString("password")); 
	        user.setRole(rs.getString("role"));
	        return user;
	    }

	    @Override
	    public void insert(User user) throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;

	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_INSERT);
	            
	            pstmt.setString(1, user.getAccountName());
	            pstmt.setString(2, user.getDisplayName());
	            pstmt.setString(3, user.getPassword());
	            pstmt.setString(4, user.getRole());

	            pstmt.executeUpdate();
	        } finally {
	            // 手動關閉資源
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	    }

	    @Override
	    public User findByAccountAndPassword(String accountName, String password) throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        User user = null;

	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_FIND_BY_ACCOUNT_AND_PASSWORD);
	            pstmt.setString(1, accountName);
	            pstmt.setString(2, password);
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                user = mapResultSetToUser(rs);
	            }
	        } finally {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	        return user;
	    }
	    
	    @Override
	    public boolean isAccountExist(String accountName) throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_CHECK_ACCOUNT);
	            pstmt.setString(1, accountName);
	            rs = pstmt.executeQuery();

	            return rs.next(); // 如果有下一行，表示帳號存在
	        } finally {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	    }
}
