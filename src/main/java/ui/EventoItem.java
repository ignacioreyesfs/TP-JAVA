package ui;

import org.uqbar.commons.model.annotations.Observable;
import java.time.LocalDate;

@Observable
public class EventoItem {
    private String titulo;
    private LocalDate fecha;
    private String sugerenciasDisponibles;

	public EventoItem(String titulo, LocalDate fecha, boolean sugerenciasDisponibles) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.sugerenciasDisponibles = booleanToString(sugerenciasDisponibles);
    }

    public String getTitulo() { return titulo; }
    public LocalDate getFecha() { return fecha; }
    public String getSugerenciasDisponibles() { return sugerenciasDisponibles; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public void setSugerenciasDisponibles(boolean sugerenciasDisponibles) { this.sugerenciasDisponibles = booleanToString(sugerenciasDisponibles); }
    
    public String booleanToString(boolean bool) {
    	if (bool) {
    		return("Si");
    	}
    	else {
    		return("No");
    	}
    }
}
