package df.middleware.mybatis.reflection.property;

import java.util.Iterator;

/**
 * @description 属性分解标记
 * 可以解析处理简单属性以及list集合Map等等的数据
 * 亮点：可迭代,可以看看
 */
public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {

    // 例子：班级[0].学生.成绩
    // 班级，父表达式
    private String name;
    // 班级[0]，带索引的表达式，由父表达式和下标组成
    private String indexedName;
    // 0 ，下标，该属性只对字段类型为map|list|array类型的字段有效，对于list和array类型的字段，index保存的是下标。
    private String index;
    // 学生.成绩，，子表达式:该属性只对嵌套表达式有效
    private String children;

    public PropertyTokenizer(String fullname) {
        // 班级[0].学生.成绩
        // 找这个点 .
        // 解析出parent表达式和children表达式
        int delim = fullname.indexOf('.');
        if (delim > -1) {
            name = fullname.substring(0, delim);//截取到parent表达式
            children = fullname.substring(delim + 1);//截取到children表达式
        } else {
            // 找不到.的话，取全部部分
            name = fullname;//fullname 即为parent表达式
            children = null;//无children
        }
        indexedName = name;
        // 把中括号里的数字给解析出来
        delim = name.indexOf('[');
        if (delim > -1) {//如果有下标
            index = name.substring(delim + 1, name.length() - 1);////保存下标到index
            name = name.substring(0, delim);//3.截取出field name，
        }
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getChildren() {
        return children;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    // 取得下一个,非常简单，直接再通过儿子来new另外一个实例
    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }

    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }
}
