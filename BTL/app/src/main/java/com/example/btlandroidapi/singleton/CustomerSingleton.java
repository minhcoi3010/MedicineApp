package com.example.btlandroidapi.singleton;

public class CustomerSingleton {
    private static CustomerSingleton instance;
    private String customerId;

    // Private constructor để ngăn việc tạo đối tượng từ bên ngoài
    private CustomerSingleton() {
    }

    // Phương thức getInstance để lấy thể hiện duy nhất của lớp
    public static CustomerSingleton getInstance() {
        if (instance == null) {
            instance = new CustomerSingleton();
        }
        return instance;
    }

    // Phương thức để đặt mã Khách hàng khi đăng nhập
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    // Phương thức để lấy mã Khách hàng
    public String getCustomerId() {
        return customerId;
    }
}

