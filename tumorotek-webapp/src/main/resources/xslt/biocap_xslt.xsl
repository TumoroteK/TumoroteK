<?xml version="1.0" encoding="UTF-8"?>
<?mso-application progid="Excel.Sheet"?>
<xsl:stylesheet version="1.0" 
	xmlns:html="http://www.w3.org/TR/REC-html40"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="urn:schemas-microsoft-com:office:spreadsheet" 
	xmlns:o="urn:schemas-microsoft-com:office:office" 
	xmlns:x="urn:schemas-microsoft-com:office:excel"
	xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
	<xsl:output method="xml" indent="yes"/>

	<xsl:template match="/">

		<Workbook>
			<Styles>
				<Style ss:ID="Default" ss:Name="Normal">
					<Alignment ss:Vertical="Bottom" />
					<Borders />
					<Font />
					<Interior />
					<NumberFormat />
					<Protection />
				</Style>
				<Style ss:ID="s21">
					<Font ss:Size="22" ss:Bold="1" />
				</Style>
				<Style ss:ID="s22">
					<Font ss:Size="14" ss:Bold="1" />
				</Style>
				<Style ss:ID="s23">
					<Font ss:Size="12" ss:Bold="1" />
				</Style>
				<Style ss:ID="s24">
					<Font ss:Size="10" ss:Bold="1" />
				</Style>
			</Styles>

			<Worksheet ss:Name="BioCAP">
				<Table><!-- ss:Color="red"  -->
					<Column ss:AutoFitWidth="1" ss:Width="85" />
					<Column ss:AutoFitWidth="1" ss:Width="115" />
					<Column ss:AutoFitWidth="1" ss:Width="115" />
					<Column ss:AutoFitWidth="0" ss:Width="85" />
					<Column ss:AutoFitWidth="0" ss:Width="115" />
					<Column ss:AutoFitWidth="0" ss:Width="85" />
					<Column ss:AutoFitWidth="0" ss:Width="85" />
					<Column ss:AutoFitWidth="0" ss:Width="160" />

					<Row ss:AutoFitHeight="0" ss:Height="15">
						<Cell ss:StyleID="s24">
							<Data ss:Type="String">Export Catalogue Biocap</Data>
						</Cell>
					</Row>
					<Row>
						<Cell>
							<Data ss:Type="String">
							</Data>
						</Cell>
					</Row>

					<xsl:call-template name="biocap" />

				</Table>
			</Worksheet>
		</Workbook>

	</xsl:template>



	<xsl:template name="biocap">

		<Row ss:AutoFitHeight="0" ss:Height="18">
			<Cell ss:StyleID="s23">
				<Data ss:Type="String">
				</Data>
			</Cell>
		</Row>
		<Row>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text disable-output-escaping="yes">ID site Biocap</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Site Soins</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Hôpital</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Service</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text disable-output-escaping="yes">ID Patient Biocap</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>ID Patient lab</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Nom</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Prénom</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Date naissance patient</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Sexe patient</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Statut juridique</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Statut vital</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Date du diagnostic</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Diagnostic principal de la tumeur initiale </xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Diagnostic principal</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Diagnostic secondaire</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Diagnostic tertiaire</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Rechute</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Essai clinique</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>ID prélèvement BIOCAP</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Etablissement de prelevement</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Centre de stockage</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>ID prélèvement</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Date prélèvement</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Date de réception</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Type de prélèvement</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Classification utilisée</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Code Organe CIM0/SNOMED/ADICAP</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Type lésionnel histopathologique CIM0/SNOMED/ADICAP</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Version de l'ADICAP</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Grade Tumoral</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Stade Tumoral</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Stade de la maladie</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Edition du pTNM</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>pT</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>pN</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>pM</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>ID Echantillon tumoral</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Echantillon tumoral</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Mode de conservation</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Type échantillon</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Mode de préparation</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Délai de congélation</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Date de congélation</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Contrôle sur tissu</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Précision sur Contrôle</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Nombre de tubes</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Pourcentage cellules tumorales</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>ADN dérivé</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Méthode d'extraction ADN</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>ARN dérivé</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Méthode d'extraction ARN</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Protéines dérivées</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Sérum</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Plasma</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Sang total</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Autres Liquides</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Autres Liquides nature</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>ADN constitutionnel</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>CR anatomopathologique standardisé interrogeable</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Données Cliniques disponibles dans une base</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Inclusion de la tumeur dans un protocole de recherche ?</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Sortie de l'échantillon</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Nombre de tubes sortis</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Date de sortie</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Lieu d'affectation</xsl:text>
				</Data>
			</Cell>
			<Cell ss:StyleID="s24">
				<Data ss:Type="String">
					<xsl:text>Sortie sanitaire ou recherche</xsl:text>
				</Data>
			</Cell>
		</Row>

		<xsl:for-each select="//results/row">

			<Row>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="SITE_FINESS" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="ETABLISSEMENT" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="SERVICE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="PATIENT_ID" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="NOM" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="PRENOM" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="DATE_NAISSANCE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="SEXE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="STATUT_JURIDIQUE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="PATIENT_ETAT" />
					</Data>
				</Cell>
				<!-- MALADIE -->
				<!-- MALADIE -->
				<!-- MALADIE -->
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="DATE_DIAGNOSTIC" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="LIBELLE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="CODE_MALADIE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="A6_1" />
					</Data>
				</Cell>
				<!-- PRELEVEMENT -->
				<!-- PRELEVEMENT -->
				<!-- PRELEVEMENT -->
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="ETABLISSEMENT" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="COLLECTION" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="PRELEVEMENT_ID" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="DATE_PRELEVEMENT" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="DATE_ARRIVEE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="PREL_TYPE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:text>AC</xsl:text>
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="CODE_ORGANE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="CODE_LESIONNEL" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<!-- ECH -->
				<!-- ECH -->
				<!-- ECH -->
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="ECHANTILLON_ID" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="boolean(TUMORAL)" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="CONTENEUR_TEMP" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="TYPE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="MODE_PREPARATION" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="DELAI_CGL" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="DATE_STOCKAGE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="boolean(CONFORME_TRAITEMENT)" />
						<xsl:text> / </xsl:text>
						<xsl:value-of select="boolean(CONFORME_CESSION)" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="ECHAN_QUALITE" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="ECHAN_TOTAL" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
					<xsl:if test="PROD_TYPE = 'ADN'">
						<xsl:text>OUI</xsl:text>
					</xsl:if>
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="MODE_EXTRACTION" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:if test="PROD_TYPE = 'ARN'">
						<xsl:text>OUI</xsl:text>
					</xsl:if>
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="MODE_EXTRACTION" />
					</Data>
				</Cell>
				<Cell>
					<xsl:if test="PROD_TYPE = 'PROTEINE'">
						<xsl:text>OUI</xsl:text>
					</xsl:if>
				</Cell>
				<!-- RESS BIO ASS -->
				<!-- RESS BIO ASS -->
				<!-- RESS BIO ASS -->
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<!-- COMPL -->
				<!-- COMPL -->
				<!-- COMPL -->
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="A20_1" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="STATUT" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">
						<xsl:value-of select="x" />
					</Data>
				</Cell>
			</Row>

		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>