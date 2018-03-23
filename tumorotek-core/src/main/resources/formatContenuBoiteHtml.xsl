<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="no" encoding="ISO-8859-1"/>
<xsl:template match="ImpressionContenu">
	<html>
		<body>
				<xsl:apply-templates select="PageContenuBoite"/>
		</body>
	</html>
</xsl:template>
<xsl:template match="PageBoite">
	<xsl:apply-templates select="Titre"/>
	<xsl:apply-templates select="Contenu"/>
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

<xsl:template match="Contenu">	
	<table cellspacing="0" cellpadding="0" width="700">
		<xsl:apply-templates select="ListeParents"/>
		<xsl:apply-templates select="Modelisation"/>
		<xsl:apply-templates select="ListeNombres"/>
	</table>
</xsl:template>

<xsl:template match="Modelisation">
	<tr height="20"><td></td></tr>
	<tr>
		<td>
		<table cellspacing="0" cellpadding="0">
			<xsl:apply-templates select="LigneCoordonnees"/>
			<xsl:apply-templates select="LigneBoite"/>
		</table>
		</td>
	</tr>
	<tr height="20"><td></td></tr>
</xsl:template>

<xsl:template match="LigneCoordonnees">
	<tr>
		<td>
			<table align="center" cellspacing="0" cellpadding="1" border="0pt solid #00227c">
				<tr height="30">
				</tr>
			</table>
		</td>
		<td>
			<table align="center" cellspacing="0" cellpadding="1" border="0pt solid #00227c">
				<tr height="30">
				<xsl:apply-templates select="Coordonnee"/>
				</tr>
			</table>
		</td>
	</tr>
</xsl:template>

<xsl:template match="LigneBoite">
	<tr>
		<td>
			<table align="center" cellspacing="0" cellpadding="1" border="0pt solid #00227c">
				<tr height="80">
				<xsl:apply-templates select="Coordonnee"/>
				</tr>
			</table>
		</td>
		<td>
			<table align="center" cellspacing="0" cellpadding="0" border="1pt solid #00227c">
				<tr height="80">
				<xsl:apply-templates select="Emplacement"/>
				</tr>
			</table>
		</td>
	</tr>
</xsl:template>

<xsl:template match="Emplacement">
	<td width="80" style="background-color:#d2d8f3;">
		<table width="80" height="80">
			<xsl:apply-templates select="EmpLibre"/>
			<xsl:apply-templates select="EmpOccupe"/>
		</table>
	</td>	
</xsl:template>

<xsl:template match="EmpLibre">
	<tr height="15">
		<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:6pt;" align="center" 
			width="15" valign="top">
			<xsl:value-of select="Position"/>
		</td>
		<td width="65"><div width="65"></div></td>
	</tr>
	<tr align="center">
		<th style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:6pt;" colspan="2"
			align="center" width="80">
			<xsl:value-of select="NomEmplacement"/>
		</th>
	</tr>
	<tr>
		<th style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:6pt;" colspan="2"
			align="center" width="80">
			<font color="#00227c">
				<xsl:value-of select="TypeEmplacement"/>
			</font>
		</th>
	</tr>
</xsl:template>

<xsl:template match="EmpOccupe">
	<tr height="15">
		<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:6pt;font-weight:bold;" align="center" 
			width="15" valign="top">
			<xsl:value-of select="Position"/>
		</td>
		<td width="65" align="right">
			<img>
				<xsl:attribute name="src">
					<xsl:apply-templates select="AdrImage"/>
				</xsl:attribute>
			</img>
		</td>
	</tr>
	<tr>
		<th style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:6pt;font-weight:normal;" colspan="2"
			align="center" width="80">
			<font color="#00227c">
				<xsl:value-of select="NomEmplacement"/>
			</font>
		</th>
	</tr>
	<tr>
		<th style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:6pt;font-weight:normal;" colspan="2"
			align="center" width="80">
			<font color="#00227c">
				<xsl:value-of select="TypeEmplacement"/>
			</font>
		</th>
	</tr>
</xsl:template>

<xsl:template match="ListeParents">
	<xsl:apply-templates select="Parent"/>
</xsl:template>

<xsl:template match="Parent">
	<tr>
		<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:9pt;">
			<xsl:value-of select="text()"/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="ListeNombres">
	<xsl:apply-templates select="Nombre"/>
</xsl:template>

<xsl:template match="Nombre">
	<tr>
		<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
			font-size:9pt;">
			<xsl:value-of select="text()"/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="Coordonnee">
	<td width="80">
		<table width="80">
			<tr>
			<td style="font-family:Verdana, Arial, Helvetica, sans-serif;
				font-size:11pt; font-weight:bold;" 
				align="center" width="80">
					<xsl:value-of select="text()"/>
			</td>
		</tr>
		</table>
	</td>	
</xsl:template>

<xsl:template match="AdrImage">
	<xsl:value-of select="text()"/>
</xsl:template>

</xsl:stylesheet>