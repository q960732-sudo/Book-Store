package vo;

import java.time.LocalDateTime;

public class BorderSummaryVO {
	private int orderId;      
    private LocalDateTime orderTime; 
    private int totalAmount;    
    private String clerkDisplayName; 
    private int clerkId;        

    public BorderSummaryVO() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getClerkDisplayName() {
        return clerkDisplayName;
    }

    public void setClerkDisplayName(String clerkDisplayName) {
        this.clerkDisplayName = clerkDisplayName;
    }
    
    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }
}
