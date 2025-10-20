package config;

import po.service.BorderService;
import po.service.ProductService;
import po.service.UserService;
import po.service.impl.BorderServiceImpl;
import po.service.impl.ProductServiceImpl;
import po.service.impl.UserServiceImpl;

public class AppConfig {
	private static final UserService userService = new UserServiceImpl();
    private static final ProductService productService = new ProductServiceImpl();
    private static final BorderService borderService = new BorderServiceImpl();
    
    private AppConfig() {}

    public static UserService getUserService() {
        return userService;
    }

    public static ProductService getProductService() {
        return productService;
    }

    public static BorderService getBorderService() {
        return borderService;
    }
}
