var map, featureList, boroughSearch = [], municipiosSearch=[];
var fecha = new Date();
var dias = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];
var meses = ["Enero", "Febrero", "Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"];


var getColor;
var casos;
var tipocasos;

var valoresESConfirmados;
var valoresESNegativos;
var valoresESSospechosos;
var valoresESDefunciones;


var valoresMunConfirmados;
var valoresMunNegativos;
var valoresMunSospechosos;
var valoresMunDefunciones;


$(window).resize(function() {
  sizeLayerControl();
});

$(document).on("click", ".feature-row", function(e) {
  $(document).off("mouseout", ".feature-row", clearHighlight);
  sidebarClick(parseInt($(this).attr("id"), 10));
});

if ( !("ontouchstart" in window) ) {
  $(document).on("mouseover", ".feature-row", function(e) {
    highlight.clearLayers().addLayer(L.circleMarker([$(this).attr("lat"), $(this).attr("lng")], highlightStyle));
  });
}

$(document).on("mouseout", ".feature-row", clearHighlight);

$("#about-btn").click(function() {
  $("#aboutModal").modal("show");
  $(".navbar-collapse.in").collapse("hide");
  return false;
});

$("#full-extent-btn").click(function() {
  map.fitBounds(boroughs.getBounds());
  $(".navbar-collapse.in").collapse("hide");
  return false;
});

$("#legend-btn").click(function() {
  $("#legendModal").modal("show");
  $(".navbar-collapse.in").collapse("hide");
  return false;
});

/*$("#login-btn").click(function() {
  $("#loginModal").modal("show");
  $(".navbar-collapse.in").collapse("hide");
  return false;
}); */

$("#list-btn").click(function() {
  animateSidebar();
  return false;
});

$("#nav-btn").click(function() {
  $(".navbar-collapse").collapse("toggle");
  return false;
});

$("#sidebar-toggle-btn").click(function() {
  animateSidebar();
  return false;
});

$("#sidebar-hide-btn").click(function() {
  animateSidebar();
  return false;
});



function animateSidebar() {
  $("#sidebar").animate({
    width: "toggle"
  }, 350, function() {
    map.invalidateSize();
  });
}

function sizeLayerControl() {
  $(".leaflet-control-layers").css("max-height", $("#map").height() - 50);
}

function clearHighlight() {
  highlight.clearLayers();
}

function sidebarClick(id) {
  var layer = boroughs.getLayer(id);
  var es = layer.getBounds();
  map.setView([es.getCenter().lat, es.getCenter().lng], 17);
  //var layer = markerClusters.getLayer(id);
  //map.setView([layer.getLatLng().lat, layer.getLatLng().lng], 17);
  

  layer.fire("click");
  /* Hide sidebar and go to the map on small screens */
  if (document.body.clientWidth <= 767) {
    $("#sidebar").hide();
    map.invalidateSize();
  }
}

function dataNacional (){
  
$.ajax({
        url:'assets/php/getDatosNacionales.php',
        data: {
          table: "confirmados_estatal"
        },
        success:function(data){
          $("#overlay").html(data);
        
        }
      });
}





//================================================================================================
// CAPAS BASE
//================================================================================================

var cartoLight = L.tileLayer("https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}.png", {
  maxZoom: 19,
  attribution: '&copy; <a href="https://www.inegi.org.mx/datos/">INEGI Datos</a>,  &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> , &copy; <a href="https://cartodb.com/attributions">CartoDB</a>'
});

/*var SateliteMap = L.tileLayer("https://api.tiles.mapbox.com/v4/mapbox.satellite/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiYWJlcm5hbDI3IiwiYSI6ImNrZGo5aTF0ajBjc2Eycm9mNWxuNHEydHMifQ.oFTp7TsGQInbDy-TtSVD4A", {
  maxZoom: 19
}); */

var OpenStreet = L.tileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",{
  maxZoom:19,
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> '
}) 

//================================================================================================
// CAPAS SOBREPUESTAS
//================================================================================================
var highlight = L.geoJson(null);
var highlightStyle = {
  stroke: false,
  fillColor: "#00FFFF",
  fillOpacity: 0.7,
  radius: 10
};
//================================================================================================
// FUNCION PARA ZOOM AL DAR CLICK
//================================================================================================
function zoomToFeature(e) {
    map.fitBounds(e.target.getBounds());
  }
//================================================================================================
//================================================================================================
var info = L.control({position: 'bottomleft'});
info.onAdd = function (map) {
    this._div = L.DomUtil.create('div', 'info');
   // this.update();
    return this._div;
  };

info.update = function (valor) {
   var layer = valor.target;
    this._div.innerHTML = '<h5>'+layer.feature.properties.nombre+'</h5>';   
}
//===============================================================================================

//================================================================================================
//================================================================================================

var legend = L.control({position: 'topright'});
legend.onAdd = function (map) {
    var div = L.DomUtil.create('div', 'info legend');
    div.innerHTML = '<select id="tipocaso"><option value="1">Confirmados</option><option value="2">Negativos</option><option value="3">Sospechosos</option><option value="4">Defunciones</option></select>';
    div.firstChild.onmousedown = div.firstChild.ondblclick = L.DomEvent.stopPropagation;
    return div;
};




//====================================================================================================
// FUNCION DE ESTILO
//====================================================================================================

function style (feature){


  casos = feature.properties.confirmados; 
  //dropdown.selectedIndex="0";
  
  return {
        
        fillColor: getColor(casos),
        fillOpacity: 0.7,
        weight: 0.5,
        //color: 'white',
        clickable: true
      };
}


//====================================================================================================
// CAPA ESTADOS
//====================================================================================================



//====================================================================================================
// CAPA ESTADOS
//====================================================================================================

boroughsLayer = L.geoJson(null);
var boroughs = L.geoJson(null, {
  style: style,
  onEachFeature: function (feature, layer) {
    boroughSearch.push({
      name: layer.feature.properties.nombre,
      source: "Boroughs",
      id: L.stamp(layer),
      bounds: layer.getBounds()
    });
    if (feature.properties) {
      var content = "<table class='table table-striped table-bordered table-condensed'>" +  
                    "<tr><th><font color ='green'>Confirmados</font></th><td>" + feature.properties.confirmados + "</td></tr>" + 
                    "<tr><th><font color ='red'>Negativos</font></th><td>" + feature.properties.negativos + "</td></tr>" + 
                    "<tr><th><font color ='orange'>Sospechosos</font></th><td>" + feature.properties.sospechosos + "</td></tr>" + 
                    "<tr><th>Defunciones</th><td>" + feature.properties.defunciones + "</td></tr>" + 
                    "<table>";
      layer.on({
        click: function (e) {
          $("#feature-title").html(feature.properties.nombre);
          $("#feature-subtitle").html("<i>"+ dias[fecha.getDay()] +" "+ fecha.getDate()+" de "+ meses[fecha.getMonth()] +" de "+ fecha.getFullYear()+"</i>");
          $("#feature-info").html(content);
          $("#featureModal").modal("show");

        }
      });
    };
    var es = layer.getBounds();
    $("#feature-list tbody").append('<tr class="feature-row" id="' + L.stamp(layer) + '" lat="' + es.getCenter().lat + '" lng="' + es.getCenter().lng + '"><td style="vertical-align: middle;"><img width="16" height="18" src="assets/img/estado.png"></td><td class="feature-name">' + layer.feature.properties.nombre + '</td><td style="vertical-align: middle;"><i class="fa fa-chevron-right pull-right"></i></td></tr>');
    layer.on({
      mouseover: function (e) {
        var layer = e.target;
        layer.setStyle({
          weight: 4,
          fillOpacity: 0.7
         // color :'black'
          //fillColor: '#9564a1'
        });
        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
          layer.bringToFront();
        }
        //highlightFeature(feature.properties.nombre);
         info.update(e);
         //$("#info").html(feature.properties.nombre);
      },
      mouseout: function (e) {
        var layer = e.target;
        layer.setStyle({
          weight: 0.5,
          fillOpacity: 0.7
          //color :'white'
          //fillColor: '#9564a1'
        });
      },
      click: zoomToFeature
    });
  }
});

$.ajax("assets/php/getDataEstados.php", {
    data: {
      table: "estados"
    },
    success: function(data){

         

      valoresESConfirmados = data.features.map(function(feature) {
      return feature.properties.confirmados;
      });

      valoresESNegativos = data.features.map(function(feature) {
      return feature.properties.negativos;
    });

    valoresESSospechosos = data.features.map(function(feature) {
      return feature.properties.sospechosos;
    });

    valoresESDefunciones = data.features.map(function(feature) {
      return feature.properties.defunciones;
    });

   

    getColor = d3
      .scaleQuantile()
      .domain(valoresESConfirmados) // get the min and max values
      .range(d3.schemeGreens[5]); // set 5 color buckets


      boroughs.addData(data);
      map.addLayer(boroughsLayer);


    }
  });


function cadaPoligonoMun (feature, layer){
  municipiosSearch.push({
      name: layer.feature.properties.nombre,
      source: "Municipios",
      id: L.stamp(layer),
      bounds: layer.getBounds()
    });
    if (feature.properties) {
      var content = "<table class='table table-striped table-bordered table-condensed'>" + 
                    "<tr><th><font color ='green'>Confirmados</font></th><td>" + feature.properties.confirmados + 
                    "</td></tr>" + "<tr><th><font color ='red'>Negativos</font></th><td>" + feature.properties.negativos + 
                    "</td></tr>" + "<tr><th><font color ='orange'>Sospechosos</font></th><td>" + feature.properties.sospechosos + 
                    "</td></tr>" + "<tr><th>Defunciones</th><td>" + feature.properties.defunciones + 
                    "</td></tr>" + "<table>";
      layer.on({
        click: function (e) {
          $("#feature-title").html(feature.properties.nombre);
          $("#feature-subtitle").html("<i>"+ dias[fecha.getDay()] +" "+ fecha.getDate()+" de "+ meses[fecha.getMonth()] +" de "+ fecha.getFullYear()+"</i>");
          $("#feature-info").html(content);
          $("#featureModal").modal("show");

        }
      });
    }
    layer.on({
      mouseover: function (e) {
        var layer = e.target;
        layer.setStyle({
          weight: 3,
          color:'black',
          //fillColor: "#9564a1",
          fillOpacity: 2
        });
        if (!L.Browser.ie && !L.Browser.opera) {
          layer.bringToFront();
        }
        info.update(e);
      },
      mouseout: function (e) {
        var layer = e.target;
        layer.setStyle({
          weight: 0.5,
          color: 'white',
          //fillColor: "#9564a1",
          fillOpacity: 0.7
        });
      },
      click: zoomToFeature
    });
}






var municipiosLayer = L.geoJson(null);
var municipios = function(){
  var mun = null;
  $.ajax("assets/php/getCasosMunicipios.php", {
    async: false,
    data: {
      table: "municipios"
    },
    success: function(data){    

       

    valoresMunConfirmados = data.features.map(function(feature) {
      return feature.properties.confirmados;
    });

    valoresMunNegativos = data.features.map(function(feature) {
      return feature.properties.negativos;
    });

    valoresMunSospechosos = data.features.map(function(feature) {
      return feature.properties.sospechosos;
    });

    valoresMunDefunciones = data.features.map(function(feature) {
      return feature.properties.defunciones;
    });



    getColor = d3
      .scaleQuantile()
      .domain(valoresMunConfirmados) // get the min and max values
      .range(d3.schemeGreens[5]); // set 5 color buckets

      mun = L.geoJson(data,{ style: style, onEachFeature: cadaPoligonoMun});
      
    }
  });
  return mun;
}();


//====================================================================================================
// DATOS NACIONALES 
//====================================================================================================






map = L.map("map", {
  zoom: 10,
  center: [40.702222, -73.979378],
  layers: [ OpenStreet, boroughs], 
  zoomControl: false,
  attributionControl: false
});



map.on("overlayadd", function(e) {
  

 dropdown.selectedIndex="0";
  if (e.layer === boroughsLayer) {
    boroughs.addLayer(boroughs);
    
  }





});

map.on("overlayremove", function(e) {
  dropdown.selectedIndex="0";

  if (e.layer === boroughsLayer) {
    boroughs.removeLayer(boroughsLayer);

  }





});

/* Filter sidebar feature list to only show features in current map bounds */
map.on("moveend", function (e) {
  //syncSidebar();
});

/* Clear feature highlight when map is clicked */
map.on("click", function(e) {
  highlight.clearLayers();
});

/* Attribution control */
function updateAttribution(e) {
  $.each(map._layers, function(index, layer) {
    if (layer.getAttribution) {
      $("#attribution").html((layer.getAttribution()));
    }
  });

}
map.on("layeradd", updateAttribution);
map.on("layerremove", updateAttribution);

var attributionControl = L.control({
  position: "bottomright"
});
attributionControl.onAdd = function (map) {
  var div = L.DomUtil.create("div", "leaflet-control-attribution");
  div.innerHTML = "<span class='hidden-xs'>Desarrollado por <a href='https://www.cs.cinvestav.mx/'>Cinvestav</a> | </span><a href='#' onclick='$(\"#attributionModal\").modal(\"show\"); return false;'>Datos</a>";
  return div;
};
map.addControl(attributionControl);

var zoomControl = L.control.zoom({
  position: "topleft"
}).addTo(map);

/* Control de la geolocalización del usuario */
var locateControl = L.control.locate({
  position: "topleft",
  drawCircle: true,
  follow: true,
  setView: true,
  keepCurrentZoomLevel: true,
  markerStyle: {
    weight: 1,
    opacity: 0.8,
    fillOpacity: 0.8
  },
  circleStyle: {
    weight: 1,
    clickable: false
  },
  icon: "fa fa-location-arrow",
  metric: true,
  strings: {
    title: "Mi ubicación",
    popup: "Te ubicas a {distance} {unit} desde este punto",
    outsideMapBoundsMsg: "Pareces estar ubicado fuera de los límites del mapa"
  },
  locateOptions: {
    maxZoom: 18,
    watch: true,
    enableHighAccuracy: true,
    maximumAge: 10000,
    timeout: 10000
  }
}).addTo(map);

//Boton para expandir el mapa


var expandir = L.easyButton({
  states: [{
    stateName: 'expandir',
    icon: 'fa-expand fas',
    position: 'topleft',
    onClick: function() {
      map.fitBounds(boroughs.getBounds());
    }
  }]
});
expandir.addTo(map);
   


/* Larger screens get expanded layer control and visible sidebar */
if (document.body.clientWidth <= 767) {
  var isCollapsed = true;
} else {
  var isCollapsed = false;
}

var baseLayers = {
  "Carto Street Map": cartoLight,
  "Open Street Map": OpenStreet
};

var groupedOverlays = {
  
  "Mapas": {
    "Estados": boroughs,
    "Municipios": municipios
  }

};

var layerControl = L.control.groupedLayers(baseLayers, groupedOverlays, {
  collapsed: isCollapsed,
  exclusiveGroups: ["Mapas"],
  groupCheckboxes: false
}).addTo(map);


info.addTo(map);
legend.addTo(map);



var dropdown = document.getElementById("tipocaso");
$('select').change(function(){

   tipocasos = dropdown.options[dropdown.selectedIndex].value;
   
   if (map.hasLayer(municipios)){

      switch(tipocasos){
          case '1': getColor = d3.scaleQuantile().domain(valoresMunConfirmados).range(d3.schemeGreens[5]); 
          break; 
          case '2': getColor = d3.scaleQuantile().domain(valoresMunNegativos).range(d3.schemeReds[5]); 
          break;
          case '3': getColor = d3.scaleQuantile().domain(valoresMunSospechosos).range(d3.schemeOranges[5]); 
          break;
          case '4': getColor = d3.scaleQuantile().domain(valoresMunDefunciones).range(d3.schemeGreys[5]); 
          break;
          }
      
      municipios.eachLayer(function(layer){
        switch(tipocasos){
          case '1': casos = layer.feature.properties.confirmados; 
          break; 
          case '2': casos = layer.feature.properties.negativos; 
          break;
          case '3': casos = layer.feature.properties.sospechosos; 
          break;
          case '4': casos = layer.feature.properties.defunciones; 
          break;
          }

          layer.setStyle({fillColor: getColor(casos)});
      });
    }    // Fin del if de municipios
    if (map.hasLayer(boroughs)){

      switch(tipocasos){
          case '1': getColor = d3.scaleQuantile().domain(valoresESConfirmados).range(d3.schemeGreens[5]); 
          break; 
          case '2': getColor = d3.scaleQuantile().domain(valoresESNegativos).range(d3.schemeReds[5]); 
          break;
          case '3': getColor = d3.scaleQuantile().domain(valoresESSospechosos).range(d3.schemeOranges[5]); 
          break;
          case '4': getColor = d3.scaleQuantile().domain(valoresESDefunciones).range(d3.schemeGreys[5]); 
          break;
          }
      
      boroughs.eachLayer(function(layer){
        switch(tipocasos){
          case '1': casos = layer.feature.properties.confirmados; 
          break; 
          case '2': casos = layer.feature.properties.negativos; 
          break;
          case '3': casos = layer.feature.properties.sospechosos; 
          break;
          case '4': casos = layer.feature.properties.defunciones; 
          break;
          }

          layer.setStyle({fillColor: getColor(casos)});
      });
    }    // Fin del if de estados
});



/* Highlight search box text on click */
$("#searchbox").click(function () {
  $(this).select();
});

/* Prevent hitting enter from refreshing the page */
$("#searchbox").keypress(function (e) {
  if (e.which == 13) {
    e.preventDefault();
  }
});

$("#featureModal").on("hidden.bs.modal", function (e) {
  $(document).on("mouseout", ".feature-row", clearHighlight);
});


/* Typeahead search functionality */
$(document).one("ajaxStop", function () {
  $("#loading").hide();
  sizeLayerControl();
  dataNacional();
  /* Fit map to boroughs bounds */
  map.fitBounds(boroughs.getBounds());


  var boroughsBH = new Bloodhound({
    name: "Boroughs",
    datumTokenizer: function (d) {
      return Bloodhound.tokenizers.whitespace(d.name);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    local: boroughSearch,
    limit: 10
  });


   var municipiosBH = new Bloodhound({
    name: "Municipios",
    datumTokenizer: function (d) {
      return Bloodhound.tokenizers.whitespace(d.name);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    local: municipiosSearch,
    limit: 10
  });


  var geonamesBH = new Bloodhound({
    name: "GeoNames",
    datumTokenizer: function (d) {
      return Bloodhound.tokenizers.whitespace(d.name);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
      url: "http://api.geonames.org/searchJSON?username=bootleaf&featureClass=P&maxRows=5&countryCode=MX&name_startsWith=%QUERY",
      filter: function (data) {
        return $.map(data.geonames, function (result) {
          return {
            name: result.name + ", " + result.adminCode1,
            lat: result.lat,
            lng: result.lng,
            source: "GeoNames"
          };
        });
      },
      ajax: {
        beforeSend: function (jqXhr, settings) {
          settings.url += "&east=" + map.getBounds().getEast() + "&west=" + map.getBounds().getWest() + "&north=" + map.getBounds().getNorth() + "&south=" + map.getBounds().getSouth();
          $("#searchicon").removeClass("fa-search").addClass("fa-refresh fa-spin");
        },
        complete: function (jqXHR, status) {
          $('#searchicon').removeClass("fa-refresh fa-spin").addClass("fa-search");
        }
      }
    },
    limit: 10
  });
  boroughsBH.initialize();
  municipiosBH.initialize();
  geonamesBH.initialize();

  /* instantiate the typeahead UI */
  $("#searchbox").typeahead({
    minLength: 3,
    highlight: true,
    hint: false
  }, {
    name: "Boroughs",
    displayKey: "name",
    source: boroughsBH.ttAdapter(),
    templates: {
      header: "<h4 class='typeahead-header'>Estados</h4>"
    }
  }, {
    name: "Municipios",
    displayKey: "name",
    source: municipiosBH.ttAdapter(),
    templates: {
      header: "<h4 class='typeahead-header'>Municipios</h4>"
    }
  }, 
    {
    name: "GeoNames",
    displayKey: "name",
    source: geonamesBH.ttAdapter(),
    templates: {
      header: "<h4 class='typeahead-header'><img src='assets/img/globe.png' width='25' height='25'>&nbsp;GeoNames</h4>"
    }
  }).on("typeahead:selected", function (obj, datum) {
    if (datum.source === "Boroughs") {
      map.fitBounds(datum.bounds);
    }
    if (datum.source === "Municipios") {
      map.fitBounds(datum.bounds);
    }

    if (datum.source === "GeoNames") {
      map.setView([datum.lat, datum.lng], 14);
    }
    if ($(".navbar-collapse").height() > 50) {
      $(".navbar-collapse").collapse("hide");
    }
  }).on("typeahead:opened", function () {
    $(".navbar-collapse.in").css("max-height", $(document).height() - $(".navbar-header").height());
    $(".navbar-collapse.in").css("height", $(document).height() - $(".navbar-header").height());
  }).on("typeahead:closed", function () {
    $(".navbar-collapse.in").css("max-height", "");
    $(".navbar-collapse.in").css("height", "");
  });
  $(".twitter-typeahead").css("position", "static");
  $(".twitter-typeahead").css("display", "block");
});

// Leaflet patch to make layer control scrollable on touch browsers
var container = $(".leaflet-control-layers")[0];
if (!L.Browser.touch) {
  L.DomEvent
  .disableClickPropagation(container)
  .disableScrollPropagation(container);
} else {
  L.DomEvent.disableClickPropagation(container);
}
