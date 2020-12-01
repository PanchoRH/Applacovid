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

/*
select t.cve_ent, t.nom_ent, ST_AsGeoJSON(st_simplify(t.geom,0.01)) as shape, (select  sum(value::int)
from confirmados_estatal , json_each_text(row_to_json(confirmados_estatal)) with ordinality
where ordinality > 3 and confirmados_estatal.cve_ent = t.cve_ent ) as confirmados  from estados t; */

$campos = "t.cve_ent as cve, t.nom_ent as nombre, ST_AsGeoJSON(st_simplify(t.geom,0.01)) as shape, ";
$confirmados ="(select  sum(value::int)
from confirmados_estatal , json_each_text(row_to_json(confirmados_estatal)) with ordinality
where ordinality > 3 and confirmados_estatal.cve_ent = t.cve_ent ) as confirmados, ";
$negativos = "(select  sum(value::int)
from negativos_estatal , json_each_text(row_to_json(negativos_estatal)) with ordinality
where ordinality > 3 and negativos_estatal.cve_ent = t.cve_ent ) as negativos, ";
$sospechosos ="(select  sum(value::int)
from sospechosos_estatal , json_each_text(row_to_json(sospechosos_estatal)) with ordinality
where ordinality > 3 and sospechosos_estatal.cve_ent = t.cve_ent ) as sospechosos, ";
$defunciones = "(select  sum(value::int)
from defunciones_estatal , json_each_text(row_to_json(defunciones_estatal)) with ordinality
where ordinality > 3 and defunciones_estatal.cve_ent = t.cve_ent ) as defunciones ";

$campos = $campos . $confirmados . $negativos . $sospechosos . $defunciones;


//$fields = $_GET['fields'];

//turn fields array into formatted string
//$fieldstr = "t.cve_ent as cve, t.nom_ent as nombre, ";
/*foreach ($fields as $i => $field){
	$fieldstr = $fieldstr . "l.$field, ";
}*/

//$fieldstr = $fieldstr . "ST_AsGeoJSON(st_simplify(t.geom,0.01)) as shape";


//create basic sql statement

$sql = "SELECT $campos FROM $table t";
//$sql = "SELECT t.cve_ent as cve, t.nom_ent as nombre, ST_AsGeoJSON(t.geom) as shape from estados t";

//join for spatial query - table geom is in EPSG:26916
//$sql = $sql . " LEFT JOIN $table r ON ST_DWithin(l.geom, r.geom, $distance) WHERE r.featname = '$featname';";

// echo $sql;

$result = pg_query($conn,$sql) or die('Consulta fallida: ' . pg_last_error());


/*if (!$result = pg_query($conn, $sql)) {
	echo "A ocurrido un error.\n";
	exit;
}*/

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
//unset($row);

header("Content-Type:application/json");
echo json_encode($geojson);
pg_free_result($result);
pg_close($conn);


//echo json_encode($geojson,JSON_NUMERIC_CHECK);

	


//send the query





//echo the data back to the DOM
/*while ($row = pg_fetch_row($response)) {
	foreach ($row as $i => $attr){
		echo $attr.", ";
	}
	echo ";";
}*/

?>
