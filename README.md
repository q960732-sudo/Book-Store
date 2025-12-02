# Book-Store
書旅人 POS 系統：基於 Java Swing GUI 介面和三層式架構設計的簡易銷售點管理系統。支援會員登入、商品瀏覽、結帳交易與員工商品維護功能。

# 📚 書旅人 簡易 POS (銷售點) 管理系統

這是一個使用 **Java Swing** 桌面應用程式介面 (GUI) 實現的簡易銷售點管理系統。專案採用經典的 **三層式架構**（Controller/Service/DAO），用於模擬基本的書籍或商品銷售與員工管理流程。

## ✨ 主要功能特色

* **會員/員工登入系統：** 區分一般會員與員工權限，並根據身份進入不同介面。
* **商品瀏覽與結帳 (CheckoutFrame)：** 會員/收銀員可瀏覽商品列表並執行結帳交易。
    * **購物車功能：** 即時計算應付總額與找零。
    * **交易紀錄匯出：** 支援將最近成功交易的訂單明細匯出為 `.xls` 格式的 Excel 檔案。
    * **流程優化：** 結帳成功後，保留訂單明細在購物車，允許使用者先列印/匯出，再手動清空購物車。
* **員工管理中心 (ClerkFunctionFrame)：** 員工登入後的管理頁面。
    * **商品維護 (AddProductFrame)：** 員工可進行商品的「新增」、「根據編號查詢」操作，並查看最新的商品列表。
    * **簡化維護：** 商品維護介面已移除複雜的 ID 欄位和更新/刪除功能，專注於新增與查詢。

## 💻 技術棧 (Technology Stack)

| 類別 | 技術名稱 | 用途說明 |
| :--- | :--- | :--- |
| **程式語言** | Java 8+ | 核心開發語言。 |
| **桌面介面** | Java Swing (JFrame, JPanel) | 用於構建所有圖形用戶介面 (GUI)。 |
| **架構模式** | 三層式架構 | 實現 Controller (GUI)、Service (業務邏輯)、DAO (數據訪問) 分層。 |
| **數據處理** | JDBC, POJO/VO/PO | 數據庫連接與對象模型。 |
| **工具庫** | Apache POI | 處理 Excel 匯出功能。 |

## 📂 專案結構 (Controller 層檔案列表)

專案的 Controller (介面) 層包含了以下關鍵的視窗檔案：

* `MainFrame.java`: 應用程式主入口。
* `MemberLoginFrame.java`: 會員登入。
* `RegisterMemberFrame.java`: 會員註冊。
* `ClerkLoginFrame.java`: **員工登入**。
* `ClerkFunctionFrame.java`: **員工功能中心** (連接著 AddProductFrame)。
* `CheckoutFrame.java`: **結帳介面** (核心交易邏輯)。
* `AddProductFrame.java`: **商品維護介面**。

## 📷 介面展示 (Screenshots)

### 結帳操作面板 (CheckoutFrame)

* **優化後的佈局**：輸入欄位和操作按鈕分離，排版整齊，不再因 GridLayout 限制而混亂。
* **按鈕邏輯：** 結帳後，需手動點擊「清空購物車」或「列印 Excel」。

### 商品維護介面 (AddProductFrame)

* **簡化設計：** 介面只顯示商品編號、名稱、價格欄位。
* **返回邏輯：** 提供「返回員工首頁」按鈕，可順暢回到 `ClerkFunctionFrame`。

## 🛠️ 如何運行專案

1.  **環境：** 確保您的系統已安裝 **Java Development Kit (JDK) 8+**。
2.  **資料庫：** 配置專案中的 Service/DAO 層，確保數據庫連線正常（例如 MySQL 或其他）。
3.  **外部依賴：** 必須將 **Apache POI** (至少 `poi-x.x.x.jar`) 的 JAR 檔添加到專案的 **Build Path** 中，否則 Excel 匯出功能將因找不到類別而失敗 (`NoClassDefFoundError`)。
4.  **啟動：** 運行 `MainFrame.java` 的 `main` 方法即可啟動應用程式。
