package po.Dao;

import java.sql.SQLException;
import java.util.List;

import po.Product;

public interface ProductDao {
	void insert(Product product) throws SQLException;
    
    /** R: 依據商品編號查找單一書籍 (用於購買介面掃描) */
    Product findByProductNo(String productNo) throws SQLException;

    /** R: 獲取所有書籍清單 (用於商品列表顯示) */
    List<Product> findAll() throws SQLException;
    
    Product findById(int productId) throws SQLException;
}
