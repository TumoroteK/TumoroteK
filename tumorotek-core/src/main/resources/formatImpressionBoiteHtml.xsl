<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="no" encoding="ISO-8859-1"/>
<xsl:template match="ImpressionBoite">
	<html>
		<body>
				<xsl:apply-templates select="PageBoite"/>
		</body>
	</html>
</xsl:template>
<xsl:template match="PageBoite">
	<xsl:apply-templates select="Titre"/>
	<xsl:apply-templates select="Boite"/>
</xsl:template>

<xsl:template match="Titre">	
	<p style="border-bottom : 1pt solid #E03F06; 
	font-family:Verdana, Arial, Helvetica, sans-serif;
	width:600px;font-size:12pt;margin-bottom:20px;
	background-color:#ecf4fb; text-align: center;"
	width="300px">
		<font color="#00227c">
		<b><xsl:value-of select="text()"/></b>
	</font></p>
</xsl:template>

<xsl:template match="TitreIntermediaire">	
	<p style="border-bottom : 1pt solid #00227c; 
	max-width:400px;
	font-family:Verdana, Arial, Helvetica, sans-serif;
	font-size:10pt; margin-bottom:10;margin-top:30px;">
		<font color="#00227c">
		<b><xsl:value-of select="text()"/></b>
	</font></p>
</xsl:template>

<xsl:template match="Boite">
	<xsl:apply-templates select="Separateur"/>
	<xsl:apply-templates select="TitreIntermediaire"/>
	<table>
		<th style="border:1pt solid #00227c; background-color:#1E62BB;
			font-weight:bold; color:#FFFFFF;
			font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:9pt;"
			width="300px"
			padding="5">
			<xsl:apply-templates select="TitreModelisation"/>
		</th>
		<th style="border:1pt solid #00227c; background-color:#1E62BB;
			font-weight:bold; color:#FFFFFF;
			font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:9pt;"
			width="300px"
			padding="5">
			<xsl:apply-templates select="TitreInstruction"/>
		</th>
		<tr>
			<td style="background-color:#d2d8f3;" align="center">
				<xsl:apply-templates select="Modelisation"/>
			</td>
			<td valign="top" style="background-color:#d2d8f3;">
				<table>
					<xsl:apply-templates select="Instructions"/>
					<xsl:apply-templates select="ListeElements"/>
				</table>
			</td>
		</tr>
	</table>
</xsl:template>

<xsl:template match="TitreModelisation">
		<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="TitreInstruction">
		<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="Separateur">
	<p style="border-bottom : 1pt solid #00227c; 
	max-width:600px;
	font-family:Verdana, Arial, Helvetica, sans-serif;
	font-size:10pt; margin-bottom:30;margin-top:30px;">
	</p>
</xsl:template>

<xsl:template match="Modelisation">
	<xsl:apply-templates select="TitreBoite"/>
	<table align="center" cellspacing="0" cellpadding="0">
		<xsl:apply-templates select="LigneCoordonnees"/>
		<xsl:apply-templates select="LigneBoite"/>
	</table>
	<table width="300px">
		<tr><td height="10"></td></tr>
		<tr>
			<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			background-color:#d2d8f3;
			font-size:9pt;" align="left">
				<img>
					<xsl:attribute name="src">
						<xsl:apply-templates select="ImageLegendeVide"/>
					</xsl:attribute>
				</img>
				<xsl:apply-templates select="LegendeVide"/>
			</td>
		</tr>
		<tr>
			<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			background-color:#d2d8f3;
			font-size:9pt;" align="left">
				<img>
					<xsl:attribute name="src">
						<xsl:apply-templates select="ImageLegendePris"/>
					</xsl:attribute>
				</img>
				<xsl:apply-templates select="LegendePris"/>
			</td>
		</tr>
		<tr>
			<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			background-color:#d2d8f3;
			font-size:9pt;" align="left">
				<xsl:apply-templates select="LegendeSelectionne"/>
			</td>
		</tr>
	</table>
</xsl:template>

<xsl:template match="ImageLegendeVide">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="LegendeVide">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="ImageLegendePris">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="LegendePris">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="LegendeSelectionne">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="TitreBoite">	
	<p style="font-family:Verdana, Arial, Helvetica, sans-serif;
		font-size:9pt; margin-bottom:0;margin-top:0;
		text-align: center;">
		<xsl:value-of select="text()"/>
	</p>
</xsl:template>

<xsl:template match="LigneBoite">
	<tr>
		<td>
			<table align="center" cellspacing="0" cellpadding="0" border="0pt solid #00227c">
				<tr>
					<xsl:apply-templates select="Coordonnee"/>
				</tr>
			</table>
		</td>
		<td>
			<table align="center" cellspacing="0" cellpadding="1" border="1pt solid #00227c">
				<tr>
					<xsl:apply-templates select="Emplacement"/>
				</tr>
			</table>
		</td>
	</tr>
</xsl:template>

<xsl:template match="LigneCoordonnees">
	<tr>
		<td>
			<table align="center" cellspacing="0" cellpadding="2" border="0pt solid #00227c">
				<tr height="16">
				</tr>
			</table>
		</td>
		<td>
			<table align="center" valign="bottom" cellspacing="0" cellpadding="2" border="0pt solid #00227c">
				<tr height="16">
					<xsl:apply-templates select="Coordonnee"/>
				</tr>
			</table>
		</td>
	</tr>
</xsl:template>

<xsl:template match="Emplacement">
	<xsl:apply-templates select="EmpLibre"/>
	<xsl:apply-templates select="EmpOccupe"/>
	<xsl:apply-templates select="EmpSelectionne"/>
</xsl:template>

<xsl:template match="EmpLibre">
	<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
		background-color:#d2d8f3;" align="center" cellspacing="0">
		<img>
			<xsl:attribute name="src">
				<xsl:apply-templates select="AdrImage"/>
			</xsl:attribute>
		</img>
	</td>
</xsl:template>

<xsl:template match="EmpOccupe">
	<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
		background-color:#d2d8f3;" align="center">
		<img>
			<xsl:attribute name="title">
				<xsl:value-of select="text()"/>
			</xsl:attribute>
			<xsl:attribute name="src">
				<xsl:apply-templates select="AdrImage"/>
			</xsl:attribute>
		</img>
	</td>
</xsl:template>

<xsl:template match="AdrImage">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="EmpSelectionne">
	<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
		background-color:#d2d8f3;border=1pt solid #00227c;
		font-size:9pt;" align="center" width="16px">
		<xsl:value-of select="text()"/>
	</td>
</xsl:template>

<xsl:template match="Instructions">
	<xsl:apply-templates select="Instruction"/>
</xsl:template>

<xsl:template match="Instruction">
	<tr>
		<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:9pt;" align="center">
			<xsl:value-of select="text()"/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="ListeElements">
	<xsl:apply-templates select="TitreListe"/>
	<xsl:apply-templates select="Element"/>
</xsl:template>

<xsl:template match="TitreListe">
	<tr>
		<td style="border:1pt solid #00227c; background-color:#1E62BB;
			font-weight:bold; color:#FFFFFF;
			font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:9pt;text-align: center;">
			<xsl:value-of select="text()"/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="Element">
	<tr>
		<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:9pt;"
			width="300px;"
			align="center">
			<xsl:value-of select="text()"/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="Coordonnee">
	<td width="16px">
		<table width="16px">
			<tr>
			<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
				background-color:#d2d8f3;border=1pt solid #00227c;
				font-size:9pt; font-weight:bold;" 
				align="center" width="16px">
					<xsl:value-of select="text()"/>
			</td>
		</tr>
		</table>
	</td>	
</xsl:template>

</xsl:stylesheet>