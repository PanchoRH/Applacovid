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


$campos = "t.cve_ent as cve, t.nom_ent as nombre, ST_AsGeoJSON(ST_Simplify(t.geom,0.01)) as shape, ";
$confirmados ="(select  sum(value::int)
from confirmados_estatal , json_each_text(row_to_json(confirmados_estatal)) with ordinality
where ordinality > 3 and confirmados_estatal.cve_ent = t.cve_ent ) as confirmados ";

$campos = $campos . $confirmados;



$sql = "SELECT $campos FROM $table t";


$result = pg_query($conn,$sql) or die('Consulta fallida: ' . pg_last_error());


$geojson = array(
    'type'      => 'FeatureCollection',
    'features'  => array()
 );


while($row = pg_fetch_array($result,null,PGSQL_ASSOC)){
	$feature = array(
		'type' => 'Feature',
		'geometry' => json_decode($row["shape"]),
		'properties' => array(
      			'cve' => $row["cve"],
            	'nombre' => $row["nombre"],
            	'confirmados' => $row["confirmados"]
            	)
	);
	array_push($geojson['features'], $feature);
}


unset($result);


header("Content-Type:application/json");
echo json_encode($geojson);
pg_free_result($result);
pg_close($conn);
?>
