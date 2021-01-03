public interface Prophet {
    boolean move(FieldCode fieldFrom, FieldCode fieldTo, Player player, Board board);
    boolean isWinner(Player player, Board board);
}
