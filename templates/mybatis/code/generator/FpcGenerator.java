package #【packageName】.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author lqy
 * @description TODO 自动生成Mybatis 代码
 * @date 2023/2/2 16:39
 **/
public class FpcGenerator {

    public static void main(String[] args) {
        //用来获取Mybatis-Plus.properties文件的配置信息
        final ResourceBundle rb = ResourceBundle.getBundle("generator/mybatis-plus");
        //获取当前工程路径
        String projectPath = System.getProperty("user.dir");

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setFileOverride(true);// 是否覆盖文件
        gc.setOpen(false);// 生成后是否打开目录
        gc.setBaseResultMap(true);//生成SQL映射文件
        gc.setBaseColumnList(true);//sql片段
        gc.setActiveRecord(true);//是否支持AR模式 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(true);// XML 二级缓存
        gc.setAuthor(rb.getString("author"));
        gc.setSwagger2(true);//使用swagger
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setEntityName("%sVo");//实体类生成策略
        gc.setServiceName("%sService"); //service接口的名字的首字母是否为I//这样设置接口名没有I
        gc.setIdType(IdType.AUTO);//主键生成方式
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(rb.getString("url"));
        dsc.setDriverName(rb.getString("driver"));
        dsc.setUsername(rb.getString("username"));
        dsc.setPassword(rb.getString("password"));
        if (!rb.getString("schema").equals(""))
            dsc.setSchemaName(rb.getString("schema"));
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(rb.getString("parent"));
        pc.setController("controller" + (rb.getString("className").equals("") ? "" : "." + rb.getString("className")));
        pc.setService("service" + (rb.getString("className").equals("") ? "" : "." + rb.getString("className")));
        pc.setServiceImpl("service" + (rb.getString("className").equals("") ? "" : "." + rb.getString("className")) + ".impl");
        pc.setEntity("pojo" + (rb.getString("className").equals("") ? "" : "." + rb.getString("className")));
        pc.setMapper("mapper" + (rb.getString("className").equals("") ? "" : "." + rb.getString("className")));
        mpg.setPackageInfo(pc);
                
		//策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的策略 下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//字段策略 下划线转驼峰
        strategy.setEntityLombokModel(true);//设置实体Lombok模型
        strategy.setRestControllerStyle(true);//是否使用RestController
        strategy.setTablePrefix(rb.getString("tablePrefix").split(","));//过滤带此前缀的表
        if (!rb.getString("tableName").trim().equals("")) {
            strategy.setInclude(rb.getString("tableName").split(",")); // 设置要映射的表名 不配置默认处理全部表
        }
        // 自动填充 (表中如果有创建时间、修改时间话，可以使用自动填充)
        String createTimeField = rb.getString("createTimeField");//创建时间填充字段
        String updateTimeField = rb.getString("updateTimeField");//更新时间填充字段
        ArrayList<TableFill> tableFills = new ArrayList<>();
        if (!createTimeField.trim().equals("")) {
            TableFill createTime = new TableFill(createTimeField, FieldFill.INSERT);
            tableFills.add(createTime);
        }

        if (!updateTimeField.trim().equals("")) {
            TableFill updateTime = new TableFill(updateTimeField, FieldFill.INSERT_UPDATE);
            tableFills.add(updateTime);
        }

        if (tableFills.size() > 0)
            strategy.setTableFillList(tableFills);

        //自定义配置
        String outputDirXml = rb.getString("outputDirXml");
        if (!outputDirXml.equals("")) {
            pc.setXml(null);//设置不重复生成xml
            // 自定义配置
            InjectionConfig cfg = new InjectionConfig() {
                @Override
                public void initMap() {
                    // to do nothing
                }
            };

            // 如果模板引擎是 freemarker
            String templatePath = "/templates/mapper.xml.ftl";
            // 如果模板引擎是 velocity
            //String templatePath = "/templates/mapper.xml.vm";

            // 自定义输出配置
            List<FileOutConfig> focList = new ArrayList<>();
            // 自定义配置会被优先输出
            // 这里自定义配置的是*Mapper.xml文件
            // 所以templatePath = "/templates/mapper.xml.vm";
            // 如果你想自定义配置其它 修改templatePath即可
            focList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名 如果你 Entity 设置了前后缀
                    String entityName = tableInfo.getEntityName();
                    int length = entityName.length();
                    entityName = entityName.substring(0, length - 6);
                    // 自定义输入文件名称
                    return  projectPath + outputDirXml +
                            entityName + "Mapper" + StringPool.DOT_XML;
                }
            });
            cfg.setFileOutConfigList(focList);
            mpg.setCfg(cfg);
        }



        //配置代码生成器
        mpg.setGlobalConfig(gc);
        //配置数据源
        mpg.setDataSource(dsc);

        mpg.setPackageInfo(pc);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());//模板渲染
        //执行操作
        mpg.execute();
        System.out.println("=======  Done 相关代码生成完毕  ========");

    }
}
