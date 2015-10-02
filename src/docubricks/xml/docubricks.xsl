<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html lang="en">
	<head>
		<meta charset="UTF-8" />
		<title>Docubricks document</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
		
		<link href="terrier/dist/terrier.css" rel="stylesheet" />
		<link href="docubricks.css" rel="stylesheet" />
		
		<script src="terrier/jquery-1.11.3.min.js"></script>
		<script src="docubricks.js"></script>
	</head>

	<!--  Original XML data goes in here  -->
	<hiddendata class="hideclass" id="hiddendata">
		<xsl:copy-of select="docubricks"/>
	</hiddendata>
	
	
	<!--  Bill of materials table for one brick -->
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
	
	
	



	<!--  Bill of materials table for one step -->
	<hidden class="hideclass">
		<div id="stepbomtable">
			<div class="divbom">
				<p class="p_h2" id="stepbomname">
					Referenced components
				</p>
			</div>
			<p align="center">
				<table>
					<thead>
						<tr>
							<th>DESCRIPTION</th>
							<th>QUANTITY</th>
						</tr>
					</thead>
					<tbody id="stepbombody">
					</tbody>
				</table>
			</p>
		</div>
		<tr id="stepbomrow">
			<td><a id="description"> </a></td>
			<td id="quantity"></td>
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



	<!--  Left tree node -->
	<hidden>
		<div id="lefttreeinstance">
			<div>
				<a id="lefttreenode">
				
				</a>
			</div>
			<br clear="all"/>
			<div class="lefttreesubdiv" id="lefttreesubdiv">
			</div>
		</div>
	</hidden>



	<!-- The visible content -->
	<body>
		<!-- Left side tree -->
		<div id="ptree2" class="ptree2"></div>

		<!-- Main body -->
		<div id="ccentre" class="container center"></div>
		
		<script type="text/javascript">
			loadxml2();
		</script> 
	</body>
	
	
	
</html>
</xsl:template>
</xsl:stylesheet>
