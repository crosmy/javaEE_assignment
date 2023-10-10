import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniApplicationContext {
    private Map<String, Object> beans;

    public MiniApplicationContext(String configLocation) {
        beans = new HashMap<>();
        parseXml(configLocation);
        injectDependencies();
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    private void parseXml(String configLocation) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(configLocation);
            Element root = document.getRootElement();
            List<Element> beanElements = root.elements("bean");
            for (Element beanElement : beanElements) {
                String name = beanElement.attributeValue("name");
                String className = beanElement.attributeValue("class");
                Class<?> clazz = Class.forName(className);
                Object bean = clazz.newInstance();
                beans.put(name, bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectDependencies() {
        for (Object bean : beans.values()) {
            try {
                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        String fieldName = field.getName();
                        Object dependency = beans.get(fieldName);
                        field.setAccessible(true);
                        field.set(bean, dependency);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MiniApplicationContext context = new MiniApplicationContext("config.xml");
        myClass MyClass = (myClass) context.getBean("MyClass");
        MyClass.printMessage();
    }
}