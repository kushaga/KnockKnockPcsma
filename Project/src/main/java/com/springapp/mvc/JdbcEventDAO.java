package com.springapp.mvc;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by suhanth on 12/4/15.
 */
public class JdbcEventDAO implements  EventDAO {


    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/pcsma");
        dataSource.setUsername("root");
        dataSource.setPassword("suhanth");

        return dataSource;
    }



    @Override
    public void insert(Event event) {

        String query = "INSERT INTO EVENT (NAME,LOCATION,DESCRIPTION,ID,TIME,CATEGORY,USER_ID) VALUES (?, ?,?,?,?,?,?)";
        Connection conn = null;

        try {

            conn = this.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(query);


            ps.setString(1, event.getEvent_name());
            ps.setString(2, event.getRoom_number());
            ps.setInt(4, Integer.parseInt(event.getEvent_id()));
            ps.setString(3, event.getEvent_description());
            ps.setString(5,event.getTime());
            ps.setString(6,event.getEvent_category());
            ps.setString(7,event.getUser_id());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }

    }


    public List<Event> fetch(String query)
    {


        Connection conn = null;
        Statement statement=null;
        List<Event> events = new ArrayList<Event>();



        try {

            conn = this.getDataSource().getConnection();

            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {

                String evntname = rs.getString("NAME");
                String roomno = rs.getString("LOCATION");
                String evntdesc = rs.getString("DESCRIPTION");
                String evid = rs.getString("ID");
                String evttime= rs.getString("TIME");
                String evcategory= rs.getString("CATEGORY");
                String userid= rs.getString("USER_ID");
                Event e = new Event();

                e.setEvent_id(evid);
                e.setTime(evttime);
                e.setEvent_description(evntdesc);
                e.setUser_id(userid);
                e.setRoom_number(roomno);
                e.setEvent_name(evntname);
                e.setEvent_category(evcategory);

                events.add(e);
            }

            return events;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }

    }
}
