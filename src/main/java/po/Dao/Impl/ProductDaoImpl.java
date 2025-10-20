package po.Dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import po.Product;
import po.Dao.ProductDao;
import util.DbConnection;

public class ProductDaoImpl implements ProductDao{
	private static final String SQL_INSERT = 
	        "INSERT INTO Product (product_no, product_name, isbn10, total_pages, price) VALUES (?, ?, ?, ?, ?)";
	    private static final String SQL_FIND_BY_PRODUCT_NO = 
	        "SELECT id, product_no, product_name, isbn10, total_pages, price FROM Product WHERE product_no = ?";
	    private static final String SQL_FIND_ALL = 
	        "SELECT id, product_no, product_name, isbn10, total_pages, price FROM Product ORDER BY product_no";
	    private static final String SQL_FIND_BY_ID = 
	    	    "SELECT id, product_no, product_name, isbn10, total_pages, price FROM Product WHERE id = ?";

	    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
	        Product product = new Product();
	        product.setId(rs.getInt("id"));
	        product.setProductNo(rs.getString("product_no"));
	        product.setProductName(rs.getString("product_name"));
	        product.setIsbn10(rs.getString("isbn10"));
	        product.setTotalPages(rs.getInt("total_pages"));
	        product.setPrice(rs.getInt("price"));
	        return product;
	    }

	    @Override
	    public void insert(Product product) throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_INSERT);
	            
	            pstmt.setString(1, product.getProductNo());
	            pstmt.setString(2, product.getProductName());
	            pstmt.setString(3, product.getIsbn10());
	            pstmt.setInt(4, product.getTotalPages());
	            pstmt.setInt(5, product.getPrice());

	            pstmt.executeUpdate();
	        } finally {
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	    }

	    @Override
	    public Product findByProductNo(String productNo) throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        Product product = null;
	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_FIND_BY_PRODUCT_NO);
	            pstmt.setString(1, productNo);
	            rs = pstmt.executeQuery();
	            if (rs.next()) {
	                product = mapResultSetToProduct(rs);
	            }
	        } finally {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	        return product;
	    }
	    
	    @Override
	    public List<Product> findAll() throws SQLException {
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        List<Product> products = new ArrayList<>();
	        try {
	            conn = DbConnection.getDb();
	            pstmt = conn.prepareStatement(SQL_FIND_ALL);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                products.add(mapResultSetToProduct(rs));
	            }
	        } finally {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        }
	        return products;
	    }

		@Override
		public Product findById(int productId) throws SQLException { 
		    Connection conn = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    Product product = null;

		    try {
		        conn = DbConnection.getDb();
		        pstmt = conn.prepareStatement(SQL_FIND_BY_ID);
		        pstmt.setInt(1, productId);
		        rs = pstmt.executeQuery();

		        if (rs.next()) {
		            product = mapResultSetToProduct(rs); 
		        }
		    } finally {
		        if (rs != null) rs.close();
		        if (pstmt != null) pstmt.close();
		        if (conn != null) conn.close();
		    }
		    return product;
		}

}
