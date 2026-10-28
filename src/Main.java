import java.util.Scanner;
import service.Sistema;
import ui.Menu;
public class Main {
    public static void main(String[] args){
        new Menu(new Sistema(), new Scanner(System.in)).executar();
    }
}
