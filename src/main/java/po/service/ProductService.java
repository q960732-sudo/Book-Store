package po.service;

import java.util.List;

import exception.BusinessException;
import po.Product;

public interface ProductService {
   List<Product> findAllProducts() throws BusinessException;
   
   Product findProductByNo(String productNo) throws BusinessException;
   
   //員工新增新商品 
   void addNewProduct(Product product) throws BusinessException;
}
