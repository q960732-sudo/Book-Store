package po.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import po.Border;

public interface BorderDao {
	/** C: 新增一筆訂單記錄，並回傳由資料庫產生的 orderId (用於交易) */
    int insert(Connection conn, Border order) throws SQLException;

    /** R: 依據員工 ID 查找該員工建立的訂單清單 (用於員工查找訂單) */
    List<Border> findByUserId(int userId) throws SQLException;
    
    /** R: 依據訂單 ID 查找單一訂單 */
    Border findById(int orderId) throws SQLException;
}
