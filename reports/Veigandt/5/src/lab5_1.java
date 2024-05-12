interface Building{
    void Open();
    void Close();
}
abstract class PublicBuilding implements Building{
    @Override
    public void Open() {
        System.out.println("Здание открыто");
    }
    @Override
    public void Close() {
        System.out.println("Здание закрыто");
    }
}
class Theater extends PublicBuilding{
    public void doEvent(){
        System.out.println("Проходит репетиция");
    }
}
public class Main {
    public static void main(String[] args) {
        Theater theater = new Theater();
        theater.Open();
        theater.doEvent();
        theater.Close();
    }
}
