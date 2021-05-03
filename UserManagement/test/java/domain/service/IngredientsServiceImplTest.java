package domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.function.Executable;

import domain.model.Ingredient;
import eu.drus.jpa.unit.api.JpaUnit;

//import domain.service.IngredientServiceImpl;  

@ExtendWith(JpaUnit.class)
@ExtendWith(MockitoExtension.class)
class IngredientsServiceImplTest {

	@Spy
	@PersistenceContext(unitName = "IngredientPUTest")
	EntityManager em;

	@InjectMocks
	private IngredientServiceImpl ingredientsService;
	
	@Test
	void testCreate() {
		Ingredient ingredientTest = getRandomIngredient();
		ingredientsService.create(ingredientTest);
		assertNotNull(ingredientTest.getId());
	}
	
	@Test
	void testCreateNull() {
		assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
            	Ingredient ingredientTest = getRandomIngredient();
            	ingredientTest.setId((long) 1000);
            	ingredientsService.create(ingredientTest);
            }
        });
	}
	
	@Test
	void testGet() {
		ingredientsService.create(getRandomIngredient());
		Ingredient ingredient = ingredientsService.getAll().get(0);
		assertNotNull(ingredient);
		Long id = ingredient.getId();
		Ingredient getIngredient = ingredientsService.get(id);
		assertEquals(ingredient.getId(), getIngredient.getId());
	}
	
	@Test
	void testGetAll() {
		List<Ingredient> ingredients = ingredientsService.getAll();
		int size = ingredients.size();
		
		ingredientsService.create(getRandomIngredient()); // tests create at the same time 
		ingredientsService.create(getRandomIngredient1());
		ingredientsService.create(getRandomIngredient2());
		ingredientsService.create(getRandomIngredient3());
		
		assertEquals(size + 4, ingredientsService.getAll().size());
	}
	
	@Test
	void testGetAllNames() {
		List<Ingredient> ingredients = ingredientsService.getAll();
		int size = ingredients.size();
		
		ingredientsService.create(getRandomIngredient()); // tests create at the same time 
		ingredientsService.create(getRandomIngredient1());
		ingredientsService.create(getRandomIngredient2());
		ingredientsService.create(getRandomIngredient3());
		
		assertEquals(size + 4, ingredientsService.getAllNames().size());
	}
	
	@Test
	void testGetSelectedIngredients() {
		ingredientsService.create(getRandomIngredient()); // tests create at the same time 
		ingredientsService.create(getRandomIngredient1());
		ingredientsService.create(getRandomIngredient2());
		ingredientsService.create(getRandomIngredient3());
		
		Ingredient ingredient1 = ingredientsService.getAll().get(0);
		assertNotNull(ingredient1);
		Ingredient ingredient2 = ingredientsService.getAll().get(1);
		assertNotNull(ingredient2);
		
		List<Long> listID = new ArrayList<>();
		listID.add(ingredient1.getId());
		listID.add(ingredient2.getId());
		
		assertEquals(2,ingredientsService.getSelectedIngredients(listID).size());
	}

	@Test
	void testComputeCalories() {
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient1());
		Ingredient ingredient1 = ingredientsService.getAll().get(0);
		assertNotNull(ingredient1);
		Ingredient ingredient2 = ingredientsService.getAll().get(1);
		assertNotNull(ingredient2);
		
		double getKcalTotal = ingredient1.getKcal() + ingredient2.getKcal();
		List<Long> listID = new ArrayList<>();
		listID.add(ingredient1.getId());
		listID.add(ingredient2.getId());

		double testComputeCalories = ingredientsService.computeCalories(listID);
		//assertEquals(getKcalTotal, 11)
		//assertEquals(testComputeCalories, 11);
		assertEquals(getKcalTotal, testComputeCalories); 
	}
	
	@Test
	void testComputeCaloriesNull() {
		assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
            	List<Long> listID = new ArrayList<>();
        		listID.add((long) 1000);
                ingredientsService.computeCalories(listID);
            }
        });
	}
	
	@Test
	void testComputeFat() {
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient1());
		Ingredient ingredient1 = ingredientsService.getAll().get(0);
		assertNotNull(ingredient1);
		Ingredient ingredient2 = ingredientsService.getAll().get(1);
		assertNotNull(ingredient2);

		
		double getFatTotal = ingredient1.getFat() + ingredient2.getFat();
		List<Long> listID = new ArrayList<>();
		listID.add(ingredient1.getId());
		listID.add(ingredient2.getId());

		double testComputeFat = ingredientsService.computeFat(listID);
		//assertEquals(11,getKcalTotal)
		//assertEquals(11,testComputeCalories);
		assertEquals(getFatTotal, testComputeFat); 
	}
	
	@Test
	void testComputeFatNull() {
		assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
            	List<Long> listID = new ArrayList<>();
        		listID.add((long) 1000);
                ingredientsService.computeFat(listID);
            }
        });
	}
	
	@Test
	void testComputeCholesterol() {
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient1());
		Ingredient ingredient1 = ingredientsService.getAll().get(0);
		assertNotNull(ingredient1);
		Ingredient ingredient2 = ingredientsService.getAll().get(1);
		assertNotNull(ingredient2);

		double getCholesterolTotal = ingredient1.getCholesterol() + ingredient2.getCholesterol();
		List<Long> listID = new ArrayList<>();
		listID.add(ingredient1.getId());
		listID.add(ingredient2.getId());
		
		double testComputeCholesterol = ingredientsService.computeCholesterol(listID);
		assertEquals(getCholesterolTotal, testComputeCholesterol); 
	}
	
	@Test
	void testComputeCholesterolNull() {
		assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
            	List<Long> listID = new ArrayList<>();
        		listID.add((long) 1000);
                ingredientsService.computeCholesterol(listID);
            }
        });
	}
	
	@Test
	void testComputeProteins() {
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient1());
		Ingredient ingredient1 = ingredientsService.getAll().get(0);
		assertNotNull(ingredient1);
		Ingredient ingredient2 = ingredientsService.getAll().get(1);
		assertNotNull(ingredient2);
		
		double getProteinsTotal = ingredient1.getProtein() + ingredient2.getProtein();
		List<Long> listID = new ArrayList<>();
		listID.add(ingredient1.getId());
		listID.add(ingredient2.getId());
		
		double testComputeProteins = ingredientsService.computeProteins(listID);
		assertEquals(getProteinsTotal, testComputeProteins); 
	}
	
	@Test
	void testComputeProteinsNull() {
		assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
            	List<Long> listID = new ArrayList<>();
        		listID.add((long) 1000);
                ingredientsService.computeProteins(listID);
            }
        });
	}
	
	@Test
	void testSalt() {
		System.out.println("--- testcomputeSalt : ---");
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient1());
		Ingredient ingredient1 = ingredientsService.getAll().get(0);
		assertNotNull(ingredient1);
		Ingredient ingredient2 = ingredientsService.getAll().get(1);
		assertNotNull(ingredient2);
		
		double getSaltTotal = ingredient1.getSalt() + ingredient2.getSalt();
		List<Long> listID = new ArrayList<>();
		listID.add(ingredient1.getId());
		listID.add(ingredient2.getId());
		
		double testComputeSalt = ingredientsService.computeSalt(listID);
		assertEquals(getSaltTotal, testComputeSalt); 
	}
	
	@Test
	void testComputeSaltNull() {
		assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
            	List<Long> listID = new ArrayList<>();
        		listID.add((long) 1000);
                ingredientsService.computeSalt(listID);
            }
        });
	}
	
	@Test
	void testGetNonExistant() {
		List<Ingredient> ingredients = ingredientsService.getAll();
		assertNull(ingredientsService.get(Long.MAX_VALUE));
	}
	
	@Test
	void testCount() {
		List<Ingredient> ingredients = ingredientsService.getAll();
		int size = ingredients.size();
		
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient());
		ingredientsService.create(getRandomIngredient());
		
		Long count = ingredientsService.count();
		assertEquals(size + 4, count);
	}
	
	
	private Ingredient getRandomIngredient() {
		Ingredient ing = new Ingredient();
		ing.setName("ingredient test0");
		ing.setKcal(8);
		ing.setFat(3);
		ing.setCholesterol(4);
		ing.setProtein(5);
		ing.setCholesterol(6);
		ing.setSalt(7);
		return ing;
	}
	
	
	private Ingredient getRandomIngredient1() {
		Ingredient ing = new Ingredient();
		ing.setName("chocolat au lait");
		ing.setKcal(3.1);
		ing.setFat(3);
		ing.setCholesterol(4);
		ing.setProtein(5);
		ing.setCholesterol(6);
		ing.setSalt(7);
		return ing;
	}
	
	
	private Ingredient getRandomIngredient2() {
		Ingredient ing = new Ingredient();
		ing.setName("chocolat");
		ing.setKcal(8);
		ing.setFat(3);
		ing.setCholesterol(4);
		ing.setProtein(5);
		ing.setCholesterol(6);
		ing.setSalt(7);
		return ing;
	}
	
	
	private Ingredient getRandomIngredient3() {
		Ingredient ing = new Ingredient();
		ing.setName("nothing");
		ing.setKcal(8);
		ing.setFat(3);
		ing.setCholesterol(4);
		ing.setProtein(5);
		ing.setCholesterol(6);
		ing.setSalt(7);
		return ing;
	}
	
}