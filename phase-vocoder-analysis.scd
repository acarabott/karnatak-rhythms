// This file is used to do phase vocoder analysis of all audio files and store the result in matching .scpv files

(
	SynthDef("pvrec", { arg bufnum=0, recBuf=1, soundBufnum=2;
		var in, chain;
		Line.kr(1, 1, BufDur.kr(soundBufnum), doneAction: 2);
		in = PlayBuf.ar(1, soundBufnum, BufRateScale.kr(soundBufnum), loop: 0);
		// note the window type and overlaps... this is important for resynth parameters
		chain = FFT(bufnum, in, 0.25, 1); 
		chain = PV_RecordBuf(chain, recBuf, 0, 1, 0, 0.25, 1);
		// no ouput ... simply save the analysis to recBuf
		}).send(s);

	SynthDef("pvplay", { arg out=0, bufnum=0, recBuf=1;
		var in, chain;
		chain = PV_PlayBuf(bufnum, recBuf,1, 0, 0);
		Out.ar(out, IFFT(chain, 1).dup);
		}).send(s);

);

s.boot
(

~syls = Array.with("Tam", "Ta", "Ka", "Ki", "Tah", "Di", "Mi", "Da", "Gi", "Na", "Dom", "-", "Ju", "Lan", "Gu", "Tom", "Nam", "Ri", "Du", "Din");
p = Array.newClear(~syls.size);
// path to a sound file here
p.size.do({|i| p[i]= Platform.userExtensionDir +/+ "karnatak-rhythms/samples/"++~syls[i]++".wav"});
// the frame size for the analysis - experiment with other sizes (powers of 2)
f = 1024; 
// the hop size
h = 0.25;

~sf = Array.newClear(~syls.size);
x = Array.fill(~syls.size, {Buffer.alloc(s, f)});
y = Array.newClear(~syls.size);
z = Array.newClear(~syls.size);
~bufs = Array.newClear(~syls.size);

{
~syls.size.do { |i|
	~sf[i] = SoundFile.new(p[i]);
	s.sync;
	~sf[i].openRead;
	s.sync;
	~sf[i].close;
	s.sync;
	y[i] = Buffer.alloc(s, ~sf[i].duration.calcPVRecSize(f, h));
	s.sync;
}}.fork
)
(
{	
~syls.size.do { |i|
	z[i] = Buffer.read(s, p[i]);
};	
{
~syls.size.do { |i|
	Synth("pvrec", [\bufnum, x[i], \recBuf, y[i], \soundBufnum, z[i]]);
	2.wait;
	i.postln;
}
}.fork
~syls.size.do { |i|
	y[i].write(p[i]++".scpv", "wav", "float32");
}

~syls.size.do { |i|
	~bufs[i] = Buffer.read(s, p[i]++".scpv");
}
