//package com.yt.ytojbackend.component;
//
//import liquibase.Liquibase;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.resource.ClassLoaderResourceAccessor;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class LiquibaseExample {
//    public static void main(String[] args) {
//        try {
//            // 建立数据库连接
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1111");
//
//            // 创建Liquibase对象，并指定JSON配置文件路径
//            Liquibase liquibase = new Liquibase("/liquibase/changelog.json", new ClassLoaderResourceAccessor(), DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection)));
//
//            // 执行数据库变更
//            liquibase.update("");
//
//            // 关闭数据库连接
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}