package queMePongo.evento;

public class JobEventosPendientes {

	public static void main(String[] args) {
		System.out.println("Job inicializado.");
		RepositorioEventos.getInstance().ejecutarEventosConFecha();
		RepositorioEventos.getInstance().validarTemperaturaEventos();
		RepositorioEventos.getInstance().finalizarEventos();
		System.out.println("Job ejecutado correctamente.");
	}

}
