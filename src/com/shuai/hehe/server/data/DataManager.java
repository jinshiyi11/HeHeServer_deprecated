package com.shuai.hehe.server.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class DataManager {

    private boolean debug = Constants.DEBUG;

    private String mDbName;
    private String mDbUserName;
    private String mDbPassword;
    private String mDbHost;
    private int mDbPort;

    private static String mDriverName = "com.mysql.jdbc.Driver";//"org.sqlite.JDBC"

    {
        if (debug) {
            mDbName = "hehe";
            mDbUserName = "hot_feed_user";
            mDbPassword = "test";
            mDbHost = "localhost";
            mDbPort = 3306;
        } else {
            mDbName = "";
            mDbUserName="";
            mDbPassword="";
            mDbHost="";
            mDbPort=4050;
        }

    }

    public DataManager() {
        try {
            Class.forName(mDriverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public Connection getConnection() throws SQLException {
        //		String path=Constant.DB_PATH;
        //		Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path);

        String connectionString = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", mDbHost, mDbPort, mDbName,
                mDbUserName, mDbPassword);
        Connection connection = DriverManager.getConnection(connectionString);

        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                //				connection=null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 创建数据库
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void createDb() throws SQLException, ClassNotFoundException {
        //Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        Statement statement = null;
        connection = getConnection();

        String[] sqls = {
                "CREATE TABLE IF NOT EXISTS hot_feed(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "type INT,title VARCHAR(255) NOT NULL UNIQUE," + "content TEXT," + "`from` INT,"
                        + "state INT DEFAULT -1," + "insert_time TIMESTAMP DEFAULT  CURRENT_TIMESTAMP(),"
                        + "show_time TIMESTAMP DEFAULT 0" + ")",

                "CREATE TABLE IF NOT EXISTS pic(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + "feed_id INT,"
                        + "thumb_url TEXT," + "big_url TEXT," + "description TEXT," + "insert_time TIMESTAMP DEFAULT 0"
                        + ")" };

        		String[] indexs={/*"CREATE INDEX IF NOT EXISTS type_title_index ON hot_feed(type,title)",*/
        		        "CREATE INDEX IF NOT EXISTS pic_feedid_index ON pic (feed_id)"};

        statement = connection.createStatement();
        //创建表
        for (String sql : sqls) {
            statement.execute(sql);
        }

        //创建索引
		for(String sql:indexs){
			statement.execute(sql);				
		}

        closeConnection(connection);
    }

    /**
     * 返回的数据按时间排列，从新到旧
     * 
     * @param showTime
     * @param isAdmin
     * @param count
     * @param version
     * @return
     * @throws SQLException
     */
    public ArrayList<Feed> getFeeds(Date showTime, boolean isAdmin, int count, double version) throws SQLException {
        ArrayList<Feed> feeds = new ArrayList<Feed>();

        Connection connection = getConnection();
        String sql;

        //		if(count>0){
        //			sql="SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE show_time>? ORDER BY show_time DESC LIMIT ?";
        //		}else{
        //			sql="SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE show_time<? ORDER BY show_time DESC LIMIT ?";
        //		}

        //Date currentDate=new Date();

        if (count > 0) {
            sql = "SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE state!=0 AND show_time>? "
                    + (isAdmin ? "" : " AND show_time<CURRENT_TIMESTAMP() ");
        } else {
            sql = "SELECT id,type,title,content,`from`,insert_time,show_time FROM hot_feed WHERE state!=0 AND show_time<? ";
        }

        if (version<Constants.VERSION_1_1) {
            sql = sql + " AND type=" + FeedType.TYPE_ALBUM;
        }else if(version<Constants.VERSION_1_3){
            sql = String.format("%s AND (type=%d or type=%d)",sql,FeedType.TYPE_ALBUM,FeedType.TYPE_VIDEO);
        }

        sql = sql + " ORDER BY show_time " + (count < 0 ? " DESC " : " ASC ") + " LIMIT ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        statement.setString(1, simpleDateFormat.format(showTime));
        statement.setInt(2, Math.abs(count));
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            Feed feed = new Feed();

            int index = 1;
            feed.setId(resultSet.getInt(index++));
            feed.setType(resultSet.getInt(index++));
            feed.setTitle(resultSet.getString(index++));
            feed.setContent(resultSet.getString(index++));
            feed.setFrom(resultSet.getInt(index++));
            feed.setShowTime(resultSet.getTimestamp("show_time").getTime());
            feeds.add(feed);
        }

        closeConnection(connection);

        if (count > 0) {
            Collections.reverse(feeds);
        }
        return feeds;
    }

    public void hideFeed(long feedId) throws SQLException {
        Connection connection = getConnection();
        String sql = "UPDATE hot_feed SET state=0 WHERE id=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, feedId);
        statement.execute();

        closeConnection(connection);
    }

    public void updateShowTime(HttpServletResponse response) throws SQLException, IOException, ParseException {
        PrintWriter writer = response.getWriter();
        long mShowTimeStep = 60 * 60 * 1000;

        Connection connection = getConnection();
        String sql = "SELECT * FROM hot_feed WHERE show_time<CURRENT_TIMESTAMP() ORDER BY show_time DESC LIMIT 1";

        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        long feedId=0;
        long startTime=simpleDateFormat.parse("2014-07-20 00:00:00").getTime();
        String title="";
        if (rs.next()) {
            feedId = rs.getLong("id");
            startTime = rs.getTimestamp("show_time").getTime();
            
            title = rs.getString("title");
            rs.close();
        }
        writer.println("feedId:" + feedId + " title:" + title + " startTime:" + new Date(startTime));
        
        PreparedStatement prepareStatement = connection.prepareStatement(
                "SELECT id,state FROM hot_feed where id>? ORDER BY id ASC", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        
        prepareStatement.setLong(1, feedId);

        ResultSet resultSet = prepareStatement.executeQuery();
        
        PreparedStatement updateStatement=connection.prepareStatement("update hot_feed set show_time=? where id=?");
        while (resultSet.next()) {
            int feedState=resultSet.getInt("state");
            if(feedState==Feed.STATE_HIDDEN)
                continue;
            startTime += mShowTimeStep;
//                resultSet.updateTimestamp("show_time", new Timestamp(startTime));
//                resultSet.updateRow();
            
            updateStatement.setTimestamp(1, new Timestamp(startTime));
            updateStatement.setInt(2, resultSet.getInt("id"));
            updateStatement.executeUpdate();

//                feedId = resultSet.getLong("id");
//                startTime = resultSet.getTimestamp("show_time").getTime();
//                title = resultSet.getString("title");
//
//                writer.println("feedId:" + feedId + " title:" + title + " startTime:" + new Date(startTime));
        }
        resultSet.close();
        

        writer.println("update success");
        closeConnection(connection);
    }
    
    /**
     * 获取一条相册新鲜事的所有图片信息
     * 
     * @param feedId
     *            相册新鲜事id
     * @return
     * @throws SQLException
     */
    public ArrayList<PicInfo> getAlbumPics(int feedId) throws SQLException {
        ArrayList<PicInfo> result = new ArrayList<PicInfo>();

        Connection connection = getConnection();
        String sql = "SELECT id,big_url,description FROM pic WHERE feed_id=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, feedId);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            PicInfo info = new PicInfo();

            info.setId(resultSet.getInt("id"));
            info.setBigPicUrl(resultSet.getString("big_url"));
            info.setPicDescription(resultSet.getString("description"));
            result.add(info);
        }

        closeConnection(connection);
        return result;
    }

    /**
     * 获取由feedId指定的日志的信息
     * @param feedId
     * @return
     * @throws SQLException 
     */
    public BlogInfo getBlogInfo(int feedId) throws SQLException {
        BlogInfo result = new BlogInfo();

        Connection connection = getConnection();
        String sql = "SELECT id,feed_id,html_content FROM blog WHERE feed_id=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, feedId);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            result.setId(resultSet.getInt("id"));
            result.setFeedId(resultSet.getInt("feed_id"));
            result.setHtmlContent(resultSet.getString("html_content"));
        }

        closeConnection(connection);
        return result;
    }

}
