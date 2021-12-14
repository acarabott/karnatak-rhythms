/* ======================================================================= */
/* = KonaTime Class - Collection class that represents phrases of music  = */
/* ======================================================================= */

KonaTime : List {

	var <tani;		//	The tani this belongs to
	var <tala;		//	The tala of the tani
	var <talaSum;	//	The sum of the tala beats
	var dur;		//	The duration of this instance
	var jatis;		//	The total number of Jatis in this instance
	var matras;		//	The total number of matras in this instance
	var numTalas;	//	The number of talas this instance represents
	var rout;		//	The playback routine
	var gati;		//	The gati of the first object. In most cases	
					//	the gati will be the same for all objects, 
					//	but for experimental work this might be mixed.

	*new {|argTani|
		^super.new.konaTimeInit(argTani);
	}
	
	//Create a new instance from a given collection of KonaItems
	*newFrom{|aCol, aTani|
		var tani;
		var ret;

		tani = aTani;
		ret  = this.new(tani);

		aCol.size.do { |i|
			ret.add(aCol[i])
		};

		^ret
	}

	*fill{ |size, function, argTani|
		var newCollection = KonaTime.new(argTani);
		size.do { |i|
			newCollection.add(function.())
		};
		^newCollection;
	}

	konaTimeInit {|argTani|
		tani = argTani;
		if(tani!=nil,
			{
				tala = tani.tala;
				talaSum = tala.sum;
			}
		);

	}
	
	//Add an item
	add {|argItem|
		//Ensure against non-KonaItems
		if(argItem.class==KonaWord || (argItem.class==KonaTime)) {
			if(argItem==this) {
				super.add(KonaTime.newFrom(argItem, argItem.tani))
			} {
				super.add(argItem);
			};
		};
		//Accent the first item in the phrase
		if(this.size==1) {
			this.accentFirst;
			gati = this[0].gati;
		};
	}

	//Add a collection
	addAll { |argCollection|
		super.addAll(argCollection);
	}

	//Duration getter method
	dur {
		var ret = 0;
		this.do { |item, i|
			ret = ret + item.dur;
		};
		^ret;
	}

	//Duration getter method for all contained objects
	allDurs {
		var allDurs = Array.newClear(this.size);

		this.do{ |item, i|
			if(item.class==KonaWord) {
				allDurs[i] = Array.fill(item.jatis, item.speed);
			} {
				allDurs[i] = item.allDurs
			};
		}

		^allDurs;
	}

	//Jatis getter method
	jatis {
		jatis = 0;
		this.size.do { |i|
			jatis = jatis + this[i].jatis;
		};
		^jatis
	}

	//Matras getter method
	matras {
		matras = 0;
		this.size.do { |i|
			matras = matras + this[i].matras;
		};
		^matras;
	}

	//Karve getter method, returns the mode karve of the instance.
	karve {
		var karves = this.collect({|item, i| item.karve});
		
		^karves.maxItem({|item, i| karves.occurrencesOf(item)})		
	}
	
	//Returns the number of cycles this instance occupies
	numTalas {
		numTalas = 0;
		^numTalas = this.dur/this.talaSum;
	}

	//The playback routine for this instance
	rout {
		rout = Routine.new({});
		this.do { |item, i|
			rout = rout++item.rout
		};
		^rout;
	}

	//Play the instance routine to the parent KonaTani's clock.
	play {
		this.rout.play(tani.clock);
	}


	//Concatonation method for combining KonaItems
	++ { |aKonaItem|
		var newTime = KonaTime.new(tani);
		newTime.addAll(this).addAll(aKonaItem);
		^newTime
	}
	
	//Getter method for the word of all of the contained objects
	word {
		var word = List[];
		this.do({|item, i| word.add(item.word)});
		^word.asArray;
	}
	
	//Method for printing the word and timing of all contained objects
	postWord {|argW=true, argD=true, argF=true|
		var words, decimals, fractions;
		var get;
		this.do {|item, i|
			get = item.postWord(false, false, false);
			words = words ++ get[0];
			decimals = decimals ++ get[1];
			fractions = fractions ++ get[2];
		};

		if(argW) {
			words.postln;
		};
		if(argD) {
			decimals.postln;
		};
		if(argF) {
			fractions.postln;
		};
		"".postln;
		^[words, decimals, fractions];
	}

	//Method to accent the first syllable of the first item in the KonaWord
	//Works recursively on KonaTimes
	accentFirst {
		if(this[0]!=nil) {
			this[0].accentFirst;
		};
	}

	//Method to return the maximum speed in this KonaTime, works recursively 
	//	on KonaTimes.
	speed {
		var ret = this[0].speed;
		this.do { |item, i|
			if(item.speed < ret) {
				ret = item.speed;
			};
		};
		^ret;
	}

	//Getter method, returns the gati of the first object.
	gati {
		^this[0].gati
	}

	//Method to return the largest word in number of jatis (works recursively 
	//	for KonaTimes)
	greatestJatis {
		var ret = 0;
		var val;
		this.do { |item, i|
			if(item.class==KonaWord) {
				val = item.jatis;
			} {
				val = item.greatestJatis;
			};

			if(val>ret) {ret = val};
		};

		^ret;
	}
	
	//Overridden reverse method to include passing the tani into the resulting
	//	reversed KonaTime
	reverse {
		^this.class.newFrom(this.asArray.reverse, tani)
	}
}

