<?php

/**
 * Departamento de Computación, CINVESTAV CDMX.
 *
 * Sistema informático financiado con el proyecto CONACyT - 313572
 *
 * Derechos de autor (c) 2020
 * Todos los derechos reservados.
 */

$correo = "";

if ($_POST){
	if (isset($_POST['correo'])){
		$correo = str_replace(array("\r", "\n", "%0a", "%0d"), '', $_POST['correo']);
		if (exec('grep '.escapeshellarg($correo).' correos.txt')){
			header("Location: mensaje.php?titulo=Ya%20te%20suscribiste&cuerpo=Ya%20tenemos%20registrado%20este%20correo");
			exit();
		}
		$correo = filter_var($correo, FILTER_VALIDATE_EMAIL);
		$fp = fopen("correos.txt", a);
		fwrite($fp, $correo."\n");
		fclose($fp);
		header("Location: mensaje.php?titulo=Registro%20exitoso&cuerpo=Gracias%20por%20suscribirte");
    }else{
		header("Location: mensaje.php?titulo=Hubo%20un%20problema&cuerpo=El%20correo%20proporcionado%20no%20es%20válido");
    }
	exit();
}

header("Location: ../index.html");
exit();

?>

