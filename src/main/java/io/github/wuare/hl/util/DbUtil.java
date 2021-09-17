package io.github.wuare.hl.util;

import io.github.wuare.hl.constant.ConfigConstant;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {

    static {
        try {
            Class.forName(ConfigConstant.DRIVER_CLASS_MYSQL);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> query(String sql, Object... param) {
        Connection conn = null;
        try {
            conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    ps.setObject(i, param[i]);
                }
            }
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(metaData.getColumnName(i), rs.getObject(i));
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(conn);
        }
    }

    public static boolean execute(String sql,  Object... param) {
        Connection conn = null;
        try {
            conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    ps.setObject(i, param[i]);
                }
            }
            return ps.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(conn);
        }
    }

    public static Connection getConn() throws Exception {
        return DriverManager.getConnection(ConfigConstant.JDBC_URL, ConfigConstant.USER, ConfigConstant.PWD);
    }
}
