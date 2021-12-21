import static org.junit.Assert.assertNotNull;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.dao.systeme.VersionDao;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextInterceptor.xml", "classpath:applicationContextDao-test-mysql.xml"}) 
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class Test {

	@Autowired
	private VersionDao versionDao;
	
	@org.junit.Test
	public void test() {
		assertNotNull(versionDao);
		assertNotNull(versionDao.findById(1));
		assertNotNull(versionDao.findByDateAntiChronologique());
	}
}
