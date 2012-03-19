var refresh = 2000;

var HelpScript = new Object();
HelpScript.run = function(){
    var val_sen_tmp = god.getSensorValor("TMP",0);
    if(val_sen_tmp != null){ // existe el sensor
        if(!isNaN(val_sen_tmp)){ // tiene algun valor conosido ?
            god.print("El valor es:"+val_sen_tmp);
            if(val_sen_tmp >= 98){
                god.accionar("rl",1,true);
            }else{
                god.accionar("rl",1,false);
            }
        }
    }else{
        god.print("No existe el Sensor");
    }
}