package com.jtissdev.apps.hbgestmag;

import com.jtissdev.databaseconnect.DbGestion;
import com.jtissdev.databaseconnect.FlieReaderWirtter.XlsxReaderWritter;
import com.jtissdev.databaseconnect.Parameter.DbCParser;
import com.jtissdev.databaseconnect.Parameter.Parameter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import org.json.simple.JSONObject;

/**
 * @author J.Tissier
 * @version 1.0
 */
public final class Setup {
    /** DataBase Structure File. */
    private static final JSONObject STRUCTURE
            = DbCParser.parseFile("./Setup/data/structure.json");
    /** Input retrieve database connexions parameter. */
    private static final InputStream INPUT = Thread.currentThread()
            .getContextClassLoader().getResourceAsStream("config.properties");
    /** Xlsx data Sources File. */
    private static final String XLSXDATAPATH
            = "./Setup/data/Inventaire Magasin 2021 en cours Modifier.xlsx";
    /** Json Save Data File.*/
    private static final String JSON_DATA_PATH = "./Setup/data/dataSaved.json";
    /** Exit code for each script console.*/
    private static final int EXIT_CODE = 99;
    /** because maven says I have to.*/
    private static final int SAVE_CHOICE = 3;
    /** because maven says I have to.*/
    private static final int CONSOMMABLE = 3;
    /** because maven says I have to.*/
    private static final int DISQUE_TRON_MEUL = 4;
    /** because maven says I have to.*/
    private static final int EPI = 5;
    /** because maven says I have to.*/
    private static final int FORET = 6;
    /** because maven says I have to.*/
    private static final int MAT_INFO = 7;
    /** because maven says I have to.*/
    private static final int MATIERE = 8;
    /** because maven says I have to.*/
    private static final int PILES = 9;
    /** because maven says I have to.*/
    private static final int PAPIER = 10;
    /** because maven says I have to.*/
    private static final int TIGES = 11;
    /** DbGestion Système de gestion de connexion à la DB. */
    private static DbGestion dbGest;
    /** Connexion JDBC to DataBase.*/
    private static Connection connexion;

    private Setup() { }

    /**Program launch method.
     *
     */
    public static void main(String[] args) {

        dbGest = new DbGestion(INPUT, STRUCTURE);
        connexion = dbGest.getConnexion("grant");
        System.out.println("=================================================================");
        System.out.println("Bienvenue dans le Script de Maintenance de l'application de "
                +
                "\n Gestion du Magasin du Lycéee Henri Brisson de Vierzon.");
        System.out.println("=================================================================");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (choice != EXIT_CODE) {
            System.out.println(printChoice());
            System.out.println("Votre choix = ");
            choice = scanner.nextInt();
            switchChoice(choice);
        }
        if (connexion != null) {
            try {
                connexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================================");
        System.out.println("Merci de Choisir le Format de Fichier source.");
        System.out.println("=================================================================");
        int choice = 0;
        while (choice != EXIT_CODE) {
            System.out.println(printLoadChoice());
            System.out.println("Votre choix = ");
            choice = scanner.nextInt();
            switchLoadChoice(choice);
        }
    }

    private static void switchChoice(int choice) {
        switch (choice) {
            case EXIT_CODE:
                System.out.println("=================================================================");
                System.out.println("Vous avez choisi de quitter l'utilitaire d'installation.");
                System.out.println("=================================================================");
                break;
            case 1:
                System.out.println("=================================================================");
                System.out.println("Début de l'installation de l'application.");
                System.out.println("=================================================================");
                setup();
                System.out.println("=================================================================");
                System.out.println("Installation terminée");
                System.out.println("=================================================================");
                break;
            case 2:
                System.out.println("=================================================================");
                System.out.println("test de load .");
                System.out.println("=================================================================");
                loadChoice();
                System.out.println("=================================================================");
                System.out.println("Loading Finished");
                System.out.println("=================================================================");
                break;
            case SAVE_CHOICE:
                System.out.println("=================================================================");
                System.out.println("Saving Data");
                System.out.println("=================================================================");
                saveData();
                System.out.println("=================================================================");
                System.out.println("Data Saved");
                System.out.println("=================================================================");
                break;
            default:
                System.out.println("=================================================================");
                System.out.println("Merci de faire un choix correct.");
                System.out.println("=================================================================");
        }
    }

    private static void saveData() {
        JSONObject object = dbGest.SaveDataToJson();
        jsonWrite(object);
    }

    private static void jsonWrite(JSONObject object) {
        FileWriter wrfile = null;
        File file = new File(Setup.JSON_DATA_PATH);
        file.getParentFile().mkdirs(); // Will create parent directories if not exists
        try {
            file.createNewFile();
            wrfile = new FileWriter(Setup.JSON_DATA_PATH);
            wrfile.write(object.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (wrfile != null) {
                    wrfile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void switchLoadChoice(int choice) {
        switch (choice) {
            case EXIT_CODE:
                System.out.println("=================================================================");
                System.out.println("Vous avez choisi d'annuler le chargement.");
                System.out.println("=================================================================");
                break;
            case 1:
                System.out.println("=================================================================");
                System.out.println("Loading From Json File.");
                System.out.println("=================================================================");
                jsonLoad();
                System.out.println("=================================================================");
                System.out.println("Installation terminée");
                System.out.println("=================================================================");
                break;
            case 2:
                System.out.println("=================================================================");
                System.out.println("Début de l'installation de l'application.");
                System.out.println("=================================================================");
                xlsxLoad();
                System.out.println("=================================================================");
                System.out.println("Installation terminée");
                System.out.println("=================================================================");
                break;
            default:
                System.out.println("=================================================================");
                System.out.println("Merci de faire un choix correct.");
                System.out.println("=================================================================");
        }
    }

    private static void setup() {
        dbGest.DbCreate();
        dbGest.UsersCreate();
        dbGest.TableCreate();
        loadChoice();
    }

    private static void jsonLoad() {
        JSONObject datas = DbCParser.parseFile(JSON_DATA_PATH);
        dbGest.InsertData(Parameter.GetDataTableParameter(STRUCTURE), datas, connexion);
    }

    private static void xlsxLoad() {
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("alestaraudlame"), XlsxReaderWritter.Reader(XLSXDATAPATH, 0), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("bobine"), XlsxReaderWritter.Reader(XLSXDATAPATH, 1), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("cartouche"), XlsxReaderWritter.Reader(XLSXDATAPATH, 2), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("consommable"), XlsxReaderWritter.Reader(XLSXDATAPATH, CONSOMMABLE), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("disqueTronMeul"), XlsxReaderWritter.Reader(XLSXDATAPATH, DISQUE_TRON_MEUL), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("epi"), XlsxReaderWritter.Reader(XLSXDATAPATH, EPI), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("foret"), XlsxReaderWritter.Reader(XLSXDATAPATH, FORET), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("matInfo"), XlsxReaderWritter.Reader(XLSXDATAPATH, MAT_INFO), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("matiere"), XlsxReaderWritter.Reader(XLSXDATAPATH, MATIERE), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("piles"), XlsxReaderWritter.Reader(XLSXDATAPATH, PILES), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("papier"), XlsxReaderWritter.Reader(XLSXDATAPATH, PAPIER), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(STRUCTURE)
                .get("tiges"), XlsxReaderWritter.Reader(XLSXDATAPATH, TIGES), connexion);
    }

    private static String printChoice() {
        String line = "------------------------------------------------------- \n";
        return "Merci de choisir l'action que vous souhaiter accomplir. \n" + line + "Choix 1 : Setup.\n" +
                line + "Choix 2 : Load Db.\n" +
                line + "Choix 3 : Save Data.\n" +
                line + "Choix 99 : Quit.\n" +
                line;
    }

    private static String printLoadChoice() {
        String line = "------------------------------------------------------- \n";
        return "Merci de choisir le format du Fichier. \n" + line + "Choix 1 : JSON : data.json.\n" +
                line + "Choix 2 : XLSX : inventaire Magasin.\n" +
                line + "Choix 99 : Quit.\n" +
                line;
    }

}
