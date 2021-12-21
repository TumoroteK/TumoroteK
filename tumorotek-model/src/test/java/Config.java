import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;

import fr.aphp.tumorotek.dao.utilisateur.CollaborateurDao;
import fr.aphp.tumorotek.dao.utilisateur.PlateformeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;

@Configuration
@ContextConfiguration("classpath:spring-jpa-test.xml")
@EnableJpaRepositories(basePackages = {"fr.aphp.tumorotek.dao.utilisateur"}, entityManagerFactoryRef = "entityManagerFactory")
public class Config {
	
}
