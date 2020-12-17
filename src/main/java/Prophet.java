public class Prophet {
    public Prophet(){

    }

    public boolean move(FieldCode fieldFrom, FieldCode fieldTo, Player player, Board board){        //for now player can fly to any free field

        if(board.getFieldOwner(fieldFrom.getKey(), fieldFrom.getValue()) == player
                && board.getFieldOwner(fieldTo.getKey(), fieldTo.getValue()) == null
        ){
            return true;
        }

        return false;
    }

    public boolean isWinner(Player player, Board board){return board.isWinner(player);}
}
