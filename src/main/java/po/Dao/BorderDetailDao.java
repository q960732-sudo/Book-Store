package po.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import po.BorderDetail;

public interface BorderDetailDao {
	/** C: 批量新增訂單明細 (用於交易) */
    void insertBatch(Connection conn, List<BorderDetail> details) throws SQLException;

    /** R: 依據訂單 ID 查找所有明細 (列印訂單明細) */
    List<BorderDetail> findByOrderId(int orderId) throws SQLException;
}

