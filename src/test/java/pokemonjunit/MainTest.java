
package pokemonjunit;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Test;

import entitybeans.Pokemon;
import io.helidon.microprofile.tests.junit5.HelidonTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@HelidonTest
class MainTest {

	@Inject
	private WebTarget target;

	@Test
	void testPokemonTypes() {
		JsonArray types = target.path("pokemon/type/Caterpie").request().get(JsonArray.class);
		assertTrue(types.size() >= 0);
	}

	private int getPokemonCount() {
		JsonArray pokemons = target.path("pokemon").request().get(JsonArray.class);
		return pokemons.size();
	}

	@Test
	void testPokemon() {
		assertTrue(getPokemonCount() >= 1);

		Pokemon pokemon = target.path("pokemon/1").request().get(Pokemon.class);
		assertThat(pokemon.getName(), is("Bulbasaur"));

		pokemon = target.path("pokemon/name/Charmander").request().get(Pokemon.class);
		assertThat(pokemon.getType(), is("Rock"));

		try (Response response = target.path("pokemon/1").request().get()) {
			assertThat(response.getStatus(), is(200));
		}

		Pokemon test = new Pokemon();
		test.setType("1");
		test.setId("100");
		test.setName("Test");
		test.setAge(20);
		try (Response response = target.path("pokemon").request()
				.post(Entity.entity(test, MediaType.APPLICATION_JSON))) {
			assertThat(response.getStatus(), is(204));
			assertTrue(getPokemonCount() >= 1);
		}

		try (Response response = target.path("pokemon/100").request().delete()) {
			assertThat(response.getStatus(), is(204));
			assertTrue(getPokemonCount() >= 1);
		}
	}

	@Test
	void testHealthMetrics() {
		try (Response response = target.path("health").request().get()) {
			assertThat(response.getStatus(), is(200));
		}
		try (Response response = target.path("metrics").request().get()) {
			assertThat(response.getStatus(), is(200));
		}
	}

}
