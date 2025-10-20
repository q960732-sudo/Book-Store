package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import vo.BorderDetailVO;

public class ExcelUtil {
	public static void exportBorderDetails(List<BorderDetailVO> details, String outputFilePath, int orderId)
			throws IOException {

		if (details == null || details.isEmpty()) {
			throw new IOException("無訂單明細可匯出。");
		}

		Workbook workbook = new HSSFWorkbook();

		Sheet sheet = workbook.createSheet("訂單明細_" + orderId);

		String[] headers = { "商品編號", "商品名稱", "單價", "數量", "小計" };
		Row headerRow = sheet.createRow(0);

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		int rowNum = 1;
		for (BorderDetailVO detail : details) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(detail.getProductNo());
			row.createCell(1).setCellValue(detail.getProductName());
			row.createCell(2).setCellValue(detail.getUnitPrice());
			row.createCell(3).setCellValue(detail.getQuantity());
			row.createCell(4).setCellValue(detail.getSubtotal());
		}

		// 5. 調整欄位寬度以適應內容
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// 6. 寫入檔案
		try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
			workbook.write(fileOut);
		} finally {
			workbook.close();
		}
	}
}
