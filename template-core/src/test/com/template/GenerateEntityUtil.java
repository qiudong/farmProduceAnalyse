package com.suixingpay;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.suixingpay.takin.util.time.ClockUtils;
import com.suixingpay.takin.util.time.DateFormatUtils;


/**
 * @Description: java类作用描述
 * @Author: qiudong
 * @CreateDate: 2019-04-10 11:55
 * @UpdateUser: qiudong
 * @UpdateDate: 2019-04-10 11:55
 * @Version: V1.0
 */
public class GenerateEntityUtil {

    // 如果为null，遍历所有表
    private String tableName = "f_report_read_info";
    // 如果为null，则驼峰命名
    private String beanName = null;
    // 是否开启驼峰命名转换
    private final boolean processField = true;

    private final String mapper_extends = "GenericMapper";
    private final String service = "service";



    private final static String _path = "/Users/qiudong/project/farcloud-platform/commons/";
    private final String _package = "com.suixingpay.saas.userprovider.dao.";

    private final String bean = "domain";
    private final String mapper = "mapper";
    private final String java_path = "/src/main/java/com/suixingpay/saas/userprovider/dao/";
    private final String resources_path = "/src/main/resources/com/suixingpay/saas/userprovider/dao/";

    private final String bean_path = _path + "farcloud-core" + java_path + bean + "/";
    private final String mapper_path = _path + "farcloud-core" + java_path + mapper + "/";
    private final String xml_path = _path + "farcloud-core" + resources_path + mapper + "/";
    private final String service_package = _package + service;

    private final String service_extends = "AbstractService";


    //	private final String client_path = _path  + "globalg-trading-dao-client" + java_path + client + "/" + datebase + "/";
    private final String service_path = _path + "farcloud-core" + java_path + service  + "/";


    private final String bean_package = _package + bean;
    private final String mapper_package = _package + mapper;


    // private final String user = "query";
    // private final String password = "query_on";
    // private final String driverName = "oracle.jdbc.driver.OracleDriver";
    // private final String url = "jdbc:oracle:thin:@172.16.135.252:1521/BAPDB";

    private final String user = "root";
    private final String password = "suxingpay,123";
    //	private final String driverName = "com.mysql.cj.jdbc.Driver";
    private final String driverName = "com.mysql.jdbc.Driver";

    private final String url = "jdbc:mysql://172.16.143.213:30426/farcloud_platform?useUnicode=true&characterEncoding=utf-8&useSSL=false";

    private final String type_char = "char";

    private final String type_date = "date";

    private final String type_timestamp = "timestamp";

    private final String type_int = "int";

    private final String type_bigint = "bigint";

    private final String type_text = "text";

    private final String type_bit = "bit";

    private final String type_decimal = "decimal";

    private final String type_blob = "blob";


    private String mapperName = null;
    private String serviceName = null;
    private String apiName = null;
    private String controllerName = null;
    private String clientName = null;

    private Connection conn = null;

    private void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);
    }

    /**
     * 获取所有的表
     *
     * @return
     * @throws SQLException
     */
    private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
        if (null != tableName) {
            tables.add(tableName);
            return tables;
        }
        PreparedStatement pstate = conn.prepareStatement("show tables");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString(1);
            // if ( tableName.toLowerCase().startsWith("yy_") ) {
            tables.add(tableName);
            // }
        }
        return tables;
    }

    private void processTable(String table) {
        if (table.substring(0, 2).equals("f_")) table = table.substring(2, tableName.length());
        StringBuilder sb = new StringBuilder(table.length());
        String tableNew = table.toLowerCase();
        String[] tables = tableNew.split("_");
        String temp = null;
        for (int i = 0; i < tables.length; i++) {
            temp = tables[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        beanName = sb.toString();
        mapperName = beanName + "Mapper";
        serviceName = beanName + "Service";
        apiName = beanName + "Api";
        controllerName = beanName + "Controller";
        clientName = apiName + "Client";
    }

    /**
     * 获取列类型
     *
     * @param type
     * @return
     */
    private String processType(String type) {
        if (type.indexOf(type_char) > -1) {
            return "String";
        } else if (type.indexOf(type_bigint) > -1) {
            return "Long";
        } else if (type.indexOf(type_int) > -1) {
            return "Integer";
        } else if (type.indexOf(type_date) > -1) {
            return "java.util.Date";
        } else if (type.indexOf(type_text) > -1) {
            return "String";
        } else if (type.indexOf(type_timestamp) > -1) {
            return "java.util.Date";
        } else if (type.indexOf(type_bit) > -1) {
            return "Boolean";
        } else if (type.indexOf(type_decimal) > -1) {
            return "java.math.BigDecimal";
        } else if (type.indexOf(type_blob) > -1) {
            return "byte[]";
        }
        return null;
    }


    /**
     * 获取列类型
     *
     * @param type
     * @return
     */
    private String _processType(String type) {
        if (type.indexOf(type_char) > -1) {
            return "String";
        } else if (type.indexOf(type_bigint) > -1) {
            return "Long";
        } else if (type.indexOf(type_int) > -1) {
            return "Integer";
        } else if (type.indexOf(type_date) > -1) {
            return "Date";
        } else if (type.indexOf(type_text) > -1) {
            return "String";
        } else if (type.indexOf(type_timestamp) > -1) {
            return "Date";
        } else if (type.indexOf(type_bit) > -1) {
            return "Boolean";
        } else if (type.indexOf(type_decimal) > -1) {
            return "BigDecimal";
        } else if (type.indexOf(type_blob) > -1) {
            return "byte[]";
        }
        return null;
    }

    /**
     * 驼峰命名
     *
     * @param field
     * @return
     */
    private String processField(String field) {
        if (!processField) {
            return field;
        }
        StringBuffer sb = new StringBuffer(field.length());
        field = field.toLowerCase();
        String[] fields = field.split("_");
        String temp = null;
        sb.append(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }

    /**
     * 将实体类名首字母改为小写
     *
     * @param beanName
     * @return
     */
    private String processResultMapId(String beanName) {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }

    /**
     * 构建类上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException
     */
    private BufferedWriter buildClassComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" * @author:  qiudong");
        bw.newLine();
        bw.write(" * @date: " + DateFormatUtils.formatDate(DateFormatUtils.PATTERN_ISO, ClockUtils.currentDate()));
        bw.newLine();
        bw.write(" * @version: V1.0");
        bw.newLine();
        bw.write(" * @review: qiudong/" + DateFormatUtils.formatDate(DateFormatUtils.PATTERN_ISO, ClockUtils.currentDate()));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
        return bw;
    }

    /**
     * 构建方法上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException
     */
    // private BufferedWriter buildMethodComment(BufferedWriter bw, String text)
    // throws IOException {
    // bw.newLine();
    // bw.write("\t/**");
    // bw.newLine();
    // bw.write("\t * " + text);
    // bw.newLine();
    // bw.write("\t **/");
    // return bw;
    // }

    /**
     * 生成实体类
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
    private void buildEntityBean(List<String> columns, List<String> types, List<String> comments, String tableComment)
            throws IOException {
        File beanFile = new File(bean_path + beanName + ".java");
        // 先得到文件的上级目录，并创建上级目录，在创建文件
        if (!beanFile.getParentFile().exists()) {
            beanFile.getParentFile().mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + bean_package + ";");
        bw.newLine();
        bw.newLine();
        Long dateCount = columns.stream().filter(str -> {
            boolean flag = types.stream().anyMatch(clou -> "Date".equals(_processType(clou)));
            return flag;
        }).count();
        Long bigDecimalCount = columns.stream().filter(str -> {
            boolean flag = types.stream().anyMatch(clou -> "BigDecimal".equals(_processType(clou)));
            return flag;
        }).count();
        if (dateCount > 0) {
            bw.write("import java.util.Date;");
            bw.newLine();
        }

        if (bigDecimalCount > 0) {
            bw.write("import java.math.BigDecimal;");
            bw.newLine();
        }
        bw.write("import com.suixingpay.takin.data.domain.BaseDomain;");
        bw.newLine();
        bw.write("import io.swagger.annotations.ApiModel;");
        bw.newLine();

        bw.write("import io.swagger.annotations.ApiModelProperty;");
        bw.newLine();
        bw.write("import lombok.Data;");
        bw.newLine();
        bw.write("import lombok.EqualsAndHashCode;");
        bw.newLine();
        bw.write("import lombok.experimental.Accessors;");
        bw = buildClassComment(bw, tableComment);
        bw.write("@ApiModel");
        bw.newLine();
        bw.write("@Data");
        bw.newLine();
        bw.write("@Accessors(chain = true)");
        bw.newLine();
        bw.write("public class " + beanName + " {");
        bw.newLine();
        bw.newLine();
        bw.write("\tprivate static final long serialVersionUID = 1L;");
        bw.newLine();
        bw.newLine();
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            String str = comments.get(i);
            str = str.contains("\"") ? str.replace("\"", "'") : str;
            bw.write("\t@ApiModelProperty(\"" + str + "\")");
            bw.newLine();
            bw.write("\tprivate " + _processType(types.get(i)) + " " + processField(columns.get(i)) + ";");
            bw.newLine();
            bw.newLine();
        }
        bw.write("}");
        bw.flush();
        bw.close();
    }

    /**
     * 构建Mapper文件
     *
     * @throws IOException
     */
    private void buildMapper() throws IOException {
        File mapperFile = new File(mapper_path, mapperName + ".java");
        // 先得到文件的上级目录，并创建上级目录，在创建文件
        if (!mapperFile.getParentFile().exists()) {
            mapperFile.getParentFile().mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + mapper_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import com.suixingpay.takin.mybatis.mapper.GenericMapper;");
        bw = buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + ", String> {");
        bw.newLine();
        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }

    /**
     * 构建Service文件
     *
     * @throws IOException //
     */
    private void buildService() throws IOException {
        File serviceFile = new File(service_path, serviceName + ".java");
        // 先得到文件的上级目录，并创建上级目录，在创建文件
        if (!serviceFile.getParentFile().exists()) {
            serviceFile.getParentFile().mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(serviceFile), "utf-8"));
        bw.write("package " + service_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import " + mapper_package + "." + mapperName + ";");
        bw.newLine();
        bw.write("import com.suixingpay.takin.mybatis.service.AbstractService;");
        bw.newLine();
        bw.write("import org.springframework.stereotype.Service;");
        bw.newLine();
        bw.write("import org.springframework.beans.factory.annotation.Autowired;");
        bw = buildClassComment(bw, serviceName + "业务实现类");
        bw.write("@Service");
        bw.newLine();
        bw.write("public class " + serviceName + " implements " + service_extends + "<" + beanName + ", String> {");
        bw.newLine();
        bw.newLine();
        bw.write("\t@Autowired");
        bw.newLine();
        bw.write("\tprivate " + mapperName + " " + processResultMapId(mapperName) + ";");
        bw.newLine();
        bw.newLine();
        bw.write("\t@Override");
        bw.newLine();
        bw.write("\tpublic " + mapperName + " getMapper() {");
        bw.newLine();
        bw.write("\t\treturn " + processResultMapId(mapperName) + ";");
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }

    /**
     * 构建Controller文件
     *
     * @throws IOException
     */
//    private void buildController(String tableComment) throws IOException {
//        File controllerFile = new File(controller_path, controllerName + ".java");
//        // 先得到文件的上级目录，并创建上级目录，在创建文件
//        if (!controllerFile.getParentFile().exists()) {
//            controllerFile.getParentFile().mkdirs();
//        }
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(controllerFile), "utf-8"));
//        bw.write("package " + controller_package + ";");
//        bw.newLine();
//        bw.newLine();
//        bw.write("import org.springframework.beans.factory.annotation.Autowired;");
//        bw.newLine();
//        bw.write("import org.springframework.web.bind.annotation.RestController;");
//        bw.newLine();
//        bw.write("import com.suixingpay.trader.controller.AbstractController;");
//        bw.newLine();
//        bw.write("import io.swagger.annotations.Api;");
//        bw.newLine();
//        bw.write("import " + bean_package + "." + beanName + ";");
//        bw.newLine();
//        bw.write("import " + service_package + "." + serviceName + ";");
//        bw.newLine();
//        bw.write("import " + api_package + "." + apiName + ";");
//        bw = buildClassComment(bw, controllerName + "业务类");
//        bw.write("@Api(tags = { \""+processResultMapId(beanName)+"\" })");
//        bw.newLine();
//        bw.write("@RestController");
//        bw.newLine();
////		bw.write("@RequestMapping(\"/"+processResultMapId(beanName)+"\")");
////		bw.newLine();
//        bw.write("public class " + controllerName + " extends AbstractController implements "+beanName+"Api {");
//        bw.newLine();
//        bw.newLine();
//        bw.write("\t@Autowired");
//        bw.newLine();
//        bw.write("\tprivate " + serviceName + " " + processResultMapId(serviceName) + ";");
//        bw.newLine();
//        bw.newLine();
//        bw.write("}");
//        bw.flush();
//        bw.close();
//    }


    /**
     * 生成api
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
//    private void buildApi() throws IOException {
//        File apiFile = new File(api_path + apiName + ".java");
//        // 先得到文件的上级目录，并创建上级目录，在创建文件
//        if (!apiFile.getParentFile().exists()) {
//            apiFile.getParentFile().mkdirs();
//        }
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(apiFile), "utf-8"));
//        bw.write("package " + api_package + ";");
//        bw.newLine();
//        bw.newLine();
//        bw.write("import org.springframework.cloud.netflix.feign.FeignClient;");
//        bw.newLine();
//        bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
//        bw.newLine();
//        bw.write("import com.suixingpay.trader.api.BaseApi;");
//        bw = buildClassComment(bw, apiName + "接口实现类");
//        bw.write("@FeignClient(value = BaseApi.SERVICE_NAME)");
//        bw.newLine();
//        bw.write("@RequestMapping(\"/" + processResultMapId(beanName) + "\")");
//        bw.newLine();
//        bw.write("public interface " + apiName +" {");
//        bw.newLine();
//        bw.newLine();
//        bw.newLine();
//        bw.write("}");
//        bw.flush();
//        bw.close();
//    }

    /**
     * 生成Client
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
//    private void buildClient() throws IOException {
////		File clientFile = new File(client_path + clientName + ".java");
//        File clientFile = new File("" + clientName + ".java");
//        // 先得到文件的上级目录，并创建上级目录，在创建文件
//        if (!clientFile.getParentFile().exists()) {
//            clientFile.getParentFile().mkdirs();
//        }
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(clientFile), "utf-8"));
//        bw.write("package " + client_package + ";");
//        bw.newLine();
//        bw.newLine();
//        bw.write("import org.springframework.cloud.netflix.feign.FeignClient;");
//        bw.newLine();
//        bw.write("");
//        bw.newLine();
//        bw.write("import " + api_package + "." + apiName + ";");
//        bw = buildClassComment(bw, clientName + "接口实现类");
//        bw.write("");
//        bw.newLine();
//        bw.write("public interface " + clientName + " extends "+apiName+" {");
//        bw.newLine();
//        bw.newLine();
//        bw.write("}");
//        bw.flush();
//        bw.close();
//    }

    /**
     * 构建实体类映射XML文件
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
    private void buildMapperXml(List<String> columns, List<String> types, List<String> comments) throws IOException {
        File mapperXmlFile = new File(xml_path, mapperName + ".xml");
        if (!mapperXmlFile.getParentFile().exists()) {
            mapperXmlFile.getParentFile().mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
        bw.newLine();
        bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        bw.newLine();
        bw.write("<mapper namespace=\"" + mapper_package + "." + mapperName + "\">");
        bw.newLine();
        bw.newLine();

        bw.write("\t<!--实体映射-->");
        bw.newLine();
        bw.write("\t<resultMap id=\"" + this.processResultMapId(beanName) + "\" type=\"" + bean_package + "." + beanName
                + "\">");
        bw.newLine();
        bw.write("\t\t<!--" + comments.get(0) + "-->");
        bw.newLine();
        bw.write("\t\t<id property=\"" + this.processField(columns.get(0)) + "\" column=\"" + columns.get(0) + "\" />");
        bw.newLine();
        int size = columns.size();
        for (int i = 1; i < size; i++) {
            bw.write("\t\t<!--" + comments.get(i) + "-->");
            bw.newLine();
            bw.write("\t\t<result property=\"" + this.processField(columns.get(i)) + "\" column=\"" + columns.get(i)
                    + "\" />");
            bw.newLine();
        }
        bw.write("\t</resultMap>");

        bw.newLine();
        bw.newLine();

        // 下面开始写SqlMapper中的方法
        buildSQL(bw, columns, types);

        bw.write("</mapper>");
        bw.flush();
        bw.close();
    }

    private void buildSQL(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
        bw.write("\t<!--表名-->");
        bw.newLine();
        bw.write("\t<sql id=\"table\">");
        bw.newLine();
        bw.write("\t\t" + tableName);
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();

        int size = columns.size();
        // 通用结果列
        bw.write("\t<!-- 通用查询结果列-->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Column_List\">");

        for (int i = 0; i < size; i++) {
            if (i % 7 == 0) {
                bw.newLine();
                bw.write("\t\t");
            }
            bw.write(columns.get(i));
            if (i != size - 1) {
                bw.write(",");
            }
        }

        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();

        // 通用查询条件
        bw.write("\t<!-- 通用查询条件-->");
        bw.newLine();
        bw.write("\t<sql id=\"dynamicWhere\">");
        bw.newLine();

        bw.write("\t\t<where>");
        bw.newLine();
        for (int i = 0; i < size; i++) {
            bw.write("\t\t\t<if test=\"");
            bw.write(" null !=" + this.processField(columns.get(i)) + " and "  + this.processField(columns.get(i)) +" != ''\"> AND " + columns.get(i));
            bw.write(" = #{" + this.processField(columns.get(i)) + "} </if>");
            bw.newLine();
        }
        bw.write("\t\t</where>");
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();

        // 查询所有数据
        bw.write("\t<!-- 查询所有数据 -->");
        bw.newLine();
        bw.write("\t<select id=\"findAll\" resultMap=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完

        // 查询（根据主键ID查询）
        bw.write("\t<!-- 查询（根据主键ID查询） -->");
        bw.newLine();
        bw.write("\t<select id=\"findOneById\" resultMap=\"" + processResultMapId(beanName)
                + "\" parameterType=\"java.lang." + processType(types.get(0)) + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完

        // 查询（根据主键ID查询）
        bw.write("\t<!-- 查询（根据实体查询） -->");
        bw.newLine();
        bw.write("\t<select id=\"find\" resultMap=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write("\t\t <include refid=\"dynamicWhere\"/>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完

        // 查询总数（根据主键ID查询）
        bw.write("\t<!-- 查询总数（根据实体查询） -->");
        bw.newLine();
        bw.write("\t<select id=\"count\" resultMap=\"" + processResultMapId(beanName) + "\" resultType=\"java.lang.Long\">");
        bw.newLine();
        bw.write("\t\t SELECT COUNT(*) FROM ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write("\t\t <include refid=\"dynamicWhere\"/>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完

        // 删除（根据主键ID删除）
        bw.write("\t<!--删除：根据主键ID删除-->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteById\" parameterType=\"java.lang." + processType(types.get(0)) + "\">");
        bw.newLine();
        bw.write("\t\t DELETE FROM ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完

        // 批量删除（根据主键ID删除）
        bw.write("\t<!--批量删除：根据主键ID删除-->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteBatchIds\">");
        bw.newLine();
        bw.write("\t\t DELETE FROM ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " in ");
        bw.newLine();
        bw.write("\t\t <foreach collection=\"pkList\" item=\"i\" open=\"(\" separator=\",\" close=\")\">");
        bw.newLine();
        bw.write("\t\t\t #{i}");
        bw.newLine();
        bw.write("\t\t </foreach>");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完

//		// 添加insert方法
//		bw.write("\t<!-- 添加 -->");
//		bw.newLine();
//		bw.write("\t<insert id=\"add\" parameterType=\"" + bean_package + "." + beanName + "\">");
//		bw.newLine();
//		bw.write("\t\t INSERT INTO ");
//		bw.newLine();
//		bw.write("\t\t <include refid=\"table\" />");
//		bw.newLine();
//		bw.write(" \t\t\t(");
//		for (int i = 0; i < size; i++) {
//			bw.write(columns.get(i));
//			if (i != size - 1) {
//				bw.write(",");
//			}
//		}
//		bw.write(") ");
//		bw.newLine();
//		bw.write("\t\t VALUES ");
//		bw.newLine();
//		bw.write(" \t\t\t(");
//		for (int i = 0; i < size; i++) {
//			bw.write("#{" + processField(columns.get(i)) + "}");
//			if (i != size - 1) {
//				bw.write(",");
//			}
//		}
//		bw.write(") ");
//		bw.newLine();
//		bw.write("\t</insert>");
//		bw.newLine();
//		bw.newLine();
//		// 添加insert完

        // --------------- insert方法（匹配有值的字段）
        bw.write("\t<!-- 添加 （匹配有值的字段）-->");
        bw.newLine();
        bw.write("\t<insert id=\"add\" parameterType=\"" + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t INSERT INTO ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        String tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            if ("Integer".equals(processType(types.get(0)))) {
                bw.write("\t\t\t<if test=\"null !=" + tempField + " and "+ tempField + " != '' \"> ");
            } else {
                bw.write("\t\t\t<if test=\"null !=" + tempField + " and "+ tempField + " != '' \"> ");
            }
            bw.write(columns.get(i) + ", </if>");
            bw.newLine();
        }

        bw.write("\t\t </trim>");
        bw.newLine();

        bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            if ("Integer".equals(processType(types.get(0)))) {
                bw.write("\t\t\t<if test=\"null !=" + tempField + " and "+ tempField + " != '' \"> ");
            } else {
                bw.write("\t\t\t<if test=\"null !=" + tempField + " and "+ tempField + " != '' \"> ");
            }
            bw.write("#{" + tempField + "}, </if>");
            bw.newLine();
        }

        bw.write("\t\t </trim>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // --------------- 完毕

        // --------------- 完毕

        // 修改update方法
        bw.write("\t<!-- 修 改-->");
        bw.newLine();
        bw.write("\t<update id=\"updateById\" parameterType=\"" + bean_package + "." + beanName
                + "\">");
        bw.newLine();
        bw.write("\t\t UPDATE ");
        bw.newLine();
        bw.write("\t\t <include refid=\"table\" />");
        bw.newLine();
        bw.write(" \t\t <set> ");
        bw.newLine();

        tempField = null;
        for (int i = 1; i < size; i++) {
            tempField = processField(columns.get(i));
            if ("Integer".equals(processType(types.get(0)))) {
                bw.write("\t\t\t<if test=\"null !=" + tempField + " and "+ tempField + " != '' \"> ");
            } else {
                bw.write("\t\t\t<if test=\"null !=" + tempField + " and "+ tempField + " != '' \"> ");
            }
            bw.write(columns.get(i) + " = #{" + tempField + "}, </if>");
            bw.newLine();
        }

        bw.write(" \t\t </set>");
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
        // update方法完毕

//		// ----- 批量插入
//		bw.write("\t<!-- 批量插入-->");
//		bw.newLine();
//		bw.write("\t<insert id=\"insertBatch\" parameterType=\"java.util.List\">");
//		bw.newLine();
//		bw.write("\t\t INSERT INTO " + tableName);
//		bw.newLine();
//		bw.write("\t\t\t(");
//		for (int i = 0; i < size; i++) {
//			bw.write(columns.get(i));
//			if (i != size - 1) {
//				bw.write(",");
//			}
//		}
//		bw.write(") ");
//		bw.newLine();
//		bw.write("\t\t VALUES ");
//		bw.newLine();
//		bw.write("\t\t <foreach collection =\"list\" item=\"reddemCode\" index= \"index\" separator =\",\">");
//		bw.newLine();
//		bw.write("\t\t\t(");
//		bw.newLine();
//		for (int i = 0; i < size; i++) {
//			bw.write("\t\t\t\t#{reddemCode." + processField(columns.get(i)) + "}");
//			if (i != size - 1) {
//				bw.write(",");
//			}
//			bw.newLine();
//		}
//		bw.write("\t\t\t)");
//		bw.newLine();
//		bw.write("\t\t </foreach>");
//		bw.newLine();
//		bw.write("\t</insert>");
//		bw.newLine();
//		bw.newLine();
//		// 批量insert完
    }

    /**
     * 获取所有的数据库表注释
     *
     * @return
     * @throws SQLException
     */
    private Map<String, String> getTableComment() throws SQLException {
        Map<String, String> maps = new HashMap<String, String>();
        PreparedStatement pstate = conn.prepareStatement("show table status");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }

    public void generate() throws ClassNotFoundException, SQLException, IOException {
        init();
        String prefix = "show full fields from ";
        List<String> columns = null;
        List<String> types = null;
        List<String> comments = null;
        PreparedStatement pstate = null;
        List<String> tables = getTables();
        Map<String, String> tableComments = getTableComment();
        for (String table : tables) {
            columns = new ArrayList<String>();
            types = new ArrayList<String>();
            comments = new ArrayList<String>();
            pstate = conn.prepareStatement(prefix + table);
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                columns.add(results.getString("FIELD"));
                types.add(results.getString("TYPE"));
                comments.add(results.getString("COMMENT"));
            }
            tableName = table;
            processTable(table);
            // this.outputBaseBean();
            String tableComment = tableComments.get(tableName);
            buildEntityBean(columns, types, comments, tableComment);
//
            buildMapper();
            buildMapperXml(columns, types, comments);
            buildService();
//            buildApi();
            //buildClient();
//            buildController(tableComment);

        }
        conn.close();
    }

    public static void main(String[] args) {
        try {
            new GenerateEntityUtil().generate();
            // 自动打开生成文件的目录
            Runtime.getRuntime().exec("open " + _path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
