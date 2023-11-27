package fr.aphp.tumorotek.manager.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jpa.AbstractJpaTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Classe qui va gérer les tests sur les Managers.
 *
 *  Notez que l'utilisation de la classe AbstractManagerTest4 ne fonctionnera pas,
 *  car elle présente des conflits de dépendances dans sa définition.
 *  Si vous souhaitez étendre cette classe pour tester un "Manager"", assurez-vous de l'inclure
 *  dans le fichier applicationContextManagerTest.xml.
 *
 * Pour tester les DAO, étendez la classe AbstractDAOTest.
 *
 * Cette classe abstraite sert de base pour tous les tests sur les managers.
 * Elle utilise le {@link SpringJUnit4ClassRunner} pour intégrer le framework Spring avec JUnit.
 * Le contexte Spring nécessaire aux tests est configuré via l'annotation {@link ContextConfiguration},
 * spécifiant l'emplacement du fichier de configuration {@code applicationContextManager-test-mysql.xml}.
 *
 * Assurez-vous d'annoter vos méthodes de test avec {@link Test}, car {@link AbstractJpaTests} est déprécié
 * et l'utilisation de {@link Test} est nécessaire pour indiquer les méthodes de test à exécuter.
 *
 * @version 23/11/2023
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContextManagerForTest.xml")
public abstract class AbstractManagerTest extends AbstractTransactionalJUnit4SpringContextTests
{





}





