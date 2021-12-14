/* ================================================================== */
/* = KonaGenerator Class - Class to generate and manipulate rhythms = */
/* ================================================================== */

KonaGenerator {
	var tani;			//	The Tani that this KonaGenerator belongs to
	var laya;			//	The Laya (tempo) of the tani
	var <>gati; 		//	The current Gati (beat subdivision) of the Tani
	var <gatiPowers;	//	The series of powers belonging to the gati;
	var <tala;			//	The Tala of this Tani
	var <>pMax;	//Maximum perceptual time for a motif. May make this method specific

	*new {|argTani, argLaya, argTala, argGati=4|
		^super.new.konaGeneratorInit(argTani, argLaya, argTala, argGati);
	}

	konaGeneratorInit {|argTani, argLaya, argTala, argGati|
		tani = argTani;
		laya = argLaya;
		tala = argTala;
		gati = argGati;
				
		gatiPowers = this.setGatiPowers(argGati);

		pMax = 5;		//Rough perceptual present in seconds 
	}

	//	setGatiPowers
	//	Method to create the series of values to which the gati belongs
	//
	//	e.g. Series for gati 3 = [3, 6, 12, 24, 48, 96, 192]
	//	Special circumstances for gati 4 to include 2 = [2, 4, 8, 16, 32, 64, 128]
	setGatiPowers {|aGati|
		var powers;
				
		if(aGati==4,
			{powers = List[(aGati/2).asInteger]},
			{powers = List[(aGati).asInteger]}
		);
		10.do { |i|
			powers.add( (powers[powers.size-1]*2).asInteger)
		};
		
		^powers.asArray;
		
	}

	/* ======================================================= */
	/* =            Generation Methods                       = */
	/* ======================================================= */

					/* = Maths Methods = */

	//	allPartitions
	//	@n		Total number of beats to partition
	//	@min	Minimum part size
	//	@max	Maximum part size
	//
	//	-Method to generate all partitions and permutations of an integer (duration)
	//	-Uses ZS2 Algorithm from "Fast Algorithms For Generating Integer Partitions" 
	//		by Zoghbi and Stojmenovic
	//	-Doubly restricted by default to 2 and 9, but the mininum 
	//		restriction is available as an argument
	//	-This is because of the restriction on Konakkol word size

	allPartitions { |aNum, aMin=2, aMax, aUParts|
		var tmax; 	//	The maxmimum size a part of a partition may be
		var x;		//	An array to store each new partition in;
		var h;		//	The index of the last part of partition that is > 1
		var m;		//	The number of parts in a partition
		var j;		//	The index of the next part to be increased
		var r;		//	A variable used to calculate the next m
		var partition; 	//	A freshly baked partition
		var add;		//	Boolean; whether this partition should be added.
		var ret;		//	The array to be returned. 
		var readFunc;	//	Function to read partitions from a file.
		var n, min, max;	//vars for aNum aMin and aMax.

		//Ensure against floats.
		n = aNum.asInteger;
		min = aMin.asInteger;
		readFunc = {|val| Object.readArchive(Platform.userExtensionDir++"/FYPClasses/partitions/"++val.asString)};

		case
			//There are no partitions of 1
			{n==1}				{^[[1]]}
			//If n==2 and min==2 there are no partitions of
			{n==2 && (min==2)}	{^[[2]]}
			{n>=40}				{
				if(aUParts==nil) {
					^readFunc.(n);
				} {
					^this.removeGreaterThan(readFunc.(n), aUParts);
				};
			};

		ret = List[];
		//Fill the array with n 1s
		x = Array.fill(n, 1);
		//Add the array as it forms the first partition
		if(min==1) {
			ret.add(x[0..n]);
		};
		//Alter x; set the second element ([1]) to 2 and add the subarray x[1..n]
		x[1] = 2;
		if(min==1) {
			ret.add(x[1..n]);
		};

		h = 1;
		m = n-1;

		//If the max argument is not set, use n if below 9, else use 9
		if( aMax == nil ) {
			if(n>9) {
				tmax = 9;
			} {
				tmax = n;
			};
		} {
			//Else if the argument is n-1 or n, use the argument as given
			if(aMax >= (n-1)) {
				tmax = aMax.asInteger;
			} {
				//Else add 1 to the argument.
				//This is to ensure that the maximum argument works correctly
				//If used as given, a max arg of 3 would return results up to size 3
				tmax = aMax.asInteger+1;
			};

		};

		//Generate further partitions
		while({x[1] != tmax},
			{
				if( (m-h) > 1) {
					h = h+1;
					x[h] = 2;
					m = (m-1);
				} {
					j = (m-2);

					while({x[j] == x[m-1]},
						{
							x[j] = 1;
							j = (j-1);
						}
					);
					h = (j+1);
					x[h] = (x[m-1] +1);
					r = (x[m] + ((x[m-1])*(m-h-1)));

					x[m] = 1;

					if( (m-h) > 1 ) {
						x[m-1] = 1
					};
					m = (h + (r-1));

				};
				partition = x[1..m];
				//If a maximum number of unique parts has been set
				if(aUParts!=nil) {
					//If the number of unique parts is acceptable.
					if(partition.asSet.size<=aUParts) {
						add = true
					} {
						add = false
					};
				} {
					add = true;
				};

				if(partition.minItem >= min && (add==true)) {
					ret.add(partition);
				};

		});

		^ret.asArray;

	}

	//	randomPartition
	//	Method to choose a random partition
	//
	//	@duration 	number of beats for the partition
	//	@min 		minimum part size
	//	@max		maximum part size
	//	@notSize	boolean, true means partition of 1 part equal to size will
	//					not be returned. E.g. duration 4 can't return partition [4]
	//	@seed		seed for random selection

	randomPartition { |duration, min=2, aMax, notSize=false, seed|
		var allPartitions;
		var max;

		if(seed!=nil) {
			thisThread.randSeed=seed
		};

		max = aMax ?? {if(duration>9) {9} {duration}};

		allPartitions = this.allPartitions(duration, min, max);

		if(notSize && (allPartitions.size>1)) {

			allPartitions.do { |item, i|
				if(item[0]==duration) {
					allPartitions.removeAt(i)
				};
			};
		};

		^allPartitions.choose;
	}

	//	allPerms
	//	Method to generate all permutations of a partition
	//
	//	@aCollection	The partition array to generate permutations of

	allPerms {|aCollection|
		var col;	//Collection to permute
		var ret;	//Array to return permutations
		var perm;	//Temp variable for storing permutation
		col = aCollection;
		ret = List[];

		//If the partition is not just made up of 1 unique number (e.g. [2,2,2])
		if(col.occurrencesOf(col[0]) != col.size) {
			//Loop to create all permutations
			col.size.factorial.asInteger.do { |i|
				perm = col.permute(i);
				//If ret doesn't already contain the new permutation
				if(not(ret.any { |item, i| item.asArray == perm	})) {
					//Add it
					ret.add(col.permute(i));
				};
			};
		} {
			//Else (if the partition IS made up of just 1 unique number)
			ret.add(col);
		};

		//Return all partitions
		^ret.asArray;
	}
 
	//	randomPerm
	//	Method to choose a random permutation
	//
	//	@partition 	Partition to generate permutations from
	//	@seed		seed for random selection
	randomPerm { |partition, seed|
		var permutation;
		if(seed!=nil) {
			thisThread.randSeed=seed
		};

		permutation = (partition.size+1).factorial.asInteger.rand;
		
		^partition.permute(permutation);
	}

	//	removeGreaterThan
	//	Method to remove all partitions from a collection 
	//		that have more than a given number of unique parts
	//
	//	@aCollection	Collection to remove partitions from
	//	@val			Maximum number of unique partitions
	removeGreaterThan {|aCollection, val, weight=0.97|
		var col;		//	Instance collection
		var temp;		//	Temporary list for checking partitions

		col = aCollection;
		temp = List[];
		col.do { |item, i|
			if(item.asSet.size>val) {
				temp.add(i)
			};
		};
		col = col.removeAtIndexes(temp);
		^col;
	}

	//	removeThoseContaining
	//	Method to remove all partitions from a collection 
	//		that contain certain values
	//	Individual weights can be passed for probabalistic results
	//
	//	@aCollection	Collection to remove paritions from
	//	@valCol			Collection of taboo values
	//	@weightCol		Collection of weights for taboo values
	removeThoseContaining {|aCollection, valCol, weightCol|

		var col;		//	Partitions
		var vCol;		//	Values
		var wCol;		//	Weights
		var inds;		//	Indexes of partitions to remove
		var saveIndex;

		col = aCollection;

		vCol = valCol;
		
		//If no weights are supplied, remove is guaranteed
		wCol = weightCol ?? {Array.fill(vCol.size, 1)};
		
		inds = List[];
		//For each forbidden value
		vCol.size.do { |i|
			//Check for partitions that include the value
			col.do { |jtem, j|				
				if(jtem.includes(vCol[i])) {
					//Store the index depending on given weight
					if(wCol[i].coin) {
						inds.add(j);
					};
				};
			};
		};
		inds = inds.asSet.asArray.sort;
			
		//If all partitions are to be removed, select one at random to keep
		if(inds.size==col.size) {
			//Store the index of value least likely to be removed
			saveIndex = wCol.indexOf(wCol.minItem);
			//Select an index from those partitions that include 
			//	the least likely value
			saveIndex = inds.select({|item, i|
				col[i].includes(vCol[saveIndex]);
			}).choose;
			
			inds.removeAt(saveIndex);
		};
		col.removeAtIndexes(inds.asArray)
	
		//Return updated collection
		^col
	}

	//	partsToWords
	//	Method to turn a partition array into KonaWords
	//
	//	@aPartitionArray	Partition Array
	//	@aOne				Boolean; if KonaWords can be 1 syllable
	//	@aMult				Boolean; if Konawords can have syllables == part size
	partsToWords {|aPartitionArray, aKarve, aOne=true, aMult=true|
		var partitionArray;
		var one, mult;
		var ret;
		var chance;
		var jatis, karve;

		partitionArray = aPartitionArray;
		one = aOne;
		mult = aMult;
		case
			{aOne==true && (aMult==true)}		{chance = 0.5}
			{aOne==true && (aMult==false)}	{chance = 1}
			{aOne==false && (aMult==true)}	{chance = 0}
			{aOne==false && (aMult==false)}	{chance = 0.5};

		ret = KonaTime.new(tani);

		aPartitionArray.size.do { |i|
			if(chance.coin) {
				jatis = 1;
				karve = aPartitionArray[i];
			} {
				jatis = aPartitionArray[i];
				karve = 1;
			};
			ret.add(KonaWord.new(jatis, gati, karve*aKarve, tani))
		};
		^ret
	}

											/* ================= */
											/* = Music Methods = */
											/* ================= */


	//	vSarvaPhraseLength
	//	Method to determine the phrase length (in beats) 
	//		for sarvalaghu patterns for the Vilamba Kala section
	//	Uses a tweaked perceptual present model, 
	//		currently uses a window of 5 seconds.
	vSarvaPhraseLength {
		var phraseLength;
		var oneBeat;
		var maxBeats;
		var val;
		//Time in seconds for one beat
		oneBeat = 60/laya;
		Post << "oneBeat: " <<  oneBeat << "\n"; 
		
		//The number of beats that can fit into the maximum perceptual time
		//With a maximum number of beats of 5. Even if perceptual time is 3 seconds, 
		//	phrases are not usually longer than this
		maxBeats = (pMax/oneBeat).min(5);
		Post << "maxBeats: " <<  maxBeats << "\n"; 
		
		//	This algorithm attempts to find that largest phrase length that 
		//	fits neatly into a full cycle. 
		//	At the moment this only works in terms of half/quarter/eigth cycles etc
		//	Could be adapted to find other durations of phrase that 
		//	can fit neatly into a cycle
		//	E.g. a 9 beat tala could be made up of 3 * 3 beat phrases
		//	Not a huge amount of material to support this theory.

		phraseLength = 0;
		val = tala.sum;

		while({phraseLength==0},
			{
				phraseLength = maxBeats-(maxBeats%val);
				val = val/2;
			}
		);
		
		Post << "phraseLength: " <<  phraseLength << "\n"; 
		
		^phraseLength
	}

	//	vSarvaPhraseAuto
	//	Automation of vSarvaPhrase
	vSarvaPhraseAuto {

		"vSarvaPhraseLength*gati: ".post; (this.vSarvaPhraseLength*gati).postln;
		
		^this.vSarvaPhrase(this.vSarvaPhraseLength*gati);
	}

	//	vSarvaPhrase
	//	Method to generate a phrase for the Vilamba section sarvalaghu
	vSarvaPhrase {|phraseDur, aMin=2|
			var phraseMatras;
			var jatiParts;
			var min;
			var max;
			var partsArray, weights, maxW, maxW1, maxW2, maxW1MI;
			var muteChance;
			var ret;

			if(phraseDur%1!=0) {
				ret = KonaTime.new(tani);
				ret.add(this.vSarvaPhrase(phraseDur.floor, aMin));
				ret.add(KonaWord.new(1, gati, (phraseDur-phraseDur.floor)));
				^ret;
			};
			
			phraseMatras = phraseDur;
			
			if(phraseDur<aMin) {
				min = phraseDur;
			} {
				min = aMin;
			};
			
			if(2*gati<=phraseDur) {
				max = gati;
			} {
				max = phraseDur;
			};
			
			//Possible part sizes
			partsArray = (min..max);			
			Post << "partsArray: " <<  partsArray << "\n"; 
			
			//Parts 2 to gati. Given heaviest weightings
			weights = Array.fill(partsArray.size, 0);

			weights.size.do { |i|

				if(this.gatiPowers.includes(partsArray[i]),
					{weights[i] = 1.5},
					{weights[i] = 0.4}
				);

				if(partsArray[i]<gati) {
				  weights[i] = weights[i] + 0.25;
				} {
					if(partsArray[i]!=gati) {
				  		weights[i] = weights[i] - 0.4
					};
				};

				if(gati==5 || (gati==7)) {
				  if((partsArray[i]== 3) || (partsArray[i]== 2)) {
				    	weights[i] = weights[i] + 0.25;
					} {
						weights[i] = weights[i] - 0.25
					};
				};

				if(weights[i]<0,
					{weights[i] = 0}
				);

			};
			//Scale and invert values.
			weights = (weights/weights.maxItem-1).round(0.01).abs;
			
			jatiParts = this.allPartitions(phraseMatras.asInteger, aMax: max);
			
			jatiParts = this.removeGreaterThan(jatiParts, 4);	
			
			jatiParts = this.removeThoseContaining(jatiParts, partsArray, weights);
			
			jatiParts = jatiParts.choose;
			
			jatiParts = this.randomPerm(jatiParts);
			
			ret = KonaTime.new(tani);
			jatiParts.size.do { |i|
				if((jatiParts[i].even && (jatiParts[i]>2)) && 0.75.coin ) {
					ret.add(KonaWord.new(jatiParts[i]/2, gati, 2, tani))
				} {
					ret.add(KonaWord.new(1, gati, jatiParts[i], tani))
				};
			};
			"Conversion to KonaWords: ".postln;ret.postWord(true, true, false);
			
			ret = this.combineSimilar(ret, 2, 4, 0.9);
			"combineSimilar: ".postln; ret.postWord(true, true, false);
			
			muteChance = 0.75;
			3.rand.do { |i|
				if(muteChance.coin) {
					ret = this.randomMuteJati(ret);
				};
				muteChance = muteChance/2;
			};
			"randomMuteJati: ".postln; ret.postWord(true, true, false);
			
			^ret;

	}

	//	mutatePhrase
	//	Method to mutate a given phrase using many possible 
	//		combinations of automated manipulation methods
	//
	//	@aKonaItem Item to manipulate;
	mutatePhrase {|aKonaItem, aChance, aRec=0.75, aNum|
		var col;		//	Input collection
		var ret;		//	Output collection
		var change;		//	The chance an item will be mutated;
		var min;		//	Minimum value for alteration
		var max;		//	Maximum value for alteration
		var val;		//	Variable used to calculate density possibilties
		var count;		//	Variable used when calculating density possibilities
		var index;		//	Index of element to mutate
		var store;		//	Array to store indexes to be removed (atDensity)
		var num;		//	Index of process to use;

		if(aKonaItem.class==KonaTime) {
			col = aKonaItem;
		} {
			col = KonaTime.newFrom([aKonaItem], tani);
		};

		change = aChance ?? {(1/col.size)*1.5};
		if(aNum==nil) {
			num = {5.rand}
		} {
			num = {aNum}
		};
		ret = KonaTime.new(tani);
		col.size.do { |i|
			if(change.coin,
				{
					if(col[i].class==KonaWord) {
					switch (num.())
						{0}	{
								ret.add(this.randomAtDensity(col[i]));
							}
						{1}	{
								ret.add(this.randomExtendJati(col[i]));
							}
						{2}	{
								ret.add(this.randomMuteJati(col[i]));
							}
						{3}	{
								ret.add(this.randomDensityJati(col[i]));
							}
						{4}	{
								ret.add(this.partitionWord(col[i]));
							};
					} {
						ret.add(this.mutatePhrase(col[i], aRec=aRec/4))
					};

				},
				{
					ret.add(col[i])
				}
			);
		};

		//Possible recursion for more mutation.
		if(aRec.coin) {
			^this.mutatePhrase(ret, aRec:aRec/2, aNum:aNum)
		} {
			^ret
		};
	}

	//	vSarvaStat
	//	A method for generating Sarva Laghu material based on 
	//		a statistical analysis of a performance by Trichy Sankaran;
	//	Currently only works with an n of 1, no context.
	vSarvaStat {
		var stats;
		var ret;

		ret = KonaTime.new(tani);

		stats = [
			100, 37.5, 87.5, 68.75,
			93.75, 12.5, 100, 25,

			100, 80, 100, 13,
			100, 13, 100, 6,

			100, 0, 73, 53,
			100, 0, 100, 22,

			89, 66, 100, 22,
			100, 30, 80, 30
		];

		stats.size.do { |i|
			if((stats[i]/100).coin) {
				ret.add(KonaWord.new(1,4,1,tani))
			} {
				ret.add(KonaWord.new(0,4,1,tani))
			};
		};

		^ret
	}

	/////////////////////////	Moras	/////////////////////////
	
	//	createSimpleMora
	//	Builds a mora structure from a given statement 
	//		with optional gap and offset.
	//
	//	@statement	KonaObject for Statement
	//	@gap		KonaObject for Gap
	//	@offset		KonaObject for Offset
	createSimpleMora {|statement, gap, offset|
		
		var mora = KonaTime.new(tani);

		if(offset!=nil,
			{mora.add(offset)}
		);

		2.do {
			mora.add(statement);
			if(gap!=nil,
				{mora.add(gap)}
			);
		};
		mora.add(statement);

		^mora
	}
	
	//	moraStatement
	//	Method to generate a mora statement 
	//
	//	@statePulses	Statement jatis
	//	@aGati		Gati of the statement
	//	@aKarve		Karve to use
	moraStatement {|aStateMatras, aGati, aKarve|
		var statePulses;
		var statement;
		var ret;
		var temp;

		
		statePulses = aStateMatras*(1/aKarve);
		
		//Turn statements into KonaItems
		//If the statement duration can be a single word
		if(statePulses<=9) {
				statement = KonaWord.new(statePulses, aGati, aKarve, tani);
		} {
			//If a statement duration requires more than a single word
			//Generate a partition
			statement = this.randomPartition(statePulses.asInteger);
			//Choose a permutation
			statement = this.randomPerm(statement);

			//Convert to KonaTime
			ret = KonaTime.new(tani);

			statement.size.do {|i|
				//New word jatis equal to part duration
				temp = KonaWord.new(statement[i], aGati, aKarve, tani);
				ret.add(temp);
			};
			statement = ret;
		};
		//statement = this.partitionWord(statement);
		if(0.5.coin) {
			statement = this.randomDensityJati(statement);
		};
		
		^statement;
	}

	//	moraGap
	//	Method to generate a mora gap.
	//
	//	@gapPulses		Statement jatis
	//	@aGati		Gati of the statement
	//	@aKarve		Karve to use
	moraGap {|aGapMatras, aGati, aKarve|
		var gapPulses;
		var gap;
		var temp;

		gapPulses = aGapMatras*(1/aKarve);
		
		if(gapPulses==0) {
			gap=nil;
		} {
			if(gapPulses>4 && 0.95.coin) {
				gap = this.randomPartition(gapPulses.asInteger, notSize:true);
				gap = this.randomPerm(gap);

				temp = KonaTime.new(tani);
				gap.size.do { |i|
					if(i==0) { 
						temp.add(KonaWord.new(1, aGati, gap[i]*aKarve, tani))
					} {
						temp.add(KonaWord.new(gap[i], aGati, aKarve, tani));
					};
				};
				//gap = this.mutatePhrase(temp);
				gap = temp;
				
			} {
				//Generate single jati gap with gapPulses duration
				if(aKarve>=0.25 && (0.5.coin)) {
					gap = KonaWord.new(0, aGati, gapPulses*aKarve, tani)
				} {
					gap = KonaWord.new(1, aGati, gapPulses*aKarve, tani)
				};
			};
		};

		^gap
	}

	//	moraOffset
	//	Method to generate a mora offset
	//
	//	@offsetPulses	Offset jatis
	//	@aGati		Gati of the statement
	//	@aKarve		Karve to use
	moraOffset {|aOffsetMatras, aGati, aKarve|
		var offsetPulses;
		var offset = nil;
		var phraseMin;		//Minimum part size if the offset is to be a phrase.
		
		
		offsetPulses = aOffsetMatras*(1/aKarve);
		
		if(offsetPulses!=0) {
			case
				//If the offset is greater than 2 beats, use a phrase
				{offsetPulses>(aGati*2)}	{
					if(offsetPulses>20) {
						phraseMin = 4
					} {
						phraseMin = 2;
					};
					offset = this.vSarvaPhrase(aOffsetMatras, aMin:phraseMin);
					
				}
				//If the offset is less than 2 beats, has a 0.05 chance articulation.
				{0.05.coin}	{
					offset = KonaWord.new(offsetPulses, aGati, aKarve, tani)
				}
				//Else a single syllable word is used.
				{true}	{
					offset = KonaWord.new(1, aGati, aOffsetMatras, tani)
				};

		} {
			offset = nil;
		};
		
		^offset
	}
	
	//	randomMoraValues
	//	Calculation of mora values (statement, gap, offset durations).
	//
	//	@aMatras	The duration of the mora in matras
	//	@aGati		The gati of the mora elements
	//	@aGati		The karve of the mora elements
	//	@aGap		Boolean, gaps or not, overridden for certain durations.
	//	@aOffset	Boolean, offset or not
	randomMoraValues {|aMatras, aGati, aKarve, aGap=true, aOffset=true|
		var pulses;
		var stateMin, gapMin;
		var stateMax, gapMax;
		var gapArray, gapWeights;
		var stateMatras, gapMatras, offsetMatras;
		var totalStateMatras, totalGapMatras;
		
		pulses = aMatras*(1/aKarve);

		//	Nelson 2008 p 23
		//	'It is a practical fact of Karnatak rhythmic behaviour that if a mora 
		//	statement is shorter than five pulses, its gap will nearly always be 
		//	at least two pulses'.
		//	This is impossible if a duration of less than 7 is used.
		//	In this instance a mora with the same duration, 
		//	but using double the jatis and half the karve is returned
		//	Moras under 2 whole beats are also given a chance of being altered.
		
		if(pulses<7 || (aMatras/aGati<=2 && 0.25.coin && (aKarve>0.5))) {
			^this.randomMoraValues(aMatras, aGati, aKarve/2, aGap, aOffset);
		};
		
		//Any duration under 15 will result in statements less than 5 pulses
		//	so requires a minimum gap of 2
		if(pulses<15) {
			gapMin = 2;
		} {
			gapMin = 0;
		};
		
		//	Calculate the mininum matras for the statements. 
		//	If might be no gap, use a minimum size of 1/4 of the total mora duration
		if(gapMin==0) {
			stateMin = (pulses/(3.00, 3.05..4.00).choose).asInteger
		} {
			stateMin = (pulses/5).asInteger
		};
		
		//Calculate the maximum possible statement size
		stateMax = (pulses-(gapMin*2)/3).asInteger;
		
		//Select a statement duration
		stateMatras = (stateMin..stateMax).choose;
		totalStateMatras = stateMatras*3;

		if(aGap) {
			//Calculate the maximum possible gap size.
			gapMax = (pulses-totalStateMatras)/2;

			gapArray = (gapMin..gapMax);

			//Calculate weights for gap matras selection, with bias for smaller gaps.
			gapWeights = (gapArray.size..1).normalizeSum;

			//Choose a gap duration
			gapMatras = gapArray.wchoose(gapWeights);
		} {
			gapMatras = 0;
		};
		totalGapMatras = gapMatras*2;

		//If there should be an offset, calculate the duration
		if(aOffset) {
			offsetMatras = pulses - totalStateMatras - totalGapMatras;
		} {
			offsetMatras = 0;
		};
				
				
		^[stateMatras, gapMatras, offsetMatras, aKarve];
	}

	//	randomMora
	//	Generation of a mora from given parameters;
	//	
	//	@aMatras	The duration of the mora in matras
	//	@aGati		The gati of the mora elements
	//	@aGap		Boolean, whether there should be gaps or not
	//	@aOffset	Boolean, whether there should be an offset or not
	randomMora {|aMatras, aGati, aKarve, aGap=true, aOffset=true|
		var values;
		var statement, gap, offset;
		
		values = this.randomMoraValues(aMatras, aGati, aKarve, aGap, aOffset);
		

		//Convert statements/gaps/offset into KonaItems

		statement = this.moraStatement(values[0]*values[3], aGati, values[3]);
		
		
		gap = this.moraGap(values[1]*values[3], aGati, values[3]);
		if(gap!=nil) {
		};
		
		
		offset = this.moraOffset(values[2]*values[3], aGati, values[3]);
		if(offset!=nil) {
		};
		
		
		^this.createSimpleMora(statement, gap, offset);
	}
	
	//	Generative method to create a mora from a given statement, 
	//		with optional maximum mora size, gap and offset.
	//	Differs from createSimpleMora in that gaps and offsets will 
	//		be calculated and generated if possible
	//
	//	@aStatement		Kona object to use for statement
	//	@aMoraMatras	Total maximum number of matras, overidden if less 
	//						than sum of aStatement, aGap, aOffset matras
	//	@aGap			Kona object to use for gap
	//	@aOffset		Kona object to use for offset
	moraFrom {|aStatement, aMoraMatras, aGap, aOffset|
		var statement, gap, offset;
		var objArray;
		var objMatras, moraMatras, gapMatras, offsetMatras;
		
		statement = aStatement;
		gap = aGap;
		offset = aOffset;
		
		objMatras = 0;
		objArray = [statement,gap,offset];
		//Calculate total matras of mora sections passed as arguments
		objArray.do { |item, i|
			var val;
			if(item!=nil && (item!=false)) {
				switch (item)
					{statement}	{val = 3}
					{gap}		{val = 2}
					{offset}	{val = 1};
				
				objMatras = objMatras + (item.matras*val);
			};
		};
		
		//If no maximum duration has been given
		if(aMoraMatras==nil || (aMoraMatras ? 0 <objMatras) ) {
			
			//Calculate the new maximum duration
			moraMatras = objMatras;
			if(gap==nil || (gap==false)) {
				gapMatras=0;
			} {
				gapMatras=gap.matras
			};
			if(offset==nil) {
				offsetMatras=0;
			} {
				offsetMatras = offset.matras;
			};
		} {
			//If a maximum duration has been given
			moraMatras = aMoraMatras;
			
			//Calculate the lengths of the various sections.
			//Various cases of passed in gaps and offset.
			case
				{gap==nil && (offset==nil)}	{
					gapMatras = (0..(moraMatras-objMatras)/2).choose;
					objMatras = objMatras + (gapMatras*2);				
					offsetMatras = moraMatras - objMatras;
					objMatras = objMatras + offsetMatras;
				}
				{gap==nil && (offset!=nil)}	{
					offsetMatras = offset.matras;
					gapMatras = (0..(moraMatras-objMatras)/2);
					objMatras = objMatras + gapMatras*2;
				}
				{gap!=nil && (gap!=false) && (offset==nil)}	{
					if(gap!=false) {
						gapMatras = gap.matras;					
					};
					offsetMatras = moraMatras - objMatras;
					objMatras = objMatras + offsetMatras;
				}
				{gap!=nil && (gap!=false) && (offset!=nil)} {
					gapMatras = gap.matras;
					offsetMatras = offset.matras;
				};
		};
		
		//If no gap has been set, create one.
		gap = gap ?? {this.moraGap(gapMatras, statement.gati, statement.karve)};
		//If there should be no gap, set it to nil
		if(gap==false) {
			gap = nil;
		};
	
		//If no offset has been calculate the duration and create one
		if(offsetMatras==nil) {
			if(offset==nil) {
				offsetMatras = moraMatras - objMatras;
			} {
				offsetMatras = offset.matras;
				
			};
		};
		offset =  offset ?? {this.moraOffset(offsetMatras, statement.gati, statement.karve)};
		
		//Construct and return the mora
		^this.createSimpleMora(statement, gap, offset);
	}

	//	randomSamaCompoundMora
	//	Generate a random compound mora with the 'sama' shape
	//
	//	@aMatras	Duration in matras
	//	@aGati		The gati to use
	//	@aKarve		The karve to use
	randomSamaCompoundMora {|aMatras, aGati, aKarve|
		var values;
		var stateDur, gapDur, offsetDur;
		var statement, gap, offset;
		
		values = this.randomMoraValues(aMatras, aGati, aKarve, true, true);
		
		stateDur = values[0];
		
		gapDur = values[1];
		
		statement = this.randomMora(stateDur*values[3], aGati, values[3], true, false);
		
		if(gapDur==0) {
			gap = false;
		} {
			gap = this.moraGap(gapDur*values[3], aGati, values[3]);
		};
		
		^this.moraFrom(statement, aMatras, gap);		
	}
	
	//	basicStructure
	//	A method to generate a basic structure based on the tala. 
	//	The purpose is to get a feel for the system's components in a context
	//	Always follows the same structure:
	//	First Cycle:		Basic phrase and a development with suffix
	//	Second Cycle:		More developments of the basic phrase
	//	Third Cycle:		Basic phrases with a half cycle mora
	//	Fourth Cycle:		Developed phrases
	//	Fifth/Sixth Cycle:	Compound Mora
	//	Remaining Cycles:	Play previous six cycles in a new gati, 
	//							with a final mora to fill any unfinished cycles.

	basicStructure {
		var basicPhrase, developedPhrase;
		var phraseMatras, cycleMatras, moraMatras;
		var phraseMult;
		var newGati, newKarve, gatiChange, gatiChangeMatras, changeRemainder;
		var preFinalFiller, finalMora;
		var ret;


		ret = KonaTime.new(tani);
		cycleMatras = tani.tala.sum*tani.gati;
		newGati = [3,5,7,9].wchoose([1,0.5,0.25,0.125].normalizeSum);
		switch (newGati)
			{3}	{newKarve = [0.5, 1].choose}
			{5}	{newKarve = 2}
			{7}	{newKarve = 4}
			{9}	{newKarve = 4};
		
		basicPhrase = this.vSarvaPhraseAuto;
		phraseMatras = basicPhrase.matras;
		phraseMult = cycleMatras/phraseMatras;
		developedPhrase = this.mutatePhrase(basicPhrase);


		//Fill first cycle
		ret.addAll([basicPhrase,developedPhrase]);
		(phraseMult-2).do { |i|
			ret.add(this.mutatePhrase(basicPhrase));
		};
		ret[ret.size-1] = this.addSuffix(ret[ret.size-1])[0];
		
		Post << "1st ret.dur: " <<  ret.dur << "\n"; 
		
		//Fill second cycle
		ret.add(this.makePostMora(developedPhrase));
		(phraseMult-1).do { |i|
			ret.add(this.randomDensityJati(developedPhrase));
		};
		Post << "2nd ret.dur: " <<  ret.dur << "\n"; 

		//Fill third cycle
		moraMatras = (cycleMatras/2).ceil;
		if(cycleMatras-moraMatras!=0) {
			ret.add(this.vSarvaPhrase(cycleMatras-moraMatras));
		};
		ret.add(this.randomMora(moraMatras, tani.gati, 1));
		Post << "3rd ret.dur: " <<  ret.dur << "\n"; 

		//Fill fourth cycle
		ret.add(this.makePostMora(developedPhrase));
		(phraseMult-1).do { |i|
			ret.add(this.mutatePhrase(developedPhrase));
		};
		Post << "4th ret.dur: " <<  ret.dur << "\n"; 
		
		//Fill fifth and sixth cycles
		ret.add(this.randomSamaCompoundMora(cycleMatras*2, tani.gati, 1));

		Post << "5th 6th ret.dur: " <<  ret.dur << "\n"; 
		//Gati Change
		gatiChange = this.phraseAtGati(ret, newGati, newKarve);
		gatiChangeMatras = (gatiChange.matras/newGati)*tani.gati;
		Post << "newGati: " <<  newGati << "\n"; 
		
		Post << "gatiChangeMatras: " <<  gatiChangeMatras << "\n"; 
		
		if(gatiChangeMatras%1!=0) {
			preFinalFiller = (1-(gatiChangeMatras%1))*newGati;
			ret.add(KonaWord.new(1, newGati, preFinalFiller, tani))
		};
		changeRemainder = gatiChangeMatras%cycleMatras;
		ret.add(gatiChange);
		Post << " gati change  ret.dur: " <<  ret.dur << "\n"; 

		Post << "changeRemainder: " <<  changeRemainder << "\n"; 
		
		//Fill remainder
		if(changeRemainder!=0) {
			if(changeRemainder>(cycleMatras/2)) {
				finalMora = changeRemainder-(cycleMatras/2);
				ret.add(this.vSarvaPhrase(changeRemainder-(cycleMatras/2)));
			} {
				finalMora = changeRemainder;
			};
			finalMora = this.randomMora(finalMora, tani.gati, [1, 0.5].choose);
		};
		
		ret.add(KonaWord.new(1, tani.gati, tani.gati, tani));
		Post << "ret.dur: " <<  ret.dur << "\n"; 
		
		^ret;
	}
	
	
	/* ================================================================================ */
	/* =                          Manipulation Methods                                = */
	/* ================================================================================ */




	//	wordAtGati
	//	@argWord		Word to manipulate
	//	@argGati		New Gati
	//	@argKarve		Number of gati divisions each jati should occupy
	//
	//Method to return a word  at a new Gati, including double tempo etc
	//The gati and karve arguments are taken absolutely
	//For 4-->G3E1 A word with Gati 4, Karve 2, [0.125, 0.125], would be [0.33, 0.33]

	wordAtGati { |argWord, argGati, argKarve|
		^KonaWord.new(argWord.jatis, argGati, argKarve, tani)
	}

	//	phraseAtGati
	//	@argObj					KonaTime (or word) to manipulate
	//	@argGati				New Gati
	//	@argGatiExpansion		Karve multiple. 
	//
	//	Method to return a phrase at a new Gati, including double tempo etc
	//	The expansion value is relative to the input objects expansion, 
	//		so that phrases maintain their relative values


	phraseAtGati {|argObj, argGati, argGatiExp|
		var temp = KonaTime.new(tani);

		if(argObj.class==KonaWord) {
			^KonaWord.new(argObj.jatis, argGati, argObj.karve*argGatiExp, tani)
		} {
			argObj.do{ |item, i|
				temp.add(this.phraseAtGati(item, argGati, argGatiExp));
			}
			^temp;
		};
	}

	//	combine
	//	@aCollection	A collection of KonaItems with a combined desired jati number
	//
	//	Method to create a new KonaWord/KonaTime from the number of 
	//		syllables of the given items
	//	Only used for KonaWords of the same karve

	combine {|aCollection|
		var dur;		//Desired jatis for output
		var karve;		//Karve of the input (determines output Karve)
		var ret;		//KonaItems to return
		var allRest; 	//Boolean; whether the collection is silent syllables
		var oneSyl;		//Boolean; if the collection is one syllable.

		dur = 0;
		ret = KonaTime.new(tani);
		oneSyl = false;
		allRest = aCollection.every { |item, i| item.word == ['-']};

		if(aCollection.size>0) {
		  	karve = aCollection[0].karve;
		} {
		  karve = nil;
		};

		aCollection.size.do { |i|
			dur = dur + aCollection[i].jatis;
			if(i==0) {
				if(aCollection[i].word==['Ta']) {
					oneSyl = true;
				};
			} {
				if(aCollection[i].word!=['-']) {
					oneSyl = false;
				};
			};

		};


		if(oneSyl) {
			ret = KonaWord.new(1, gati, aCollection.matras, tani);
		} {
			while({dur!=0},
				{
					if(dur<=9) {
						if(allRest) {
							ret.add(KonaWord.new(0, gati, dur, tani));
						} {
							ret.add(KonaWord.new(dur, gati, karve, tani));
						};

						dur = dur - dur;
					} {
						if(allRest) {
							ret.add(KonaWord.new(0, gati, dur, tani));
						} {
							ret.add(KonaWord.new(9, gati, karve, tani));
						};
						dur = dur - 9;
					};
				}
			);
		};

		if(ret.size==1) {
			^ret[0];
		} {
			^ret;
		};
	}

	//	combineSimilar
	//	@aCol		A KonaTime containing KonaWords. 
	//	@alMax		Maximum number of items to combine
	//	@avMax		The maximum size for a combination
	//	@prob		Probability of combination
	//
	//	Method to combine identical adjacent KonaWords within a KonaTime
	combineSimilar {|aCol, alMax, avMax=9, aProb=1|

		var col;					//	Collecion to modify
		var lMax;					//	Maximum string length to combine
		var vMax;					//	Maximum value of a combination
		var start, middle, newMiddle, end;		//	Temporary storage
		var n;						//	Item lookahead number
		var i;						//	Iterator variabale
		var y;						//	Next Iterator variable value
		var func;					//	Function to do most of the work
		var prob;					//	Probability the function will occur

		col = aCol;
		prob = aProb;

		n=1;
		i = 0;

		// use argument length if provided
		lMax = alMax ?? {col.size};
		// set combo max
		vMax = avMax;

		func = {

			//Reduce n to the index of the last matching value
			n = n-1;
			//If this is not the first item/string to be evaluated
			if(i>0) {
				//Store the sub-array that proceeds the string/items
				start = KonaTime.newFrom(col[0..i-1], tani);

				//Store the next index to evaluate.
				y = start.size+1;
			} {
				//Else, this this is the first item/string to be evaluated
				//There are no values before the first item
				start = KonaTime.new(tani);
				//Store next index to use: 1
				y = 1;
			};

			//The string of matching values
			middle = KonaTime.newFrom(col[i..i+n], tani);

			if(middle.size>4) {
				prob=1
			};

			if(prob.coin) {
				newMiddle = this.combine(middle);
			} {
				newMiddle = middle;
			};

			//If all elements have been evaluated or combined
			if(middle.includes(col[col.size-1]).not) {
				//Use all elements after the middle
				end = KonaTime.newFrom(col[i+n+1..col.size-1], tani);
			} {
				//Else. There are no values after the last element
				end = KonaTime.new(tani);

			};

			//Combine three sections, summing the middle items
			col = start ++ newMiddle ++ end;
			//Reset n
			n = 1;
			//Set the next index to start as;
			i = y;
		};


		//Evaluate the whole collection
		col.size.do {
			if(col[i].class==KonaTime) {
				col[i] = this.combineSimilar(col[i], prob:0.85);
			} {

				//If there are trailing elements
				if(col[i+n]!=nil) {
				//If a value is followed by an identical value, 
				//	and current string length is within bounds;
					if( ( (col[i].val == col[i+n].val) || (col[i].word==['Ta'] && col[i+n].word==['-']) )  && (n<lMax) && (col[i..i+n].jatis <= vMax)) {
						if(col[i-1]!=nil) {
							if(col[i].word==['Ta'] && (col[i-1].word==['-']) ) {
								func.();
							} {
								//Extend string length
								n = n+1;
							};
						} {
							func.();
						};
					} {

						//Else combine all identical adjacent items.
						func.();
					}
				} {
					//If there are no more trailing items, combine those stored.
					func.();
				};
			};
		};

		^col;
	}

	//	atDensity
	//	@item		Item to alter density of
	//	@density	Density multiplier
	//
	//	Method to return the word at a new density (same duration, more notes).
	//	E.g. Ta - Ka - Di - Mi -  @ density 2 becomes TaKaDiMiTaKaJuNa

	atDensity { |aKonaItem, density|
		var item;
		var newJatis;
		var newKarve;
		var newWords;
		var newPhraseFunc;
		var ret;
		
		item = this.fillOut(aKonaItem, true);
		Post << "density: " <<  density << "\n"; 
		
		newPhraseFunc = {
			this.phraseAtGati(item, item.gati, 1/density)
		};
		
		if(item.class==KonaWord) {

			newJatis = (item.jatis*density).max(1);
			newKarve = (item.karve*(1/density)).min(item.matras);
			//If the result can be a single word
			if(newJatis<=9) {
				//If the adjustment results in a non Integer
				if(newJatis%1!=0) {
					//A 1 syllable word with a matching duration is returned
					^KonaWord.new(1, item.gati, item.jatis, tani)
				} {
					//Otherwise a new word with adjusted density is returned
					^KonaWord.new(newJatis, item.gati, newKarve, tani)
				};
			} {
				//If the results require multiple words, create them
				newWords = newJatis/item.jatis;
				^KonaTime.fill(newWords, {newPhraseFunc.()}, tani)
			};
		} {
			
			//If the item is a collection of words, call this method on each of them
			ret = KonaTime.new(tani);

			item.size.do { |i|
				ret.add(this.atDensity(item[i], density))
			};

			^ret
		};
	}

	//	randomAtDensity
	//	automation of the atDensity method
	//
	//	@aKonaItem	Item to be manipulaed
	randomAtDensity {|aKonaItem|
		var store;			//Storage for invalid density multipliers
		var min, max;		//Min and Max multiplier values
		var val;			//Array of multipliers from min to max
		var wVal;			//Array of weights for multipliers;
		var count;			//Counter for the value of greatest reduction.
		var xGatiFunc;		//Function to calculate the xGati, for gati=4 exception.
		var xGati;			//Gati of object

		store = List[];
		val = List[];
		count = 1;
		xGatiFunc = {
			xGati = aKonaItem.gati;
			if(xGati==4) {xGati=2};	//Exception for caturasra gati 
		};
		xGatiFunc.();
		//Find largest numbers of jatis in the object
		if(aKonaItem.class==KonaWord) {
			min = aKonaItem.jatis;
		} {
			min = aKonaItem.greatestJatis;
		};

		//Calculate the smallest possible multiplier
		while({count*min>1}, {
			count = 1;
			count = count/xGati;
			xGati = xGati*2;
		});
		//Reset the xGati;
		xGatiFunc.();
		min = count;

		//Calculate the greatest possible multiplier
		max = aKonaItem.speed/(0.5/aKonaItem.gati);		

		//Calculate all values between max and min.
		val.add(max);
		while({(max/xGati)>min}, {
			val.add(max/xGati);
			xGati = xGati*2;
		});
		val.add(min);
		val = val.asArray.reverse;
		//Convert whole numbers into Integers
		val.size.do { |i|
			if(val[i]==val[i].asInteger) {
				val[i] = val[i].asInteger
			}
		};
		//Remove multiplier of 1 if others are possible
		if(val.size>1) {val.remove(1)};
		//Create weights from multiplier values. 
		//	Positive multipliers are given the heaviest weight, 
		//	with greatest weight given to those
		// >=2. Multipliers below 0.5 are given the lowest weightings.
		wVal = Array.newClear(val.size);
		val.size.do { |i|
			if(val[i]<1) {
				wVal[i]=0.75;
			} {
				wVal[i]=1
			};
			if(val[i]<0.5 || (val[i]>2)) {
				wVal[i] = wVal[i] - 0.25;
			};
		};

		val = val.wchoose(wVal.normalizeSum);
		//Return the altered item
		^this.atDensity(aKonaItem, val.asInteger);
	}

	//	extendJati
	//	Method to extend the jati of a given Kona Word.
	//	e.g. TaKaDiMi with index [1] extended by 1 becomes TaTa-Ta
	//
	//	@aKonaWord	Word to manipulate
	//	@aIndex		Index of jati to ext
	//	@aExt		Number of jatis to extend the given jati by

	extendJati {|aKonaWord, aIndex, aExt|
		var ret;	//	Variable to return output;
		var start;	//	Jatis before the extended jati
		var middle;	//	Extended Jati
		var end;	//	Jatis after extended jati

		//If there is 1 jati, extension is impossible so return the input.
		if(aKonaWord.jatis==1) {
			^aKonaWord
		};

		//If overextending...
		if( (aIndex+aExt+1)>aKonaWord.jatis,
			{^"Trying to extend tooo much..."}
		);

		//If the first jatis is being extended, there are no prior jatis to store
		if(aIndex==0) {
			start = nil;
		} {
			//Else use all jatis before the extended jati
			start = KonaWord.new((0..aIndex-1).size, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani);
		};

		//Jati to extend
		middle = KonaWord.new(1, aKonaWord.gati, 1+aExt*aKonaWord.karve, aKonaWord.tani);

		//If the extension will take up all of the word duration
		if((aIndex+aExt)==(aKonaWord.jatis-1)) {
			//Have no following jatis
			end = nil;
		} {
			//Else use the jatis following the extension
			end = KonaWord.new((aIndex+aExt+1..aKonaWord.jatis-1).size, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani);
		};

		//Create a KonaTime, add the new KonaWords and return it
		ret = KonaTime.new(tani);
		[start,middle,end].do { |item, i|
			if(item!=nil, {ret.add(item)});
		};
		^ret;

	}

	//	randomExtendJati
	//	Automation of the extendJati Method
	//
	//	@aKonaItem	Object to manipulate

	randomExtendJati{|aKonaItem|
		var itemSize;
		var index;
		var max;
		var count;
		var ret;
		var chance;
		var success;
		
		if(aKonaItem.class==KonaTime) {
			ret = KonaTime.newClear(aKonaItem.size, tani);
			success = Array.newClear(aKonaItem.size);
			chance = 1/aKonaItem.size;
			aKonaItem.size.do {|i|
				if(chance.coin) {
					ret[i] = this.randomExtendJati(aKonaItem[i]);
					chance = chance/2;
					success[i]=true;
				} {
					ret[i] = aKonaItem[i];
					success[i]=false;
				}
			};
			if(success.every { |item, i| item.not }) {
				success = aKonaItem.size.rand;
				ret[success] = this.randomExtendJati(ret[success])
			};
			^ret;
		} {
			index = (aKonaItem.jatis-1).rand;
			max = (aKonaItem.jatis-1 - index);
			count = (1..max).choose;

			^this.extendJati(aKonaItem, index, count);
		};
	}

	//	muteJati
	//	Method to mute a jati of a given Kona Word.
	//	e.g. TaKaDiMi with index [1] muted becomes Ta-Taka
	//
	//	@aKonaWord	Word to manipulate
	//	@aIndex		Index of jati to mute
	muteJati {|aKonaWord, aIndex|
		var start;
		var middle;
		var end;
		var ret;

		if(aIndex>=aKonaWord.jatis,
			{^"overstretched yourself a bit..."}
		);
		//If the first jatis is being extended, there are no prior jatis to store
		if(aIndex==0) {
			start = nil;
		} {
			//Else use all jatis before the extended jati
			start = KonaWord.new((0..aIndex-1).size, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani);
		};

		//Muted Jati
		middle = KonaWord.new(0, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani);

		//Following Jatis
		if(aIndex==(aKonaWord.jatis-1)) {
			end = nil;
		} {
			end = KonaWord.new((aIndex+1..(aKonaWord.jatis-1)).size, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani)
		};

		//Combine Jatis into a new KonaTime
		ret = KonaTime.new(tani);
		[start,middle,end].do { |item, i|
			if(item!=nil, {ret.add(item)});
		};
		^ret;
	}

	//	randomMuteJati
	//	Automation of the muteJati Method
	//
	//	@aKonaItem	Object to manipulate
	randomMuteJati {|aKonaItem|
		var index;
		var ret;
		var iter;
		var chance;
		var success;
		
		if(aKonaItem.class==KonaTime) {
			ret = KonaTime.new(tani);
			success = Array.newClear(aKonaItem.size);
			chance = 1/aKonaItem.size;
			iter = aKonaItem.size-1;
			aKonaItem.size.do { |i|
				if(chance.coin) {
					ret.add(this.randomMuteJati(aKonaItem[iter]));
					chance = chance/5;
					success[i] = true;
				} {
					ret.add(aKonaItem[iter]);
					success[i] = false;
				};
				iter = iter - 1;
			};
			if(success.every { |item, i| item.not }) {
				success = aKonaItem.size.rand;
				ret[success] = this.randomMuteJati(ret[success]);
			};
			ret = ret.reverse;
			^ret;
		} {
			index = aKonaItem.jatis.rand;
			^this.muteJati(aKonaItem, index);
		};
	}

	//	densityJati
	//	Method to alter the density of a jati in a given Kona Word.
	//	e.g. Ta Ka Di Mi with index [1], density 2 becomes Ta TaKa Ta Ka
	//
	//	@aKonaWord	Word to manipulate
	//	@aIndex		Index of jati to mute
	//	@aDensity	Density to alter by
	densityJati {|aKonaWord, aIndex, aDensity|
		var start;
		var middle;
		var end;
		var ret;

		if(aIndex>=aKonaWord.jatis,
			{^"overstretched yourself a bit..."}
		);

		//If the first jatis is being altered, there are no prior jatis to store
		if(aIndex==0) {
			start = nil;
		} {
			//Else use all jatis before the extended jati
			start = KonaWord.new((0..aIndex-1).size, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani);
		};

		//Density altered jati;
		middle = this.atDensity(KonaWord.new(1, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani), aDensity.max(1));

		//Following Jatis
		if(aIndex==(aKonaWord.jatis-1)) {
			end = nil;
		} {
			end = KonaWord.new((aIndex+1..(aKonaWord.jatis-1)).size, aKonaWord.gati, aKonaWord.karve, aKonaWord.tani)
		};

		//Combine Jatis into a new KonaTime
		ret = KonaTime.new(tani);
		[start,middle,end].do { |item, i|
			if(item!=nil, {ret.add(item)});
		};
		^ret;

	}

	//	randomDensityJati
	//	Automation of the densityJati Method
	//
	//	@aKonaItem	Item to manipulate
	//	@aRec		Chance of recursion
	randomDensityJati {|aKonaItem, aRec=0.5|
		var store;
		var index;
		var val;
		var wVal;
		var max;
		var ret;
		var chance;
		var success;
		
		if(aKonaItem.class==KonaTime) {
			ret = KonaTime.new(tani);
			success = Array.newClear(aKonaItem.size);
			chance = 1.5/aKonaItem.size;

			aKonaItem.size.do { |i|
				if(chance.coin) {
					ret.add(this.randomDensityJati(aKonaItem[i], 0));
					chance = chance/2;
					success[i] = true;
				} {
					ret.add(aKonaItem[i]);
					success[i] = false;
				};	
			};
			
			if(success.every { |item, i| item.not }) {
				success = aKonaItem.size.rand;
				ret[success] = this.randomDensityJati(ret[success], 0);
			};
			
		} {
			store = List[];

			index = aKonaItem.jatis.rand;

			max = aKonaItem.speed/(0.5/aKonaItem.gati);
			val = (2..max);
			
			//Remove unacceptable densities (for this gati)
			val.do { |item, i|
				if((aKonaItem.speed*(1/val[i])%(0.5/aKonaItem.gati)).round(0.001)!=0,
					{store.add(i)}
				);
			};
			val.removeAtIndexes(store);
			//If the item is not alterable
			if(val.size==0) {
				^aKonaItem
			};
			//Select an expansion
			wVal = Array.newClear(val.size);
			val.size.do { |i|
				if(val[i]>2)
					{ wVal[i] = 0.5 }
					{ wVal[i] = 1};
			};
		
			wVal = wVal.normalizeSum;
			val = val.wchoose(wVal);
			Post << "val: " <<  val << "\n"; 
			

			ret = this.densityJati(aKonaItem, index, val);
		};

		if(aRec.coin) {
			^this.randomDensityJati(ret, (aRec/2));
		} {
			^ret
		};

	}

	//	permutePhrase
	//	Method to return a permutation of a KonaTime phrase
	//
	//	@aKonaTime 		KonaTime to be permuted
	//	@permutation	Optional permutation specificiation
	//	@seed			Optional seed for random selection;
	permutePhrase { |aKonaTime, permutation, seed|
		var phrase;
		var partition;
		var permuteNum;

		if(seed!=nil) {
			thisThread.randSeed=seed
		};

		if(aKonaTime.class==KonaTime) {
			phrase = aKonaTime;
		} {
			phrase = KonaTime.newFrom([aKonaTime], tani)
		};

		//Permutations 0 and size.factorial return the input, so they are ignored.
		permuteNum = permutation ?? {if(phrase.size==1) {1} {(1..phrase.size.factorial-1).choose}};

		^phrase.permute(permuteNum.asInteger);
	}

	//	partitionWord
	//	Method to partition and permute a KonaWord
	//	E.g. KonaWord(5,4,1) could be returned 
	//		as KonaTime[KonaWord(2,4,1),KonaWord(3,4,1)];
	//
	//	@aKonaWord	Word to partition
	//	@seed		Optional seed value for randomness
	partitionWord {|aKonaWord, min=2, aMax, seed|
		var partition;
		var int;
		var ret;
		var max;

		if(seed!=nil) {
			thisThread.randSeed=seed
		};
		
		int = aKonaWord.jatis;

		max = aMax ?? {if(int>9) {9} {int}};

		partition = this.randomPartition(int, min, max, true, seed);

		partition = this.randomPerm(partition, seed:seed);
		
		ret = this.partsToWords(partition, aKonaWord.karve, false);

		^ret
	}


	//	randomPartitionMutate
	//	Method to (possibly) partition a word and (definitely) mutate it
	//
	//	@aKonaWord	Word to part/mutate
	//	@aChance	Chance that partitioning will occur
	//	@seed		Random Thread seed
	randomPartitionMutate {|aKonaWord, aChance=0.5, seed|
		var ret;
		var chance;

		chance = aChance;
		if(seed!=nil) {
			thisThread.randSeed=seed
		};

		//Decision: Partition word or not
		if(chance.coin) {
			ret = this.partitionWord(aKonaWord, seed:seed);

		} {
			ret = KonaTime.newFrom([aKonaWord], tani);
		};


		//Mutate the phrase for added interest
		ret = this.mutatePhrase(ret);
		^ret
	}

	//	addSuffix
	//	Method to add a densly articulated suffix to the end of a phrase
	//
	//	@aPhrase	Phrase to alter
	addSuffix {|aPhrase|
		var phrase;			//Phrase to be altered and returned
		var iter;			//Iterator
		var suffixMatras;	//Number of matras in the suffix
		var suffixTemp;		//Temporary storage for items to be included in the suffix
		var densMax;		//The maximum possible density;
		var densities;		//Density multipliers for suffix parts
		var temp;			//Temporary storage for mutations.

		phrase = aPhrase.deepCopy;
		densMax = {|item| item.speed/(0.5/item.gati); };	
		
		if(phrase.class!=KonaTime) {
			phrase = this.partitionWord(phrase, aMax:(phrase.matras/4));
		} {
			if(phrase.size==1) {
				phrase = this.partitionWord(phrase, aMax:(phrase.matras/4));
			};
		};

		iter = phrase.size-1;
		suffixMatras = 0;

		//Add up item matras from the end until a sufficient number is found 
		while({suffixMatras < (phrase.matras/4)}, {
			suffixMatras = suffixMatras + phrase[iter].matras;
			iter = iter - 1;
		});

		//Store those KonaItems to be used for the suffix.
		suffixTemp = phrase.select({|item, i| (iter+1..phrase.size-1).includes(phrase.indexOf(item)) });
		densities = Array.newClear(suffixTemp.size);

		//Store suitable densities
		if(suffixTemp.size==1) {
			densities[0] = 4
		} {
		
			suffixTemp.do { |item, i|
				
				case
					{i==0}						{densities[i] = 2}
					{i==(suffixTemp.size-1)}	{densities[i] = [2,4].choose}
					{true}						{densities[i] = [2,4].choose}
			};
		};
		
		
		//Alter densitiy of items
		densities.size.do { |i|
			//Protect against going too fast.
			if(suffixTemp[i].karve/densities[i] < densMax.(suffixTemp[i])) {
				densities[i] = densMax.(suffixTemp[i]);
			};
			
			temp = this.atDensity(suffixTemp[i], densities[i]);
			temp = this.randomMuteJati(temp);
			
			if(i==(suffixTemp.size-1)) {
				"hi".postln;
				temp = this.randomDensityJati(temp);
			};
			phrase[iter+1+i] = temp;

		};

		^[phrase, iter+1];
	}

	//	fillOut
	//	Method to 'fill in' konaWords/Times 
	//		e.g. a KonaWord 1,4,3 (0.75) gets turned into 3,4,1.
	//
	//	@aKonaItem		Item to be altered
	//	@aOnlyUneven	Boolean, if only uneven parts should be filled in.
	fillOut {|aKonaItem, aOnlyUneven=false|
		var ret;
		var mult;
		var jatis;
		var action;
		var i;
		var temp;
		
		if(aKonaItem.class==KonaWord) {
			mult = aKonaItem.speed/(1/aKonaItem.gati);
			
			block {|break|
				while({mult.round(0.0001 )%1!=0}, {
					mult = (mult*2);		
					if (i==100) { break.value(999)}; 
				});				
			};

			if(mult.asInteger.odd && (mult!=1)) {
				action = true;
			} {
				if(aOnlyUneven) {
					action = false;
				} {
					action = true
				};
			};

			if(action) {
				jatis = mult*aKonaItem.jatis;

				if(mult>9) {
					ret = this.partsToWords(this.randomPartition(jatis, notSize:true), aKonaItem.karve/mult, false);
				} {		
					ret = KonaWord.new(jatis, aKonaItem.gati, aKonaItem.karve/mult, tani)
				};

			} {
				ret = aKonaItem.deepCopy;
			};		
		} {
			ret = KonaTime.new(tani);
			aKonaItem.size.do { |i|
				temp = this.fillOut(aKonaItem[i], aOnlyUneven);
				ret.add(temp);
			};
		};
		^ret
	}


	//	makePostMora
	//	Method to convert a phrase so that it's suitable after a mora, 
	//		i.e. that it starts with a strong long beat
	//	
	//	@aKonaItem		Phrase to be altered
	makePostMora {|aKonaItem|
		var phrase;				//Phrase being altered
		var sectionMatras;		//Matras in the section being overridden
		var i;					//Iterator
		var index;				//Index used when calculating makeup Matras
		var hitMatras;			//Duration of the initial beat in matras
		var makeupMatras;		//Duration of the material being made up for
		var hit;				//KonaWord for the initial beat
		var makeup;				//KonaWord for the makeup
		var ret;				//KonaTime to return the phrase
		
		//If phrase is a KonaWord, convert it to a KonaTime
		if(aKonaItem.class==KonaTime) {
			phrase = aKonaItem.deepCopy;
		} {
			phrase = KonaTime.newFrom([aKonaItem], tani);
		};
		
		sectionMatras = 0;
		i = 0;
		//Create initial hit
		hitMatras = aKonaItem.gati;
		hit = KonaWord.new(1, phrase.gati, hitMatras, tani);
		
		//Count how many parts of the phrase will be overridden
		while({sectionMatras<hitMatras}, {
			sectionMatras =  sectionMatras + phrase[i].matras;
			i = i + 1;	
		});
		//Calculate the duration of the overridden section that needs to be recreated
		makeupMatras = sectionMatras - hitMatras;
		//Generate makeup material.
		if(makeupMatras!=0) {
			
			if(phrase.size==1) {
				index = 0
			} {
				index = i-1;
			};
			makeup = this.vSarvaPhrase(makeupMatras)
		};
		//Add and return all items
		ret = KonaTime.newFrom([hit], tani);	
		if(makeup!=nil) {ret.add(makeup)};
		if(phrase.size>1) {
			ret.addAll(phrase[i..phrase.size-1]);
		};
		
		^ret;
	}
}
