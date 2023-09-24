package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            // 读取属性文件
            Properties properties = loadProperties("myapp.properties");

            // 获取配置的类名
            String className = properties.getProperty("bootstrapClass");

            // 根据类名创建对象
            Class<?> clazz = Class.forName(className);
            Object object = clazz.newInstance();

            // 调用带有@InitMethod注解的初始化方法
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(InitMethod.class)) {
                    method.invoke(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);
        properties.load(inputStream);
        inputStream.close();
        return properties;
    }
}
