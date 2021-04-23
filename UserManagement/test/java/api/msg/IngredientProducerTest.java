package api.msg;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.aerogear.kafka.SimpleKafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.model.Ingredient;
import domain.service.IngredientService;

@ExtendWith(MockitoExtension.class)
class IngredientProducerTest {

	@Mock
	private SimpleKafkaProducer<String, Ingredient> kafkaProducer;
	@Mock
	private IngredientService ingredientService;

	@InjectMocks
	private IngredientProducer producer;

	@Test
	void testSendAllIngredients() {
		List<Ingredient> ingredients = getRandomIngredientCollection();
		// test print
		System.out.println("TEST SendAllIngredients : " + ingredients);
		// end test print (no id because not created (in service)
		when(ingredientService.getAll()).thenReturn(ingredients);
		producer.sendAllIngredients();
		verify(kafkaProducer, times(ingredients.size())).send(eq("ingredients"), any(Ingredient.class));
	}

	@Test
	void testSendIngredient() {
		Ingredient ingredients = getRandomIngredient();
		producer.send(ingredients);
		verify(kafkaProducer, times(1)).send("ingredients", ingredients);
	}

	@Test
	void testSendLong() {
		Ingredient ingredients = getRandomIngredient();
		System.out.println("TEST testSendLong : " + ingredients);
		when(ingredientService.get(ingredients.getId())).thenReturn(ingredients);
		producer.send(ingredients.getId());
		verify(kafkaProducer, times(1)).send("ingredients", ingredients);
	}

	@Test
	void testSendLongNull() {
		Ingredient ingredients = getRandomIngredient();
		when(ingredientService.get(ingredients.getId())).thenReturn(null);
		producer.send(ingredients.getId());
		verify(kafkaProducer, times(0)).send("ingredients", ingredients);
	}

	private List<Ingredient> getRandomIngredientCollection() {
		List<Ingredient> ingredients = new ArrayList<>();
		//long numberOfIngredient = Math.round((Math.random() * 1000));
		long numberOfIngredient = 3;
		for (int i = 0; i < numberOfIngredient; i++) {
			ingredients.add(getRandomIngredient());
		}
		return ingredients;
	}

	private Ingredient getRandomIngredient() {
		Ingredient ing = new Ingredient();
		//ing.setName(UUID.randomUUID().toString());
		//ing.setId((long) 1);
		ing.setName("ingredient test0");
		ing.setKcal(8);
		//ing.setFat((int) Math.round(Math.random()*1000));
		ing.setFat(3);
		ing.setCholesterol(4);
		ing.setProtein(5);
		ing.setCholesterol(6);
		ing.setSalt(7);
		return ing;
	}
	
}