package queMePongo.notificaciones;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import queMePongo.evento.Evento;
import queMePongo.usuario.Usuario;

@Entity
public class WhatsAppConcreteObserver extends NotificadorObserver {
	
    @Override
    public void notificarSugerenciasListas(Usuario usuario, Evento evento) {}
    
    @Override
    public void notificarCambiosSugerencias(Usuario usuario, Evento evento) {}
}
