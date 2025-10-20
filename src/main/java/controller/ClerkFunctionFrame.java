package controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import po.User;
import java.awt.Color;

public class ClerkFunctionFrame extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private User currentClerk; 

    public ClerkFunctionFrame(User clerk) {
        this.currentClerk = clerk;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        setTitle("員工功能中心");
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 20));

        // 頂部歡迎訊息
        JLabel lblWelcome = new JLabel("員工管理中心，歡迎 " + currentClerk.getDisplayName(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        contentPane.add(lblWelcome, BorderLayout.NORTH);
        
        // 中央按鈕區
        contentPane.add(createButtonPanel(), BorderLayout.CENTER);
        
        // 底部登出按鈕
        JButton btnLogout = new JButton("登出 (返回首頁)");
        btnLogout.setBackground(new Color(192, 192, 192));
        btnLogout.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        btnLogout.addActionListener(e -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            dispose();
        });
        contentPane.add(btnLogout, BorderLayout.SOUTH);
    }
    
    /** 創建中央功能按鈕面板 */
    private JPanel createButtonPanel() {
        // 使用 GridLayout 讓按鈕平均分佈
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 15)); 
        buttonPanel.setBackground(new Color(128, 128, 128));
        
        // 1. 商品維護按鈕
        JButton btnProduct = new JButton("① 商品新增/維護");
        btnProduct.setBackground(new Color(210, 210, 210));
        btnProduct.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
        btnProduct.addActionListener(e -> {
            // 開啟 AddProductFrame
            AddProductFrame frame = new AddProductFrame(currentClerk);
            frame.setVisible(true);
            dispose();
        });
        buttonPanel.add(btnProduct);
        
        // 2. 訂單查詢按鈕 (此功能尚未實作，先作為導航佔位)
        JButton btnOrderQuery = new JButton("② 訂單查詢/統計 (TODO)");
        btnOrderQuery.setBackground(new Color(210, 210, 210));
        btnOrderQuery.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
        btnOrderQuery.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "訂單查詢功能正在開發中！", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(btnOrderQuery);
        
        // 3. 會員管理按鈕 (此功能尚未實作，先作為導航佔位)
        JButton btnMemberMgmt = new JButton("③ 會員資料管理 (TODO)");
        btnMemberMgmt.setBackground(new Color(210, 210, 210));
        btnMemberMgmt.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
        btnMemberMgmt.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "會員管理功能正在開發中！", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(btnMemberMgmt);
        
        return buttonPanel;
    }
}
