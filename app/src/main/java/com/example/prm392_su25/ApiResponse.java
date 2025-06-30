// ApiResponse.java
package com.example.prm392_su25;

import java.util.List;

public class ApiResponse<T> {
    public int statusCode;
    public boolean isSuccess;
    public List<String> errorMessages;
    public T result;
    public T getResult() {
        return result;
    }
}
