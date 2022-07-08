<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="no" encoding="ISO-8859-1" />
	<xsl:template match="Impression">
		<html>
			<body>
				<xsl:apply-templates select="Page" />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="Page">
		<xsl:apply-templates select="Titre" />
		<xsl:apply-templates select="Paragraphe" />
		<xsl:apply-templates select="Liste" />
	</xsl:template>

	<xsl:template match="Titre">
		<p
			style=" 
	font-family:Verdana, Arial, Helvetica, sans-serif;
	font-size:14pt;margin-bottom:5px;">
			<font color="#00227c">
				<b>
					<xsl:value-of select="text()" />
				</b>
			</font>
		</p>
	</xsl:template>

	<xsl:template match="Paragraphe">
		<xsl:apply-templates select="TitreParagraphe" />
		<xsl:apply-templates select="Inconnu" />
		<table>
			<tr>
				<td>
					<xsl:apply-templates select="Ligne" />
				</td>
			</tr>
		</table>
		<xsl:apply-templates select="SousParagraphe" />
		<xsl:apply-templates select="Liste" />
	</xsl:template>

	<xsl:template match="SousParagraphe">
		<xsl:apply-templates select="TitreSousParagraphe" />
		<xsl:apply-templates select="Inconnu" />
		<table>
			<tr>
				<td>
					<xsl:apply-templates select="Ligne" />
				</td>
			</tr>
		</table>
		<xsl:apply-templates select="Liste" />
	</xsl:template>

	<xsl:template match="TitreParagraphe">
		<p
			style="border-bottom : 1pt solid #E03F06; 
	font-family:Verdana, Arial, Helvetica, sans-serif;
	max-width:600px;font-size:12pt;margin-bottom:5px;
	background-color:#ecf4fb;">
			<font color="#00227c">
				<b>
					<xsl:value-of select="text()" />
				</b>
			</font>
		</p>
	</xsl:template>

	<xsl:template match="TitreSousParagraphe">
		<p
			style="border-bottom : 1pt solid #00227c; 
	max-width:400px;
	font-family:Verdana, Arial, Helvetica, sans-serif;
	font-size:10pt; margin-bottom:0;margin-top:0;">
			<font color="#00227c">
				<b>
					<xsl:value-of select="text()" />
				</b>
			</font>
		</p>
	</xsl:template>

	<xsl:template match="Inconnu">
		<p
			style="font-family:Verdana, Arial, Helvetica, sans-serif;
		font-size:9pt; margin-bottom:0;margin-top:0;">
			<i>
				<xsl:value-of select="text()" />
			</i>
		</p>
	</xsl:template>

	<xsl:template match="Ligne">
		<xsl:apply-templates select="LigneParagraphe" />
		<xsl:apply-templates
			select="LigneDeuxColonnesParagraphe" />
		<xsl:apply-templates
			select="LigneSimpleParagraphe" />
	</xsl:template>

	<xsl:template match="LigneParagraphe">
		<table>
			<th width="150px" />
			<th width="150px" />
			<th width="150px" />
			<th width="150px" />
			<tr>
				<xsl:apply-templates select="CoupleValeurs" />
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="LigneDeuxColonnesParagraphe">
		<table>
			<th width="150px" />
			<th width="450px" />
			<tr>
				<xsl:apply-templates select="CoupleValeurs" />
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="LigneSimpleParagraphe">
		<table>
			<th width="600px" />
			<tr>
				<xsl:apply-templates
					select="CoupleSimpleValeurs" />
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="CoupleValeurs">
		<td
			style="font-family:Verdana, Arial, Helvetica, sans-serif;
		font-size:9pt;color=#003399;">
			<b>
				<xsl:value-of select="NomValeur" />
			</b>
			<b>
				<xsl:value-of select="NomValeurObligatoire" />
			</b>
			&#160;
		</td>
		<td
			style="font-family:Verdana, Arial, Helvetica, sans-serif;
		font-size:9pt;">
			<xsl:value-of select="Valeur" />
			<xsl:apply-templates select="ValeurAnonyme" />
		</td>
	</xsl:template>

	<xsl:template match="CoupleSimpleValeurs">
		<td
			style="font-family:Verdana, Arial, Helvetica, sans-serif;
		font-size:9pt;color=#003399;">
			<b>
				<xsl:value-of select="NomValeur" />
			</b>
			<b>
				<xsl:value-of select="NomValeurObligatoire" />
			</b>
			&#160;
			<xsl:value-of select="Valeur" />
			<xsl:apply-templates select="ValeurAnonyme" />
		</td>
	</xsl:template>

	<xsl:template match="ValeurAnonyme">
		<div style="background-color : #aeb4c6;" width="50px">
			<xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text>
		</div>
	</xsl:template>

	<xsl:template match="Liste">
		<xsl:apply-templates select="TitreParagraphe" />
		<table cellspacing="0" cellpadding="4">
			<xsl:apply-templates select="EnteteListe" />
			<xsl:apply-templates select="LigneListe" />
		</table>
	</xsl:template>

	<xsl:template match="EnteteListe">
		<xsl:apply-templates select="NomColonne" />
	</xsl:template>

	<xsl:template match="NomColonne">
		<th
			style="border:1pt solid #00227c; background-color:#1E62BB;
		font-weight:bold; color:#FFFFFF;
		font-family:Verdana, Arial, Helvetica, sans-serif;
		font-size:9pt;"
			padding="5">
			<xsl:value-of select="text()" />
		</th>
	</xsl:template>

	<xsl:template match="LigneListe">
		<tr>
			<xsl:apply-templates select="Cellule" />
		</tr>
	</xsl:template>

	<xsl:template match="Cellule">
		<td
			style="font-family:Verdana, Arial, Helvetica, sans-serif;
		border:1pt solid #00227c; background-color:#d2d8f3;
		font-size:9pt;">
			<xsl:value-of select="text()" />
		</td>
	</xsl:template>

</xsl:stylesheet>