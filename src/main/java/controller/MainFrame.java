package controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import po.User;
import po.service.BorderService;
import po.service.ProductService;
import po.service.UserService;
import po.service.impl.BorderServiceImpl;
import po.service.impl.ProductServiceImpl;
import po.service.impl.UserServiceImpl;
import java.awt.Color;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
    
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setTitle("書旅人");
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // 標題
        JLabel lblTitle = new JLabel("請選擇您的身份", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(0, 0, 0));
        lblTitle.setBounds(20, 20, 394, 28);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        contentPane.add(lblTitle);
        
        // 按鈕區
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(128, 128, 128));
        buttonPanel.setBounds(20, 78, 394, 110);
        
        // 1. 會員登入按鈕
        JButton btnMemberLogin = new JButton("會員登入 (開始購買)");
        btnMemberLogin.setBackground(new Color(192, 192, 192));
        btnMemberLogin.setBounds(100, 0, 187, 38);
        btnMemberLogin.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        btnMemberLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 開啟 MemberLoginFrame
                MemberLoginFrame frame = new MemberLoginFrame();
                frame.setVisible(true);
                // 關閉當前視窗
                dispose(); 
            }
        });
        buttonPanel.setLayout(null);
        buttonPanel.add(btnMemberLogin);
        
        // 2. 員工登入按鈕
        JButton btnClerkLogin = new JButton("員工登入 (系統管理)");
        btnClerkLogin.setBackground(new Color(192, 192, 192));
        btnClerkLogin.setBounds(100, 62, 187, 38);
        btnClerkLogin.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        btnClerkLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClerkLoginFrame frame = new ClerkLoginFrame();
                frame.setVisible(true);
                dispose(); 
            }
        });
        buttonPanel.add(btnClerkLogin);
        
        contentPane.add(buttonPanel);
        
        // 退出按鈕
        JButton btnExit = new JButton("退出系統");
        btnExit.setBackground(new Color(192, 192, 192));
        btnExit.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        btnExit.setBounds(148, 214, 134, 23);
        btnExit.addActionListener(e -> System.exit(0));
        contentPane.add(btnExit);
    }
    
}
