package com.zhangyin.mysqlconfig;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;
@State(
        name = "gen-mysql",
        storages = {
                @Storage(
                        id = "gen-mysql",
                        file = "$APP_CONFIG$/gen-mysql.xml"
                )
        }
)
public class MySqlPersistent implements PersistentStateComponent<MySqlPersistent.MySqlConfig> {


    public static MySqlConfig getMySqlConfig() {
        MySqlPersistent service = ServiceManager.getService(MySqlPersistent.class);
        if(service.getState()==null){
            service.loadState(new MySqlConfig());
        }
        return  service.getState();
    }
    public static void  saveMySqlConfig(MySqlConfig mySqlConfig){
        MySqlPersistent service = ServiceManager.getService(MySqlPersistent.class);
        service.loadState(mySqlConfig);
    }

    MySqlConfig mySqlConfig;

    @Nullable
    @Override
    public MySqlConfig getState() {
        return mySqlConfig;
    }

    @Override
    public void loadState(MySqlConfig sqlConfig) {
        mySqlConfig=sqlConfig;
    }


    public static class MySqlConfig{
        private  String driverName= "com.mysql.jdbc.Driver";
        private  String jdbcURL= "jdbc:mysql://rm-wz970j84167q2388a.mysql.rds.aliyuncs.com:3306/sz-local?zeroDateTimeBehavior=convertToNull";
        private  String username= "voffice_sz";
        private  String password= "Db2017Admin";

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getJdbcURL() {
            return jdbcURL;
        }

        public void setJdbcURL(String jdbcURL) {
            this.jdbcURL = jdbcURL;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTableSchema(){
            int i = jdbcURL.indexOf("?");
            int i1 = jdbcURL.lastIndexOf("/");
            return  jdbcURL.substring(i1+1, i);
        }
    }
}
