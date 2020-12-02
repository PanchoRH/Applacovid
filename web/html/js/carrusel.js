/**
 * Departamento de Computación, CINVESTAV CDMX.
 *
 * Sistema informático financiado con el proyecto CONACyT - 313572
 *
 * Derechos de autor (c) 2020
 * Todos los derechos reservados.
 */

var k = banners.length;

var carrusel = `
	<!-- Indicadores -->
	<ol class="carousel-indicators">
	<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
`;

for (var i = 1; i < k; i++) {
	carrusel += "<li data-target='#myCarousel' data-slide-to='"+i+"'></li>";
}

carrusel +=`
	</ol>
	<div class="carousel-inner">
`;

for (var i = 0; i < k; i++) {
	carrusel += '<div class="carousel-item';
	if (i==0) carrusel += " active";
	carrusel += `" style="background-color: `+banners[i].color+`">
		<a href="`+banners[i].url+`">
		<img class="d-block w-100" src="img/slide`+i+`.png">
		<div class="carousel-caption d-float d-md-block">
<h1>`+banners[i].titulo+`</h1>
<br>
<p class="vanish-mini">`+banners[i].texto+`</p>
`+banners[i].extra+`
		</div>
		</a>
	</div>
	`;
}

carrusel += `
	</div>
	<!-- Controles -->
	<a class="carousel-control-prev" href="#myCarousel"
role="button" data-slide="prev">
<span class="carousel-control-prev-icon" aria-hidden="true"></span>
<span class="sr-only">Previous</span>
	</a>
	<a class="carousel-control-next" href="#myCarousel"
role="button" data-slide="next">
<span class="carousel-control-next-icon" aria-hidden="true"></span>
<span class="sr-only">Next</span>
	</a>
`;

