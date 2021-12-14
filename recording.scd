s.boot
(
SynthDef(\takaRec, {|bufnum|
	var rec;
	rec =RecordBuf.ar(SoundIn.ar(0), bufnum, loop:0);
	FreeSelfWhenDone.kr(rec);
}).load(s)
)


~syls = Array.with("Tam", "Ta", "Ka", "Ki", "Tah", "Di", "Mi", "Da", "Gi", "Na", "Dom", "-", "Ju", "Lan", "Gu", "Tom", "Nam", "Ri", "Du", "Din");

b = {Buffer.alloc(s, (s.sampleRate/2))}

~bufs = Array.newClear(~syls.size);

~syls.size.do({|i|
	~bufs[i] = b.();
})

r=Routine.new({
	~syls.size.do({|i|
		Synth(\takaRec, [\bufnum, ~bufs[i]]);
		~syls[i+1].postln;
		0.yield
	})
})

~syls.size.do({|i|
	~bufs[i].write(Platform.userExtensionDir +/+ "karnatak-rhythms/samples/"++~syls[i]++".wav", "WAV", "int16");
})

b = {Buffer.alloc(s, (48000/2))}

~bufs = Array.newClear(~syls.size);

~syls.size.do({|i|
	~bufs[i] = b.();
})
~bufs.size.do({|i|
	~bufs[i] = Buffer.read(s, Platform.userExtensionDir +/+ "karnatak-rhythms/samples/"++~syls[i]++".wav")
})

SynthDef(\takaPlay, { arg out=0,bufnum=0;
	Out.ar(out,
		PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), doneAction:2)
	)
}).load(s)

r = Routine.new({
	~syls.size.do({|i|
		Synth(\takaPlay, [\bufnum, ~bufs[i].bufnum]);
		0.25.wait;
	});
});
r.play
r.reset
r.next
