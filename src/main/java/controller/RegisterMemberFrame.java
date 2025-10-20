package controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import exception.BusinessException;
import po.User;
import config.AppConfig;
import config.UserRole;

import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class RegisterMemberFrame extends JFrame {
	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtAccount;
    private JPasswordField txtPassword;
    private JTextField txtDisplayName;
    private JFrame callingFrame;
   
    public RegisterMemberFrame(JFrame callingFrame) {
        this.callingFrame = callingFrame;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        setTitle("會員註冊");
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("新會員註冊", SwingConstants.CENTER);
        lblTitle.setBounds(107, 15, 197, 48);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        contentPane.add(lblTitle);
        JLabel label = new JLabel("");
        label.setBounds(222, 15, 197, 48);
        contentPane.add(label); // 佔位

        JLabel label_1 = new JLabel("帳            號:", SwingConstants.RIGHT);
        label_1.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        label_1.setBounds(-46, 73, 197, 37);
        contentPane.add(label_1);
        txtAccount = new JTextField();
        txtAccount.setBounds(161, 73, 197, 37);
        contentPane.add(txtAccount);

        JLabel label_2 = new JLabel("密            碼:", SwingConstants.RIGHT);
        label_2.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        label_2.setBounds(-46, 131, 197, 37);
        contentPane.add(label_2);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(161, 131, 197, 37);
        contentPane.add(txtPassword);
        
        JLabel label_3 = new JLabel("姓名 / 暱稱:", SwingConstants.RIGHT);
        label_3.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        label_3.setBounds(-46, 189, 197, 37);
        contentPane.add(label_3);
        txtDisplayName = new JTextField();
        txtDisplayName.setBounds(161, 189, 197, 37);
        contentPane.add(txtDisplayName);

        JButton btnBack = new JButton("取消/返回");
        btnBack.setBackground(new Color(192, 192, 192));
        btnBack.setBounds(47, 258, 159, 37);
        btnBack.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        btnBack.addActionListener(e -> {
            callingFrame.setVisible(true);
            dispose();
        });
        contentPane.add(btnBack);
        
        JButton btnRegister = new JButton("確認註冊");
        btnRegister.setBackground(new Color(192, 192, 192));
        btnRegister.setBounds(233, 258, 159, 37);
        btnRegister.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });
        contentPane.add(btnRegister);
    }
    
    private void performRegistration() {
        String account = txtAccount.getText().trim();
        String password = new String(txtPassword.getPassword());
        String displayName = txtDisplayName.getText().trim();
        
        if (account.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有欄位皆為必填。", "輸入錯誤", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            User newUser = new User();
            newUser.setAccountName(account); 
            newUser.setPassword(password);
            newUser.setDisplayName(displayName);
            newUser.setRole(UserRole.MEMBER); 
            
            AppConfig.getUserService().registerMember(newUser); 
            
            JOptionPane.showMessageDialog(this, "註冊成功！您現在可以使用此帳號登入購買。\n請返回登入。", "成功", JOptionPane.INFORMATION_MESSAGE);
            
            callingFrame.setVisible(true); 
            dispose(); 
            
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, "註冊失敗：" + e.getMessage(), "註冊錯誤", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "系統錯誤：請檢查 User PO 是否有 setAccountName/setPassword 等方法。", "系統錯誤", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
