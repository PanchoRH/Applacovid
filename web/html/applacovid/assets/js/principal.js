 var SColors = new Array(); 
 //NColors['01']='yellow';NColors['02']='yellow';NColors['03']='orange';NColors['04']='green';NColors['07']='yellow';NColors['08']='yellow';NColors['09']='orange';NColors['05']='yellow';NColors['06']='orange';NColors['10']='yellow';NColors['11']='yellow';NColors['12']='orange';NColors['13']='orange';NColors['14']='orange';NColors['15']='orange';NColors['16']='orange';NColors['17']='orange';NColors['18']='orange';NColors['19']='orange';NColors['20']='yellow';NColors['21']='yellow';NColors['22']='yellow';NColors['23']='orange';NColors['24']='orange';NColors['25']='yellow';NColors['26']='yellow';NColors['27']='yellow';NColors['28']='yellow';NColors['29']='yellow';NColors['30']='orange';NColors['31']='orange';NColors['32']='orange';  
SColors = new Array(); SColors['01']='#FF7000';SColors['02']='#FF7000';SColors['03']='#FF7000';SColors['04']='#02A247';SColors['07']='#FFC60F';SColors['08']='#FF0000';SColors['09']='#FF7000';SColors['05']='#FF7000';SColors['06']='#FF7000';SColors['10']='#FF7000';SColors['11']='#FFC60F';SColors['12']='#FF7000';SColors['13']='#FF7000';SColors['14']='#FF7000';SColors['15']='#FF7000';SColors['16']='#FF7000';SColors['17']='#FFC60F';SColors['18']='#FF7000';SColors['19']='#FF7000';SColors['20']='#FFC60F';SColors['21']='#FFC60F';SColors['22']='#FF7000';SColors['23']='#FF7000';SColors['24']='#FF7000';SColors['25']='#FFC60F';SColors['26']='#FFC60F';SColors['27']='#FFC60F';SColors['28']='#FFC60F';SColors['29']='#FFC60F';SColors['30']='#FFC60F';SColors['31']='#FF7000';SColors['32']='#FF7000';

 var map = L.map('map').setView([24, -102], 5);

  L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoiYWJlcm5hbDI3IiwiYSI6ImNrZGo5aTF0ajBjc2Eycm9mNWxuNHEydHMifQ.oFTp7TsGQInbDy-TtSVD4A', {
    maxZoom: 18,
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> , ' +
      'Imagenes &copy <a href="https://www.mapbox.com/">Mapbox</a>',
    id: 'mapbox/light-v9',
    tileSize: 512,
    zoomOffset: -1
  }).addTo(map);

var info = L.control({position: 'bottomleft'});
info.onAdd = function (map) {
    this._div = L.DomUtil.create('div', 'info');
   // this.update();
    return this._div;
  };
info.update = function (valor) {
   var layer = valor.target;
    this._div.innerHTML = '<h5>'+layer.feature.properties.nombre+'</h5> <font color ="green">Casos confirmados: '+layer.feature.properties.confirmados+' </font>';   
}
info.update2 = function (valor) {
  var layer = valor.target;
  this._div.innerHTML = '<h5> <font color ="'+SColors[layer.feature.properties.cve]+'">'+layer.feature.properties.nombre+' </font></h5>';
}
info.reset = function () {
    this._div.innerHTML = '';
}


 //========================================================================================
 //========================================================================================
 // CAPA SEMAFORO

  function style(feature) {
    return {
      weight: 1,
      opacity: 0.7,
      color: 'white',
      fillOpacity: 0.6,
      fillColor: SColors[feature.properties.cve]
    };
  }

  
  var semaforo = L.geoJson(null, {
    style: style,
    onEachFeature: function (feature, layer) {
      layer.on({
        mouseover: function (e) {
         layer.setStyle({
          weight: 3,
          fillOpacity: 1
        });
         info.update2(e);
        },
        mouseout: function (e) {
          layer.setStyle({
            weight: 1,
            fillOpacity: 0.6
          });
          info.reset();
        }
      });
    }
  });


$.ajax("assets/php/getSemaforo.php", {
    data: {
      table: "estados"
    },
    success: function(data){
      semaforo.addData(data);

    }
  });



//========================================================================================
//========================================================================================
// CAPA RIESGO

var riesgo = function(){
  var r = null;
  $.ajax("assets/php/getCabeceras.php", {
    async: false,
    data: {
      table: "cabeceras"
    },
    success: function(data){    

      r = L.heatLayer(data, {minOpacity: 3000, radius: 15});
      
    }
  });
  return r;
}();

riesgo.addTo(map);


//========================================================================================
//========================================================================================
// CAPA CASOS (Solo confirmados)

  function style2(feature) {
    return {
      weight: 1,
      opacity: 0.5,
      color: 'white',
      fillOpacity: 0.6,
      fillColor: '#9564a1'
    };
  }

  
  var casos = L.geoJson(null, {
    style: style2,
    onEachFeature: function (feature, layer) {
      layer.on({
        mouseover: function (e) {
         layer.setStyle({
          weight: 3,
          fillOpacity: 1
        });
         info.update(e);
        },
        mouseout: function (e) {
          layer.setStyle({
            weight: 1,
            fillOpacity: 0.6
          });
          info.reset();
        }
      });
    }
  });


$.ajax("assets/php/getCasos.php", {
    data: {
      table: "estados"
    },
    success: function(data){
      casos.addData(data);
    }
  });



map.attributionControl.addAttribution('Datos &copy; <a href="https://www.inegi.org.mx/datos/">INEGI</a>');


var expandir = L.easyButton({
  states: [{
    stateName: 'expandir',
    icon: 'fa-expand fas',
    position: 'topleft',
    onClick: function() {
      map.fitBounds(semaforo.getBounds());
    }
  }]
});
expandir.addTo(map);
info.addTo(map);





$("#botonriesgo").on("click", function(){

if (map.hasLayer(semaforo)){
    map.removeLayer(semaforo);
    map.addLayer(riesgo);
  }
  else{
      if(map.hasLayer(casos)){
        map.removeLayer(casos);
        map.addLayer(riesgo);
      }
      else{
        if (!map.hasLayer(riesgo)){
            map.addLayer(riesgo);
        }
      }
  }
});

$("#botonsemaforo").on("click", function(){

 if (map.hasLayer(casos)){
    map.removeLayer(casos);
    map.addLayer(semaforo);
  }
  else{
      if(map.hasLayer(riesgo)){
        map.removeLayer(riesgo);
        map.addLayer(semaforo);
      }
      else{
        if (!map.hasLayer(semaforo)){
            map.addLayer(semaforo);
        }
      }
  }
});

$("#botoncasos").on("click", function(){

  if (map.hasLayer(riesgo)){
    map.removeLayer(riesgo);
    map.addLayer(casos);
  }
  else{
      if(map.hasLayer(semaforo)){
        map.removeLayer(semaforo);
        map.addLayer(casos);
      }
      else{
        if (!map.hasLayer(casos)){
            map.addLayer(casos);
        }
      }
  }
});