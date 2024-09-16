//@author Daiane Tararam

package view;
import controller.TriatloModerno;
public class Main {

	public static void main(String[] args) {
        TriatloModerno[] atletas = new TriatloModerno[25];
        for (int i = 0; i < 25; i++) {
            atletas[i] = new TriatloModerno(i + 1);
            atletas[i].start();
        }
        for (int i = 0; i < 25; i++) {
            try {
                atletas[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
