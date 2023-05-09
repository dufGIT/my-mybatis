package df.middleware.mybatis.builder;

import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.mapping.ResultMap;
import df.middleware.mybatis.mapping.SqlCommandType;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.scripting.LanguageDriver;
import df.middleware.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author df
 * @Date 2023/3/29 12:38
 * @Version 1.0
 * 与XMLStatementBuilder是聚合关系，聚合关系使用空心菱形表示，聚合表示
 * 集体与个体（整体与部分）之间关联的关系，MapperBuilderAssistant就是XMLStatementBuilder的
 * 一部分，帮忙处理mapper绑定的部分
 *
 * 也有组合关系，组合关系比聚合关系关联性更强，组合关系生命周期相同，
 * 理解聚合与组合的区别，主要在于聚合的成员可独立，组合的成员必须依赖于整体才有意义。
 * 如果关系可以分开，就是聚合，不可以分开就是组合。
 * */
public class MapperBuilderAssistant extends BaseBuilder {
    private String currentNamespace;
    private String resource;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    /**
     * 将当前namespace和id字符串进行拼接
     * */
    public String applyCurrentNamespace(String base, Boolean isReferent) {
        if (base == null)
            return null;
        if (isReferent) {
            if (base.contains(".")) return base;
        }
        return currentNamespace + "." + base;
    }

    /**
     * 添加映射器语句
     */
    public MappedStatement addMapperStatement(
            String id,
            SqlSource sqlSource,
            SqlCommandType sqlCommandType,
            Class<?> parameterType,
            String resultMap,
            Class<?> resultType,
            LanguageDriver lang
    ) {
        id = applyCurrentNamespace(id, false);
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlCommandType, sqlSource, resultType);

        setStatementResultMap(resultMap, resultType, statementBuilder);

        MappedStatement mappedStatement = statementBuilder.build();
        configuration.addMappedStatement(mappedStatement);
        return mappedStatement;
    }

    // 封装ResultMap对象
    private void setStatementResultMap(
            String resultMap,
            Class<?> resultType,
            MappedStatement.Builder statementBuilder
    ) {
        resultMap = applyCurrentNamespace(resultMap, true);
        List<ResultMap> resultMaps = new ArrayList<>();

        if (resultMap != null) {
             // 暂时先不处理resultMap情况
        } else if (resultType != null) {
            // 适配一下resultType也封装处理为ResultMap
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration,
                    statementBuilder.id() + "-Inline",
                    resultType,
                    new ArrayList());
            resultMaps.add(inlineResultMapBuilder.build());
        }
        statementBuilder.resultMaps(resultMaps);
    }
}
