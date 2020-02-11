package ui;

import org.uqbar.commons.model.annotations.Observable;

import queMePongo.evento.EventoConFecha;
import queMePongo.evento.RepositorioEventos;
import queMePongo.prenda.Guardarropa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Observable
public class EventoViewModel {
    private String fechaDesde;
    private String fechaHasta;
    private List<EventoItem> eventos;

    public String getFechaDesde() { return fechaDesde; }
    public String getFechaHasta() { return fechaHasta; }
    public List<EventoItem> getEventos() { return eventos; }

    public void setFechaDesde(String fechaDesde) { this.fechaDesde = fechaDesde; }
    public void setFechaHasta(String fechaHasta) { this.fechaHasta = fechaHasta; }
    public void setEventos(List<EventoItem> eventos) { this.eventos = eventos; }

    public EventoViewModel() {
        RepositorioEventos.getInstance().limpiarEventosTerminados();
        RepositorioEventos.getInstance().cargarEvento(new EventoConFecha(null, "evento1", LocalDateTime.now().plusDays(2), new HashSet<Guardarropa>()));
        RepositorioEventos.getInstance().cargarEvento(new EventoConFecha(null, "evento2", LocalDateTime.now().plusDays(3), new HashSet<Guardarropa>()));
        RepositorioEventos.getInstance().cargarEvento(new EventoConFecha(null, "evento3", LocalDateTime.now().plusDays(4), new HashSet<Guardarropa>()));
    }

    public void filtrarPorRangoFechas() {
        this.eventos = RepositorioEventos.getInstance()
                .proximosEventosEntre(LocalDate.parse(fechaDesde).atStartOfDay(), LocalDate.parse(fechaHasta).atStartOfDay())
                .stream()
                .map(eventoConFecha -> new EventoItem(eventoConFecha.getNombre(), eventoConFecha.getFecha().toLocalDate(),
                !eventoConFecha.getSugerencias().isEmpty()))
                .collect(Collectors.toList());
        		
    }
}
