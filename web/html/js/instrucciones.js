/**
 * Departamento de Computación, CINVESTAV CDMX.
 *
 * Sistema informático financiado con el proyecto CONACyT - 313572
 *
 * Derechos de autor (c) 2020
 * Todos los derechos reservados.
 */

var instrucciones = `
        <div class="col-sm-12 col-xs-12 tab-mini">
        <div class="d-flex d-row">
        <button class="tablinks boton-morado1" onclick="openTab(event, 'tab1')">A</button>
        <button class="tablinks boton-morado2" onclick="openTab(event, 'tab2')">B</button>
        <button class="tablinks boton-morado3" onclick="openTab(event, 'tab3')">C</button>
        </div>
        </div>

	<div class="col-lg-1 col-md-1"></div>

        <div class="col-lg-2 col-md-2 tab">
        <button class="tablinks boton-morado1" onclick="openTab(event, 'tab1')">A</button>
        <button class="tablinks boton-morado2" onclick="openTab(event, 'tab2')">B</button>
        <button class="tablinks boton-morado3" onclick="openTab(event, 'tab3')">C</button>
        </div>

<div id="inst-contenido" class="col-lg-8 col-md-8 col-sm-12 col-xs-12">

	<div id="tab1" class="tabcontent">
		<div class="d-flex flex-row justify-content-center">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
				<div class="w-100 d-flex justify-content-center align-items-center">
<img src="img/logo-android.png" class="img-fluid" style="max-width: 150px;">
				</div>
				<div class="w-100 d-flex justify-content-center align-items-center">
					<div class="bloque-instrucciones">
<h4>`+subtitulo_android+`</h4>
<br>
`+instrucciones_android+`
					</div>
				</div>
			</div>
			<div class="vanish-mini col-lg-6 col-md-6 col-sm-6 col-xs-12">
				<div class="w-100 d-flex justify-content-center align-items-center">
<img src="img/logo-ios.png" class="img-fluid" style="max-width: 150px;">
				</div>
				<div class="w-100 d-flex justify-content-center align-items-center">
					<div class="bloque-instrucciones">
<h4>`+subtitulo_ios+`</h4>
<br>
`+instrucciones_ios+`
					</div>
				</div>
			</div>
		</div>
		<div class="d-flex flex-row justify-content-center align-items-center"">
			<div class="only-mini col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="w-100 d-flex justify-content-center align-items-center">
<img src="img/logo-ios.png" class="img-fluid" style="max-width: 150px;">
				</div>
				<div class="w-100 d-flex justify-content-center align-items-center">
					<div class="bloque-instrucciones">
<h4>`+subtitulo_ios+`</h4>
<br>
`+instrucciones_ios+`
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="tab2" class="tabcontent">
        	<div class="d-flex justify-content-center align-items-center">
			<div class="d-flex justify-content-center col-lg-12 col-md-12 col-sm-12 col-xs-12">
<img src="img/bluetooth.png" class="img-fluid only-mini" style="max-height: 250px;">
			</div>
		</div>
        	<div class="d-flex justify-content-center align-items-center">
			<div class="bloque-instrucciones2 col-lg-8 col-md-8 col-sm-7 col-xs-12">
<h4>`+subtitulo_uso+`</h4>
<br>
`+instrucciones_uso+`
			</div>
			<div class="vanish-mini col-lg-4 col-md-4 col-sm-5 col-xs-12">
<img src="img/bluetooth.png" class="img-fluid">
			</div>
		</div>
	</div>

	<div id="tab3" class="tabcontent">
		<div class="only-mini d-flex justify-content-center col-lg-12 col-md-12 col-sm-12 col-xs-12">
<img src="img/phoneQR.png" class="img-fluid only-mini" style="max-height: 200px;">
		</div>
        	<div class="d-flex flex-row justify-content-center align-items-center">
			<div class="bloque-instrucciones2 col-lg-8 col-md-8 col-sm-7 col-xs-12">
<h4>`+subtitulo_reporte+`</h4>
<br>
`+instrucciones_reporte+`
			</div>
			<div class="vanish-mini col-lg-4 col-md-4 col-sm-5 col-xs-12">
<img src="img/phoneQR.png" class="img-fluid">
			</div>
		</div>
	</div>

</div>
`
