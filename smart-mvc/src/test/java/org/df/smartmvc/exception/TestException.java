package org.df.smartmvc.exception;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 17:52
 **/
public class TestException extends RuntimeException {
    private String name;

    public TestException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
