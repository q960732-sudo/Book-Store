package po.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.BusinessException;
import po.Border;
import po.BorderDetail;
import po.Product;
import po.Dao.BorderDao;
import po.Dao.BorderDetailDao;
import po.Dao.ProductDao;
import po.Dao.Impl.BorderDaoImpl;
import po.Dao.Impl.BorderDetailDaoImpl;
import po.Dao.Impl.ProductDaoImpl;
import po.service.BorderService;
import util.DbConnection;
import vo.BorderDetailVO;

public class BorderServiceImpl implements BorderService{

    private final BorderDao borderDao = new BorderDaoImpl();
    private final BorderDetailDao borderDetailDao = new BorderDetailDaoImpl();
    private final ProductDao productDao = new ProductDaoImpl();
    
    @Override
    public int checkout(Border border, List<BorderDetail> details) throws BusinessException {
        if (details == null || details.isEmpty()) {
            throw new BusinessException("結帳失敗：訂單明細不可為空。");
        }
        
        Connection conn = null;
        int borderId = -1;

        try {
            conn = DbConnection.getDb();
            conn.setAutoCommit(false); 
            borderId = borderDao.insert(conn, border);
            
            if (borderId == -1) {
                throw new BusinessException("訂單主表新增失敗，交易中止。"); 
            }
            for (BorderDetail detail : details) {
                detail.setOrderId(borderId); 
            }
            borderDetailDao.insertBatch(conn, details);
            conn.commit(); 
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("交易回滾失敗: " + rollbackEx.getMessage());
                }
            }
            System.err.println("結帳交易失敗 (SQLException): " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("結帳失敗，系統處理訂單時發生錯誤，資料已回滾。", e);
            
        } catch (BusinessException be) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("交易回滾失敗: " + rollbackEx.getMessage());
                }
            }
            throw be;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("關閉連線失敗: " + closeEx.getMessage());
                }
            }
        }
        
        return borderId;
    }
    
    // R: 依據員工 ID 查找訂單
    @Override
    public List<Border> findBordersByClerkId(int userId) throws BusinessException {
        try {
            return borderDao.findByUserId(userId);
        } catch (SQLException e) {
            System.err.println("查找員工訂單時資料庫錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，無法查詢訂單。", e);
        }
    }
    
    // R: 依據訂單 ID 查找明細
    @Override
    public List<BorderDetail> findDetailsByBorderId(int borderId) throws BusinessException {
        try {
            return borderDetailDao.findByOrderId(borderId);
        } catch (SQLException e) {
            System.err.println("查找訂單明細時資料庫錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，無法查詢訂單明細。", e);
        }
    }

    //數據整合與 VO 轉換方法 (供 Excel 匯出)
    @Override
    public List<BorderDetailVO> findDetailVOsForExport(int borderId) throws BusinessException {
        try {
            List<BorderDetail> detailPOs = borderDetailDao.findByOrderId(borderId);
            
            if (detailPOs.isEmpty()) {
                return new ArrayList<>();
            }

            List<BorderDetailVO> voList = new ArrayList<>();
            
            for (BorderDetail po : detailPOs) {
                
                Product product = productDao.findById(po.getProductId());
                
                BorderDetailVO vo = new BorderDetailVO();
                
                vo.setQuantity(po.getQuantity());
                vo.setUnitPrice(po.getUnitPrice());
                vo.setSubtotal(po.getSubtotal());

                if (product != null) {
                    vo.setProductNo(product.getProductNo());
                    vo.setProductName(product.getProductName());
                } else {
                    vo.setProductNo("N/A");
                    vo.setProductName("商品已移除 (ID:" + po.getProductId() + ")");
                }
                
                voList.add(vo);
            }
            
            return voList;

        } catch (SQLException e) {
            System.err.println("查找訂單明細並轉換 VO 時資料庫錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("系統忙碌中，無法準備匯出數據。", e);
        }
    }
}
	

