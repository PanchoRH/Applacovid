/**
 * Departamento de Computación, CINVESTAV CDMX.
 *
 * Sistema informático financiado con el proyecto CONACyT - 313572
 *
 * Derechos de autor (c) 2020
 * Todos los derechos reservados.
 */

function send(){
	var correo = document.myform.correo.value;
	if (correo == ""){
		alert("Debes escribir una dirección de correo");
	}else{
		if (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(correo)){
			document.forms["suscribete"].submit();
		}else{
			alert("ERROR: La dirección de correo no es válida");
		}
	}
}


var header = `
<div class="col-lg-1 col-md-1 col-sm-1"></div>
	<nav id="botonera" class="col-lg-10 col-md-10 col-sm-10 navbar navbar-expand-xl navbar-expand-lg navbar-light">
	<a class="navbar-brand lm-auto" href="index.html">
<div id="logo">
<img src="img/LOGO_APP_COLOR.svg" class="img-fluid">
</div>
	</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse"
data-target="#navbarContent" aria-controls="navbarContent"
aria-expanded="false" aria-label="Toggle navigation">
	<span class="navbar-toggler-icon"></span></button>
	<div class="collapse navbar-collapse" id="navbarContent">
	<ul class="navbar-nav">
		<li class="nav-item active">
                <a class="nav-link" href="index.html#titulo0">&iquest;Qu&eacute; es Applacovid?</a></li>
                <li class="nav-item">
                <a class="nav-link" href="index.html#titulo1">&iquest;C&oacute;mo funciona?</a></li>
                <li class="nav-item">
                <a class="nav-link" href="https://pakal.cs.cinvestav.mx/applacovid/">Mapa</a></li>
                <li class="nav-item">
                <a class="nav-link" href="noticias.html">Noticias</a></li>
                <li class="nav-item">
                <a class="nav-link" href="faqs.html">FAQs</a></li>
	</ul>

	<div id="social" class="ml-auto">
	<ol class="d-flex justify-content-center">
		<a href="https://www.facebook.com/Departamento-de-Computaci%C3%B3n-Cinvestav-IPN-154959587937045/" target="_blank">
			<li id="face-icon"></li>
		</a>
		<a href="https://twitter.com/CinvestavComp" target="_blank">
			<li id="twitter-icon"></li>
		</a>
		<a href="https://www.youtube.com/channel/UCXlDAr9iAxGWOLheU2eS2OA" target="_blank">
			<li id="youtube-icon"></li>
		</a>
	</ol>
	</div>
	</div>
</nav>
<div class="col-lg-1 col-md-1 col-sm-1"></div>
`

var footer = `
<div class="espacio-mini"></div>
	<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
		<img src="img/LOGO_APP_BLANCO.svg" id="logo-blanco" class="img-fluid">
	</div>
<div class="espacio-mini"></div>

	<div class="footer-ligas d-flex col-lg-2 col-md-2 col-sm-6">
		<div id="divisor1"></div>
		<ul>
		<li><a href="index.html#titulo0">
			&iquest;Qu&eacute; es Applacovid?
		</a></li>
		<li><a href="index.html#titulo1">
			&iquest;C&oacute;mo funciona?
		</a></li>
		<li><a href="https://pakal.cs.cinvestav.mx/applacovid/">
			Mapa
		</a></li>
		</ul>
	</div>
	<div class="footer-ligas d-flex col-lg-2 col-md-2 col-sm-6">
		<ul>
		<li><a href="noticias.html">
			Noticias
		</a></li>
		<li><a href="faqs.html">
			FAQs
		</a></li>
		<li><a href="privacidad.html">
			Aviso de privacidad
		</a>
		</li>
		</ul>
	</div>

<div class="espacio-mini"></div>

	<div class="d-flex align-items-center col-lg-3 col-md-3 col-sm-6 col-xs-12">
		<div id="divisor2"></div>
	<div class="hide-small-size w-100 col-lg-4 col-md-4 col-sm-4 col-xs-4">
		<p>S&iacute;guenos</p>
	</div>
	<div id="social"  class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
	<div class="show-small-size col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<p>S&iacute;guenos</p>
	</div>
		<ol class="d-flex justify-content-center">
		<a href="mailto:applacovid@gmail.com">
		<li id="email-icon"></li>
		</a>
		<a href="https://www.facebook.com/Departamento-de-Computaci%C3%B3n-Cinvestav-IPN-154959587937045/" target="_blank">
			<li id="face-icon"></li>
		</a>
		<a href="https://twitter.com/CinvestavComp" target="_blank">
			<li id="twitter-icon"></li>
		</a>
		<a href="https://www.youtube.com/channel/UCXlDAr9iAxGWOLheU2eS2OA" target="_blank">
			<li id="youtube-icon"></li>
		</a>
		</ol>
	</div>
	</div>

	<div class="d-flex col-lg-3 col-md-3 col-sm-6 col-xs-12">
		<div id="divisor3"></div>
		<div>
<div id="logos-footer">
<img src="img/logos-footer.png" class="img-fluid" style="max-height: 150px;">
<p style="margin: 0 10px;">Proyecto apoyado por CONACyT</p>
</div>

<!--	Descartado RSS	-->
<!--
<p>
Suscr&iacute;bete
<div id="rss-button" onclick="document.location='feed.xml'"></div>
</p>
-->

<!--	Descartado PHP	-->
<!--
<form id="suscribete" name="myform" role="form" action="suscribete/suscribete.php" method="post">
<div class="form-group">
<label>Suscr&iacute;bete</label>
<input name="correo" type="email" class="form-control" placeholder="Ingresa tu correo" required>
<div id="submit-email" onclick="send();"></div>
</div>
</form>
-->

		</div>
	</div>
<div class="espacio-mini"></div>
<div id="copydiv" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">&copy; CINVESTAV 2020</div>
`




