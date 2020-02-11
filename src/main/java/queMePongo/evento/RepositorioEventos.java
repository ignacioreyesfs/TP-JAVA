package queMePongo.evento;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class RepositorioEventos implements WithGlobalEntityManager, TransactionalOps {
    private Set<Evento> eventosPendientes = new HashSet<>();
    private Set<Evento> eventosRepetitivos = new HashSet<>();
    private static RepositorioEventos instancia;

    public static RepositorioEventos getInstance() {
        if (instancia == null) {
            instancia = new RepositorioEventos();
        }
        return instancia;
    }
    
    //Cada 1 hora
    public void ejecutarEventosConFecha() {
    	entityManager()
                .createQuery("from Evento where DTYPE = 'EventoConFecha'", EventoConFecha.class)
                .getResultList()
                .forEach(eventoConFecha -> eventoConFecha.ejecutar());
    }
    
    //Cada 24 horas
    public void ejecutarEventosRepetitivos() {
        entityManager()
                .createQuery("from Evento where DTYPE = 'EventoRepetitivo'", EventoRepetitivo.class)
                .getResultList()
                .forEach(eventoRepetitivo -> eventoRepetitivo.ejecutar());
    }
    
    //Cada 1 hora
    public void finalizarEventos() {
        entityManager()
                .createQuery("from Evento", Evento.class)
                .getResultList()
                .forEach(evento -> evento.finalizar());
        this.limpiarEventosTerminados();
    }

    //Cada 1 hora
    public void validarTemperaturaEventos() {
        entityManager()
                .createQuery("from Evento where DTYPE ='EventoConFecha'", EventoConFecha.class)
                .getResultList()
                .forEach(eventoConFecha -> eventoConFecha.validarSugerenciasTemperatura());
    }
    
    public Evento guardarEvento(Evento evento) {
		if(evento.getId() == null) {
			entityManager().persist(evento);
		}else {
			evento = entityManager().merge(evento);
		}
		return evento;
	}

	public Evento buscarEventoPorId(int id) {
		return entityManager().find(Evento.class, id);
	}
    
    public List<Evento> proximosEventosEntre(LocalDateTime fechaDesde, LocalDateTime fechaHasta){
    	Set<Evento> proximosEventos = new HashSet<>();
    	proximosEventos.addAll(eventosPendientes);
    	proximosEventos.addAll(eventosRepetitivos);
    	return proximosEventos.stream().flatMap(evento -> evento.eventosEntreFechas(fechaDesde, fechaHasta)
    			.stream()).collect(Collectors.toList());
    }

    public void cargarEvento(Evento evento) {
    	if(evento instanceof EventoConFecha)
    		eventosPendientes.add(evento);
    	else
    		eventosRepetitivos.add(evento);
    	this.guardarEvento(evento);
        
    }
    
    public void eliminarEvento(Evento evento) {
    	if(evento instanceof EventoConFecha)
    		this.eventosPendientes.remove(evento);
    	else
    		this.eventosRepetitivos.remove(evento);
    	
    	if (entityManager().contains(evento)) {
    		entityManager().remove(evento);
        } else {
        	entityManager().remove(entityManager().merge(evento));
        }
    }

    public void limpiarEventos() { eventosPendientes.clear(); }

    public void limpiarEventosTerminados() {
        if(!eventosPendientes.isEmpty()) {
            eventosPendientes.removeIf(evento -> evento.eventoYaTermino());
        }
    }

    public Set<Evento> getEventosPendientes() { 
    	return eventosPendientes;
    }

    public List<Evento> getEventosDe(Integer id) {
        return entityManager()
                .createQuery("from Evento where usuario_id = :id", Evento.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Evento> buscarEventosPorUsuario(int usuario_id) {
        return entityManager()
                .createQuery("from Evento where usuario_id = :usuario_id and DTYPE = 'EventoConFecha'", Evento.class)
                .setParameter("usuario_id", usuario_id)
                .getResultList();
    }
    
	public List<Evento> buscarEventosPorUsuarioProximos(int usuario_id){
        return entityManager()
                .createQuery("from Evento where usuario_id = :usuario_id and DTYPE = 'EventoConFecha' and fecha > :fechaAhora", Evento.class)
                .setParameter("usuario_id", usuario_id)
                .setParameter("fechaAhora", LocalDateTime.now())
                .getResultList();
	}

    public Evento buscarEventoPorNombre(String nombre) {
        return entityManager()
                .createQuery("from Evento where nombre = :nombre", Evento.class)
                .setParameter("nombre", nombre)
                .getSingleResult();

    }

    public void eliminarEventosConNombre(String nombre) {
        entityManager()
                .createQuery("delete from Evento where nombre = :nombre")
                .setParameter("nombre", nombre)
                .executeUpdate();
    }
}
