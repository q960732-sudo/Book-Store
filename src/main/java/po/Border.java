package po;

import java.time.LocalDateTime;

public class Border {
	private int orderId;
    private int userId; 
    private LocalDateTime orderTime; 
    private int totalAmount; 
    private int paymentReceived; 
    private int changeDue;
    
	public Border() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Border(int userId, LocalDateTime orderTime, int totalAmount, int paymentReceived, int changeDue) {
		super();
		this.userId = userId;
		this.orderTime = orderTime;
		this.totalAmount = totalAmount;
		this.paymentReceived = paymentReceived;
		this.changeDue = changeDue;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getPaymentReceived() {
		return paymentReceived;
	}

	public void setPaymentReceived(int paymentReceived) {
		this.paymentReceived = paymentReceived;
	}

	public int getChangeDue() {
		return changeDue;
	}

	public void setChangeDue(int changeDue) {
		this.changeDue = changeDue;
	} 
	
	

}
