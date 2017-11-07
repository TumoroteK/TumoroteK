<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:template match="Impression">
<fo:root>
	<fo:layout-master-set>
		<!--description de la mise en page-->
		<fo:simple-page-master master-name="simple" page-height="29.7cm" 
												page-width="21cm" 
												margin-top="1cm" 
												margin-bottom="1cm" 
												margin-left="1.5cm" 
												margin-right="1.5cm"
												font-family="Verdana" font-size="12pt" initial-page-number="1">
			<fo:region-body margin-top="2cm" margin-bottom="2cm"/>
			<fo:region-before extent="1cm"/>
			<fo:region-after extent="1cm"/>
		</fo:simple-page-master>
	</fo:layout-master-set>
	<xsl:apply-templates select="Page"/>
</fo:root>
</xsl:template>

<xsl:template match="Page">	
	<fo:page-sequence master-reference="simple">
		<!-- haut de page -->
		<fo:static-content flow-name="xsl-region-before">
			<!--paragraphe signature-->
			<xsl:apply-templates select="../HautDePage"/>
		</fo:static-content>
		<!-- bas de page -->
		<fo:static-content flow-name="xsl-region-after">
			<!--paragraphe signature-->
			<xsl:apply-templates select="../BasDePage"/>
		</fo:static-content>
		<!--ecriture dans le corps de la page-->
		<fo:flow flow-name="xsl-region-body">
			<!--paragraphe no bon de commande-->
			<xsl:apply-templates select="Titre"/>
			<xsl:apply-templates select="Paragraphe"/>
			<xsl:apply-templates select="Liste"/>
		</fo:flow>
	</fo:page-sequence>
</xsl:template>

<xsl:template match="ImageHautDePage">
	<fo:external-graphic width="80px" height="40px"
		content-height="scale-to-fit" content-width="scale-to-fit" >
		<xsl:attribute name="src">
			<xsl:value-of select="text()"/>
		</xsl:attribute>
	</fo:external-graphic>
</xsl:template>

<xsl:template match="Titre">	
	<fo:block text-align="left" space-after.optimum="20pt"
				font-size="12pt" font-weight="bold"
				text-decoration="underline"
				color="#00227c">
		<xsl:value-of select="text()"/>
	</fo:block>
</xsl:template>

<!-- haut de page -->
<xsl:template match="HautDePage">
	<!-- <fo:block text-align="left" font-size="10pt" font-weight="bold"
			font-style="italic" color="#00227c">
		<xsl:value-of select="text()"/>
	</fo:block>-->
	<fo:table width="100%" table-layout="fixed" font-size="10pt">
		<fo:table-column border="0"/>
		<fo:table-column border="0"/>
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell border="0">
					<fo:block text-align="left" font-size="10pt" font-weight="bold"
							font-style="italic" color="#00227c">
						<xsl:value-of select="text()"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border="0">
					<fo:block text-align="right">
						<xsl:apply-templates select="ImageHautDePage"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
</xsl:template>

<!-- Pied de page -->
<xsl:template match="BasDePage">	
	<fo:table width="100%" table-layout="fixed" font-size="10pt">
		<fo:table-column border="0"/>
		<fo:table-column border="0"/>
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell border="0">
					<fo:block font-size="10pt" text-align="left" color="#00227c"
						font-style="italic">
						<xsl:value-of select="text()"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border="0">
					<fo:block font-size="10pt" text-align="right" color="#00227c"
						font-weight="bold">
						Page <fo:page-number />
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
</xsl:template>

<xsl:template match="Paragraphe">
	<fo:block keep-together.within-page="always">
		<xsl:apply-templates select="TitreParagraphe"/>
		<xsl:apply-templates select="Inconnu"/>
		<fo:table width="100%" table-layout="fixed" font-size="9pt" 
			text-align="left">
			<fo:table-column column-width="18cm" border="0"/>
			<fo:table-body>
				<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
				<fo:table-row><fo:table-cell>
				<fo:block>
				<xsl:apply-templates select="Ligne"/>
				</fo:block>
				</fo:table-cell></fo:table-row>
			</fo:table-body>
		</fo:table>
	</fo:block>
	<xsl:apply-templates select="SousParagraphe"/>
	<xsl:apply-templates select="Liste"/>
</xsl:template>

<xsl:template match="SousParagraphe">
	<fo:block keep-together.within-page="always">
		<xsl:apply-templates select="TitreSousParagraphe"/>
		<xsl:apply-templates select="Inconnu"/>
		<fo:table width="100%" table-layout="fixed" font-size="9pt" 
			text-align="left">
			<fo:table-column column-width="18cm" border="0"/>
			<fo:table-body>
				<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
				<fo:table-row><fo:table-cell>
				<fo:block>
				<xsl:apply-templates select="Ligne"/>
				</fo:block>
				</fo:table-cell></fo:table-row>
			</fo:table-body>
		</fo:table>
	</fo:block>
	<xsl:apply-templates select="Liste"/>
</xsl:template>

<xsl:template match="Ligne">
	<xsl:apply-templates select="LigneParagraphe"/>
	<xsl:apply-templates select="LigneDeuxColonnesParagraphe"/>
	<xsl:apply-templates select="LigneSimpleParagraphe"/>
</xsl:template>

<xsl:template match="TitreParagraphe">	
	<fo:block text-align="left" space-before.optimum="15pt"
				space-after.optimum="6pt"
				font-size="12pt" font-weight="bold"
				color="#00227c"
				background-color="#ecf4fb"
				border-bottom="1pt solid #E03F06"
				>
		<xsl:value-of select="text()"/>
	</fo:block>
</xsl:template>

<xsl:template match="TitreSousParagraphe">	
	<fo:block text-align="left"
				space-before.optimum="3pt"
				space-after.optimum="4pt"
				font-size="10pt" font-weight="bold"
				color="#00227c"
				border-bottom="1pt solid #00227c"
				end-indent="40pt"
				>
		<xsl:value-of select="text()"/>
	</fo:block>
</xsl:template>

<xsl:template match="Inconnu">	
	<fo:block font-size="9pt" font-style="italic">
		<xsl:value-of select="text()"/>
	</fo:block>
</xsl:template>

<xsl:template match="LigneParagraphe">
	<fo:table width="100%" table-layout="fixed" font-size="9pt" 
		text-align="left">
		<fo:table-column column-width="4cm" border="0"/>
		<fo:table-column column-width="5cm" border="0"/>
		<fo:table-column column-width="4cm" border="0"/>
		<fo:table-column column-width="5cm" border="0"/>
		<fo:table-body>
			<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
			<fo:table-row>
				<xsl:apply-templates select="CoupleValeurs"/>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
</xsl:template>

<xsl:template match="LigneDeuxColonnesParagraphe">
	<fo:table width="100%" table-layout="fixed" font-size="9pt" 
		text-align="left">
		<fo:table-column column-width="4cm" border="0"/>
		<fo:table-column column-width="14cm" border="0"/>
		<fo:table-body>
			<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
			<fo:table-row>
				<xsl:apply-templates select="CoupleValeurs"/>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
</xsl:template>

<xsl:template match="LigneSimpleParagraphe">
	<fo:table width="100%" table-layout="fixed" font-size="9pt" 
		text-align="left">
		<fo:table-column column-width="18cm" border="0"/>
		<fo:table-body>
			<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
			<fo:table-row>
				<xsl:apply-templates select="CoupleSimpleValeurs"/>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
</xsl:template>

<xsl:template match="CoupleValeurs">
	<fo:table-cell border="0">
		<fo:block font-weight="bold">
			<xsl:value-of select="NomValeur"/>
		</fo:block>
		<fo:block font-weight="bold" color="#FF0000">
			<xsl:value-of select="NomValeurObligatoire"/>
		</fo:block>
	</fo:table-cell>
	<fo:table-cell border="0">
		<fo:block space-after="5pt" color="#003399">
			<xsl:value-of select="Valeur"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>

<xsl:template match="CoupleSimpleValeurs">
	<fo:table-cell border="0">
		<fo:block>
			<xsl:apply-templates select="NomValeur"/>
			<xsl:apply-templates select="NomValeurObligatoire"/>
			<xsl:apply-templates select="Valeur"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>

<xsl:template match="NomValeur">
	<fo:inline font-weight="bold" space-end="10pt">
		<xsl:value-of select="text()"/>
	</fo:inline>
</xsl:template>

<xsl:template match="NomValeurObligatoire">
	<fo:inline font-weight="bold" color="#FF0000">
		<xsl:value-of select="text()"/>
	</fo:inline>
</xsl:template>

<xsl:template match="Valeur">
	<fo:inline color="#003399">
		<xsl:value-of select="text()"/>
	</fo:inline>
</xsl:template>

<xsl:template match="Liste">
	<fo:block>
		<fo:block>
			<xsl:apply-templates select="TitreParagraphe"/>
		</fo:block>
		<fo:block>
			<fo:table width="100%" table-layout="fixed" font-size="7pt" 
				text-align="left">
				<xsl:apply-templates select="Colonnes"/>
				<xsl:apply-templates select="EnteteListe"/>
				<fo:table-body>
					<fo:table-row><fo:table-cell><fo:block/></fo:table-cell></fo:table-row>
					<xsl:apply-templates select="LigneListe"/>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</fo:block>
</xsl:template>

<xsl:template match="Colonnes">
	<xsl:apply-templates select="Colonne"/>
</xsl:template>

<xsl:template match="Colonne">
	<fo:table-column/>
</xsl:template>

<xsl:template match="EnteteListe">
	<fo:table-header>
		<fo:table-row>
			<xsl:apply-templates select="NomColonne"/>
		</fo:table-row>
	</fo:table-header>
</xsl:template>

<xsl:template match="NomColonne">
	<fo:table-cell border="1pt solid #FFFFFF" background-color="#1E62BB"
	padding="5pt">
		<fo:block font-weight="bold" color="#FFFFFF">
			<xsl:value-of select="text()"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>

<xsl:template match="LigneListe">
	<fo:table-row>
		<xsl:apply-templates select="Cellule"/>
	</fo:table-row>
</xsl:template>

<xsl:template match="Cellule">
	<fo:table-cell border="1pt solid #FFFFFF" background-color="#d2d8f3"
	padding="3pt">
		<fo:block><xsl:value-of select="text()"/></fo:block>
	</fo:table-cell>
</xsl:template>

</xsl:stylesheet>
