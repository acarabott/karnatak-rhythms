(
var anuDrutham, drutham, laghu, laghuName, laghuFingers, capu;
var druva, matya, rupaka, jhampa, triputa, ata, eka, misraCapu, khandaCapu;
var kallaiFunc, gatiFunc;

var images, netAd;
images = ["clap", "wave", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
netAd=NetAddr("127.0.0.1", 12000); 



anuDrutham = {	arg kallai=1;
					var action = "Clap";
					kallaiFunc.value(kallai, action)+"  ";
					
					{netAd.sendMsg(action);
					1.wait;
					}.fork;
				};

drutham = {	arg kallai=1;
				var action1 = "Clap";
				var action2 = "Wave";
				var ret;
				ret = kallaiFunc.value(kallai, action1)+ kallaiFunc.value(kallai, action2)+ "   ";				
				{netAd.sendMsg(action1);
				0.wait;
				netAd.sendMsg(action2);
				1.wait
				}.fork;
				};


laghu = {	arg jhati=4, kallai=1; 
			var action = "Clap";
			var ret;
			
			if( (jhati<3) || (jhati>9), 
				{ret= "Bad Jhati!"},
				{	ret = kallaiFunc.value(kallai, action);
					0.wait;	//Why??
					netAd.sendMsg(action);
					1.wait;
					for (0, jhati-2, {	arg i; ret = ret + kallaiFunc.value(kallai, laghuFingers[i]);
											netAd.sendMsg((i+2).asString);
											1.wait;
						});
					ret;	
				}.fork;
			); 
		};

capu = {arg gest="Clap", claps=1, kallai=1;
				var ret = "";
				for(0,claps-1, {arg i; ret = ret + b.value(kallai, gest)});
				ret = ret + b.value(kallai, "Rest");
				ret.postln
			};		
			
laghuName = {arg jhati;
				switch (jhati)
				{3} 	{"Thisra"}
				{4} 	{"Chaturusra"}
				{5}		{"Khanda"}
				{6}		{"Bad Jhati!"}
				{7}		{"Misra"}
				{8}		{"Bad Jhati!"}
				{9}		{"Shankeerna"};
				};

laghuFingers = ["2 lf", "3 rf", "4 mf", "5 ff", "6 t", "7 lf", "8 rf", "9 mf"]; 

kallaiFunc = {arg kallai=1, action="NoAction!";
			var ret, ret2;
			ret = action;
			ret2 = "";
			if(kallai!=1, {for(0, kallai-2, {ret2 = ret2 + "("+action+")"}); ret = ret+ret2}, {ret});
			};
			
gatiFunc = {arg gati=2; 
				switch(gati)
				{2}		{""}
				{3}		{"Thisra Gati"}
				{4}		{"Chaturusra Gati"}
				{5}		{"Khanda Gati"}
				{6}		{"Bad Gati!"}
				{7}		{"Misra Gati"}
				{8}		{"Bad Gati!"}
				{9}		{"Shankeerna Gati"}
			};

druva = {arg jhati=4, kallai=1, gati=2; laghuName.value(jhati) + "Jhati" + "Druva Thala, : " + laghu.value(jhati) + drutham.value(kallai) + laghu.value(jhati) + gatiFunc.value(gati)};
matya = {arg jhati=4, kallai=1, gati=2; laghuName.value(jhati) + "Jhati" + "Matya Thala, : " + laghu.value(jhati) + drutham.value(kallai) + laghu.value(jhati) + gatiFunc.value(gati)};
rupaka = {arg jhati=4, kallai=1, gati=2; laghuName.value(jhati) + "Jhati" + "Rupaka Thala, : " + drutham.value(kallai) + laghu.value(jhati) + gatiFunc.value(gati)};
jhampa = {arg jhati=7, kallai=1, gati=2; laghuName.value(jhati) + "Jhati" + "Jhampa Thala, : " + laghu.value(jhati) + anuDrutham.value(kallai) + drutham.value(kallai) + gatiFunc.value(gati)};
triputa = {arg jhati=3, kallai=1, gati=2; laghuName.value(jhati) + "Jhati" + "Triputa Thala, : " + laghu.value(jhati,kallai) + drutham.value(kallai) + drutham.value(kallai) + gatiFunc.value(gati)};
ata = {arg jhati=5, kallai=1, gati=2; laghuName.value(jhati) + "Jhati" + "Ata Thala, : " + laghu.value(jhati) + laghu.value(kallai) + drutham.value(kallai) + drutham.value(kallai) + gatiFunc.value(gati)};
eka = {arg jhati=4, kallai=1, gati=2; laghuName.value(jhati) + "Jhati " + "Eka Thala, : " + laghu.value(jhati) + gatiFunc.value(gati)};

khandaCapu = {arg kallai=1, gati=2; "Khanda Capu Tala" + capu.value("Clap", 1, kallai) + capu.value("Clap", 2, kallai) + gatiFunc.value(gati) };
misraCapu = {arg kallai=1, gati=2; "Misra Capu Tala" + capu.value("Wave", 2, kallai) + capu.value("Clap", 1, kallai) + capu.value("Clap", 1, kallai) + gatiFunc.value(gati)};


//druva.value().postln;
//matya.value().postln;
//rupaka.value().postln;
//jhampa.value().postln;
//triputa.value().postln;
//ata.value().postln;
//eka.value().postln;

laghu.value(3,3).postln;
//drutham.value(1).postln;
//misraCapu.value(2, 3).postln
)