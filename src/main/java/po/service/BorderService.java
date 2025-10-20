package po.service;

import java.util.List;

import exception.BusinessException;
import po.Border;
import po.BorderDetail;
import vo.BorderDetailVO;

public interface BorderService {
	//執行結帳交易：同時新增訂單主表和訂單明細。
    int checkout(Border border, List<BorderDetail> details) throws BusinessException;

    List<Border> findBordersByClerkId(int userId) throws BusinessException;
    
    List<BorderDetail> findDetailsByBorderId(int borderId) throws BusinessException;
    
    List<BorderDetailVO> findDetailVOsForExport(int borderId) throws BusinessException;
    
}
