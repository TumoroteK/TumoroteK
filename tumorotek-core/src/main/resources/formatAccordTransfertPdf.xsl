<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:template match="AccordTransfert">
<fo:root>
	<fo:layout-master-set>
		<!--description de la mise en page-->
		<fo:simple-page-master master-name="simple" page-height="29.7cm" 
												page-width="21cm" 
												margin-top="1cm" 
												margin-bottom="0.5cm" 
												margin-left="1.5cm" 
												margin-right="1.5cm"
												font-family="Verdana" font-size="12pt" initial-page-number="1">
			<fo:region-body margin-top="4cm" margin-bottom="2cm"/>
			<fo:region-before extent="1cm"/>
			<fo:region-after extent="1cm"/>
		</fo:simple-page-master>
	</fo:layout-master-set>
	<xsl:apply-templates select="Page"/>
</fo:root>
</xsl:template>

<xsl:template match="Page">	
	<fo:page-sequence master-reference="simple" keep-with-next.within-page="always">
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
			<xsl:apply-templates select="Tableau"/>
			<xsl:apply-templates select="Signatures"/>
		</fo:flow>
	</fo:page-sequence>
</xsl:template>

<!-- haut de page -->
<xsl:template match="HautDePage">
	<fo:block text-align="left" font-size="10pt"
			font-style="italic">
		<xsl:value-of select="text()"/>
	</fo:block>
</xsl:template>

<!-- Pied de page -->
<xsl:template match="BasDePage">	
	<fo:table width="100%" table-layout="fixed" font-size="10pt">
		<fo:table-column border="0"/>
		<fo:table-column border="0"/>
		<fo:table-body>
			<fo:table-row>
				<fo:table-cell border="0">
					<fo:block font-size="10pt" text-align="left"
						font-style="italic">
						<xsl:value-of select="text()"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell border="0">
					<fo:block font-size="10pt" text-align="right"
						font-weight="bold">
						Page <fo:page-number />
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body>
	</fo:table>
</xsl:template>

<xsl:template match="Titre">	
	<fo:block text-align="left" space-after.optimum="30pt"
				font-size="12pt" font-weight="bold"
				text-decoration="underline">
		<xsl:value-of select="text()"/>
	</fo:block>
</xsl:template>

<xsl:template match="Tableau">
	<fo:block keep-together.within-page="always">
		<fo:table table-layout="fixed" keep-with-next.within-page="always"
		border="1pt solid black">
		<fo:table-column column-width="4cm"/>
		<fo:table-column column-width="14cm"/>
		<fo:table-body>
		<xsl:apply-templates select="BlocPrincipal"/>
		</fo:table-body>
		</fo:table>
	</fo:block>
</xsl:template>

<xsl:template match="BlocPrincipal">	
	<fo:table-row border="1pt solid black">
		<fo:table-cell border="1pt solid black">
			<fo:block text-align="center" 
					font-weight="bold" font-size="11pt" 
					padding-before="0.2cm">
				<xsl:apply-templates select="NomBloc"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<xsl:apply-templates select="LigneAccord"/>
		</fo:table-cell>
	</fo:table-row>
</xsl:template>

<xsl:template match="NomBloc">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="LigneAccord">
	<!-- Inner table -->
	<fo:table table-layout="fixed" keep-with-next.within-page="always">

		<fo:table-column column-width="5cm"/>
		<fo:table-column column-width="9cm"/>
		<fo:table-body>
			<xsl:apply-templates select="CoupleAccordValeur"/>
		</fo:table-body>
	</fo:table>
</xsl:template>

<xsl:template match="CoupleAccordValeur">
	<fo:table-row>
		<fo:table-cell margin-left="0.2cm" 
			padding-before="0.2cm" padding-after="0.1cm" border="1pt solid black">
			<fo:block font-size="10pt">
				<xsl:apply-templates select="NomValeur"/>
			</fo:block>
		</fo:table-cell>
		
		<xsl:apply-templates select="Valeurs"/>
		
	</fo:table-row>
</xsl:template>

<xsl:template match="NomValeur">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="Valeurs">
	<fo:table-cell margin-left="0.2cm" 
			padding-before="0.2cm" padding-after="0.2cm" border="1pt solid black">
		
	<xsl:apply-templates select="Valeur"/>
	</fo:table-cell>
</xsl:template>


<xsl:template match="Valeur">
	<fo:block font-size="10pt">
		<xsl:value-of select="text()"/>
	</fo:block>
</xsl:template>

<xsl:template match="Signatures">
	<fo:block keep-together.within-page="always" space-before.optimum="30pt">
		<fo:table table-layout="fixed" keep-with-next.within-page="always">
		<fo:table-column column-width="7cm"/>
		<fo:table-column column-width="4cm"/>
		<fo:table-column column-width="7cm"/>
		<fo:table-body>
			<xsl:apply-templates select="Entete"/>
			<xsl:apply-templates select="ListeSignature"/>
		</fo:table-body>
		</fo:table>
	</fo:block>
</xsl:template>

<xsl:template match="Entete">	
	<fo:table-row>
		<fo:table-cell>
			<fo:block text-align="left" 
					font-weight="bold" font-size="11pt" 
					padding-before="0.2cm">
				<xsl:apply-templates select="Titre1"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<fo:block>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<fo:block text-align="left" 
					font-weight="bold" font-size="11pt" 
					padding-before="0.2cm">
				<xsl:apply-templates select="Titre2"/>
			</fo:block>
		</fo:table-cell>
	</fo:table-row>
</xsl:template>

<xsl:template match="ListeSignature">
	<xsl:apply-templates select="ValeursSignatures"/>
</xsl:template>

<xsl:template match="ValeursSignatures">	
	<fo:table-row space-before.optimum="30pt">
		<fo:table-cell>
			<fo:block text-align="left" 
					font-size="10pt" 
					padding-before="15pt" padding-bottom="30pt">
				<xsl:apply-templates select="Valeur1"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<fo:block>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<fo:block text-align="left" 
					font-size="10pt" 
					padding-before="15pt" padding-bottom="30pt">
				<xsl:apply-templates select="Valeur2"/>
			</fo:block>
		</fo:table-cell>
	</fo:table-row>
</xsl:template>

<xsl:template match="Titre1">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="Titre2">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="Valeur1">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="Valeur2">
	<xsl:value-of select="text()"/>
</xsl:template>

</xsl:stylesheet>
