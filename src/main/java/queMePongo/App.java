package queMePongo;

import org.uqbar.arena.Application;
import org.uqbar.arena.windows.Window;
import ui.EventoViewModel;
import ui.EventoWindow;

public class App extends Application {
    public static void main(String[] args) {  	  	
        new App().start();
    }

    protected Window<?> createMainWindow() {
        return new EventoWindow(this, new EventoViewModel());
    }
}
