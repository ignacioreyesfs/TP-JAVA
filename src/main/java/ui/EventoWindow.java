package ui;

import org.uqbar.arena.layout.ColumnLayout;
import org.uqbar.arena.layout.VerticalLayout;
import org.uqbar.arena.widgets.*;
import org.uqbar.arena.widgets.Button;
import org.uqbar.arena.widgets.Label;
import org.uqbar.arena.widgets.Panel;
import org.uqbar.arena.widgets.tables.Column;
import org.uqbar.arena.widgets.tables.Table;
import org.uqbar.arena.widgets.tables.labelprovider.PropertyLabelProvider;
import org.uqbar.arena.windows.SimpleWindow;
import org.uqbar.arena.windows.WindowOwner;

public class EventoWindow extends SimpleWindow<EventoViewModel> {
    private static final String ERROR = "Error inesperado!! Compruebe las fechas ingresadas.";
    public EventoWindow(WindowOwner parent, EventoViewModel model) {
        super(parent, model);
    }

    @Override
    protected void addActions(Panel panel) {}

    @Override
    protected void createFormPanel(Panel mainPanel) {
        this.setMinHeight(220);
        this.setMinWidth(220);
        setTitle("Eventos");
        mainPanel.setLayout(new VerticalLayout());

        new Label(mainPanel)
                .setText("Rango de fechas")
                .setFontSize(20)
                .setWidth(100);

        new Label(mainPanel)
                .setText("Ejemplo de formato de fecha: yyyy-mm-dd \n");

        Panel form = new Panel(mainPanel);
        form.setLayout(new ColumnLayout(2));
        
        new LabeledTextBox(form, "Fecha desde: ", "fechaDesde");
        new LabeledTextBox(form, "Fecha hasta: ", "fechaHasta");

        new Button(mainPanel)
                .setCaption("Filtrar eventos")
                .onClick(() -> this.filtrarEventos());

        Table<EventoItem> tablaEventos = new Table<>(mainPanel, EventoItem.class);
        tablaEventos.setNumberVisibleRows(10);
        tablaEventos.setWidth(200);

        crearColumnaEvento(tablaEventos, "Titulo", "titulo");
        crearColumnaEvento(tablaEventos, "Fecha", "fecha");
        crearColumnaEvento(tablaEventos, "Hay sugerencias disponibles", "sugerenciasDisponibles");

        tablaEventos.bindItemsToProperty("eventos");
    }

    private void filtrarEventos() {
        try {
            getModelObject().filtrarPorRangoFechas();
        } catch (Exception e) {
            showError(ERROR);
        }
    }

    @Override
    public void showError(String message) {
        super.showError(message);
    }
    
    private PropertyLabelProvider<EventoItem> crearColumnaEvento(Table<EventoItem> tabla, String encabezado, String propiedad) {
    	return new Column<>(tabla)
    			.setTitle(encabezado)
    			.setFixedSize(200)
    			.bindContentsToProperty(propiedad);
    }
}