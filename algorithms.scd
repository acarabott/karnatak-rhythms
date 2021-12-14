/* 
	Various algorithms, implemented as functions, and later converted to class methods
 */
 
~toFill = 1.5;

~spaceFill = {|space, gati|
	var gatiDur;
	gatiDur = 1/gati;
	gatiDur.postln;
	space/gatiDur;
};

x = ~spaceFill.(~toFill, 4);
/* ======= */
/* = ZS1 = */
/* ======= */
[1,2,3]
(

~zs1 ={|n, min=2, max=9|
	var x, m, h, r, t, temp, ret;
	x=Array.newClear(n+1);
	for(0,n,{|i| x[i] = 1});
	x[0]=0;
	x[1]=n;
	m=1;
	h=1;

	temp = List[];
	while({x[1] != 1}, {
		if(x[h] == 2) {
			m = m+1;
			x[h] = 1;
			h = h-1;
		} {
			r = x[h]-1;
			t=(m-h+1);
			x[h] = r;

			while({t>=r},{

				h=h+1;
				x[h]=r;
				t= t-r;

			});


			if(t==0) {
				m=h
			} {
				m=(h+1);
				if(t>1) {
					h=(h+1);
					x[h] = t;
				};
			};
		};

		temp.add(x[1..m]);

	});

	temp.removeAllSuchThat({ |item, i|
		item.any({|jtem, j|
			(jtem<min) || (jtem>max)
		});
	});

	ret = List[];
	temp.do { |item, i|
		"item: ".post; item.postln;

		if(item.occurrencesOf(item[0]) != item.size) {
			if(item.every({ |jtem, j| (jtem>=min) && (jtem<=max)}))
			{
				item.size.factorial.asInteger.do { |j|
					ret.add(item.permute(j))
				};
			};
		}	{
			ret.add(item);
		};
	};

	ret.add([n]);

};
)
~zs1.(7)

/* =============== */
/* = MyAlgorithm = */
/* =============== */
(
~store2 = List[];
~findAll = {|num, argMin=2|
	var min, max, vals;
	var store, store2, store3;
	var temp;
	var ret;
	min = argMin;
	//Maximum is total - minimum
	max = (num-min);
	//Values to subtract from total
	vals = (min..max);

	//Arrays for storing simple combinations
	store = List[];
	ret = List[];
	//Add simple combinations to Store
	if(max>1) {
		vals.do { |item, i|
			store.add([(num-item), item])
		};

		//For each single combination, find all compound combinations
		store.do { |item, i|
			//Array to store combinations for each item
			store2 = List[];
			ret = ret.asList;
			//For each item, add all possible combinations to store2
			//Some items will be stored as follows (reduced items)
			//[[2,3], [3,2], 2]
			//Which represents the possible combinations
			//[2,3,2] and [3,2,2]
			item.do { |jtem, j|
				store2.add(~findAll.(jtem));
			};

			//For each reduced item, create all tuplets
			store3 = Array.newFrom(store2.asArray.allTuples);
			store3.do { |item, i|
				store3[i] = item.flat;
			};

			ret.addAll([store, store3].flatten);

			ret = ret.asArray;
			ret.add(num);

			ret.removeAllSuchThat({|item, i| ret.occurrencesOf(item) > 1 });
		};
	} {
		ret = num;
	};

	ret;
}

/* ======= */
/* = ZS1 = */
/* ======= */

(

~zs1 ={|n, min=2, max=9|
	var x, m, h, r, t, ret;
	x=Array.newClear(n+1);
	for(0,n,{|i| x[i] = 1});
	x[0]=0;
	x[1]=n;
	m=1;
	h=1;

	ret = List[];

	while({x[1] != 1}, {
		if(x[h] == 2) {
			m = m+1;
			x[h] = 1;
			h = h-1;
		} {
			r = x[h]-1;
			t=(m-h+1);
			x[h] = r;

			while({t>=r},{

				h=h+1;
				x[h]=r;
				t= t-r;

			});


			if(t==0) {
				m=h
			} {
				m=(h+1);
				if(t>1) {
					h=(h+1);
					x[h] = t;
				};
			};
		};

		ret.add(x[1..m]);

	});

	ret.add([n]);

};
)

~zs1.(9)

/* ======= */
/* = ZS2 = */
/* ======= */
(
~zs2 = {|argN, argMax|
	var n;	//	The integer to partition
	var x;	//	An array to store each new partition in;
	var h;	//	The index of the last part of partition that is > 1
	var m;	//	The number of parts in a partition
	var j;	//	The index of the next part to be increased
	var r;	//	A variable used to calculate the next m

	var ret;	//	A list to store all partitions in, methods returns this;
	var max;	//	The maximum value of any part (e.g. partion of 5 with a max of 3 can return [3,1,1] but not [4,1] or [5])

	n = argN;
	ret = List[];
	if(argMax==nil) {
		max = n;
	} {
		max = argMax;
	};

	//Fill the array with n *1
	x = Array.fill(n, 1);
	//Add the array as it forms the first partition
	ret.add(x[0..n]);
	//Alter x by setting the second element[1] to 2 and returning the subarray x[1..n]
	x[1] = 2;
	ret.add(x[1..n]);
	h = 1;
	m = n-1;

	//Generate further partitions
	while({x[1] != max},
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
			ret.add(x[1..m]);
		}
	);
	ret
}
)

~zs2.(5)



//Combine any string of same numbers
x = [4,4,3,3,3,2,5,3,2,2,3,4,3,3,4,4];


(
f = {|aCol|
	var col = aCol;
	var s, m, e;
	var n;
	var i;
	n=1;
	i = 0;

	col.size.do {
		"".postln;
		if(col[i]==col[i+n]) {
		  n = n+1;
		} {
			"i: ".post; i.postln;
			n = n-1;
			"i+n: ".post; (i+n).postln;
			if(i>0) {
				s = Array.newFrom(col[0..i-1]);
				y = s.size+1;
			} {
				s = [];
				y = 1;
			};
			"s: ".post; s.postln;

			m = Array.newFrom(col[i..i+n]);
			"m: ".post; m.postln;

			if((i+1)!=(col.size-1)) {
				e = Array.newFrom(col[i+n+1..col.size-1]);
			} {
			   e = []
			};
			"e: ".post; e.postln;

			col = s ++ m.sum ++ e;
			"col: ".post; col.postln;
			n = 1;
			i = y;

		};
	};
}
)

f.(x)


//Combine Pairs
(
x = [4,4,3,3,3,2,5,3,2,2,3,4,3,3,4,4];

f = {|aCol|
	var col = aCol;
	var s, m, e;
	var n;
	i = 0;

	col.postln;
	while({i<(col.size)},
		{
			if(col[i]==col[i+1]) {
				if(i>0) {
				  s = Array.newFrom(col[0..i-1]);
				} {
				  s = [];
				};

				m = Array.newFrom(col[i..i+1]);

				if((i+1)!=(col.size-1)) {
					e = Array.newFrom(col[i+2..col.size-1]);
				} {
				   e = []
				};

				col = s ++ m.sum ++ e;
			};
			i=i+1;
		}
	);
	col.postln;
}
)

f.(x)


(
x = [3,4,4,3,3,3,2,5,3,2,2,3,4,3,3,4,4];

f = {|aCol, nMax, avMax|
	var col = aCol;
	var s, m, e;
	var n;
	var i;
	var max;
	var vMax;
	n=1;
	i = 0;

	if(nMax==nil,
		{max = col.size},
		{max = nMax}
	);
	vMax = avMax ?? {9};

	col.size.do {
		"".postln;
		if(col[i]==col[i+n] && (n<max) && (col[i..i+n].sum <=vMax)) {
		  n = n+1;
		} {
			"i: ".post; i.postln;
			n = n-1;
			"i+n: ".post; (i+n).postln;
			if(i>0) {
				s = Array.newFrom(col[0..i-1]);
				y = s.size+1;
			} {
				s = [];
				y = 1;
			};
			"s: ".post; s.postln;

			m = Array.newFrom(col[i..i+n]);
			"m: ".post; m.postln;

			if((i+1)!=(col.size-1)) {
				e = Array.newFrom(col[i+n+1..col.size-1]);
			} {
			   e = []
			};
			"e: ".post; e.postln;

			col = s ++ m.sum ++ e;
			"col: ".post; col.postln;
			n = 1;
			i = y;

		};
	};
}
)
f.(x, 2, 4);


(
~gati = 5;
	//Potential max part sizes
	~maxArray = (2..2*~gati);
	if(~gati==4,
		{~gatiPowers = List[(~gati/2).asInteger]},
		{~gatiPowers = List[(~gati).asInteger]}
	);
	10.do { |i|
		~gatiPowers.add( (~gatiPowers[~gatiPowers.size-1]*2).asInteger)
	};


	//Parts 2 to gati. Given heaviest weightings
	~maxW1 = Array.fill(~maxArray[0..16].size, 0);
	~maxW1.size.do { |i|
		if(~gatiPowers.includes(~maxArray[i]),
			{~maxW1[i] = 1},
			{~maxW1[i] = 0.4}
		);
		if(~maxArray[i]<~gati) {
		  ~maxW1[i] = ~maxW1[i] + 0.25;
		} {
			if(~maxArray[i]!=~gati) {
		  		~maxW1[i] = ~maxW1[i] - 0.4
			};
		};

		if(~gati==5 || (~gati==7)) {
		  if((~maxArray[i]== 3) || (~maxArray[i]== 2)) {
		    	~maxW1[i] = ~maxW1[i] + 0.25;
			} {
				~maxW1[i] = ~maxW1[i] - 0.25
			};
		};
		if(~maxW1[i]<0,
			{~maxW1[i] = 0}
		);
	};
)
	~maxArray
	~gatiPowers
	~maxW1.normalizeSum

	maxW1MI = maxW1.maxItem;
	//Parts gati+1 to max phrase jatis
	maxW2 = Array.newClear(maxArray[gati-1..maxW1.size].size);
	maxW2.seriesFill((maxW1MI-(maxW1MI/(maxW2.size+1))), maxW1MI/(maxW2.size+1).neg);




List[2,3,4].includes(2)



randomMora {|duration, aGati, aKarve|
	var stateArray, gapMin, gapMax, gapArray;
	var stateDur, gapDur, offsetDur;
	var statement, gap, offset;
	var temp;
	var ret;

	//	Nelson 2008 p 23
	//	'It is a practical fact of Karnatak rhythmic behaviour that if a mora statement is shorter than five pulses,
	//	its gap will nearly always be at least two pulses'
	//	This is impossible if a duration of less than 7 is used.
	//		In this instance a mora with the same duration, but using double the jatis and half the karve is returned
	if(duration<7,
		{^this.randomMora(duration*2, aGati, aKarve/2)} // Might need to alter this so that it doesn't ever go to fast..
	);

	//12.5% chance of returning a mora with the same duration as given, but doubled density for moras < 2 beats
	if((duration/aGati)<=2,
		{if(0.125.coin, {"densityAlt ".postln; ^this.randomMora(duration*2, aGati, aKarve/2)})}
	);


	//	Any duration under 15 will result in statements less than five pulses, so requires a minimum gap of 2
	if(duration<15) {
		gapMin = 2;
	} {
		gapMin = 0
	};

	//Select a value for the statement duration
	stateArray = (1..(duration-(gapMin*2)/3));
	"stateArray".post; stateArray.postln;
	stateDur = stateArray.wchoose((1..stateArray.size).normalizeSum);
	"stateDur: ".post; stateDur.postln;

	//Select a value for the gap duration;
	if(stateDur<5,
		{gapMin=2}
	);
	gapMax = (duration-(stateDur*3))/2;
	gapArray = (gapMin..gapMax);
	gapArray.remove(1);
	gapDur = gapArray.wchoose((gapArray.size..1).normalizeSum);
	"gapDur: ".post; gapDur.postln;

	//Work out the necessary offset
	offsetDur = duration - (stateDur*3) - (gapDur*2);
	"offsetDur: ".post; offsetDur.postln;

	//Turn statements into KonaItems
	//If the statement duration can be a single word
	if(stateDur<=9) {
		//Decide whether or not the duration also determines jatis
		//E.g. 5 could be Ta - - - - or Da Di Gi Na Dom
		if(0.85.coin) {
			//This is a word with jatis equal to the duration
			statement = KonaWord.new(stateDur, aGati, aKarve, tani);

			//Partition/mutate this for added interest (non-deterministic)
			statement = this.randomPartitionMutate(statement);

		} {
			//This is a 1 jati word of the statement duration
			statement = KonaWord.new(aKarve, aGati, stateDur, tani);
		};
	} {
		//If a statement duration requires more than a single word
		//Generate a partition
		statement = this.randomPart(stateDur.asInteger);
		//Choose a permutation
		statement = this.randomPerm(statement);

		//Convert to KonaTime
		ret = KonaTime.new(tani);

		statement.size.do {|i|
			if(0.85.coin) {

				//New word jatis equal to part duration
				temp = KonaWord.new(statement[i], aGati, aKarve, tani);
				temp = this.randomPartitionMutate(temp);
				ret.add(temp);

			} {

				//Single jati word with duration equal to part duration
				ret.add(KonaWord.new(aKarve, aGati, statement[i], tani))
			};
		};

		"statement pre-mutation: ".postln; ret.postWord;
		statement = this.mutatePhrase(ret);

	};

	"statement: ".postln; statement.postWord;

	if(gapDur==0) {
		gap=nil;
	} {
		if(0.85.coin) {
			//Generate single jati gap with gapDur duration
			gap = KonaWord.new(aKarve, aGati, gapDur)
		} {
			gap = this.randomPart(gapDur.asInteger);
			gap = this.randomPerm(gap);

			ret = KonaTime.new(tani);
			gap.size.do { |i|
				ret.add(KonaWord.new(gap[i], aGati, aKarve));
			};
			"gap pre-mutation: ".postln; ret.postWord;
			gap = this.randomPartitionMutate(ret, 0.25);
		};
	};

	if(gap!=nil,
		{"gap: ".postln; gap.postWord},
		{"no gap".postln}
	);

	//Generate offset word

	if(offsetDur>=1) {

		if(0.25.coin) {

			//Generate word as jatis==duration
			"offsetDur: ".post; offsetDur.postln;
			"aGati: ".post; aGati.postln;
			"aKarve: ".post; aKarve.postln;
			"tani: ".post; tani.postln;

			offset = KonaWord.new(offsetDur, aGati, aKarve, tani);

			//add randomPart to offset?
		} {

			//Generate word as jatis==1, karve = duration
			if(0.85.coin) {

				//Word is a rest
				offset = KonaWord.new(0, aGati, offsetDur, tani);

			} {

				//Word is 1 syllable (Ta)
				offset = KonaWord.new(1, aGati, offsetDur, tani);

			};
		};
	} {

		offset = nil;

	};

	ret = KonaTime.new(tani);
	if(offset!=nil,
		{ret.add(offset)}
	);

	2.do {
		ret.add(statement);
		if(gap!=nil,
			{ret.add(gap)}
		);
	};
	ret.add(statement);

	"ret: ".postln; ret.postWord;
	^ret
}


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

			//Store the next index to evaluate. The index after the preceeding sub-array
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
		"Old Middle".postln;
		middle.postWord;

		if(middle.size>4) {
			prob=1
		};

		if(prob.coin) {
			newMiddle = this.combine(middle);
		} {
			newMiddle = middle;
		};
		"new Middle".postln;
		newMiddle.postWord;
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
			//If a value is followed by an identical value, and the current string length is within bounds;

				if( (col[i].val) == (col[i+n].val) && (n<lMax) && (col[i..i+n].jatis <= vMax) ) {
					i.postln;
					//Extend string length
					n = n+1;
				} {
					if(col[i].val[2]==['Ta'] && (col[i+n].val[2]==['-'])) {
						n = n+1;
					} {
						//Else combine all identical adjacent items.
						func.();
					};
				}
			} {
				//If there are no more trailing items, combine those stored.
				func.();
			};
		};
	};

	^col;
}

x = [4,4];

if( (x[0]==x[1] || false)  && (x[0]<5) && (x[1]<5)  ) {"true".postln} {"false".postln}

(
~addSuffix = {|aPhrase|
	var phrase;			//Phrase to be altered and returned
	var iter;			//Iterator
	var suffixMatras;	//Number of matras in the suffix
	var suffixTemp;		//Temporary storage for items to be included in the suffix
	var densities;		//Density multipliers for suffix parts
	var temp;			//Temporary storage for mutations.
	
	phrase = aPhrase.deepCopy;
	
	
	if(phrase.class!=KonaTime) {
		phrase = a.gen.partitionWord(phrase);
	} {
		if(phrase.size==1) {
			phrase = a.gen.partitionWord(phrase);
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
				{i==0}				{densities[i] = 2;}
				{i==(z.size-1)}		{densities[i] = 4}
				{true}				{densities[i] = [2,4].choose};	
		};
	};
	Post << "densities: " <<  densities << "\n"; 
	
	//Alter densitiy of items
	densities.size.do { |i|
		//Protect against going too fast.
		if(suffixTemp[i].karve/densities[i] < ((1/a.gati)/2)) {
			"hi".postln;
			densities[i] = densities[i]*2
		};

		temp = a.gen.atDensity(suffixTemp[i], densities[i]);
		temp = a.gen.randomMuteJati(temp);
		
		phrase[iter+1+i] = temp;
		
	};
	
	phrase;
}
)


~fillOutWord= {|aKonaWord|
	var mult; 
	var jatis;
	
	mult = aKonaWord.speed/(1/aKonaWord.gati);
	while({mult%1!=0}, {
		mult = mult*2;
	});
	jatis = mult*aKonaWord.jatis;
	Post << "jatis: " <<  jatis << "\n"; 
	
	if(jatis>9) {
		a.gen.partsToWords(a.gen.randomPartition(jatis,notSize:true), aKonaWord.karve/mult, false);
	} {		
		"hi".postln;
		KonaWord.new(jatis, aKonaWord.gati, aKonaWord.karve/mult, a)
	};
}

(
~fillOut = {|aKonaItem, aOnlyUneven=false|
	var ret;
	var mult;
	var jatis;
	var action;
	
	if(aKonaItem.class==KonaWord) {
		mult = aKonaItem.speed/(1/aKonaItem.gati);
		while({mult%1!=0}, {
			mult = mult*2;
		});
		Post << "mult: " <<  mult << "\n"; 
		
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
			Post << "jatis: " <<  jatis << "\n"; 

			if(mult>9) {
				ret = a.gen.partsToWords(a.gen.randomPartition(jatis,notSize:true), aKonaItem.karve/mult, false);
			} {		
				ret = KonaWord.new(jatis, aKonaItem.gati, aKonaItem.karve/mult, a)
			};
			
		} {
			ret = aKonaItem.deepCopy;
		};		
	} {
		"hi".postln;
		ret = KonaTime.new(a);
		aKonaItem.do { |item, i|
			ret.add(~fillOut.(item, aOnlyUneven))
		};
	};
	
	ret
}

)

(
~makePostMora={|aKonaItem|
	var phrase;
	var sectionMatras;
	var i;
	var index;
	var hitMatras;
	var makeupMatras;
	var hit;
	var makeup;
	var ret;

	
	if(aKonaItem.class==KonaTime) {
		phrase = aKonaItem.deepCopy;
	} {
		phrase = KonaTime.newFrom([aKonaItem],a);
	};
	sectionMatras = 0;
	i = 0;
	hitMatras = aKonaItem.gati;
	//what if first item is < 2 matras
	while({sectionMatras<hitMatras}, {
		sectionMatras =  sectionMatras + phrase[i].matras;
		i = i + 1;	
	});
	
	makeupMatras = sectionMatras - hitMatras;
	Post << "makeupMatras: " <<  makeupMatras << "\n"; 
	
	hit = KonaWord.new(1, phrase.gati, hitMatras, a);
	if(makeupMatras!=0) {
		if(phrase.size==1) {
			index = 0
		} {
			index = i-1;
		};
		makeup = a.gen.vSarvaPhrase(makeupMatras)
	};
	
	ret = KonaTime.newFrom([hit], a);	
	if(makeup!=nil) {ret.add(makeup)};
	if(phrase.size>1) {
		ret.addAll(phrase[i..phrase.size-1]);
	};
	
	ret;
}
)