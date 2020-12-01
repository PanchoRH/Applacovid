<?php

/**
 * Departamento de Computación, CINVESTAV CDMX.
 *
 * Sistema informático financiado con el proyecto CONACyT - 313572
 *
 * Derechos de autor (c) 2020
 * Todos los derechos reservados.
 */

$titulo = "";
$cuerpo = "";

if ($_GET){
	if (isset($_GET['titulo']) || isset($_GET['cuerpo'])){
		$titulo = str_replace(array("\r", "\n", "%0a", "%0d"), '', $_GET['titulo']);
		$cuerpo = str_replace(array("\r", "\n", "%0a", "%0d"), '', $_GET['cuerpo']);
	}else{
		header("Location: ../index.html");
		exit();
	}
}else{
	header("Location: ../index.html");
	exit();
}

?>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
<head>
<title>Applacovid</title>
<style type="text/css">

* {
	background-color: #ededed;
}

#container {
	display: flex;
	align-items: center;
	justify-content: center;
}

#box {
	border: solid 1px black;
	border-radius: 30px;
	margin-top: 10%;
	width: 400px;
	height: 200px;
}

#titulo {
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: 30px 30px 0 0;
    background-color: #9869a4;
	width: 100%;
	height: 50px;
}

#titulo h2 {
    color: white;
    background-color: #9869a4;
}

#cuerpo {
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: white;
	width: 100%;
	height: calc(100% - 100px);
}

#cuerpo p {
	background-color: white;
}

#pie {
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: white;
	border-radius: 0 0 30px 30px;
	width: 100%;
	height: 50px;
}

.btn-default {
        background-color: #9869a4;
        color: white;
	width: 20%;
	height: 30px;
}

.btn-default:hover {
        background-color: #4d486c;
        color: white;
}

</style>
<script type="text/javascript">
function goHome(){
	document.location = "../index.html";
}
</script>
</head>
<body>

<div id="container">
<div id="box">
	<div id="titulo"><h2><?php echo $titulo; ?></h2></div>
	<div id="cuerpo"><p><?php echo $cuerpo;?></p></div>
	<div id="pie">
		<button id="boton" class="btn btn-default" onclick="goHome();">Volver</button>
	</div>
</div>
</div>

</body>
</html>

<?php
exit();
?>


