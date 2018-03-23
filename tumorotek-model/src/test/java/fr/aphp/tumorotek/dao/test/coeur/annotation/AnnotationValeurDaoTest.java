/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
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
 *
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
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.dao.test.coeur.annotation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.annotation.AnnotationValeurDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.FichierDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 *
 * Classe de test pour le DAO AnnotationValeurDao et le
 * bean du domaine AnnotationValeur.
 * Classe de test créée le 01/02/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1.1
 *
 */
public class AnnotationValeurDaoTest extends AbstractDaoTest
{

   /** Beans Dao. */
   private AnnotationValeurDao annotationValeurDao;
   private ItemDao itemDao;
   private ChampAnnotationDao champAnnotationDao;
   private BanqueDao banqueDao;
   private TableAnnotationDao tableAnnotationDao;
   private FichierDao fichierDao;
   private EntiteDao entiteDao;

   /**
    * Constructeur.
    */
   public AnnotationValeurDaoTest(){}

   public void setItemDao(final ItemDao iDao){
      this.itemDao = iDao;
   }

   public void setChampAnnotationDao(final ChampAnnotationDao cDao){
      this.champAnnotationDao = cDao;
   }

   public void setAnnotationDefautDao(final AnnotationValeurDao avDao){
      this.annotationValeurDao = avDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setTableAnnotationDao(final TableAnnotationDao tabDao){
      this.tableAnnotationDao = tabDao;
   }

   public void setFichierDao(final FichierDao fDao){
      this.fichierDao = fDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Test la méthode toString.
    * @throws ParseException 
    */
   public void testToString() throws ParseException{
      final AnnotationValeur av1 = annotationValeurDao.findById(1);
      assertTrue(av1.toString().equals("{Valeur: Alphanum1.AlphanumValue1}"));
      final AnnotationValeur av2 = new AnnotationValeur();
      assertTrue(av2.toString().equals("{Empty AnnotationValeur}"));
      av2.setChampAnnotation(champAnnotationDao.findById(1));
      assertTrue(av2.toString().equals("{Empty AnnotationValeur}"));
      final Fichier f = new Fichier();
      f.setNom("file1");
      f.setPath("/file1");
      av2.setFichier(f);
      assertTrue(av2.toString().equals("{Valeur: Alphanum1.{/file1}}"));
      av2.setItem(itemDao.findById(1));
      assertTrue(av2.toString().equals("{Valeur: Alphanum1.item1-1}"));
      av2.setTexte("You and me always");
      assertTrue(av2.toString().equals("{Valeur: Alphanum1.You ...}"));
      av2.setTexte("You");
      assertTrue(av2.toString().equals("{Valeur: Alphanum1.You}"));
      final Calendar sDate = Calendar.getInstance();
      sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/02/2006 12:02:45"));
      av2.setDate(sDate);
      assertTrue(av2.toString().equals("{Valeur: Alphanum1.12/02/2006 12:02:45}"));
      av2.setBool(true);
      assertTrue(av2.toString().equals("{Valeur: Alphanum1.true}"));
      av2.setAlphanum("Dogg");
      assertTrue(av2.toString().equals("{Valeur: Alphanum1.Dogg}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllValeurs(){
      final List<AnnotationValeur> valeurs = annotationValeurDao.findAll();
      assertTrue(valeurs.size() == 12);
   }

   public void testFindByChampAndObjetId(){
      List<AnnotationValeur> valeurs = annotationValeurDao.findByChampAndObjetId(champAnnotationDao.findById(12), 1);
      assertTrue(valeurs.size() == 2);
      valeurs = annotationValeurDao.findByChampAndObjetId(champAnnotationDao.findById(8), 1);
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurDao.findByChampAndObjetId(champAnnotationDao.findById(4), 3);
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurDao.findByChampAndObjetId(null, null);
      assertTrue(valeurs.size() == 0);
   }

   public void testFindByTableAndBanque(){
      List<AnnotationValeur> valeurs =
         annotationValeurDao.findByTableAndBanque(tableAnnotationDao.findById(3), banqueDao.findById(1));
      assertTrue(valeurs.size() == 3);
      valeurs = annotationValeurDao.findByTableAndBanque(tableAnnotationDao.findById(3), banqueDao.findById(3));
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurDao.findByTableAndBanque(tableAnnotationDao.findById(4), banqueDao.findById(1));
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurDao.findByTableAndBanque(null, null);
      assertTrue(valeurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      final AnnotationValeur av1 = annotationValeurDao.findById(1);
      List<AnnotationValeur> valeurs = annotationValeurDao.findByExcludedId(av1.getChampAnnotation(), av1.getObjetId(), 1);
      assertTrue(valeurs.size() == 0);
      assertFalse(valeurs.contains(av1));
      final AnnotationValeur av12 = annotationValeurDao.findById(12);
      valeurs = annotationValeurDao.findByExcludedId(av12.getChampAnnotation(), av12.getObjetId(), 12);
      assertTrue(valeurs.size() == 1);
      valeurs = annotationValeurDao.findByExcludedId(null, null, null);
      assertTrue(valeurs.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un Item.
    */
   @Rollback(false)
   public void testCrudValeur(){
      final AnnotationValeur av2 = new AnnotationValeur();
      av2.setObjetId(2);
      av2.setChampAnnotation(champAnnotationDao.findById(2));
      av2.setAlphanum("val1");
      av2.setBanque(banqueDao.findById(1));

      annotationValeurDao.createObject(av2);
      assertNotNull(av2.getAnnotationValeurId());

      final Integer aId = av2.getAnnotationValeurId();

      assertTrue(annotationValeurDao.findByChampAndObjetId(champAnnotationDao.findById(2), 2).size() == 1);
      assertTrue(annotationValeurDao.findById(aId).getAlphanum().equals("val1"));

      // update
      av2.setAlphanum("val2");

      annotationValeurDao.updateObject(av2);
      assertTrue(av2.getAnnotationValeurId() == aId);
      assertTrue(annotationValeurDao.findById(aId).getAlphanum().equals("val2"));

      // Test de la délétion
      annotationValeurDao.removeObject(aId);
      assertNull(annotationValeurDao.findById(aId));
      testReadAllValeurs();
   }

   /**
    * Test des méthodes surchargées "equals" et hashcode.
    */
   public void testEqualsAndHashCode(){
      final AnnotationValeur av1 = new AnnotationValeur();
      final AnnotationValeur av2 = new AnnotationValeur();
      assertFalse(av1.equals(null));
      assertNotNull(av2);
      assertTrue(av1.equals(av1));
      assertTrue(av1.equals(av2));
      assertTrue(av1.hashCode() == av2.hashCode());

      final Integer i1 = new Integer(1);
      final Integer i2 = new Integer(2);
      final Integer i3 = new Integer(2);

      av1.setObjetId(i1);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av2.setObjetId(i2);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av1.setObjetId(i2);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertTrue(av1.hashCode() == av2.hashCode());
      av1.setObjetId(i3);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertTrue(av1.hashCode() == av2.hashCode());

      final ChampAnnotation c1 = champAnnotationDao.findById(1);
      final ChampAnnotation c2 = champAnnotationDao.findById(2);
      final ChampAnnotation c3 = new ChampAnnotation();
      c3.setNom(c2.getNom());
      c3.setTableAnnotation(c2.getTableAnnotation());
      assertFalse(c1.equals(c2));
      assertFalse(c1.hashCode() == c2.hashCode());
      assertTrue(c2.equals(c3));
      av1.setChampAnnotation(c1);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av2.setChampAnnotation(c2);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av1.setChampAnnotation(c3);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertTrue(av1.hashCode() == av2.hashCode());
      av1.setChampAnnotation(c2);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertTrue(av1.hashCode() == av2.hashCode());

      final Item it1 = itemDao.findById(1);
      final Item it2 = itemDao.findById(2);
      final Item it3 = new Item();
      it3.setLabel(it2.getLabel());
      it3.setChampAnnotation(it2.getChampAnnotation());
      it3.setPlateforme(it2.getPlateforme());
      assertFalse(it1.equals(i2));
      assertFalse(it1.hashCode() == it2.hashCode());
      assertTrue(it2.equals(it3));
      av1.setItem(it1);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av2.setItem(it2);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av1.setItem(it3);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertFalse(av1.hashCode() == av2.hashCode());
      av1.setItem(it2);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertTrue(av1.hashCode() == av2.hashCode());

      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = new Banque();
      b3.setNom(b2.getNom());
      b3.setPlateforme(b2.getPlateforme());
      assertFalse(b1.equals(b2));
      assertFalse(b1.hashCode() == b2.hashCode());
      assertTrue(b2.equals(b3));
      av1.setBanque(b1);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av2.setBanque(b2);
      assertFalse(av1.equals(av2));
      assertFalse(av2.equals(av1));
      assertTrue(av1.hashCode() != av2.hashCode());
      av1.setBanque(b3);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertTrue(av1.hashCode() == av2.hashCode());
      av1.setBanque(b2);
      assertTrue(av1.equals(av2));
      assertTrue(av2.equals(av1));
      assertTrue(av1.hashCode() == av2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(av1.equals(c));
   }

   public void testIsEmpty() throws ParseException{
      final AnnotationValeur av1 = new AnnotationValeur();
      assertTrue(av1.isEmpty());
      av1.setChampAnnotation(champAnnotationDao.findById(1));
      av1.setObjetId(1);
      av1.setBanque(banqueDao.findById(2));
      assertTrue(av1.isEmpty());
      av1.setAlphanum("ee");
      assertFalse(av1.isEmpty());
      av1.setAlphanum(null);
      av1.setBool(true);
      assertFalse(av1.isEmpty());
      av1.setBool(null);
      final Calendar sDate = Calendar.getInstance();
      sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/02/2006 12:02:45"));
      av1.setDate(sDate);
      assertFalse(av1.isEmpty());
      av1.setDate(null);
      av1.setTexte("eee");
      assertFalse(av1.isEmpty());
      av1.setTexte(null);
      av1.setItem(itemDao.findById(1));
      assertFalse(av1.isEmpty());
      av1.setItem(null);
      assertTrue(av1.isEmpty());
   }

   public void testClone() throws IOException{
      final AnnotationValeur av1 = annotationValeurDao.findById(5);
      // rempli pour contourner les nulls
      av1.setAlphanum("zert");
      av1.setBool(true);
      av1.setTexte("ee");
      av1.setItem(itemDao.findById(1));
      av1.setFichier(fichierDao.findById(1));
      final byte[] byteArray = "erfr".getBytes();
      final ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
      av1.setStream(bais);
      AnnotationValeur clone = av1.clone();

      assertTrue(av1.equals(clone));
      assertTrue(av1.hashCode() == clone.hashCode());
      assertTrue(clone.getAnnotationValeurId().equals(av1.getAnnotationValeurId()));
      assertTrue(clone.getChampAnnotation().equals(av1.getChampAnnotation()));
      assertTrue(clone.getObjetId().equals(av1.getObjetId()));
      assertTrue(clone.getAlphanum().equals(av1.getAlphanum()));
      assertTrue(clone.getBool().equals(av1.getBool()));
      assertTrue(clone.getDate().equals(av1.getDate()));
      assertTrue(clone.getTexte().equals(av1.getTexte()));
      assertTrue(clone.getItem().equals(av1.getItem()));
      assertTrue(clone.getFichier().equals(av1.getFichier()));
      // assertTrue(clone.getStream().equals(av1.getStream()));
      final byte[] buffer = new byte[8192];
      final byte[] buffer2 = new byte[8192];
      while((clone.getStream().read(buffer)) > 0){
         av1.getStream().read(buffer2);
         Assert.assertArrayEquals(buffer, buffer2);
      }
      assertTrue(clone.getBanque().equals(av1.getBanque()));

      av1.setFichier(null);
      clone = av1.clone();
      assertNull(clone.getFichier());
   }

   public void testFindByObjectIdAndEntite(){
      List<AnnotationValeur> vals =
         annotationValeurDao.findByObjectIdAndEntite(new Integer(1), entiteDao.findByNom("Echantillon").get(0));
      assertTrue(vals.size() == 6);

      vals = annotationValeurDao.findByObjectIdAndEntite(new Integer(1), entiteDao.findByNom("Patient").get(0));
      assertTrue(vals.size() == 1);

      vals = annotationValeurDao.findByObjectIdAndEntite(new Integer(3), entiteDao.findByNom("Echantillon").get(0));
      assertTrue(vals.size() == 0);
   }

   public void testFormateAnnotationValeur(){
      final AnnotationValeur av1 = annotationValeurDao.findById(1);
      assertTrue(av1.formateAnnotationValeur().equals("AlphanumValue1"));

      final AnnotationValeur av7 = annotationValeurDao.findById(7);
      assertTrue(av7.formateAnnotationValeur().equals("textVal1"));

      final AnnotationValeur av11 = annotationValeurDao.findById(11);
      assertTrue(av11.formateAnnotationValeur().equals("http://google.com"));

      final AnnotationValeur av5 = annotationValeurDao.findById(5);
      assertTrue(av5.formateAnnotationValeur().equals("12/12/2002 10:00"));

      final AnnotationValeur av4 = annotationValeurDao.findById(4);
      assertTrue(av4.formateAnnotationValeur().equals("Oui"));

      final AnnotationValeur av8 = annotationValeurDao.findById(8);
      assertTrue(av8.formateAnnotationValeur().equals("item1-1"));

      // @since 2.1 teste nom fichier
      final AnnotationValeur av10 = annotationValeurDao.findById(10);
      assertTrue(av10.formateAnnotationValeur().equals("nom1"));
   }

   public void testFindCountByItem(){
      Item i = itemDao.findById(5);
      List<Long> count = annotationValeurDao.findCountByItem(i);
      assertTrue(count.get(0) == 1);
      i = itemDao.findById(2);
      count = annotationValeurDao.findCountByItem(i);
      assertTrue(count.get(0) == 0);
   }

   public void testFindCountByTableAnnotationBanque(){
      final TableAnnotation t3 = tableAnnotationDao.findById(3);
      final Banque b1 = banqueDao.findById(1);
      List<Long> count = annotationValeurDao.findCountByTableAnnotationBanque(t3, b1);
      assertTrue(count.get(0) == 3);
      final Banque b2 = banqueDao.findById(2);
      count = annotationValeurDao.findCountByTableAnnotationBanque(t3, b2);
      assertTrue(count.get(0) == 2);
      final TableAnnotation t5 = tableAnnotationDao.findById(5);
      count = annotationValeurDao.findCountByTableAnnotationBanque(t5, b2);
      assertTrue(count.get(0) == 0);
      count = annotationValeurDao.findCountByTableAnnotationBanque(t5, b1);
      assertTrue(count.get(0) == 3);
      count = annotationValeurDao.findCountByTableAnnotationBanque(t5, null);
      assertTrue(count.get(0) == 0);
      count = annotationValeurDao.findCountByTableAnnotationBanque(null, b1);
      assertTrue(count.get(0) == 0);

   }

   public void testGetValeur() throws ParseException{
      final AnnotationValeur av = new AnnotationValeur();
      final DataType type = new DataType();
      final ChampAnnotation chp = new ChampAnnotation();
      av.setChampAnnotation(chp);
      chp.setDataType(type);

      av.setAlphanum("test2");
      av.setBool(true);
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("22/01/1987"));
      av.setDate(cal);
      av.setTexte("texte");
      final Fichier file = new Fichier();
      file.setNom("file1");
      av.setFichier(file);
      final Item i = new Item();
      i.setLabel("ite1");
      av.setItem(i);

      type.setType("alphanum");
      assertTrue(av.getValeur().equals("test2"));
      type.setType("boolean");
      assertTrue((Boolean) av.getValeur());
      type.setType("date");
      assertTrue(av.getValeur().equals(cal));
      av.setAlphanum("12.33");
      type.setType("num");
      assertTrue(av.getValeur().equals(12.33));
      type.setType("texte");
      assertTrue(av.getValeur().equals("texte"));
      type.setType("thesaurus");
      assertTrue(av.getValeur().equals("ite1"));
      type.setType("fichier");
      assertTrue(av.getValeur().equals("file1"));
      av.setAlphanum("test3");
      type.setType("hyperlien");
      assertTrue(av.getValeur().equals("test3"));
      type.setType("thesaurusM");
      assertTrue(av.getValeur().equals("ite1"));
      type.setType("other");
      assertNull(av.getValeur());
   }

}
