/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 * <p>
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.dao.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.jpa.AbstractJpaTests;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;

/**
 * Classe qui va gérer l'ensemble des tests sur les DAOs.
 *
 * @author Pierre Ventadour
 * @version 09/09/2009
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {
        "classpath:db-config-test-h2.xml",
        "classpath:applicationContextDao.xml",
        "classpath:applicationContextDao-codes.xml",
        "classpath:applicationContextDao-interfacages.xml"
})
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})

public abstract class AbstractDaoTest extends AbstractJpaTests {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    protected EntityManagerFactory entityManagerFactoryCodes;

    @Autowired
    protected EntityManagerFactory entityManagerFactoryInterfacages;

    protected EntityManager entityManager;
    protected EntityManager entityManagerCodes;
    protected EntityManager entityManagerInterfacages;

    @Before
    public void initEntityManagers() {
        entityManager = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
        entityManagerCodes = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactoryCodes);
        entityManagerInterfacages = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactoryInterfacages);
        assertTrue(entityManager != null);
        assertNotNull(entityManagerCodes);
    }


    @Before
    public void logContextFiles() throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:*.xml");
        for (Resource r : resources) {
            System.out.println("Found: " + r.getFilename());
        }
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }


}