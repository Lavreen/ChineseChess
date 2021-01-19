import javax.persistence.*;

import java.sql.Date;
import java.util.Set;

@Entity(name="Game")
@Table(name="game")
public class Game_DB {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "id",cascade=CascadeType.ALL)
    private Set<Moves_DB> moves_db;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Moves_DB> getArticles() {
        return moves_db;
    }

    public void setArticles(Set<Moves_DB> moves_db) {
        this.moves_db = moves_db;
    }
}
