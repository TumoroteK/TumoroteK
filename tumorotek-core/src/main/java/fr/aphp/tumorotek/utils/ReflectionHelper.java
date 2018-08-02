package fr.aphp.tumorotek.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ReflectionHelper {

   public static List<Class<?>> findClassesImplenenting(final Class<?> interfaceClass) {

       if (interfaceClass == null) {
           return null;
       }

       Package fromPackage = interfaceClass.getPackage();

       final List<Class<?>> rVal = new ArrayList<Class<?>>();
       try {
           final Class<?>[] targets = getAllClassesFromPackage(fromPackage.getName());
           if (targets != null) {
               for (Class<?> aTarget : targets) {
                   if (aTarget == null) {
                       continue;
                   }
                   else if (aTarget.equals(interfaceClass)) {
                       continue;
                   }
                   else if (!interfaceClass.isAssignableFrom(aTarget)) {
                       continue;
                   }
                   else {
                       rVal.add(aTarget);
                   }
               }
           }
       }
       catch (ClassNotFoundException e) {
          e.printStackTrace();
       }
       catch (IOException e) {
          e.printStackTrace();
       }

       return rVal;
   }

   /**
    * Load all classes from a package.
    * 
    * @param packageName
    * @return
    * @throws ClassNotFoundException
    * @throws IOException
    */
   public static Class<?>[] getAllClassesFromPackage(final String packageName) throws ClassNotFoundException, IOException {
       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
       assert classLoader != null;
       String path = packageName.replace('.', '/');
       Enumeration<URL> resources = classLoader.getResources(path);
       List<File> dirs = new ArrayList<File>();
       while (resources.hasMoreElements()) {
           URL resource = resources.nextElement();
           dirs.add(new File(resource.getFile()));
       }
       ArrayList<Class<?>> classes = new ArrayList<>();
       for (File directory : dirs) {
           classes.addAll(findClasses(directory, packageName));
       }
       return classes.toArray(new Class[classes.size()]);
   }

   public static Class<?>[] getAllClassesFromPackage(final ClassLoader cl, final String packageName) throws ClassNotFoundException, IOException {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      assert classLoader != null;
      String path = packageName.replace('.', '/');
      Enumeration<URL> resources = classLoader.getResources(path);
      List<File> dirs = new ArrayList<File>();
      while (resources.hasMoreElements()) {
          URL resource = resources.nextElement();
          dirs.add(new File(resource.getFile()));
      }
      ArrayList<Class<?>> classes = new ArrayList<>();
      for (File directory : dirs) {
          classes.addAll(findClasses(directory, packageName));
      }
      return classes.toArray(new Class[classes.size()]);
   }
   
   /**
    * Find file in package.
    * 
    * @param directory
    * @param packageName
    * @return
    * @throws ClassNotFoundException
    */
   public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
       List<Class<?>> classes = new ArrayList<>();
       if (!directory.exists()) {
           return classes;
       }
       File[] files = directory.listFiles();
       for (File file : files) {
           if (file.isDirectory()) {
               assert !file.getName().contains(".");
               classes.addAll(findClasses(file, packageName + "." + file.getName()));
           }
           else if (file.getName().endsWith(".class")) {
               classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
           }
       }
       return classes;
   }
}
