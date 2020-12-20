import java.util.Objects;

/**
 * Calls with rules
 */

public class Prophet {
    public Prophet(){

    }

    /**
     * Checks if player can move from fieldFrom to fieldTo on board
     * @param fieldFrom field from
     * @param fieldTo field to
     * @param player owner of move
     * @param board board on which player wanna make move
     * @return true if move can be made
     */
    public boolean move(FieldCode fieldFrom, FieldCode fieldTo, Player player, Board board){        //for now player can fly to any free field

        if(!Objects.isNull( board.getFieldOccupant(fieldFrom.getKey(), fieldFrom.getValue()))
                && board.getFieldOccupant(fieldFrom.getKey(), fieldFrom.getValue()).equals(player)
                && Objects.isNull(board.getFieldOccupant(fieldTo.getKey(), fieldTo.getValue()))
        ){
            if(board.areNeighbors(fieldFrom.getKey(), fieldFrom.getValue(), fieldTo.getKey(), fieldTo.getValue())){
                return true;
            }
            else if(board.areFarNeighbors(fieldFrom.getKey(), fieldFrom.getValue(), fieldTo.getKey(), fieldTo.getValue())){
                return true;
            }
          }

         return false;
    }

    /**
     * Checks if player is winner on board
     * @param player player
     * @param board board
     * @return true if player won
     */
    public boolean isWinner(Player player, Board board){return board.isWinner(player);}
}
