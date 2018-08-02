/** 
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
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

package fr.aphp.tumorotek.manager.impl.systeme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Enregistre les informations nécessaires au déplacement d'un fichier 
 * dans le filesystem adossé à TK.
 * Correctif bug TK-155
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.2.0
 */
public class MvFichier {
	
	private Path fromPath;
	private Path toPath;
	private boolean moved = false;
	
	public MvFichier(Path _a, Path _d) {
		this.fromPath = _a;
		this.toPath = _d;
	}
	
	/**
	 * Déplace le fichier.
	 * Flag moved=true si le movement s'est bien passé.
	 * @throws IOException
	 */
	public void move() throws IOException {
		Files.move(fromPath, toPath);
		moved = true;
	}
	
	/**
	 * Replace le fichier, sil il a  été déplacé (rollback)
	 * Flag moved=false si le movement s'est bien passé.
	 * @throws IOException
	 */
	public void revert() throws IOException {
		if (moved) {
			Files.move(toPath, fromPath);
			moved = false;
		}
	}
	
	public Path getFromPath() {
		return fromPath;
	}
	
	public void setFromPath(Path _a) {
		this.fromPath = _a;
	}
	
	public Path getToPath() {
		return toPath;
	}
	
	public void setToPath(Path _d) {
		this.toPath = _d;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		MvFichier test = (MvFichier) obj;
		return (((this.fromPath != null 
				&& this.fromPath.equals(test.fromPath))
					|| this.fromPath == test.fromPath)
				&& (this.toPath == test.toPath 
					|| (this.toPath != null 
					&& this.toPath.equals(test.toPath))) 
				);
	}
	
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashFromPath = 0;
		int hashToPath = 0;
		
		if (this.fromPath != null) {
			hashFromPath = this.fromPath.hashCode();
		}
		if (this.toPath != null) {
			hashToPath = this.toPath.hashCode();
		}
		
		hash = 31 * hash + hashFromPath;
		hash = 31 * hash + hashToPath;
		
		return hash;	
	}
	
	@Override
	public String toString() {
		return fromPath.toString() + " -> " + toPath.toString();
	}
	
}

