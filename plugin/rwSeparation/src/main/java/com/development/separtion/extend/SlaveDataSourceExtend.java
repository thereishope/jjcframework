package com.development.separtion.extend;
import com.development.separtion.enums.DataBase;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**获取从数据库集合
 * @author jiajunchen
 * @title SlaveDataSourceExt
 * @project rwSeparation
 */
public interface SlaveDataSourceExtend {

    public List<Map<DataBase,DataSource>> getSlaveDataSourceList();
}
