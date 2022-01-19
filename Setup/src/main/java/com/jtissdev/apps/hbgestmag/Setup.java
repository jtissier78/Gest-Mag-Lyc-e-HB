package com.jtissdev.apps.hbgestmag;

import com.jtissdev.databaseconnect.DbGestion;
import com.jtissdev.databaseconnect.FlieReaderWirtter.XlsxReaderWritter;
import com.jtissdev.databaseconnect.Parameter.DbCParser;
import com.jtissdev.databaseconnect.Parameter.Parameter;
import org.json.simple.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Setup {
    private static final JSONObject structure = DbCParser.parseFile("./Setup/data/structure.json");
    private static final InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
    private static final String dataPath = "./data/Inventaire Magasin 2021 en cours Modifier.xlsx";

    private static DbGestion dbGest;
    private static Connection connexion;

    public static void main(String[] args) {
        dbGest = new DbGestion(input, structure);
        connexion = dbGest.getConnexion("grant");
        System.out.println("=================================================================");
        System.out.println("Bienvenue dans le Script de Maintenance de l'application de \n Gestion du Magasin du Lycéee Henri Brisson de Vierzon.");
        System.out.println("=================================================================");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (choice != 99) {
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

    private static void LoadChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================================");
        System.out.println("Merci de Choisir le Format de Fichier source.");
        System.out.println("=================================================================");
        int choice = 0;
        while (choice != 99) {
            System.out.println(printLoadChoice());
            System.out.println("Votre choix = ");
            choice = scanner.nextInt();
            switchLoadChoice(choice);
        }
    }

    private static void switchChoice(int choice) {
        switch (choice) {
            case 99:
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
                XlsxLoad();
                System.out.println("=================================================================");
                System.out.println("Loading Finished");
                System.out.println("=================================================================");
                break;
            case 3:
                System.out.println("=================================================================");
                System.out.println("Saving Data");
                System.out.println("=================================================================");
                SaveData();
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

    private static void SaveData() {
        JSONObject object = dbGest.SaveDataToJson();
        JsonWrite(object,"./Setup/data/dataSaved.json");
    }

    private static void JsonWrite(JSONObject object, String path) {
        FileWriter wrfile = null;
        File file = new File(path);
        file.getParentFile().mkdirs(); // Will create parent directories if not exists
        try {
            file.createNewFile();
            FileOutputStream s = new FileOutputStream(file,false);
            wrfile = new FileWriter(path);
            wrfile.write(object.toJSONString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                wrfile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void switchLoadChoice(int choice) {
        switch (choice) {
            case 99:
                System.out.println("=================================================================");
                System.out.println("Vous avez choisi d'annuler le chargement.");
                System.out.println("=================================================================");
                break;
            /*case 1:
                System.out.println("=================================================================");
                System.out.println("Début de l'installation de l'application.");
                System.out.println("=================================================================");
                JsonLoad();
                System.out.println("=================================================================");
                System.out.println("Installation terminée");
                System.out.println("=================================================================");
                break;*/
            case 2:
                System.out.println("=================================================================");
                System.out.println("Début de l'installation de l'application.");
                System.out.println("=================================================================");
                XlsxLoad();
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
        LoadChoice();
    }

    /*private static void JsonLoad() {
        JSONObject datas = DbCParser.parseFile("data.json");
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure),datas,connexion);
    }*/

    private static void XlsxLoad() {
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("alestaraudlame"), XlsxReaderWritter.Reader(dataPath, 0), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("bobine"), XlsxReaderWritter.Reader(dataPath, 1), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("cartouche"), XlsxReaderWritter.Reader(dataPath, 2), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("consommable"), XlsxReaderWritter.Reader(dataPath, 3), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("disqueTronMeul"), XlsxReaderWritter.Reader(dataPath, 4), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("epi"), XlsxReaderWritter.Reader(dataPath, 5), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("foret"), XlsxReaderWritter.Reader(dataPath, 6), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("matInfo"), XlsxReaderWritter.Reader(dataPath, 7), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("matiere"), XlsxReaderWritter.Reader(dataPath, 8), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("piles"), XlsxReaderWritter.Reader(dataPath, 9), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("papier"), XlsxReaderWritter.Reader(dataPath, 10), connexion);
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("tiges"), XlsxReaderWritter.Reader(dataPath, 11), connexion);
    }

    private static String printChoice() {
        String line = "------------------------------------------------------- \n";
        StringBuilder msg = new StringBuilder("Merci de choisir l'action que vous souhaiter accomplir. \n");
        msg.append(line).append("Choix 1 : Setup.\n");
        msg.append(line).append("Choix 2 : Load Db.\n");
        msg.append(line).append("Choix 3 : Save Data.\n");
        msg.append(line).append("Choix 99 : Quit.\n");
        msg.append(line);
        return msg.toString();
    }

    private static String printLoadChoice() {
        String line = "------------------------------------------------------- \n";
        StringBuilder msg = new StringBuilder("Merci de choisir le format du Fichier. \n");
        msg.append(line).append("Choix 1 : JSON : data.json.\n");
        msg.append(line).append("Choix 2 : XLSX : inventaire Magasin.\n");
        msg.append(line).append("Choix 99 : Quit.\n");
        msg.append(line);
        return msg.toString();
    }

}
