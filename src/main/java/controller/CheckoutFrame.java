package controller;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import config.AppConfig;
import exception.BusinessException;
import po.Border;
import po.BorderDetail;
import po.Product;
import po.User;
import util.ExcelUtil;
import vo.BorderDetailVO;
import vo.ProductVO;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.Color;

public class CheckoutFrame extends JFrame {
	private static final long serialVersionUID = 1L;
    private User currentUser; 
    private JPanel contentPane;

    // GUI 元件
    private JLabel lblWelcome;
    private JLabel lblTime;
    private JTable productTable;
    private JTable cartTable;
    
    private ShoppingCartTableModel cartModel; 
    private JComboBox<String> cmbProductName; 
    private JTextField txtQuantity;
    private JLabel lblTotalAmount;
    private JTextField txtPayment;

    private int lastSuccessfulOrderId = -1; 
    private Timer timer; 
    
    private Map<String, Product> productMap = new HashMap<>(); 
    
    private class ShoppingCartTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        private List<Object[]> cartItems = new ArrayList<>(); 
        private final String[] columnNames = {"商品編號", "商品名稱", "單價", "數量", "小計"};

        public List<Object[]> getCartItems() { return cartItems; }

        @Override public int getRowCount() { return cartItems.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int columnIndex) { return columnNames[columnIndex]; }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object[] row = cartItems.get(rowIndex);
            ProductVO productVO = (ProductVO)row[0];
            int quantity = (int)row[1];
            
            if (columnIndex == 0) return productVO.getProductNo(); 
            if (columnIndex == 1) return productVO.getProductName(); 
            if (columnIndex == 2) return productVO.getPrice(); 
            if (columnIndex == 3) return quantity; 
            if (columnIndex == 4) return quantity * productVO.getPrice(); 
            return null;
        }

        public void addItem(ProductVO productVO, int quantity) {
            cartItems.add(new Object[]{productVO, quantity});
            fireTableRowsInserted(cartItems.size() - 1, cartItems.size() - 1);
        }
        
        public void clearCart() {
            int oldSize = cartItems.size();
            cartItems.clear();
            if (oldSize > 0) {
                fireTableRowsDeleted(0, oldSize - 1);
            }
        }
        
        public void removeItem(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < cartItems.size()) {
                cartItems.remove(rowIndex);
                fireTableRowsDeleted(rowIndex, rowIndex);
            }
        }
    }


    public CheckoutFrame(User user) {
        this.currentUser = user;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        setTitle("購買介面");
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        startClock(); 
        
        contentPane.add(createHeaderPanel(), BorderLayout.NORTH);
        contentPane.add(createMainPanel(), BorderLayout.CENTER);
        
        loadAllProducts(); 
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        lblWelcome = new JLabel("歡迎光臨，會員: " + currentUser.getDisplayName(), SwingConstants.LEFT);
        lblWelcome.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        lblTime = new JLabel("", SwingConstants.RIGHT);
        lblTime.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        headerPanel.add(lblTime, BorderLayout.EAST);
        return headerPanel;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(128, 128, 128));
        
        productTable = new JTable(); 
        productTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setBorder(new TitledBorder("所有商品列表 (參考編號/價格)"));
        productScrollPane.setPreferredSize(new Dimension(450, 0)); 
        mainPanel.add(productScrollPane, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(new Color(192, 192, 192));
        
        cartModel = new ShoppingCartTableModel(); 
        cartTable = new JTable(cartModel);
        cartTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(new TitledBorder("訂單內容 / 購物車"));
        rightPanel.add(cartScrollPane, BorderLayout.CENTER);
        
        rightPanel.add(createControlPanel(), BorderLayout.SOUTH);
        
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(new TitledBorder("操作與交易"));
        
        // 1. 輸入區 - JComboBox
        JLabel label = new JLabel("商品名稱:", SwingConstants.RIGHT);
        label.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        panel.add(label); 
        cmbProductName = new JComboBox<>();
        cmbProductName.setBackground(new Color(223, 223, 223));
        cmbProductName.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        panel.add(cmbProductName);
        
        JLabel label_3 = new JLabel("購買數量:", SwingConstants.RIGHT);
        label_3.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        panel.add(label_3);
        txtQuantity = new JTextField("1");
        panel.add(txtQuantity);
        
        // 2. 總金額
        JLabel label_2 = new JLabel("應付總額 (TWD):", SwingConstants.RIGHT);
        label_2.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        panel.add(label_2);
        lblTotalAmount = new JLabel("$0", SwingConstants.LEFT);
        lblTotalAmount.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        panel.add(lblTotalAmount);
        
        // 3. 實收金額與結帳
        JLabel label_1 = new JLabel("實收金額 (TWD):", SwingConstants.RIGHT);
        label_1.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        panel.add(label_1);
        txtPayment = new JTextField("0"); 
        panel.add(txtPayment);
        
        // 4. 操作按鈕：清空 / 加入
        JButton btnClear = new JButton("清空購物車");
        btnClear.setBackground(new Color(192, 192, 192));
        btnClear.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        btnClear.addActionListener(e -> clearCartAndReset());
        panel.add(btnClear);
        
        JButton btnAdd = new JButton("加入購物車");
        btnAdd.setBackground(new Color(192, 192, 192));
        btnAdd.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        btnAdd.addActionListener(e -> addItemToCart());
        panel.add(btnAdd);

        // 5. 交易按鈕：結帳 / Excel
        JButton btnCheckout = new JButton("確認結帳");
        btnCheckout.setBackground(new Color(192, 192, 192));
        btnCheckout.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        btnCheckout.addActionListener(e -> performCheckoutTransaction());
        panel.add(btnCheckout);
        
        JButton btnExport = new JButton("列印 Excel (最近訂單)");
        btnExport.setBackground(new Color(192, 192, 192));
        btnExport.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        btnExport.addActionListener(e -> exportLastOrderToExcel());
        panel.add(btnExport);

        // 6. 離開按鈕
        JButton btnExit = new JButton("離開介面 (返回首頁)");
        btnExit.setBackground(new Color(192, 192, 192));
        btnExit.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        btnExit.addActionListener(e -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            dispose();
        });
        panel.add(btnExit);
        
        return panel;
    }
    
    /** 啟動即時時間顯示 */
    private void startClock() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            }
        });
        timer.start();
    }
    
    private void loadAllProducts() {
        try {
            List<Product> poProducts = AppConfig.getProductService().findAllProducts(); 
            
            productMap.clear();
            cmbProductName.removeAllItems();
            
            List<ProductVO> voList = new ArrayList<>();
            for (Product po : poProducts) {
                productMap.put(po.getProductName(), po); 
                
                cmbProductName.addItem(po.getProductName());
                
                ProductVO vo = new ProductVO();
                vo.setProductNo(po.getProductNo());
                vo.setProductName(po.getProductName());
                vo.setPrice(po.getPrice());
                voList.add(vo);
            }

            productTable.setModel(new AbstractTableModel() {
                private final String[] COLUMNS = {"編號", "名稱", "價格"};
                @Override public int getRowCount() { return voList.size(); }
                @Override public int getColumnCount() { return COLUMNS.length; }
                @Override public String getColumnName(int col) { return COLUMNS[col]; }
                @Override public Object getValueAt(int row, int col) {
                    ProductVO vo = voList.get(row);
                    if (col == 0) return vo.getProductNo();
                    if (col == 1) return vo.getProductName();
                    if (col == 2) return vo.getPrice();
                    return null;
                }
            });
            
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, "無法載入商品列表：" + e.getMessage(), "數據錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addItemToCart() {
        String productName = (String) cmbProductName.getSelectedItem();
        if (productName == null || productName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "請選擇一個商品。", "操作錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int quantity = Integer.parseInt(txtQuantity.getText().trim());

            if (quantity <= 0) {
                 throw new BusinessException("購買數量必須大於 0。");
            }
            
            Product product = productMap.get(productName);
            
            if (product == null) {
                 throw new BusinessException("找不到該商品：[" + productName + "]。");
            }
            
            ProductVO productVO = new ProductVO();
            productVO.setProductNo(product.getProductNo());
            productVO.setProductName(product.getProductName());
            productVO.setPrice(product.getPrice());

            cartModel.addItem(productVO, quantity);
            updateTotalAmount();
            
            txtQuantity.setText("1"); 
            
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, "加入失敗：" + e.getMessage(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "數量必須是有效數字。", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearCartAndReset() {
        cartModel.clearCart();
        updateTotalAmount();
        txtPayment.setText("0"); 
        JOptionPane.showMessageDialog(this, "購物車已清空。", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /** 更新總金額顯示 */
    private void updateTotalAmount() {
        int total = 0;
        for (Object[] item : cartModel.getCartItems()) {
            ProductVO vo = (ProductVO) item[0];
            int quantity = (int) item[1];
            total += vo.getPrice() * quantity;
        }
        lblTotalAmount.setText("$" + total);
        
        if (total == 0) {
            txtPayment.setText("0");
        }
    }

    private void performCheckoutTransaction() {
        if (cartModel.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "購物車為空，無法結帳。", "結帳失敗", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int totalAmount = Integer.parseInt(lblTotalAmount.getText().replace("$", ""));
            int paymentReceived = Integer.parseInt(txtPayment.getText().trim()); 
            int changeDue = paymentReceived - totalAmount;

            if (changeDue < 0) {
                JOptionPane.showMessageDialog(this, "實收金額不足，請補足差額。", "結帳失敗", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<BorderDetail> details = new ArrayList<>();
            for (Object[] item : cartModel.getCartItems()) {
                ProductVO vo = (ProductVO) item[0];
                int quantity = (int) item[1];
                
                Product product = productMap.get(vo.getProductName());
                if (product == null) {
                    throw new BusinessException("商品資料不完整，無法結帳。");
                }
                
                BorderDetail detail = new BorderDetail();
                detail.setProductId(product.getId()); 
                detail.setQuantity(quantity);
                detail.setUnitPrice(vo.getPrice());
                detail.setSubtotal(vo.getPrice() * quantity);
                details.add(detail);
            }
            
            Border border = new Border();
            border.setUserId(currentUser.getUserId()); 
            border.setOrderTime(LocalDateTime.now());
            border.setTotalAmount(totalAmount);
            border.setPaymentReceived(paymentReceived);
            border.setChangeDue(changeDue);

            int newBorderId = AppConfig.getBorderService().checkout(border, details);
            lastSuccessfulOrderId = newBorderId; 
            
            String successMsg = String.format("交易成功！訂單ID: %d\n找零: %d", 
                                              newBorderId, changeDue);
            JOptionPane.showMessageDialog(this, successMsg, "結帳完成", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, "結帳交易失敗：" + e.getMessage(), "交易錯誤", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "金額輸入錯誤，請確認實收金額為有效數字。", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "系統處理錯誤：" + e.getMessage(), "系統錯誤", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /** 列印 Excel (最近一筆成功訂單的明細) */
    private void exportLastOrderToExcel() {
        if (lastSuccessfulOrderId == -1) {
            JOptionPane.showMessageDialog(this, "目前沒有成功交易的訂單可以匯出。", "匯出錯誤", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("選擇訂單明細儲存位置");
        
        String defaultFileName = "Order_" + lastSuccessfulOrderId + "_" + 
                                 LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd")) + ".xls";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String savePath = fileToSave.getAbsolutePath();
            
            if (!savePath.toLowerCase().endsWith(".xls")) {
                savePath += ".xls";
            }

            try {
                List<BorderDetailVO> detailsToExport = AppConfig.getBorderService().findDetailVOsForExport(lastSuccessfulOrderId);
                
                ExcelUtil.exportBorderDetails(detailsToExport, savePath, lastSuccessfulOrderId);
                
                JOptionPane.showMessageDialog(this, "訂單明細已成功匯出到:\n" + savePath, "匯出成功", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (BusinessException ex) {
                JOptionPane.showMessageDialog(this, "匯出失敗: 無法從資料庫獲取數據。", "數據錯誤", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "匯出失敗: 無法寫入檔案。\n原因: " + ex.getMessage(), "檔案錯誤", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "匯出失敗: 發生未預期錯誤。", "系統錯誤", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
