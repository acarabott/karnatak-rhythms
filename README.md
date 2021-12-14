# Computer Representation and Generation of Karnatak Rhythms

[SuperCollider](https://supercollider.github.io/) Code from my 2009 [undergraduate dissertation](https://www.arthurcarabott.com/assets/projects/konnakkol/dissertation.pdf) _Computer Representation and Generation of
Karnatak Rhythms_.

## Installation

Clone this repository, or copy the folder to your SuperCollider application support directory (e.g. `~/Library/Application Support/SuperCollider/Extensions/` on mac). Make sure it is named `karnatak-rhythms`.

### Dependencies

Requires the PV classes from [sc3-plugins](https://supercollider.github.io/sc3-plugins/), install these as per instructions.

### Partitions >= 40

If you want to use the partitioning algorithm for integers >= 40, you can use the precomputed values in `partitions.zip`, just unzip it.

## Examples

See `example.scd` for example usage with samples and MIDI

## Samples

All samples have phase vocoder analysis pre-computed into `.scpv` files. The code to do this is provided in `phase-vocoder-analysis.scd`


## Thanks

- [Nick Collins](https://composerprogrammer.com/), my supervisor.
- Ezio Taeggi for inspiring me to clean this up (slightly) and put it online
