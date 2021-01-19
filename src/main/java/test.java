import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class test {

    public static void main(String[] args) {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("config/spring-configurations.xml");
        Game_DB game_db = new Game_DB();    //tworzymy nowy obiekt bazodanowy game -  id samo sie przyznaje

        Game_DB_DAO game_db_dao = (Game_DB_DAO) appContext.getBean("Game_DB_DAO");      //+/- służty do wysyłania danych z klas game_db,  move_db do  bazy danych

        Moves_DB moves_db1 = new Moves_DB("R1","R2", 1, game_db);   //tworzymy nowy ruch bazodanowy, podajemy skad, dokad, ktory jest to ruchz  kolei i grę której ten ruch dotyczy
        Moves_DB moves_db2 = new Moves_DB("R4","R3", 2, game_db);

        Set<Moves_DB> set = new HashSet<>();        //zbieramy tu wszystkie ruchy dotyczace jednej gry

        set.add(moves_db1);
        set.add(moves_db2);

        game_db.setArticles(set);       //dodajemy do gry ruchy

        game_db_dao.createGame(game_db);    //wysyłamy do bazy danych gre i ruch w tranzakcji (albo wysle sie wszystko albo nic)

    }


}
