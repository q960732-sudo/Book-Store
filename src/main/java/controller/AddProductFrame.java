package controller;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import config.AppConfig;
import exception.BusinessException;
import po.Product;
import po.User;
import vo.ProductVO;

public class AddProductFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private User currentClerk;

	private JTable productTable;
	private JTextField txtProductNo;
	private JTextField txtProductName;
	private JTextField txtPrice;

	private ProductTableModel productTableModel;

	public AddProductFrame(User clerk) {
		this.currentClerk = clerk;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setTitle("員工管理中心 (商品維護) - 員工: " + currentClerk.getDisplayName());

		contentPane = new JPanel();
		contentPane.setBackground(new Color(128, 128, 128));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		contentPane.add(createMainPanel());

		contentPane.add(createBottomPanel());

		loadAllProducts();
	}

	/** 創建中央主要面板*/
	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(new Color(128, 128, 128));
		mainPanel.setBounds(10, 10, 764, 496);

		productTableModel = new ProductTableModel();
		productTable = new JTable(productTableModel);
		productTable.getTableHeader().setReorderingAllowed(false);

		productTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = productTable.getSelectedRow();
				if (selectedRow != -1) {
					displayProductDetails(selectedRow);
				}
			}
		});

		JScrollPane productScrollPane = new JScrollPane(productTable);
		productScrollPane.setBorder(new TitledBorder("所有商品列表"));
		productScrollPane.setPreferredSize(new Dimension(500, 0)); 
		mainPanel.add(productScrollPane, BorderLayout.CENTER);

		mainPanel.add(createDetailPanel(), BorderLayout.EAST);

		return mainPanel;
	}

	/** 創建右側詳情輸入和操作區 */
	private JPanel createDetailPanel() {
		JPanel detailPanel = new JPanel(new BorderLayout(0, 10));
		detailPanel.setBackground(new Color(192, 192, 192));
		detailPanel.setBorder(new TitledBorder("商品詳情與操作"));
		detailPanel.setPreferredSize(new Dimension(250, 0));

		JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));

		JLabel label = new JLabel("編號 (查詢/新增):", SwingConstants.RIGHT);
		label.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		inputPanel.add(label);
		txtProductNo = new JTextField(15);
		inputPanel.add(txtProductNo);

		JLabel label_1 = new JLabel("名稱:", SwingConstants.RIGHT);
		label_1.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		inputPanel.add(label_1);
		txtProductName = new JTextField(15);
		inputPanel.add(txtProductName);

		JLabel label_2 = new JLabel("價格:", SwingConstants.RIGHT);
		label_2.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		inputPanel.add(label_2);
		txtPrice = new JTextField(15);
		inputPanel.add(txtPrice);

		detailPanel.add(inputPanel, BorderLayout.NORTH);

		// 操作按鈕面板
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10));
		buttonPanel.setBackground(new Color(192, 192, 192));

		JButton btnQuery = new JButton("根據編號查詢");
		btnQuery.setBackground(new Color(210, 210, 210));
		btnQuery.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnQuery.addActionListener(e -> queryProductByNo());
		buttonPanel.add(btnQuery);

		JButton btnAdd = new JButton("新增商品");
		btnAdd.setBackground(new Color(210, 210, 210));
		btnAdd.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnAdd.addActionListener(e -> addProduct());
		buttonPanel.add(btnAdd);

		detailPanel.add(buttonPanel, BorderLayout.CENTER);

		return detailPanel;
	}

	/** 創建底部按鈕 (返回) */
	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(192, 192, 192));
		bottomPanel.setBounds(485, 516, 289, 35);

		JButton btnClear = new JButton("清空輸入欄");
		btnClear.setBounds(10, 5, 117, 25);
		btnClear.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnClear.setBackground(new Color(192, 192, 192));
		btnClear.addActionListener(e -> clearInputs());
		bottomPanel.setLayout(null);
		bottomPanel.add(btnClear);

		JButton btnRefresh = new JButton("重新載入列表");
		btnRefresh.setBounds(156, 5, 123, 25);
		btnRefresh.setBackground(new Color(192, 192, 192));
		btnRefresh.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnRefresh.addActionListener(e -> loadAllProducts());
		bottomPanel.add(btnRefresh);
		
		return bottomPanel;
	}

	private class ProductTableModel extends AbstractTableModel {
		private final String[] COLUMNS = { "編號", "名稱", "價格" };
		private List<ProductVO> productList = new ArrayList<>();

		@Override
		public int getRowCount() {
			return productList.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public String getColumnName(int col) {
			return COLUMNS[col];
		}

		public ProductVO getProductVOAt(int row) {
			return productList.get(row);
		}

		public void setProductList(List<ProductVO> list) {
			this.productList = list;
			fireTableDataChanged();
		}

		@Override
		public Object getValueAt(int row, int col) {
			ProductVO vo = productList.get(row);
			if (col == 0)
				return vo.getProductNo();
			if (col == 1)
				return vo.getProductName();
			if (col == 2)
				return vo.getPrice();
			return null;
		}
	}

	/** 從 Service 載入所有商品 */
	private void loadAllProducts() {
		try {
			List<Product> poProducts = AppConfig.getProductService().findAllProducts();

			List<ProductVO> voList = poProducts.stream().map(po -> {
				ProductVO vo = new ProductVO();
				vo.setId(po.getId());
				vo.setProductNo(po.getProductNo());
				vo.setProductName(po.getProductName());
				vo.setPrice(po.getPrice());
				return vo;
			}).collect(Collectors.toList());

			productTableModel.setProductList(voList);
			clearInputs();

		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, "無法載入商品列表：" + e.getMessage(), "數據錯誤", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void displayProductDetails(int selectedRow) {
		ProductVO vo = productTableModel.getProductVOAt(selectedRow);
		if (vo != null) {
			txtProductNo.setText(vo.getProductNo());
			txtProductName.setText(vo.getProductName());
			txtPrice.setText(String.valueOf(vo.getPrice()));
		}
	}

	/** 根據編號查詢單一商品 */
	private void queryProductByNo() {
		String productNo = txtProductNo.getText().trim();
		if (productNo.isEmpty()) {
			JOptionPane.showMessageDialog(this, "請輸入商品編號進行查詢。", "輸入錯誤", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			Product product = AppConfig.getProductService().findProductByNo(productNo);

			txtProductNo.setText(product.getProductNo());
			txtProductName.setText(product.getProductName());
			txtPrice.setText(String.valueOf(product.getPrice()));

			JOptionPane.showMessageDialog(this, "查詢成功！商品名稱: " + product.getProductName(), "查詢成功",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (BusinessException e) {
			clearInputs();
			txtProductNo.setText(productNo); 
			JOptionPane.showMessageDialog(this, "查詢失敗：" + e.getMessage(), "查詢錯誤", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 新增商品 */
	private void addProduct() {
		String productNo = txtProductNo.getText().trim();
		String productName = txtProductName.getText().trim();
		String priceStr = txtPrice.getText().trim();

		if (productNo.isEmpty() || productName.isEmpty() || priceStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "所有欄位皆為必填。", "輸入錯誤", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			int price = Integer.parseInt(priceStr);
			if (price <= 0) {
				throw new NumberFormatException("價格必須大於零。");
			}

			Product newProduct = new Product();
			newProduct.setProductNo(productNo);
			newProduct.setProductName(productName);
			newProduct.setPrice(price);

			AppConfig.getProductService().addNewProduct(newProduct);

			JOptionPane.showMessageDialog(this, "新增商品成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
			loadAllProducts();

		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, "新增失敗：" + e.getMessage(), "業務錯誤", JOptionPane.ERROR_MESSAGE);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "價格必須是有效且大於零的數字。", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 清空輸入欄位 */
	private void clearInputs() {
		txtProductNo.setText("");
		txtProductName.setText("");
		txtPrice.setText("");
		productTable.clearSelection();
	}
}
