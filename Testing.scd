s.boot


a = KonaTani.new(155, ["I4","O","O"], 4, argSynth:\MIDIPlay);

x = a.gen.vSarvaPhraseAuto

x.play
z = {a.gen.randomDensityJati(x)}

play
q = Routine {
       inf.do {
               z.().rout.embedInStream;
               z.reset;
       };
};

b = TempoClock.new(155/60)

q.play(b)
q.stop
4

a.add(a.gen.basicStructure)
a.gen.basicStructure.
a.play    
a.stop

x = a.gen.allPartitions(16)
x[..]

x[(x.size/2).asInteger..((x.size/2)+1).asInteger]
a = KonaTani.new(84, #["U1", "R", "W", "W", "R"], 4, argSynth:\MIDITranscribe);

~vikkuMisraTala = KonaTime.new(a);

~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	ka
~vikkuMisraTala.add(KonaWord.new(1,4,2,a));		//	Dim
~vikkuMisraTala.add(KonaWord.new(2,4,1,a));		//	Ta	ka
~vikkuMisraTala.add(KonaWord.new(2,4,2,a));		//	Dim na


a.gen.mutatePhrase(~vikkuMisraTala,1).play


a
.class.findRespondingMethodFor(\ar).argNames

Onsets.class.findRespondingMethodFor(${1:ar}).argNames;

Onsets.class.findRespondingMethodFor(\kr).argNames;

Phasor.class.findRespondingMethodFor(\ar).argNames;
