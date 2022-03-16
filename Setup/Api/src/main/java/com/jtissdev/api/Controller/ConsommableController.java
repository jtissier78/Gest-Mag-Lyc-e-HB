package com.jtissdev.api.Controller;

import com.jtissdev.api.model.Consumable;
import com.jtissdev.databaseconnect.Connexion;
import com.jtissdev.databaseconnect.DbGestion;
import com.jtissdev.databaseconnect.Parameter.DbCParser;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConsommableController {


    /**
     * DataBase Structure File.
     */
    private static final JSONObject STRUCTURE
            = DbCParser.parseFile("./Setup/data/structure.json");
    /**
     * Input retrieve database connexions parameter.
     */
    private static final InputStream INPUT = Thread.currentThread()
            .getContextClassLoader().getResourceAsStream("config.properties");
    /**
     * DbGestion Système de gestion de connexion à la DB.
     */
    private static DbGestion dbGest;
    /**
     * Connexion JDBC to DataBase.
     */
    private static Connection connexion;
    private static PreparedStatement preparedStmt = null;

    /* ******************** Constructor ******************** */

    /**
     * Empty Constructor
     *
     * @since 1.0
     */
    public ConsommableController() {
    }

    /**
     * Method gets all Consumables in DataBase sort by designation ascendant.
     *
     * @return JSONObject
     * @see #resultToObject(ResultSet)
     * @since 1.0
     */
    public static JSONObject getConsommables() {
        dbGest = new DbGestion(INPUT, STRUCTURE);
        connexion = dbGest.getConnexion("grant");
        JSONObject jsondata = new JSONObject();
        String query = "SELECT * FROM consommable ORDER by designation ASC";
        ResultSet result = null;
        JSONArray data = new JSONArray();
        try {
            preparedStmt = connexion.prepareStatement(query);
            result = preparedStmt.executeQuery();
            jsondata = resultToObject(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (preparedStmt != null) preparedStmt.close();
                if (connexion != null) connexion.close();
            } catch (SQLException ignore) {
            }
        }
        return jsondata;
    }

    private static JSONObject resultToObject(ResultSet result) {
        JSONObject jsondata = new JSONObject();
        JSONArray data = new JSONArray();
        try {
            while (result.next()) {
                //System.out.println(result);
                data.add(new Consumable(result.getInt("id"), result.getString("designation"), result.getString("unite"), result.getInt("quantite"), result.getDouble("pu")).toJSON());
            }
            jsondata.put("data", data);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return jsondata;
    }

}


