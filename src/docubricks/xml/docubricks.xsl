<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html lang="en">
	<head>
		<meta charset="UTF-8" />
		<title>Docubricks document</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
		<link href="css-js-dbricks/smart-grid.css" rel="stylesheet" />
		<link href="css-js-dbricks/dist/terrierX.css" rel="stylesheet" />
		<link href="css-js-dbricks/docubricks.css" rel="stylesheet" />

		<script src="css-js-dbricks/jquery-1.11.3.min.js"></script>
		<script src="css-js-dbricks/CollapsibleLists.js"></script>
		<script src="css-js-dbricks/docubricks.js"></script>


	</head>

	<!--  Original XML data goes in here  -->
	<hiddendata class="hideclass" id="hiddendata">
		<xsl:copy-of select="docubricks"/>
	</hiddendata>


	<!--  Bill of materials table for one brick-->
	<hidden class="hideclass">
		<div class="col12 colExample" id="brickbomtable">
			<div class="divbom">
				<p class="p_h2" id="brickbomname">
					Subcomponents
				</p>
			</div>
			<p align="center">
				<table width="100%">
					<thead>
						<tr>
							<th>#</th>
							<th>DESCRIPTION</th>
							<th>QUANTITY</th>
							<th>SUPPLIER</th>
						</tr>
					</thead>
					<tbody id="brickbombody">
					</tbody>
				</table>
			</p>
		</div>
		<tr id="brickbomrow">
			<td id="partnum"></td>
			<td><a id="description"> </a></td>
			<td id="quantity"></td>
			<td id="supplier"></td>
		</tr>
	</hidden>



	<!--  Bill of materials table for the total project -->
	<hidden class="hideclass">
		<div id="totalbomtable">
			<div class="project_title">
				<h1>
					Bill of materials
				</h1>
			</div>
			<div class="col12 colExample">
				<p align="center">
					<table width="100%">
						<thead>
							<tr>
								<th>#</th>
								<th>DESCRIPTION</th>
								<th>QUANTITY</th>
								<th>SUPPLIER</th>
							</tr>
						</thead>
						<tbody id="totalbombody">
						</tbody>
					</table>
				</p>
			</div>
		</div>
		<tr id="totalbomrow">
			<td id="partnum"></td>
			<td><a id="description"> </a></td>
			<td id="quantity"></td>
			<td id="supplier"></td>
		</tr>
	</hidden>


	<!--  Instruction table -->
	<hidden>
		<div class="col12 colExample" id="instructiontable">
			<div class="divbom">
				<p class="p_h2" id="instructionname"></p>
			</div>
		</div>
		<div id="instructionstep">
		</div>
	</hidden>


	<!-- The visible content -->
	<body>
		<!-- Left side tree -->
	<div id="content" class="container">

		<div class="row">
			<div id="ptree2" class="columns one-fourth"></div>
			<div id="ccentre" class="columns three-fourths offset-four"></div>
	</div>
</div>
	<script type="text/javascript">
		loadxml2();
	</script>
</body>



</html>
</xsl:template>
</xsl:stylesheet>
