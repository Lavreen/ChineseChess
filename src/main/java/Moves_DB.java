import javax.persistence.*;


@Entity(name="Moves")
@Table(name="moves")
public class Moves_DB {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="Field_from")
    private String fieldFrom;

    @Column(name="Field_to")
    private String fieldTo;

    @Column(name="Move_number")
    private int moveNumber;

    @ManyToOne()
    @JoinColumn(name="id_game")
    private Game_DB game_db;


    public Moves_DB() {
    }

    public Moves_DB(String fieldFrom, String fieldTo, int moveNumber, Game_DB game_db) {
        this.fieldFrom = fieldFrom;
        this.fieldTo = fieldTo;
        this.moveNumber = moveNumber;
        this.game_db = game_db;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int id) {
        this.moveNumber = moveNumber;
    }

    public String getFieldFrom(){return fieldFrom;}

    public void setFieldFrom(String fieldFrom){this.fieldFrom = fieldFrom;}

    public String getFieldTo(){return fieldTo;}

    public void setFieldTo(String fieldTo){this.fieldTo = fieldTo;}

}

