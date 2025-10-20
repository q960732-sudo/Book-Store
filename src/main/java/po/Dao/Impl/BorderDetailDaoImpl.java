package po.Dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import po.BorderDetail;
import po.Dao.BorderDetailDao;
import util.DbConnection;

public class BorderDetailDaoImpl implements BorderDetailDao{
	
    private static final String SQL_INSERT = 
        "INSERT INTO BorderDetail (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_ORDER_ID = 
        "SELECT detail_id, order_id, product_id, quantity, unit_price, subtotal FROM BorderDetail WHERE order_id = ?";

    private BorderDetail mapResultSetToDetail(ResultSet rs) throws SQLException {
        BorderDetail detail = new BorderDetail();
        detail.setDetailId(rs.getInt("detail_id"));
        detail.setOrderId(rs.getInt("order_id"));
        detail.setProductId(rs.getInt("product_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getInt("unit_price"));
        detail.setSubtotal(rs.getInt("subtotal"));
        return detail;
    }

    @Override
    public void insertBatch(Connection conn, List<BorderDetail> details) throws SQLException {
        if (details == null || details.isEmpty()) return;
        
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQL_INSERT);
            
            for (BorderDetail detail : details) { 
                pstmt.setInt(1, detail.getOrderId());
                pstmt.setInt(2, detail.getProductId());
                pstmt.setInt(3, detail.getQuantity());
                pstmt.setInt(4, detail.getUnitPrice());
                pstmt.setInt(5, detail.getSubtotal());
                pstmt.addBatch();
            }

            pstmt.executeBatch(); 
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    @Override
    public List<BorderDetail> findByOrderId(int orderId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BorderDetail> details = new ArrayList<>(); 
        
        try {
            conn = DbConnection.getDb();
            pstmt = conn.prepareStatement(SQL_FIND_BY_ORDER_ID);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                details.add(mapResultSetToDetail(rs));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
        return details;
    }

}
