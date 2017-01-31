package br.diego.leilaoonline.leilao;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Type;

import javax.money.Monetary;

import org.hamcrest.Matchers;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class LeilaoWSTest {

	private Usuario mauricio;
	private Usuario guilherme;
	private Leilao geladeira;

	@Before
	public void setUp() {

		mauricio = Usuario.builder()
				.id(1L)
				.nome("Mauricio Aniche")
				.email("mauricio.aniche@caelum.com.br")
				.build();

		guilherme = Usuario.builder()
				.id(2L)
				.nome("Guilherme Silveira")
				.email("guilherme.silveira@caelum.com.br")
				.build();

		geladeira = Leilao.builder()
				.id(1L)
				.descricao("Geladeira")
				.valorInicial(Money.of(800.0, Monetary.getCurrency("BRL")))
				.dono(mauricio)
				.create();
	}
	
	@Test
	public void deveRetornarLeilao() {
		String json = given()
		.header("Accept", "application/json")
		.params("leilao.id", 1)
		.get("/leiloes/show")
		.andReturn()
		.asString();
		

		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Leilao.class, new JsonDeserializer<Leilao>() {

			@Override
			public Leilao deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext arg2) throws JsonParseException {
				JsonObject leilaoJson = jsonElement.getAsJsonObject().get("leilao").getAsJsonObject();
				
				GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Money.class, new JsonDeserializer<Money>() {

					@Override
					public Money deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext arg2)
							throws JsonParseException {
						return Money.of(jsonElement.getAsJsonPrimitive().getAsDouble(), Monetary.getCurrency("BRL"));
					}
					
				});
				
				Money valorInicial = builder.create().fromJson(leilaoJson.get("valorInicial"), Money.class);
			
				Leilao leilao = builder.create().fromJson(leilaoJson, Leilao.class);
				
				return leilao.getBuilder().valorInicial(valorInicial).create();
				
			}
		});

		Leilao leilao = builder.create().fromJson(json, Leilao.class);
		
		assertThat(leilao, Matchers.equalTo(geladeira));
	}
}
