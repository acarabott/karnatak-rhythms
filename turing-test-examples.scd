/*
	These are the examples used for a musical Turing Test. Made up of transcribed phrases from real players, and generated phrases

	TODO 
	correct strokes in logic
		For vikku examples get strokes from DVD.0***
*/

///////////////////////////////////////////////////////////////Undeveloped phrases//////////////////////////////////////////////////
{

//Palghat R. Raghu's opening phrase
a = KonaTani.new(98, #["I4","O","O"], 4, argSynth:\MIDITranscribe);
~raghuOpening = KonaTime.new(a);
~raghuOpening.add(KonaWord.new(2,4,2,a));		//	Din	-	Din	-
~raghuOpening.add(KonaWord.new(0,4,2,a));		//	-	-
~raghuOpening.add(KonaWord.new(1,4,2,a));		//	Tam	-
~raghuOpening.add(KonaWord.new(0,4,4,a));		//	-	-	-	-
~raghuOpening.add(KonaWord.new(0,4,2,a));		//	-	-
~raghuOpening.add(KonaWord.new(1,4,2,a));		//	Tam	-


a.add(~raghuOpening)
a.add(KonaWord.new(1,4,32,a))		//	break

~myRaghuOpening = a.gen.vSarvaPhraseAuto
2.do { |i|
	a.add(~myRaghuOpening);
	a.add(~myRaghuOpening);
	a.add(KonaWord.new(1,4,32,a))		//	break	
};

a.play 
a.stop

//Trichy S. Sankaran's opening phrase
a = KonaTani.new(90, #["I4","O","O"], 4, argSynth:\MIDITranscribe);
~trichyOpening = KonaTime.new(a);
4.do { |i|
~trichyOpening.add(KonaWord.new(2,4,2,a))		//	Na -	Din -
												//	Din	-	Na	-	
};

~trichyOpening.add(KonaWord.new(1,4,16,a))		//	break

a.add(~trichyOpening);

~myTrichyOpening = a.gen.vSarvaPhraseAuto

2.do { |i|
	a.add(~myTrichyOpening);
	a.add(~myTrichyOpening);
	a.add(KonaWord.new(1,4,32,a))		//	break
	
};
a.clear
a.play
a.stop

//Karaikudi R. Mani's Opening
a = KonaTani.new(91, #["I4","O","O"], 4, argSynth:\MIDITranscribe);
~maniOpening = KonaTime.new(a);
2.do { |i|
~maniOpening.add(KonaWord.new(1,4,4,a));		//	Tam	-	-	-
~maniOpening.add(KonaWord.new(0,4,2,a));		//	-	-
~maniOpening.add(KonaWord.new(1,4,2,a));		//	Tam	-
};

~maniOpening.add(KonaWord.new(1,4,16,a))		//	break
~maniOpening.postWord
a.add(~maniOpening)

~myManiOpening = a.gen.vSarvaPhraseAuto
~myManiOpening.postWord
2.do { |i|
	a.add(~myManiOpening);
	a.add(~myManiOpening);
	a.add(KonaWord.new(1,4,32,a))
};
a.play    
a.stop


//Vikku Sankirna Chapu
a = KonaTani.new(80, #["U","U","U", "W", "W", "R"], 4, argSynth:\MIDITranscribe);

~vikkuSankirna = KonaTime.new(a);

~vikkuSankirna.add(KonaWord.new(4,4,1, a));		//	Ta	ka	di	mi
~vikkuSankirna.add(KonaWord.new(2,4,1, a));		//	Ta	ka	
~vikkuSankirna.add(KonaWord.new(2,4,1, a));		//	Ju	na
~vikkuSankirna.add(KonaWord.new(4,4,1, a));		//	Ta	ka	di	mi
~vikkuSankirna.add(KonaWord.new(3,4,1, a));		//	Ta	ki	ta
~vikkuSankirna.add(KonaWord.new(3,4,1, a));		//	Ta	di	mi

a.add(~vikkuSankirna)
a.add(KonaWord.new(1,4,18,a))

~myVikkuSankirna = a.gen.vSarvaPhraseAuto;
~myVikkuSankirna.postWord
2.do { |i|
	a.add(~myVikkuSankirna);
	a.add(~myVikkuSankirna);
	a.add(KonaWord.new(1,4,18,a))
};
a.play
a.stop

//Vikku Adi Tala Tisra Gati
a = KonaTani.new(120, #["I4","O","O"], 3, argSynth:\MIDITranscribe);
 
~vikkuTisra = KonaTime.new(a);

~vikkuTisra.add(KonaWord.new(1,3,2, a));		//	Tam
~vikkuTisra.add(KonaWord.new(6,3,1, a));		//	Ta	ka	ki	tah	ta	ka
~vikkuTisra.add(KonaWord.new(2,3,1, a));		//	Din	ta
~vikkuTisra.add(KonaWord.new(2,3,1, a));		//	Din	ta

2.do { |i|
	a.add(~vikkuTisra)
	a.add(KonaWord.new(1,3,24,a))	
};

~myVikkuTisra = a.gen.vSarvaPhraseAuto;

2.do { |i|
	a.add(~myVikkuTisra);
	a.add(~myVikkuTisra);
	a.add(KonaWord.new(1,3,24,a))		
};

a.play  
a.stop

//Vikku Rupaka Tala
//Also good for phrase variations.
a = KonaTani.new(90, #["U","O"], 4, argSynth:\MIDITranscribe);

~vikkuRupaka = KonaTime.new(a);

2.do { |i|
	2.do { |j|
		~vikkuRupaka.add(KonaWord.new(1,4,4,a));		//	Ta
		~vikkuRupaka.add(KonaWord.new(2,4,1,a));		//	Ta	ka
		~vikkuRupaka.add(KonaWord.new(1,4,2,a));		//	Ta	
		~vikkuRupaka.add(KonaWord.new(2,4,2,a));		//	Ta	ka
	};
~vikkuRupaka.add(KonaWord.new(1,4,12,a));
	
};
a.add(~vikkuRupaka)

~myVikkuRupaka = a.gen.vSarvaPhraseAuto
~myVikkuRupaka.postWord
2.do { |i|
	a.add(~myVikkuRupaka);
	a.add(~myVikkuRupaka);
	a.add(KonaWord.new(1,4,12,a));
};
a.play
a.stop

//Vikku Adi Tala misra gati

a = KonaTani.new(80, #["I4","O","O"], 7, argSynth:\MIDITranscribe);

~vikkuMisraTala = KonaTime.new(a);


	~vikkuMisraTala.add(KonaWord.new(2,7,2,a));		//	Ta	din
	~vikkuMisraTala.add(KonaWord.new(1,7,2,a));		//	Ta	
	~vikkuMisraTala.add(KonaWord.new(1,7,1,a));		//	Gu

	~vikkuMisraTala.add(KonaWord.new(2,7,2,a));		//	Ta	din
	~vikkuMisraTala.add(KonaWord.new(1,7,3,a));		//	Ta	

	~vikkuMisraTala.add(KonaWord.new(0,7,4,a));		//	-	-	-	-
	~vikkuMisraTala.add(KonaWord.new(1,7,2,a));		//	Ta	
	~vikkuMisraTala.add(KonaWord.new(1,7,1,a));		//	Gu

	~vikkuMisraTala.add(KonaWord.new(2,7,2,a));		//	Ta	din
	~vikkuMisraTala.add(KonaWord.new(1,7,2,a));		//	Ta	
	~vikkuMisraTala.add(KonaWord.new(1,7,1,a));		//	Gu

	~vikkuMisraTala.add(KonaWord.new(2,7,2,a));		//	Ta	din
	~vikkuMisraTala.add(KonaWord.new(1,7,2,a));		//	Ta	
	~vikkuMisraTala.add(KonaWord.new(1,7,1,a));		//	Gu

	~vikkuMisraTala.add(KonaWord.new(2,7,2,a));		//	Ta	din
	~vikkuMisraTala.add(KonaWord.new(1,7,3,a));		//	Ta	

	~vikkuMisraTala.add(KonaWord.new(0,7,4,a));		//	-	-	-	-
	~vikkuMisraTala.add(KonaWord.new(1,7,2,a));		//	Ta	
	~vikkuMisraTala.add(KonaWord.new(1,7,1,a));		//	Gu

	~vikkuMisraTala.add(KonaWord.new(2,7,2,a));		//	Ta	din
	~vikkuMisraTala.add(KonaWord.new(1,7,3,a));		//	Ta	

	~vikkuMisraTala.add(KonaWord.new(1,7,56,a));	


a.add(~vikkuMisraTala)

~myVikkuMisraTala = a.gen.vSarvaPhraseAuto
~myVikkuMisraTala.postWord
2.do { |i|
	a.add(~myVikkuMisraTala);
	a.add(~myVikkuMisraTala);
	a.add(KonaWord.new(1,7,56,a))
};
a.play
a.stop

//Vikku Khanda Chappu
a = KonaTani.new(84, #["U1", "R", "W", "W", "R"], 4, argSynth:\MIDITranscribe);

~vikkuKhanda = KonaTime.new(a);

2.do { |i|
	2.do { |j|
		~vikkuKhanda.add(KonaWord.new(2,4,1,a));		//	Ta	ka
		~vikkuKhanda.add(KonaWord.new(1,4,2,a));		//	Dim
		~vikkuKhanda.add(KonaWord.new(2,4,1,a));		//	Ta	ka
		~vikkuKhanda.add(KonaWord.new(2,4,2,a));		//	Dim na
	
	};
	~vikkuKhanda.add(KonaWord.new(1,4,20,a))
};

a.add(~vikkuKhanda)

~myVikkuKhanda = a.gen.vSarvaPhraseAuto

2.do { |i|
	a.add(~myVikkuKhanda);
	a.add(~myVikkuKhanda);
	a.add(KonaWord.new(1,4,20,a));
};
a.play
a.stop
//Vikku Adi Tala Khanda Gati

a = KonaTani.new(70, #["I4","O","O"], 5, argSynth:\MIDITranscribe);

~vikkuKhandaGati = KonaTime.new(a);

2.do { |i|
	~vikkuKhandaGati.add(KonaWord.new(0,5,2,a));	//	-	-
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(0,5,2,a));	//	-	-
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(0,5,2,a));	//	-	-
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(0,5,2,a));	//	-	-
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta	-	
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(0,5,2,a));	//	-	-
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(0,5,2,a));	//	-	-
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(0,5,2,a));	//	-	-
	~vikkuKhandaGati.add(KonaWord.new(1,5,1,a));	//	Ta
	~vikkuKhandaGati.add(KonaWord.new(1,5,2,a));	//	Ta - 

	~vikkuKhandaGati.add(KonaWord.new(1,5,8*5,a));	
};

a.add(~vikkuKhandaGati);

~myVikkuKhandaGati = a.gen.vSarvaPhraseAuto
~myVikkuKhandaGati.postWord
2.do { |i|
	a.add(~myVikkuKhandaGati);
	a.add(~myVikkuKhandaGati);
	a.add(KonaWord.new(1,5,8*5,a))
};
a.play
a.stopgsls


and 
a = KonaTani.new(70, #["I4","O","O"], 5, argSynth:\MIDITranscribe);

~vikkuKhandaGati2 = KonaTime.new(a);

~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta	-	
~vikkuKhandaGati2.add(KonaWord.new(1,5,1,a));	//	Ta
~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta - 

~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta	-	
~vikkuKhandaGati2.add(KonaWord.new(1,5,1,a));	//	Ta
~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta - 

~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta	-	
~vikkuKhandaGati2.add(KonaWord.new(1,5,1,a));	//	Ta
~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta - 

~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta	-	
~vikkuKhandaGati2.add(KonaWord.new(1,5,1,a));	//	Ta
~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta - 

~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta	-	
~vikkuKhandaGati2.add(KonaWord.new(1,5,1,a));	//	Ta
~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta - 

~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta	-	
~vikkuKhandaGati2.add(KonaWord.new(1,5,1,a));	//	Ta
~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta - 

~vikkuKhandaGati2.add(KonaWord.new(1,5,2,a));	//	Ta	-	
~vikkuKhandaGati2.add(KonaWord.new(3,5,1,a));	//	Ta
~vikkuKhandaGati2.add(KonaWord.new(5,5,1,a));	//	Ta - 
}


////////////////////////////////////////////////////////////////Phrase Development//////////////////////////////////////////////////
{
//Vikku Khanda Chappu
//Also phrase dev;
a = KonaTani.new(84, #["U1", "R", "W", "W", "R"], 4, argSynth:\MIDITranscribe);


~vikkuMisraTala = KonaTime.new(a);


~clear = {
a.clear;
~vikkuMisraTala = KonaTime.new(a);
~vikkuMisraTala.add(KonaWord.new(0,4,10,a));
2.do { |i|
	~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	ka
	~vikkuMisraTala.add(KonaWord.new(1,4,2,a));		//	Dim
	~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	ka
	~vikkuMisraTala.add(KonaWord.new(2,4,2,a));		//	Dim na
	
};
~vikkuMisraTala.add(KonaWord.new(1,4,20,a));

}
~vikkuMisraTala.play
~original = KonaTime.new(a);
	~original.add(KonaWord.new(2,4,1,a));		//	Ta	ka
	~original.add(KonaWord.new(1,4,2,a));		//	Dim
	~original.add(KonaWord.new(2,4,1,a));		//	Ta	ka
	~original.add(KonaWord.new(2,4,2,a));		//	Dim na
	

~myVikkuMisraTala1 = a.gen.mutatePhrase(~original)
~myVikkuMisraTala2 = a.gen.mutatePhrase(~original)
~myVikkuMisraTala3 = a.gen.mutatePhrase(~original)
~myVikkuMisraTala4 = a.gen.mutatePhrase(~original)

~clear.()
a.add(~vikkuMisraTala)
a.add(~myVikkuMisraTala4)
a.add(KonaWord.new(1,4,20,a));
a.play  
a.stop
//Phrase Dev;
~vikkuMisraTala = KonaTime.new(a);

~vikkuMisraTala.add(KonaWord.new(0,4,2,a));		//	-	-
~vikkuMisraTala.add(KonaWord.new(4,4,1,a));		//	Ki	tah ta ka
~vikkuMisraTala.add(KonaWord.new(2,4,2,a));		//	Din	na


~vikkuMisraTala.add(KonaWord.new(1,4,1,a));		//	Ta
~vikkuMisraTala.add(KonaWord.new(3,4,1,a));		//	Ta ki tah
~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	-	ka	-
~vikkuMisraTala.add(KonaWord.new(2,4,2,a));		//	Din	na

~vikkuMisraTala.add(KonaWord.new(4,4,1,a));		//	Ki	tah ta ka
~vikkuMisraTala.add(KonaWord.new(4,4,1,a));		//	Ki	tah ta ka
~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	ka

~vikkuMisraTala.add(KonaWord.new(0,4,2,a));		//	-	-
~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	ka
~vikkuMisraTala.add(KonaWord.new(4,4,1,a));		//	Ki	tah ta ka
~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	ka

//Vikku Rupaka Tala
//Also good for phrase variations.
a = KonaTani.new(90, #["U","O"], 4, argSynth:\MIDITranscribe);

~fix = {
a.clear;
~vikkuRupaka = KonaTime.new(a);
2.do { |i|
	~vikkuRupaka.add(KonaWord.new(1,4,4,a));		//	Ta
	~vikkuRupaka.add(KonaWord.new(2,4,1,a));		//	Ta	ka
	~vikkuRupaka.add(KonaWord.new(1,4,2,a));		//	Ta	
	~vikkuRupaka.add(KonaWord.new(2,4,2,a));		//	Ta	ka
	
};
~vikkuRupaka.add(KonaWord.new(1,4,24,a));

}

~original = KonaTime.new(a)
	~original.add(KonaWord.new(1,4,4,a));		//	Ta
	~original.add(KonaWord.new(2,4,1,a));		//	Ta	ka
	~original.add(KonaWord.new(1,4,2,a));		//	Ta	
	~original.add(KonaWord.new(2,4,2,a));		//	Ta	ka
	
//Variations
~vikkuRupaka.add(KonaWord.new(0,4,2,a));		//	-	-
~vikkuRupaka.add(KonaWord.new(2,4,1,a));		//	Ta	ka
~vikkuRupaka.add(KonaWord.new(2,4,1,a));		//	Ta	ka
~vikkuRupaka.add(KonaWord.new(1,4,2,a));		//	Ta	
~vikkuRupaka.add(KonaWord.new(2,4,2,a));		//	Ta	ka


~vikkuRupaka.add(KonaWord.new(4,4,1,a));		//	Ta	ka	di	mi
~vikkuRupaka.add(KonaWord.new(2,4,1,a));		//	Ta	ka
~vikkuRupaka.add(KonaWord.new(1,4,2,a));		//	Ta	
~vikkuRupaka.add(KonaWord.new(2,4,2,a));		//	Ta	ka


~vikkuRupaka.add(KonaWord.new(0,4,2,a));		//	-	-
~vikkuRupaka.add(KonaWord.new(4,4,1,a));		//	Ta	ka	di	mi
~vikkuRupaka.add(KonaWord.new(1,4,2,a));		//	Ta	
~vikkuRupaka.add(KonaWord.new(2,4,2,a));		//	Ta	ka

~fix.()
~myVikkuRupaka1 = a.gen.mutatePhrase(~original)
~myVikkuRupaka2 = a.gen.mutatePhrase(~original)
~myVikkuRupaka3 = a.gen.mutatePhrase(~original)

~vikkuRupaka.add(~myVikkuRupaka3)

~vikkuRupaka.add(KonaWord.new(1,4,4,a));
a.add(~vikkuRupaka)
a.play 
a.stop

//Trichy Sankaran, Phrase development with suffix
a = KonaTani.new(90, #["I4", "O", "O"], 4, argSynth:\MIDITranscribe);

a.clear
~trichyDev = KonaTime.new(a);

//Original
~trichyDev = KonaTime.new(a);
~original = KonaTime.new(a);
4.do { |i|
~trichyDev.add(KonaWord.new(2,4,2,a));			//	Na -	Din -
												//	Din	-	Na	-	
~original.add	(KonaWord.new(2,4,2,a))											
};
~trichyDev.add(KonaWord.new(1,4,16,a));

//Development
~trichyDev.add(KonaWord.new(1,4,2,a));			//	Tam
~trichyDev.add(KonaWord.new(2,4,1,a));			//	Ta	ka
~trichyDev.add(KonaWord.new(2,4,2,a));			//	Din	-	na	-
~trichyDev.add(KonaWord.new(2,4,1,a));			//	Ta	ka
~trichyDev.add(KonaWord.new(1,4,2,a));			//	Din
~trichyDev.add(KonaWord.new(2,4,2,a));			//	Din	-	na	-

//With suffix
~trichyDev.add(KonaWord.new(1,4,2,a));			//	Tam
~trichyDev.add(KonaWord.new(2,4,1,a));			//	Ki	Ta
~trichyDev.add(KonaWord.new(2,4,2,a));			//	Din	-	na	-

//Suffix 1
~trichyDev.add(KonaWord.new(4,4,1,a));			//	Na	ka	jo	nu
~trichyDev.add(KonaWord.new(4,4,0.5,a));		//	Ta	ka	jo	nu
~trichyDev.add(KonaTime.newFrom([
	KonaWord.new(1,4,0.5,a),					//	Ta	
	KonaWord.new(1,4,1,a),						//	Lan	
	KonaWord.new(1,4,0.5,a)						//	Gu	
	],a));
~trichyDev.add(KonaWord.new(1,4,16,a));

~var1 = a.gen.mutatePhrase(~original)
~var2 = a.gen.mutatePhrase(~original)
~var2 = a.gen.addSuffix(~var2)
~var2 = ~var2[0]

~var3 = a.gen.mutatePhrase(~original)
~var4 = a.gen.mutatePhrase(~original)
~var4 = a.gen.addSuffix(~var4)
~var4 = ~var4[0]


~trichyDev.add(~var3)
~trichyDev.add(~var4)
~trichyDev.add(KonaWord.new(1,4,16,a));


~trichyDev.play
a.add(~trichyDev)
a.play
a.stop
a.clear
~trichyDev.play
//Suffix 2
~trichyDev.add(KonaWord.new(2,4,1,a));			//	Na	ka	
~trichyDev.add(KonaWord.new(1,4,1,a));			//	Din
~trichyDev.add(KonaWord.new(2,4,0.5,a));		//	Ki	ta
~trichyDev.add(KonaWord.new(1,4,1,a));			//	Din
~trichyDev.add(KonaWord.new(2,4,0.5,a));		//	Ki	ta
~trichyDev.add(KonaWord.new(1,4,1,a));			//	Din
~trichyDev.add(KonaWord.new(1,4,1,a));			//	Ta

//Vellore G. ramabhadran phrase development	
a = KonaTani.new(95, #["I4", "O", "O"], 4, argSynth:\MIDITranscribe);

~velloreDev = KonaTime.new(a);

//Original
~velloreDev.add(KonaWord.new(2,4,2,a));			//	Na	-	din	-	
~velloreDev.add(KonaWord.new(2,4,2,a));			//	Din	-	na	-	
~velloreDev.add(KonaWord.new(2,4,2,a));			//	Na	-	din	-	
~velloreDev.add(KonaWord.new(2,4,2,a));			//	Din	-	na	-	

//Development
~velloreDev.add(KonaWord.new(1,4,2,a));			//	Din	-	
~velloreDev.add(KonaWord.new(2,4,1,a));			//	jo	nu
~velloreDev.add(KonaWord.new(2,4,2,a));			//	Din	-	na	-	
~velloreDev.add(KonaWord.new(4,4,0.5,a));		//	Ta	ri	gi	du
~velloreDev.add(KonaWord.new(1,4,2,a));			//	Din	-	
~velloreDev.add(KonaWord.new(2,4,2,a));			//	Din	-	na	-	

//With suffix
~velloreDev.add(KonaWord.new(2,4,2,a));			//	Na	-	din	-	
~velloreDev.add(KonaWord.new(1,4,2,a));			//	Din	-	
~velloreDev.add(KonaWord.new(2,4,1,a));			//	na	da
~velloreDev.add(KonaWord.new(1,4,1,a));			//	na	
~velloreDev.add(KonaWord.new(1,4,1,a));			//	tom
~velloreDev.add(KonaWord.new(1,4,1,a));			//	tom
~velloreDev.add(KonaWord.new(1,4,1,a));			//	ta
~velloreDev.add(KonaWord.new(1,4,1,a));			//	tom
~velloreDev.add(KonaWord.new(1,4,1,a));			//	tom
~velloreDev.add(KonaWord.new(1,4,1,a));			//	tom
~velloreDev.add(KonaWord.new(1,4,1,a));			//	ta


}

/////////////////////////////////////////////////////////////////////Moras//////////////////////////////////////////////////////////
{

//Mani short mora
a = KonaTani.new(91, #["I4", "O", "O"], 4, argSynth:\MIDITranscribe);

~maniMora1 = KonaTime.new(a);

3.do { |i|
	~maniMora1.add(KonaWord.new(1,4,0.5,a));		// Ta	
	~maniMora1.add(KonaWord.new(6,4,0.25,a));		// Ta ri ki tah ta ka
	~maniMora1.add(KonaWord.new(1,4,1,a));			// Ta		
};

a.clear
a.add(KonaWord.new(1,4,24,a))
a.add(~maniMora1)
a.play
a.stop

~myManiMora = a.gen.randomMora(8,4,0.5,false,false).play
~myManiMora.matras
a.add(KonaWord.new(1,4,24.5,a))
a.add(~myManiMora)
a.add(KonaWord.new(1,4,26,a))

a.play
a.stop
//Raghu Mora

a = KonaTani.new(98, #["I4", "O", "O"], 4, argSynth:\MIDITranscribe);

~raghuMora = KonaTime.new(a);


2.do { |i|
	~raghuMora.add(KonaWord.new(1,4,2,a));				//	Tam
	~raghuMora.add(KonaWord.new(1,4,1,a));				//	Tom
	~raghuMora.add(KonaWord.new(1,4,1,a));				//	Tom
	~raghuMora.add(KonaWord.new(1,4,1,a));				//	Ta
	~raghuMora.add(KonaWord.new(0,4,1,a));				//	-
};
	~raghuMora.add(KonaWord.new(1,4,2,a));				//	Tam
	~raghuMora.add(KonaWord.new(1,4,1,a));				//	Tom
	~raghuMora.add(KonaWord.new(1,4,1,a));				//	Tom
	~raghuMora.add(KonaWord.new(1,4,1,a));				//	Ta
	~raghuMora.add(KonaWord.new(1,4,3,a));				//	-
	

a.clear

~myRaghuMora = a.gen.randomMora(20,4,1,true,false).play

a.add(KonaWord.new(1,4,15,a))
a.add(~myRaghuMora)
a.add(KonaWord.new(1,4,1,a))
a.play 
a.stop 
~raghuMora.matras
~raghuMora.play
~raghuMora.word
//Trichy Big Mora

a = KonaTani.new(90, #["I4", "O", "O"], 4, argSynth:\MIDIPlay);

//Mora1
~trichyMora = KonaTime.new(a);

~statement = KonaTime.newFrom([
	KonaWord.new(2,4,0.5,a),				//Ta	ka
	KonaWord.new(6,4,0.5,a),				//Ta	ri	ki	tah	ta	ka		
	KonaWord.new(2,4,0.5,a),				//Ta	ka
	KonaWord.new(4,4,0.5,a),				//Ta	ri	ki	tah	
	KonaWord.new(1,4,1,a),					//Tom
	KonaWord.new(1,4,1,a),					//Tom
	KonaWord.new(1,4,1,a)					//Ta
	],a);

~gap = KonaTime.newFrom([
	KonaWord.new(1,4,1,a),					//Tom
	KonaWord.new(4,4,0.5,a)					//Ki	tah	ta	ka
	])
	
~trichyMora.addAll(a.gen.createSimpleMora(~statement,~gap,nil));

~trichyMora.add(KonaWord.new(1,4,4,a))		//Tom

~myTrichyMora = a.gen.randomMora(36,4,0.5,true,false).play
~myTrichyMora.matras
16-36%16
a.clear
a.add(KonaWord.new(1,4,0.5,a))
a.add(~myTrichyMora)
a.play
a.stop

//Mora2
a = KonaTani.new(90, #["I4", "O", "O"], 4, argSynth:\MIDITranscribe);

~trichyMora2 = KonaTime.new(a);

~statement = KonaTime.newFrom([
	KonaWord.new(1,4,2,a),					//Ta
	KonaWord.new(2,4,1,a),					//Ki	Ta
	KonaWord.new(4,4,1,a),					//Ta	ka	di	na
	KonaWord.new(1,4,1,a),					//Tom
	KonaWord.new(3,4,1,a),					//Ta	ki	Ta
	KonaWord.new(4,4,1,a),					//Ta	ka	di	na
	KonaWord.new(1,4,2,a),					//Tom
	KonaWord.new(1,4,2,a),					//Ta
	],a);
	
~gap = KonaWord.new(1,4,2,a)					//Tom
~trichyMora2.addAll(a.gen.createSimpleMora(~statement,~gap,nil));
~trichyMora2.add(KonaWord.new(1,4,4,a))		//Ta

~trichyMora2.matras

~myTrichyMora2 = a.gen.randomMora(64,4,1,true,false)
~myTrichyMora2.matras%32
a.clear
a.add(KonaWord.new(0,4,1,a))
a.add(~myTrichyMora2)
a.add(KonaWord.new(1,4,1,a))

a.play
a.stop
//Compound Mora
//Not printed, find strokes
a = KonaTani.new(90, #["I4", "O", "O"], 4, argSynth:\MIDIPlay);

~trichyCompound = KonaTime.new(a);

~statement = KonaTime.newFrom(
	a.gen.createSimpleMora(
		//Statement
		KonaTime.newFrom([
			KonaWord.new(1,4,0.5,a),				//	Ta
			KonaWord.new(1,4,1,a),					//	Din
			KonaWord.new(1,4,0.5,a),				//	Gu		
			KonaWord.new(1,4,1,a)					//	Ta	
			],a),
		//Gap	
		KonaTime.newFrom([
			KonaWord.new(0,4,1,a),					//	-
			KonaWord.new(1,4,2,a),					//	Din
			],a),
		//Offset
		nil
	),
	a
);

~gap = KonaTime.newFrom([
		KonaWord.new(2,4,4,a);						// Ta ----	Din ----
	],a);
	

~trichyCompound.addAll(a.gen.createSimpleMora(~statement,~gap,nil));
~trichyCompound.add(KonaWord.new(1,4,4,a))		//Ta

~trichyCompound.matras
~myTrichyCompound = a.gen.randomSamaCompoundMora(65,4,0.5,true,false).stop
~myTrichyCompound.matras

a.clear
a.add(KonaWord.new(1,4,31,a))
a.add(~myTrichyCompound)
a.add(KonaWord.new(1,4,15,a))
a.play
a.stop

//Mani Compound p.236 of analysis p88(31) of transcription
a = KonaTani.new(91, #["I4", "O", "O"], 7, argSynth:\MIDIPlay);

a = Buffer.alloc(s,44100)
a.free
~maniCompound = KonaTime.new(a);

~innerStatement = KonaTime.newFrom([
	KonaWord.new(1,7,1,a),			//	Ta
	KonaWord.new(1,7,2,a),			//	Di	-	
	KonaWord.new(3,7,1,a)			//	Ki tah tom
	],a);


~statement = KonaTime.newFrom(a.gen.createSimpleMora(~innerStatement, nil, nil),a);
~statement.play

~gap = KonaWord.new(0,7,1,a);	//-	

~maniCompound.addAll(a.gen.createSimpleMora(~statement, ~gap, nil));

~maniCompound.matras

~myManiCompound = a.gen.randomSamaCompoundMora(56,7,1)

~myManiCompound.matras
a.clear
a.add(~myManiCompound)
a.add(KonaWord.new(1,7,7,a))
a.play
a.stop 
~maniCompound.add(1,7,7,a);
~maniCompound.play
}


////////////////////////////////////////////////////////////////Accompaniment////////////////////////////////////////////////////
{
a = KonaTani.new(91, #["I4", "O", "O"], 4, argSynth:\MIDIPlay);

~myAccomp = a.gen.vSarvaPhraseAuto
~myAccomp.play
a.gen.mutatePhrase(~myAccomp).play

q = Routine {
       inf.do {
               a.gen.mutatePhrase(~myAccomp).rout.embedInStream;
       };
};
a.stop
a.play;
q.play(a.clock)
a.stop
q.stop
~maniAccomp = KonaTime.newFrom([
	
	KonaWord.new(1,4,4,a),
	KonaWord.new(1,4,6,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(1,4,4,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(1,4,3,a),
	KonaWord.new(1,4,1,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(1,4,4,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(1,4,6,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(4,4,1,a),
	KonaWord.new(2,4,2,a),
		
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,2,a),

	KonaWord.new(2,4,2,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(2,4,2,a),
	KonaWord.new(2,4,2,a),

	KonaWord.new(2,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(1,4,3,a),
	KonaWord.new(1,4,1,a),

	KonaWord.new(2,4,2,a),
	KonaTime.newFrom([
		KonaWord.new(1,4,1,a), KonaWord.new(1,4,2,a), KonaWord.new(1,4,1,a),
		],a),
	KonaWord.new(2,4,2,a),
	KonaTime.newFrom([
		KonaWord.new(1,4,1,a), KonaWord.new(1,4,2,a), KonaWord.new(1,4,1,a),
		],a),
	KonaWord.new(2,4,2,a),
	KonaTime.newFrom([
		KonaWord.new(1,4,1,a), KonaWord.new(1,4,2,a), KonaWord.new(1,4,1,a),
		],a),

	KonaWord.new(2,4,2,a),
	KonaWord.new(1,4,3,a),
	KonaWord.new(1,4,1,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(2,4,1,a),
	KonaWord.new(1,4,1,a),
	KonaWord.new(1,4,2,a),
	KonaWord.new(1,4,1,a),
	KonaWord.new(4,4,1,a),
	KonaWord.new(4,4,1,a)
],a)

a.add(~maniAccomp)
a.add(KonaWord.new(1,4,4,a))

a.play
a.stop
}
