package po.Dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import po.Border;
import po.Dao.BorderDao;
import util.DbConnection;

public class BorderDaoImpl implements BorderDao{

	private static final String SQL_INSERT = 
	        "INSERT INTO `Border` (user_id, order_time, total_amount, payment_received, change_due) VALUES (?, ?, ?, ?, ?)";
	    private static final String SQL_FIND_BY_USER_ID = 
	        "SELECT order_id, user_id, order_time, total_amount, payment_received, change_due FROM `Border` WHERE user_id = ? ORDER BY order_time DESC";
	    private static final String SQL_FIND_BY_ID = 
	        "SELECT order_id, user_id, order_time, total_amount, payment_received, change_due FROM `Border` WHERE order_id = ?";


	    private Border mapResultSetToOrder(ResultSet rs) throws SQLException {
	        Border border = new Border(); 
	        border.setOrderId(rs.getInt("order_id"));
	        border.setUserId(rs.getInt("user_id"));
	        
	        border.setOrderTime(rs.getTimestamp("order_time").toInstant()
	                             .atZone(ZoneId.systemDefault())
	                             .toLocalDateTime());
	        border.setTotalAmount(rs.getInt("total_amount"));
	        border.setPaymentReceived(rs.getInt("payment_received"));
	        border.setChangeDue(rs.getInt("change_due"));
	        return border;
	    }

	    @Override
	    public int insert(Connection conn, Border border) throws SQLException {
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        int generatedId = -1;
	        try {
	            pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
	            
	            pstmt.setInt(1, border.getUserId());
	            pstmt.setObject(2, border.getOrderTime()); 
	            pstmt.setInt(3, border.getTotalAmount());
	            pstmt.setInt(4, border.getPaymentReceived());
	            pstmt.setInt(5, border.getChangeDue());

	            int affectedRows = pstmt.executeUpdate();
	            if (affectedRows > 0) {
	                rs = pstmt.getGeneratedKeys();
	                if (rs.next()) {
	                    generatedId = rs.getInt(1);
	                }
	            }
	        } finally {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        }
	        return generatedId;
	    }
	    
	    @Override
	    public List<Border> findByUserId(int userId) throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        List<Border> borders = new ArrayList<>(); 

	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_FIND_BY_USER_ID);
	            pstmt.setInt(1, userId);
	            rs = pstmt.executeQuery();

	            while (rs.next()) {
	                borders.add(mapResultSetToOrder(rs));
	            }
	        } finally {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	        return borders;
	    }

	    @Override
	    public Border findById(int orderId) throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        Border border = null; 

	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_FIND_BY_ID);
	            pstmt.setInt(1, orderId);
	            rs = pstmt.executeQuery();
	            
	            if (rs.next()) {
	                border = mapResultSetToOrder(rs);
	            }
	        } finally {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	        return border;
	    }

}
