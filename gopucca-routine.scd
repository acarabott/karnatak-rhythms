//Gopucca. 	Kanakku rhythmic pattern
	//startDur  	Duration of longest pattern in gatis
	gopucca {|startDur|
		var ret;
		ret = Routine.new({});
		//For each set of durations, create and concatonate a routine with 1 less subdivision
		startDur.do({|i|
			ret = ret ++ this.play(((startDur-i)*0.25))		});
		//Add Tam full stop to the end of the phrase
		ret = ret ++Routine.new({ Synth(\konnaHit, [\bufnum, tam.bufnum, \numFrames, tam.numFrames, \sampleRate, tam.sampleRate]); 0.25.wait})
		^ret
	}