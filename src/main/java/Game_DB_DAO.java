import org.hibernate.Session;
import org.hibernate.SessionFactory;
public class Game_DB_DAO {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createGame(Game_DB game_db) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(game_db);
        session.getTransaction().commit();
        session.close();
    }

//    public int getMaxId(){
//        Session session = sessionFactory.openSession();
//        String sqlQuery = "FROM Article WHERE id = (select max(id) from game)";
//
//        int temp = (int) session.create
//    }

}
