package po;

public class BorderDetail {
	private int detailId;
    private int orderId;
    private int productId;
    private int quantity; 
    private int unitPrice; // 當時單價 (INT)
    private int subtotal;  // 小計 (INT)
    
	public BorderDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BorderDetail(int orderId, int productId, int quantity, int unitPrice, int subtotal) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.subtotal = subtotal;
	}

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(int subtotal) {
		this.subtotal = subtotal;
	}
    
	
}

