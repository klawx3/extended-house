var refresh = 5000;

var hola = new Object();
hola.run = function(){
    var val_sen_tmp = god.getSensorValor("TMP",0);
    if(val_sen_tmp != null){ // existe el sensor
        if(!isNaN(val_sen_tmp)){ // tiene algun valor conosido ?
            god.print("El valor es:"+val_sen_tmp);
        }else{
            god.print("El valor es NaN (pero existe el sensor)");   
        }
    }else{
        god.print("No existe el Sensor");
    }
}