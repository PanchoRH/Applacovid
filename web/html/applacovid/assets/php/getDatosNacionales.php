<?php

/**
 * Departamento de Computación, CINVESTAV CDMX.
 *
 * Sistema informático financiado con el proyecto CONACyT - 313572
 *
 * Derechos de autor (c) 2020
 * Todos los derechos reservados.
 */

ini_set('memory_limit', '-1'); 

//database login info
$host = "localhost";
$port = "5432";
$dbname = "applacovid";
$user = "postgres";
$password = "l1t7leGre3nB@g";

$conn = pg_connect("host=$host port=$port dbname=$dbname user=$user password=$password");
if (!$conn) {
	echo "Not connected : " . pg_error();
	exit;
}

$table = $_GET['table'];

$campos = "cve_ent as cve, ";
$confirmados ="(select  sum(value::int)
from confirmados_estatal , json_each_text(row_to_json(confirmados_estatal)) with ordinality
where ordinality > 3 and confirmados_estatal.cve_ent = '000' ) as confirmados, ";
$negativos = "(select  sum(value::int)
from negativos_estatal , json_each_text(row_to_json(negativos_estatal)) with ordinality
where ordinality > 3 and negativos_estatal.cve_ent = '000' ) as negativos, ";
$sospechosos ="(select  sum(value::int)
from sospechosos_estatal , json_each_text(row_to_json(sospechosos_estatal)) with ordinality
where ordinality > 3 and sospechosos_estatal.cve_ent = '000' ) as sospechosos, ";
$defunciones = "(select  sum(value::int)
from defunciones_estatal , json_each_text(row_to_json(defunciones_estatal)) with ordinality
where ordinality > 3 and defunciones_estatal.cve_ent = '000' ) as defunciones ";

$campos =  $campos . $confirmados . $negativos . $sospechosos . $defunciones;


$sql = "SELECT $campos FROM $table where cve_ent='000'";
$result = pg_query($conn,$sql) or die('Consulta fallida: ' . pg_last_error());


while($row = pg_fetch_array($result,null,PGSQL_ASSOC)){
	$resultados = array(
      			'cve' => $row["cve"],
            	'confirmados' => $row["confirmados"],
            	'negativos' => $row["negativos"],
            	'sospechosos' => $row["sospechosos"],
            	'defunciones' => $row["defunciones"]
            	);
}

unset($result);

//header("Content-Type:application/json");
echo "<p><b> Confirmados: </b>". $resultados['confirmados']."  <b>Negativos: </b>".$resultados['negativos']." <b>Sospechosos: </b>".$resultados['sospechosos']." <b>Defunciones: </b>".$resultados['defunciones']." </p>";
pg_free_result($result);
pg_close($conn);

?>
