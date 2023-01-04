package oh_heaven.game;

// import ch.aplu.jcardgame.Location
import oh_heaven.game.Player;
import oh_heaven.game.Oh_Heaven;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.Properties;


// taken from Snakes and Ladders on a Plane


public class PropertiesLoader {
    public static final String DEFAULT_DIRECTORY_PATH = "properties/";
    public static Properties loadPropertiesFile(String propertiesFile) {
        if (propertiesFile == null) {
            try (InputStream input = new FileInputStream( DEFAULT_DIRECTORY_PATH + "runmode.properties")) {

                Properties prop = new Properties();

                // load a properties file
                prop.load(input);

                propertiesFile = DEFAULT_DIRECTORY_PATH + prop.getProperty("current_mode");
                System.out.println(propertiesFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try (InputStream input = new FileInputStream(propertiesFile)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public static List<Player> loadPlayers(Properties properties) {
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < Oh_Heaven.nbPlayers; i++){
            String playerType = properties.getProperty("players." + i);
            players.add(new Player(i, playerType));
        }
        return players;

    }
    

}