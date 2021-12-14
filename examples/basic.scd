// start the server
s.boot

/*
argSynth arguments
nil             // text printing
\konaHit        // konnakol samples
\MIDITranscribe // MIDI using only two notes: 48 for the first syllable of each word, 52 for the others
\MIDIPlay       // MIDI using random notes from 36 - 55
*/


// Example 1, audio playback
// ----------------------------------------------------------------------------

// create a piece at 155 bpm, in Adi Thala, with a gati of 4
a = KonaTani.new(155, ["I4","O","O"], 4, argSynth:\konaHit);

// generate a phrase
x = a.gen.vSarvaPhraseAuto

// play it
x.play;

// function to alter the density of syllables in the phrase
z = {a.gen.randomDensityJati(x)}

// generate and play one
z.().play

// generate a whole composition
a.add(a.gen.basicStructure)

// play it
a.play;

// it may be long, you may want to stop it
a.stop;


// example 2, MIDI for transcription
// ----------------------------------------------------------------------------
b = KonaTani.new(84, #["U1", "R", "W", "W", "R"], 4, argSynth:\MIDITranscribe);
// init MIDI and set the midi out device
MIDIClient.init;
b.midiOut = MIDIOut.newByName("IAC", "Bus 1");

// generate a phrase
~vikkuMisraTala = KonaTime.new(b);
~vikkuMisraTala.add(KonaWord.new(2,4,1,b));		//	Ta	ka
~vikkuMisraTala.add(KonaWord.new(1,4,2,b));		//	Dim
~vikkuMisraTala.add(KonaWord.new(2,4,1,b));		//	Ta	ka
~vikkuMisraTala.add(KonaWord.new(2,4,2,b));		//	Dim na

// mutate and play it
b.gen.mutatePhrase(~vikkuMisraTala,1).play
