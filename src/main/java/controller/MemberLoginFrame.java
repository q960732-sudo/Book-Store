package controller;

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
import exception.BusinessException;
import po.User;
import java.awt.Color;


public class MemberLoginFrame extends JFrame {
	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtAccount;
    private JPasswordField txtPassword;

    public MemberLoginFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setBounds(100, 100, 430, 321);
        setTitle("會員登入");
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("會員登入", SwingConstants.CENTER);
        lblTitle.setBounds(115, 16, 172, 37);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        contentPane.add(lblTitle);
        JLabel label = new JLabel("");
        label.setBounds(197, 16, 172, 37);
        contentPane.add(label); 

        JLabel label_1 = new JLabel("帳            號:", SwingConstants.RIGHT);
        label_1.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        label_1.setBounds(-16, 80, 172, 37);
        contentPane.add(label_1);
        txtAccount = new JTextField();
        txtAccount.setBounds(181, 84, 172, 37);
        contentPane.add(txtAccount);

        JLabel label_2 = new JLabel("密            碼:", SwingConstants.RIGHT);
        label_2.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        label_2.setBounds(-16, 139, 172, 37);
        contentPane.add(label_2);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(181, 143, 172, 37);
        contentPane.add(txtPassword);

        JButton btnRegister = new JButton("註冊會員");
        btnRegister.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        btnRegister.setBounds(66, 207, 130, 37);
        btnRegister.setBackground(new Color(192, 192, 192));
        btnRegister.addActionListener(e -> {
            RegisterMemberFrame frame = new RegisterMemberFrame(this);
            frame.setVisible(true);
            this.setVisible(false); 
        });
        contentPane.add(btnRegister);
        
        JButton btnLogin = new JButton("登入");
        btnLogin.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        btnLogin.setBounds(225, 207, 130, 37);
        btnLogin.setBackground(new Color(192, 192, 192));
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        contentPane.add(btnLogin);
    }
    
    private void performLogin() {
        String account = txtAccount.getText();
        String password = new String(txtPassword.getPassword());
        
        try {
            User user = AppConfig.getUserService().login(account, password);
            
            JOptionPane.showMessageDialog(this, "登入成功！歡迎 " + user.getDisplayName() + "。", "成功", JOptionPane.INFORMATION_MESSAGE);
            
            CheckoutFrame frame = new CheckoutFrame(user);
            frame.setVisible(true);
            dispose(); 
            
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, "登入失敗：帳號或密碼錯誤。\n請重新登入。", "登入失敗", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
}
