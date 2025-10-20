package po.service.impl;

import java.sql.SQLException;
import java.util.List;

import exception.BusinessException;
import po.Product;
import po.Dao.ProductDao;
import po.Dao.Impl.ProductDaoImpl;
import po.service.ProductService;

public class ProductServiceImpl implements ProductService{
	private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public List<Product> findAllProducts() throws BusinessException {
        try {
            return productDao.findAll();
        } catch (SQLException e) {
            System.err.println("查找所有商品時資料庫錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，無法載入商品清單。", e);
        }
    }

    @Override
    public Product findProductByNo(String productNo) throws BusinessException {
        if (productNo == null || productNo.trim().isEmpty()) {
            throw new BusinessException("商品編號不可為空。");
        }
        try {
            Product product = productDao.findByProductNo(productNo);
            if (product == null) {
                throw new BusinessException("找不到商品編號: " + productNo);
            }
            return product;
        } catch (SQLException e) {
            System.err.println("依編號查找商品時資料庫錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，無法查詢商品。", e);
        }
    }

    @Override
    public void addNewProduct(Product product) throws BusinessException {
        // 基礎驗證
        if (product.getProductNo() == null || product.getProductNo().trim().isEmpty() ||
            product.getProductName() == null || product.getProductName().trim().isEmpty() ||
            product.getPrice() <= 0) {
            throw new BusinessException("新增商品失敗：商品編號、名稱或價格無效。");
        }

        try {
            // 檢查商品編號是否重複 (業務規則)
            if (productDao.findByProductNo(product.getProductNo()) != null) {
                throw new BusinessException("新增商品失敗：該商品編號已存在。");
            }
            
            productDao.insert(product);
        } catch (SQLException e) {
            System.err.println("新增商品時資料庫錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，商品新增失敗。", e);
        }
    }
    
    
}
