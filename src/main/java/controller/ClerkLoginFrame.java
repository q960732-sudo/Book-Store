package controller;

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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import config.AppConfig;
import config.UserRole;
import exception.BusinessException;
import po.User;
import java.awt.Color;

public class ClerkLoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtAccount;
    private JPasswordField txtPassword;

    public ClerkLoginFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 250);
        setTitle("員工登入");
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblTitle = new JLabel("員工登入 - 系統管理", SwingConstants.CENTER);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        contentPane.add(lblTitle);
        contentPane.add(new JLabel("")); 

        JLabel label = new JLabel("帳號:", SwingConstants.RIGHT);
        label.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        contentPane.add(label);
        txtAccount = new JTextField();
        contentPane.add(txtAccount);

        JLabel label_1 = new JLabel("密碼:", SwingConstants.RIGHT);
        label_1.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        contentPane.add(label_1);
        txtPassword = new JPasswordField();
        contentPane.add(txtPassword);

        JButton btnBack = new JButton("返回首頁");
        btnBack.setBackground(new Color(192, 192, 192));
        btnBack.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        btnBack.addActionListener(e -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            dispose();
        });
        contentPane.add(btnBack);
        
        JButton btnLogin = new JButton("登入");
        btnLogin.setBackground(new Color(192, 192, 192));
        btnLogin.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        contentPane.add(btnLogin);
    }
    
    /** 執行登入邏輯 */
    private void performLogin() {
        String account = txtAccount.getText();
        String password = new String(txtPassword.getPassword());
        
        try {
            User user = AppConfig.getUserService().login(account, password);
            
            if (!UserRole.CLERK.equals(user.getRole())) {
                throw new BusinessException("權限不足，只有員工可以登入系統管理介面。");
            }
            
            JOptionPane.showMessageDialog(this, "登入成功！歡迎 " + user.getDisplayName() + "。", "成功", JOptionPane.INFORMATION_MESSAGE);
            
            ClerkFunctionFrame frame = new ClerkFunctionFrame(user);
            frame.setVisible(true);
            dispose(); 
            
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, "登入失敗：" + e.getMessage(), "登入失敗", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText(""); 
        }
    }
}
