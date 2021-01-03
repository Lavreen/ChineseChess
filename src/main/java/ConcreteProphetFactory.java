public class ConcreteProphetFactory implements ProphetFactory{
    @Override
    public Prophet getProphet(int mode) throws Exception {
        if(mode == 1){
            return new Prophet_1();
        }
        else if(mode == 2){
            return new Prophet_2();
        }
        else{
            throw new Exception();
        }
    }
}
