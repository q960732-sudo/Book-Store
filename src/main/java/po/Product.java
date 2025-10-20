package po;

public class Product {
	private int id;
    private String productNo; 
    private String productName; 
    private String isbn10;
    private int totalPages;
    private int price;
    
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Product(String productNo, String productName, String isbn10, int totalPages, int price) {
		super();
		this.productNo = productNo;
		this.productName = productName;
		this.isbn10 = isbn10;
		this.totalPages = totalPages;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	} 
	
	@Override
	public String toString() {
	    return "Product [id=" + id + ", productNo=" + productNo + ", productName=" + productName 
	         + ", isbn10=" + isbn10 + ", totalPages=" + totalPages + ", price=" + price + "]";
	}
	
    
}
