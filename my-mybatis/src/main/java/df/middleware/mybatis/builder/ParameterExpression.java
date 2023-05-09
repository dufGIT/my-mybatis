package df.middleware.mybatis.builder;

import java.util.HashMap;

/**
 * @Author df
 * @Date 2022/12/7 17:06
 * @Version 1.0
 * 参数表达式
 * ParameterExpression内部完成了#{}内容的分解，并将其属性以key/value的形式放到HashMap里。
 */
public class ParameterExpression extends HashMap<String, String> {
    public ParameterExpression(String expression) {
        parse(expression);
    }

    private void parse(String expression) {
        int p = skipWS(expression, 0);
        if (expression.charAt(p) == '(') {
            //expression(expression, p + 1);
        } else {
            property(expression, p);
        }
    }

    private void property(String expression, int left) {
        // #{property,javaType=int,jdbcType=NUMERIC}
        // property:VARCHAR
        if (left < expression.length()) {
            //首先，得到逗号或者冒号之前的字符串，加入到property
            int right = skipUntil(expression, left, ",:");
            put("property", trimmedStr(expression, left, right));
            // 第二，处理javaType，jdbcType
            jdbcTypeOpt(expression, right);
        }
    }

    private int skipUntil(String expression, int p, final String endChars) {
        for (int i = p; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (endChars.indexOf(c) > -1) {
                return i;
            }
        }
        return expression.length();
    }
    private String trimmedStr(String str, int start, int end) {
        while (str.charAt(start) <= 0x20) {
            start++;
        }
        while (str.charAt(end - 1) <= 0x20) {
            end--;
        }
        return start >= end ? "" : str.substring(start, end);
    }

    private void jdbcTypeOpt(String expression, int p) {
        // #{property,javaType=int,jdbcType=NUMERIC}
        // property:VARCHAR
        // 首先去除空白,返回的p是第一个不是空白的字符位置
        p = skipWS(expression, p);
        if (p < expression.length()) {
            //第一个property解析完有两种情况，逗号和冒号
            if (expression.charAt(p) == ':') {
                //jdbcType(expression, p + 1);
            } else if (expression.charAt(p) == ',') {
               // option(expression, p + 1);
            } else {
                throw new RuntimeException("Parsing error in {" + new String(expression) + "} in position " + p);
            }
        }
    }


    private int skipWS(String expression, int p) {
        for (int i = p; i < expression.length(); i++) {
            if (expression.charAt(i) > 0x20) {
                return i;
            }
        }
        return expression.length();
    }

}
