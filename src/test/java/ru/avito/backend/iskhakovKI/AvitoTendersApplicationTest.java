package ru.avito.backend.iskhakovKI;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AvitoTendersApplicationTest {

	@Test
	public void contextLoads() {
		assertEquals("Expected correct message","Hello World","Hello "+"World");
	}

}
