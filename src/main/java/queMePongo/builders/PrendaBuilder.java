package queMePongo.builders;


import queMePongo.exceptions.ColorSecundarioIgualPrimarioException;
import queMePongo.exceptions.FaltanDatosException;
import queMePongo.exceptions.MaterialInconsistenteException;
import queMePongo.prenda.Material;
import queMePongo.prenda.Prenda;
import org.apache.commons.lang3.tuple.Pair;
import queMePongo.prenda.TipoPrenda;
import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PrendaBuilder {
	private String nombre;
    private TipoPrenda tipoPrenda;
    private Material material;
    private Color colorPrimario;
    private Color colorSecundario;
    private String urlImagen;
    
    public PrendaBuilder setNombre(String nombre) {
    	this.nombre = nombre;
    	return this;
    }

    public PrendaBuilder setTipoPrenda(TipoPrenda tipoPrenda) {
        this.tipoPrenda = tipoPrenda;
        return this;
    }

    public PrendaBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public PrendaBuilder setColorPrimario(Color colorPrimario) {
        this.colorPrimario = colorPrimario;
        return this;
    }

    public PrendaBuilder setColorSecundario(Color colorSecundario) {
        this.colorSecundario = colorSecundario;
        return this;
    }

    public PrendaBuilder setImagen(String urlImagen) {
        this.urlImagen = urlImagen;
        return this;
    }

    public Prenda build() {
        this.validarPrenda();
        Prenda prenda = new Prenda(nombre, tipoPrenda, material, colorPrimario, colorSecundario, urlImagen);
        return prenda;
    }

    private void validarPrenda() {
    	Pair<String, String> nombreError = Pair.of(nombre, "Debe especificar el nombre de la prenda");
        Pair<TipoPrenda, String> tipoPrendaError = Pair.of(tipoPrenda, "Debe especificar el tipo de prenda");
        Pair<Material, String> materialError = Pair.of(material, "Debe especificar el material");
        Pair<Color, String> colorPrimarioError = Pair.of(colorPrimario, "Debe especificar el color primario");

        List<String> mensajesError = Arrays.asList(nombreError, tipoPrendaError, materialError, colorPrimarioError)
                                            .stream()
                                            .filter(pair -> pair.getLeft() == null)
                                            .map(pair -> pair.getRight())
                                            .collect(Collectors.toList());

        if (mensajesError.size() > 0) throw new FaltanDatosException(String.join("\n", mensajesError));
        
        if (!this.tipoPrenda.puedeSerDeMaterial(this.material)) throw new MaterialInconsistenteException();

        if (colorPrimario.equals(colorSecundario)) throw new ColorSecundarioIgualPrimarioException();
        
        new ValidadorExistencia(tipoPrenda, material, colorPrimario).validar();
        new ValidadorMaterial(tipoPrenda, material).validar();
        new ValidadorColores(colorPrimario, colorSecundario).validar();
    }
}
