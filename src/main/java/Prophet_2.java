import java.util.Objects;

public class Prophet_2 implements Prophet{

    public Prophet_2(){}

    @Override
    public boolean move(FieldCode fieldFrom, FieldCode fieldTo, Player player, Board board) {
        if(!Objects.isNull( board.getFieldOccupant(fieldFrom.getKey(), fieldFrom.getValue()))
                && board.getFieldOccupant(fieldFrom.getKey(), fieldFrom.getValue()).equals(player)
                && Objects.isNull(board.getFieldOccupant(fieldTo.getKey(), fieldTo.getValue()))
        ){
            return true;
        }
        return false;
    }

    @Override
    public boolean isWinner(Player player, Board board){
        return board.isWinner(player);
    }
}
