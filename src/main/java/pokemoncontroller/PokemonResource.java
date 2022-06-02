
package pokemoncontroller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entitybeans.Pokemon;


/**
 * This Controller implements REST endpoints to interact with Pokemons. The following end points it will support 
 * 
 *
 * <ul>
 * <li>GET /pokemon: Retrieve list of all pokemons</li>
 * <li>GET /pokemon/{id}: Retrieve single pokemon by ID</li>
 * <li>GET /pokemon/name/{name}: Retrieve single pokemon by name</li>
 * <li>DELETE /pokemon/{id}: Delete a pokemon by ID</li>
 * <li>POST /pokemon: Create/Update a new pokemon</li>
 * <li>GET /pokemon/type: Retrieve list of all pokemons types</li> 
 * </ul>
 *
 * 
 */
@Path("pokemon")
public class PokemonResource {

    @PersistenceContext(unitName = "pokemon")
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pokemon> getPokemons() {
       return entityManager.createNamedQuery("getPokemons", Pokemon.class).getResultList();
    	
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Pokemon getPokemonById(@PathParam("id") String id) {
        Pokemon pokemon = entityManager.find(Pokemon.class, id);
        if (pokemon == null) {
            throw new NotFoundException("Unable to find pokemon with ID " + id);
        }
        return pokemon;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public void deletePokemon(@PathParam("id") String id) {
        Pokemon pokemon = getPokemonById(id);
        entityManager.remove(pokemon);
    }

    @GET
    @Path("name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Pokemon getPokemonByName(@PathParam("name") String name) {
        TypedQuery<Pokemon> query = entityManager.createNamedQuery("getPokemonByName", Pokemon.class);
        List<Pokemon> list = query.setParameter("name", name).getResultList();
        if (list.isEmpty()) {
            throw new NotFoundException("Unable to find pokemon with name " + name);
        }
        return list.get(0);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public void createPokemon(Pokemon pokemon) {
        try {
            System.out.println("came to update / create method " +pokemon);
            
            Pokemon pokemonDBObj = entityManager.find(Pokemon.class, pokemon.getId());
            if (pokemonDBObj != null)
            {
            entityManager.merge(pokemon);
            }else {
            entityManager.persist(pokemon);
            }
        } catch (Exception e) {
            throw new BadRequestException("Error in creating/updating pokemon with ID " + pokemon.getId());
        }
    }
    
    @GET
    @Path("type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pokemon> getPokemonTypes(@PathParam("type") String type) {
    	
    	TypedQuery<Pokemon> query = entityManager.createNamedQuery("getPokemonByType", Pokemon.class);
        List<Pokemon> list = query.setParameter("type", type).getResultList();
        return list;
    }
}
