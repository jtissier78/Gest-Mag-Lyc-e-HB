package com.jtissdev.api;

import com.jtissdev.api.Resources.ConsommableRessource;
import com.jtissdev.databaseconnect.Parameter.DbCParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Level;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.json.simple.JSONObject;


public class Main {

    private static final JSONObject STRUCTURE
            = DbCParser.parseFile("./Setup/data/structure.json");
    public static final URI BASE_URI = getBaseURI();
    /** Input retrieve database connexions parameter. */
    private static final InputStream INPUT = Thread.currentThread()
            .getContextClassLoader().getResourceAsStream("config.properties");



    /**
     * Method to generate API's URI.
     * @version 1.0
     * @return URI
     * generated for local usage
     */
    private static URI getBaseURI() {return UriBuilder.fromUri(getProperties(INPUT).getProperty("Api.Url")).port(9991).build();}

    /**
     * Main method.
     * @version 1.0
     * @param args
     */
    public static void main(String[] args) {
        ResourceConfig rc = new ResourceConfig();
        rc.registerClasses(ConsommableRessource.class);
        /*rc.registerClasses(AuthorRessource.class);
        rc.registerClasses(BookRessource.class);*/
        rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());

        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
            server.start();

            System.out.println(String.format(
                    "Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
                    BASE_URI, BASE_URI));

            System.in.read();
            server.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* ToDo externalise */
    private static Properties getProperties(java.io.InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException ioError) {
            IOErrorMsg(ioError);
        }
        return properties;
    }

    private static void IOErrorMsg(IOException ioError) {
        ioError.printStackTrace();
    }
}
