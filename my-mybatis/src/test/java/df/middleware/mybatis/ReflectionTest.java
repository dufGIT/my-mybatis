package df.middleware.mybatis;

import cn.hutool.json.JSON;
import df.middleware.mybatis.reflection.MetaObject;
import df.middleware.mybatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/30 10:10
 * @Version 1.0
 */
public class ReflectionTest {

    private Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void test_reflection() {
        Teacher teacher = new Teacher();
        List<Teacher.Student> list = new ArrayList<>();
        list.add(new Teacher().student);
        teacher.setName("小傅哥");
        teacher.setStudents(list);
        MetaObject metaObject = SystemMetaObject.forObject(teacher);
        //logger.info("getGetterNames：{}", metaObject.getGetterNames());
        //logger.info("getSetterNames：{}", metaObject.getSetterNames());
        //logger.info("name的get方法返回值：{}", metaObject.getGetterType("name"));
        //logger.info("students的set方法参数值：{}", metaObject.getGetterType("name"));
        //logger.info("获取值：{}", metaObject.getValue("name"));
        //logger.info("设置值获取值：{}", metaObject.getValue("name"));
        // 设置属性（集合）的元素值
        metaObject.setValue("students[0].id", "001");
        logger.info("获取students集合的第一个元素的属性值：{}", metaObject.getValue("students[0].id"));
    }

    static class Teacher {

        private String name;

        private double price;

         private List<Student> students;

         private Student student;

        public static class Student {

             private String id;

             public String getId() {
                 return id;
             }

             public void setId(String id) {
                 this.id = id;
             }
         }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }
    }
}
