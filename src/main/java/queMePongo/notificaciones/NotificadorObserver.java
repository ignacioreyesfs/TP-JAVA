package queMePongo.notificaciones;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import queMePongo.evento.Evento;
import queMePongo.usuario.Usuario;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class NotificadorObserver {
	@Id @GeneratedValue
	private int id;
    public abstract void notificarSugerenciasListas(Usuario usuario, Evento evento);
    
    public abstract void notificarCambiosSugerencias(Usuario usuario, Evento evento);
}
