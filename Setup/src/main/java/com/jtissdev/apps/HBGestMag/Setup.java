package com.jtissdev.apps.HBGestMag;

import com.jtissdev.databaseconnect.DbGestion;
import com.jtissdev.databaseconnect.FlieReaderWirtter.XlsxReaderWritter;
import com.jtissdev.databaseconnect.Parameter.*;
import org.json.simple.JSONObject;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Scanner;

public class Setup {

    private static final JSONObject structure = DbCParser.parseFile("./data/structure.json");
    private static final InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
    private static final String dataPath = "./data/Inventaire Magasin 2021 en cours Modifier.xlsx";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================================");
        System.out.println("Bienvenue dans le Script de Maintenance de l'application de \n Gestion du Magasin du Lycéee Henri Brisson de Vierzon.");
        System.out.println("=================================================================");

        int choice = 0;
        while (choice!=99){
            System.out.println(printChoice());
            System.out.println("Votre choix = ");
            choice = scanner.nextInt();
            switchChoice(choice);
        }

    }

    private static void switchChoice(int choice) {
        switch (choice){
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
            case 2 :
                System.out.println("=================================================================");
                System.out.println("test de load .");
                System.out.println("=================================================================");
                Load();
                System.out.println("=================================================================");
                System.out.println("test fini");
                System.out.println("=================================================================");
                break;
            default:
                System.out.println("=================================================================");
                System.out.println("Merci de faire un choix correct.");
                System.out.println("=================================================================");
        }
    }

    private static void Load(){
        DbGestion dbGest = new DbGestion(input,structure);
        Connection connexion = dbGest.getConnexion("grant");
        dbGest.InsertData((JSONObject) Parameter.GetDataTableParameter(structure).get("alestaraudlame"), XlsxReaderWritter.Reader( dataPath,0),connexion);
    }

    private static String printChoice() {
        String line="------------------------------------------------------- \n";
        StringBuilder msg=new StringBuilder("Merci de choisir l'action que vous souhaiter accomplir. \n");
        msg.append(line).append("Choix 1 : Setup.\n");
        msg.append(line).append("Choix 2 : Test Load Db.\n");
        msg.append(line).append("Choix 99 : Quit.\n");
        msg.append(line);
        return msg.toString();
    }

    private static void setup(){
        DbGestion dbGest = new DbGestion(input,structure);
        dbGest.DbCreate();
        dbGest.UsersCreate();
        dbGest.TableCreate();
    }
}
