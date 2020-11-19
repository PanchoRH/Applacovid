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

$campos = "lat, long, casos ";



$sql = "SELECT $campos FROM cabeceras ";
//$sql = "SELECT lat, long, casos FROM cabeceras ";


$result = pg_query($conn,$sql) or die('Consulta fallida: ' . pg_last_error());


/*if (!$result = pg_query($conn, $sql)) {
	echo "A ocurrido un error.\n";
	exit;
}*/

$casos = array();


while($row = pg_fetch_array($result,null,PGSQL_ASSOC)){
	$feature = array($row["lat"], $row["long"], $row["casos"]);
	array_push($casos, $feature);
}


unset($result);
//unset($row);

header("Content-Type:application/json");




//$casos[0][0] = floatval($casos[0][0]);
//$casos[0][1] = floatval($casos[0][1]);
//$casos[0][2] = floatval($casos[0][2]);
//echo json_encode($casos[0]);
echo json_encode($casos);
pg_free_result($result);
pg_close($conn);

?>
