package de.hawhh.in2.aufg2;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

import org.junit.jupiter.api.Test;

class VersionTest
{

	@Test
	void testParallelUpdate()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("haw_demo2");
		EntityManager em = emf.createEntityManager();
		EntityManager em2 = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.createQuery("Delete from Customer").executeUpdate();
		final Customer test1 = new Customer("122", "Mr.", "Heckenschere", "12.04.2001");
		em.persist(test1);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		em.find(Customer.class, "122").setFirstName("Neuer");
		em.find(Customer.class, "122").setFamilyName("Custo");
		em.find(Customer.class, "122").setEntryDate("13.04.2001");
		
		em2.getTransaction().begin();
		em2.find(Customer.class, "122").setFirstName("NeuerKonflikt");
		em2.find(Customer.class, "122").setFamilyName("Konflikto");
		em2.find(Customer.class, "122").setEntryDate("13.04.2000");
		
		em.getTransaction().commit();

		assertThrows(RollbackException.class, () -> {em2.getTransaction().commit();}); //hier sollte eine RollbackException geworfen werden
		
		final Customer test2 = new Customer("222", "Mr.", "Heckenschere", "12.04.2001");
		em.getTransaction().begin();
		em.persist(test2);
		em.getTransaction().commit();
		
		em2.getTransaction().begin();
		em2.find(Customer.class, "222").setFirstName("NeuerKonflikt");
		
		em.getTransaction().begin();
		em.find(Customer.class, "222").setEntryDate("13.04.2000");
		
		em.getTransaction().commit();
		
		assertThrows(OptimisticLockException.class, () -> {em2.flush();}); //hier dagegen eine OptimisticLockException
	}
	
	@Test
	void testUpdateHintereinander()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("haw_demo2");
		EntityManager em = emf.createEntityManager();
		EntityManager em2 = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.createQuery("Delete from Customer").executeUpdate();
		final Customer test1 = new Customer("122", "Mr.", "Heckenschere", "12.04.2001");
		em.persist(test1);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		em.find(Customer.class, "122").setFirstName("Neuer");
		em.find(Customer.class, "122").setFamilyName("Custo");
		em.find(Customer.class, "122").setEntryDate("13.04.2001");
		em.getTransaction().commit();
		assertEquals(em.find(Customer.class, "122").getFirstName(), "Neuer");
		assertEquals(em.find(Customer.class, "122").getFamilyName(), "Custo");
		assertEquals(em.find(Customer.class, "122").getEntryDate(), "13.04.2001");
		
		em2.getTransaction().begin();
		em2.find(Customer.class, "122").setFirstName("Second");
		em2.find(Customer.class, "122").setFamilyName("Typ");
		em2.find(Customer.class, "122").setEntryDate("13.04.2000");
		em2.getTransaction().commit();
		assertEquals(em2.find(Customer.class, "122").getFirstName(), "Second");
		assertEquals(em2.find(Customer.class, "122").getFamilyName(), "Typ");
		assertEquals(em2.find(Customer.class, "122").getEntryDate(), "13.04.2000");
	}
}
