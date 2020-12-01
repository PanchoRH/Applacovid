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

//$tipoCasos = $_GET['tipoCasos'];
$table = $_GET['table'];

/*select t.nom_mun, ST_AsGeoJSON(st_simplify(t.geom,0.03)) as shape, r.cve_ent, r.confirmados  from municipios t, (select  cve_ent, sum(value::int) as confirmados
from confirmados_municipal , json_each_text(row_to_json(confirmados_municipal)) with ordinality
where ordinality > 3 group by 1 order by 1) as r where t.cvegeo=r.cve_ent; */


$campos = "nom_mun as nombre, ST_AsGeoJSON(geom) as shape, cvegeo as cve,  confirmados, negativos, sospechosos, defunciones ";


//$campos = "t.nom_mun as nombre, ST_AsGeoJSON(t.geom) as shape,  c.cve_ent as cve, c.sumacasos";
//$desde = "municipios t, (select cve_ent, sum(value::int) as sumacasos from ".$table.", json_each_text(row_to_json(".$table.")) with ordinality where ordinality > 3 group by 1 order by 1) as c where t.cvegeo=c.cve_ent";

$sql = "SELECT $campos FROM $table";
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
            	'confirmados' => $row["confirmados"],
            	'negativos' => $row["negativos"],
            	'sospechosos' => $row["sospechosos"],
            	'defunciones' => $row["defunciones"]
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
