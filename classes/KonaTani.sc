/* =========================================================================== */
/* = KonaTani Class - Class to contain and playback whole pieces of music	 = */
/* =========================================================================== */

KonaTani {
	classvar allTalas;	//	A set of the common talas
	
	var <laya;			//	Tempo
	var <tala;			//	Beats in cycle
	var <talaSym;		//	The tala in symbols
	var <gati;			//	'Default' sub-divisions per beats
	var <otherGatis;	//	Sub-divisions to change to
	var <gen;			//	Material generator
	var <store;			//	Whole solo stored in a KonaTime

	//Playback variables
	var <s;				//	Server
	var <konaSynth;		//	Synth to use
	var <clock;			//	Playback Tempo Clock
	var <fftBuffArray;	//	Array of buffers for PV_PlayBuf FFT
	var <fftRout;		//	Routine to cycle through FFT buffers
	var <fftBuff;		//	Next FFT buffer to use
	var <syls;			//	Array of syllables for opening analysis file/directory
	var <buffers;		//	Buffers for scpv files
	var <talaRout;		//	Routine for tala clapping
	var <pRout;			//	Routine for playback;
	var <>mOut;			//	MIDIOut


	*initClass {
		allTalas = [
						["I4","O","O"],					//	Adi Tala
						["U","O"],						//	Rupaka Tala
						["U1", "R", "W", "W", "R"],		//	Khanda Capu
						["W", "W", "R", "U", "U"]		//	Misra Capu
					];
	}

	*new {|argLaya=60, argTala=#["I4","O","O"], argGati=4, argOtherGatis=#[3], argSynth=\konaHit|

		^super.new.konaTaniInit(argLaya, argTala, argGati, argOtherGatis, argSynth);
	}
	
	*rand {|argLaya, argTala, argGati, argOtherGatis, argSynth=\konaHit|
		
		var laya = argLaya ?? {rrand(60,140)};
		var tala = argTala ?? {allTalas.choose};
		var gati = argGati ?? {[4,3,5,7,9].choose};
		var otherGatis = argOtherGatis ?? {[4,3,5,7,9].select({|item, i| item!=gati })};
		
		^super.new.konaTaniInit(laya, tala, gati, otherGatis, argSynth);
		
	}
	
	konaTaniInit {|argLaya, argTala, argGati, argOtherGatis, argSynth|
		var oneCycleDur;

		laya = argLaya;
		talaSym = argTala;
		tala = this.setTala(talaSym);
		gati = argGati;
		otherGatis = argOtherGatis;

		gen = KonaGenerator.new(this, laya, tala, gati);

		oneCycleDur = tala.sum*(60/laya);			//Duration of one cycle

		store = KonaTime.new(this);

		//Playback Variables
		konaSynth = argSynth;
		this.setPlayback;

		//Setup tala and clapping Routine
		this.makeTalaRout();

		//Load SynthDefs
		this.setSynthDefs;
	}

	//Convert the tala from symbols to numbers 
	setTala {|aTala|
		var ret = List[];
		
		if(aTala.every { |item, i| item.class==Integer}) {
			^aTala;
		} {
			aTala.do { |item, i|
				switch (item[0].asSymbol)
					{'I'}	{ret.add(item[1].digit)}
					{'O'}	{ret.add(2)}
					{'U'}	{
								switch ((item[1]!=nil).and({item[1].digit}) )
									{1}		{ret.add(0.5)}
									{false}	{ret.add(1)};
							}
					{'W'}	{ret.add(0.5)}
					{'R'}	{ret.add(0.5)};
			};	
		};
		
		^ret.asArray;
		
	}
	
	//Setup playback variables
	setPlayback {
		s = Server.default;
		clock = TempoClock.new(laya/60);
		//Array of buffers for FFT
		fftBuffArray = Array.fill(10, {Buffer.alloc(s, 1024)});

		//Syllables Array
		syls = ['Tam', 'Ta', 'Ka', 'Ki', 'Tah', 'Di', 'Mi', 'Da', 'Gi', 'Na',
				'Dom',	'-', 'Ju', 'Lan', 'Gu', 'Tom', 'Nam', 'Ri', 'Du', 'Din'];

		//Buffers for PV analysis files
		buffers=Array.newClear(syls.size);
		buffers.size.do({|i| buffers[i] = Buffer.read(s, Platform.userExtensionDir +/+ "karnatak-rhythms/samples/"++syls[i]++".wav.scpv")});

		//FFT Buffers and Routine
		fftBuff=fftBuffArray[0];
		fftRout = Routine.new({
			inf.do({|i|
				fftBuff = fftBuffArray.wrapAt(i);
				0.yield;
			})
		});

		//MIDI
		// MIDIClient.init(1,1);
		// mOut = MIDIOut.newByName("IAC Driver", "Bus 1");

	}

	//Setup SynthDefs
	setSynthDefs {
		//Default SynthDef
		SynthDef(\konaHit, { arg out=0, bufnum=0, recBuf=1, rate=1, amp=0.8;
			var chain, signal;
			chain = PV_PlayBuf(bufnum, recBuf, rate, 0, 0);
			signal = IFFT(chain, 1)*amp;
			DetectSilence.ar(signal, doneAction:2);
			Out.ar(out, signal.dup);
		}).load(s);

		//	Clapping SynthDef by Thor Magnusson
		SynthDef(\clapping, {arg t_trig=1, amp=0.5, filterfreq=100, rq=0.1;
			var env, signal, attack,  noise, hpf1, hpf2;
			noise = WhiteNoise.ar(1)+SinOsc.ar([filterfreq/2,filterfreq/2+4 ], pi*0.5, XLine.kr(1,0.01,4));
			hpf1 = RLPF.ar(noise, filterfreq, rq);
			hpf2 = RHPF.ar(noise, filterfreq/2, rq/4);
			env = EnvGen.kr(Env.perc(0.003, 0.00035));
			signal = (hpf1+hpf2) * env;
			signal = CombC.ar(signal, 0.5, 0.03, 0.031)+CombC.ar(signal, 0.5, 0.03016, 0.06);
			//signal = FreeVerb.ar(signal, 0.23, 0.15, 0.2);
			signal = Limiter.ar(signal, 0.7, 0.01);
			Out.ar(0, Pan2.ar(signal*amp, 0));
			DetectSilence.ar(signal, doneAction:2);
		}).load(s)
	}

	//Playback the contained piece of music with the tala cycle
	play {
			talaRout.reset;
			pRout.reset;
			talaRout.play(clock);
		{
			((60/laya)*(tala.sum)).wait;
			pRout.play(clock);
		}.fork
	}
	
	//Stop routine playback
	stop {
		//this.clock.stop;
		talaRout.stop;
		pRout.stop;

	}

	//Generate the clapping routine for the Tala
	makeTalaRout {
		var func = {|amp1, amp2, freq1, freq2, rq| s.bind {Synth(\clapping, [\amp, rrand(amp1, amp2), \filterfreq, rrand(freq1, freq2), \rq, rq.rand]) }};
	
		talaRout = Routine {
			inf.do {
				talaSym.do { |item, i|
					switch (item[0].asSymbol)
						{'I'}	{	
									//Clap
									func.(0.4, 0.5, 2000, 2500, 0.9);
									1.wait;
									//Finger Taps
									(item[1].digit-1).do { |j|
									func.(0.01, 0.05, 6000, 7000, 0.9);
									1.wait;
								};
								}
						{'O'}	{	
									//Clap
									func.(0.4, 0.5, 2000, 2500, 0.9);
									1.wait;
									//Back of hand / wave
									func.(0.01, 0.03, 400, 600, 0.9);
									1.wait;
								}
						{'U'}	{		
									//Clap
									func.(0.4, 0.5, 2000, 2500, 0.9);
									switch ((item[1]!=nil).and({item[1].digit}) )
										{1}		{0.5.wait}
										{false}	{1.wait};
								}
						{'W'}	{
									//Wave
									func.(0.3, 0.4, 400, 600, 0.9);
									0.5.wait;
								}
						{'R'}	{
									//Rest
									0.5.wait;
								};
				};
			};
		};
				
	}

	//Getter for the music routine
	rout {
		^store.rout;
	}

	//Add an item to this instance's KonaTime
	add {|aKonaItem|
		store.add(aKonaItem);
		pRout = store.rout;
	}

	//Add a collection of items to this instance's KonaTime
	addAll { |argCollection|
		store.addAll(argCollection);
		pRout = store.rout;
	}
	
	//Clear this instance's KonaTime
	clear {
		store = KonaTime.new(this);
		pRout = store.rout;
	}
	
	//Setter method for the gati
	gati_{|aGati|
		gati = aGati;
		gen.gati = aGati;
	}
}
